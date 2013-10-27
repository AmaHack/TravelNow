package com.amadeus.social.media.servlets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amadeus.social.media.framework.json.JSONArray;
import com.amadeus.social.media.framework.json.JSONObject;
import com.amadeus.social.media.interfaces.EventApiInterface;
import com.amadeus.social.media.file.CityNameUtility;
import com.amadeus.social.media.tools.EventfulApiHandler;

/**
 * this servlet provides a interface to user to access event API and get cities with top events
 * @author ssinha
 *
 */
public class GetEventsInCity extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		// error json array
		JSONArray errors = null;
		
		// to access eventful API
		EventApiInterface eventApi = new EventfulApiHandler();
		
		// initialize a HashMap with default capacity and load factor
		Map<String, JSONObject> cityMap = new HashMap<String, JSONObject>();
		
		try {
			
			int maxNumberOfEvents = 5;
			String cityName = request.getParameter("city_name");
			
			if (cityName == null) {
				JSONObject json = new JSONObject();
				json.put("server_msg", "Please provide a valid city name");
				json.put("msg_type", "E");
				json.put("msg_number", "1011");
				
				if (errors == null) {
					errors = new JSONArray();
				}
				
				errors.put(json);
			}
			
			try {
				maxNumberOfEvents = Integer.parseInt(request.getParameter("max_events"));
			} catch (NumberFormatException e) {
				// no action
			} catch (NullPointerException e) {
				// no action
			}
			
			if (errors == null || errors.length() == 0) {
					
				// create input params for API
				JSONObject input = new JSONObject();
				for (Object key: request.getParameterMap().keySet()) {
					// if request parameter is String
					if (key instanceof String 
							&& !key.equals("max_events")
							&& !key.equals("app_key")
							&& !key.equals("page_size")
							&& !key.equals("city_name")
							&& !key.equals("keywords")) {
						if (request.getParameterMap().get(key) instanceof String) {
							input.put((String)key, request.getParameterMap().get(key));
						} else if (request.getParameterMap().get(key) instanceof Collection<?> 
									&& ((Collection<?>)request.getParameterMap().get(key)).size() > 0) {
							// get 0th element of the collection
							input.put((String)key, ((Collection<?>)request.getParameterMap().get(key)).toArray()[0]);
						} else if (request.getParameterMap().get(key) instanceof Object[]
									&& ((Object[])request.getParameterMap().get(key)).length > 0) {
							// get 0th element of the array
							input.put((String)key, ((Object[])request.getParameterMap().get(key))[0]);
						}
					}
				}
				
				// get data from event API
				JSONObject json = eventApi.getEventJSON(input);
				
				if (json != null && json.has("responseJSON") && json.get("responseJSON") instanceof JSONObject) {
					
					// process JSON for city and event info
					JSONObject resJSON = json.getJSONObject("responseJSON");
					if (resJSON.get("events") instanceof JSONObject 
							&& resJSON.getJSONObject("events").get("event") instanceof JSONArray) {
						JSONArray events = resJSON.getJSONObject("events").getJSONArray("event");
						for (int i =0; i < events.length(); i++) {
							if (events.get(i) instanceof JSONObject) {
								
								StringBuilder key = new StringBuilder();
								JSONObject event = events.getJSONObject(i);
								
								if (event.get("city_name") instanceof String) {
									key.append(event.getString("city_name"));
								}
								
								JSONObject filteredEventJSON = new JSONObject();
								filteredEventJSON.put("event_name", event.get("title"));
								filteredEventJSON.put("lng", event.get("longitude"));
								filteredEventJSON.put("lat", event.get("latitude"));
								filteredEventJSON.put("region_name", event.get("region_name"));
								filteredEventJSON.put("country_name", event.get("country_name"));
								
								// send description
								if (event.get("description") instanceof String) {
									filteredEventJSON.put("description", URLEncoder.encode((String)event.get("description"),"UTF-8"));
								}
								
								if (event.get("image") instanceof JSONObject 
										&& event.getJSONObject("image").get("medium") instanceof JSONObject 
										&& event.getJSONObject("image").getJSONObject("medium").get("url") instanceof String) {
									filteredEventJSON.put("image", event.getJSONObject("image").getJSONObject("medium").get("url"));
								}
								
								// if city in events is same as current city
								if (key.toString().equalsIgnoreCase(cityName)) {
										
									// add to tree map
									if (cityMap.containsKey(key.toString())) {
										
										// if already our map contains maximum number of events
										// (which is provided in parameter) then don't add to array 
										if (cityMap.get(key.toString()).length() < maxNumberOfEvents) {
											JSONObject cityJson = cityMap.get(key.toString());
											cityJson.getJSONArray("events").put(filteredEventJSON);
										}
									} else {
										
										JSONObject cityJson = new JSONObject();
										cityJson.put("events", new JSONArray().put(filteredEventJSON));
										
										// add to Map
										cityMap.put(key.toString(), cityJson);
									}
									
								}
							}
						}
					}
					
					for (String city: cityMap.keySet()) {
						JSONObject cityJson = cityMap.get(city);
						JSONArray events  = cityJson.getJSONArray("events");
						if (events.get(0) instanceof JSONObject) {
							JSONObject event = events.getJSONObject(0);
							JSONObject airport = CityNameUtility.getCurrentCity(Double.parseDouble(event.getString("lat")), Double.parseDouble(event.getString("lng")));
							if (airport != null) {
								cityJson.put("airport", airport);
							}
						}
					}
					
					// write response to output stream
					JSONObject responseJson = new JSONObject();
					responseJson.put("deals", new JSONObject(cityMap));
					
					this.sendResponseData(response, responseJson.toString());
				} else {
					
					JSONObject error = new JSONObject();
					error.put("server_msg", "Sorry we are not able to find any events for provided city");
					error.put("msg_type", "E");
					error.put("msg_number", "1012");
					
					if (errors == null) {
						errors = new JSONArray();
					}
					
					errors.put(error);
					
					// write response to output stream
					this.sendResponseData(response, new JSONObject().put("messages", errors).toString());
				}
				
			} else {
				// write response to output stream
				this.sendResponseData(response, new JSONObject().put("messages", errors).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendResponseData(HttpServletResponse response, String data) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		writer.write(data);
		writer.flush();
		writer.close();
	}
}
