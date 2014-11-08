package sm.com.httesting;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ResourceListAdapter extends BaseAdapter {

    private Context c;
    private LayoutInflater inflater;
    private List<Resource> data;

    public ResourceListAdapter(Context ctxt, List<Resource> data){
        this.data = data;
        this.c = ctxt;
        inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.resouce_list_item,null);
        }
        TextView resourceName = (TextView)convertView.findViewById(R.id.textview_resource_name);
        TextView resourceAddress = (TextView) convertView.findViewById(R.id.textview_resource_address);
        TextView resourceCity = (TextView)convertView.findViewById(R.id.textview_resource_city);
        TextView resourcePhone = (TextView)convertView.findViewById(R.id.textview_resource_phone);
        TextView resourceWebsite = (TextView)convertView.findViewById(R.id.textview_resource_website);
        TextView resourceComments = (TextView)convertView.findViewById(R.id.textview_resource_counter);

        Resource currentResource = data.get(position);
        resourceName.setText(currentResource.get_location_name());
        resourceAddress.setText(currentResource.get_location_address());
        resourceCity.setText(currentResource.get_location_city());
        resourcePhone.setText(currentResource.get_location_phone());

        resourceComments.setText(currentResource.get_location_comments() + " comments");

        if (resourceName.getText().toString().equals("")) resourceName.setVisibility(View.GONE);
        if (resourceAddress.getText().toString().equals("")) resourceAddress.setVisibility(View.GONE);
        if (resourceCity.getText().toString().equals("")) resourceCity.setVisibility(View.GONE);
        resourceWebsite.setVisibility(View.GONE);
        if (resourcePhone.getText().toString().equals("")) resourcePhone.setVisibility(View.GONE);


        return convertView;
    }
}
