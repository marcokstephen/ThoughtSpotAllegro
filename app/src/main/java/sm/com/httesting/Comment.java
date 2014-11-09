package sm.com.httesting;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {

    private String text;
    private int upvotes;
    private int comment_id;

    public Comment(String text, int upvotes, int id){
        this.text = text;
        this.upvotes = upvotes;
        this.comment_id = id;
    }

    public Comment(JSONObject comment){
        try {
            this.text = comment.getString("comment_text");
            this.upvotes = comment.getInt("comment_upvotes");
            this.comment_id = comment.getInt("comment_id");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getText(){
        return text;
    }
    public int getUpvotes(){
        return upvotes;
    }
    public int get_comment_id(){
        return comment_id;
    }
    public String toString(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("comment_text", text);
            jo.put("comment_upvotes", upvotes);
            jo.put("comment_id",comment_id);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return jo.toString();
    }

}
