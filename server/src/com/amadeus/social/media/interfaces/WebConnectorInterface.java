package com.amadeus.social.media.interfaces;

import com.amadeus.social.media.framework.json.JSONObject;

/**
 * defines a contract on how to access web-services
 * @author ssinha
 *
 */
public interface WebConnectorInterface {
	
	/**
	 * connects to web-service with provided input and returns the web-service response
	 * @param input {@link JSONObject} input parameters for web-service
	 * @return {@link JSONObject} web-service response
	 */
	public JSONObject getServiceResponse(JSONObject input);
}
