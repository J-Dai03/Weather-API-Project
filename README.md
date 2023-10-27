# Weather-API-Project
A program I created to practice and learn using APIs, though I learned quite a bit more as well. (See the "What I Learned" section)

## IMPORTANT NOTES: 
- The program uses the OpenWeather API and thus requires a key from them which needs to be maunally installed before compilation.
  - At time of writing, a valid key can be aquired for free using a free subscription that has all the permissions needed for this API (See [Here](https://openweathermap.org/price)).
  - The key is installed in the APIKeyManager file. Simply open it in a text editor or code editor and copy in the key you get from OpenWeather to the directed location.
- The program does use json.simple, so make sure you have the .jar file installed
- There are 2 reasons everything was uploaded at once. 
  1. Firstly, because I used my university account's OneDrive instead of GitHub, as it to allowed me to access the project from the university computers without logging into GitHub. 
  2. Secondly, because I was being paranoid about accidently publishing my API key. While I doubt anyone would use someone else's API key if an equivalent could be aquired for free, I felt like I should get in the habit of trying very hard at keeping them secret.
- I have many possible ideas for upgrades for the system, but I have already spent quite a lot time on this project. When I reached the free subscription's request limit during testing, I decided it was time to stop. On a sidenote, the response I reached the limit was:  
> {"cod":429, "message": "Your account is temporary blocked due to exceeding of requests limitation of your subscription type. Please choose the proper subscription https://openweathermap.org/price"}.
- Due to testing being cut short, not all ways of backing out have been thoroghly tested.


I have many possible ideas for upgrades for the system, but I have already spent quite a lot time on this project, when I reached the free subscription's request limit during testing, I decided it was time to stop. On a sidenote, the response I reached the limit was: {"cod":429, "message": "Your account is temporary blocked due to exceeding of requests limitation of your subscription type. Please choose the proper subscription https://openweathermap.org/price"}.


## Possible upgrades to the program:
- Adding more UI options.
  - Currently, the only way to use the program is with the console/terminal.
- Allowing the user to see more details about the location selected when selecting via coordinates.
  - This could be done with the OpenWeather Reverse Geocoding API, also accessable for free with the same API key at time of writing.
- Showing the location data and weather data next to each other.
- Showing more weather data.
  - Wind direction and air pressure are provided by the API, but I didn't extract the data.
- Extracting the weather data from the JSONObject could be done with a more maintainable recursive function.
  - See the "Better JSON Data Extraction" section for more details.
- The JSON could be handled with something more modern like GSON or Jackson instead of json.sinmple
- The functions that get the coordinates (getCoordsByCode, getCoordsByName, and getCoordsDirectly) could return a tuple of 2 doubles and a boolean to signify the coordinates and whether or not the user backed out or not, instead of returning a container class.
- A better way of storing a location's zipcode/post and its state.
  - Currently they are stored in the same String variable, and a boolean named stateNotZip is used to determine which it is.
- For the select by zipcode feature, we could add the option to choose to re-enter the country code when entering the zip code.
- For the select by coordinates feature, we could add the option to choose to re-enter the latitude when entering the longitude.
- Allowing the user to enter their own API key after compilation
  - Currently, the key needs to be maunally installed before compilation by opening the APIKeyManager.java file in a text editor or a code editor, and copying in the key to the directed location.
- We could make it more obvious at a glance what information we are asking to the user to enter
  -  The prompt given to the user when asking them to input data could be customised for each unique piece of information requested

### Better JSON Data Extraction:

- I didn't do this because of time constraints and because it would require getting a better understanding of json.simple, which I would rather replace with gson or Jackson instead.  
- A better implementation would be to make a recursive function, which would try and extract the data for each key.  
  - If the data is a kind of JSON object, like a JSONObject or a JSONArray, the recursive function would be called on it. 
    - I would need to learn a lot more about JSON objects and find a way to determine what kind of object it is. I could use a try catch block, where succesful conversion to a particular object class would set a previously initialised variable to a value specific to that particular object class, but that is probably quite inefficient in terms of memory and processing time.
  - If the data is a string or a double or int, the result would be written to the Dictionary.   
  - Getting an appropriate name for the key of each Dictionary entry could be difficult - snow in the last hour and rain in the last hour both have the key "1h", but the first is in the JSONObject accessed with the key "snow", and the second is in the JSONObject accessed with the key "rain".  
    - We could use a concatenation of the keys used to reach the value in the first place, but it would require a special character/group of charcters that can't appear in the keys in order to seperate each level of recursion. We need the special character/group of charcters, or we could get a situation where mains.now and main.snow would have the same key. While such a situation is unlikely, it is a possibilty that cannot be ignored.

## What I Learned:
Over the course of creating this program, I learned:
- How to make RESTFUL API calls
- How to handle JSONObjects and JSONArrays in java with json.simple
- How to use Markdown Documentation, as evidenced by this file, and by my Demonstration Documentation file.
- How to use Javadoc  

It also helped me practice:
- General programming
- Debugging
- Inheritance
- Polymorphism
- Abstract classes
- Container classes
- Object Oriented Programming
- (Rudimentary) UI Design
