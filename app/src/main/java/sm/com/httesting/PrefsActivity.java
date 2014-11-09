package sm.com.httesting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PrefsActivity extends Activity{

    SharedPreferences prefs;
    private Context ctxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        ctxt = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = prefs.getString(ChatFragment.CHAT_USERNAME,"Anonymous");
        final Button buttonChangeUsername = (Button)findViewById(R.id.button_change_username);
        buttonChangeUsername.setText("Change chat username ("+username+")");
        buttonChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OUTPUT", "onCLick has occurred");
                final EditText editTextUsername = new EditText(ctxt);
                editTextUsername.setText(username);
                new AlertDialog.Builder(ctxt)
                        .setTitle("Change username")
                        .setView(editTextUsername)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newUsername = editTextUsername.getText().toString();
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(ChatFragment.CHAT_USERNAME,newUsername);
                                Toast.makeText(ctxt,"New username: "+newUsername,Toast.LENGTH_SHORT).show();
                                editor.commit();
                                buttonChangeUsername.setText("Change chat username ("+newUsername+")");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setView(editTextUsername)
                        .create().show();
            }
        });
    }
}
