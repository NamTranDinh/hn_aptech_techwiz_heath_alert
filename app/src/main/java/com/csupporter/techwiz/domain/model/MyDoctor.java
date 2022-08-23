package com.csupporter.techwiz.domain.model;

public class MyDoctor extends BaseModel{
    private String id;
    private String userId;
    private String doctorId;
    private long createAt;

    public MyDoctor(){
    }

    public MyDoctor(String id,String userId, String doctorId) {
        this.id = id;
        this.userId = userId;
        this.doctorId = doctorId;
        this.createAt = System.currentTimeMillis();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
