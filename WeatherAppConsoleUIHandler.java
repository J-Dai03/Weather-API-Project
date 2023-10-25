import java.util.Dictionary;
import java.util.Hashtable;


public class WeatherAppConsoleUIHandler extends WeatherAppUIHandler{
    private static String getUserInput(){
        System.out.print("Enter input: ");        
        return System.console().readLine();
    }

    public WeatherAppConsoleUIHandler(){}

    /** Contains the welcome message and returns true if a key for OpenWeather is in the APIKeyManager, though it doesn't check if it is valid. */
    public boolean welcome(){
        boolean toReturn = true;
        String[] welcomeMessage = {"Welcome to my basic weather information program, courtesey of OpenWeather.", 
        "Their website: https://openweathermap.org/",
        "Choose a location and get the current weather!", 
        "Please note that the program requires an internet connection, and an key to the OpenWeather API"};
        String toPrint = makeStringFromStringArray(welcomeMessage);
        
        System.out.println(makeStringFromStringArray(welcomeMessage));

        if (APIKeyManager.getAPIKey("Open_Weather_API").equals("")){
            toPrint = toPrint + "Please add your own API key by opening the APIKeyManager.java file in a text editor or code editor and pasting the key where the comment tells you to." + System.getProperty("line.separator");
            toReturn = false;
        }
        System.out.println(toPrint);
        return toReturn;
    }

    public String getLocationSelectionMethod(){
        String toReturn = "";
        String[] methodsAndDescriptions = {"(Select 1) By name - Enter the name of the city", 
        "(Select 2) By zipcode/postcode - Enter the zipcode/postcode and the country code", 
        "(Select 3) By coordinates - Enter the latitude and longitude", 
        "(Select X) Quit"};
        String invalidInputMessage = "Invalid input. Reminder, the valid inputs are:" + System.getProperty("line.separator") + makeStringFromStringArray(methodsAndDescriptions);

        boolean inputValid = false;

        Dictionary<String, String> userInputToReturnValueDictionary = new Hashtable<>();
        userInputToReturnValueDictionary.put("1", "Name");
        userInputToReturnValueDictionary.put("2", "Code");
        userInputToReturnValueDictionary.put("3", "Coordinates");
        userInputToReturnValueDictionary.put("X", "X");
        // Would set validInputs to be the keys of userInputToReturnValueDictionary, but I don't want to deal with Enumeration
        String[] validInputs = {"1", "2", "3", "X"};

        String toPrint = "How would you like to choose the location?" + System.getProperty("line.separator") + makeStringFromStringArray(methodsAndDescriptions);
        System.out.println(toPrint);
        while (inputValid == false){

            String userInput = getUserInput();

            // Check if the input is valid
            for (int i = 0; i < validInputs.length; i++){
                inputValid = (userInput.equals(validInputs[i]));
                if (inputValid){break;}
            }

            // if the user input is valid
            if (inputValid){
                toReturn = userInputToReturnValueDictionary.get(userInput);
            } else {
                System.out.println(invalidInputMessage);
                // Wipe toPrint for the next loop
                toPrint = "";
            }
        }

        return toReturn;
    }

    public String[] getCoordsByCodePrompt(){
        /* IMPORTANT NOTE: Geocoder.verifyCountryCode and Geocoder.verifyZipCode don't work yet because:
            I haven't found a free API to do it
            I don't have time to make an algorithm myself
        At time of writing, both functions always return true */

        String[] toReturn = {"", "", ""};

        // Get country code
        String[] getCountryCodePrompt = {"Please enter the country code, or enter X to back out", "Note that we use the 2 charcter ISO 3166:2 standard"};
        System.out.println(makeStringFromStringArray(getCountryCodePrompt));
        String userInput = getUserInput();

        while (!"X".equals(userInput) && !Geocoder.verifyCountryCode(userInput)){
            System.out.println("Invalid country code. Please try again.");
            System.out.println(makeStringFromStringArray(getCountryCodePrompt));
            userInput = getUserInput();
        }

        // If the user wants to back out
        if ("X".equals(userInput)){
            toReturn[2] = "X";
        } else {
            // If the user wants to continue, write the country code to slot 1
            toReturn[1] = userInput;

            // Get zipcode/postcode
            String[] getZipCodePrompt = {"Please enter the zipcode/postcode, or enter X to back out"};
            System.out.println(makeStringFromStringArray(getZipCodePrompt));
            userInput = getUserInput();

            // Loop until the user wants to back out or until the zipcode is verified to be valid
            // Note that this has been coded to to keep looping if the user doesn't want to back out and the zipcode isn't verified to be valid
            while (!"X".equals(userInput) && !Geocoder.verifyZipCode(userInput, toReturn[1])){
                System.out.println("Invalid zipcode/postcode. Please try again.");
                System.out.println(makeStringFromStringArray(getZipCodePrompt));
                userInput = getUserInput();
            }
            // If the user wants to back out
            if ("X".equals(userInput)){
                toReturn[2] = "X";
            } else {
                // Write the zipcode to slot 0
                toReturn[0] = userInput;
            }
        }
        return toReturn;
    }

