package com.csupporter.techwiz.domain.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

public interface ImageManager {

    enum Type {
        AVATAR, CERTIFICATION,MEDICINE
    }

    void upload(@NonNull Type type, @NonNull String id, @NonNull byte[] source,
                @Nullable Consumer<Uri> onSuccess,
                @Nullable Consumer<Throwable> onError);

    void upload(@NonNull Type type, @NonNull String id, @NonNull Uri source,
                @Nullable Consumer<Uri> onSuccess,
                @Nullable Consumer<Throwable> onError);

    void delete(@NonNull Type type, @NonNull String id);

    void delete(@NonNull Type type, @NonNull String id,
                @Nullable Consumer<Void> onSuccess,
                @Nullable Consumer<Throwable> onError);

}
