package com.example.zakiva.tworder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class business_orders__screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";
    ExpandableListView businessExpandableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders__screen);

        //Log.i(TAG, " on create .. ");
        get_all_user_orders();

    }


    void push_notification(final String username, final String message)
    {
        //is_user_exist = 0;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    if (count > 0) {//The user exists!!
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("notification_id", username);
                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setMessage(message);
                        push.sendInBackground();
                        //Log.d("success", "The number is " + count);
                    } else {
                        //The user does not exist. We need to connect him some other way
                    }
                } else {
                    // The request failed
                    Log.d("fail", "bummer");
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }

    void log_out()
    {
        ParseUser.logOut();
    }


    public void OnLogOutClick(View view){
        log_out();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }

    protected void get_all_user_orders() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());
        query.whereNotEqualTo("status", "READY");
        query.orderByDescending("prior"); // true first
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


    public void createNewOrderClick(View view) {
        Intent i = new Intent(this, new_order_screen.class);
        startActivity(i);
    }

    protected void draw_orders(List<ParseObject> orders){
        businessExpandableList = (ExpandableListView)findViewById(R.id.expandableList);
        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders__screen.this, arrayParents));

        for (ParseObject order: orders){
            business_list_group parent = new business_list_group();
            parent.setTitle("Order Number " + order.getString("code"));
            parent.setUrgent(order.getInt("prior"));
            parent.setItemKey(order.getObjectId());
            arrayChildren = new ArrayList<String>();
            arrayChildren.add("Customer phone: " + order.getString("customer_phone"));
            arrayChildren.add("Details : " + order.getString("details"));
            arrayChildren.add("Status : " + order.getString("status"));
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);

        }
    }


    public void changeStatus(View view){
        final Context context = this;
        LinearLayout r =(LinearLayout) view.getParent();
        TextView t = (TextView) r.findViewById(R.id.key);
        final TextView status = (TextView) r.findViewById(R.id.list_item_text_child);
        final Button button1 = (Button) r.findViewById(R.id.statusButton);
        final String itemId = t.getText().toString();
        //open a pop up window and select the string
        PopupMenu popup = new PopupMenu(business_orders__screen.this, button1);
        popup.getMenuInflater().inflate(R.menu.popup_change_status_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(final MenuItem item) {
                Toast.makeText(business_orders__screen.this, "status changed to : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                query.getInBackground(itemId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            if (item.getTitle().equals("READY")) {
                                object.put("status", "READY");
                                String message = "Your order from " + object.getString("business_name") + " is ready!";
                                push_notification(object.getString("customer_phone"), message);
                            } else {
                                object.put("status", item.getTitle());
                            }
                            object.saveInBackground();
                            get_all_user_orders();
                        } else {

                        }
                    }
                });

                return true;
            }
        });
            popup.show();
        }

    public void history_clicked(View view) {
        Intent i = new Intent(this, business_orders_history.class);
        startActivity(i);
    }

    public void customers_clicked(View view) {
        Intent i = new Intent(this, business_customers.class);
        startActivity(i);
    }
}


