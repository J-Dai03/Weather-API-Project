public abstract class WeatherAppUIHandler {

    /** Called by WeatherApp.run() near the beginning. Prints the welcome message and returns true if a key for OpenWeather is in the APIKeyManager, though it doesn't check if it is valid. */
    public abstract boolean welcome();

    /** Called by WeatherApp.run() to determine how the user wants to choose the location. Invalid values should have the same effect as "X"
        @return Should return a String or certain values:
        "Code" to indicate geocoding by zipcode/postcode.
        "Name" to indicate geocoding by the name or the location.
        "Coordinates" to indicate using user given latitude and longitude.
        "X" to indicate the user wants to back out and not do any of those.
        */ 
    public abstract String getLocationSelectionMethod();

    /** Called by getCoordsByCode of the WeatherApp to determine the zipcode.
        @return Should return an array of Strings, 3 items long:
        item 0: Contains the zipcode/postcode.
        item 1: Contains the country code.
        item 2: Contains whether the user wants to back out or not. "X" means they want to back out.
        A tuple would have been better, but I didn't think of it then, and this is good enough.
        */ 
    public abstract String[] getCoordsByCodePrompt();

    /** Called by getCoordsByCode of the WeatherApp to report that the API response doesn't have location data */
    public abstract void GeocodingFailure();

    /** Called by getCoordsByCode of the WeatherApp to confirm that the location is the one the user wants.
     * @param zipcodedLocation Data on the location retrieved in a LocationData class object
     * @return Returns true if the location is the one the user wants */
    public abstract boolean confirmGeocodingByCode(LocationData zipcodedLocation);

    /** Called by getCoordsByCode of the WeatherApp when the user says location retrieved is wrong */
    public abstract void tryToGeocodeByCodeAgainMessage();

    /** Called by getCoordsByName of the WeatherApp when the API doesn't begin with [, and thus can't be a JSONArray as expected 
     * @param APIResponse The String form of the API response that couldn't be read.
    */
    public abstract void displayErrorMessageWrongAPIResponse(String APIResponse);

    /** Called by getCoordsByName of the WeatherApp to determine which of the presented locations the user wants
     * @param input Data on the locations retrieved in an array of LocationData class objects.
     * @return Returns an integer describing which option was chosen. 
     * 0 means the 1st item was chosen, 1 means the 2nd item was chosen, etc, -1 means all options were rejected. */
    public abstract int displayLocationDataFromGeocodingByName(LocationData[] input);

    /** Called by getCoordsByName of the WeatherApp when no results were returned */
    public abstract void GeocodingByNameFailureNoResults();

    /** Called by getCoordsByName of the WeatherApp when the user says all locations retrieved are wrong */
    public abstract void tryToGeocodeByNameAgainMessage();

    /** Called by getCoordsByName of the WeatherApp to get the name of the location that the user wants to search for.
        @return Returns a String with the location that the user wants to search for, or an X, for if the user wants to back out. 
        */ 
    public abstract String getLocationName();

    /** Called by getCoordsDirectly of the WeatherApp to coordinates that the user wants to search for
        @return Returns an array of 3 Strings. Item 0: latitiude, Item 1: longitude, Item 3: whether the user wants to back out or not - Item 3's value is X when they want to back out
    */
    public abstract String[] getCoordsDirectly();

    /** Called by WeatherApp.run() to present the weather data to the user. 
     * @param input An object of the WeatherData type, holding the data on the weather to be displayed.
    */
    public abstract void displayWeather(WeatherData input);

    /** Called by WeatherApp.run() to get whether the user wants to continue to make requests, or to back out. */
    public abstract boolean queryComplete();

    /** Presents a debug message to the user
     * @param message The message to be presented
     */
    public abstract void debugMessage(String message);

    /** Presents a error message to the user
     * @param message The message to be presented
     */
    public abstract void errorMessage(String message);

    /** Called by run to sginify that the program is ending because the user has chose to quit.*/
    public abstract void quittingMessage();
}