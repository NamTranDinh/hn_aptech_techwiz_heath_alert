package com.csupporter.techwiz.presentation.internalmodel;

import com.csupporter.techwiz.R;

public enum Departments {
    DENTIST("Dentist", R.drawable.dentist),
    PEDIATRICIAN("Pediatrician", R.drawable.pediatrician_doctor),
    CARDIOLOGIST("Cardiologist", R.drawable.cardiologist_doctor),
    BEAUTY_SURGEON("Beauty surgeon", R.drawable.beauty_surgeon),
    PSYCHOLOGIST("Psychologist", R.drawable.psycho_doctor),
    OBSTETRICIAN("Obstetrician", R.drawable.obstetrical);


    private int resourceFile;
    private String category;

    Departments(String category, int resourceFile) {
        this.category = category;
        this.resourceFile = resourceFile;
    }

    public int getResourceFile() {
        return resourceFile;
    }

    public void setResourceFile(int resourceFile) {
        this.resourceFile = resourceFile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
