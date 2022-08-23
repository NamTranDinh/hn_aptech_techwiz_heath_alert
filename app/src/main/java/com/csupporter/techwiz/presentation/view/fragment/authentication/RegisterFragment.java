package com.csupporter.techwiz.presentation.view.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.presentation.presenter.authentication.RegisterPresenter;
import com.csupporter.techwiz.presentation.presenter.authentication.SendOtpPresenter;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.WindowUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.toast.ToastUtils;

public class RegisterFragment extends BaseFragment implements View.OnClickListener, AuthenticationCallback.EnterOTPCallBack, AuthenticationCallback.VerifyAccountCallBack {

    private TextInputLayout txtFirstName;
    private TextInputLayout txtLastName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private TextInputLayout txtConfPassword;
    private RadioButton rbUser;

    private LoadingDialog dialog;
    private Account account;

    private RegisterPresenter registerPresenter;
    private SendOtpPresenter sendOtpPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registerPresenter = new RegisterPresenter(this);
        sendOtpPresenter = new SendOtpPresenter(this);
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_register);
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowUtils.setWindowBackground(getActivity(), R.drawable.background_register);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        return view;
    }

    private void init(@NonNull View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view1 -> popLastFragment());
        txtEmail = view.findViewById(R.id.register_email_layout);
        txtFirstName = view.findViewById(R.id.register_firstname_layout);
        txtLastName = view.findViewById(R.id.register_lastname_layout);
        txtPassword = view.findViewById(R.id.register_password_layout);
        txtConfPassword = view.findViewById(R.id.register_cf_password_layout);
        rbUser = view.findViewById(R.id.rb_user);
        AppCompatButton btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.btn_register) {
            account = getDataFromForm();
            String confPass = getText(txtConfPassword.getEditText());
            registerPresenter.verifyAccount(account, confPass, this);
        }
    }

    @NonNull
    private Account getDataFromForm() {
        String firstName = getText(txtFirstName.getEditText());
        String lastName = getText(txtLastName.getEditText());
        String email = getText(txtEmail.getEditText());
        String password = getText(txtPassword.getEditText());
        Account acc = new Account();
        acc.setId(FirebaseUtils.uniqueId());
        acc.setFirstName(firstName);
        acc.setLastName(lastName);
        acc.setEmail(email);
        acc.setType(rbUser.isChecked() ? Account.TYPE_USER : Account.TYPE_DOCTOR);
        acc.setStatus(Account.STATUS_ACTIVE);
        acc.setPassword(password);
        return acc;
    }

    @NonNull
    private String getText(EditText editText) {
        if (editText == null) {
            return "";
        } else {
            return editText.getText().toString().trim();
        }
    }


    @Override
    public void dataInvalid(String alert, @NonNull AuthenticationCallback.ErrorTo errorTo) {
        if (getContext() == null) return;
        switch (errorTo) {
            case NONE:
                break;
            case FIRST_NAME:
                showSoftInput(txtFirstName.getEditText());
                break;
            case EMAIL:
                showSoftInput(txtEmail.getEditText());
                break;
            case PASSWORD:
                showSoftInput(txtPassword.getEditText());
                break;
            case CF_PASSWORD:
                showSoftInput(txtConfPassword.getEditText());
                break;
        }
        showToast(alert, ToastUtils.ERROR, true);
    }

    @Override
    public void verified() {
            hideSoftInput();
            sendOtpPresenter.sendVerificationOtp(account, this);
    }

    @Override
    public void onSentOTPSuccess(int OTP) {
        if (getContext() != null) {
            showToast("An otp had been sent. Please check your email!", ToastUtils.INFO);
            Fragment fragment = EnterOTPFragment.newInstance(account, OTP, EnterOTPFragment.FROM_REGISTER);
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