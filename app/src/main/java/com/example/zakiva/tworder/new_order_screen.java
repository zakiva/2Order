package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

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
        final EditText orderDetails = (EditText) findViewById(R.id.orderDetailsInput);
        final RatingBar orderUrgent = (RatingBar) findViewById(R.id.setUrgentBar);

        int prior = (int) orderUrgent.getRating();

        String customer_phone = customerPhone.getText().toString();
        String order_number = orderNumber.getText().toString();
        String order_details = orderDetails.getText().toString();

        create_new_order(customer_phone, order_number, order_details, prior);

        startActivity(i);

    }

    public void create_new_order (String phone, String code, String details, int prior){
        ParseObject order = new ParseObject("Order");
        order.put("business_user", ParseUser.getCurrentUser());
        order.put("business_id", ParseUser.getCurrentUser().getObjectId());
        order.put("business_name", ParseUser.getCurrentUser().getString("name"));
        order.put("business_address", ParseUser.getCurrentUser().getString("address"));
        order.put("customer_phone", phone);
        order.put("code", code);
        order.put("details", details);
        order.put("prior", prior);
        order.put("status", "In Progress");
        order.saveInBackground();
        handle_customer(phone);
    }

    public void handle_customer(final String phone){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Customer");
        query.whereEqualTo("phone", phone);
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> customers,
                                                    ParseException e) {
                                       if (e == null) {
                                           if (customers.size() == 0) {//add new customer
                                               String name = get_customer_name_from_user();
                                               final ParseObject customer = new ParseObject("Customer");
                                               customer.put("phone", phone);
                                               customer.put("name", name);
                                               customer.put("orders_counter", 1);
                                               customer.saveInBackground();

                                               customer.saveInBackground(new SaveCallback() {
                                                   public void done(ParseException e) {
                                                       if (e == null) {
                                                           ParseUser user = ParseUser.getCurrentUser();
                                                           ParseRelation<ParseObject> relation = user.getRelation("customers");
                                                           relation.add(customer);
                                                           user.saveInBackground();
                                                       } else {
                                                       }
                                                   }
                                               });
                                           } else {//customer already exists
                                               ParseObject customer = customers.get(0);
                                               customer.put("orders_counter", customer.getInt("orders_counter") + 1);
                                               customer.saveInBackground();
                                           }
                                       } else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }
        );
    }

    public String get_customer_name_from_user(){
        return "temp name"; // should be input from user # nir sade
    }
}
