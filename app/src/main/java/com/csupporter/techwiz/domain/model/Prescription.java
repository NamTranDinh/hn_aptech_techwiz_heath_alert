package com.csupporter.techwiz.domain.model;

public class Prescription extends BaseModel{

    private String userId;
    private String doctorId;
    private String titlePrescription;
    private long createAt;
    private String note;
    private boolean isUserCreate;

    public Prescription() {

    }

    public Prescription(String userId, String doctorId, String titlePrescription, long createAt, String note, boolean isUserCreate) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.createAt = createAt;
        this.titlePrescription = titlePrescription;
        this.note = note;
        this.isUserCreate = isUserCreate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitlePrescription() {
        return titlePrescription;
    }

    public void setTitlePrescription(String titlePrescription) {
        this.titlePrescription = titlePrescription;
    }


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isUserCreate() {
        return isUserCreate;
    }

    public void setUserCreate(boolean userCreate) {
        isUserCreate = userCreate;
    }
}
