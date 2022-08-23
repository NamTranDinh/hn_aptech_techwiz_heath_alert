package com.csupporter.techwiz.presentation.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.mct.components.baseui.BaseOverlayDialog;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EditHealthTracking extends BaseOverlayDialog {

    private EditText height;
    private EditText weight;
    private EditText heartBeat;
    private EditText bloodSugar;
    private EditText bloodPressure;
    private EditText note;
    private Button btnEditNew;
    private HealthTracking healthTracking;

    public OnClickEditHealthTracking onClickEditHealthTracking;

    public EditHealthTracking(@NonNull Context context, HealthTracking healthTracking, OnClickEditHealthTracking onClickEditHealthTracking ) {
        super(context);
        this.healthTracking = healthTracking;
        this.onClickEditHealthTracking = onClickEditHealthTracking;
    }

    public interface OnClickEditHealthTracking {
        void onEditTrack(BaseOverlayDialog dialog,HealthTracking newTrack);
    }
    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_health_tracking, null);

        init(view);
        setData(healthTracking);
        eventEditClick();

        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    private void setData(HealthTracking healthTracking) {
        height.setText(String.valueOf(healthTracking.getHeight()));
        weight.setText(String.valueOf(healthTracking.getWeight()));
        bloodSugar.setText(String.valueOf(healthTracking.getBloodSugar()));
        bloodPressure.setText(String.valueOf(healthTracking.getBloodPressure()));
        heartBeat.setText(String.valueOf(healthTracking.getHeartbeat()));
        note.setText(String.valueOf(healthTracking.getOther()));
    }

    private void init(View view) {
        height = view.findViewById(R.id.edt_height);
        weight = view.findViewById(R.id.edt_weight);
        bloodSugar = view.findViewById(R.id.edt_blood_sugar);
        bloodPressure = view.findViewById(R.id.edt_blood_pressure);
        heartBeat = view.findViewById(R.id.edt_heart_beat);
        note = view.findViewById(R.id.edt_note_health);
        btnEditNew = view.findViewById(R.id.btn_edit_health_tracking);

    }

    private void eventEditClick() {


        btnEditNew.setOnClickListener(view -> {
            String txtHeight = height.getText().toString().trim();
            String txtWeight = weight.getText().toString().trim();
            String txtBloodSugar = bloodSugar.getText().toString().trim();
            String txtBloodPressure = bloodPressure.getText().toString().trim();
            String txtHeartBeat = heartBeat.getText().toString().trim();
            String txtNote = note.getText().toString().trim();

            healthTracking.setId(healthTracking.getId());
            healthTracking.setHeight(Float.parseFloat(txtHeight));
            healthTracking.setWeight(Float.parseFloat(txtWeight));
            healthTracking.setBloodSugar(Float.parseFloat(txtBloodSugar));
            healthTracking.setBloodPressure(Float.parseFloat(txtBloodPressure));
            healthTracking.setHeartbeat(Float.parseFloat(txtHeartBeat));
            healthTracking.setOther(txtNote);
            healthTracking.setCreateAt(healthTracking.getCreateAt());
            onClickEditHealthTracking.onEditTrack(this,healthTracking);


        });


    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().

                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().

                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().

                getAttributes().windowAnimations = androidx.appcompat.R.style.Base_Animation_AppCompat_DropDownUp;
        dialog.getWindow().

                setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void setOnClickEditHealthTracking(OnClickEditHealthTracking onClickEditHealthTracking) {
        this.onClickEditHealthTracking = onClickEditHealthTracking;
    }


}
