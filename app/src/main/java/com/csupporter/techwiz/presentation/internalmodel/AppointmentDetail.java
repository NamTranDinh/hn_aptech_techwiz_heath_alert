package com.csupporter.techwiz.presentation.internalmodel;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.google.firebase.firestore.auth.User;

public class AppointmentDetail  {

    private Appointment appointment;
    private Account mAcc;

    public AppointmentDetail(Appointment appointment, Account mAcc) {
        this.appointment = appointment;
        this.mAcc = mAcc;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Account getAcc() {
        return mAcc;
    }

    public void setAcc(Account mAcc) {
        this.mAcc = mAcc;
    }
}
