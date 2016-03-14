package com.example.zakiva.tworder;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class customer_orders_screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";

    void log_out()
    {
        ParseUser.logOut();
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

    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        final Button b = (Button) findViewById(R.id.logOutCustomerButton);
        b.setEnabled(false);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", "user is logged out");
        installation.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("is_signed_in", "no");
                    user.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (AccessToken.getCurrentAccessToken() == null) {
                                    //return; // already logged out
                                } else {
                                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                            .Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse graphResponse) {

                                            LoginManager.getInstance().logOut();

                                        }
                                    }).executeAsync();
                                }
                                log_out();
                                Intent i = new Intent(getApplicationContext(), first_screen.class);
                                startActivity(i);
                            } else {
                                Log.i(TAG, "e is not null");
                                Log.i(TAG, String.format("%s", e.toString()));
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "e is not null");
                    Log.i(TAG, String.format("%s", e.toString()));
                }
            }
        });


    }
}
