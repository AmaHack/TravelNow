package com.amadeus.social.media.servlets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amadeus.social.media.framework.json.JSONObject;
import com.amadeus.social.media.interfaces.WebConnectorInterface;
import com.amadeus.social.media.tools.EPowerCalendarSearchConnector;

/**
 * process input pararmeters and return availability based in that
 * @author ssinha
 *
 */
public class GetBookAvailability extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		// create input
		JSONObject input = new JSONObject();
		input.put("org", request.getParameter("org"));
		input.put("dest", request.getParameter("dest"));
		
		WebConnectorInterface connector = new EPowerCalendarSearchConnector();
		JSONObject data = connector.getServiceResponse(input);
		
		try {
			this.sendResponseData(response, data.toString());
		} catch (IOException e) {
			// do nothing
		}
	}
	
	private void sendResponseData(HttpServletResponse response, String data) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		writer.write(data);
		writer.flush();
		writer.close();
	}
}
