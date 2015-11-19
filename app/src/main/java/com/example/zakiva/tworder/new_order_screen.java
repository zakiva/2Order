package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class new_order_screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_screen);
    }

    public void onCreateNewOrder(View view){
        Intent i = new Intent(this, business_orders__screen.class);
        final EditText customerPhone = (EditText) findViewById(R.id.customerPhoneInput);
        final EditText orderNumber = (EditText) findViewById(R.id.orderNumberInput);
        final EditText orederDetails = (EditText) findViewById(R.id.orderDetailsInput);
        final RatingBar orderUrgent = (RatingBar) findViewById(R.id.setUrgentBar);

        int prior = (int) orderUrgent.getRating();
        String customer_phone = customerPhone.getText().toString();
        String order_number = orderNumber.getText().toString();
        String order_details = orederDetails.getText().toString();

        create_new_order (customer_phone, order_number, order_details, prior);

        startActivity(i);

    }

    public void create_new_order (String phone, String code, String details, int prior){
        ParseObject order = new ParseObject("Order");
        order.put("business_user", ParseUser.getCurrentUser());
        order.put("customer_phone", phone);
        order.put("code", code);
        order.put("details", details);
        order.put("prior", prior);
        order.put("status", "In Progress");
        order.saveInBackground();
    }
}
