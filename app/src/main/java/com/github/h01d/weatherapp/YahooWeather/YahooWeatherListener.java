package com.github.h01d.weatherapp.YahooWeather;

/**
 * This is a part of WeatherApp Project (https://github.com/h01d/WeatherApp)
 * Licensed under Apache License 2.0.
 *
 * YahooWeatherListener interface handles the response or error from API.
 *
 * @author Raf (https://github.com/h01d)
 * @version 1.0
 * @since 01/08/2018
 */

public interface YahooWeatherListener
{
    void getResult(Weather weather);

    void getError(String error);
}
