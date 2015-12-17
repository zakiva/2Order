package com.example.zakiva.tworder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class customers_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_search);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        EditText et = (EditText) findViewById(R.id.search_box);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }

        });

        get_customers();
    }

    protected void get_customers() {
        ParseRelation relation = ParseUser.getCurrentUser().getRelation("customers");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> customers,
                                                    ParseException e) {
                                       if (e == null) {
                                           draw_customers(customers);
                                       } else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }   );
    }

    protected void draw_customers(List<ParseObject> customers) {
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (ParseObject customer : customers) {
            String[] item = new String[2];
            item[0] = customer.getString("name");
            item[1] = customer.getString("phone");
            items.add(item);
        }
        ListAdapter customerAdapter = new customers_search_adapter(getBaseContext(), items);
        ListView customerListView = (ListView) findViewById(R.id.listView);
        customerListView.setAdapter(customerAdapter);
    }
}
