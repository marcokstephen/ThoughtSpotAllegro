package sm.com.httesting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ResourceListFragment extends Fragment {

    private static final String ARG_CATEGORY_NUMBER = "category_number";
    private int category;
    private ListView resoureListView;

    public static ResourceListFragment newInstance(int category){
        ResourceListFragment rlf = new ResourceListFragment();
        Bundle args = new Bundle(1);
        args.putInt(ARG_CATEGORY_NUMBER,category);
        rlf.setArguments(args);
        return rlf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        View rootView = inflater.inflate(R.layout.fragment_listing,viewGroup,false);
        category = getArguments().getInt(ARG_CATEGORY_NUMBER);
        resoureListView = (ListView)rootView.findViewById(R.id.listview_resources);

        TextView tvsample = (TextView)rootView.findViewById(R.id.textview_sample);
        tvsample.setText(MyActivity.sectionTitles[category]);

        return rootView;
    }
}
