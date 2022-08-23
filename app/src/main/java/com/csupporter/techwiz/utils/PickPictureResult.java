package com.csupporter.techwiz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This class is custom to pick image from device
 */
public class PickPictureResult extends ActivityResultContract<Void, Uri> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
//        return new Intent(Intent.ACTION_GET_CONTENT)
//                .addCategory(Intent.CATEGORY_OPENABLE)
//                .setType("image/*");
        return new Intent(Intent.ACTION_PICK)
                .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    }

    @Nullable
    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getData();
        }
        return null;
    }
}
