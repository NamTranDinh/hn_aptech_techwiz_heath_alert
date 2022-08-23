package com.csupporter.techwiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.view.fragment.docters.DoctorFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.MainFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.toast.ToastUtils;

public class MainActivity extends BaseActivity {

    private static final String KEY_ACCOUNT = "key_account";

    public static void startActivity(Context context, @NonNull Account account) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account account = (Account) getIntent().getSerializableExtra(KEY_ACCOUNT);
        if (account == null) {
            finish();
        } else {
            App.getApp().setAccount(account);
            DataInjection.provideSettingPreferences().setToken(account.getId());
            if (account.getType() == Account.TYPE_USER) {
                replaceFragment(new MainFragment());
            } else {
                replaceFragment(new DoctorFragment());
            }
        }
    }

    @Override
    protected int getContainerId() {
        return Window.ID_ANDROID_CONTENT;
    }

    @Override
    protected void showToastOnBackPressed() {
        ToastUtils.makeWarningToast(this, Toast.LENGTH_SHORT, getString(R.string.toast_back_press), false).show();
    }

}