package com.example.zakiva.tworder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class customer_order_adapter extends ArrayAdapter<String[]> {

    public customer_order_adapter(Context context, ArrayList<String[]> items) {
        super(context,R.layout.customer_orders_row ,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.customer_orders_row, parent, false);

        // 5 var (business Name,Business Address, order Number, status, details)
        String[] item = getItem(position);
        // need to check there is 5 item else return false!

        TextView businessName = (TextView) customView.findViewById(R.id.businessName);
        TextView businessAddress = (TextView) customView.findViewById(R.id.businessAddress);
        TextView orderNumber = (TextView) customView.findViewById(R.id.orderNumber);
        TextView status = (TextView) customView.findViewById(R.id.orderStatus);
        TextView details = (TextView) customView.findViewById(R.id.orderDetails);


        businessName.setText("  Business: " + item[0]);
        businessAddress.setText("Address: " + item[1]);
        orderNumber.setText("Order # " + item[2]);
        status.setText("   Status: " + item[3]);
        details.setText("Details: " + item[4]);

        if(item[3].equals("READY")){
            status.setTextColor(Color.parseColor("GREEN"));
        }
        return customView;
    }
}