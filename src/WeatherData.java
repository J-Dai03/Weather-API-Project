import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Dictionary;
import java.util.Hashtable;

public class WeatherData {
    static final boolean DEBUGSTATUS = false;

    /* A better implementation would be to make a recursive function, 
        which would try and extract the data for each key, 
        and if the data is a string or a double or int, the result would be written to the Dictionary.
        Getting an appropriate name for each key could be difficult
            - snow in the last hour and rain in the last hour both have the key "1h", but are in 2 different JSONObjects, the 2 of which are in the JSONObject
        We could use the keys used to reach the value in the first place, but it would require something to seperate each level, and it can't appear in the keys.

        I didn't do this because of time constraints and how it would require getting a better understanding of json.simple, 
        which I would rather replace with gson or Jacksoninstead.
    */  


    private Dictionary<String, String> weatherFeatures;
    private String[] weatherFeatureNames = {"main", "description", "temp", "feels_like", "humidity", "speed", "all", "rainInLast1h", "rainInLast3h", "snowInLast1h", "snowInLast3h"};

    /** Returns null for invalid keys, and empty strings for when there is no data for that key.
     * Keys:
     * "main": weather group descriptor, e.g. "Thunderstorm", "Clear", "Clouds", etc.
     * "description": condition within the group, e.g. "thunderstorm with light rain", "clear sky", "scattered clouds: 25-50%". 
     * "temp": Temperature in Celsius.
     * "feels_like": Perceived temperature in Celsius.
     * "humdity": is a percentage.
     * "speed": windspeed is in m/s.
     * "all": cloudiness as a percentage.
     * "rainInLast1h": Rain volume for the last 1 hour, in mm.
     * "rainInLast3h": Rain volume for the last 3 hours, in mm.
     * "snowInLast1h": Snow volume for the last 1 hour, in mm.
     * "snowInLast3h": Snow volume for the last 3 hours, in mm.
    */
    public String getWeatherFeatureData(String FeatureName){
        return weatherFeatures.get(FeatureName);
    } 

    /** Removes the entry with the input key. Added for testing purposes.*/
    public void remove(String FeatureName){
        weatherFeatures.remove(FeatureName);
    }

    /**
     * Keys:
     * "main": weather group descriptor, e.g. "Thunderstorm", "Clear", "Clouds", etc.
     * "description": condition within the group, e.g. "thunderstorm with light rain", "clear sky", "scattered clouds: 25-50%". 
     * "temp": Temperature in Celsius.
     * "feels_like": Perceived temperature in Celsius.
     * "humdity": is a percentage.
     * "speed": windspeed is in m/s.
     * "all": cloudiness as a percentage.
     * "rainInLast1h": Rain volume for the last 1 hour, in mm.
     * "rainInLast3h": Rain volume for the last 3 hours, in mm.
     * "snowInLast1h": Snow volume for the last 1 hour, in mm.
     * "snowInLast3h": Snow volume for the last 3 hours, in mm.
    */
    public WeatherData(JSONObject OpenWeatherAPIResponse, WeatherAppUIHandler UIHandler){
        if (DEBUGSTATUS){
            UIHandler.debugMessage("Begin constructing WeatherData object");
        }

        // intialise the data storage dictionary and assign all the keys with the default value, an empty string
        weatherFeatures = new Hashtable<>();
        for (int i = 0; i < weatherFeatureNames.length; i++){
            weatherFeatures.put(weatherFeatureNames[i], "");
        }

        // Go through each group
        // weather group
        try {
			JSONArray weatherHoldingArray = (JSONArray) OpenWeatherAPIResponse.get("weather");
            JSONObject weather = (JSONObject) weatherHoldingArray.getFirst();

            String[] dataToGet = {"main", "description"};
            for (int i = 0; i < dataToGet.length; i++){
                String pulledData = weather.get(dataToGet[i]).toString();
                if (pulledData != null){
                    weatherFeatures.put(dataToGet[i], pulledData);
                }
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONArray with key "weather"
		}
        // main group
        try {
            JSONObject main = (JSONObject) OpenWeatherAPIResponse.get("main");

            String[] dataToGet = {"temp", "feels_like", "humidity"};
            for (int i = 0; i < dataToGet.length; i++){
                String pulledData = main.get(dataToGet[i]).toString();
                if (pulledData != null){
                    weatherFeatures.put(dataToGet[i], pulledData);
                }
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONObject with key "main"
		}
        // wind group
        try {
            JSONObject wind = (JSONObject) OpenWeatherAPIResponse.get("wind");
            String[] dataToGet = {"speed"};
            for (int i = 0; i < dataToGet.length; i++){
                String pulledData = wind.get(dataToGet[i]).toString();
                if (pulledData != null){
                    weatherFeatures.put(dataToGet[i], pulledData);
                }
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONObject with key "wind"
		}
        // clouds group
        try {
            JSONObject clouds = (JSONObject) OpenWeatherAPIResponse.get("clouds");
            String[] dataToGet = {"all"};
            for (int i = 0; i < dataToGet.length; i++){
                String pulledData = clouds.get(dataToGet[i]).toString();
                if (pulledData != null){
                    weatherFeatures.put(dataToGet[i], pulledData);
                }
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONObject with key "clouds"
		}
        // rain group
        try {
            JSONObject rain = (JSONObject) OpenWeatherAPIResponse.get("rain");
            String pulledData = rain.get("1h").toString();
            if (pulledData != null){
                weatherFeatures.put("rainInLast1h", pulledData);
            }

            pulledData = rain.get("3h").toString();
            if (pulledData != null){
                weatherFeatures.put("rainInLast3h", pulledData);
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONObject with key "rain"
		}
        // snow group
        try {
            JSONObject snow = (JSONObject) OpenWeatherAPIResponse.get("snow");
            String pulledData = snow.get("1h").toString();
            if (pulledData != null){
                weatherFeatures.put("snowInLast1h", pulledData);
            }

            pulledData = snow.get("3h").toString();
            if (pulledData != null){
                weatherFeatures.put("snowInLast3h", pulledData);
            }
        } catch(NullPointerException e) {
            // NullPointerException when attempting to extract from JSONObject with key "smow"
		}

        if (DEBUGSTATUS){
            String contents = "";
            for (int i = 0; i < weatherFeatureNames.length; i++){
                contents = contents + weatherFeatureNames[i] + ": " + getWeatherFeatureData(weatherFeatureNames[i]) + System.getProperty("line.separator");
            }
            UIHandler.debugMessage("Data in WeatherData object:" + System.getProperty("line.separator") + contents);
        }
    }

    // Constructor for testing
    public WeatherData(){
        // intialise the data storage dictionary and assign all the keys with the default value, an empty string
        weatherFeatures = new Hashtable<>();
        String toAdd;
        for (int i = 0; i < weatherFeatureNames.length; i++){
            toAdd = "Value_for_" + weatherFeatureNames[i];
            weatherFeatures.put(weatherFeatureNames[i], toAdd);
        }
    }
}
