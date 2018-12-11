package com.github.h01d.weatherapp.YahooWeather;

import java.util.ArrayList;

/**
 * This is a part of WeatherApp Project (https://github.com/h01d/WeatherApp)
 * Licensed under Apache License 2.0.
 *
 * Weather class is a model for our requests.
 *
 * @author Raf (https://github.com/h01d)
 * @version 1.0
 * @since 01/08/2018
 */

public class Weather
{
    private String lastBuild, locationCity, locationCountry, conditionCode, conditionTemp, conditionText;
    private double windSpeed, atmosphereHumidity;
    private ArrayList<String> forecastCodes, forecastDays, forecastHigh, forecastLow;

    Weather()
    {

    }

    /**
     * getWeatherCode method returns the code of the condition to be used
     * in the UI of the app
     *
     * @return the code of condition
     */

    public int getWeatherCode()
    {
        switch(Integer.parseInt(conditionCode))
        {
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 44:
                return 1; //clouds

            case 0:
            case 2:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
                return 2; //broken

            case 1:
            case 3:
            case 4:
            case 9:
            case 10:
            case 11:
            case 12:
            case 35:
            case 37:
            case 38:
            case 39:
            case 40:
            case 45:
            case 47:
                return 3; //rain

            case 8:
            case 25:
                return 4; //freeze

            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 41:
            case 42:
            case 43:
            case 46:
                return 5; //snow

            default:
                return 0; //clear
        }
    }

    public String getLastBuild()
    {
        return lastBuild;
    }

    public void setLastBuild(String lastBuild)
    {
        this.lastBuild = lastBuild;
    }

    public String getLocationCity()
    {
        return locationCity;
    }

    public void setLocationCity(String locationCity)
    {
        this.locationCity = locationCity;
    }

    public String getLocationCountry()
    {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry)
    {
        this.locationCountry = locationCountry;
    }

    public void setConditionCode(String conditionCode)
    {
        this.conditionCode = conditionCode;
    }

    public String getConditionTemp()
    {
        return conditionTemp;
    }

    public void setConditionTemp(String conditionTemp)
    {
        this.conditionTemp = conditionTemp;
    }

    public String getConditionText()
    {
        return conditionText;
    }

    public void setConditionText(String conditionText)
    {
        this.conditionText = conditionText;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public double getAtmosphereHumidity()
    {
        return atmosphereHumidity;
    }

    public void setAtmosphereHumidity(double atmosphereHumidity)
    {
        this.atmosphereHumidity = atmosphereHumidity;
    }

    public ArrayList<String> getForecastCodes()
    {
        return forecastCodes;
    }

    public void setForecastCodes(ArrayList<String> forecastCodes)
    {
        this.forecastCodes = forecastCodes;
    }

    public ArrayList<String> getForecastDays()
    {
        return forecastDays;
    }

    public void setForecastDays(ArrayList<String> forecastDays)
    {
        this.forecastDays = forecastDays;
    }

    public ArrayList<String> getForecastHigh()
    {
        return forecastHigh;
    }

    public void setForecastHigh(ArrayList<String> forecastHigh)
    {
        this.forecastHigh = forecastHigh;
    }

    public ArrayList<String> getForecastLow()
    {
        return forecastLow;
    }

    public void setForecastLow(ArrayList<String> forecastLow)
    {
        this.forecastLow = forecastLow;
    }
}
