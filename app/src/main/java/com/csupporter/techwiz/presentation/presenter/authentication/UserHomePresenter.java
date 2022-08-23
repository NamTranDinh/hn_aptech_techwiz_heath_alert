package com.csupporter.techwiz.presentation.presenter.authentication;


import androidx.core.util.Consumer;

import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;


public class UserHomePresenter extends BasePresenter {

    public UserHomePresenter(BaseView baseView) {
        super(baseView);
    }

//    public void getUpcomingAppointment(Account account, MainViewCallBack.UserHomeCallBack callBack) {
//        getBaseView().showLoading();
//        DataInjection.provideRepository().appointment.getAppointments(account, appointmentList -> {
//            getBaseView().hideLoading();
//            callBack.listAppointment(appointmentList);
//        }, throwable -> getBaseView().hideLoading());
//    }


}
