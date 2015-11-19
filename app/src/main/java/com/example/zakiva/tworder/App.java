package com.example.zakiva.tworder; /**
 * Created by Ariel on 11/18/2015.
 */
import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JwL3fVDM1GoYSss63FFTwy7jyLt1abq7gYMfiDij", "MRKSsUUKU0AMGRYNTiEz48DXJ4KovXAaqHz0UAG5");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
