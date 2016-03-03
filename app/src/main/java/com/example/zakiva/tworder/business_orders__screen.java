package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.ViewGroup;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.ExpandableListView.OnGroupClickListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class business_orders__screen extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = ">>>>debug";
    ExpandableListView businessExpandableList;
    public SlidingMenu slidingMenu;
    protected static String mode = "orders";
    private Button create_button;
    private TextView screen_title;
    SwipeRefreshLayout swipeRefreshLayout;

    //this function get a parse object and returns the number of days since it was created
    float time_since_order_created(ParseObject order)
    {
        Date start_date = order.getCreatedAt();
        Date cur_date = new Date();
        long interval = cur_date.getTime() - start_date.getTime();
        float days = (float) interval / (1000*60*60*24);
        return days;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, " on create .. ");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setContentView(R.layout.activity_business_orders__screen);
        create_button = (Button)findViewById(R.id.createNewOrder);
        screen_title = (TextView)findViewById(R.id.screen_title);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );

        load_screen();

        EditText sv = (EditText) findViewById(R.id.editText);
        sv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mode.equals("customers")) {
                        //Log.d("mode: ", mode);
                        ParseRelation relation = ParseUser.getCurrentUser().getRelation("customers");
                        ParseQuery query2 = relation.getQuery();
                        query2.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(final List<ParseObject> customers, ParseException e) {
                                                        if (e == null) {
                                                            EditText sv = (EditText) findViewById(R.id.editText);
                                                            sv.addTextChangedListener(new TextWatcher() {
                                                                @Override
                                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                                    if (s.toString().equals("")) {
                                                                        draw_customers(customers);
                                                                    } else {
                                                                        //Log.d("a: ", s.toString());
                                                                        List<ParseObject> list2 = new ArrayList<ParseObject>();
                                                                        for (ParseObject po : customers) {
                                                                            if (po.getString("name").contains(s.toString()) || po.getString("phone").contains(s.toString()) || Integer.toString(po.getInt("orders_counter")).contains(s.toString())) {
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

                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                        if (mode.equals("orders")) {
                            //Log.d("mode: ", mode);
                            query.whereEqualTo("business_user", ParseUser.getCurrentUser());
                            query.whereNotEqualTo("status", "Ready");
                            query.orderByDescending("prior"); // true first
                            query.addAscendingOrder("createdAt"); // old first
                        } else if (mode.equals("history")) {
                            //Log.d("mode: ", mode);
                            query.whereEqualTo("business_user", ParseUser.getCurrentUser());
                            query.whereEqualTo("history", "yes");
                            query.addDescendingOrder("createdAt"); // new first
                        }
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
                                    Log.d("problem: ", "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                }
            }
        });

        //Log.i(TAG, " on create .. ");

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

    private void check_notifications() {
        final Button notifications_button = (Button) findViewById(R.id.button_notifications);
        final String user_id = ParseUser.getCurrentUser().getObjectId().toString();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.whereEqualTo("user_id", user_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {

                    ParseObject business = objects.get(0);

                    Log.i(TAG, " CHECKING ###  .. ");
                    Log.i(TAG, String.format(" numebr = %d  .. ", business.getInt("new_notifications")));

                    if (business.getInt("new_notifications") > 0)
                        notifications_button.setBackgroundResource(R.drawable.red_bell);
                    else
                        notifications_button.setBackgroundResource(R.drawable.bell);

                } else {
                    // Something went wrong.
                }
            }
        });



    }

    private void load_screen() {

        if (mode.equals("orders"))
            get_all_user_orders();
        else if (mode.equals("history"))
            get_all_user_history();
        else if (mode.equals("customers"))
            get_all_user_customers();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, " on restart .. ");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if (mode.equals("orders"))
            get_all_user_orders();
        else if (mode.equals("history"))
            get_all_user_history();
        else if (mode.equals("customers"))
            get_all_user_customers();

    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, " on back pressed .. ");
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
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

    void log_out() {
        ParseUser.logOut();
    }


    public void OnLogOutClick() {
        mode = "orders";
        log_out();
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }

    protected void get_all_user_orders() {
        EditText et = (EditText) findViewById(R.id.editText);
        et.getText().clear();
        et.setHint("search order");
        screen_title.setText("My Orders");
        create_button.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());
        query.whereEqualTo("history", "no");
        query.whereNotEqualTo("status", "Ready"); // can be removed
        query.orderByDescending("prior"); // true first
        query.addAscendingOrder("createdAt"); // old first
        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(final List<ParseObject> orders,
                                                    ParseException e) {
                                       if (e == null) {
                                           final List late_orders = new ArrayList();
                                           for (ParseObject order: orders){
                                               if ( (time_since_order_created(order)) > order.getInt("time_late")  &&  order.getString("marked_as_late").equals("no") ){
                                                   order.put("marked_as_late", "yes");
                                                   ParseObject notification = new ParseObject("Notification");
                                                   notification.put("customer_user", ParseUser.getCurrentUser());
                                                   notification.put("type", "order_late");
                                                   notification.put("order_id", order.getObjectId());
                                                   notification.put("text", "");
                                                   notification.put("order_code", order.getString("code"));
                                                   notification.put("customer_name", order.getString("customer_name"));
                                                   notification.put("stars", 0);
                                                   notification.put("business_id", order.getString("business_id"));
                                                   notification.put("customer_phone", order.getString("customer_phone"));
                                                   late_orders.add(notification);
                                                   late_orders.add(order);
                                                   ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                                                   query.whereEqualTo("user_id", order.getString("business_id"));
                                                   query.findInBackground(new FindCallback<ParseObject>() {
                                                       @Override
                                                       public void done(List<ParseObject> objects,
                                                                        ParseException e) {
                                                           if (e == null) {
                                                               ParseObject business = objects.get(0);
                                                               business.put("new_notifications", (business.getInt("new_notifications") + 1));
                                                               late_orders.add(business);
                                                           } else {
                                                               // Something went wrong.
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                           ParseObject.saveAllInBackground(late_orders, new SaveCallback() {
                                               @Override
                                               public void done(ParseException e) {
                                                   check_notifications();
                                                   draw_orders(orders);
                                               }

                                       });} else {
                                           Log.d("Post retrieval", "Error: " + e.getMessage());
                                       }
                                   }
                               }
        );
    }

    protected void get_all_user_history() {
        EditText et = (EditText) findViewById(R.id.editText);
        et.getText().clear();
        et.setHint("search order");
        screen_title.setText("Orders History");
        create_button.setVisibility(View.GONE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("business_user", ParseUser.getCurrentUser());
        query.whereEqualTo("history", "yes");
        query.addDescendingOrder("createdAt"); // new first
        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(List<ParseObject> orders,
                                                    ParseException e) {
                                       if (e == null) {
                                           draw_history(orders);
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

    protected void draw_orders(List<ParseObject> orders) {
        businessExpandableList = (ExpandableListView) findViewById(R.id.expandableList);
        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new businees_order_adapter(business_orders__screen.this, arrayParents));

        for (ParseObject order : orders) {
            business_list_group parent = new business_list_group();
            parent.setTitle("Order " + order.getString("code"));
            if ((time_since_order_created(order)) > order.getInt("time_late")){
                parent.setTitle("Order " + order.getString("code") + " *LATE!*");
            }
            parent.setUrgent(order.getInt("prior"));
            parent.setItemKey(order.getObjectId());
            arrayChildren = new ArrayList<String>();
            arrayChildren.add("Phone: " + order.getString("customer_phone"));
            arrayChildren.add("Details: " + order.getString("details"));
            arrayChildren.add("Status: " + order.getString("status"));
            arrayChildren.add("");
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void draw_history(List<ParseObject> orders) {
        businessExpandableList = (ExpandableListView) findViewById(R.id.expandableList);
        ArrayList<business_list_group> arrayParents = new ArrayList<business_list_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new history_adapter(business_orders__screen.this, arrayParents));

        for (ParseObject order : orders) {
            business_list_group parent = new business_list_group();
            parent.setTitle("Order " + order.getString("code"));
            parent.setUrgent(order.getInt("prior"));
            DateFormat df = new SimpleDateFormat("MM/dd/yy");
            Date date = order.getCreatedAt();
            parent.setDate(df.format(date));
            parent.setItemKey(order.getObjectId());
            arrayChildren = new ArrayList<String>();
            arrayChildren.add("Phone: " + order.getString("customer_phone"));
            arrayChildren.add("Details: " + order.getString("details"));
            arrayChildren.add("Status: " + order.getString("status"));
            arrayChildren.add("");
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void onDeleteClick(View view) {
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void open_sliding_menu(View view) {
        slidingMenu.toggle();
    }

    public void get_all_user_customers() {
        screen_title.setText("My Customers");
        EditText et = (EditText) findViewById(R.id.editText);
        et.getText().clear();
        et.setHint("search customer");
        create_button.setVisibility(View.GONE);
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
                               }
        );
    }

    private void draw_customers(List<ParseObject> customers) {
        businessExpandableList = (ExpandableListView) findViewById(R.id.expandableList);
        ArrayList<business_customer_group> arrayParents = new ArrayList<business_customer_group>();
        ArrayList<String> arrayChildren;
        businessExpandableList.setAdapter(new business_customers_adapter(business_orders__screen.this, arrayParents));

        for (ParseObject customer : customers) {
            business_customer_group parent = new business_customer_group();
            parent.setTitle("Customer: " + customer.getString("name"));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = customer.getCreatedAt();
            parent.setItemKey(df.format(date));
            arrayChildren = new ArrayList<String>();
            arrayChildren.add("Phone: " + customer.getString("phone"));
            arrayChildren.add("Total orders: " + customer.getInt("orders_counter"));
            arrayChildren.add("");
            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        load_screen();
    }

    public void notifications_clicked(View view){
        Intent i = new Intent(this, notifications.class);
        startActivity(i);
    }

    /*
    public void information_order_clicked(View view){
        Intent i = new Intent(this, business_order_information.class);
        ViewGroup vp = (ViewGroup) view.getParent();
        TextView k = (TextView) vp.findViewById(R.id.key);
        String key = k.getText().toString();
        i.putExtra("orderKey", key);
        startActivity(i);
    }
    */
}
