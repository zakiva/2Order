package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

public class customer_orders extends AppCompatActivity {

    private static final String TAG = ">>>>debug";
    ExpandableListView businessExpandableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("notification_id", ParseUser.getCurrentUser().getString("phone"));
        installation.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("is_signed_in", "yes");
        user.saveInBackground();

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
        installation.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("is_signed_in", "no");
        user.saveInBackground();

        //LoginManager.getInstance().logOut();
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


    protected void draw_orders(List<ParseObject> orders){
        businessExpandableList = (ExpandableListView)findViewById(R.id.expandableList);
        ArrayList<customer_list_group> arrayParents = new ArrayList<customer_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new customer_adapter(customer_orders.this, arrayParents));

        for (ParseObject order: orders){
            customer_list_group parent = new customer_list_group();
            parent.setTitle("Order Number " + order.getString("code"));
            parent.setItemKey(order.getObjectId());
            arrayChildren = new ArrayList<String>();
            arrayChildren.add(order.getString("business_name"));
            //arrayChildren.add("Address : " + order.getString("business_address"));
            arrayChildren.add("Status: " + order.getString("status"));
            //arrayChildren.add("Details : " + order.getString("details"));
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);

        }
    }

    public void onDeleteClick(View view){
        //ariel
        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Are you sure you want to delete this order");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Delete!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void information_order_clicked(View view){
        Intent i = new Intent(this, customer_order_information.class);
        ViewGroup vp = (ViewGroup) view.getParent();
        TextView k = (TextView) vp.findViewById(R.id.key);
        String key = k.getText().toString();
        i.putExtra("orderKey", key);
        startActivity(i);
    }


}


