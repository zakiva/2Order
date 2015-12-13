package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


class history_adapter extends BaseExpandableListAdapter {


    private LayoutInflater inflater;
    private ArrayList<business_list_group> mParent;
    private Context context;

    public history_adapter(Context context, ArrayList<business_list_group> parent){
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
        final RatingBar urgentBar = (RatingBar) view.findViewById(R.id.urgentBar);
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
        ((ViewGroup) changeStatusButton.getParent()).removeView(changeStatusButton);
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
                            Intent intent = new Intent(context, single_business_history.class);
                            intent.putExtra("code", object.getString("code"));
                            intent.putExtra("details", object.getString("details"));
                            intent.putExtra("status", object.getString("status"));
                            intent.putExtra("phone", object.getString("customer_phone"));
                            intent.putExtra("name", object.getString("customer_name"));
                            intent.putExtra("priority", object.getInt("prior"));
                            intent.putExtra("order_id", itemId);

                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date date = object.getCreatedAt();

                            intent.putExtra("time_past", businees_order_adapter.get_past_time(date));
                            intent.putExtra("time", df.format(date));

                            context.startActivity(intent);
                        } else {
                            // something went wrong
                        }
                    }
                });
            }
        });

        if(childPosition!=3) {
            ((ViewGroup) information_button.getParent()).removeView(information_button);
        }
        view.setTag(holder);

        //return the entire view
        return view;
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
