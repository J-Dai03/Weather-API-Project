import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WeatherApp {
    static final boolean DEBUGSTATUS = false;

    /** Runs the weather app with the "Terminal" UI type.*/
    public static void run(){
        run("Terminal");
    }

    /** Runs the weather app. 
     * @param UIType The input determines the UI type. Currently can only handle using the terminal - input "Terminal"
     * */
    public static void run(String UIType){

        //Set up the UI Handler. Currently can only output to the terminal/console
        WeatherAppUIHandler UIHandler = null;
        switch (UIType) {
            case "Terminal":
                UIHandler = new WeatherAppConsoleUIHandler();
                break;
            default:
                // Invalid UI type, end early
                System.out.println("Invalid UI type: "+ UIType);
                return;
        }

        boolean runOrNot = UIHandler.welcome();

        String locationSelectionMethod;
        
        /** response contains the latitiude and longitude, as a boolean called Continue that describes whether to continue.
         * I would use a tuple instead of a Container class, but I don't want to deal with more jar files
         */
        MyContainerClass1 response = new MyContainerClass1();

        while (runOrNot){
            locationSelectionMethod = UIHandler.getLocationSelectionMethod();
            runOrNot = (locationSelectionMethod.equals("X") == false);
            response = getCoords(response, locationSelectionMethod, UIHandler);
            JSONObject weatherStatus = JSONConverter.convert_to_JSONObject(WeatherRetriever.getWeatherDataAsString(response.getLat(), response.getLong(), UIHandler));

            if (response.getCont()){
                // Extract weather
                WeatherData currentWeather = new WeatherData(weatherStatus, UIHandler);

                UIHandler.displayWeather(currentWeather);
            }
            // If the user hasn't chosen to quit at the Choose selection method stage.
            if (runOrNot){
                // Choose whether to make a new query or close program
                runOrNot = UIHandler.queryComplete();
            }
        }

        UIHandler.quittingMessage();
    }

    /** Fetches coordinates and tells the us whether the user wants to get the weather data at all
     * @param toReturn An object of class MyContainerClass1. It has 3 attributes, latitude, longitude and Continue. 
     * The first 2 are Strings and are what they intuitively mean. Continue is a boolean which describes whether the user wants to continue on to get the weather data or not.
     * @param UIHandler Used for handling IO.
     * @param locationSelectionMethod Tells us how the user wants to select the location. Should be "Code", "Name", "Coordinates", or "X". The Last one means the user want to back out.
	 * @return Returns the API response, some JSON as a String.
	 */
    private static MyContainerClass1 getCoords(MyContainerClass1 toReturn, String locationSelectionMethod, WeatherAppUIHandler UIHandler){
        String[] MethodsOfLocationSelection = {"Code", "Name", "Coordinates", "X"};
        int refCode = -1;
        for (int i = 0; i < MethodsOfLocationSelection.length; i++){
            if (locationSelectionMethod.equals(MethodsOfLocationSelection[i])){
                refCode = i;
                if (DEBUGSTATUS){
                    UIHandler.debugMessage("locationSelectionMethod: [" + locationSelectionMethod + "] does equal [" + MethodsOfLocationSelection[i] + "]");
                }
                break;
            }
            if (DEBUGSTATUS){
                UIHandler.debugMessage("locationSelectionMethod: [" + locationSelectionMethod + "] does not equal [" + MethodsOfLocationSelection[i] + "]");
            }
        }

        switch (refCode) {
            case 0:
                toReturn = getCoordsByCode(toReturn, UIHandler);
                break;
            case 1:
                toReturn = getCoordsByName(toReturn, UIHandler);
                break;
            case 2:
                toReturn = getCoordsDirectly(toReturn, UIHandler);
                break;
            case 3:
                //In this case, we exit the program
                toReturn.setCont(false);
                break;
            default:
                // Invalid  type, we exit the program
                UIHandler.errorMessage("Invalid location selection method: [" + locationSelectionMethod + "]");
                toReturn.setCont(false);
        }
        return toReturn;
    }

    /** Fetches coordinates via zipcode/postcode and tells the us whether the user wants to get the weather data at all 
     * @param toReturn An object of class MyContainerClass1. It has 3 attributes, latitude, longitude and Continue. 
     * The first 2 are Strings and are what they intuitively mean. Continue is a boolean which describes whether the user wants to continue on to get the weather data or not.
     * @param UIHandler Used for handling IO.
	 */
    private static MyContainerClass1 getCoordsByCode(MyContainerClass1 toReturn, WeatherAppUIHandler UIHandler){
        boolean AttemptToGetCoords = true;

        while (AttemptToGetCoords){
            // String Array with 3 entries. 
            // Entry 1 holds the zipcode, entry 2 holds the country code
            // If the 3rd entry is an X, the user wants to back out
            // The UI handler is supposed to make sure the values are valid before returning.
            String[] ZipcodeAndCountryCode = UIHandler.getCoordsByCodePrompt();

            // If the user wants to back out
            if (ZipcodeAndCountryCode[2] != ""){
                AttemptToGetCoords = false;
                toReturn.setCont(false);
            } else {
                // If the user doesn't want to back out

                // Geocode via the API
                JSONObject APIResponse = JSONConverter.convert_to_JSONObject(Geocoder.GeocodingByCodeAsString(ZipcodeAndCountryCode[0], ZipcodeAndCountryCode[1], UIHandler));

                // Check if APIResponse contains an entry with the "cod" key, which means the Geocoding failed
                if (APIResponse.containsKey("cod")){
                    UIHandler.GeocodingFailure();
                } else {
                    // Geocoding success, extract data
                    String latitiude = APIResponse.get("lat").toString();
                    String longitude = APIResponse.get("lon").toString();
                    String locationName = APIResponse.get("name").toString();
                    String country = APIResponse.get("country").toString();
                    String zip = APIResponse.get("zip").toString();

                    LocationData zipcodedLocation = new LocationData(latitiude, longitude, locationName, country, zip, false);

                    // Check with user that the correct location has been taken
                    boolean Accepted = UIHandler.confirmGeocodingByCode(zipcodedLocation);

                    // If the correct location has been taken
                    if (Accepted){
                        toReturn.setLat(latitiude);
                        toReturn.setLong(longitude);
                        toReturn.setCont(true);
                        AttemptToGetCoords = false;
                    } else {
                        UIHandler.tryToGeocodeByCodeAgainMessage();
                    }
                }
            }
        }
        return toReturn;
    }

    /** Fetches coordinates via location name and tells the us whether the user wants to get the weather data at all 
     * @param toReturn An object of class MyContainerClass1. It has 3 attributes, latitude, longitude and Continue. 
     * The first 2 are Strings and are what they intuitively mean. Continue is a boolean which describes whether the user wants to continue on to get the weather data or not.
     * @param UIHandler Used for handling IO.
	 */
    private static MyContainerClass1 getCoordsByName(MyContainerClass1 toReturn, WeatherAppUIHandler UIHandler){
        boolean AttemptToGetCoords = true;

        while (AttemptToGetCoords){
            String locationName = UIHandler.getLocationName();
            // If the user wants to back out
            if (locationName.equals("X")){
                AttemptToGetCoords = false;
                toReturn.setCont(false);
            } else {
                // If the user doesn't want to back out

                //Gecode by the API
                String stringFormAPIResponse = Geocoder.GeocodingByNameAsString(locationName, UIHandler);

                // If the string form of the API response doesn't begin with [, i.e. if it's not a JSONArray
                if (stringFormAPIResponse.substring(0, 1).equals("[") == false){
                    // There has been an error of some form
                    UIHandler.displayErrorMessageWrongAPIResponse(stringFormAPIResponse);
                } else {
                    // Convert the string form API response to a JSONArray, where each entry describes a location, and make an array to store the data from each entry
                    JSONArray APIResponse = JSONConverter.convert_to_JSONArray(Geocoder.GeocodingByNameAsString(locationName, UIHandler));
                    LocationData[] locations = new LocationData[APIResponse.size()];

                    // Loop through the JSONArray and fill the locations variable
                    for (int i = 0; i < APIResponse.size(); i++) {
                        JSONObject CurrentItem = (JSONObject) APIResponse.get(i);

                        // Geocoding success, extract data
                        String latitiude = CurrentItem.get("lat").toString();
                        String longitude = CurrentItem.get("lon").toString();
                        String pulledLocationName = CurrentItem.get("name").toString();
                        String country = CurrentItem.get("country").toString();
                        String state = "";
                        if (CurrentItem.containsKey("state")){
                            state = CurrentItem.get("state").toString();
                        }
                        // Add to array of locations
                        locations[i] = new LocationData(latitiude, longitude, pulledLocationName, country, state, true);
                    }

                    int selection;
                    if (locations.length == 0){
                        UIHandler.GeocodingByNameFailureNoResults();
                        selection = -1;
                    } else {
                        /* Show the user the locations, and let them choose which is the one they want. 
                            -1 means they want to back out.
                            */
                        selection = UIHandler.displayLocationDataFromGeocodingByName(locations);
                    }


                    // If the response is invalid
                    if ((selection < -1) || (selection >= APIResponse.size())){
                        UIHandler.errorMessage("UIHandler.displayLocationDataFromGeocodingByName(locations) returned an out of bounds value." + System.getProperty("line.separator") + selection);
                        UIHandler.tryToGeocodeByNameAgainMessage();
                    } else {
                        if (selection != -1) {
                            /*else if the number does not represent rejection, submit those numbers, set Continue to be true to show that the user doesn't want to back out, 
                            and set AttemptToGetCoords to false in order to show we don't need to try and get the coords now.
                            */
                            toReturn.setLat(locations[selection].getLat());
                            toReturn.setLong(locations[selection].getLong());
                            toReturn.setCont(true);
                            AttemptToGetCoords = false;
                        } else {
                            // The user wants to back out
                            AttemptToGetCoords = false;
                            toReturn.setCont(false);
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    /** Fetches coordinates from the user via the UIHandler parameter and tells the us whether the user wants to get the weather data at all 
     * @param toReturn An object of class MyContainerClass1. It has 3 attributes, latitude, longitude and Continue. 
     * The first 2 are Strings and are what they intuitively mean. Continue is a boolean which describes whether the user wants to continue on to get the weather data or not.
     * @param UIHandler Used for handling IO.
	 */
    private static MyContainerClass1 getCoordsDirectly(MyContainerClass1 toReturn, WeatherAppUIHandler UIHandler){
        String[] Coords = UIHandler.getCoordsDirectly();
        // If the user wants to back out
        if (Coords[2].equals("X")){
            toReturn.setCont(false);
        } else {
            toReturn.setLat(Coords[0]);
            toReturn.setLong(Coords[1]);
            toReturn.setCont(true);
        }
        return toReturn;
    }
}