package sm.com.httesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class SearchActivity extends Activity {

    private List<Resource> resourceList;
    private Context ctxt;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ctxt = this;

        final EditText editText = (EditText)findViewById(R.id.edittext_search);
        Button searchButton = (Button)findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = editText.getText().toString();
                new SearchQuery(search).execute();
            }
        });

        lv = (ListView) findViewById(R.id.listview_search_results);
    }

    public class SearchQuery extends AsyncTask<Void,Void,Void>{

        private String search_keyword;

        public SearchQuery(String search){
            search_keyword = search;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ResourceListAdapter rla = new ResourceListAdapter(ctxt,resourceList);
            lv.setAdapter(rla);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ctxt, ResourceDetails.class);
                    intent.putExtra(ResourceListFragment.INTENT_RESOURCE_ARG, resourceList.get(position).toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            resourceList = new ArrayList<Resource>();
            String url = getResources().getString(R.string.base_url) + "/api/search";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(2);
                nameValuePairList.add(new BasicNameValuePair("search", search_keyword));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String output = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                Log.d("OUTPUT", output);

                JSONObject jsonObject = new JSONObject(output);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Resource resource = new Resource(jsonArray.getJSONObject(i).toString());
                    resourceList.add(resource);
                }
            } catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }
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
