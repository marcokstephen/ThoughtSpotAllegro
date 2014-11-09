package sm.com.httesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//This activity gives further details about
//each listing returned by ThoughtSpot
public class ResourceDetails extends Activity {

    Resource resourceObject;
    private Context ctxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String object = bundle.getString(ResourceListFragment.INTENT_RESOURCE_ARG);
        resourceObject = new Resource(object);
        setContentView(R.layout.activity_resource_details);
        ctxt = this;

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

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_resource_details);
        LayoutInflater lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] commentsArray = {"This is the best place ever","this place sucks","this place is alright","this place gave me cancer and aids and I will never be the same","what you just said was one of the most insanely idiotic things i have ever heard, at no point in your rambling incoherent speech did you say anything that could resemble a rational thought. everyone in this room is now dumber for having listened to you. i award you no points, and may god have mercy on your soul","GET REKKED"};

        for (int i = 0; i < commentsArray.length; i++){
            View commentView = lf.inflate(R.layout.resource_comment_item,null);
            TextView comment = (TextView)commentView.findViewById(R.id.textview_user_comment);
            final Button helpfulButton = (Button)commentView.findViewById(R.id.button_mark_helpful);
            helpfulButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctxt,"Helpful!",Toast.LENGTH_SHORT).show();
                    helpfulButton.setEnabled(false);
                }
            });
            comment.setText(commentsArray[i]);
            linearLayout.addView(commentView);
        }
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
