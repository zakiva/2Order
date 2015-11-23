package com.example.zakiva.tworder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zakiva on 11/27/15.
 */

public class business_customers_adapter extends ArrayAdapter<String[]> {

    private static final String TAG = ">>>>debug";

    public business_customers_adapter(Context context, ArrayList<String[]> items) {
        super(context,R.layout.customer_orders_row ,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.business_customers_row, parent, false);

        // 5 var (business Name,Business Address, order Number, status, details)
        String[] item = getItem(position);
        // need to check there is 5 item else return false!

        TextView customer_phone = (TextView) customView.findViewById(R.id.customer_phone);
        TextView customer_name = (TextView) customView.findViewById(R.id.customer_name);
        TextView number_of_orders = (TextView) customView.findViewById(R.id.number_of_orders);
        Button view_orders_button = (Button) customView.findViewById(R.id.view_orders);


        customer_phone.setText("customer phone: " + item[0]);
        customer_name.setText("customer name: " + item[1]);
        number_of_orders.setText("number of orders: " + item[2]);

        final String phone = item[0];


        view_orders_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                query.whereEqualTo("business_user", ParseUser.getCurrentUser());
                query.whereEqualTo("customer_phone", phone);
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
        });

        return customView;
    }

    protected void draw_orders(List<ParseObject> orders) {
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject order : orders) {
            Log.i(TAG, String.format("order code = %s ", order.getString("code")));
            Log.i(TAG, String.format("order details = %s ", order.getString("details")));
            Log.i(TAG, String.format("order status = %s ", order.getString("status")));
        }
    }
}