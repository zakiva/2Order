package com.example.zakiva.tworder;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

public class write_sms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sms);

        Bundle extras = getIntent().getExtras();

        TextView name = (TextView) findViewById(R.id.to_customer_name);
        name.setText("To: " + extras.getString("name"));
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

    public void send_sms(final String number, final String content) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Baned");
        query.whereEqualTo("phone_number", number);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (count == 0) {
                    Log.d("banned: ", "not inside! sms should be sent");
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, content, null, null);
                    } catch (Exception e1) {

                    }
                } else {
                    Log.d("banned: ", "inside! no sms");
                    alertToast("Sms was not sent, since the phone number owner does not want to get sms messages");
                }
            }
        });
    }

    public void send_sms(View view){
        Bundle extras = getIntent().getExtras();
        String phone = extras.getString("phone");
        EditText sms_content = (EditText) findViewById(R.id.editText4);
        String content = sms_content.getText().toString();
        send_sms(phone, content);

        Intent i = new Intent(this, business_orders__screen.class);
        startActivity(i);
    }
}
