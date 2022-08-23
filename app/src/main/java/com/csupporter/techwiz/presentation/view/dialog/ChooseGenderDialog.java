package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

public class ChooseGenderDialog extends BaseOverlayDialog {

    private final OnSubmitListener listener;

    public ChooseGenderDialog(@NonNull Context context, OnSubmitListener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_gender, null);
        TextView tvMale = view.findViewById(R.id.tv_male);
        TextView tvFemale = view.findViewById(R.id.tv_female);
        tvMale.setOnClickListener(v -> {
            listener.onSubmit(Account.MALE);
            dismiss();
        });
        tvFemale.setOnClickListener(v -> {
            listener.onSubmit(Account.FEMALE);
            dismiss();
        });
        return new AlertDialog.Builder(context).setCancelable(true).setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {

    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    public interface OnSubmitListener {
        void onSubmit(int gender);
    }
}
