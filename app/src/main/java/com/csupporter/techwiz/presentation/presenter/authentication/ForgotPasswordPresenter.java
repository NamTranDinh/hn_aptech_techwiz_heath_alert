package com.csupporter.techwiz.presentation.presenter.authentication;

import android.text.TextUtils;
import android.util.Patterns;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

public class ForgotPasswordPresenter extends BasePresenter {

    public ForgotPasswordPresenter(BaseView baseView) {
        super(baseView);
    }

    public void verifyEmailAndFind(String email, AuthenticationCallback.ForgotPasswordCallBack callBack) {
        if (TextUtils.isEmpty(email)) {
            callBack.dataInvalid("Please enter your email", AuthenticationCallback.ErrorTo.EMAIL);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callBack.dataInvalid("Email is invalid!", AuthenticationCallback.ErrorTo.EMAIL);
            return;
        }
        getBaseView().showLoading();
        DataInjection.provideRepository().account.findAccountByEmail(email, account -> {
            if (account != null) {
                callBack.emailExist(account);
            } else {
                getBaseView().hideLoading();
                callBack.dataInvalid("Account isn't exits", AuthenticationCallback.ErrorTo.NONE);
            }
        }, throwable -> {
            getBaseView().hideLoading();
            callBack.dataInvalid("Error: " + throwable, AuthenticationCallback.ErrorTo.NONE);
        });
    }

}
