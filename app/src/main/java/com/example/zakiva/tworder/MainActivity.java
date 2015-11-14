package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static boolean parse_init = true;

    boolean is_user_signed()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null)
        {
            return true;
        }
        return false;
    }

    void do_if_user_singed()
    {
        if (is_user_signed())
        {
            String kind = (String) ParseUser.getCurrentUser().get("kind");
            if (kind.equals("business")){
                //what happens if business user is signed in
                Intent intent = new Intent(getBaseContext(), business_orders__screen.class);
                startActivity(intent);
            }
            else
            {
                //what happens if customer user is signed in
                //Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                //startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (parse_init){
            // Enable Local Datastore.
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "JwL3fVDM1GoYSss63FFTwy7jyLt1abq7gYMfiDij", "MRKSsUUKU0AMGRYNTiEz48DXJ4KovXAaqHz0UAG5");
            ParseInstallation.getCurrentInstallation().saveInBackground();
            parse_init = false;
        }

        do_if_user_singed();
    }

    public void onNextClick(View view){
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
