package com.csupporter.techwiz.presentation.presenter.authentication;

import androidx.core.util.Consumer;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.view.dialog.AddNewPrescriptionDialog;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

public class AllPrescriptionPresenter extends BasePresenter {

    public AllPrescriptionPresenter(BaseView baseView) {
        super(baseView);
    }

    public void createPrescription(Prescription prescription, AddNewPrescriptionDialog dialog, MainViewCallBack.CreatePrescriptionCallBack callBack) {
        getBaseView().showLoading();
        DataInjection.provideRepository().prescription.createNewPrescription(prescription, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                getBaseView().hideLoading();

                callBack.onCreateSuccess(prescription);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                getBaseView().hideLoading();
                callBack.onCreateFail();
            }
        });
    }

    public void getAllPrescription(MainViewCallBack.GetAllPrescriptionCallBack callBack) {
        getBaseView().showLoading();
        Account account = App.getApp().getAccount();
        DataInjection.provideRepository().prescription.getAllPrescription(account, new Consumer<List<Prescription>>() {
            @Override
            public void accept(List<Prescription> prescriptionList) {
                getBaseView().hideLoading();
                callBack.allPrescriptions(prescriptionList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                getBaseView().hideLoading();
            }
        });
    }
}
