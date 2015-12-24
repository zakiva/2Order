package com.example.zakiva.tworder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class feedback_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);

        final Bundle extras = getIntent().getExtras();
        TextView from = (TextView) findViewById(R.id.from);
        TextView text = (TextView) findViewById(R.id.text);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
        Button customer_info = (Button) findViewById(R.id.customer_info);


        from.setText("From: " + extras.getString("customer_name"));
        text.setText(extras.getString("text"));
        ratingBar.setRating(Integer.parseInt(extras.getString("stars")));

        ratingBar.setIsIndicator(true);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.parseColor("#FF1887D7"));

        customer_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                Intent intent = new Intent(feedback_screen.this, single_customer_information.class);
                intent.putExtra("parse", "true");
                intent.putExtra("phone", extras.getString("customer_phone"));
                startActivity(intent);
            }
        });

    }




}
