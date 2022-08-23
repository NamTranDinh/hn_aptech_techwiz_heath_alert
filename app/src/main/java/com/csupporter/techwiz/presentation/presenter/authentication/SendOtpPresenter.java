package com.csupporter.techwiz.presentation.presenter.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.AuthenticationCallback;
import com.google.gson.JsonObject;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOtpPresenter extends BasePresenter {

    public static final int TYPE_FORGOT_PASS = 1;
    public static final int TYPE_VERIFICATION = 2;

    public SendOtpPresenter(BaseView baseView) {
        super(baseView);
    }

    public void sentForgotPassOtp(@NonNull Account account, AuthenticationCallback.EnterOTPCallBack callBack) {
        Log.e("ddd", "sentForgotPassOtp: ");
        getBaseView().showLoading();
        DataInjection.provideDataService().sendMailOtp(null, account.getFirstName(), TYPE_FORGOT_PASS, account.getEmail())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        getBaseView().hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            int status = response.body().get("status").getAsInt();
                            // 200 is success
                            if (status == 200) {
                                int otp = response.body().get("data").getAsInt();
                                callBack.onSentOTPSuccess(otp);
                                return;
                            }
                        }
                        callBack.onSentOTPFailure();
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                        getBaseView().hideLoading();
                        callBack.onSentOTPFailure();
                    }
                });
    }

    public void sendVerificationOtp(@NonNull Account account, AuthenticationCallback.EnterOTPCallBack callBack) {
        getBaseView().showLoading();
        DataInjection.provideDataService().sendMailOtp(null, account.getFirstName(), TYPE_VERIFICATION, account.getEmail())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        getBaseView().hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            int status = response.body().get("status").getAsInt();
                            // 200 is success
                            if (status == 200) {
                                int otp = response.body().get("data").getAsInt();
                                callBack.onSentOTPSuccess(otp);
                                return;
                            }
                        }
                        callBack.onSentOTPFailure();
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                        getBaseView().hideLoading();
                        callBack.onSentOTPFailure();
                    }
                });
    }

}
