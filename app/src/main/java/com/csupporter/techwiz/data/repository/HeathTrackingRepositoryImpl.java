package com.csupporter.techwiz.data.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.repository.HeathTrackingRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HeathTrackingRepositoryImpl implements HeathTrackingRepository {

    private final static String DEFAULT_PATH = "health_tracking";

    @Override
    public void addTracking(HealthTracking tracking, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, tracking.getId(), tracking, onSuccess, onError);
    }

    @Override
    public void updateTracking(HealthTracking tracking, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, tracking.getId(), tracking, onSuccess, onError);
    }

    @Override
    public void deleteTracking(HealthTracking tracking, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.deleteData(DEFAULT_PATH, tracking.getId(), onSuccess, onError);
    }

    @Override
    public void getAllHealthTracking(Account account, @Nullable Consumer<List<HealthTracking>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("userId", account.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("ddd", "getAllHealthTracking: " + queryDocumentSnapshots.size());
                    List<HealthTracking> healthTrackingList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        HealthTracking health = document.toObject(HealthTracking.class);
                        if (health != null) {
                            health.setId(document.getId());
                            healthTrackingList.add(health);
                        }
                    }
                    FirebaseUtils.success(onSuccess, healthTrackingList);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void getHealthTrackingByTimeSpace(Account account, long startDate, long endDate, @Nullable Consumer<List<HealthTracking>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("userId", account.getId())
                .whereGreaterThanOrEqualTo("createAt", startDate)
                .whereLessThanOrEqualTo("createAt", endDate)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<HealthTracking> healthTrackingList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        HealthTracking health = document.toObject(HealthTracking.class);
                        if (health != null) {
                            health.setId(document.getId());
                            healthTrackingList.add(health);
                        }
                    }
                    FirebaseUtils.success(onSuccess, healthTrackingList);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void checkAddOnlyOneHealthTracking(Account account, @Nullable Consumer<HealthTracking> onSuccess, @Nullable Consumer<Throwable> onError) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("userId", account.getId())
                .whereGreaterThan("createAt", calendar.getTime().getTime())
                .whereLessThanOrEqualTo("createAt", calendar.getTime().getTime() + 24 * 60 * 60 * 1000L)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        FirebaseUtils.success(onSuccess, null);
                    } else {
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        HealthTracking healthTracking = snapshot.toObject(HealthTracking.class);
                        if (healthTracking != null) {
                            healthTracking.setId(snapshot.getId());
                            FirebaseUtils.success(onSuccess, healthTracking);
                        } else {
                            FirebaseUtils.success(onSuccess, null);
                        }
                    }
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }
}
