package com.example.zakiva.tworder;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;

public class customer_orders_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_screen);

        // Here i need to get from the dta the list of orders
        String[][] orders = {{"MyWorkshop", "benjamin 10 ta llllllloooododododo", "22", "NOT READY"}, {"steel factory 55", "beit lonatic", "203", "READY"}};
        //orders = get_orders(name+/password+/key)

        ListAdapter customerAdapter = new customer_order_adapter(this, orders);
        ListView customerListView = (ListView) findViewById(R.id.customerListView);
        customerListView.setAdapter(customerAdapter);
    }

    public void OnLogOutClick(View view){
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
