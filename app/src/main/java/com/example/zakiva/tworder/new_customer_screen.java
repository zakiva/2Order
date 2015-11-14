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

    void sign_up(String username, String password, final String kind)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("kind", kind);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null)
                {
                    if (kind.equals("business"))
                    {
                        //what happens if sign up succeeded for business user
                        Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                        startActivity(intent);
                    }
                    else
                    {
                        //what happens if sign up succeeded for business user
                        //Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                        //startActivity(intent);
                    }
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
        EditText editText = (EditText)findViewById(R.id.phoneInput);
        String username = editText.getText().toString();

        EditText editText2 = (EditText)findViewById(R.id.passwordInput);
        String password = editText2.getText().toString();

        EditText editText3 = (EditText)findViewById(R.id.repasswordInput);
        String repassword = editText3.getText().toString();

        if (repassword.equals(password)){
            sign_up(username, password, "business");
        }
    }
}
