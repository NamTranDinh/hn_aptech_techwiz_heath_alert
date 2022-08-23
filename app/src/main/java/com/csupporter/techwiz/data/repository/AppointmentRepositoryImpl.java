package com.csupporter.techwiz.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.data.data_api.APIService;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.AppointmentSchedule;
import com.csupporter.techwiz.domain.repository.AppointmentRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final static String DEFAULT_PATH = "appointments";


    @Override
    public void addAppointment(Appointment appointment,
                               AppointmentSchedule appointmentSchedule,
                               @Nullable Consumer<Void> onSuccess,
                               @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, appointment.getId(), appointment, onSuccess, onError);
        APIService.getService().addAppointment(new Gson().toJson(appointmentSchedule)).enqueue(unUseCallback());
    }

    @Override
    public void updateAppointment(Appointment appointment,
                                  AppointmentSchedule appointmentSchedule,
                                  @Nullable Consumer<Void> onSuccess,
                                  @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, appointment.getId(), appointment, onSuccess, onError);
        APIService.getService().updateAppointment(new Gson().toJson(appointmentSchedule)).enqueue(unUseCallback());
    }

    @Override
    public void getAppointments(@NonNull Account account, @Nullable Consumer<List<Appointment>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo(account.isUser() ? "userId" : "doctorId", account.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Appointment> appointments = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Appointment appointment = snapshot.toObject(Appointment.class);
                        if (appointment != null) {
                            appointment.setId(snapshot.getId());
                            appointments.add(appointment);
                        }
                    }
                    FirebaseUtils.success(onSuccess, appointments);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void getAppointmentsByDate(Account account, @Nullable Consumer<List<Appointment>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo(account.isUser() ? "userId" : "doctorId", account.getId())
                .whereIn("status", Arrays.asList(0,1))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Appointment> appointments = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Appointment appointment = snapshot.toObject(Appointment.class);
                        if (appointment != null) {
                            appointment.setId(snapshot.getId());
                            appointments.add(appointment);
                        }
                    }
                    FirebaseUtils.success(onSuccess, appointments);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @NonNull
    private Callback<Void> unUseCallback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        };
    }

    @Override
    public void getAppointmentByDateAndStatus(@NonNull Account account, long date, List<Integer> status, @Nullable Consumer<List<Appointment>> onSuccess, @Nullable Consumer<Throwable> onError) {
        Log.e("ddd", "getAppointmentByDateAndStatus: " + date + " " + status+ " " +System.currentTimeMillis());
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo(account.isUser() ? "userId" : "doctorId", account.getId())
                .whereIn("status", status)
                .whereGreaterThan("time", date)
                .whereLessThan("time", date + 24 * 60 * 60 * 1000)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.e("ddd", "getAppointmentByDateAndStatus: "+queryDocumentSnapshots.size() );
                    List<Appointment> appointments = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Appointment appointment = snapshot.toObject(Appointment.class);
                        if (appointment != null) {
                            appointment.setId(snapshot.getId());
                            appointments.add(appointment);
                        }
                    }
                    FirebaseUtils.success(onSuccess, appointments);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));

    }
}
