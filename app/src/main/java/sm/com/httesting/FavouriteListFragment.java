package sm.com.httesting;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteListFragment extends Fragment {

    private ListView favouriteListView;
    private Context ctxt;
    private List<Resource> favouriteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        View rootView = inflater.inflate(R.layout.fragment_listing,viewGroup,false);
        favouriteListView = (ListView)rootView.findViewById(R.id.listview_resources);
        favouriteList = new ArrayList<Resource>();
        ctxt = getActivity();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        String favouriteString = prefs.getString(ResourceDetails.FAVOURITE_PLACES, "[]");
        try {
            JSONArray jsonArray = new JSONArray(favouriteString);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Resource res = new Resource(jsonObject.toString());
                favouriteList.add(res);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        ResourceListAdapter rla = new ResourceListAdapter(ctxt,favouriteList);
        favouriteListView.setAdapter(rla);

        return rootView;
    }

}
