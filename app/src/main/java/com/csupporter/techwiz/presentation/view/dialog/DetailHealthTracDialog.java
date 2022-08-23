package com.csupporter.techwiz.presentation.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.presentation.presenter.authentication.HealthyTrackingPresenter;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.baseui.BaseOverlayDialog;

public class DetailHealthTracDialog extends BaseOverlayDialog implements View.OnClickListener{
    private HealthTracking healthTracking;
    private Account account;
    private View view;
    private ImageView icExit,icDelete;
    private TextView tvHeight, tvWeight, tv_heart_beat, tv_blood_pressure, tv_blood_sugar, tvOther;

    private OnCLickDeleteTrack onCLickDeleteTrack;


    public DetailHealthTracDialog(@NonNull Context context, HealthTracking healthTracking, Account account, OnCLickDeleteTrack onCLickDeleteTrack) {
        super(context);
        this.healthTracking = healthTracking;
        this.account = account;
        this.onCLickDeleteTrack = onCLickDeleteTrack;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_detail_health_track, null);
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }


    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        initView(view);
        setData(healthTracking, account);
        eventClick();
    }

    private void eventClick() {
        icExit.setOnClickListener(this);
        icDelete.setOnClickListener(this);
    }

    private void setData(HealthTracking healthTracking, Account account) {
        tvHeight.setText(String.valueOf(healthTracking.getHeight()));
        tvWeight.setText(String.valueOf(healthTracking.getWeight()));
        tv_heart_beat.setText(String.valueOf(healthTracking.getHeartbeat()));
        tv_blood_pressure.setText(String.valueOf(healthTracking.getBloodPressure()));
        tv_blood_sugar.setText(String.valueOf(healthTracking.getBloodSugar()));
        tvOther.setText(String.valueOf(healthTracking.getOther()));
    }

    private void initView(View view) {
        tvHeight = view.findViewById(R.id.tv_height);
        tvWeight = view.findViewById(R.id.tv_weight);
        tv_heart_beat = view.findViewById(R.id.tv_heart_beat);
        tv_blood_pressure = view.findViewById(R.id.tv_blood_pressure);
        tv_blood_sugar = view.findViewById(R.id.tv_blood_sugar);
        tvOther = view.findViewById(R.id.tv_other);
        icExit = view.findViewById(R.id.ic_exit);
        icDelete = view.findViewById(R.id.ic_delete);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_exit){
            dismiss();
        } else if (v.getId() == R.id.ic_delete){
            onCLickDeleteTrack.onDeleteTrack(this,healthTracking);
        }
    }

    public interface OnCLickDeleteTrack{
        void onDeleteTrack(BaseOverlayDialog dialog, HealthTracking tracking);
    }
}
