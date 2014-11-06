package sm.com.httesting;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends ActionBarActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBar actionBar;
    public static String[] sectionTitles = {"All Categories","    Legal & Financial","    Health & Social",
            "    Recreation & Culture","    Family & Friends","    Spirituality & Wellbeing",
            "    Work & School","    Sex & Relationships","Suggest a Place","Favourites"};

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
                Fragment fragment = ResourceListFragment.newInstance(i);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();

                //Changing the title of the action bar at the top of the app
                //Accounting for the spaces that we added in the array elements
                String newTitle = sectionTitles[i];
                if (i>0&&i<8) newTitle = newTitle.substring(4);
                actionBar.setTitle(newTitle);

                mDrawerList.setItemChecked(i, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
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
