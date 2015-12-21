package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class first_screen extends AppCompatActivity {
    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
    }

    public void newBusinessClick(View view){
        Intent i = new Intent(this, new_business_screen.class);
        startActivity(i);
    }

    public void newCustomerClick(View view){
        Intent i = new Intent(this, new_customer_screen.class);
        startActivity(i);
    }

}
