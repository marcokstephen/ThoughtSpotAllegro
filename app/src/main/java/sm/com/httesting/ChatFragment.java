package sm.com.httesting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatFragment extends Fragment {

    public static final String CHAT_USERNAME = "sm.com.httesting.CHAT_USERNAME";
    private ListView listView;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chatroom, container, false);

        final EditText chatEditText = (EditText)rootView.findViewById(R.id.edittext_chat_message);
        Button sendButton = (Button)rootView.findViewById(R.id.button_send_message);
        listView = (ListView)rootView.findViewById(R.id.listview_chatroom);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatEditText.getText().toString().equals("")){
                    String message = chatEditText.getText().toString();
                    chatEditText.setText("");

                    ChatMessage cm = new ChatMessage(username,message);
                    MyActivity.chatRef.push().setValue(cm);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = prefs.getString(CHAT_USERNAME,"Anonymous");
        getActivity().getActionBar().setTitle("Name: "+username);
    }

    @Override
    public void onStart(){
        super.onStart();
        final ChatroomListAdapter cla = new ChatroomListAdapter(getActivity(),MyActivity.chatRef.limitToFirst(50));
        listView.setAdapter(cla);
        cla.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                final int posn = cla.getCount() - 1;
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(posn);
                    }
                });
            }
        });
    }
}
