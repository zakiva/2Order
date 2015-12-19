package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class logIn_screen extends AppCompatActivity {

    void sign_in(String username, String password)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(final ParseUser user, ParseException e) {
                if (user != null) {
                    String s = (String) ParseUser.getCurrentUser().get("kind");
                    if (s.equals("business")) {
                        //what happens if sign in succeeded for business user
                        Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                        startActivity(intent);
                    } else {
                        //what happens if sign in succeeded for customer user
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("notification_id", ParseUser.getCurrentUser().getString("phone"));
                        installation.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                user.put("is_signed_in", "yes");
                                user.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        Intent intent = new Intent(getBaseContext(), customer_orders.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                } else {
                    alertToast("Sign-in failed");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
    }

    public void onLogInClick(View view){

        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        sign_in(username, password);
    }

    public void alertToast(String alert){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_custom_alert,
                (ViewGroup) findViewById(R.id.my_custom_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.alertText);

        text.setText(alert);
        Toast toast2 = new Toast(getApplicationContext());
        toast2.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast2.setDuration(Toast.LENGTH_LONG);
        toast2.setView(layout);
        toast2.show();
    }
}
