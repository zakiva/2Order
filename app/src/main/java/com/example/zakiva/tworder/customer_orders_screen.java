package com.example.zakiva.tworder;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class customer_orders_screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";

    void log_out()
    {
        ParseUser.logOut();
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", ParseUser.getCurrentUser().getUsername());
        installation.saveInBackground();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_screen);

        get_all_user_orders();

        // Here i need to get from the dta the list of orders
        String[][] orders = {{"MyWorkshop", "benjamin 10 ta llllllloooododododo", "22", "NOT READY"}, {"steel factory 55", "beit lonatic", "203", "READY"}};
        //orders = get_orders(name+/password+/key)

        ListAdapter customerAdapter = new customer_order_adapter(this, orders);
        ListView customerListView = (ListView) findViewById(R.id.customerListView);
        customerListView.setAdapter(customerAdapter);
    }

    protected void get_all_user_orders() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("customer_phone", ParseUser.getCurrentUser().getString("username"));
        query.addAscendingOrder("createdAt"); // old first
        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(List<ParseObject> orders,
                                                    ParseException e) {
                                       if (e == null) {
                                           draw_orders(orders);
                                       } else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }
        );
    }

    protected void draw_orders(List<ParseObject> orders){

        for (ParseObject order: orders){

            // nir sade # implement
        }
    }

    public void OnLogOutClick(View view){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", "user is logged out");
        installation.saveInBackground();
        log_out();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
