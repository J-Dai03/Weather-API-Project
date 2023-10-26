import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;

public class WeatherRetriever {

	static final boolean DEBUGSTATUS = false;

	// Get weather stuff
	public static String getWeatherDataAsString(String latitude, String longitude, WeatherAppUIHandler UIHandler){
        // Gets the current weather data at the specified latitude and longitude as JSON in string form
	    // In the event of failure, it returns an empty string.
		String toReturn = "";
		if (DEBUGSTATUS){
			UIHandler.debugMessage("WeatherRetriever.getWeatherDataAsString() called");
		}

        // Construct target URL
		String targetURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude;
        targetURL = targetURL + "&lon=" + longitude;
        targetURL = targetURL + "&appid=" + getAPIKey();
		targetURL = targetURL + "&units=" + getPreferredUnits();
	    
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
				UIHandler.debugMessage("Weather Data get success");
			}
		} catch (IOException e) {
			UIHandler.errorMessage("Weather Data get failure: " + e.getStackTrace().toString());
		} catch (InterruptedException e) {
            UIHandler.errorMessage("Weather Data get failure: " + e.getStackTrace().toString());
		}
		return toReturn;
	}

	private static String getAPIKey(){
		return APIKeyManager.getAPIKey("Open_Weather_API");
	}

	private static String getPreferredUnits(){
		return "metric";
	}
}