    public void GeocodingFailure(){
        System.out.println("Geocoding failed. Unable to get a response. Please try again.");
    }

    public boolean confirmGeocodingByCode(LocationData zipCodeLocation){
        boolean toReturn;

        String LocationDataAsStringToOutput = getLocationDataAsStringToOutput(zipCodeLocation);

        String[] responsesAndDescriptions = {"(Select Y) Yes, this is the location I wanted.", 
        "(Select N) No, this is not the location I wanted."};

        String invalidInputMessage = "Invalid input. Reminder, the valid inputs are:" + System.getProperty("line.separator") + makeStringFromStringArray(responsesAndDescriptions);


        System.out.println("Location retrieved:" + System.getProperty("line.separator") + LocationDataAsStringToOutput);
        System.out.println("Is this the location you wanted? " + System.getProperty("line.separator") + makeStringFromStringArray(responsesAndDescriptions));

        String userInput = getUserInput();

        String[] validInputs = {"Y", "N"};

        while (contains(userInput, validInputs) == false){
            System.out.println(invalidInputMessage);
            userInput = getUserInput();
        }

        if (userInput.equals("Y")){
            toReturn = true;
        } else {
            toReturn = false;
        }

        return toReturn;
    }

    public void tryToGeocodeByCodeAgainMessage(){
        System.out.println("Wrong location retrieved. Please try again.");
    }

    public void displayErrorMessageWrongAPIResponse(String input){
        System.out.println("Unexpected API response. ");
        System.out.println("API response: " + input);
    }

    public int displayLocationDataFromGeocodingByName(LocationData[] input_location_data){
        // Assemble a String describing the user's options
        String LocationDataAsStringToOutput = "";
        for (int i = 0; i < input_location_data.length; i++){
            LocationDataAsStringToOutput = LocationDataAsStringToOutput + "(Select " + String.valueOf(i + 1) + "):" + System.getProperty("line.separator") + getLocationDataAsStringToOutput(input_location_data[i]) + System.getProperty("line.separator");
        }
        LocationDataAsStringToOutput = LocationDataAsStringToOutput + "(Select 0) Quit" + System.getProperty("line.separator");

        // Prep String for when the input is invalid
        String invalidInputMessage = "Invalid input. Reminder, the valid inputs are 1 to " + String.valueOf(input_location_data.length);

        // Ask the user for input
        System.out.println("Here are the locations retrieved:" + System.getProperty("line.separator") + LocationDataAsStringToOutput);
        String userInput = getUserInput();

        int numFormUserInput = 0;
        boolean inputValid = false;
        
        // Check if the input is valid
        if (isInt(userInput)){
            numFormUserInput = Integer.parseInt(userInput);
            inputValid = (numFormUserInput >= 0 && numFormUserInput <= input_location_data.length);
        }
        // While the input is not valid
        while (!inputValid){
            System.out.println(invalidInputMessage);
            userInput = getUserInput();
            if (isInt(userInput)){
                numFormUserInput = Integer.parseInt(userInput);
                inputValid = (numFormUserInput >= 0 && numFormUserInput <= input_location_data.length);
            }
        }
        return (numFormUserInput - 1);
    }

    public void GeocodingByNameFailureNoResults(){
        System.out.println("No results found. Please try again.");
    }

