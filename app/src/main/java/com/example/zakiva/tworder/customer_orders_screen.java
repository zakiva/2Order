package com.example.zakiva.tworder;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class customer_orders_screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";

    void log_out()
    {
        ParseUser.logOut();
    }

    void notification_off(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "no");
        user.saveInBackground();
    }

    void add_feedback(String business, String feedback_content, int stars)
    {
        ParseObject feedback = new ParseObject("Business_notifications");
        feedback.put("from", ParseUser.getCurrentUser().getString("phone"));
        feedback.put("to", business);
        feedback.put("kind", "feedback");
        feedback.put("content", feedback_content);
        feedback.put("stars", stars);
        feedback.put("switch", "on");
        feedback.saveInBackground();
    }

    void poke(ParseObject order)
    {
        //posibly: order.put("poked", "yes");
        ParseObject poke = new ParseObject("Business_notifications");
        //poke.put("from", ParseUser.getCurrentUser().getString("phone"));
        //poke.put("to", order.getString("business_user"));
        poke.put("kind", "poke");
        poke.put("order", order);
        poke.put("switch", "on");
        poke.saveInBackground();
    }


    void notification_on(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "yes");
        user.saveInBackground();
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", ParseUser.getCurrentUser().getString("phone"));
        installation.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("is_signed_in", "yes");
        user.saveInBackground();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_screen);

        get_all_user_orders();
    }

    @Override
    public void onResume() {
        super.onResume();
        get_all_user_orders();
    }

    protected void get_all_user_orders() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("customer_phone", ParseUser.getCurrentUser().getString("phone"));
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

    protected void draw_orders(List<ParseObject> orders) {
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject order : orders) {
            String[] item = new String[5];
            item[0] = order.getString("business_name");
            item[1] = order.getString("business_address");
            item[2] = order.getString("code");
            item[3] = order.getString("status");
            item[4] = order.getString("details");
            items.add(item);
        }

        ListAdapter customerAdapter = new customer_order_adapter(getBaseContext(), items);
        ListView customerListView = (ListView) findViewById(R.id.customerListView);
        customerListView.setAdapter(customerAdapter);
    }

    public void OnLogOutClick(View view){

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", "user is logged out");
        installation.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("is_signed_in", "no");
        user.saveInBackground();

        if (AccessToken.getCurrentAccessToken() == null) {
            //return; // already logged out
        }
        else {
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();

                }
            }).executeAsync();
        }

        log_out();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
