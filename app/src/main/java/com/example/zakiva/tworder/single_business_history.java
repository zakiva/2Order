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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class single_business_history extends AppCompatActivity {

    //this function get a phone number and makes a call to that number
    void call(String number)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try
        {
            startActivity(callIntent);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Log.d("problem:", "can't make phone call");
        }
    }

    TextView order_number;
    TextView order_details;
    TextView phone;
    TextView name;
    TextView time_past;
    TextView time_create;
    TextView status;

    EditText edit_order_number;
    EditText edit_order_details;
    EditText edit_phone;
    EditText edit_name;

    RatingBar ratingBar;

    Intent back_to_my_orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business_history);
        back_to_my_orders = new Intent(this, business_orders__screen.class);

        Bundle extras = getIntent().getExtras();

        order_number = (TextView) findViewById(R.id.textview_code);
        order_details = (TextView) findViewById(R.id.textview_details);
        phone = (TextView) findViewById(R.id.textview_phone);
        name = (TextView) findViewById(R.id.textview_name);
        time_past = (TextView) findViewById(R.id.textview_time_past);
        time_create = (TextView) findViewById(R.id.textview_time_create);
        status = (TextView) findViewById(R.id.textview_status);

        order_number.setText(extras.getString("code"));
        order_details.setText(extras.getString("details"));
        phone.setText(extras.getString("phone"));
        name.setText(extras.getString("name"));
        time_past.setText(String.format("                     %s", extras.getString("time_past")));
        time_create.setText(String.format("Created at: %s", extras.getString("time")));
        status.setText(String.format("Status: %s", extras.getString("status")));

        edit_order_number = (EditText) findViewById(R.id.edittext_order_number);
        edit_order_details = (EditText) findViewById(R.id.edittext_details);
        edit_name = (EditText) findViewById(R.id.edittext_name);
        edit_phone = (EditText) findViewById(R.id.edittext_phone);


        edit_order_number.setText(extras.getString("code"));
        edit_order_details.setText(extras.getString("details"));
        edit_phone.setText(extras.getString("phone"));
        edit_name.setText(extras.getString("name"));

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(extras.getInt("priority"));
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.GRAY);
    }

    public void onButtonPhoneClicked(View view)
    {
        Bundle extras = getIntent().getExtras();
        call(extras.getString("phone"));
    }

    public void deleteOrderClick(View view) {
        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Delete Order");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to delete this order? (order will be lost forever)")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete_order(dialog);
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

    private void delete_order(final DialogInterface dialog) {
        Bundle extras = getIntent().getExtras();
        String order_id = extras.getString("order_id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(order_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("history", "deleted");
                    object.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.cancel();
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

    public void back_clicked(View view) {
        startActivity(back_to_my_orders);
    }

    public void button_contact_info_clicked(View view) {
        Intent intent = new Intent(single_business_history.this, single_customer_information.class);
        intent.putExtra("parse", "true");
        intent.putExtra("phone", phone.getText().toString());
        startActivity(intent);
    }
}
