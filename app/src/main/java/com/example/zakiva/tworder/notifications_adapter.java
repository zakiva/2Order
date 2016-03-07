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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zakiva on 12/23/15.
 */

public class notifications_adapter extends ArrayAdapter<String[]> {

    private Context context;
    private int new_notifications;

    public notifications_adapter(Context context, ArrayList<String[]> items, int new_notifications) {
        super(context, R.layout.notifiction_row, items);
        this.context = context;
        this.new_notifications = new_notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.notifiction_row, parent, false);

        final String[] item = getItem(position);



        Button choose = (Button) customView.findViewById(R.id.view_notification_context);
        TextView text = (TextView) customView.findViewById(R.id.text);
        TextView date = (TextView) customView.findViewById(R.id.date);

        date.setText(item[7]);

        if (position>=new_notifications){
            choose.setBackgroundColor(Color.parseColor("#FFCACBCC"));
            choose.setTextColor(Color.BLACK);
            text.setTextColor(Color.GRAY);
            date.setTextColor(Color.GRAY);
        }

        if (item[1].equals("order")){ // ariel handle here your case too
            choose.setText("View Order");
            text.setText(item[6] + " Poked You On Order " + item[5]);

        }
        else if (item[1].equals("order_late")){
            choose.setText("View Order");
            text.setText("Order " + item[5] + " is late");
        }
        else
        {
            choose.setText("View Feedback");
            text.setText(item[6] + " Sent You a Feedback");
        }

            choose.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {

                    final String itemId = item[0]; // need to put in item[0] the objectID


                    if (item[1].equals("order") || item[1].equals("order_late")) { // if the notification is poke (regular or auto)

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                        query.getInBackground(itemId, new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(v.getContext(), single_business_order.class);
                                            intent.putExtra("code", object.getString("code"));
                                            intent.putExtra("details", object.getString("details"));
                                            intent.putExtra("status", object.getString("status"));
                                            intent.putExtra("phone", object.getString("customer_phone"));
                                            intent.putExtra("name", object.getString("customer_name"));
                                            intent.putExtra("priority", object.getInt("prior"));
                                            intent.putExtra("order_id", itemId);

                                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            Date date = object.getCreatedAt();

                                            intent.putExtra("time_past", businees_order_adapter.get_past_time(date));
                                            intent.putExtra("time", df.format(date));

                                            v.getContext().startActivity(intent);
                                        }
                                    }
                                }
                        );
                    } else {//the notification is a feedback
                        Intent intent = new Intent(v.getContext(), feedback_screen.class);
                        intent.putExtra("text", item[2]);
                        intent.putExtra("stars", item[3]);
                        intent.putExtra("customer_phone", item[4]);
                        intent.putExtra("customer_name", item[6]);
                        v.getContext().startActivity(intent);
                    }
                }

            });


            return customView;
        }




    }