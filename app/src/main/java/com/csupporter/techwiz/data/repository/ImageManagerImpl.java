package com.csupporter.techwiz.data.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.repository.ImageManager;

public class ImageManagerImpl implements ImageManager {

    @Override
    public void upload(@NonNull Type type, @NonNull String id, @NonNull byte[] source, @Nullable Consumer<Uri> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.uploadImage(type.name() + id, source, onSuccess, onError);
    }

    @Override
    public void upload(@NonNull Type type, @NonNull String id, @NonNull Uri source, @Nullable Consumer<Uri> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.uploadImage(type.name() + id, source, onSuccess, onError);
    }

    @Override
    public void delete(@NonNull Type type, @NonNull String id) {
        FirebaseUtils.deleteImage(type.name() + id);
    }

    @Override
    public void delete(@NonNull Type type, @NonNull String id, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.deleteImage(type.name() + id, onSuccess, onError);
    }
}
