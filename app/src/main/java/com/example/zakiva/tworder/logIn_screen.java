package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class logIn_screen extends AppCompatActivity {

    void sign_in(String username, String password)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    String s = (String) ParseUser.getCurrentUser().get("kind");
                    if (s.equals("business")) {
                        //what happens if sign in succeeded for business user
                        Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                        startActivity(intent);
                    } else {
                        //what happens if sign in succeeded for customer user
                        //Intent intent = new Intent(getBaseContext(), Main22Activity.class);
                        //startActivity(intent);
                    }
                } else {
                    //what happens if sign in failed
                    //TextView textView3 = (TextView) findViewById(R.id.textView3);
                    //textView3.setText("Failed!!!");
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

    }
}
