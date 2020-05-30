package dev.ahmdaeyz.guardianscope;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class GuardianScopeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
