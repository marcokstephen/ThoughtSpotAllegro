package sm.com.httesting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatroomListAdapter extends BaseAdapter {

    private Context c;
    private LayoutInflater inflater;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private Query chatRef;

    public ChatroomListAdapter(Context c, Query ref){
        this.c = c;
        this.chatRef = ref;
        this.inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage cm = dataSnapshot.getValue(ChatMessage.class);
                messageList.add(cm);
                notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.chat_message_list_item,parent,false);
        }
        TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_author);
        TextView messageTextView = (TextView)convertView.findViewById(R.id.textview_message);

        authorTextView.setText(messageList.get(position).getAuthor());
        messageTextView.setText(messageList.get(position).getMessage());

        return convertView;
    }
}
