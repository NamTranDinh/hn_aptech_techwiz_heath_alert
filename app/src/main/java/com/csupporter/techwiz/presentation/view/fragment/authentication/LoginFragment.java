package com.csupporter.techwiz.presentation.view.fragment.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.presentation.presenter.authentication.LoginPresenter;
import com.csupporter.techwiz.presentation.view.activities.MainActivity;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.WindowUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.toast.ToastUtils;

public class LoginFragment extends BaseFragment implements View.OnClickListener, AuthenticationCallback.LoginCallback {

    private TextInputLayout tvUserName;
    private TextInputLayout tvPassword;
    private LoadingDialog dialog;

    private LoginPresenter loginPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginPresenter = new LoginPresenter(this);
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_login);
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_login);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    private void init(@NonNull View view) {
        tvUserName = view.findViewById(R.id.login_username_layout);
        tvPassword = view.findViewById(R.id.login_password_layout);
        TextView tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
        TextView tvRegisterNow = view.findViewById(R.id.tv_register_now);
        tvForgotPassword.setOnClickListener(this);
        tvRegisterNow.setOnClickListener(this);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvRegisterNow.setPaintFlags(tvRegisterNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        view.findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_register_now:
                replaceFragment(new RegisterFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
            case R.id.tv_forgot_password:
                replaceFragment(new ForgotPasswordFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
            case R.id.btn_login:
                String userName = getText(tvUserName.getEditText());
                String password = getText(tvPassword.getEditText());
                loginPresenter.login(userName, password, this);
                break;
        }
    }

    @Override
    public void dataInvalid(String alert) {
        showToast(alert, ToastUtils.WARNING, true);
    }

    @Override
    public void loginSuccess(Account account) {
        if (getActivity() != null) {
            hideSoftInput();
            MainActivity.startActivity(getActivity(), account);
            getActivity().finish();
        }
    }

    @Override
    public void loginError() {
        showToast("Login Fail!", ToastUtils.ERROR, true);
    }

    @Override
    public void showLoading() {
        if (getContext() == null) return;
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new LoadingDialog(getContext());
        dialog.create(null);
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @NonNull
    private String getText(EditText editText) {
        if (editText == null) {
            return "";
        } else {
            return editText.getText().toString().trim();
        }
    }
}