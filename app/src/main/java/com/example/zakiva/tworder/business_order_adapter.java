package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.ViewSwitcher;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


class businees_order_adapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<business_list_group> mParent;

    public businees_order_adapter(Context context, ArrayList<business_list_group> parent){
        mParent = parent;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getArrayChildren().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;

        if (view == null) {
            view = inflater.inflate(R.layout.business_list_group, viewGroup,false);
        }
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        textView.setText(getGroup(groupPosition).toString());
        RatingBar urgentBar = (RatingBar) view.findViewById(R.id.urgentBar);
        urgentBar.setRating(mParent.get(groupPosition).getUrgent());

        view.setTag(holder);

        //return the entire view
        return view;
    }

    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        view = inflater.inflate(R.layout.business_list_item, viewGroup,false);

        TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);

        textView.setText(mParent.get(groupPosition).getArrayChildren().get(childPosition));
        TextView key = (TextView) view.findViewById(R.id.key);
        key.setText(mParent.get(groupPosition).getItemKey());
        Button changeStatusButton = (Button) view.findViewById(R.id.statusButton);
        Button information_button = (Button) view.findViewById(R.id.information_button);

        information_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RelativeLayout r = (RelativeLayout) v.getParent();
                TextView t = (TextView) r.findViewById(R.id.key);
                final String itemId = t.getText().toString();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                query.getInBackground(itemId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(context, single_business_order.class);
                            intent.putExtra("code", object.getString("code"));
                            intent.putExtra("details", object.getString("details"));
                            intent.putExtra("status", object.getString("status"));
                            intent.putExtra("phone", object.getString("customer_phone"));
                            intent.putExtra("name", object.getString("customer_name"));
                            intent.putExtra("priority", object.getInt("prior"));
                            intent.putExtra("order_id", itemId);

                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date date = object.getCreatedAt();

                            intent.putExtra("time_past", get_past_time(date));
                            intent.putExtra("time", df.format(date));

                            context.startActivity(intent);
                        } else {
                            // something went wrong
                        }
                    }
                });
            }
        });




        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout r = (RelativeLayout) view.getParent();
                TextView t = (TextView) r.findViewById(R.id.key);
                final TextView status = (TextView) r.findViewById(R.id.list_item_text_child);
                final Button button1 = (Button) r.findViewById(R.id.statusButton);
                final String itemId = t.getText().toString();
                //open a pop up window and select the string
                PopupMenu popup = new PopupMenu(((ViewGroup) view.getParent().getParent()).getContext(), button1);
                popup.getMenuInflater().inflate(R.menu.popup_change_status_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                        query.getInBackground(itemId, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    if (item.getTitle().equals("READY")) {
                                        object.put("status", "READY");
                                        object.put("history", "yes");
                                        String message = "Your order from " + object.getString("business_name") + " is ready!";
                                        push_notification(object.getString("customer_phone"), message, itemId);
                                        mParent.get(groupPosition).getArrayChildren().set(childPosition, "Status: " + item.getTitle().toString());
                                    } else {
                                        mParent.get(groupPosition).getArrayChildren().set(childPosition, "Status: " + item.getTitle().toString());
                                        object.put("status", item.getTitle());
                                    }
                                    object.saveInBackground();
                                    //get_all_user_orders();
                                    status.setText("Status: " + item.getTitle().toString());
                                } else {

                                }
                            }
                        });
                        return true;
                    }
                });
                popup.show();
            }
        });


        if(childPosition!=2) {
            ((ViewGroup) changeStatusButton.getParent()).removeView(changeStatusButton);
        }
        if(childPosition!=3) {
            ((ViewGroup) information_button.getParent()).removeView(information_button);
        }



        view.setTag(holder);

        //return the entire view
        return view;
    }

    public static String get_past_time(Date date){

        Date cur_date = new Date();
        float interval = ((float) (cur_date.getTime() - date.getTime())) / (1000*60*60*24);
        int days = (int) interval;
        int hours = (int) ((interval-days)*24);

        if (days==0){
            if (hours==0)
                return "Less than an hour";
            else
                return String.format("%d hours ago", hours);
        }
        return String.format("%d days %d hours ago", days, hours);
    }

    static void send_sms(final String number, final String content) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Baned");
        query.whereEqualTo("phone_number", number);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (count == 0) {
                    Log.d("banned: ", "not inside! sms should be sent");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, content, null, null);
                }
                else{
                    Log.d("banned: ", "inside! no sms");
                }
            }
        });
    }

    static void send_sms2(final String number, final String content){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, content, null, null);
    }


            static void push_notification(final String username, final String message, final String itemId) {

                //is_user_exist = 0;
                final ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("phone", username);
                query.countInBackground(new CountCallback() {
                    public void done(int count, ParseException e) {
                        if (e == null) {
                            if (count > 0) {//The user exists!!
                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereEqualTo("phone", username);
                                query.getFirstInBackground(new GetCallback<ParseUser>() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user == null) {
                                            Log.d("problem: ", "can't push");
                                        } else {
                                            if (user.getString("is_signed_in").equals("yes")) {
                                                if (user.getString("wants_notification").equals("yes")) {
                                                    Log.d("send:", "push");
                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                    pushQuery.whereEqualTo("notification_id", username);
                                                    ParsePush push = new ParsePush();
                                                    push.setQuery(pushQuery);
                                                    push.setMessage(message);
                                                    push.sendInBackground();
                                                } else {
                                                    Log.d("send:", "sms- user exist and signed but does not want notification");
                                                    send_sms(username, message);
                                                }
                                            } else {
                                                Log.d("send:", "sms- user exist but not signed");
                                                send_sms(username, message + " Watch your order info at 2order.parseapp.com/?" + itemId);
                                            }
                                        }
                                    }
                                });

                            } else {
                                Log.d("send:", "sms- user does not exist");
                                send_sms(username, message + " Download 2Order: https://www.downloadapp.com" + " Watch your order info at 2order.parseapp.com/?" + itemId);
                            }
                        } else {
                            // The request failed
                            Log.d("fail: ", "bummer");
                        }
                    }
                });
            }


            @Override
            public boolean isChildSelectable(int i, int i1) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
                super.registerDataSetObserver(observer);
            }

            protected class ViewHolder {
                protected int childPosition;
                protected int groupPosition;
            }

        }




