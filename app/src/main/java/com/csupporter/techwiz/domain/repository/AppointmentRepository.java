package com.csupporter.techwiz.domain.repository;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.AppointmentSchedule;

import java.util.List;

public interface AppointmentRepository {

    void addAppointment(Appointment appointment,
                        AppointmentSchedule appointmentSchedule,
                        @Nullable Consumer<Void> onSuccess,
                        @Nullable Consumer<Throwable> onError);

    void updateAppointment(Appointment appointment,
                           AppointmentSchedule appointmentSchedule,
                           @Nullable Consumer<Void> onSuccess,
                           @Nullable Consumer<Throwable> onError);

    void getAppointments(Account account,
                         @Nullable Consumer<List<Appointment>> onSuccess,
                         @Nullable Consumer<Throwable> onError);

    void getAppointmentsByDate(Account account,
                               @Nullable Consumer<List<Appointment>> onSuccess,
                               @Nullable Consumer<Throwable> onError
    );

    void getAppointmentByDateAndStatus(Account account,
                                       long date,
                                       List<Integer> status,
                                       @Nullable Consumer<List<Appointment>> onSuccess,
                                       @Nullable Consumer<Throwable> onError);
}
