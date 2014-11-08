package sm.com.httesting;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceListFragment extends Fragment {

    private static final String ARG_CATEGORY_NUMBER = "category_number";
    public static final String INTENT_RESOURCE_ARG = "sm.com.httesting.INTENT_RESORUCE_ARG";
    private int category;
    private ListView resourceListView;
    private List<Resource> categoryData;
    private String search_keyword = "";

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
        categoryData = new ArrayList<Resource>();
        resourceListView = (ListView)rootView.findViewById(R.id.listview_resources);

        new ReceiveData().execute();

        return rootView;
    }

    private class ReceiveData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            categoryData = new ArrayList<Resource>();
            //post
            //base_url + /api/GetCategoryLocation
            //post_fields: location_category

            //String url = getResources().getString(R.string.base_url) + "/api/GetCategoryLocation";
            String url = "http://104.131.180.180:8080/api/GetCategoryLocation";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try{
                if (category == 1){
                    search_keyword = "Legal";
                } else if (category == 2){
                    search_keyword = "Health";
                } else if (category == 3){
                    search_keyword = "Recreation";
                } else if (category == 4) {
                    search_keyword = "Family";
                } else if (category == 5) {
                    search_keyword = "Spirituality";
                } else if (category == 6) {
                    search_keyword = "School";
                } else if (category == 7) {
                    search_keyword = "Sex";
                } else {
                    search_keyword = "";
                }
                Log.d("OUTPUT", "Search keyword is " + search_keyword);
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                nameValuePairList.add(new BasicNameValuePair("location_category",search_keyword));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String output = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                Log.d("OUTPUT",output);

                JSONObject jsonObject = new JSONObject(output);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < Math.min(10, jsonArray.length()); i++){
                    Resource resource = new Resource(jsonArray.getJSONObject(i).toString());
                    categoryData.add(resource);
                }

            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
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
                    intent.putExtra(INTENT_RESOURCE_ARG,categoryData.get(position).toString());
                    startActivity(intent);
                }
            });
        }

        public StringBuilder inputStreamToString(InputStream is){
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb;
        }
    }
}
