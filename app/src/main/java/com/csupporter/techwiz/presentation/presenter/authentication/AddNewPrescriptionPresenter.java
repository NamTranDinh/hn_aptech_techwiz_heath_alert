package com.csupporter.techwiz.presentation.presenter.authentication;

import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Consumer;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;
import com.csupporter.techwiz.domain.repository.ImageManager;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;
import com.mct.components.toast.ToastUtils;

public class AddNewPrescriptionPresenter extends BasePresenter {

    public AddNewPrescriptionPresenter(BaseView baseView) {
        super(baseView);
    }

//    public void createPrescriptionDetail(PrescriptionDetail prescriptionDetail, MainViewCallBack.CreatePrescriptionDetailCallBack callBack) {
//        getBaseView().showLoading();
//        DataInjection.provideRepository().prescriptionDetail.createPrescriptionDetail(prescriptionDetail.getPrescriptionId(), new Consumer<Void>() {
//            @Override
//            public void accept(Void unused) {
//                getBaseView().hideLoading();
//                callBack.createPrescriptionDetailSuccess();
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) {
//                getBaseView().hideLoading();
//                callBack.createPrescriptionFail();
//            }
//        });
//    }

    public void createPrescriptionDetail(PrescriptionDetail prescriptionDetail, byte[] source, MainViewCallBack.CreatePrescriptionDetailCallBack callback) {

        getBaseView().showLoading();
        DataInjection.provideRepository().imageManager
                .upload(ImageManager.Type.MEDICINE, prescriptionDetail.getId(), source,
                        uri -> {
                            getBaseView().hideLoading();
                            prescriptionDetail.setImageUrl(uri.toString());

                            DataInjection.provideRepository().prescriptionDetail.createPrescriptionDetail(prescriptionDetail, new Consumer<Void>() {
                                @Override
                                public void accept(Void unused) {
                                    getBaseView().hideLoading();
                                    callback.createPrescriptionDetailSuccess();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    getBaseView().hideLoading();
                                    callback.createPrescriptionFail();
                                }
                            });
                        }, throwable -> {
                            getBaseView().hideLoading();
                            callback.createPrescriptionFail();
                        });

    }


    public void editPrescription(PrescriptionDetail prescriptionDetail, MainViewCallBack.EditPrescriptionDetail callback) {
        getBaseView().showLoading();
        DataInjection.provideRepository().prescriptionDetail.updatePrescriptionDetail(prescriptionDetail, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                getBaseView().hideLoading();
                callback.editPrescriptionDetailSuccess(prescriptionDetail);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                getBaseView().hideLoading();
                Log.e("ddd", "accept: ", throwable);
                callback.editPrescriptionDetailFail();
            }
        });
    }

}
