package com.csupporter.techwiz.presentation.presenter.authentication;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

public class ProfilePresenter extends BasePresenter {

    public ProfilePresenter(BaseView baseView) {
        super(baseView);
    }

    public void updateProfile(Account account, @NonNull MainViewCallBack.UpdateProfileCallback callback) {
        getBaseView().showLoading();

        DataInjection.provideRepository().account.updateAccount(account, unused -> {
            getBaseView().hideLoading();
            callback.onSuccess();
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                getBaseView().hideLoading();
                callback.onError(throwable);
            }
        });
    }
}
