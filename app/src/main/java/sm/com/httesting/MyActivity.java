package sm.com.httesting;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

public class MyActivity extends FragmentActivity {

    public static Firebase ref;
    public static Firebase chatRef;
    DrawerLayout mDrawerLayout;
    public static Location currentLocation;
    ListView mDrawerList;
    ActionBar actionBar;
    ActionBarDrawerToggle mDrawerToggle;
    public static String[] sectionTitles = {"All Categories","    Legal & Financial","    Health & Social",
            "    Recreation & Culture","    Family & Friends","    Spirituality & Wellbeing",
            "    Work & School","    Sex & Relationships","Suggest a Place","Favourites","Chatrooms"};
    Context ctxt = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        ref = new Firebase(getString(R.string.firebase_url));
        chatRef = ref.child("chat");
        setContentView(R.layout.activity_my);
        actionBar = getActionBar();
        currentLocation = getLocationObject(Criteria.ACCURACY_LOW);

        //Setting the initial scene: the "All Categories" view
        actionBar.setTitle(sectionTitles[0]);
        Fragment fragment = ResourceListFragment.newInstance(0);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();

        //Assigning values for the drawerLayout and its corresponding ListView
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, sectionTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //We change the main fragment depending on which category the user picks to see

                mDrawerList.setItemChecked(i, true);
                mDrawerLayout.closeDrawer(mDrawerList);
                Fragment fragment;
                if (i == 8) {
                    //this is the "Suggest a Place" fragment, we need to use a map
                    //instead of the regular result
                    fragment = new SuggestPlaceFragment();
                    Intent intent = new Intent(ctxt, LocationSuggestor.class);
                    startActivity(intent);

                } else if (i == 10) {
                    //This is the "Chat" fragment, we use a different fragment than normal
                    fragment = new ChatFragment();
                } else {
                    fragment = ResourceListFragment.newInstance(i);
                }
                if (i != 8) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
                //Changing the title of the action bar at the top of the app
                //Accounting for the spaces that we added in the array elements
                String newTitle = sectionTitles[i];
                if (i>0&&i<8) newTitle = newTitle.substring(4);
                actionBar.setTitle(newTitle);

            }
        });

        //Allowing the NavigationDrawer to be called by pressing the title
        //in the action bar, for better accessibility
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer,R.string.hello_world,R.string.hello_world);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public Location getLocationObject(int accuracy){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setBearingRequired(false);
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,false));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //required to make the button open the NavDrawer
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.Help){
            Intent intent = new Intent(ctxt, HelpScreen.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
