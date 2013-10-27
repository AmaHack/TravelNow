package com.amadeus.social.media.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amadeus.social.media.framework.json.JSONArray;
import com.amadeus.social.media.framework.json.JSONObject;

/**
 * read a file and get list of all airports
 * @author ssinha
 *
 */
public abstract class AirportUtility {
	
	private static JSONArray airports;
	
	/**
	 * get list of all airports
	 * @param update {@link Boolean} if set to true then file is parsed again, otherwise earlier parsed data is returned
	 * @return {@link JSONArray}
	 */
	public static final JSONArray getAirportList(boolean update) {
		
		// if flag is set to parse file or airports list is empty
		if (update || airports == null || airports.length() == 0) {
		
			// read list of all airports from file
			// file data is taken from TP_Airport table
			InputStream is = AirportUtility.class.getClassLoader().getResourceAsStream("data/airports.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			try {
			
				String line = br.readLine();
				while (line != null) {
					
					String[] tokens = line.split("\\^");
					
					// create airport information
					JSONObject json = new JSONObject();
					json.put("code", tokens[0]);
					if (tokens.length > 12) {
						json.put("lat", Double.parseDouble(tokens[12]));
						json.put("lng", Double.parseDouble(tokens[11]));
					}
					
					// first time will be null
					if (airports == null) {
						airports = new JSONArray();
					}
					
					// add to list
					airports.put(json);
					
					// read next line
					line = br.readLine();
				}
			} catch (IOException e) {
				// create a empty list
				airports = new JSONArray();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// no action required
				}
			}
		}
		
		return airports;
	}
	
	/**
	 * from the list of all airports it will find the nearest airport from given lat and lng
	 * @param lat {@link Double} latitude
	 * @param lng {@link Double} longitude
	 * @return {@link JSONObject} nearest airport
	 */
	public static final JSONObject getNearestAirport(double lat, double lng) {
		
		double distance = -1;
		JSONObject response = null;
		
		JSONArray airports = getAirportList(false);
		for (int  i = 0; i < airports.length(); i++) {
			if (airports.get(i) instanceof JSONObject) {
				JSONObject airport = airports.getJSONObject(i);
				
				// calculate distance
				double aptLat = airport.getDouble("lat");
				double aptLng = airport.getDouble("lng");
				
				// use formula distance = ((x2-x1)^2 + (y2-y1)^2)^(1/2)
				double latDiff = aptLat - lat;
				double lngDiff = aptLng - lng;
				double airportDist = Math.sqrt((latDiff * latDiff) + (lngDiff * lngDiff));
				if (distance == -1 || distance > airportDist) {
					response = airport;
					distance = airportDist;
				}
			}
		}
		
		return response;
	}
}
