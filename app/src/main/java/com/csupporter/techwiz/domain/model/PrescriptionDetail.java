package com.csupporter.techwiz.domain.model;

public class PrescriptionDetail extends BaseModel {

    private String prescriptionId;
    private String medicineName;
    private int timePerADay;
    private int timePerWeek;
    private int quantity;
    private String imageUrl;

    public PrescriptionDetail() {

    }

    public PrescriptionDetail(String medicineName, int timePerADay, int timePerWeek, int quantity, String imageUrl) {
        this.medicineName = medicineName;
        this.timePerADay = timePerADay;
        this.timePerWeek = timePerWeek;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getTimePerADay() {
        return timePerADay;
    }

    public void setTimePerADay(int timePerADay) {
        this.timePerADay = timePerADay;
    }

    public int getTimePerWeek() {
        return timePerWeek;
    }

    public void setTimePerWeek(int timePerWeek) {
        this.timePerWeek = timePerWeek;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PrescriptionDetail copy() {
        PrescriptionDetail detail = new PrescriptionDetail(medicineName, timePerADay, timePerWeek, quantity, imageUrl);
        detail.setId(getId());
        return detail;
    }
}
