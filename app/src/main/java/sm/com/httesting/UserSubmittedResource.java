package sm.com.httesting;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSubmittedResource {

    private String category;
    private String description;
    private String title;
    private double lat;
    private double lon;

    public UserSubmittedResource(){}

    public UserSubmittedResource(JSONObject obj){
        try{
            this.category = "user-submitted";
            this.description = obj.getString("location_desc");
            this.title = obj.getString("location_name");
            this.lat = obj.getDouble("location_lat");
            this.lon = obj.getDouble("location_lon");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public UserSubmittedResource(String description, double lat, double lon, String title){
        this.category = "user-submitted";
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String toString(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("location_name",title);
            jo.put("location_lat",lat);
            jo.put("location_lon",lon);
            jo.put("location_category",category);
            jo.put("location_desc",description);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return jo.toString();
    }
}
