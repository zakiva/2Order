package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.app.ExpandableListActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class business_orders__screen extends AppCompatActivity {

    ExpandableListView businessExpandableList;

    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders__screen);

        // get_all_user_orders();

        businessExpandableList = (ExpandableListView) findViewById(R.id.expandableList);

        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren = new ArrayList<String>();

        //sets the adapter that provides data to the list.
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders__screen.this, arrayParents));

        //here we set the parents and the children
        /*

        // pulling the data from the last page

        Bundle mainData = getIntent().getExtras();
        if(mainData == null){
            return;
        }

        String customer_phone = mainData.getString("customerPhone");
        String order_number = mainData.getString("orderNumber");
        String order_details = mainData.getString("orderDetails");

        for (int i = 0; i < 3; i++){
            //for each "i" create a new Parent object to set the title and the children
            business_list_group parent = new business_list_group();
            parent.setTitle("Parent " + i);
            if(i == 1){
                business_list_group parent1 = new business_list_group();
                parent1.setTitle("ORDER NUMBER" + order_number );
                ArrayList<String> arrayChildren1 = new ArrayList<String>();
                arrayChildren1.add("CUSTOMER PHONE" + customer_phone);
                arrayChildren1.add(order_details);
                parent1.setArrayChildren(arrayChildren1);
                arrayParents.add(parent1);
            }
            arrayChildren = new ArrayList<String>();
            for (int j = 0; j < 4; j++) {
                arrayChildren.add("Child " + j);
            }
            parent.setArrayChildren(arrayChildren);

            //in this array we add the Parent object. We will use the arrayParents at the setAdapter
            arrayParents.add(parent);
        }
        */


    }

    protected void get_all_user_orders() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());// check if this is the right comparison
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


        //nir # implement


    }
}
