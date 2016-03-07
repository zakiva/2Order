package com.example.zakiva.tworder;

import android.content.Intent;
import android.graphics.Color;
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

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class new_customer_screen extends AppCompatActivity {

    void sign_up(String username, String password, String email) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("phone", username);
        user.put("kind", "customer");
        user.put("wants_notification", "yes");
        user.put("is_signed_in", "yes");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("notification_id", ParseUser.getCurrentUser().getString("phone"));
                    installation.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            Intent intent = new Intent(getBaseContext(), customer_orders.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    final Button b = (Button) findViewById(R.id.login1);
                    b.setEnabled(true);
                    alertToast(e.getMessage().replace("username", "phone number"));
                    //Log.d("aaaa:", e.toString());
                    //alertToast("Sign-up failed");

                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_screen);

        final Button b = (Button) findViewById(R.id.login1);
        b.setEnabled(true);
    }

    public void onCustomerLogClick(View view){
        final Button b = (Button) findViewById(R.id.login1);
        b.setEnabled(false);

        final EditText phoneInput = (EditText) findViewById(R.id.phoneInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        final EditText rePasswordInput = (EditText) findViewById(R.id.repasswordInput);
        final EditText emailInput = (EditText) findViewById(R.id.emailInput);

        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String rePassword = rePasswordInput.getText().toString();
        String email = emailInput.getText().toString();

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_custom_alert,
                (ViewGroup) findViewById(R.id.my_custom_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.alertText);
        if (password.equals(rePassword)) {
            if (!phone.equals("") && !email.equals("") && !password.equals("") && !rePassword.equals("")) {
                sign_up(phone, password, email);
            } else{
                b.setEnabled(true);
                alertToast("Please fill all fields");
            }
        } else{
            //final Button b = (Button) findViewById(R.id.login1);
            b.setEnabled(true);
            passwordInput.setTextColor(Color.parseColor("RED"));
            rePasswordInput.setTextColor(Color.parseColor("RED"));
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

    public void facebook_login(View view){
        Intent i = new Intent(this, facebook_login.class);
        startActivity(i);
    }

    public void allReadyHaveAcountClick(View view){
        Intent i = new Intent(this, logIn_screen.class);
        i.putExtra("from", "customer");
        startActivity(i);
    }

    public void new_account_clicked(View view) {
        Button b = (Button) findViewById(R.id.button9);
        b.setVisibility(View.GONE);
        b = (Button) findViewById(R.id.button7);
        b.setVisibility(View.GONE);
        TextView t = (TextView) findViewById(R.id.textView15);
        t.setVisibility(View.GONE);

        b = (Button) findViewById(R.id.login1);
        b.setVisibility(View.VISIBLE);
        t = (TextView) findViewById(R.id.PhoneText);
        t.setVisibility(View.VISIBLE);
        t = (TextView) findViewById(R.id.repasswordText);
        t.setVisibility(View.VISIBLE);
        t = (TextView) findViewById(R.id.passwordText);
        t.setVisibility(View.VISIBLE);
        t = (TextView) findViewById(R.id.emailText);
        t.setVisibility(View.VISIBLE);
        EditText e = (EditText) findViewById(R.id.phoneInput);
        e.setVisibility(View.VISIBLE);
        e = (EditText) findViewById(R.id.repasswordInput);
        e.setVisibility(View.VISIBLE);
        e = (EditText) findViewById(R.id.passwordInput);
        e.setVisibility(View.VISIBLE);
        e = (EditText) findViewById(R.id.emailInput);
        e.setVisibility(View.VISIBLE);

    }
}
