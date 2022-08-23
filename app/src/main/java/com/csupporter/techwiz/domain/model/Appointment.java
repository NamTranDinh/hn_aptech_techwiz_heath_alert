package com.csupporter.techwiz.domain.model;

public class Appointment extends BaseModel {

    private String userId;
    private String doctorId;
    private long time;
    private String description;
    private String location;
    private int status;

    /**
     * 0 => Dang cho confirm
     * 1 => bac si confirm
     * 2 => bac si refuse
     * 3 => bac si cancel
     * 4 => user cancel
     * 5 => timeout (when time < currentTime)
     * 6 => da kham benh
     */


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
