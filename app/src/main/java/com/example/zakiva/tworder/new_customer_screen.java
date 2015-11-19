package com.example.zakiva.tworder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                if (e == null) {
                    Intent intent = new Intent(getBaseContext(), customer_orders_screen.class);
                    startActivity(intent);
                } else {
                    alertToast("Sign-up failed");
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

        final EditText phoneInput = (EditText) findViewById(R.id.phoneInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        final EditText rePasswordInput = (EditText) findViewById(R.id.repasswordInput);

        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String rePassword = rePasswordInput.getText().toString();


        // Set Rules to inputs
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_custom_alert,
                (ViewGroup) findViewById(R.id.my_custom_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.alertText);
        if (password.equals(rePassword)) {
            sign_up(phone, password);
        } else{
            alertToast("Please retype your password");
        }

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
