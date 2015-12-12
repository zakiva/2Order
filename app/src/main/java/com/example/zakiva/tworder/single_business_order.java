package com.example.zakiva.tworder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import javax.microedition.khronos.egl.EGLDisplay;

public class single_business_order extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business_order);

        Bundle extras = getIntent().getExtras();

        order_number = (TextView) findViewById(R.id.textview_code);
        order_details = (TextView) findViewById(R.id.textview_details);
        phone = (TextView) findViewById(R.id.textview_phone);
        name  = (TextView) findViewById(R.id.textview_name);
        time_past = (TextView) findViewById(R.id.textview_time_past);
        time_create = (TextView) findViewById(R.id.textview_time_create);
        status = (TextView) findViewById(R.id.textview_status);

        order_number.setText(extras.getString("code"));
        order_details.setText(extras.getString("details"));
        phone.setText(extras.getString("phone"));
        //name.setText(extras.getString("name"));
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
        //edit_name.setText(extras.getString("name"));

    }

    public void edit_clicked(View view) {
        Button edit = (Button) findViewById(R.id.button_edit);
        Button delete = (Button) findViewById(R.id.button_delete);
        Button end_edit = (Button) findViewById(R.id.button_end_edit);
        edit.setVisibility(View.GONE);
        end_edit.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        make_switch();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void make_switch(){
        ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_number);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_details);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_name);
        switcher(viewSwitcher);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher_phone);
        switcher(viewSwitcher);
    }

    protected void switcher(final ViewSwitcher viewSwitcher){
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
        //name.setText(edit_name.getText());


        Button edit = (Button) findViewById(R.id.button_edit);
        Button end_edit = (Button) findViewById(R.id.button_end_edit);
        Button delete = (Button) findViewById(R.id.button_delete);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        edit.setVisibility(View.VISIBLE);
        end_edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);

        make_switch();
    }
}
