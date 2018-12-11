package com.github.h01d.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.h01d.weatherapp.YahooWeather.ForecastAdapter;
import com.github.h01d.weatherapp.YahooWeather.Weather;
import com.github.h01d.weatherapp.YahooWeather.YahooWeather;
import com.github.h01d.weatherapp.YahooWeather.YahooWeatherListener;

/**
 * This is a part of WeatherApp Project (https://github.com/h01d/WeatherApp)
 * Licensed under Apache License 2.0.
 *
 * MainActivity class handles application functionality.
 *
 * @author Raf (https://github.com/h01d)
 * @version 1.0
 * @since 01/08/2018
 */

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";

    public final int MY_PERMISSIONS_REQUEST_LOCATION = 69; // For handling permissions

    private RecyclerView recyclerView;
    private TextView degreesText, dateText, descriptionText, windText, humidityText, updateText, symbolText;
    private RelativeLayout mainRelative, forecastRelative;
    private ImageView backgroundImage;

    private YahooWeather yahooWeather;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyClear));

        this.getSupportActionBar().setElevation(0);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyClear)));

        recyclerView = findViewById(R.id.a_main_recycler);
        degreesText = findViewById(R.id.a_main_degrees);
        dateText = findViewById(R.id.a_main_date);
        descriptionText = findViewById(R.id.a_main_description);
        windText = findViewById(R.id.a_main_wind);
        humidityText = findViewById(R.id.a_main_humidity);
        updateText = findViewById(R.id.a_main_update);
        symbolText = findViewById(R.id.a_main_symbol);

        mainRelative = findViewById(R.id.a_main_relative);
        forecastRelative = findViewById(R.id.a_main_forecast_relative);

        backgroundImage = findViewById(R.id.a_main_image);

        yahooWeather = new YahooWeather(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        updateUI(yahooWeather.getOfflineWeather());

        loadLocationWeather();
    }

    /**
     * updateUI method updates UI with the new data we get from API
     *
     * @param weather object with the data
     */

    private void updateUI(Weather weather)
    {
        if(weather == null)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyClear));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyClear)));

            degreesText.setVisibility(View.INVISIBLE);
            dateText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            windText.setVisibility(View.INVISIBLE);
            humidityText.setVisibility(View.INVISIBLE);
            symbolText.setVisibility(View.INVISIBLE);

            recyclerView.setVisibility(View.INVISIBLE);

            updateText.setText("Latest Update: Never.");

            final Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Seems you haven't load any weather yet.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Close", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
        else
        {
            switch(weather.getWeatherCode())
            {
                case 1: //clouds
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyClear)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyClear));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkyClear));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainClear));
                    backgroundImage.setImageResource(R.drawable.clouds);
                    break;

                case 2: //broken
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyRain)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyRain));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkyRain));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainRain));
                    backgroundImage.setImageResource(R.drawable.broken);
                    break;

                case 3: //rain
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyRain)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyRain));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkyRain));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainRain));
                    backgroundImage.setImageResource(R.drawable.rain);
                    break;

                case 4: //freeze
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkySnow)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkySnow));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkySnow));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainSnow));
                    backgroundImage.setImageResource(R.drawable.freeze);
                    break;

                case 5: //snow
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkySnow)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkySnow));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkySnow));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainSnow));
                    backgroundImage.setImageResource(R.drawable.snow);
                    break;

                default: //clear
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.weatherSkyClear)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.weatherSkyClear));
                    mainRelative.setBackgroundColor(getResources().getColor(R.color.weatherSkyClear));
                    forecastRelative.setBackgroundColor(getResources().getColor(R.color.weatherTerrainClear));
                    backgroundImage.setImageResource(R.drawable.clear);
            }

            degreesText.setVisibility(View.VISIBLE);
            degreesText.setText(weather.getConditionTemp());

            symbolText.setVisibility(View.VISIBLE);

            dateText.setVisibility(View.VISIBLE);
            dateText.setText(weather.getLastBuild().substring(0, 11));

            descriptionText.setVisibility(View.VISIBLE);
            descriptionText.setText(weather.getConditionText());

            windText.setVisibility(View.VISIBLE);
            windText.setText("Wind: " + weather.getWindSpeed() + " km/h");

            humidityText.setVisibility(View.VISIBLE);
            humidityText.setText("Humidity: " + weather.getAtmosphereHumidity() + "%");

            updateText.setText("Latest Update: " + weather.getLastBuild());

            getSupportActionBar().setTitle(weather.getLocationCity() + ", " + weather.getLocationCountry());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            ForecastAdapter adapter = new ForecastAdapter(MainActivity.this, weather.getForecastDays(), weather.getForecastHigh(), weather.getForecastLow(), weather.getForecastCodes());
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * loadWeather method handles the API calls
     *
     * @param location the location to be searched
     */

    private void loadWeather(String location)
    {
        yahooWeather.getWeather(location, new YahooWeatherListener()
        {
            @Override
            public void getResult(Weather weather)
            {
                if(weather != null)
                {
                    updateUI(weather);

                    Toast.makeText(MainActivity.this, "Weather successfully updated.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    yahooWeather.getOfflineWeather();

                    Toast.makeText(MainActivity.this, "Failed to load Weather.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void getError(String error)
            {
                Log.d(TAG, error);

                Toast.makeText(MainActivity.this, "Failed to load Weather.\nError: " + error, Toast.LENGTH_LONG).show();

                updateUI(yahooWeather.getOfflineWeather());
            }
        });
    }

    /**
     * loadLocationWeather method handles the devices location search
     * and also handles the requesting permissions for android 6.0+
     * as well as to enable location
     */

    private void loadLocationWeather()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Force dialog in case user closed it in the past

                new AlertDialog.Builder(this).setTitle("Permissions").setMessage("Application requires to access your location. Would like to grand permissions now?").setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();

                        Toast.makeText(MainActivity.this, "Failed to grand permissions.", Toast.LENGTH_SHORT).show();

                        updateUI(yahooWeather.getOfflineWeather());

                    }
                }).create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        else
        {
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        if(location != null)
                        {
                            loadWeather("(" + location.getLatitude() + "," + location.getLongitude() + ")");
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Could not retrieve location. Please wait and try again.", Toast.LENGTH_SHORT).show();
                        }

                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras)
                    {
                    }

                    @Override
                    public void onProviderEnabled(String provider)
                    {
                    }

                    @Override
                    public void onProviderDisabled(String provider)
                    {
                    }
                });
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Location").setMessage("Location is disabled. Would you like turn it on?").setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();

                        Toast.makeText(MainActivity.this, "Failed to update Weather.", Toast.LENGTH_SHORT).show();

                        updateUI(yahooWeather.getOfflineWeather());
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.main_menu_search:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter the city:");

                View mView = MainActivity.this.getLayoutInflater().inflate(R.layout.search_dialog, null);

                final EditText tmp = mView.findViewById(R.id.l_search_text);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        final String city = tmp.getText().toString();

                        if(city.length() == 0)
                        {
                            Toast.makeText(getApplicationContext(), "You have to type something.", Toast.LENGTH_LONG).show();

                            dialogInterface.dismiss();
                        }
                        else
                        {
                            loadWeather(city);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.main_menu_location:
                loadLocationWeather();
                return true;
            case R.id.main_menu_about:
                AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(MainActivity.this);
                aboutBuilder.setTitle("WeatherApp v1.0");
                aboutBuilder.setMessage("Project is open source and released under Apache License 2.0.\n\nLibraries used: \n- google/volley (Apache License 2.0)\n- Erik Flowers weather-icons (SIL OFL 1.1)\n- Yahoo! Weather API\n\nRaf, 2018. All Rights Reserved.");
                aboutBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog aboutDialog = aboutBuilder.create();
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        loadLocationWeather();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Failed to grand permissions.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}