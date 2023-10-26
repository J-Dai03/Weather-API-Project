import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONConverter {

    static public JSONObject convert_to_JSONObject(String textFormData){
        // Attempts to convert text form JSON to JSONObject
        // Returns null if the input text is invalid

        JSONObject ToReturn = null;

        try {
			Object file = JSONValue.parse(textFormData);
            ToReturn = (JSONObject)file;
		} catch (Exception e) {
            System.out.println("JSON parse failure");
            System.out.println("Attempted to parse: " + textFormData);
			e.printStackTrace();
		}
		return ToReturn;
    }


    static public JSONArray convert_to_JSONArray(String textFormData){
        // Attempts to convert text form JSON to JSONArray
        // Returns null if the input text is invalid

        JSONArray ToReturn = null;

        try {
			Object file = JSONValue.parse(textFormData);
            ToReturn = (JSONArray)file;
		} catch (Exception e) {
            System.out.println("JSON parse failure");
            System.out.println("Attempted to parse: " + textFormData);
			e.printStackTrace();
		}
		return ToReturn;
    }
}
