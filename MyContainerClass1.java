public class MyContainerClass1 {
    private String latitiude;
    private String longitude;
    // Continue tells us if we should continue on to use the coordinates collect to get the weather.
    private boolean Continue;

    /** Continue tells us if we should continue on to use the coordinates collect to get the weather. */
    public MyContainerClass1(String latitiude, String longitude, boolean Continue){
        this.latitiude = latitiude;
        this.longitude = longitude;
        this.Continue = Continue;
    }

    public MyContainerClass1(){
        latitiude = "0.0";
        longitude = "0.0";
        Continue = true;
    }

    //Getters
    public String getLat(){return latitiude;}
    public String getLong(){return longitude;}
    public boolean getCont(){return Continue;}

    //Setters
    public void setLat(String input){latitiude = input;}
    public void setLong(String input){longitude = input;}
    public void setCont(boolean input){Continue = input;}
}
