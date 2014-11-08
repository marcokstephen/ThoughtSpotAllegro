package sm.com.httesting;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResourceListFragment extends Fragment {

    private static final String ARG_CATEGORY_NUMBER = "category_number";
    private int category;
    private ListView resourceListView;
    private List<Resource> categoryData;

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
        resourceListView = (ListView)rootView.findViewById(R.id.listview_resources);
        new ReceiveData().execute();

        return rootView;
    }

    private class ReceiveData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            categoryData = new ArrayList<Resource>();
            //post
            //
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ResourceListAdapter rla = new ResourceListAdapter(getActivity(),categoryData);
            resourceListView.setAdapter(rla);
            resourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), ResourceDetails.class);
                    startActivity(intent);
                }
            });
        }
    }
}
