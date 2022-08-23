package com.csupporter.techwiz.domain.repository;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;

public interface PrescriptionDetailRepository {
    void createPrescriptionDetail(PrescriptionDetail prescription, @Nullable Consumer<Void> onSuccess,
                                  @Nullable Consumer<Throwable> onError);

    void updatePrescriptionDetail(PrescriptionDetail prescriptionDetail,
                                  @Nullable Consumer<Void> onSuccess,
                                  @Nullable Consumer<Throwable> onError);

    void deletePrescriptionDetail(PrescriptionDetail prescriptionDetail,
                                  @Nullable Consumer<Void> onSuccess,
                                  @Nullable Consumer<Throwable> onError);
}
