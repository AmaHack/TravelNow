package com.amadeus.social.media.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amadeus.social.media.framework.json.JSONObject;
import com.amadeus.social.media.interfaces.WebConnectorInterface;

/**
 * this class contains methods which helps to connect to web-service
 * @author ssinha
 *
 */
public class EPowerCalendarSearchConnector implements WebConnectorInterface {
	
	private String xmlRequest;
	
	private String serviceURL;
	
	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	
	public EPowerCalendarSearchConnector() {
		// sample XML response for doing a request to calendar search operation of EPower
		xmlRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Header><AuthenticationSoapHeader xmlns=\"http://epowerv5.amadeus.com.tr/WS\"><WSUserName>wstripconciergehk</WSUserName><WSPassword>TripConcierge@epws22</WSPassword></AuthenticationSoapHeader></soap:Header><soap:Body><SearchFlightCalendar xmlns=\"http://epowerv5.amadeus.com.tr/WS\"><OTA_AirLowFareSearchRQ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" Version=\"0\" DirectFlightsOnly=\"true\"><OriginDestinationInformation><DepartureDateTime>{0}</DepartureDateTime><OriginLocation LocationCode=\"{1}\"/><DestinationLocation LocationCode=\"{2}\"/></OriginDestinationInformation><TravelerInfoSummary><AirTravelerAvail><PassengerTypeQuantity Code=\"ADT\"/></AirTravelerAvail></TravelerInfoSummary></OTA_AirLowFareSearchRQ></SearchFlightCalendar></soap:Body></soap:Envelope>";
		
		// web-service URL
		this.setServiceURL("http://staging.ws.amadeusepower.com/wstripconciergehk/EpowerService.asmx");
	}

	@Override
	public JSONObject getServiceResponse(JSONObject input) {
		
		String[] values = new String[3];
		
		// get tomorrow's date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		
		// format date and add it to array
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		values[0] = formatter.format(calendar.getTime());
		
		if (input != null) {
			if (input.get("org") instanceof String) {
				values[1] = input.getString("org");
			}
			
			if (input.get("dest") instanceof String) {
				values[2] = input.getString("dest");
			}
		}
		
		MessageFormat msgFormat = new MessageFormat(this.xmlRequest);
		this.xmlRequest = msgFormat.format(values);
		
		byte[] bytes = new byte[this.xmlRequest.length()];
		bytes = this.xmlRequest.getBytes();
		try {
			
			URL url = new URL(this.getServiceURL());
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			//connection.setRequestProperty("SOAPAction", "http://staging.ws.amadeusepower.com/wstripconciergehk/SearchFlightCalendar");
			
			OutputStream stream = connection.getOutputStream();
			stream.write(bytes);
			stream.close();
			
			JSONObject response = null;
			
			
			try {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder responseData = new StringBuilder();
				
				// read all the contents
				String line = br.readLine();
				while (line != null) {
					responseData.append(line);
					line = br.readLine();
				}
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseData.toString())));
				document.getDocumentElement().normalize();
				
				response = convertXMLToJSON(document.getDocumentElement());
				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}	
			
			return response;
			
		} catch (IOException e) {
			return null;
		}
	}
	
	private JSONObject convertXMLToJSON(Node node) {
		
		JSONObject json = new JSONObject();
		NamedNodeMap nodeMap = node.getAttributes();
		for (int i = 0; i < nodeMap.getLength(); i++) {
			Node currentNode = nodeMap.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            //calls this method for all the children which is Element
	        	json.put(currentNode.getNodeName(), convertXMLToJSON(currentNode));
	        } else {
	        	json.put(currentNode.getNodeName(), currentNode.getNodeValue());
	        }
		}
		
		NodeList nodeList = node.getChildNodes();
		if (nodeList.getLength() > 0) {
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node currentNode = nodeList.item(i);
		        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		            //calls this method for all the children which is Element
		        	json.put(currentNode.getNodeName(), convertXMLToJSON(currentNode));
		        } else {
		        	json.put(currentNode.getNodeName(), currentNode.getNodeValue());
		        }
		    } 
		}
		
		return json;
	}
	
}
