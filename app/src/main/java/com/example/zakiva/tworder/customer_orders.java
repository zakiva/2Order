package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class customer_orders extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = ">>>>debug";
    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView businessExpandableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );

        get_all_user_orders();

    }


    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    public void onResume() {
        super.onResume();
        get_all_user_orders();
    }

    void log_out() {
        ParseUser.logOut();
    }

    void notification_off(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "no");
        user.saveInBackground();
    }

    void notification_on(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "yes");
        user.saveInBackground();
    }

    public void OnLogOutClick(View view){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", "user is logged out");
        installation.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("is_signed_in", "no");
                    user.saveInBackground(new SaveCallback() {
                        public void done(ParseException e2) {
                            if (e2 == null) {
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
                                Log.i(TAG, String.format("%s", e2.toString()));
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

    protected void get_all_user_orders() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("customer_phone", ParseUser.getCurrentUser().getString("phone"));
        query.whereEqualTo("customer_visible", "yes");
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
            String[] item = new String[9];
            item[0] = order.getString("code");
            item[1] = order.getString("business_name");
            item[2] = order.getString("status");
            item[3] = order.getString("business_address");
            item[4] = order.getString("details");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = order.getCreatedAt();
            item[5] = df.format(date);
            item[6] = order.getObjectId().toString();
            item[7] = order.getString("business_id");
            item[8] = order.getString("customer_name");
            if (item[2].equals("READY"))
                items.add(0, item);
            else
                items.add(item);
        }
        ListAdapter customerAdapter = new customer_list_orders_adapter(getBaseContext(), items);
        ListView customerListView = (ListView) findViewById(R.id.customer_orders_list);
        customerListView.setAdapter(customerAdapter);

        swipeRefreshLayout.setRefreshing(false);


    }

    public void settings_clicked(View view) {
        Intent i = new Intent(view.getContext(), settings_customer.class);
        startActivity(i);
    }

    static int x = 0;

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        get_all_user_orders();
    }
}


