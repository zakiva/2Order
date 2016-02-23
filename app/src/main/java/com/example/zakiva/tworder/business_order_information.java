package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class business_order_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_order_information);

        final TextView orderNumberText = (TextView) findViewById(R.id.orderNumberText);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView phoneText = (TextView) findViewById(R.id.phoneText);
        final TextView statusText = (TextView) findViewById(R.id.statusText);
        final TextView orderDetailsText = (TextView) findViewById(R.id.orderDetailsText);

        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("orderKey").toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(key, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //object.put("status", item.getTitle());
                    //object.saveInBackground();
                    orderNumberText.setText("Order Number : " + object.getString("code"));
                    ratingBar.setRating(object.getInt("prior"));
                    phoneText.setText("Phone Number : " + object.getString("customer_phone"));
                    statusText.setText("Status: " + object.getString("status"));
                    orderDetailsText.setText("Details order : " + object.getString("details"));
                } else {

                }
            }
        });

        //need to get all order details and set them

        //orderNumberText.setText("Order Number : 1");
        //ratingBar.setRating(2);
        //phoneText.setText("Phone Number : 052474567");
        //statusText.setText("status : Ready");
        //orderDetailsText.setText("order Details : there is lots of details for this product  !!! ");
    }

    public void onEditClick(View view){
        String subStr;
        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("orderKey").toString();
        ViewGroup vp = (ViewGroup) view.getParent();
        Button editButton = (Button) vp.findViewById(R.id.EditButton);
        Button deleteButton = (Button) vp.findViewById(R.id.deleteButton);
        //first click
        ViewSwitcher orderNumberSwitcher = (ViewSwitcher) vp.findViewById(R.id.orderNumberSwitcher);
        TextView orderNumberText = (TextView) vp.findViewById(R.id.orderNumberText);
        EditText orderNumberEditText = (EditText) vp.findViewById(R.id.orderNumberEditText);

        ViewSwitcher phoneSwitcher = (ViewSwitcher) vp.findViewById(R.id.phoneSwitcher);
        TextView phoneText = (TextView) vp.findViewById(R.id.phoneText);
        EditText phoneEditText = (EditText) vp.findViewById(R.id.phoneEditText);

        ViewSwitcher statusSwitcher = (ViewSwitcher) vp.findViewById(R.id.statusSwitcher);
        TextView statusText = (TextView) vp.findViewById(R.id.statusText);
        EditText statusEditText = (EditText) vp.findViewById(R.id.statusEditText);

        ViewSwitcher detailsSwitcher = (ViewSwitcher) vp.findViewById(R.id.detailsSwitcher);
        TextView orderDetailsText = (TextView) vp.findViewById(R.id.orderDetailsText);
        EditText orderDetailsEditText = (EditText) vp.findViewById(R.id.orderDetailsEditText);

        RatingBar ratingBar = (RatingBar) vp.findViewById(R.id.ratingBar);
        TextView ratingText = (TextView) vp.findViewById(R.id.ratingText);

        if(editButton.getText().toString().equals("EDIT")){
            editButton.setText("Finish EDIT");
            orderNumberSwitcher.showNext();
            subStr = orderNumberText.getText().toString().substring(15);
            orderNumberEditText.setText(subStr);

            phoneSwitcher.showNext();
            subStr = phoneText.getText().toString().substring(15);
            phoneEditText.setText(subStr);

            statusSwitcher.showNext();
            subStr = statusText.getText().toString().substring(9);
            statusEditText.setText(subStr);

            detailsSwitcher.showNext();
            subStr = orderDetailsText.getText().toString().substring(16);
            orderDetailsEditText.setText(subStr);

            deleteButton.setVisibility(View.VISIBLE);
            ratingBar.setIsIndicator(false);
            ratingText.setVisibility(View.VISIBLE);


        }
        else {
            orderNumberSwitcher.showPrevious();
            orderNumberText.setText("Order Number : " + orderNumberEditText.getText().toString());

            phoneSwitcher.showPrevious();
            phoneText.setText("Phone Number : " + phoneEditText.getText().toString());

            statusSwitcher.showPrevious();
            statusText.setText("Status: " + statusEditText.getText().toString());

            detailsSwitcher.showPrevious();
            orderDetailsText.setText("order Details : " + orderDetailsEditText.getText().toString());


            ratingBar.setRating(ratingBar.getRating());
            ratingBar.setIsIndicator(true);
            //set new rating for parse user

            updateParse(key, orderNumberEditText.getText().toString(), phoneEditText.getText().toString(),
                    statusEditText.getText().toString(), orderDetailsEditText.getText().toString());

            deleteButton.setVisibility(View.GONE);
            ratingText.setVisibility(View.GONE);
            editButton.setText("EDIT");
        }
    }

    public void onBackClick(View view){
        super.onBackPressed();
    }


    public void deleteOrderClick(View view) {
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

    //need to fill
    public void updateParse(String key, String number, String phone,
                            String status, String details){

    }
}
