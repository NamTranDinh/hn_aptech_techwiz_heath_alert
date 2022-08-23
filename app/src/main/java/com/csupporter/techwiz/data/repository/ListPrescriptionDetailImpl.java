package com.csupporter.techwiz.data.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;
import com.csupporter.techwiz.domain.repository.ListPrescriptionDetailRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListPrescriptionDetailImpl implements ListPrescriptionDetailRepository {

    private static final String DEFAULT_PATH = "prescription_details";

    @Override
    public void getAllPrescriptionDetail(Prescription prescription, @Nullable Consumer<List<PrescriptionDetail>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("prescriptionId", prescription.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PrescriptionDetail> prescriptionDetailsList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        PrescriptionDetail prescriptionDetails = document.toObject(PrescriptionDetail.class);
                        if (prescriptionDetails != null) {
                            prescriptionDetails.setId(document.getId());
                            prescriptionDetailsList.add(prescriptionDetails);
                        }
                    }
                    FirebaseUtils.success(onSuccess, prescriptionDetailsList);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }
}
