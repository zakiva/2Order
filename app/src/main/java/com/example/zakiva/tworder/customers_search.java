package com.example.zakiva.tworder;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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

        EditText sv = (EditText) findViewById(R.id.search_box);
        sv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                        //Log.d("mode: ", mode);
                        ParseRelation relation = ParseUser.getCurrentUser().getRelation("customers");
                        ParseQuery query2 = relation.getQuery();
                        query2.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(final List<ParseObject> customers, ParseException e) {
                                                        if (e == null) {
                                                            EditText sv = (EditText) findViewById(R.id.search_box);
                                                            sv.addTextChangedListener(new TextWatcher() {
                                                                @Override
                                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                                    if (s.toString().equals("")) {
                                                                        draw_customers(customers);
                                                                    } else {
                                                                        //Log.d("a: ", s.toString());
                                                                        List<ParseObject> list2 = new ArrayList<ParseObject>();
                                                                        for (ParseObject po : customers) {
                                                                            if (po.getString("name").toLowerCase().contains(s.toString().toLowerCase()) || po.getString("phone").toLowerCase().contains(s.toString().toLowerCase())) {
                                                                                list2.add(po);
                                                                            }
                                                                        }
                                                                        draw_customers(list2);
                                                                    }
                                                                }

                                                                @Override
                                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                    // TODO Auto-generated method stub
                                                                }

                                                                @Override
                                                                public void afterTextChanged(Editable s) {

                                                                    // TODO Auto-generated method stub
                                                                }
                                                            });

                                                        } else {
                                                            Log.d("Post retrieval", "Error: " + e.getMessage());
                                                        }
                                                    }
                                                }
                        );

                }
            }
        });
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
        });
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
