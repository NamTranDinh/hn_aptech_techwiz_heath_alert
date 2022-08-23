package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

public class EnterNameDialog extends BaseOverlayDialog {

    private EditText edtFirstName;
    private EditText edtLastName;
    private final OnSubmitListener listener;
    private final Account account;

    public EnterNameDialog(@NonNull Context context, Account account, OnSubmitListener listener) {
        super(context);
        this.listener = listener;
        this.account = account;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_enter_name, null);
        edtFirstName = view.findViewById(R.id.edt_first_name);
        edtLastName = view.findViewById(R.id.edt_last_name);
        edtFirstName.setText(account.getFirstName());
        edtLastName.setText(account.getLastName());
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
                ToastUtils.makeWarningToast(context, Toast.LENGTH_SHORT, "First name can be empty", true).show();
            } else {
                if (listener != null) {
                    dismiss();
                    listener.onSubmit(edtFirstName.getText().toString(), edtLastName.getText().toString());
                }
            }
        });
        return new AlertDialog.Builder(context).setCancelable(false).setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {

    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    public interface OnSubmitListener {
        void onSubmit(String firstName, String lastName);
    }
}
