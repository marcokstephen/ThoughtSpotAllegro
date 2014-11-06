package sm.com.httesting;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends FragmentActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBar actionBar;
    ActionBarDrawerToggle mDrawerToggle;
    public static String[] sectionTitles = {"All Categories","    Legal & Financial","    Health & Social",
            "    Recreation & Culture","    Family & Friends","    Spirituality & Wellbeing",
            "    Work & School","    Sex & Relationships","Suggest a Place","Favourites","Chatrooms"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        actionBar = getActionBar();

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
                } else if (i == 10) {
                    //This is the "Chat" fragment, we use a different fragment than normal
                    fragment = new ChatFragment();
                } else {
                    fragment = ResourceListFragment.newInstance(i);
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();

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
        return super.onOptionsItemSelected(item);
    }
}
