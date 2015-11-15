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

        // GET ALL THE INPUTS FROM THE USER
        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText businessNameInput = (EditText) findViewById(R.id.businessNameInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        final EditText repasswordInput = (EditText) findViewById(R.id.repasswordInput);
        final EditText businessAddressInput = (EditText) findViewById(R.id.businessAddressInput);

        String name = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String rePassword = repasswordInput.getText().toString();
        String businessName = businessNameInput.getText().toString();
        String business_address =  businessAddressInput.getText().toString();

        // need to check the password and repassword
            sign_up(name, password, businessName);

        Intent i = new Intent(this, business_orders__screen.class);
        startActivity(i);
    }

}
