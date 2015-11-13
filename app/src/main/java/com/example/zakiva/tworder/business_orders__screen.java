package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.app.ExpandableListActivity;

import java.util.ArrayList;

public class business_orders__screen extends AppCompatActivity {

    ExpandableListView businessExpandableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders__screen);

        businessExpandableList = (ExpandableListView)findViewById(R.id.expandableList);

        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren = new ArrayList<String>();

        //here we set the parents and the children
        for (int i = 0; i < 3; i++){
            //for each "i" create a new Parent object to set the title and the children
            business_list_group parent = new business_list_group();
            parent.setTitle("Parent " + i);

            arrayChildren = new ArrayList<String>();
            for (int j = 0; j < 4; j++) {
                arrayChildren.add("Child " + j);
            }
            parent.setArrayChildren(arrayChildren);

            //in this array we add the Parent object. We will use the arrayParents at the setAdapter
            arrayParents.add(parent);
        }

        //sets the adapter that provides data to the list.
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders__screen.this, arrayParents));
    }
}
