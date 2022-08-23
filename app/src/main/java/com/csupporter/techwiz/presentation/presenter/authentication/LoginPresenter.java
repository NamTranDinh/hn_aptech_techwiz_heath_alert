package com.csupporter.techwiz.presentation.presenter.authentication;

import static com.csupporter.techwiz.utils.Const.PASSWORD_REGEX;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.utils.EncryptUtils;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.regex.Pattern;

public class LoginPresenter extends BasePresenter {

    public LoginPresenter(BaseView baseView) {
        super(baseView);
    }

    public void login(String userName, String password, AuthenticationCallback.LoginCallback callback) {

        if (!verifyDataInputLogin(userName, password, callback)) {
            return;
        }

        getBaseView().showLoading();
        password = EncryptUtils.encrypt(password);
        DataInjection.provideRepository().account.checkUserNameAndPassword(userName, password, account -> {
            getBaseView().hideLoading();
            if (account != null) {
                callback.loginSuccess(account);
            } else {
                callback.dataInvalid("Email or password is wrong !");
            }
        }, throwable -> {
            getBaseView().hideLoading();
            callback.loginError();
        });
    }


    private boolean verifyDataInputLogin(@NonNull String userName, String password, AuthenticationCallback.LoginCallback callback) {
        if (userName.isEmpty() || password.isEmpty()) {
            callback.dataInvalid("Please complete the input field !");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            getBaseView().hideLoading();
            callback.dataInvalid("Email is invalid !");
            return false;
        }

        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            callback.dataInvalid(
                    "Password must contain at least one uppercase letter, lowercase letter and number!");
            return false;
        }
        return true;
    }
}
