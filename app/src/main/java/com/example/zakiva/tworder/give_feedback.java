package com.example.zakiva.tworder;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class give_feedback extends AppCompatActivity {

    private static final String TAG = ">>>>debug";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
    }

    public void send_feedback_clicked(View view) {

        EditText text = (EditText) findViewById(R.id.text);
        RatingBar grade = (RatingBar) findViewById(R.id.ratingBar2);

        Bundle extras = getIntent().getExtras();
        final String business_id = extras.getString("business_id");
        ParseObject notification = new ParseObject("Notification");
        notification.put("customer_user", ParseUser.getCurrentUser());
        notification.put("type", "feedback");
        notification.put("text", text.getText().toString());
        notification.put("order_id", ""); // can specify but feedback is more per user
        notification.put("stars", grade.getRating());
        notification.put("customer_name", extras.getString("customer_name"));
        notification.put("business_id", extras.getString("business_id"));
        notification.put("customer_phone", ParseUser.getCurrentUser().getString("phone"));
        notification.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                    query.whereEqualTo("user_id", business_id);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects,
                                         ParseException e) {
                            if (e == null) {

                                ParseObject business = objects.get(0);

                                Log.i(TAG, " UPDATING ###  .. ");
                                Log.i(TAG, String.format(" numebr = %d  .. ", business.getInt("new_notifications")));


                                business.put("new_notifications", (business.getInt("new_notifications") + 1));
                                business.saveInBackground();
                             } else {
                                // Something went wrong.
                            }
                        }
                    });

                    give_feedback.super.onBackPressed();

                } else {

                }
            }
        });



    }
}
