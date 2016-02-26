package com.example.zakiva.tworder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class single_customer_information extends AppCompatActivity {

    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_customer_information);

        final TextView phone = (TextView) findViewById(R.id.textview_phone);
        name = (TextView) findViewById(R.id.textview_name);
        final TextView counter = (TextView) findViewById(R.id.total_orders_id);
        final TextView time = (TextView) findViewById(R.id.added_id);

        final Bundle extras = getIntent().getExtras();

        if (extras.getString("parse").equals("false")) {

            phone.setText("Phone: " + extras.getString("phone"));
            name.setText("Name: " + extras.getString("name"));
            time.setText("Added on: " + extras.getString("date"));
            counter.setText("Total orders: " + extras.getString("counter"));

        } else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Customer");
            query.whereEqualTo("phone", extras.getString("phone"));
            query.whereEqualTo("business_id", ParseUser.getCurrentUser().getObjectId().toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                                       @Override
                                       public void done(List<ParseObject> customers,
                                                        ParseException e) {
                                           if (e == null) {
                                               ParseObject customer = customers.get(0);

                                               phone.setText("Phone: " + extras.getString("phone"));
                                               name.setText("Name: " + customer.getString("name"));

                                               DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                               Date date = customer.getCreatedAt();
                                               time.setText("Added on: " + df.format(date));
                                               counter.setText("Total orders: " + Integer.toString(customer.getInt("orders_counter")));

                                           } else {
                                               Log.d("Post retrieval", "Error: " + e.getMessage());
                                           }
                                       }
                                   }
            );
        }
    }


    //this function get a phone number and makes a call to that number
    void call(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.d("problem:", "can't make phone call");
        }
    }

    public void callClick(View view) {
        Bundle extras = getIntent().getExtras();
        call(extras.getString("phone"));
    }

    public void smsclick(View view){
        Bundle extras = getIntent().getExtras();
        Intent i = new Intent(getBaseContext(), write_sms.class);
        i.putExtra("phone", extras.getString("phone"));
        i.putExtra("name", name.getText().toString().substring(6));
        startActivity(i);
    }


}
