package com.csupporter.techwiz.presentation.presenter.doctor;

import com.csupporter.techwiz.di.DataInjection;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

public class DoctorProfilePresenter extends BasePresenter {

    public DoctorProfilePresenter(BaseView baseView) {
        super(baseView);
    }

    public void logOut(){
        DataInjection.provideSettingPreferences().setToken(null);
    }
}
