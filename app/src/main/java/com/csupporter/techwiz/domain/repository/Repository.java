package com.csupporter.techwiz.domain.repository;

public abstract class Repository {

    public final ImageManager imageManager;
    public final AccountRepository account;
    public final AppointmentRepository appointment;
    public final HeathTrackingRepository heathTracking;
    public final MyDoctorRepository myDoctor;
    public final PrescriptionRepository prescription;
    public final PrescriptionDetailRepository prescriptionDetail;
    public final ListPrescriptionDetailRepository listPrescriptionDetail;

    public Repository(ImageManager imageManager, AccountRepository account, AppointmentRepository appointment, HeathTrackingRepository heathTracking, MyDoctorRepository doctorRepository,
                      PrescriptionRepository prescription,PrescriptionDetailRepository prescriptionDetail,
                      ListPrescriptionDetailRepository listPrescriptionDetail) {
        this.imageManager = imageManager;
        this.account = account;
        this.appointment = appointment;
        this.heathTracking  = heathTracking;
        this.myDoctor = doctorRepository;
        this.prescription = prescription;
        this.prescriptionDetail = prescriptionDetail;
        this.listPrescriptionDetail = listPrescriptionDetail;
    }

}
