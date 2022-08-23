package com.csupporter.techwiz.presentation.presenter.authentication;

import androidx.core.util.Consumer;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

public class ListPrescriptionDetailPresenter extends BasePresenter {

    public ListPrescriptionDetailPresenter(BaseView baseView) {
        super(baseView);
    }

    public void getAllPrescriptionDetail(Prescription prescription, MainViewCallBack.ListPrescriptionDetailCallback callback) {
        getBaseView().showLoading();

        DataInjection.provideRepository().listPrescriptionDetail
                .getAllPrescriptionDetail(prescription, prescriptionDetailList -> {

                    getBaseView().hideLoading();
                    callback.getAllListPrescription(prescriptionDetailList);

                }, throwable -> {

                    getBaseView().hideLoading();

                });
    }

    public void getUserCreatedPrescription(Prescription prescription, MainViewCallBack.GetUserCreatedPrescription callback) {
        getBaseView().showLoading();

        DataInjection.provideRepository().account
                .findAccountById(prescription.isUserCreate() ? prescription.getUserId() : prescription.getDoctorId(), account -> {

                    getBaseView().hideLoading();
                    callback.getUserCreatedPrescription(account);

                }, throwable -> getBaseView().hideLoading());

    }

    public void deletePrescription(PrescriptionDetail prescriptionDetail, int pos, MainViewCallBack.DeletePrescriptionDetail callback) {
        getBaseView().showLoading();
        DataInjection.provideRepository().prescriptionDetail.deletePrescriptionDetail(prescriptionDetail, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                getBaseView().hideLoading();
                callback.deletePrescriptionDetailSuccess(prescriptionDetail, pos);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                getBaseView().hideLoading();
                callback.deletePrescriptionDetailFail();
            }
        });
    }

}
