package com.csupporter.techwiz.presentation.view.dialog;

import android.annotation.SuppressLint;
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

public class EnterLocationDialog  extends BaseOverlayDialog implements View.OnClickListener {
    private View view;
    private EditText edtLocation;
    private Button btnCancel, btnConfirm;
    private Account account;
    private OnSubmitListener mOnSubmitListener;

    public EnterLocationDialog(@NonNull Context context, Account account, OnSubmitListener onSubmitListener ) {
        super(context);
        this.account = account;
        this.mOnSubmitListener = onSubmitListener;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_enter_location, null);
        return new AlertDialog.Builder(context).setCancelable(false).setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        initDialog(view);
        eventClick();
        setData();
    }

    private void setData() {
        edtLocation.setText(account.getLocation());
    }

    private void eventClick() {
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void initDialog(View view) {
        edtLocation = view.findViewById(R.id.edt_location);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);

    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm){
            if (TextUtils.isEmpty(edtLocation.getText().toString())) {
                ToastUtils.makeWarningToast(context, Toast.LENGTH_SHORT, "Location can be empty!!", true).show();
            } else {
                if (mOnSubmitListener != null) {
                    dismiss();
                    edtLocation.setText(edtLocation.getText().toString().trim());
                    mOnSubmitListener.onSubmit(edtLocation.getText().toString().trim());
                }
            }
        }else {
            dismiss();
        }
    }

    public interface OnSubmitListener {
        void onSubmit(String location);
    }
}
