package com.csupporter.techwiz.presentation.view.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.authentication.ResetPasswordPresenter;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.presentation.view.activities.MainActivity;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.WindowUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener,
        AuthenticationCallback.ResetPasswordCallBack,
        AuthenticationCallback.ChangePassCallback,
        BaseActivity.OnBackPressed {
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    private ResetPasswordPresenter resetPasswordPresenter;

    private LoadingDialog dialog;
    private Account account;
    private EditText edtPassword, edtRePassword;
    private Button btnConfirm;

    View view;

    @NonNull
    public static ResetPasswordFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ACCOUNT, account);
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        resetPasswordPresenter = new ResetPasswordPresenter(this);
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
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        account = (Account) requireArguments().getSerializable(KEY_ACCOUNT);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView(view);
        eventClick();
    }


    @Override
    public boolean onBackPressed() {
        if (getContext() == null) {
            return false;
        }
        new ConfirmDialog(getContext(),
                ConfirmDialog.LAYOUT_HOLD_USER,
                R.drawable.ic_discard,
                "Do you want to cancel the password change?",
                new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                        popLastFragment();
                    }

                    @Override
                    public void onCancel(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                    }
                }).create(null);
        return true;
    }

    private void eventClick() {
        btnConfirm.setOnClickListener(this);
    }

    private void intiView(@NonNull View view) {
        edtPassword = view.findViewById(R.id.edt_enter_new_pw);
        edtRePassword = view.findViewById(R.id.edt_reenter_new_pw);
        btnConfirm = view.findViewById(R.id.btn_confirm);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.btn_confirm) {
            String newPass = edtPassword.getText().toString().trim();
            String reNewPass = edtRePassword.getText().toString().trim();
            resetPasswordPresenter.verifyPasswords(newPass, reNewPass, this);
        }
    }

    @Override
    public void verified(String pw) {
        account.setPassword(pw);
        resetPasswordPresenter.resetPassword(account, this);
    }

    @Override
    public void dataInvalid(String alert, @NonNull AuthenticationCallback.ErrorTo errorTo) {
        if (getContext() == null) return;
        switch (errorTo) {
            case NONE:
                break;
            case PASSWORD:
                showSoftInput(edtPassword);
                break;
            case CF_PASSWORD:
                showSoftInput(edtRePassword);
                break;
        }
        showToast(alert, ToastUtils.ERROR, true);
    }

    @Override
    public void onSuccess(Account account) {
        if (getActivity() != null) {
            MainActivity.startActivity(getActivity(), account);
            getActivity().finish();
        }
    }

    @Override
    public void onFailure() {
        showToast("Some thing wrong!! Let's try again! ", ToastUtils.ERROR);
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
