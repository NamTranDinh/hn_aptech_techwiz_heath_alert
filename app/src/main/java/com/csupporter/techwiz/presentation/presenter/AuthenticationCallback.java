package com.csupporter.techwiz.presentation.presenter;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;

import java.util.List;

public abstract class AuthenticationCallback {

    public enum ErrorTo {
        FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, CF_PASSWORD, NONE
    }


    public interface LoginCallback {
        void dataInvalid(String alert);

        void loginSuccess(Account account);

        void loginError();
    }

    public interface VerifyAccountCallBack {

        void dataInvalid(String alert, ErrorTo errorTo);

        void verified();
    }

    public interface RegisterCallBack {

        void registerSuccess();

        void registerError();
    }

    public interface ForgotPasswordCallBack {
        void emailExist(Account account);

        void dataInvalid(String alert, ErrorTo errorTo);
    }

    public interface EnterOTPCallBack {
        void onSentOTPSuccess(int OTP);

        void onSentOTPFailure();

    }

    public interface ResetPasswordCallBack {
        void verified(String pw);

        void dataInvalid(String alert, ErrorTo errorTo);

    }

    public interface ChangePassCallback {
        void onSuccess(Account account);

        void onFailure();
    }


}
