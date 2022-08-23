package com.csupporter.techwiz.utils;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.csupporter.techwiz.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;

public class PermissionUtils {

    public static void requestReadStoragePermission(@NonNull Fragment fragment, RequestCallback callback) {
        Context context = fragment.requireContext();
        PermissionX.init(fragment)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList,
                        fragment.getString(R.string.permission_explain_read_storage_request_reason),
                        getButtonPositive(context),
                        getButtonNegative(context)))
                .onForwardToSettings(getForwardToSettingsCallback(context))
                .request(callback);
    }


    private static String getButtonPositive(@NonNull Context context) {
        return context.getString(R.string.permission_button_positive);
    }

    private static String getButtonNegative(@NonNull Context context) {
        return context.getString(R.string.permission_button_negative);
    }

    @NonNull
    private static ForwardToSettingsCallback getForwardToSettingsCallback(Context context) {
        return (scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList,
                context.getString(R.string.permission_forward_to_settings),
                context.getString(R.string.permission_button_go_setting),
                getButtonNegative(context)
        );
    }
}
