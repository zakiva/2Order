package com.example.zakiva.tworder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zakiva on 12/17/15.
 */

public class customers_search_adapter extends ArrayAdapter<String[]> {

    public customers_search_adapter(Context context, ArrayList<String[]> items) {
        super(context,R.layout.customer_row ,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.customer_row, parent, false);

        final String[] item = getItem(position);

        RelativeLayout rl = (RelativeLayout) customView.findViewById(R.id.main_view_RelativeLayout);

        rl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                customer_chosen(v, item[0], item[1]);
            }
        });

        Button choose = (Button) customView.findViewById(R.id.button_choose);

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                customer_chosen(v, item[0], item[1]);
            }
        });

        TextView name = (TextView) customView.findViewById(R.id.name);
        TextView phone = (TextView) customView.findViewById(R.id.phone);

        name.setText(item[0]);
        phone.setText(item[1]);

        return customView;
    }

    private void customer_chosen(View v, String name, String phone) {
        Intent intent = new Intent(v.getContext(), new_order_screen.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        v.getContext().startActivity(intent);
    }
}