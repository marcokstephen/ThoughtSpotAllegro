package sm.com.httesting;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends FragmentActivity {

    FragmentTransaction transaction;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    public static String[] sectionTitles = {"All Categories","    Legal & Financial","    Health & Social",
            "    Recreation & Culture","    Family & Friends","    Spirituality & Wellbeing",
            "    Work & School","    Sex & Relationships","Suggest a Place","Favourites"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        transaction = getFragmentManager().beginTransaction();
        Fragment fragment = ResourceListFragment.newInstance(0);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, sectionTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = getFragmentManager().beginTransaction();
                Fragment fragment = ResourceListFragment.newInstance(i);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                //Changing the title of the action bar at the top of the app
                //Accounting for the spaces that we added in the array elements
                String newTitle = sectionTitles[i];
                if (i>0&&i<8) newTitle = newTitle.substring(4);
                getActionBar().setTitle(newTitle);
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
