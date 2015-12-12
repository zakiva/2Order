package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class customer_order_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_information);

        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("orderKey").toString();
        //need to get the order and that get strings
        final TextView orderStatus = (TextView) findViewById(R.id.statusText);
        final TextView businessName = (TextView) findViewById(R.id.businessNameText);
        final TextView businessAddress = (TextView) findViewById(R.id.businessAddressText);
        final TextView orderDetails = (TextView) findViewById(R.id.orderDetailsText);

        //get order values
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(key, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //object.put("status", item.getTitle());
                    //object.saveInBackground();
                    orderStatus.setText(object.getString("status"));
                    businessName.setText(object.getString("business_name"));
                    businessAddress.setText(object.getString("business_address"));
                    orderDetails.setText(object.getString("details"));
                } else {

                }
            }
        });

        //orderStatus.setText("Order Status");
        //businessName.setText("business Name");
        //businessAddress.setText("Business Address");
        //orderDetails.setText("Order Details");


    }

    public void onBackClick(View view){
        super.onBackPressed();
    }

    public void gotItemClick(View view){

    }
}
