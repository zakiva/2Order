package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

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

public class notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        get_notifications();
    }

    protected void get_notifications(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
        query.whereEqualTo("business_id", ParseUser.getCurrentUser().getObjectId().toString());
        query.addDescendingOrder("createdAt"); // new first
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> notifications,
                                                    ParseException e) {
                                       if (e == null) {
                                           draw_notifications(notifications);
                                       } else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }
        );

    }


    protected void draw_notifications(List<ParseObject> notifications) {
        final ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject notification : notifications) {
            String[] item = new String[8];
            item[0] = notification.getString("order_id"); // if its order
            item[1] = notification.getString("type"); // order or feedback
            item[2] = notification.getString("text");
            // text if it is a feedback or a generic message about poke that we write here
            // according to the type of poke (user or auto)
            item[3] = Integer.toString(notification.getInt("stars"));
            item[4] = notification.getString("customer_phone");
            item[5] = notification.getString("order_code");
            item[6] = notification.getString("customer_name");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = notification.getCreatedAt();
            item[7] = df.format(date);
            items.add(item);
        }



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {

                    ParseObject business = objects.get(0);

                    int new_notifications = business.getInt("new_notifications");
                    ListAdapter notificationAdapter = new notifications_adapter(getBaseContext(), items, new_notifications);
                    ListView notifications_list = (ListView) findViewById(R.id.notifications_list);
                    notifications_list.setAdapter(notificationAdapter);

                    business.put("new_notifications", 0);
                    business.saveInBackground();
                } else {
                    // Something went wrong.
                }
            }
        });
    }

}
