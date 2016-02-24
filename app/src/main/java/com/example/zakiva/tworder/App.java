package com.example.zakiva.tworder; /**
 * Created by Ariel on 11/18/2015.
 */
import android.app.Application;
import android.content.res.Configuration;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

import java.util.Locale;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JwL3fVDM1GoYSss63FFTwy7jyLt1abq7gYMfiDij", "MRKSsUUKU0AMGRYNTiEz48DXJ4KovXAaqHz0UAG5");
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
