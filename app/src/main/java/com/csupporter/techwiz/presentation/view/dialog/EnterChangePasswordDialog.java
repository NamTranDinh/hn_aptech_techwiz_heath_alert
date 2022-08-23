package com.csupporter.techwiz.presentation.view.dialog;

import static com.csupporter.techwiz.utils.Const.PASSWORD_REGEX;

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
import com.csupporter.techwiz.utils.EncryptUtils;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.util.regex.Pattern;

public class EnterChangePasswordDialog extends BaseOverlayDialog {

    private EditText edtCurrentPass;
    private EditText edtNewPass;
    private EditText edtCfNewPass;
    private final OnSubmitListener listener;
    private final Account account;
    private Toast mToast;

    public EnterChangePasswordDialog(@NonNull Context context, Account account, OnSubmitListener listener) {
        super(context);
        this.listener = listener;
        this.account = account;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_enter_password, null);
        edtCurrentPass = view.findViewById(R.id.edt_current_pass);
        edtNewPass = view.findViewById(R.id.edt_new_pass);
        edtCfNewPass = view.findViewById(R.id.edt_cf_new_pass);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> {
            String currentPass = edtCurrentPass.getText().toString().trim();
            String newPass = edtNewPass.getText().toString().trim();
            String cfNewPass = edtCfNewPass.getText().toString().trim();
            if (verify(currentPass, newPass, cfNewPass)) {
                listener.onSubmit(EncryptUtils.encrypt(newPass));
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
        void onSubmit(String password);
    }

    private boolean verify(String currentPass, String newPass, String reNewPass) {
        if (TextUtils.isEmpty(currentPass)) {
            showToast("Please enter current password", ToastUtils.WARNING);
            edtCurrentPass.requestFocus();
            return false;
        }
        if (!EncryptUtils.checkPassword(currentPass, account.getPassword())) {
            showToast("Current password incorrect", ToastUtils.WARNING);
            edtCurrentPass.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newPass)) {
            showToast("Please enter new password", ToastUtils.WARNING);
            edtNewPass.requestFocus();
            return false;
        }
        if (!Pattern.matches(PASSWORD_REGEX, newPass)) {
            showToast("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", ToastUtils.WARNING);
            edtNewPass.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(reNewPass)) {
            showToast("Please confirm new password", ToastUtils.WARNING);
            edtCfNewPass.requestFocus();
            return false;
        }
        if (!Pattern.matches(PASSWORD_REGEX, reNewPass)) {
            showToast("Password at least one number, one lowercase letter, one uppercase letter and greater than or equal to 8 characters", ToastUtils.WARNING);
            edtCfNewPass.requestFocus();
            return false;
        }
        if (!newPass.equals(reNewPass)) {
            showToast("Password and confirm password are not the same", ToastUtils.WARNING);
            edtCfNewPass.requestFocus();
            return false;
        }
        return true;
    }

    protected void showToast(String msg, int type) {
        if (context != null) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            mToast = ToastUtils.makeText(context, Toast.LENGTH_SHORT, type, msg, true);
            mToast.show();
        }
    }
}
