package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class single_customer_information extends AppCompatActivity {

    TextView phone;
    TextView name;
    TextView counter;
    TextView time;



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
