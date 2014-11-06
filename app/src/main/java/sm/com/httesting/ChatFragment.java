package sm.com.httesting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatFragment extends Fragment {

    public static final String CHAT_USERNAME = "sm.com.httesting.CHAT_USERNAME";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatroom, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = prefs.getString(CHAT_USERNAME,"Anonymous");
        getActivity().getActionBar().setTitle(username);

        return rootView;
    }
}
