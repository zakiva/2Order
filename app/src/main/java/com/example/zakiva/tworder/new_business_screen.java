package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class new_business_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_business_screen);
    }

    public void onBusinessLogClick(View view){
<<<<<<< HEAD

        // GO BACK TO THE SAME ACTIVITY

        Intent i = new Intent(this, business_orders__screen.class);
        final EditText userNameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText businessNameInput = (EditText) findViewById(R.id.businessNameInput);
        final EditText passInput = (EditText) findViewById(R.id.passwordInput);
        //final EditText businessAdressInput = (EditText) findViewById(R.id.businessAdressInput);
        String name = userNameInput.getText().toString();
        String pass = passInput.getText().toString();
        String busiName = businessNameInput.getText().toString();
        //String busiAdd =  businessAdressInput.getText().toString();
        i.putExtra("userName", name);
        i.putExtra("password", pass);
        i.putExtra("businessName", busiName);
        //i.putExtra("businessAdress", busiAdd);
=======
        Intent i = new Intent(this, new_business_screen.class);
        final EditText userNameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText businessNameInput = (EditText) findViewById(R.id.businessNameInput);
        final EditText passInput = (EditText) findViewById(R.id.passwordInput);
        final EditText businessAdressInput = (EditText) findViewById(R.id.businessAdressInput);
        String name = userNameInput.getText().toString();
        String pass = passInput.getText().toString();
        String busiName = businessNameInput.getText().toString();
        String busiAdd =  businessAdressInput.getText().toString();
        i.putExtra("userName", name);
        i.putExtra("password", pass);
        i.putExtra("businessName", busiName);
        i.putExtra("businessAdress", busiAdd);
>>>>>>> new customer screen + priority

        // need to check the validity of the password

        startActivity(i);
    }
<<<<<<< HEAD

=======
>>>>>>> new customer screen + priority
}
