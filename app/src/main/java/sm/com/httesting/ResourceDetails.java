package sm.com.httesting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//This activity gives further details about
//each listing returned by ThoughtSpot
public class ResourceDetails extends Activity {

    Resource resourceObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String object = bundle.getString(ResourceListFragment.INTENT_RESOURCE_ARG);
        resourceObject = new Resource(object);
        setContentView(R.layout.activity_resource_details);

        Button buttonWeb = (Button) findViewById(R.id.button_website);
        Button buttonCall = (Button) findViewById(R.id.button_call);
        Button buttonMap = (Button) findViewById(R.id.button_maps);

        if (resourceObject.get_location_website().equals("")){
            buttonWeb.setVisibility(View.GONE);
        } else {
            buttonWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = resourceObject.get_location_website();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resource_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
