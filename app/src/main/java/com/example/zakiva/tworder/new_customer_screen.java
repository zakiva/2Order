package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class new_customer_screen extends AppCompatActivity {

    void sign_up(String username, String password)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("kind", "customer");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null)
                {
                    //Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                    //startActivity(intent);
                }
                else
                {
                    //what happens if sign up failed
                    //TextView textView3 = (TextView) findViewById(R.id.textView3);
                    //textView3.setText("Failed!!!");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_screen);
    }

    public void onCustomerLogClick(View view){

    }
}
