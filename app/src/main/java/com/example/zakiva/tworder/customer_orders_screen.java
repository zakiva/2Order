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
import com.parse.GetCallback;
import com.parse.Parse;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        get_all_user_orders();
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

    protected void draw_orders(List<ParseObject> orders) {
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject order : orders) {
            String[] item = new String[5];
            item[0] = order.getString("business_name");
            item[1] = order.getString("business_address");
            item[2] = order.getString("code");
            item[3] = order.getString("status");
            item[4] = order.getString("details");
            items.add(item);
        }

        ListAdapter customerAdapter = new customer_order_adapter(getBaseContext(), items);
        ListView customerListView = (ListView) findViewById(R.id.customerListView);
        customerListView.setAdapter(customerAdapter);
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
