package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class new_business_screen extends AppCompatActivity {

    void sign_up(String username, String password, String name)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("kind", "business");
        user.put("name", name);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null)
                {
                    Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                    startActivity(intent);
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
        setContentView(R.layout.activity_new_business_screen);
    }

    public void onBusinessLogClick(View view){

        // GO BACK TO THE SAME ACTIVITY

        //Intent i = new Intent(this, business_orders__screen.class);
        final EditText userNameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText businessNameInput = (EditText) findViewById(R.id.businessNameInput);
        final EditText passInput = (EditText) findViewById(R.id.passwordInput);
        //final EditText businessAdressInput = (EditText) findViewById(R.id.businessAdressInput);
        String name = userNameInput.getText().toString();
        String pass = passInput.getText().toString();
        String busiName = businessNameInput.getText().toString();

        sign_up(name, pass, busiName);
        //String busiAdd =  businessAdressInput.getText().toString();
        //i.putExtra("userName", name);
        //i.putExtra("password", pass);
        //i.putExtra("businessName", busiName);
        //i.putExtra("businessAdress", busiAdd);

        // need to check the validity of the password

        //startActivity(i);
    }

}
