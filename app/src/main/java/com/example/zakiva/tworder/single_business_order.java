package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class single_business_order extends AppCompatActivity {

    //this function get a phone number and makes a call to that number
    void call(String number)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try
        {
            startActivity(callIntent);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Log.d("problem:", "can't make phone call");
        }
    }


    TextView order_number;
    TextView order_details;
    TextView phone;
    TextView name;
    TextView time_past;
    TextView time_create;
    TextView status;

    EditText edit_order_number;
    EditText edit_order_details;
    EditText edit_phone;
    EditText edit_name;

    RatingBar ratingBar;

    Intent back_to_my_orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business_order);
        back_to_my_orders = new Intent(this, business_orders__screen.class);

        Bundle extras = getIntent().getExtras();

        order_number = (TextView) findViewById(R.id.textview_code);
        order_details = (TextView) findViewById(R.id.textview_details);
        phone = (TextView) findViewById(R.id.textview_phone);
        name = (TextView) findViewById(R.id.textview_name);
        time_past = (TextView) findViewById(R.id.textview_time_past);
        time_create = (TextView) findViewById(R.id.textview_time_create);
        status = (TextView) findViewById(R.id.textview_status);

        order_number.setText(extras.getString("code"));
        order_details.setText(extras.getString("details"));
        phone.setText(extras.getString("phone"));
        name.setText(extras.getString("name"));
        time_past.setText(String.format("                     %s", extras.getString("time_past")));
        time_create.setText(String.format("Created at: %s", extras.getString("time")));
        status.setText(String.format("Status: %s", extras.getString("status")));

        edit_order_number = (EditText) findViewById(R.id.edittext_order_number);
        edit_order_details = (EditText) findViewById(R.id.edittext_details);
        edit_name = (EditText) findViewById(R.id.edittext_name);
        edit_phone = (EditText) findViewById(R.id.edittext_phone);


        edit_order_number.setText(extras.getString("code"));
        edit_order_details.setText(extras.getString("details"));
        edit_phone.setText(extras.getString("phone"));
        edit_name.setText(extras.getString("name"));

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(extras.getInt("priority"));
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.GRAY);
    }

    public void edit_clicked(View view) {
        Button edit = (Button) findViewById(R.id.button_edit);
        Button delete = (Button) findViewById(R.id.button_delete);
        Button end_edit = (Button) findViewById(R.id.button_end_edit);
        Button call = (Button) findViewById(R.id.button_call);
        Button info = (Button) findViewById(R.id.button_contact_info);

        edit.setVisibility(View.GONE);
        end_edit.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        call.setVisibility(View.GONE);
        info.setVisibility(View.GONE);

        make_switch();
        ratingBar.setIsIndicator(false);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.parseColor("#FF1887D7"));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void make_switch() {
        ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_number);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_details);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_name);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_phone);
        switcher(viewSwitcher);
    }

    protected void switcher(final ViewSwitcher viewSwitcher) {
        // Create and Animations on the ViewSwitcher to make it beautifully slide
        Animation animationOut = AnimationUtils.loadAnimation(
                getApplicationContext(), android.R.anim.slide_out_right);
        Animation animationIn = AnimationUtils.loadAnimation(
                getApplicationContext(), android.R.anim.slide_in_left);

        //set Animations
        viewSwitcher.getNextView().startAnimation(animationIn);
        viewSwitcher.setOutAnimation(animationOut);

        // Show next widget
        viewSwitcher.showNext();
    }

    public void end_edit_clicked(View view) {
        order_number.setText(edit_order_number.getText());
        order_details.setText(edit_order_details.getText());
        phone.setText(edit_phone.getText());
        name.setText(edit_name.getText());

        edit_order_parse(edit_order_number.getText().toString(), edit_order_details.getText().toString(), edit_phone.getText().toString(), edit_name.getText().toString(), (int) (ratingBar.getRating()));


        Button edit = (Button) findViewById(R.id.button_edit);
        Button end_edit = (Button) findViewById(R.id.button_end_edit);
        Button delete = (Button) findViewById(R.id.button_delete);
        Button call = (Button) findViewById(R.id.button_call);
        Button info = (Button) findViewById(R.id.button_contact_info);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        edit.setVisibility(View.VISIBLE);
        call.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        end_edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        ratingBar.setIsIndicator(true);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.GRAY);
        make_switch();
    }

    protected void edit_order_parse(final String code, final String details, final String phone, final String name, final int prior) {

        Bundle extras = getIntent().getExtras();
        String order_id = extras.getString("order_id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(order_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("code", code);
                    object.put("details", details);
                    object.put("prior", prior);
                    object.put("customer_name", name);
                    if (!(object.getString("customer_phone").equals(phone))) {
                        final String old_phone = object.getString("customer_phone");
                        object.put("customer_phone", phone);
                        object.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    change_customer(old_phone, phone, name);
                                } else {
                                }
                            }
                        });
                    } else
                        object.saveInBackground();
                } else {
                    // something went wrong
                }
            }
        });
    }

    private void change_customer(String old_phone, final String phone, final String name) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Customer");
        query.whereEqualTo("phone", old_phone);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> customers,
                             ParseException e) {
                if (e == null) {
                    final ParseObject customer = customers.get(0);
                    customer.put("orders_counter", customer.getInt("orders_counter") - 1);
                    customer.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                new_order_screen.handle_customer(phone, name);
                            } else {
                            }
                        }
                    });
                } else {
                    Log.d("Post retrieval", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void onButtonPhoneClicked(View view)
    {
        Bundle extras = getIntent().getExtras();
        call(extras.getString("phone"));
    }

    public void deleteOrderClick(View view) {
        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Remove Order");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to remove this order? (order will be available on Orders History)")
                .setCancelable(false)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        move_order_to_history(dialog);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void move_order_to_history(final DialogInterface dialog) {
        Bundle extras = getIntent().getExtras();
        String order_id = extras.getString("order_id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(order_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("history", "yes");
                    object.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.cancel();
                                startActivity(back_to_my_orders);
                            } else {
                            }
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });

    }

    public void back_clicked(View view) {
        startActivity(back_to_my_orders);
    }


    public void change_status_clicked(View view) {
        //RelativeLayout r = (RelativeLayout) view.getParent();
        //TextView t = (TextView) r.findViewById(R.id.key);
        //final TextView status = (TextView) r.findViewById(R.id.list_item_text_child);
        //final Button button1 = (Button) r.findViewById(R.id.statusButton);

        Button status_button = (Button) findViewById(R.id.button_update_status);

        Bundle extras = getIntent().getExtras();
        final String itemId = extras.getString("order_id");

        //open a pop up window and select the string
        PopupMenu popup = new PopupMenu(((ViewGroup) view.getParent().getParent()).getContext(), status_button);
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
                                businees_order_adapter.push_notification(object.getString("customer_phone"), message);
                            } else {
                                object.put("status", item.getTitle());
                            }
                            object.saveInBackground();
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

    public void button_contact_info_clicked(View view) {
        Intent intent = new Intent(single_business_order.this, single_customer_information.class);
        intent.putExtra("parse", "true");
        intent.putExtra("phone", phone.getText().toString());
        startActivity(intent);
    }
}
