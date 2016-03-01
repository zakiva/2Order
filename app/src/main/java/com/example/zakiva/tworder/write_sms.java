package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    static void send_sms(String number, String content) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, content, null, null);
        }
        catch (Exception e1){

        }
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
