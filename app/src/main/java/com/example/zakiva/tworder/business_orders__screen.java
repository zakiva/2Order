package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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

    public SlidingMenu slidingMenu ;



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders__screen);


        get_all_user_orders();


        EditText sv = (EditText) findViewById(R.id.editText);
        sv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                    query.whereEqualTo("business_user", ParseUser.getCurrentUser());
                    query.whereNotEqualTo("status", "READY");
                    query.orderByDescending("prior"); // true first
                    query.addAscendingOrder("createdAt"); // old first
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(final List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                EditText sv = (EditText) findViewById(R.id.editText);
                                sv.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (s.toString().equals("")) {
                                            draw_orders(objects);
                                        } else {
                                            //Log.d("a: ", s.toString());
                                            List<ParseObject> list2 = new ArrayList<ParseObject>();
                                            for (ParseObject po : objects) {
                                                if (po.getString("code").contains(s.toString()) || po.getString("details").contains(s.toString()) || po.getString("customer_phone").contains(s.toString())) {
                                                    list2.add(po);
                                                }
                                            }
                                            draw_orders(list2);
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
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

       // getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.slidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void send_sms(String number, String content) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, content, null, null);
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
                        send_sms(username, message);
                    }
                } else {
                    // The request failed
                    Log.d("fail", "bummer");
                }
            }
        });
    }


    void log_out() {
        ParseUser.logOut();
    }


    public void OnLogOutClick(){
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
            arrayChildren.add("Phone : " + order.getString("customer_phone"));
            arrayChildren.add("Details : " + order.getString("details"));
            arrayChildren.add("Status : " + order.getString("status"));
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);

        }
    }

    public void onDeleteClick(View view){
        //ariel
        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Are you sure you want to delete this order");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Delete!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void history_clicked() {
        Intent i = new Intent(this, business_orders_history.class);
        startActivity(i);
    }

    public void customers_clicked() {
        Intent i = new Intent(this, business_customers.class);
        startActivity(i);
    }

}


