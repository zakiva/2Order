package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
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
                    final Button b = (Button) findViewById(R.id.logButton);
                    b.setEnabled(true);
                    alertToast(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        final Button b = (Button) findViewById(R.id.logButton);
        b.setEnabled(true);

        final TextView head = (TextView) findViewById(R.id.usernameText);
        final EditText phoneOrEmail = (EditText) findViewById(R.id.usernameInput);

        Intent i = getIntent();
        if (i != null) {
            String s = i.getStringExtra("from");
            if (s.equals("customer")) {
                head.setText("Phone Number");
                phoneOrEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else{
                head.setText("Email");
            }
        }

        EditText email_edit = (EditText) findViewById(R.id.editText5);
        email_edit.setVisibility(View.INVISIBLE);
        TextView email_text = (TextView) findViewById(R.id.textView22);
        email_text.setVisibility(View.INVISIBLE);
        Button button = (Button) findViewById(R.id.button13);
        button.setVisibility(View.INVISIBLE);

    }

    public void forgotClicked(View view){
        EditText email_edit = (EditText) findViewById(R.id.editText5);
        email_edit.setVisibility(View.VISIBLE);
        TextView email_text = (TextView) findViewById(R.id.textView22);
        email_text.setVisibility(View.VISIBLE);
        Button button = (Button) findViewById(R.id.button13);
        button.setVisibility(View.VISIBLE);
    }

    public void restoreClicked(View view){
        EditText email_edit = (EditText) findViewById(R.id.editText5);
        String email = email_edit.getText().toString();

        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    TextView email_text = (TextView) findViewById(R.id.textView22);
                    //email_text.setText("Password sent to:");
                } else {
                    alertToast("Invalid email address");
                }
            }
        });
    }

    public void onLogInClick(View view){
        final Button b = (Button) findViewById(R.id.logButton);
        b.setEnabled(false);

        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        String username = usernameInput.getText().toString();
        username = username.replaceAll("\\s","");
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
