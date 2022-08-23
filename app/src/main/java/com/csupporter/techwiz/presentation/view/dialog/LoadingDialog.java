package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class LoadingDialog extends BaseOverlayDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
    }

    @Override
    protected int getBackgroundColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

}
