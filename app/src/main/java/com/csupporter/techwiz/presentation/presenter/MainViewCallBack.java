package com.csupporter.techwiz.presentation.presenter;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.model.MyDoctor;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;
import com.csupporter.techwiz.presentation.internalmodel.AppointmentDetail;
import com.csupporter.techwiz.presentation.presenter.authentication.HealthyTrackingPresenter;

import java.util.List;

public abstract class MainViewCallBack {

    public interface NavUserInfoCallBack {

    }

//    public interface UserHomeCallBack {
//        void listAppointment(List<Appointment> appointmentList);
//    }

    public interface UserAppointmentCallBack {
        void onRequestSuccess(List<Account> accounts);

        void onFailure();
    }

    public interface HealthTrackingCallBack {
        void addHealthTrackingSuccess(HealthTracking healthTracking);

        void addHealthTrackingFail(String message);

        void onGetDataSuccess(List<HealthTracking> trackingList);

        void checkAddOnlyOne(HealthTracking tracking);

        void onGetDataFailure();

    }

    public interface UpdateTrackCallBack {
        void onSuccess();

        void onFailure();
    }

    public interface DeleteTrackCallBack {
        void onDeleteSuccess();

        void onDeleteFailure();
    }

    public interface UploadAvatarCallback {
        void onSuccess(String url);

        void onError(Throwable throwable);
    }

    public interface UpdateCertificate {
        void onSuccess(String url);

        void onError(Throwable throwable);
    }

    public interface UpdateProfileCallback {
        void onSuccess();

        void onError(Throwable throwable);
    }

    public interface GetAllMyDoctorCallBack {
        void allMyDoctor(List<Account> myDoctorList);
    }

    public interface AddMyDoctor {
        void addMyDoctorSuccess(Account doctor, int position);

        void addMyDoctorFail();
    }

    public interface DeleteMyDoctor {
        void deleteMyDoctorSuccess();

        void deleteMyDoctorFail();
    }

    public interface GetAppointmentHistoryCallback {
        void onGetHistorySuccess(List<AppointmentDetail> appointmentDetails);

        void onError(Throwable throwable);
    }

    public interface CreateAppointmentCallback {
        void onCreateSuccess();

        void onError(Throwable throwable);
    }

    public interface UpdateAppointmentCallback {
        void onCreateSuccess();

        void onError(Throwable throwable);
    }

    public interface CreatePrescriptionCallBack {
        void onCreateSuccess(Prescription prescription);

        void onCreateFail();
    }

    public interface GetAllPrescriptionCallBack {
        void allPrescriptions(List<Prescription> prescriptionList);
    }

    public interface CreatePrescriptionDetailCallBack {
        void createPrescriptionDetailSuccess();

        void createPrescriptionFail();
    }

    public interface ListPrescriptionDetailCallback {
        void getAllListPrescription(List<PrescriptionDetail> prescriptionDetailList);
    }

    public interface GetUserCreatedPrescription {
        void getUserCreatedPrescription(Account account);
    }
    public interface DeletePrescriptionDetail{
        void deletePrescriptionDetailSuccess(PrescriptionDetail prescriptionDetail,int pos);
        void deletePrescriptionDetailFail();
    }
    public interface EditPrescriptionDetail{
        void editPrescriptionDetailSuccess(PrescriptionDetail prescriptionDetail);
        void editPrescriptionDetailFail();
    }
}
