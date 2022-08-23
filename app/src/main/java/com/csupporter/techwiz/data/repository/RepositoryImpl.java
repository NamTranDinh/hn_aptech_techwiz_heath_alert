package com.csupporter.techwiz.data.repository;

import com.csupporter.techwiz.domain.repository.Repository;

public class RepositoryImpl extends Repository {

    private static RepositoryImpl instance;

    public static RepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RepositoryImpl();
        }
        return instance;
    }

    private RepositoryImpl() {
        super(new ImageManagerImpl(),
                new AccountRepositoryImpl(),
                new AppointmentRepositoryImpl(),
                new HeathTrackingRepositoryImpl(),
                new MyDoctorRepositoryImpl(),
                new PrescriptionRepositoryImpl(),
                new PrescriptionDetailRepositoryImpl(),
                new ListPrescriptionDetailImpl()
        );
    }
}
