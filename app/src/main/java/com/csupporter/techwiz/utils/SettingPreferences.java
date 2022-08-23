package com.csupporter.techwiz.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.domain.model.Account;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class SettingPreferences {

    ///////////////////////////////////////////////////////////////////////////
    // KEY PREF
    ///////////////////////////////////////////////////////////////////////////

    private static final String PREF_NAME = "setting_preferences";
    private static final String KEY_TOKEN = "key_tokens";

    ///////////////////////////////////////////////////////////////////////////
    // INSTANCE
    ///////////////////////////////////////////////////////////////////////////

    private final SharedPreferences mSharedPreferences;

    private SettingPreferences() {
        mSharedPreferences = App.getApp().getSharedPreferences(PREF_NAME, 0);
    }

    private static class Holder {
        private static final SettingPreferences INSTANCE = new SettingPreferences();
    }

    public static SettingPreferences getInstance() {
        return Holder.INSTANCE;
    }

    private SharedPreferences.Editor editor() {
        return mSharedPreferences.edit();
    }

    ///////////////////////////////////////////////////////////////////////////
    // SETTING HERE
    ///////////////////////////////////////////////////////////////////////////

    public void setToken(String userId) {
        if (TextUtils.isEmpty(userId)){
            editor().putString(KEY_TOKEN, "").apply();
            return;
        }
        byte[] token = Base64.encode(userId.getBytes(StandardCharsets.UTF_8), 0);
        editor().putString(KEY_TOKEN, new String(token)).apply();
    }

    public String getToken() {
        String token = mSharedPreferences.getString(KEY_TOKEN, null);
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        byte[] jsonByte = Base64.decode(token.getBytes(StandardCharsets.UTF_8), 0);
        return new String(jsonByte);
    }
}
