package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class TicketMasterAPI {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = ""; // no restriction
	private static final String API_KEY = "y7PFQCAonDvpC7z0mMUZCbEgcruj7Bba";
	
	public JSONArray search(double lat, double lon, String keyword) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8"); // hello world => hello%20world
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String geoHash = GeoHash.encodeGeohash(lat, lon, 9); // 9 means transfer to 9 digits
		
		String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=50", API_KEY, geoHash, keyword);
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL + "?" + query).openConnection();

			connection.setRequestMethod("GET");
			
			// HTTP/1.0 200 OK; HTTP/1.0 401 Unauthorized.  
			// It will return 200 and 401 respectively. Returns -1 if no code can be discerned from the response 
			int responseCode = connection.getResponseCode();
			
			System.out.println("Sending 'GET' to URL: " + URL);
			System.out.println("Response Code: " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String inputLine;
			
//			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			StringBuilder response = new StringBuilder();
//			
//			String inputLine;
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//			
//			JSONObject obj = new JSONObject(response.toString());
//			if (!obj.isNull("_embedded")) {
//				JSONObject embbeded = obj.getJSONObject("_embedded");
//				return embbeded.getJSONArray("events");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new JSONArray();
	}
	
	
	// for debug use, print the returned json array
	private void queryAPI(double lat, double lon) {
		JSONArray events = search(lat, lon, null);
		try {
			for (int i = 0; i < events.length(); ++i) {
				JSONObject event = events.getJSONObject(i);
				System.out.println(event.toString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
