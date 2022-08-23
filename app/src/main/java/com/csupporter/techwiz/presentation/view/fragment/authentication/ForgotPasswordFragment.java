package com.csupporter.techwiz.presentation.view.fragment.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.authentication.ForgotPasswordPresenter;
import com.csupporter.techwiz.presentation.presenter.authentication.SendOtpPresenter;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.WindowUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.toast.ToastUtils;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener, AuthenticationCallback.ForgotPasswordCallBack, AuthenticationCallback.EnterOTPCallBack {
    private LoadingDialog dialog;
    private Button btnSubmit;
    private EditText edtEmail;
    private TextView tvBackToLogin;
    private Account account;

    private ForgotPasswordPresenter forgotPasswordPresenter;
    private SendOtpPresenter sendOtpPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this);
        sendOtpPresenter = new SendOtpPresenter(this);
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_forgot_password);
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_forgot_password);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        eventClick();
    }

    private void eventClick() {
        btnSubmit.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_get_otp:
                String email = edtEmail.getText().toString().trim();
                forgotPasswordPresenter.verifyEmailAndFind(email, this);
                break;
            case R.id.tv_back_to_login:
                popLastFragment();
                break;
        }
    }

    private void initView(@NonNull View view) {
        btnSubmit = view.findViewById(R.id.btn_get_otp);
        edtEmail = view.findViewById(R.id.edt_enter_email);
        tvBackToLogin = view.findViewById(R.id.tv_back_to_login);
    }

    @Override
    public void emailExist(Account account) {
            hideSoftInput();
            this.account = account;
            sendOtpPresenter.sentForgotPassOtp(account, this);
    }

    @Override
    public void dataInvalid(String alert, @NonNull AuthenticationCallback.ErrorTo errorTo) {
        if (getContext() == null) return;
        switch (errorTo) {
            case NONE:
                break;
            case EMAIL:
                showSoftInput(edtEmail);
                break;
        }
        showToast(alert, ToastUtils.ERROR, true);
    }

    @Override
    public void onSentOTPSuccess(int OTP) {
        if (getContext() != null) {
            showToast("An otp had been sent. Please check your email!", ToastUtils.INFO);
            Fragment fragment = EnterOTPFragment.newInstance(account, OTP, EnterOTPFragment.FROM_FORGOT_PW);
            replaceFragment(fragment, true, BaseActivity.Anim.TRANSIT_FADE);
        }
    }

    @Override
    public void onSentOTPFailure() {
        showToast("Sent otp false", ToastUtils.ERROR);
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
}
