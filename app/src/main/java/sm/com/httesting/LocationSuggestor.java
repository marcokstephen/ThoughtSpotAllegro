package sm.com.httesting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class LocationSuggestor extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Context ctxt;
    private UserSubmittedResource userResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_suggestor);
        ctxt = this;
        setUpMapIfNeeded();
        Toast.makeText(this,getResources().getString(R.string.toast_map_instructions),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        final Context ctxt = this;
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(43.653, -79.383), 14.0f) );
        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Extract info from lat lng
                //Store this info temporarily in class object suggestedLocation

                final double [] latlng = {latLng.latitude, latLng.longitude};
                final Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latlng[0], latlng[1]))
                                .title("New suggestion")
                                .draggable(true)
                );
                AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
                LayoutInflater dli = (LayoutInflater) ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mapPopup = dli.inflate(R.layout.map_popup,null);
                builder.setView(mapPopup)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //When the user presses submit, the entered fields (name and description) get recorded into the class
                                //UserSubmittedResource

                                NetworkCall nc = new NetworkCall();
                                //Note: Must give context of the findViewById because there may be many things with that id et_location
                                EditText submitted_location = (EditText)mapPopup.findViewById(R.id.et_location);
                                EditText submitted_comments = (EditText)mapPopup.findViewById(R.id.et_comments);
                                String name = submitted_location.getText().toString(); //Grabs user entered information
                                String description = submitted_comments.getText().toString();
                                userResource = new UserSubmittedResource(description,latlng[0],latlng[1],name); //Store into class instance
                                dialog.dismiss(); //Gets rid of the dialog

                                //Logging for successful recording
                                Log.d("DESCRIPTION: ", description);
                                Log.d("LATITUDE: ", String.valueOf(latlng[0]));
                                Log.d("LONGITUDE: ", String.valueOf(latlng[1]));
                                Log.d("NAME: ", name);

                                new NetworkCall().execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                marker.remove();
                            }
                        });
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();

            }
        });
    }
    public class NetworkCall extends AsyncTask<Void,Void,Void> {
        //Database call to insert user suggested location
        //Location, name, latitude, longitude, category, address, city, website, phone
        //Location: User Provided
        //Name: User Provided
        //Description: User Provided
        //Latitude and Longitude: Grabbed from user pointed
        //Category: "user-submitted"
        //Address, city, website and phone: IDK
        @Override
        protected Void doInBackground(Void... params) {
            //post
            //base_url + /api/GetCategoryLocation
            //post_fields: location_category

            String url = getResources().getString(R.string.base_url) + "/api/AddNewLocation";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try{
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                //Adding name-value pairs as arguments for the post request
                nameValuePairList.add(new BasicNameValuePair("location_name",userResource.getTitle()));
                nameValuePairList.add(new BasicNameValuePair("location_desc",userResource.getDescription()));
                nameValuePairList.add(new BasicNameValuePair("location_lat",String.valueOf(userResource.getLat())));
                nameValuePairList.add(new BasicNameValuePair("location_lon",String.valueOf(userResource.getLon())));
                nameValuePairList.add(new BasicNameValuePair("location_category",userResource.getCategory()));
                //Sets parameters for the post request
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));

                //Receive a response indicating a message for failure or success
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String output = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                //Log the output
                Log.d("OUTPUT",output);
            } catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
        //Function that reads in stuff from the stream and creates a built string
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
}
