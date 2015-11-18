package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.app.ExpandableListActivity;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class business_orders__screen extends AppCompatActivity {

    void push_notification(final String username)
    {
        //is_user_exist = 0;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    if (count > 0) {//The user exists!!
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("notification_id", username);
                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setMessage("Your order is ready!!");
                        push.sendInBackground();
                        //Log.d("success", "The number is " + count);
                    } else{
                        //The user does not exist. We need to connect him some other way
                    }
                } else {
                    // The request failed
                    Log.d("fail", "bummer");
                }
            }
        });
    }

    private static final String TAG = ">>>>debug";


    ExpandableListView businessExpandableList;

    @Override
    public void onBackPressed(){
        //do nothing
    }

    void log_out()
    {
        ParseUser.logOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders__screen);

        //Log.i(TAG, " on create .. ");
        get_all_user_orders();

    }


    public void OnLogOutClick(View view){
        log_out();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }

    protected void get_all_user_orders() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());
        query.orderByDescending("prior"); // true first
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


    public void createNewOrderClick(View view) {
        Intent i = new Intent(this, new_order_screen.class);
        startActivity(i);
    }

    protected void draw_orders(List<ParseObject> orders){
        businessExpandableList = (ExpandableListView)findViewById(R.id.expandableList);
        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders__screen.this, arrayParents));

        for (ParseObject order: orders){
            business_list_group parent = new business_list_group();
            parent.setTitle("Order Number " + order.getString("code"));
            arrayChildren = new ArrayList<String>();
            arrayChildren.add(order.getString("customer_phone"));
            arrayChildren.add(order.getString("details"));
            if(order.getBoolean("prior") == true){
                arrayChildren.add("Urgent");
            } else {
                arrayChildren.add("Not Urgent");
            }
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);

        }
    }

}