    public void tryToGeocodeByNameAgainMessage(){
         System.out.println("No location selected. Please try again.");
    }

    public String getLocationName(){
        System.out.println("Please enter the name of the location you want to search for, or X to exit.");
        return getUserInput();
    }

    public String[] getCoordsDirectly(){
        String[] toReturn = {"", "", ""};

        // Get latitude 
        System.out.println("Please enter the latitude, or X to back out.");
        String userInput = getUserInput();

        // While the input is not X and unable to be converted to a double
        while (!"X".equals(userInput) && !isDouble(userInput)){
            System.out.println("Invalid input. Please try again.");
            System.out.println("Please enter the latitude, or X to back out.");
            userInput = getUserInput();
        }

        // If the user wants to back out
        if ("X".equals(userInput)){
            toReturn[2] = "X";
        } else {
            // If the user wants to continue, write the latitude, i.e. the userInput, to slot 0
            toReturn[0] = userInput;

            // Get longitude
            System.out.println("Please enter the longitude, or X to back out.");
            userInput = getUserInput();

            // While the input is not X and unable to be converted to a double
            while (!"X".equals(userInput) && !isDouble(userInput)){
                System.out.println("Invalid input. Please try again.");
                System.out.println("Please enter the longitude, or X to back out.");
                userInput = getUserInput();
            }
            // If the user wants to back out
            if ("X".equals(userInput)){
                toReturn[2] = "X";
            } else {
                // Write the longitude to slot 1
                toReturn[1] = userInput;
            }
        }
        return toReturn;
    }

