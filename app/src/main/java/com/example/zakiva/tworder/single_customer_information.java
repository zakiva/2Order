package com.example.zakiva.tworder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class single_customer_information extends AppCompatActivity {

    TextView phone;
    TextView name;
    TextView counter;
    TextView time;

    //this function get a phone number and makes a call to that number
    void call(String number)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try
        {
            startActivity(callIntent);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Log.d("problem:", "can't make phone call");
        }
    }

    public void callClick(View view){
        Bundle extras = getIntent().getExtras();
        call(extras.getString("phone"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_customer_information);

        phone = (TextView) findViewById(R.id.textview_phone);
        name = (TextView) findViewById(R.id.textview_name);
        counter = (TextView) findViewById(R.id.total_orders_id);
        time = (TextView) findViewById(R.id.added_id);

        Bundle extras = getIntent().getExtras();
        phone.setText("Phone: " + extras.getString("phone"));
        name.setText("Name: " + extras.getString("name"));
        time.setText("Added on: " + "temp");
        counter.setText("Total orders: " + extras.getString("counter"));
    }
}
