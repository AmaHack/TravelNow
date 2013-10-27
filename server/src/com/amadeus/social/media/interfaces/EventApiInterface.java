package com.amadeus.social.media.interfaces;

import java.io.IOException;
import java.net.MalformedURLException;

import com.amadeus.social.media.framework.json.JSONObject;

/**
 * this defines the contract between client and server to access Event api
 * @author ssinha
 *
 */
public interface EventApiInterface {
	
	/**
	 * this method will hit the event apis based on provided input and will return the {@link JSONObject} as output
	 * @param input {@link JSONObject} inputs params
	 * @return {@link JSONObject}
	 * @throws MalformedURLException, {@link IOException}
	 */
	public JSONObject getEventJSON(JSONObject input) throws MalformedURLException, IOException;
	
	/**
	 * returns the base URL of API used for getting events
	 * @return {@link String}
	 */
	public String getEventApiURL();
}
