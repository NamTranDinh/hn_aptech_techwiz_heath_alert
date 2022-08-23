package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class EditPrescriptionDialog extends BaseOverlayDialog {



    private OnClickEdit onClickAddNewHealthTracking;

    public EditPrescriptionDialog(@NonNull Context context, OnClickEdit onClickAddNewHealthTracking) {
        super(context);
        this.onClickAddNewHealthTracking = onClickAddNewHealthTracking;
    }

    public interface OnClickEdit {
        void onClickAddNew(String txtHeight, String txtWeight,
                           String txtHeartBeat, String txtBloodSugar,
                           String txtBloodPressure, String txtNote);
    }

    @NonNull
    @Override
    protected androidx.appcompat.app.AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_health_tracking, null);

        init(view);

        return new androidx.appcompat.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    private void init(View view) {


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
}
