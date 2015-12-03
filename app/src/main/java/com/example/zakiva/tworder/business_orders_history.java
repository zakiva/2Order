package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class business_orders_history extends AppCompatActivity {

    private static final String TAG = ">>>>debug";
    private Spinner businessSpinner;
    ExpandableListView businessExpandableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders_history);

        businessSpinner = (Spinner) findViewById(R.id.businessSpinner);
        List<String> list = new ArrayList<String>();
        list.add("HISTORY");
        list.add("ORDERS");
        list.add("CUSTOMERS");
        list.add("LOG OUT");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessSpinner.setAdapter(dataAdapter);
        businessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (businessSpinner.getItemAtPosition(position).toString().equals("ORDERS")) {
                    orders_clicked(businessSpinner);
                } else if (businessSpinner.getItemAtPosition(position).toString().equals("CUSTOMERS")) {
                    customers_clicked(businessSpinner);
                } else if (businessSpinner.getItemAtPosition(position).toString().equals("LOG OUT")) {
                    OnLogOutClick(businessSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        get_all_user_orders();
    }

    protected void get_all_user_orders() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());
        query.whereEqualTo("status", "READY");
        query.addDescendingOrder("createdAt"); // new first
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
        businessExpandableList = (ExpandableListView)findViewById(R.id.expandableList);
        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders_history.this, arrayParents));

        for (ParseObject order: orders) {
            business_list_group parent = new business_list_group();
            parent.setTitle("Order Number " + order.getString("code"));
            parent.setUrgent(order.getInt("prior"));
            parent.setItemKey(order.getObjectId());
            arrayChildren = new ArrayList<String>();
            arrayChildren.add("Phone : " + order.getString("customer_phone"));
            arrayChildren.add("Details : " + order.getString("details"));
            arrayChildren.add("Status : " + order.getString("status"));
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);
        }
    }

    public void customers_clicked(View view) {
        Intent i = new Intent(this, business_customers.class);
        startActivity(i);
    }

    public void orders_clicked(View view) {
        Intent i = new Intent(this, business_orders__screen.class);
        startActivity(i);
    }

    public void OnLogOutClick(View view){
        ParseUser.logOut();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
