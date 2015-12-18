package com.example.zakiva.tworder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class single_customer_order extends AppCompatActivity {

    TextView order_number;
    TextView order_details;
    TextView business_name;
    TextView business_address;
    TextView time_create;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_customer_order);

        Bundle extras = getIntent().getExtras();

        order_number = (TextView) findViewById(R.id.textview_code);
        order_details = (TextView) findViewById(R.id.textview_details);
        business_name = (TextView) findViewById(R.id.textview_from);
        business_address = (TextView) findViewById(R.id.textview_adress);
        time_create = (TextView) findViewById(R.id.textview_time_create);
        status = (TextView) findViewById(R.id.textview_status);

        order_number.setText(extras.getString("code"));
        order_details.setText(extras.getString("details"));
        business_name.setText(extras.getString("business_name"));
        business_address.setText(extras.getString("business_address"));
        time_create.setText(String.format("Created at: %s", extras.getString("date")));
        status.setText(String.format("Status: %s", extras.getString("status")));
    }
}
