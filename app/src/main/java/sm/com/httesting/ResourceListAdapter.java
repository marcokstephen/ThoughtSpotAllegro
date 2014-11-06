package sm.com.httesting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ResourceListAdapter extends BaseAdapter {

    private Context c;
    private LayoutInflater inflater;
    private List<Object> data;

    public ResourceListAdapter(Context ctxt, List<Object> data){
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
        TextView tv = (TextView)convertView.findViewById(R.id.textview_resource_name);
        tv.setText(getItem(position).toString());

        return convertView;
    }
}
