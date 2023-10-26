import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONConverter {

    /** Attempts to convert text form JSON to JSONObject
     * @param textFormData The data to be converted to a JSONObject
     * @return Returns the JSONObject form of the parameter textFormData or, if the input text is invalid, null */ 
    static public JSONObject convert_to_JSONObject(String textFormData){

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


    /** Attempts to convert text form JSON to JSONArray
     * @param textFormData The data to be converted to a JSONArray
     * @return Returns the JSONArray form of the parameter textFormData or, if the input text is invalid, null */ 
    static public JSONArray convert_to_JSONArray(String textFormData){

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
