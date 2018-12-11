package com.github.h01d.weatherapp.YahooWeather;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.h01d.weatherapp.R;

import java.util.ArrayList;

/**
 * This is a part of WeatherApp Project (https://github.com/h01d/WeatherApp)
 * Licensed under Apache License 2.0.
 *
 * ForecastAdapter class handles RecycleViewAdapter to display
 * forecast based on data we extract from request.
 *
 * @author Raf (https://github.com/h01d)
 * @version 1.0
 * @since 01/08/2018
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>
{
    private ArrayList<String> forecastDays, forecastHigh, forecastLow, forecastCodes;
    private Context context;

    /**
     * ViewHolder class handles the UI for RecycleViewAdapter
     */

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private Context context;

        private TextView dayTextView, highTextView, lowTextView, conditionTextview;

        /**
         * ViewHolder constructor assigns values to our attributes
         *
         * @param context  the application context
         * @param itemView the layout view
         */

        private ViewHolder(Context context, View itemView)
        {
            super(itemView);

            this.context = context;

            dayTextView = itemView.findViewById(R.id.l_card_day);
            highTextView = itemView.findViewById(R.id.l_card_high);
            lowTextView = itemView.findViewById(R.id.l_card_low);
            conditionTextview = itemView.findViewById(R.id.l_card_condittion);
            conditionTextview.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/weather.ttf"));
        }

        /**
         * setData method assigns values on the TextViews of the card
         *
         * @param day
         * @param high      temperature of the day
         * @param low       temperature of the day
         * @param condition code of the day
         */

        private void updateUI(String day, String high, String low, String condition)
        {
            dayTextView.setText(day);
            highTextView.setText(high + "°");
            lowTextView.setText(low + "°");

            // Set the icon based on condition code

            conditionTextview.setText(context.getResources().getIdentifier("wi_yahoo_" + condition, "string", context.getPackageName()));
        }
    }

    /**
     * ForecastAdapter constructor will assign values to our attributes
     *
     * @param context       the application context
     * @param forecastDays  the array with the days
     * @param forecastHigh  the array with high temperatures
     * @param forecastLow   the array with low temperatures
     * @param forecastCodes the array with condition codes
     */

    public ForecastAdapter(Context context, ArrayList<String> forecastDays, ArrayList<String> forecastHigh, ArrayList<String> forecastLow, ArrayList<String> forecastCodes)
    {
        this.context = context;

        this.forecastDays = forecastDays;
        this.forecastHigh = forecastHigh;
        this.forecastLow = forecastLow;
        this.forecastCodes = forecastCodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_card, parent, false);

        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.updateUI(forecastDays.get(position), forecastHigh.get(position), forecastLow.get(position), forecastCodes.get(position));
    }

    @Override
    public int getItemCount()
    {
        return forecastDays.size();
    }
}