    public void displayWeather(WeatherData inputWeatherData){
        // Set up String to hold text to print
        String[] toPrintLines = new String[8];
        for (int i = 0; i < toPrintLines.length; i++){
            toPrintLines[i] = "";
        }
        String toAdd;

        // Make line 1
        toAdd = inputWeatherData.getWeatherFeatureData("main");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[0] = toAdd.toUpperCase();
        }
        toAdd = inputWeatherData.getWeatherFeatureData("description");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            if (!toPrintLines[0].equals("")){
                toPrintLines[0] = toPrintLines[0] + " - ";
            }
            toPrintLines[0] = toPrintLines[0] + toAdd.toUpperCase();
        }

        // Make line 2
        toAdd = inputWeatherData.getWeatherFeatureData("temp");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[1] = "Temp: " + toAdd + "°C";
        }
        toAdd = inputWeatherData.getWeatherFeatureData("feels_like");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            if (!toPrintLines[1].equals("")){
                toPrintLines[1] = toPrintLines[1] + " - ";
            }
            toPrintLines[1] = toPrintLines[1] + "Feels like: " + toAdd + "°C";
        }

        // Make line 3
        toAdd = inputWeatherData.getWeatherFeatureData("humidity");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[2] = "Humidity: " + toAdd + "%";
        }
        toAdd = inputWeatherData.getWeatherFeatureData("speed");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            if (!toPrintLines[2].equals("")){
                toPrintLines[2] = toPrintLines[2] + " - ";
            }
            toPrintLines[2] = toPrintLines[2] + "Windspeed: " + toAdd + "m/s";
        }

        // Make line 4
        toAdd = inputWeatherData.getWeatherFeatureData("all");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[3] = "Cloudiness: " + toAdd + "%";
        }

        // Make line 5
        toAdd = inputWeatherData.getWeatherFeatureData("rainInLast1h");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[4] = "Rain volume for the last 1 hour: " + toAdd + "mm";
        }

        // Make line 6
        toAdd = inputWeatherData.getWeatherFeatureData("rainInLast3h");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[5] = "Rain volume for the last 3 hours: " + toAdd + "mm";
        }

        // Make line 7
        toAdd = inputWeatherData.getWeatherFeatureData("snowInLast1h");
        if ((toAdd != null) && (toAdd.equals("") == false)){
            toPrintLines[6] = "Snow volume for the last 1 hour: " + toAdd + "mm";
        }

        // Make line 8
        toAdd = inputWeatherData.getWeatherFeatureData("snowInLast3h");
        if (toAdd != null){
            toPrintLines[7] = "Snow volume for the last 3 hours: " + toAdd + "mm";
        }

        String toPrint = "Weather:" + System.getProperty("line.separator") + makeStringFromStringArrayWithoutEmptyLines(toPrintLines);
        System.out.println(toPrint);
        /*
        for (int i = 0; i < toPrintLines.length; i++){
            System.out.println("Line " + i + ":" + toPrintLines[i]);
        } */
    }

    public boolean queryComplete(){
        boolean toReturn = true;
        String[] responsesAndDescriptions = {"(Select Y) Yes, I want to continue and make more queries.", 
        "(Select N or X) No, I want to quit."};
        String[] validInputs = {"Y", "N", "X"};
        String invalidInputMessage = "Invalid input. Reminder, the valid inputs are:" + System.getProperty("line.separator") + makeStringFromStringArray(responsesAndDescriptions);

        System.out.println("Do you wish to make more queries?" + System.getProperty("line.separator") + makeStringFromStringArray(responsesAndDescriptions));
        String userInput = getUserInput();

        while (contains(userInput, validInputs) == false){
            System.out.println(invalidInputMessage);
            userInput = getUserInput();
        }

        if (userInput.equals("Y")){
            toReturn = true;
        } else {
            toReturn = false;
        }

        return toReturn;
    }

    public void debugMessage(String message){
        System.out.println("|||--- Debug Message begin ---|||" + System.getProperty("line.separator") + message + System.getProperty("line.separator") + "|||--- Debug Message end ---|||");
    }

    public void errorMessage(String message){
        System.out.println("|||--- Error Message begin ---|||" + System.getProperty("line.separator") + message + System.getProperty("line.separator") + "|||--- Error Message end ---|||");
    }

    public void quittingMessage(){
        System.out.println("Quitting");
    }

    /** Fuses an array of strings to a single string, where each of the original strings are followed by a new line*/
    public String makeStringFromStringArray(String[] input){
        String toReturn = "";
        for (int i = 0; i < input.length; i++){
            toReturn = toReturn + input[i] + System.getProperty("line.separator");
        }
        return toReturn;
    }

    /** Fuses an array of strings to a single string, where each of the original strings are followed by a new line, and where empty strings are ignored*/
    public String makeStringFromStringArrayWithoutEmptyLines(String[] input){
        String toReturn = "";
        for (int i = 0; i < input.length; i++){
            if (!input[i].equals("")){
                toReturn = toReturn + input[i] + System.getProperty("line.separator");
            }
        }
        return toReturn;
    }

    /** Returns true if the input can be converted to a double */
    private boolean isDouble(String input){
        boolean toReturn;
        double convertedHolder = 0.0;
        try {
            Double doub = Double.parseDouble(input);
            toReturn = true;

        } catch (NumberFormatException e){
            toReturn = false;
        }
        return toReturn;
    }

    /** Returns a string which can be printed to show the input LocationData to the user */
    public String getLocationDataAsStringToOutput(LocationData input){
        String toPrint = "Coordinates: {" + input.getLat() + ", " + input.getLong() + "}" + System.getProperty("line.separator");
        toPrint = toPrint + "Name: " + input.getName() + System.getProperty("line.separator");
        toPrint = toPrint + "Country: " + input.getCountry() + System.getProperty("line.separator");
        if (input.getStateNotZip()){
            toPrint = toPrint + "State: ";
        } else {
            toPrint = toPrint + "Zipcode/Postcode: ";
        }
        toPrint = toPrint + input.getStateOrZip() + System.getProperty("line.separator");

        return toPrint;
    }

    /** Returns a boolean describing if the input can be converted to an integer */
    private static boolean isInt(String input) {
        boolean toReturn;
        try { 
            Integer.parseInt(input);
            toReturn = true;
        } catch(NumberFormatException e) { 
            toReturn = false; 
        }
        return toReturn;
    }

    /** Returns a boolean describing if inputString is equal to a String in inputStringArray */
    private static boolean contains(String inputString, String[] inputStringArray) {
        boolean toReturn = false;
        int i = 0;
        while (i < inputStringArray.length && toReturn == false){
            toReturn = inputString.equals(inputStringArray[i]);
            i++;
        }
        return toReturn;
    }
}
