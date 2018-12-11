package com.github.h01d.weatherapp.YahooWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is a part of WeatherApp Project (https://github.com/h01d/WeatherApp)
 * Licensed under Apache License 2.0.
 *
 * YahooWeather class handle everything have to with
 * sending requests, retrieving and caching data.
 *
 * @author Raf (https://github.com/h01d)
 * @version 1.0
 * @since 01/08/2018
 */

public class YahooWeather
{
    private final String TAG = "YahooWeather";

    private final String URL_PREFIX = "https://query.yahooapis.com/v1/public/yql?q=";
    private final String URL_SUFFIX = "&format=json";

    private final String QUERY_PREFIX = "SELECT * FROM weather.forecast WHERE woeid in (SELECT woeid FROM geo.places WHERE text=\"";
    private final String QUERY_SUFFIX = "\") AND u='c'";

    private final String OFFLINE_PREF = "WeatherAppPrefs";
    private final String OFFLINE_KEY = "latestResponse";

    private Context context;
    private RequestQueue requestQueue;

    public YahooWeather(Context context)
    {
        this.context = context;

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * getWeather method handles the GET request to Yahoo! Weather API
     * and return the result as a Weather Object on a listener.
     *
     * @param location the location for the request, could be lat,lon or city
     * @param listener the listener to parse the data
     */

    public void getWeather(final String location, final YahooWeatherListener listener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PREFIX + QUERY_PREFIX + location + QUERY_SUFFIX + URL_SUFFIX, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, response);

                //Building our weather object and parsing it to the listener

                listener.getResult(createWeather(response));

                //Saving latest result

                try
                {
                    SharedPreferences.Editor editor = context.getSharedPreferences(OFFLINE_PREF, Context.MODE_PRIVATE).edit();
                    editor.putString(OFFLINE_KEY, response);
                    editor.commit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();

                    Log.d(TAG, "Failed to update shared preferences.");
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof TimeoutError)
                {
                    listener.getError("Request timed out.");
                }
                else if(error instanceof NoConnectionError)
                {
                    listener.getError("No internet connection.");
                }
                else if(error instanceof AuthFailureError)
                {
                    listener.getError("Authentication failed.");

                }
                else if(error instanceof ServerError)
                {
                    listener.getError("Server error " + error.networkResponse.statusCode);

                }
                else if(error instanceof NetworkError)
                {
                    listener.getError("Network error.");

                }
                else if(error instanceof ParseError)
                {
                    listener.getError("Parse error.");
                }

                if(error.getMessage() != null)
                {
                    Log.d(TAG, error.getMessage());
                }
            }
        });

        stringRequest.setShouldCache(false); //prevent caching result for LTE networks
        requestQueue.add(stringRequest);
    }

    /**
     * getOfflineWeather returns a Weather Object from the latest result
     * saved in memory.
     *
     * @return Weather Object if any result is saved otherwise null.
     */

    public Weather getOfflineWeather()
    {
        String offlineResult = context.getSharedPreferences(OFFLINE_PREF, Context.MODE_PRIVATE).getString(OFFLINE_KEY, "");

        if(offlineResult.length() == 0)
        {
            return null;
        }
        else
        {
            return createWeather(offlineResult);
        }
    }

    /**
     * createWeather method creates a Weather Object based on response we got
     * from request.
     *
     * @param response the response we got from request
     * @return Weather Object if response no problem occurs otherwise null
     */

    private Weather createWeather(String response)
    {
        Weather weather = new Weather();

        JSONObject results;

        // For some reason LAT,LON request return channel as an object, but LOCATION request return channel as an array

        try
        {
            results = new JSONObject(response).getJSONObject("query").getJSONObject("results").getJSONObject("channel");
        }
        catch(JSONException e)
        {
            try
            {
                results = new JSONObject(response).getJSONObject("query").getJSONObject("results").getJSONArray("channel").getJSONObject(0);
            }
            catch(JSONException e1)
            {
                e1.printStackTrace();

                results = null;
            }
        }

        if(results != null)
        {
            try
            {
                // Assign the result into our object

                String lastBuild = results.getString("lastBuildDate");
                weather.setLastBuild(lastBuild);

                JSONObject location = results.getJSONObject("location");
                weather.setLocationCity(location.getString("city"));
                weather.setLocationCountry(location.getString("country"));

                JSONObject wind = results.getJSONObject("wind");
                weather.setWindSpeed(wind.getDouble("speed"));

                JSONObject atmosphere = results.getJSONObject("atmosphere");
                weather.setAtmosphereHumidity(atmosphere.getDouble("humidity"));

                JSONObject condition = results.getJSONObject("item").getJSONObject("condition");
                weather.setConditionCode(condition.getString("code"));
                weather.setConditionTemp(condition.getString("temp"));
                weather.setConditionText(condition.getString("text"));

                JSONArray forecast = results.getJSONObject("item").getJSONArray("forecast");

                ArrayList<String> forecastCodes = new ArrayList<>(), forecastDays = new ArrayList<>(), forecastHigh = new ArrayList<>(), forecastLow = new ArrayList<>();

                for(int i = 0; i < forecast.length(); i++)
                {
                    JSONObject tmp = forecast.getJSONObject(i);

                    // Will make sure first day of forecast is the next day

                    try
                    {
                        if(DateUtils.isToday(new SimpleDateFormat("dd MMM yyyy", Locale.US).parse(tmp.getString("date")).getTime()))
                        {
                            continue;
                        }
                    }
                    catch(ParseException e)
                    {
                        e.printStackTrace();
                    }

                    forecastCodes.add(tmp.getString("code"));
                    forecastDays.add(tmp.getString("day"));
                    forecastHigh.add(tmp.getString("high"));
                    forecastLow.add(tmp.getString("low"));
                }

                weather.setForecastCodes(forecastCodes);
                weather.setForecastDays(forecastDays);
                weather.setForecastHigh(forecastHigh);
                weather.setForecastLow(forecastLow);
            }
            catch(JSONException e)
            {
                e.printStackTrace();

                return null;
            }
        }

        return weather;
    }
}