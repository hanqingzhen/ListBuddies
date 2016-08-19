package com.jpardogo.android.listbuddies;

import android.app.Application;
import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by jpardogo on 23/02/2014.
 */
public class ListBuddies extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        PgyCrashManager.register(this);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
