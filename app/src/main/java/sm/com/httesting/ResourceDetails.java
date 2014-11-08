package sm.com.httesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        TextView tv = (TextView)findViewById(R.id.textview_resource_details);
        tv.setText(object);
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
