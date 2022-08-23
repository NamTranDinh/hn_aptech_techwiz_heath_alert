package com.csupporter.techwiz.presentation.presenter.authentication;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.repository.ImageManager;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;


public class NavUserInfoPresenter extends BasePresenter {

    public NavUserInfoPresenter(BaseView baseView) {
        super(baseView);
    }

    public void logOut() {
        DataInjection.provideSettingPreferences().setToken(null);
    }

    public void uploadAvatar(Account account, byte[] source, MainViewCallBack.UploadAvatarCallback callback) {
        getBaseView().showLoading();
        DataInjection.provideRepository().imageManager
                .upload(ImageManager.Type.AVATAR, App.getApp().getAccount().getId(), source,
                        uri -> {
                            getBaseView().hideLoading();
                            callback.onSuccess(uri.toString());
                            account.setAvatar(uri.toString());
                            DataInjection.provideRepository().account.updateAccount(account, null, null);
                        }, throwable -> {
                            getBaseView().hideLoading();
                            callback.onError(throwable);
                        });
    }

    public void uploadCertificate(Account account, byte[] source, MainViewCallBack.UpdateCertificate callback) {
        getBaseView().showLoading();
        DataInjection.provideRepository().imageManager
                .upload(ImageManager.Type.CERTIFICATION, App.getApp().getAccount().getId(), source,
                        uri -> {
                            getBaseView().hideLoading();
                            callback.onSuccess(uri.toString());
                            account.setCertificationUrl(uri.toString());
                            DataInjection.provideRepository().account.updateAccount(account, null, null);
                        }, throwable -> {
                            getBaseView().hideLoading();
                            callback.onError(throwable);
                        });
    }

}
