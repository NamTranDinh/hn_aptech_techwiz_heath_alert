package com.csupporter.techwiz.presentation.presenter.authentication;

import static com.csupporter.techwiz.utils.Const.PASSWORD_REGEX;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback.ErrorTo;
import com.csupporter.techwiz.utils.EncryptUtils;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter {

    public RegisterPresenter(BaseView baseView) {
        super(baseView);
    }

    public void verifyAccount(@NonNull Account account, String confPass, AuthenticationCallback.VerifyAccountCallBack callBack) {
        if (TextUtils.isEmpty(account.getFirstName())) {
            callBack.dataInvalid("Please enter your first name", ErrorTo.FIRST_NAME);
            return;
        }
        if (TextUtils.isEmpty(account.getLastName())) {
            callBack.dataInvalid("Please enter your last name", ErrorTo.LAST_NAME);
            return;
        }
        if (TextUtils.isEmpty(account.getEmail())) {
            callBack.dataInvalid("Please enter your email", ErrorTo.EMAIL);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(account.getEmail()).matches()) {
            callBack.dataInvalid("Email is invalid!", ErrorTo.EMAIL);
            return;
        }
        DataInjection.provideRepository().account.findAccountByEmail(account.getEmail(), account1 -> {
            if (account1 != null) {
                callBack.dataInvalid("Email had been taken", ErrorTo.EMAIL);
            } else {
                if (TextUtils.isEmpty(account.getPassword())) {
                    callBack.dataInvalid("Please enter your password", ErrorTo.PASSWORD);
                    return;
                }
                if (!Pattern.matches(PASSWORD_REGEX, account.getPassword())) {
                    callBack.dataInvalid("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", ErrorTo.PASSWORD);
                    return;
                }
                if (TextUtils.isEmpty(confPass)) {
                    callBack.dataInvalid("Please confirm your password", ErrorTo.CF_PASSWORD);
                    return;
                }
                if (!Pattern.matches(PASSWORD_REGEX, confPass)) {
                    callBack.dataInvalid("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", ErrorTo.CF_PASSWORD);
                    return;
                }
                if (!account.getPassword().equals(confPass)) {
                    callBack.dataInvalid("Password and confirm password are not the same", ErrorTo.NONE);
                    return;
                }
                callBack.verified();
            }
        }, throwable -> callBack.dataInvalid("Error: " + throwable, ErrorTo.NONE));
    }


    public void register(@NonNull Account account, AuthenticationCallback.RegisterCallBack callBack) {
        getBaseView().showLoading();
        account.setPassword(EncryptUtils.encrypt(account.getPassword()));
        DataInjection.provideRepository().account.addAccount(account,
                unused -> {
                    getBaseView().hideLoading();
                    callBack.registerSuccess();
                }, throwable -> {
                    getBaseView().hideLoading();
                    callBack.registerError();
                });
    }

}
