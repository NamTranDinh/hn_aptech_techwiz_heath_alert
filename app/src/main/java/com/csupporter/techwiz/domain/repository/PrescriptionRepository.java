package com.csupporter.techwiz.domain.repository;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.Prescription;

import java.util.List;

public interface PrescriptionRepository {

    void createNewPrescription(Prescription prescription, @Nullable Consumer<Void> onSuccess,
                               @Nullable Consumer<Throwable> onError);

    void getAllPrescription(Account account, @Nullable Consumer<List<Prescription>> onSuccess,
                            @Nullable Consumer<Throwable> onError);
}
