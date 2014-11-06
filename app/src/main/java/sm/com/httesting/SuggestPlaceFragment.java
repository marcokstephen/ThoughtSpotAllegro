package sm.com.httesting;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class SuggestPlaceFragment extends Fragment {

    private static GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suggestion, null);

        Fragment myMapFragment = new MyMapFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.mapfragment_container,myMapFragment)
                .addToBackStack(null)
                .commit();

        return rootView;
    }

    public static class MyMapFragment extends MapFragment {
        @Override
        public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2){
            View v = super.onCreateView(arg0,arg1,arg2);
            mMap = getMap();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6426,-79.3871), 12));
            return v;
        }
    }

    public static class PopulateMap extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //TODO: obtain markers using a network call
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //TODO: add markers to map
        }
    }
}
