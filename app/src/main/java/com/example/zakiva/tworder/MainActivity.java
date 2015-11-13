package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    private static boolean parse_init = true;

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
    }

    public void onNextClick(View view){
        Intent i = new Intent(this, first_screen.class);
        startActivity(i);
    }
}
