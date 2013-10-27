package com.amadeus.social.media.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amadeus.social.media.framework.json.JSONArray;
import com.amadeus.social.media.framework.json.JSONObject;

/**
 * City Information is parsed from a file. 
 * File source is "Select c2.cityCode, c1.cityName, c2.ProvinceCode, c2.ProvinceCountryCode, c2.countryCode, c2.Latitude, c2.Longitude from TP_CITYNAME C1, TP_CITY C2 where C1.LANGUAGECODE='GB' and C1.CITYCODE = C2.CITYCODE"
 * select
 * @author ssinha
 *
 */
public abstract class CityNameUtility {
	
	private static JSONArray cities;
	
	/**
	 * get list all cities
	 * @param update if set to true then file is parsed again otherwise already parsed data is returned
	 * @return {@link JSONArray}
	 */
	public static final JSONArray getAllCities(boolean update) {

		// if flag is set to parse file or airports list is empty
		if (update || cities == null || cities.length() == 0) {
				
			// read list of all airports from file
			// file data is taken from TP_Airport table
			InputStream is = AirportUtility.class.getClassLoader().getResourceAsStream("data/cities.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
					
			try {
					
				String line = br.readLine();
				while (line != null) {
							
					String[] tokens = line.split("\\^");
					
					if (tokens.length > 6 && !tokens[5].equalsIgnoreCase("NULL") && !tokens[6].equalsIgnoreCase("NULL")) {
					
						// create airport information
						JSONObject json = new JSONObject();
						json.put("code", tokens[0]);
						json.put("name", tokens[1]);
						json.put("lat", Double.parseDouble(tokens[5])/Math.pow(10, 5));
						json.put("lng", Double.parseDouble(tokens[6])/Math.pow(10, 5));
								
						// first time will be null
						if (cities == null) {
							cities = new JSONArray();
						}
								
						// add to list
						cities.put(json);
					}
							
					// read next line
					line = br.readLine();
				}
			} catch (IOException e) {
				// create a empty list
				cities = new JSONArray();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// no action required
				}
			}
		}
				
		return cities;
	}
	
	/**
	 * get current city based on provided latitude and longitude
	 * @param lat {@link Double} latitude
	 * @param lng {@link Double} longitude
	 * @return {@link JSONObject}
	 */
	public static final JSONObject getCurrentCity(Double lat, Double lng) {
		
		double distance = -1;
		JSONObject response = null;
		
		JSONArray cities = getAllCities(false);
		for (int i = 0; i < cities.length(); i++) {
			if (cities.get(i) instanceof JSONObject) {
				
				JSONObject city = cities.getJSONObject(i);
				
				// calculate distance
				double aptLat = city.getDouble("lat");
				double aptLng = city.getDouble("lng");
				
				// use formula distance = ((x2-x1)^2 + (y2-y1)^2)^(1/2)
				double latDiff = aptLat - lat;
				double lngDiff = aptLng - lng;
				double cityDist = Math.sqrt((latDiff * latDiff) + (lngDiff * lngDiff));
				if (distance == -1 || distance > cityDist) {
					response = city;
					distance = cityDist;
				}
			}
		}
		
		return response;
	}
}