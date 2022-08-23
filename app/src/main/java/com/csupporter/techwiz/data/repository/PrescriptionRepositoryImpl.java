package com.csupporter.techwiz.data.repository;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.repository.PrescriptionRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final static String DEFAULT_PATH = "prescriptions";

    @Override
    public void createNewPrescription(Prescription prescription, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, prescription.getId(), prescription, onSuccess, onError);
    }

    @Override
    public void getAllPrescription(Account account, @Nullable Consumer<List<Prescription>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo(account.isUser() ? "userId" : "doctorId", account.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Prescription> prescriptionsList = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Prescription prescription = snapshot.toObject(Prescription.class);
                        if (prescription != null) {
                            prescription.setId(snapshot.getId());
                            prescriptionsList.add(prescription);
                        }
                    }
                    FirebaseUtils.success(onSuccess, prescriptionsList);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }


}
