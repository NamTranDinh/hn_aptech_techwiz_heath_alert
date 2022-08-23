package com.csupporter.techwiz;

import android.app.Application;
import android.content.Context;

import com.csupporter.techwiz.domain.model.Account;

import javax.annotation.Nullable;

public class App extends Application {

    private static App sInstance;
    private Account account;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getApp() {
        return sInstance;
    }

    public static Context getContext() {
        return getApp().getApplicationContext();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
