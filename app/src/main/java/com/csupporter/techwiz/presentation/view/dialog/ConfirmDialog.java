package com.csupporter.techwiz.presentation.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class ConfirmDialog extends BaseOverlayDialog implements View.OnClickListener {

    public static final int LAYOUT_HOLD_USER = R.layout.dialog_cf_hold;
    public static final int LAYOUT_CONFIRM = R.layout.dialog_cf_confirm;
    private View view;
    private final int icon;
    private final int layout;
    private final String msg;
    private final OnConfirmListener mOnConfirmListener;

    public ConfirmDialog(@NonNull Context context, int layout, int icon, String msg, OnConfirmListener mOnConfirmListener) {
        super(context);
        this.layout = layout;
        this.icon = icon;
        this.msg = msg;
        this.mOnConfirmListener = mOnConfirmListener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        view = LayoutInflater.from(context).inflate(layout, null);
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        intiView(view);
    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    private void intiView(@NonNull View view) {
        view.findViewById(R.id.btn_no).setOnClickListener(this);
        view.findViewById(R.id.btn_yes).setOnClickListener(this);
        ImageView imgIcon = view.findViewById(R.id.img_icon);
        if (icon != 0) {
            imgIcon.setImageResource(icon);
        }
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_no:
                mOnConfirmListener.onCancel(this);
                break;
            case R.id.btn_yes:
                mOnConfirmListener.onConfirm(this);
                break;
        }
    }


    public interface OnConfirmListener {
        void onConfirm(BaseOverlayDialog overlayDialog);

        void onCancel(BaseOverlayDialog overlayDialog);
    }

}
