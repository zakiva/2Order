package com.example.zakiva.tworder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.view.ViewGroup;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import org.w3c.dom.Text;

public class new_business_screen extends AppCompatActivity {

    void sign_up(String username, String password, String name, String address) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username);
        user.put("kind", "business");
        user.put("days_alert", 3);
        user.put("Auto_orders_numbers", "no");
        user.put("orders_counter", 1);
        user.put("name", name);
        user.put("address", address);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseObject business = new ParseObject("Business");
                    business.put("user_id", ParseUser.getCurrentUser().getObjectId().toString());
                    business.put("new_notifications", 0);
                    business.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                                startActivity(intent);
                            } else {
                                alertToast("Sign-up failed");
                                final Button b = (Button) findViewById(R.id.login1);
                                b.setEnabled(true);
                                //what happens if sign up failed
                                //TextView textView3 = (TextView) findViewById(R.id.textView3);
                                //textView3.setText("Failed!!!");
                            }
                        }
                    });
                }
                else {
                    final Button b = (Button) findViewById(R.id.login1);
                    b.setEnabled(true);
                    alertToast(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_business_screen);

        final Button b = (Button) findViewById(R.id.login1);
        b.setEnabled(true);
    }

    public void onBusinessLogClick(View view) {
        final Button b = (Button) findViewById(R.id.login1);
        b.setEnabled(false);

        // GET ALL THE INPUTS FROM THE USER
        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText businessNameInput = (EditText) findViewById(R.id.businessNameInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        final EditText repasswordInput = (EditText) findViewById(R.id.repasswordInput);
        final EditText businessAddressInput = (EditText) findViewById(R.id.businessAddressInput);

        // usernameInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //  repasswordInput.setInputType(InputType.TYPE_CLASS_TEXT);
        // repasswordInput.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
        // repasswordInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // repasswordInput.setImeActionLabel("whatever", EditorInfo.IME_ACTION_DONE);


        String name = usernameInput.getText().toString();
        name = name.replaceAll("\\s","");
        String password = passwordInput.getText().toString();
        String rePassword = repasswordInput.getText().toString();
        String businessName = businessNameInput.getText().toString();
        String business_address = businessAddressInput.getText().toString();

        if (password.equals(rePassword)) {
            if (!name.equals("") && !businessName.equals("") && !business_address.equals("")) {
                sign_up(name, password, businessName, business_address);
            } else{
                alertToast("Please fill all fields");
                //final Button b = (Button) findViewById(R.id.login1);
                b.setEnabled(true);
            }
        } else {
            alertToast("Please retype your password");
            //final Button b = (Button) findViewById(R.id.login1);
            b.setEnabled(true);
        }

    }

    public void alertToast(String alert) {
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

    public void allReadyHaveAcountClick(View view) {
        Intent i = new Intent(this, logIn_screen.class);
        i.putExtra("from", "business");
        startActivity(i);
    }

}
