package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.csupporter.techwiz.R;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;


public class CropImageDialog extends BaseOverlayDialog {

    private CropImageView cropImageView;
    private Uri mUri;
    private CropImageView.CropMode cropMode;
    private final CropImageListener listener;

    public CropImageDialog(@NonNull Context context, Uri uri, CropImageView.CropMode mode, CropImageListener listener) {
        super(context);
        this.mUri = uri;
        this.cropMode = mode;
        this.listener = listener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_crop_image, null);
        cropImageView = view.findViewById(R.id.crop_image_view);
        cropImageView.setCropMode(cropMode);
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> cropImage());
        loadImage();
        return new AlertDialog.Builder(context).setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
    }

    private void loadImage() {
        cropImageView.load(mUri).execute(new LoadCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Throwable e) {
                mUri = null;
                cropImageView.setImageResource(0);
            }
        });
    }

    private void cropImage() {
        if (mUri == null) {
            return;
        }
        cropImageView.cropAsync(new CropCallback() {
            @Override
            public void onSuccess(Bitmap bm) {
                if (listener != null) {
                    listener.onCropSuccess(CropImageDialog.this, bm);
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.makeErrorToast(context, Toast.LENGTH_SHORT, "Crop error", true).show();
            }
        });
    }

    @Override
    protected int getBackgroundColor() {
        return ContextCompat.getColor(context, R.color.transparent);
    }

    public interface CropImageListener {

        void onCropSuccess(BaseOverlayDialog dialog, Bitmap bitmap);

    }
}
