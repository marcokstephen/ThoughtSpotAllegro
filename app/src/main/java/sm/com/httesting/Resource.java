package sm.com.httesting;

import org.json.JSONException;
import org.json.JSONObject;

public class Resource {

    private String location_name;
    private double location_lat;
    private double location_lon;
    private String category;
    private String address;
    private String city;
    private String phone;
    private String website;
    private int location_id;
    private String comments;
    private String description;

    //---- CONSTRUCTORS
    public Resource()
    {

    }

    public Resource(String inputString)
    {
        JSONObject jo = new JSONObject();

        try {
            jo = new JSONObject(inputString);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        try {
            this.set_location_name(jo.getString("location_name"));
        } catch (JSONException e)
        {
            this.set_location_name(null);
        }

        try {
            this.set_location_lat(jo.getDouble("location_lat"));
        } catch (JSONException e)
        {
            this.set_location_lat(0);
        }

        try {
            this.set_location_lon(jo.getDouble("location_lon"));
        } catch (JSONException e)
        {
            this.set_location_lon(0);
        }

        try {
            this.set_location_category(jo.getString("location_category"));
        } catch (JSONException e)
        {
            this.set_location_category(null);
        }

        try {
            this.set_location_address(jo.getString("location_address"));
        } catch (JSONException e)
        {
            this.set_location_address(null);
        }

        try {
            this.set_location_city(jo.getString("location_city"));
        } catch (JSONException e)
        {
            this.set_location_city(null);
        }

        try {
            this.set_location_phone(jo.getString("location_phone"));
        } catch (JSONException e)
        {
            this.set_location_phone(null);
        }

        try {
            this.set_location_website(jo.getString("location_website"));
        } catch (JSONException e)
        {
            this.set_location_website(null);
        }

        try {
            this.set_location_comments(jo.getString("location_number_comment"));
        }catch (JSONException e)
        {
            this.set_location_comments(null);
        }
        try {
            this.set_location_description(jo.getString("location_desc"));
        } catch (JSONException e){
            this.set_location_description(null);
        }

        try{
            this.set_location_id(jo.getInt("location_id"));
        } catch (JSONException e){
            this.set_location_id(0);
        }

    }

    // ----- GET METHODS
    public String get_location_name()
    {
        return location_name;
    }

    public double get_location_lat()
    {
        return location_lat;
    }

    public double get_location_lon()
    {
        return location_lon;
    }

    public String get_location_category()
    {
        return category;
    }

    public String get_location_address()
    {
        return address;
    }

    public String get_location_city()
    {
        return city;
    }

    public String get_location_phone()
    {
        return phone;
    }

    public String get_location_website()
    {
        return website;
    }

    public String get_location_comments()
    {
        return comments;
    }

    public String get_location_description() { return description; }

    public int get_location_id() { return location_id; }

    // -- SET METHODS
    public void set_location_name(String locationName)
    {
        this.location_name = locationName;
    }

    public void set_location_lat(double locationLat)
    {
        this.location_lat = locationLat;
    }

    public void set_location_lon(double locationLon)
    {
        this.location_lon = locationLon;
    }

    public void set_location_category(String cat)
    {
        this.category = cat;
    }

    public void set_location_address(String addr)
    {
        this.address = addr;
    }

    public void set_location_city(String ct)
    {
        this.city = ct;
    }

    public void set_location_phone(String phon)
    {
        this.phone = phon;
    }

    public void set_location_website(String web)
    {
        this.website = web;
    }

    public void set_location_comments(String comm)
    {
        this.comments=comm;
    }

    public void set_location_description(String description) { this.description=description; }

    public void set_location_id(int id) { this.location_id = id; }

    // toString METHOD
    public String toString()
    {
        JSONObject jo = new JSONObject();
        try {
            jo.put("location_name", this.get_location_name());
            jo.put("location_lat", this.get_location_lat());
            jo.put("location_lon", this.get_location_lon());
            jo.put("location_category", this.get_location_category());
            jo.put("location_address", this.get_location_address());
            jo.put("location_city", this.get_location_city());
            jo.put("location_phone", this.get_location_phone());
            jo.put("location_website", this.get_location_website());
            jo.put("location_number_comment", this.get_location_comments());
            jo.put("location_desc",this.get_location_description());
            jo.put("location_id",this.get_location_id());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jo.toString();
    }
}
