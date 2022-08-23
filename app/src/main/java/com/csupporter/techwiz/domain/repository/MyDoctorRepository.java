package com.csupporter.techwiz.domain.repository;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.model.MyDoctor;

import java.util.List;


public interface MyDoctorRepository {

    void createMyDoctor(MyDoctor myDoctor,
                        @Nullable Consumer<Void> onSuccess,
                        @Nullable Consumer<Throwable> onError);

    void deleteMyDoctor(MyDoctor myDoctor,
                        @Nullable Consumer<Void> onSuccess,
                        @Nullable Consumer<Throwable> onError);

    void hasLinked(String userId, String doctorId,
                   @Nullable Consumer<MyDoctor> onSuccess,
                   @Nullable Consumer<Throwable> onError);

    void getAllMyDoctor(Account account,
                        @Nullable Consumer<List<MyDoctor>> onSuccess,
                        @Nullable Consumer<Throwable> onError);
}
