package com.csupporter.techwiz.presentation.presenter.authentication;

import static com.csupporter.techwiz.utils.Const.PASSWORD_REGEX;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.csupporter.techwiz.utils.EncryptUtils;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.regex.Pattern;

public class ResetPasswordPresenter extends BasePresenter {

    public ResetPasswordPresenter(BaseView baseView) {
        super(baseView);
    }

    public void verifyPasswords(@NonNull String newPass, String reNewPass, AuthenticationCallback.ResetPasswordCallBack callBack) {
        if (TextUtils.isEmpty(newPass)) {
            callBack.dataInvalid("Please enter new password", AuthenticationCallback.ErrorTo.PASSWORD);
            return;
        }
        if (!Pattern.matches(PASSWORD_REGEX, newPass)) {
            callBack.dataInvalid("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", AuthenticationCallback.ErrorTo.PASSWORD);
            return;
        }
        if (TextUtils.isEmpty(reNewPass)) {
            callBack.dataInvalid("Please confirm new password", AuthenticationCallback.ErrorTo.CF_PASSWORD);
            return;
        }
        if (!Pattern.matches(PASSWORD_REGEX, reNewPass)) {
            callBack.dataInvalid("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", AuthenticationCallback.ErrorTo.CF_PASSWORD);
            return;
        }
        if (!newPass.equals(reNewPass)) {
            callBack.dataInvalid("Password and confirm password are not the same", AuthenticationCallback.ErrorTo.NONE);
            return;
        }
        callBack.verified(newPass);
    }

    public void resetPassword(@NonNull Account account, AuthenticationCallback.ChangePassCallback callback) {
        account.setPassword(EncryptUtils.encrypt(account.getPassword()));
        DataInjection.provideRepository().account.updateAccount(account,
                unused -> callback.onSuccess(account),
                throwable -> callback.onFailure());
    }
}
