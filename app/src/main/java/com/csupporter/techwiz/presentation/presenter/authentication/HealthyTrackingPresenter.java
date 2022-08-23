package com.csupporter.techwiz.presentation.presenter.authentication;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.di.DataInjection;

import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Consumer;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

public class HealthyTrackingPresenter extends BasePresenter {


    public HealthyTrackingPresenter(BaseView baseView) {
        super(baseView);
    }

    public void addNewHealthTracking(String txtHeight, String txtWeight, String txtHeartBeat, String txtBloodSugar, String txtBloodPressure, String txtNote, MainViewCallBack.HealthTrackingCallBack callBack) {

        HealthTracking healthTracking = verifyDataInput(txtWeight, txtHeight, txtBloodSugar, txtBloodPressure, txtHeartBeat, txtNote, callBack);

        if (healthTracking != null) {
            healthTracking.setCreateAt(System.currentTimeMillis());
            DataInjection.provideRepository().heathTracking.addTracking(healthTracking, unused -> {

                getBaseView().hideLoading();
                callBack.addHealthTrackingSuccess(healthTracking);
            }, throwable -> {
                getBaseView().hideLoading();
                callBack.addHealthTrackingFail("Add new health tracking fail!");
            });
        }
    }

    public void updateHealthTrack(HealthTracking newTrack, MainViewCallBack.UpdateTrackCallBack callBack){
        getBaseView().showLoading();
        DataInjection.provideRepository().heathTracking.updateTracking(newTrack, unused -> {
            callBack.onSuccess();
            getBaseView().hideLoading();
        }, throwable -> {
            callBack.onFailure();
            getBaseView().hideLoading();
        });
    }

    public void deleteHealthTrack(HealthTracking tracking, MainViewCallBack.DeleteTrackCallBack callBack){
        getBaseView().showLoading();
        DataInjection.provideRepository().heathTracking.deleteTracking(tracking, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                callBack.onDeleteSuccess();
                getBaseView().hideLoading();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                callBack.onDeleteFailure();
                getBaseView().hideLoading();
            }
        });
    }

    public HealthTracking verifyDataInput(String weight, String height, String bloodSugar, String bloodPressure,
                                          String heartBeat, String note, MainViewCallBack.HealthTrackingCallBack callBack) {
        Account account = App.getApp().getAccount();

        if (height.isEmpty() || weight.isEmpty() ||
                bloodSugar.isEmpty() || bloodPressure.isEmpty() ||
                heartBeat.isEmpty()) {

            getBaseView().hideLoading();
            callBack.addHealthTrackingFail("Please complete all information !");


        } else {
            HealthTracking healthTracking = new HealthTracking();

            healthTracking.setId(FirebaseUtils.uniqueId());
            healthTracking.setUserId(account.getId());
            healthTracking.setHeight(Float.parseFloat(height));
            healthTracking.setWeight(Float.parseFloat(weight));
            healthTracking.setHeartbeat(Float.parseFloat(heartBeat));
            healthTracking.setBloodSugar(Float.parseFloat(bloodSugar));
            healthTracking.setBloodPressure(Float.parseFloat(bloodPressure));
            healthTracking.setOther(note);
            healthTracking.setCreateAt(System.currentTimeMillis());
            return healthTracking;
        }
        return null;
    }

    public void getListTrack(long startDate, long endDate, MainViewCallBack.HealthTrackingCallBack callBack) {

        DataInjection.provideRepository().heathTracking.getHealthTrackingByTimeSpace(App.getApp().getAccount(), startDate, endDate, new Consumer<List<HealthTracking>>() {
            @Override
            public void accept(List<HealthTracking> healthTrackings) {
                Log.d("ddd", "accept: " + healthTrackings);
                if (healthTrackings != null) {
                    Log.d("ddd", "accept: " + healthTrackings.size());
                }
                callBack.onGetDataSuccess(healthTrackings);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                Log.d("ddd", "accept: ", throwable);
                callBack.onGetDataFailure();
            }
        });
    }

    public void checkAddOnlyOneHealthTracking(MainViewCallBack.HealthTrackingCallBack callBack) {
        Account account = App.getApp().getAccount();
        getBaseView().showLoading();
        DataInjection.provideRepository().heathTracking.checkAddOnlyOneHealthTracking(account, healthTracking -> {
            getBaseView().hideLoading();
            callBack.checkAddOnlyOne(healthTracking);
        }, throwable -> getBaseView().hideLoading());
    }

}
