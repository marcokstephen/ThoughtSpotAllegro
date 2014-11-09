package sm.com.httesting;

/**
 * Created by Bazhunga on 11/8/2014.
 */
public class NewLocation {
    private String location_comment;
    private String location_name;
    public NewLocation(String comment, String name) {
        location_comment = comment;
        location_name = name;
    }

    public String getLocation_comment() {
        return location_comment;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public void setLocation_comment(String location_comment) {
        this.location_comment = location_comment;
    }
}
