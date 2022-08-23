package com.csupporter.techwiz.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentSchedule {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("doctor_email")
    @Expose
    private String doctorEmail;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("status")
    @Expose
    private Integer status;

    public AppointmentSchedule() {
    }

    public AppointmentSchedule(String id, String userEmail, String doctorEmail, Integer time, String location, Integer status) {
        this.id = id;
        this.userEmail = userEmail;
        this.doctorEmail = doctorEmail;
        this.time = time;
        this.location = location;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
