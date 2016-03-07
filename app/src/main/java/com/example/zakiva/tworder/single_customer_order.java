package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

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

    public void poke_clicked(View view) {

        TextView poked = (TextView) findViewById(R.id.poked);
        poked.setVisibility(View.VISIBLE);
        TextView poke = (TextView) findViewById(R.id.poke_business);
        poke.setVisibility(View.GONE);
        Button poke_button = (Button) findViewById(R.id.button_poke);
        poke_button.setVisibility(View.GONE);

        final Bundle extras = getIntent().getExtras();

        if (extras.getString("status").equals("Ready")) {
            poked.setText("Order is Ready");
        }
        else
        {


        ParseObject notification = new ParseObject("Notification");
        notification.put("customer_user", ParseUser.getCurrentUser());
        notification.put("type", "order");
        notification.put("order_id", extras.get("order_id"));
        notification.put("text", "");
        notification.put("order_code", extras.getString("code"));
        notification.put("customer_name", extras.getString("customer_name"));
        notification.put("stars", 0);
        notification.put("business_id", extras.getString("business_id"));
        notification.put("customer_phone", ParseUser.getCurrentUser().getString("phone"));

        notification.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                    query.whereEqualTo("user_id", extras.getString("business_id"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects,
                                         ParseException e) {
                            if (e == null) {

                                ParseObject business = objects.get(0);

                                business.put("new_notifications", (business.getInt("new_notifications") + 1));
                                business.saveInBackground();
                            } else {
                                // Something went wrong.
                            }
                        }
                    });

                } else {

                }
            }
        });
    }

}

    public void shareOnFacebookClicked(View view){
        Bundle extras = getIntent().getExtras();
        String business_name = extras.getString("business_name");
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.downloadapp.com"))
                .setContentTitle("I would like to recommend " + business_name + ". They handled my order superbly!")
                .build();
        //ShareButton shareButton = (ShareButton)findViewById(R.id.button14);
        //shareButton.setShareContent(content);
        ShareDialog.show(this, content);
    }

    public void give_feedback_click(View view) {
        Bundle extras = getIntent().getExtras();
        Intent i = new Intent(single_customer_order.this, give_feedback.class);
        i.putExtra("business_id", extras.getString("business_id"));
        i.putExtra("customer_name", extras.getString("customer_name"));
        startActivity(i);
    }
}
