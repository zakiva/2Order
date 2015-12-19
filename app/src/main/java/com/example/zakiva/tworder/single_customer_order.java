package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class single_customer_order extends AppCompatActivity {

    TextView order_number;
    TextView order_details;
    TextView business_name;
    TextView business_address;
    TextView time_create;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_customer_order);

        Bundle extras = getIntent().getExtras();

        order_number = (TextView) findViewById(R.id.textview_code);
        order_details = (TextView) findViewById(R.id.textview_details);
        business_name = (TextView) findViewById(R.id.textview_from);
        business_address = (TextView) findViewById(R.id.textview_adress);
        time_create = (TextView) findViewById(R.id.textview_time_create);
        status = (TextView) findViewById(R.id.textview_status);

        order_number.setText(extras.getString("code"));
        order_details.setText(extras.getString("details"));
        business_name.setText(extras.getString("business_name"));
        business_address.setText(extras.getString("business_address"));
        time_create.setText(String.format("Created at: %s", extras.getString("date")));
        status.setText(String.format("Status: %s", extras.getString("status")));
    }

    public void deleteOrderClick(View view) {

        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Remove Order");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to remove this order?")
                .setCancelable(false)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        move_order_to_history(dialog);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    private void move_order_to_history(final DialogInterface dialog) {
        Bundle extras = getIntent().getExtras();
        String order_id = extras.getString("order_id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(order_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("customer_visible", "no");
                    object.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.cancel();
                                Intent back_to_my_orders = new Intent(single_customer_order.this, customer_orders.class);
                                startActivity(back_to_my_orders);
                            } else {
                            }
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });

    }
}
