package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class facebook_login extends AppCompatActivity {
    //do nothing

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        final List<String> permissions = new ArrayList<String>();

        EditText phone_number_fill = (EditText) findViewById(R.id.editText2);
        phone_number_fill.setVisibility(View.INVISIBLE);
        TextView phone_number = (TextView) findViewById(R.id.textView6);
        phone_number.setVisibility(View.INVISIBLE);
        Button ok_button = (Button) findViewById(R.id.button5);
        ok_button.setVisibility(View.INVISIBLE);

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    EditText phone_number_fill = (EditText) findViewById(R.id.editText2);
                    phone_number_fill.setVisibility(View.VISIBLE);
                    TextView phone_number = (TextView) findViewById(R.id.textView6);
                    phone_number.setVisibility(View.VISIBLE);
                    Button ok_button = (Button) findViewById(R.id.button5);
                    ok_button.setVisibility(View.VISIBLE);
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("notification_id", ParseUser.getCurrentUser().getString("phone"));
                    installation.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("is_signed_in", "yes");
                            user.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    Intent i = new Intent(getBaseContext(), customer_orders.class);
                                    startActivity(i);
                                }
                            });
                        }
                    });
                }
            }
        });
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

    public void onOkClick(View view) {
        final Button b = (Button) findViewById(R.id.button5);
        b.setEnabled(false);

        final EditText phoneInput = (EditText) findViewById(R.id.editText2);
        final String phone = phoneInput.getText().toString();
        if (phone.equals("")){
            alertToast("Please insert a valid phone number");
            //final Button b = (Button) findViewById(R.id.button5);
            b.setEnabled(true);
        }
        else{
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("notification_id", phone);
            installation.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("phone", phone);
                    user.put("kind", "customer");
                    user.put("wants_notification", "yes");
                    user.put("is_signed_in", "yes");
                    user.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            Intent i = new Intent(getBaseContext(), customer_orders.class);
                            startActivity(i);
                        }
                        });}
                    });
    }}
}
