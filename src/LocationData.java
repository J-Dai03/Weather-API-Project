public class LocationData {
    private String latitiude;
    private String longitude;
    private String locationName;
    private String country;
    private String stateOrZipcode;
    /** This is true when stateOrZipcode represents the state, and not the zipcode */
    private boolean stateNotZip;

    public LocationData(String latitiude, String longitude, String locationName, String country, String stateOrZipcode, boolean stateNotZip){
        this.latitiude = latitiude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.country = country;
        this.stateOrZipcode = stateOrZipcode;
        this.stateNotZip = stateNotZip;
    }

    //Getters
    public String getLat(){return latitiude;}
    public String getLong(){return longitude;}
    public String getName(){return locationName;}
    public String getCountry(){return country;}
    public String getStateOrZip(){return stateOrZipcode;}
    /** A boolean that returns true if getStateOrZip returns the state, and returns false if it returns the zipcode/postcode */
    public boolean getStateNotZip(){return stateNotZip;}

    //Setters
    public void setLat(String input){latitiude = input;}
    public void setLong(String input){longitude = input;}
    public void setName(String input){locationName = input;}
    public void setCountry(String input){country = input;}
    public void setStateOrZip(String input){stateOrZipcode = input;}
    public void setStateNotZip(boolean input){stateNotZip = input;}
}