package com.csupporter.techwiz.presentation.presenter.authentication;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.AppointmentSchedule;
import com.csupporter.techwiz.domain.model.MyDoctor;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.ArrayList;
import java.util.List;

public class AddAppointmentPresenter extends BasePresenter {

    public AddAppointmentPresenter(BaseView baseView) {
        super(baseView);
    }

    public void getAllMyDoctor(MainViewCallBack.GetAllMyDoctorCallBack callBack) {
        Account acc = App.getApp().getAccount();
        DataInjection.provideRepository().myDoctor.getAllMyDoctor(acc, new Consumer<List<MyDoctor>>() {
            int count;
            final List<Account> doctorList = new ArrayList<>();

            @Override
            public void accept(List<MyDoctor> myDoctorList) {
                getBaseView().hideLoading();
                if (myDoctorList.isEmpty()) {
                    callBack.allMyDoctor(doctorList);
                    return;
                }
                for (MyDoctor data : myDoctorList) {
                    DataInjection.provideRepository().account.findAccountById(data.getDoctorId(), account -> {
                        if (account != null) {
                            doctorList.add(account);
                        }
                        ++count;
                        if (count == myDoctorList.size()) {
                            callBack.allMyDoctor(doctorList);
                        }
                    }, throwable -> ++count);
                }
            }
        }, null);
    }


    public void createAppointment(Appointment appointment, AppointmentSchedule schedule, @NonNull MainViewCallBack.CreateAppointmentCallback callback) {
        getBaseView().showLoading();
        DataInjection.provideRepository().appointment.addAppointment(appointment, schedule, unused -> {
            getBaseView().hideLoading();
            callback.onCreateSuccess();
        }, throwable -> {
            getBaseView().hideLoading();
            callback.onError(throwable);
        });
    }
}
