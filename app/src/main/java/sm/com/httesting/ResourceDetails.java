package sm.com.httesting;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//This activity gives further details about
//each listing returned by ThoughtSpot
public class ResourceDetails extends Activity {

    Resource resourceObject;
    private Context ctxt;

    private String commentText;
    private int commentResourceId;

    //for comment requests
    private int commentLocationId;

    LinearLayout linearLayout;
    LayoutInflater lf;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String object = bundle.getString(ResourceListFragment.INTENT_RESOURCE_ARG);
        resourceObject = new Resource(object);
        setContentView(R.layout.activity_resource_details);
        ctxt = this;

        TextView textViewDetails = (TextView)findViewById(R.id.textview_resource_details_description);
        textViewDetails.setText(resourceObject.get_location_description());
        ActionBar actionBar = getActionBar();
        String locationName = resourceObject.get_location_name();
        actionBar.setTitle(locationName);

        Button buttonWeb = (Button) findViewById(R.id.button_website);
        Button buttonCall = (Button) findViewById(R.id.button_call);
        Button buttonMap = (Button) findViewById(R.id.button_maps);
        Button buttonAddFavourite = (Button) findViewById(R.id.button_add_favourite);
        final Button buttonAddComment = (Button) findViewById(R.id.button_add_comment);

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(ctxt);
                new AlertDialog.Builder(ctxt)
                        .setTitle("Add Comment")
                        .setMessage("Add your thoughts about this place!")
                        .setView(editText)
                        .setPositiveButton("Submit",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                commentText = editText.getText().toString();
                                commentResourceId = resourceObject.get_location_id();
                                buttonAddComment.setEnabled(false);
                                new AddComment().execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .show();
            }
        });

        if (resourceObject.get_location_website().equals("")){
            buttonWeb.setVisibility(View.GONE);
        } else {
            buttonWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = resourceObject.get_location_website();
                    if (url.startsWith("www")){
                        url = "http://" + url;
                    } else if (url.startsWith("Http")){
                        url = (url.toLowerCase()).substring(0,1) + url.substring(1);
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }

        if (resourceObject.get_location_phone().equals("")){
            buttonCall.setVisibility(View.GONE);
        } else {
            buttonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + resourceObject.get_location_phone() ;
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                }
            });
        }

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = resourceObject.get_location_lat();
                double lng = resourceObject.get_location_lon();
                String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(mapIntent);
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_resource_details);
        lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        commentLocationId = resourceObject.get_location_id();
        new RetrieveComments().execute();
    }

    public class RetrieveComments extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            commentList = new ArrayList<Comment>();
            String url = getResources().getString(R.string.base_url) + "/api/GetComment";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                nameValuePairList.add(new BasicNameValuePair("comment_location_id", commentLocationId + ""));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String output = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                Log.d("COMMENT",output);

                JSONObject jsonObject = new JSONObject(output);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < Math.min(150, jsonArray.length()); i++){
                    Comment c = new Comment(jsonArray.getJSONObject(i));
                    commentList.add(c);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (int i = 0; i < commentList.size(); i++){
                final Comment currentComment = commentList.get(i);
                final String commentText = currentComment.getText();
                final int commentVotes = currentComment.getUpvotes();

                View commentView = lf.inflate(R.layout.resource_comment_item,null);
                TextView comment = (TextView)commentView.findViewById(R.id.textview_user_comment);
                final Button helpfulButton = (Button)commentView.findViewById(R.id.button_mark_helpful);
                helpfulButton.setText("Mark Helpful - "+commentVotes);

                helpfulButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ctxt,"Helpful!",Toast.LENGTH_SHORT).show();
                        helpfulButton.setText("Mark Helpful - "+(commentVotes+1));
                        helpfulButton.setEnabled(false);
                        new MarkCommentHelpful(currentComment.get_comment_id()).execute();
                    }
                });
                comment.setText(commentText);
                linearLayout.addView(commentView);
            }

        }
    }

    public class MarkCommentHelpful extends AsyncTask<Void,Void,Void>{

        private int comment_id;

        public MarkCommentHelpful(int comment_id){
            this.comment_id = comment_id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.base_url) + "/api/AddUpVote";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                nameValuePairList.add(new BasicNameValuePair("comment_id", comment_id+""));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                httpClient.execute(httpPost);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class AddComment extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.base_url) + "/api/AddComment";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                nameValuePairList.add(new BasicNameValuePair("comment_text", commentText));
                nameValuePairList.add(new BasicNameValuePair("comment_location_id", commentResourceId + ""));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                httpClient.execute(httpPost);
            }catch (IOException e){
                e.printStackTrace();
            }

            //TODO: Add to the list in realtime
            return null;
        }
    }

    public StringBuilder inputStreamToString(InputStream is){
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
}
