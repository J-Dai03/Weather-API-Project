import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;

public class Geocoder {

	static final boolean DEBUGSTATUS = false;

	/** Gets the OpenWeather key from the APIKeyManager class.
	 * @return A String that should hold the OpenWeather key.
	 */
	private static String getAPIKey(){
		return APIKeyManager.getAPIKey("Open_Weather_API");
	}

	// Geocoding stuff
	/**  Geocoding by the location name
	 * @param location_name Gives the name of the location the user wants to search for.
	 * @param UIHandler Used for handling IO.
	 * @return Returns the API response, some JSON as a String.
	*/
	public static String GeocodingByNameAsString(String location_name, WeatherAppUIHandler UIHandler){
		/*NOTE: THE URL CANNOT HAVE ANY SPACES, OR <, or >
			so the location name must be sanitised. Currently, it just replaces spaces with underscores.
			I would use a regex, but I don't have the time.
		*/

		String ScrubbedLocationName = ScrubForURLUsability(location_name);

		return callGeocodingAPI("http://api.openweathermap.org/geo/1.0/direct?q=" + ScrubbedLocationName + "&limit=" + "5" + "&appid=" + getAPIKey(), UIHandler);
	}

	/**  Geocoding by the zipcode/postcode. NOTE: The country code is using the ISO 3166:2 standard.
	 * @param zipcode_slash_postcode Gives the zipcode/postcode of the location the user wants to search for.
	 * @param country_code Gives the country code of the location the user wants to search for. Note: This uses the ISO 3166:2 standard.
	 * @param UIHandler Used for handling IO.
	 * @return Returns the API response, some JSON as a String.
	*/
	public static String GeocodingByCodeAsString(String zipcode_slash_postcode, String country_code, WeatherAppUIHandler UIHandler){
		/* Split up version in comment
		String target_URL = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode_slash_postcode;
		target_URL = target_URL + "," + country_code;
        target_URL = target_URL + "&appid=" + getOpenWeatherAPIKey();*/
		return callGeocodingAPI("http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode_slash_postcode + "," + country_code + "&appid=" + getAPIKey(), UIHandler);
	}

	/** Calls the Geocoding API with a preassembled URL
	 * @param targetURL The URL that needs to be called.
	 * @param UIHandler Used for handling IO.
	 * @return Returns the API response, some JSON as a String.
	 */
	private static String callGeocodingAPI(String targetURL, WeatherAppUIHandler UIHandler){
	    // In the event of failure, it returns an empty string.
		String toReturn = "";
		if (DEBUGSTATUS){
			UIHandler.debugMessage("Geocoder.callGeocodingAPI() called");
		}
	    
        // Construct request
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(targetURL))
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		// Send request
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			toReturn = response.body();
			if (DEBUGSTATUS){
				UIHandler.debugMessage("Geocoding Data get success");
			}
		} catch (IOException e) {
			UIHandler.errorMessage("Geocoding Data get failure: " + e.getStackTrace().toString());
		} catch (InterruptedException e) {
            UIHandler.errorMessage("Geocoding Data get failure: " + e.getStackTrace().toString());
		}

		return toReturn;
	}

	/*NOTE: THE URL CANNOT HAVE ANY SPACES, OR <, or >
			so the location name must be sanitised. Currently, it just replaces spaces with underscores.
			I would use a regex, but I don't have the time.
		*/
	/** NOT PROPOERLY IMPLEMENTED YET. A URL cannot have any spaces, or and angular brackets (These: <>), so the data needs to be sanitised. 
	 * Currently, it just replaces spaces with underscores. 
	 * I would use a regex, but I don't have the time to learn how to use them in java and or  the time to spend refining them to a working state. 
	 * @param inputURL The URL to be scrubbed
	 * @return The scrubbed URL*/
	private static String ScrubForURLUsability(String inputURL){
		return inputURL.replace(' ', '_');
	}

	/** Should verify the validity of the input zipcode/postcode. Doesn't work yet because I don't have time to make an algorithm myself and I haven't found a free API to do it.
	 * @param inputCode The zipcode/postcode that needs to be tested.
	 * @param country_code The country of the location described by the zipcode/postcode that needs to be tested.
	 * @return (If it is working, it) Returns a boolean describing whether the zipcode/postcode is valid or not
	 */
	public static boolean verifyZipCode(String inputCode, String country_code){
		// IMPORTANT: DOESN'T WORK YET
		if (verifyCountryCode(country_code)){

			//Verifying here
			//_________
			//_________
			return true;

		} else{
			return false;
		}
	}
	/** Should verify the validity of the input as an ISO 3166:2 standard country code. Doesn't work yet because I don't have time to make an algorithm myself and I haven't found a free API to do it.
	 * @param country_code The country of the location that needs to be tested.
	 * @return (If it is working, it) Returns a boolean describing whether the country_code is valid or not
	 */
	// Should verify the validity of the input as an ISO 3166:2 standard country code 
	public static boolean verifyCountryCode(String country_code){
		// IMPORTANT: DOESN'T WORK YET
		//Verifying here
		//_________
		//_________
		return true;
	}
}