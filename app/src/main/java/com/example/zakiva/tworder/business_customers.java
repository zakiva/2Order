package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class business_customers extends AppCompatActivity {

    private static final String TAG = ">>>>debug";
    private Spinner businessSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_customers);

        businessSpinner = (Spinner) findViewById(R.id.businessSpinner);
        List<String> list = new ArrayList<String>();
        list.add("CUSTOMERS");
        list.add("ORDERS");
        list.add("HISTORY");
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
                } else if (businessSpinner.getItemAtPosition(position).toString().equals("HISTORY")) {
                    history_clicked(businessSpinner);
                } else if(businessSpinner.getItemAtPosition(position).toString().equals("LOG OUT")) {
                    OnLogOutClick(businessSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        get_all_user_customers();
    }

    protected void get_all_user_customers() {
        ParseRelation relation = ParseUser.getCurrentUser().getRelation("customers");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(List<ParseObject> customers,
                                                    ParseException e) {
                                       if (e == null) {
                                           draw_customers(customers);
                                       } else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }
        );
    }

    protected void draw_customers(List<ParseObject> customers) {


        ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject customer : customers) {
            String[] item = new String[5];
            item[0] = customer.getString("phone");
            item[1] = customer.getString("name");
            item[2] = String.valueOf(customer.getInt("orders_counter"));
            items.add(item);
        }
        ListAdapter customerAdapter = new business_customers_adapter(getBaseContext(), items);
        ListView customerListView = (ListView) findViewById(R.id.customers_list);
        customerListView.setAdapter(customerAdapter);


    }

    public void history_clicked(View view) {
        Intent i = new Intent(this, business_orders_history.class);
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
