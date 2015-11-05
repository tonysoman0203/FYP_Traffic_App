package com.example.tonyso.TrafficApp.Interface;


import com.example.tonyso.TrafficApp.model.Weather;

import java.util.List;

public interface Rss_Listener {
	void ParsedWeatherRssInfo(List<Weather> weatherList);
}
