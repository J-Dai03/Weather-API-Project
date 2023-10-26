import java.util.*;
public class APIKeyManager {
    private static Dictionary<String, String> Key_Dictionary = new Hashtable<>();
    
    // At time of writing, a free subscription with all the permissions needed for this API is available (See https://openweathermap.org/price)
    // Add your key by putting it in the second arguement of Key_Dictionary.put();
    static{
        Key_Dictionary.put("Open_Weather_API", "");
    }
    
    static public String getAPIKey(String API_name){
        String ToReturn = Key_Dictionary.get(API_name);
        if (ToReturn == null){
            ToReturn = "INVALID KEY";
        }
        return ToReturn;
    }
}