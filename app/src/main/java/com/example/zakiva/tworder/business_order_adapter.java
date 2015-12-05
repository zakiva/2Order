package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

import java.util.ArrayList;


class businees_order_adapter extends BaseExpandableListAdapter {


    private LayoutInflater inflater;
    private ArrayList<business_list_group> mParent;
    //private RatingBar urgentBar;

    public businees_order_adapter(Context context, ArrayList<business_list_group> parent){
        mParent = parent;
        inflater = LayoutInflater.from(context);
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
            mParent.get(groupPosition).setEditFlag(true);
        }
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        textView.setText(getGroup(groupPosition).toString());
        final RatingBar urgentBar = (RatingBar) view.findViewById(R.id.urgentBar);
        urgentBar.setRating(mParent.get(groupPosition).getUrgent());

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                boolean flag = mParent.get(groupPosition).getEditFlag();
                ViewGroup vp = (ViewGroup) view.getParent();
                Button b = (Button) vp.findViewById(R.id.myDeleteButton);
                if (flag) {
                    b.setVisibility(View.VISIBLE);
                    urgentBar.setIsIndicator(false);
                } else {
                    b.setVisibility(View.GONE);
                    urgentBar.setIsIndicator(true);
                    mParent.get(groupPosition).setUrgent((int) urgentBar.getRating());
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
                    query.getInBackground(mParent.get(groupPosition).getItemKey(), new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.put("prior", (int) urgentBar.getRating());
                                object.saveInBackground();
                            } else {
                            }
                        }
                    });
                }
                mParent.get(groupPosition).setEditFlag(!flag);
                return true;
            }
        });


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
        if(textView.getText().toString().equals("first")){

        }
        textView.setText(mParent.get(groupPosition).getArrayChildren().get(childPosition));
        TextView key = (TextView) view.findViewById(R.id.key);
        key.setText(mParent.get(groupPosition).getItemKey());
        Button changeStatusButton = (Button) view.findViewById(R.id.statusButton);
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout r = (LinearLayout) view.getParent();
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
                                        String message = "Your order from " + object.getString("business_name") + " is ready!";
                                        push_notification(object.getString("customer_phone"), message);
                                        mParent.get(groupPosition).getArrayChildren().set(childPosition, "Status : " + item.getTitle().toString());
                                    } else {
                                        mParent.get(groupPosition).getArrayChildren().set(childPosition, "Status : " + item.getTitle().toString());
                                        object.put("status", item.getTitle());
                                    }
                                    object.saveInBackground();
                                    //get_all_user_orders();
                                    status.setText("Status : " + item.getTitle().toString());
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

        if(!isLastChild){
            ((ViewGroup) changeStatusButton.getParent()).removeView(changeStatusButton);
            TextView textSwitcher = (TextView) view.findViewById(R.id.list_item_text_child);
            textSwitcher.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ViewGroup vp = (ViewGroup) view.getParent();
                    ViewSwitcher switcher = (ViewSwitcher) vp.findViewById(R.id.businessTextSwitcher);
                    switcher.showNext();
                    TextView t = (TextView) vp.findViewById(R.id.list_item_text_child);
                    EditText e = (EditText) vp.findViewById(R.id.businessEditText);
                    String str = t.getText().toString();
                    String s = str.substring(0,1);
                    if(s.equals("D")){
                        s = str.substring(0,10);
                        str = str.substring(10);
                    } else {
                        s = str.substring(0,8);
                        str = str.substring(8);
                    }
                    t.setText(s);
                    e.setText(str);
                    e.requestFocus();
                    return true;
                }
            });
            EditText myEdit = (EditText) view.findViewById(R.id.businessEditText);
            myEdit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ViewGroup vp = (ViewGroup) view.getParent();
                    LinearLayout r =(LinearLayout) view.getParent().getParent();
                    TextView k = (TextView) r.findViewById(R.id.key);
                    final String itemKey = k.getText().toString();
                    ViewSwitcher switcher = (ViewSwitcher) vp.findViewById(R.id.businessTextSwitcher);
                    TextView t = (TextView) vp.findViewById(R.id.list_item_text_child);
                    EditText e = (EditText) vp.findViewById(R.id.businessEditText);
                    t.setText(t.getText().toString() + e.getText().toString());
                    switcher.showPrevious();
                    if(t.getText().toString().substring(0,1).equals("D")){
                        mParent.get(groupPosition).getArrayChildren().set(childPosition,
                                "Details : " + e.getText().toString());
                        changeParseStatus("Details", e.getText().toString(), itemKey);
                    } else {
                        mParent.get(groupPosition).getArrayChildren().set(childPosition,
                                "Phone : " + e.getText().toString());
                        changeParseStatus("customer_phone", e.getText().toString(), itemKey);
                    }


                    return true;
                }
            });
        }

        view.setTag(holder);

        //return the entire view
        return view;
    }

    public void changeParseStatus(final String field, final String updateValue, String itemKey){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(itemKey, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put(field, updateValue);

                    object.saveInBackground();
                } else {

                }
            }
        });
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
                        //The user does not exist. We need to connect him some other way
                    }
                } else {
                    // The request failed
                    Log.d("fail", "bummer");
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




