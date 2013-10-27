package com.amadeus.social.media.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.amadeus.social.media.framework.json.JSONException;
import com.amadeus.social.media.framework.json.JSONObject;
import com.amadeus.social.media.interfaces.EventApiInterface;

/**
 * implementation of {@link EventApiInterface}
 * @author ssinha
 *
 */
public class EventfulApiHandler implements EventApiInterface {
	
	private String eventApiURL;
	
	public EventfulApiHandler() {
		this.eventApiURL = "http://api.eventful.com/json/events/search?app_key=8FNgK29dwTr5KQ7m&page_size=100";
	}
	
	@Override
	public JSONObject getEventJSON(JSONObject input) throws MalformedURLException, IOException {
		JSONObject response = new JSONObject();
		
		// create a URL for establishing connection
		StringBuilder urlParams = new StringBuilder();
		for (Object obj: input.keySet()) {
			// if a string is passed as both key and value then only proceed
			// these key and values will be used to create URL parameters which can only be String
			if (obj instanceof String 
					&& input.get((String)obj) instanceof String) {

				urlParams.append("&");
				urlParams.append(URLEncoder.encode((String)obj, "UTF-8"));
				urlParams.append("=");
				urlParams.append(URLEncoder.encode(input.getString((String)obj), "UTF-8"));
			}
		}
		
		URL url = new URL(this.getEventApiURL() + urlParams.toString());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setReadTimeout(600000);
		connection.setConnectTimeout(600000);
		connection.connect();
		
		int status = connection.getResponseCode();
		switch (status) {
			case 200:
			case 201:
				// read the input stream if connection response is SUCCESS
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder responseData = new StringBuilder();
				
				// read all the contents
				String line = br.readLine();
				while (line != null) {
					responseData.append(line);
					line = br.readLine();
				}
				
				// convert string to JSON
				try {
					response.put("responseJSON", new JSONObject(responseData.toString()));
				} catch (JSONException e) {
					response.put("responseTEXT", responseData.toString());
				}
				
				break;
			default:
				break;
		}
		
		return response;
	}

	@Override
	public String getEventApiURL() {
		return this.eventApiURL;
	}

}
