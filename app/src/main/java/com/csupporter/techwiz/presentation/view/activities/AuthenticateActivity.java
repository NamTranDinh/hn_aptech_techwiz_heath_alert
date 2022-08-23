package com.csupporter.techwiz.presentation.view.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.presentation.view.fragment.authentication.LoginFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.toast.ToastUtils;

public class AuthenticateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(new LoginFragment());
    }

    @Override
    protected int getContainerId() {
        return android.R.id.content;
    }

    @Override
    protected void showToastOnBackPressed() {
        ToastUtils.makeWarningToast(this, Toast.LENGTH_SHORT, getString(R.string.toast_back_press), false).show();
    }

}