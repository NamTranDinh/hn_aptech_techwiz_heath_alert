package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.csupporter.techwiz.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class AlertDialog extends BaseOverlayDialog {
    private View view;
    private final int icon;
    private final String msg;
    private final AlertDialog.OnConfirmListener mOnConfirmListener;

    public AlertDialog(@NonNull Context context, int icon, String msg, AlertDialog.OnConfirmListener mOnConfirmListener) {
        super(context);
        this.icon = icon;
        this.msg = msg;
        this.mOnConfirmListener = mOnConfirmListener;
    }

    @NonNull
    @Override
    protected androidx.appcompat.app.AlertDialog.Builder onCreateDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_alert, null);
        return new androidx.appcompat.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull androidx.appcompat.app.AlertDialog dialog) {
        intiView(view);
    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    private void intiView(@NonNull View view) {
        ImageView imgIcon = view.findViewById(R.id.img_icon);
        if (icon != 0) {
            imgIcon.setImageResource(icon);
        }
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        view.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onCancel(this);
            }
        });
    }


    public interface OnConfirmListener {
        void onCancel(BaseOverlayDialog overlayDialog);
    }
}
