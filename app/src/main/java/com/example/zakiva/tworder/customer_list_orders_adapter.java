package com.example.zakiva.tworder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zakiva on 12/18/15.
 */
public class customer_list_orders_adapter extends ArrayAdapter<String[]> {

    public customer_list_orders_adapter(Context context, ArrayList<String[]> items) {
        super(context, R.layout.customer_order_row, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.customer_order_row, parent, false);

        final String[] item = getItem(position);

        RelativeLayout rl = (RelativeLayout) customView.findViewById(R.id.main_view_RelativeLayout);

        rl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), single_customer_order.class);
                intent.putExtra("code", item[0]);
                intent.putExtra("business_name", item[1]);
                intent.putExtra("status", item[2]);
                intent.putExtra("business_address", item[3]);
                intent.putExtra("details", item[4]);
                intent.putExtra("date", item[5]);
                intent.putExtra("order_id", item[6]);
                v.getContext().startActivity(intent);
            }
        });

        TextView code = (TextView) customView.findViewById(R.id.code);
        TextView business = (TextView) customView.findViewById(R.id.business);
        TextView status = (TextView) customView.findViewById(R.id.status);

        code.setText("Order: " + item[0]);
        business.setText("From: " + item[1]);
        status.setText("Status: " + item[2]);
        return customView;
    }
}