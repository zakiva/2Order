package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_customers);

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
}
