import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;

public class Geocoder {

	static final boolean DEBUGSTATUS = false;

	private static String getAPIKey(){
		return APIKeyManager.getAPIKey("Open_Weather_API");
	}

	// Geocoding stuff
	// Geocoding by the location name
	public static String GeocodingByNameAsString(String location_name, WeatherAppUIHandler UIHandler){
		/*NOTE: THE URL CANNOT HAVE ANY SPACES, OR <, or >
			so the location name must be sanitised. Currently, it just replaces spaces with underscores.
			I would use a regex, but I don't have the time.
		*/

		String ScrubbedLocationName = ScrubForURLUsability(location_name);

		return callGeocodingAPI("http://api.openweathermap.org/geo/1.0/direct?q=" + ScrubbedLocationName + "&limit=" + "5" + "&appid=" + getAPIKey(), UIHandler);
	}

	// Geocoding by zipcode/postcode
	// NOTE: The country code is using the ISO 3166:2 standard
	public static String GeocodingByCodeAsString(String zipcode_slash_postcode, String country_code, WeatherAppUIHandler UIHandler){
		/* Split up version in comment
		String target_URL = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode_slash_postcode;
		target_URL = target_URL + "," + country_code;
        target_URL = target_URL + "&appid=" + getOpenWeatherAPIKey();*/
		return callGeocodingAPI("http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode_slash_postcode + "," + country_code + "&appid=" + getAPIKey(), UIHandler);
	}

	// Calls the Geocoding API with a preassembled URL
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

	private static String ScrubForURLUsability(String inputText){
		return inputText.replace(' ', '_');
	}

	// The following 2 functions don't work yet, because:
		//I haven't found a free API to do it
		//I don't have time to make an algorithm myself
	// Should verify the validity of the input zipcode/postcode. 
	public static boolean verifyZipCode(String inputZipcode, String country_code){
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
	// Should verify the validity of the input as an ISO 3166:2 standard country code 
	public static boolean verifyCountryCode(String country_code){
		// IMPORTANT: DOESN'T WORK YET
		//Verifying here
		//_________
		//_________
		return true;
	}
}