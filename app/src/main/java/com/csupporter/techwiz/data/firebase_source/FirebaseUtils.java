package com.csupporter.techwiz.data.firebase_source;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";

    @NonNull
    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }

    @NonNull
    public static FirebaseStorage storage() {
        return FirebaseStorage.getInstance();
    }

    @NonNull
    public static FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * @param path      path
     * @param bytes     image bytes
     * @param onSuccess listener
     * @param onError   listener
     */
    public static void uploadImage(@NonNull String path, @NonNull byte[] bytes,
                                   @Nullable Consumer<Uri> onSuccess,
                                   @Nullable Consumer<Throwable> onError) {
        StorageReference ref = storage().getReference(path);
        ref.putBytes(bytes).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                error(onError, task.getException());
            }
            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                success(onSuccess, task.getResult());
            } else {
                error(onError, task.getException());
            }
        }).addOnFailureListener(e -> error(onError, e));

    }

    /**
     * @param path      path
     * @param uri       image uri
     * @param onSuccess listener
     * @param onError   listener
     */
    public static void uploadImage(@NonNull String path, @NonNull Uri uri,
                                   @Nullable Consumer<Uri> onSuccess,
                                   @Nullable Consumer<Throwable> onError) {
        StorageReference ref = storage().getReference(path);
        ref.putFile(uri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                error(onError, task.getException());
            }
            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                success(onSuccess, task.getResult());
            } else {
                error(onError, task.getException());
            }
        }).addOnFailureListener(e -> error(onError, e));
    }

    /**
     * @param path path
     */
    public static void deleteImage(@NonNull String path) {
        storage().getReference(path).delete();
    }

    /**
     * @param path path
     */
    public static void deleteImage(@NonNull String path,
                                   @Nullable Consumer<Void> onSuccess,
                                   @Nullable Consumer<Throwable> onError) {
        storage().getReference(path).delete()
                .addOnSuccessListener(unused -> success(onSuccess, unused))
                .addOnFailureListener(e -> error(onError, e));
    }

    public static void getData(@NonNull String path,
                               @Nullable Consumer<QuerySnapshot> onSuccess,
                               @Nullable Consumer<Throwable> onError) {
        db().collection(path).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        success(onSuccess, task.getResult());
                    else {
                        error(onError, task.getException());
                    }
                }).addOnFailureListener(e -> error(onError, e));
    }

    public static void setData(@NonNull String path, @NonNull String id, Object object,
                               @Nullable Consumer<Void> onSuccess,
                               @Nullable Consumer<Throwable> onError) {
        db().collection(path).document(id).set(object)
                .addOnSuccessListener(unused -> success(onSuccess, unused))
                .addOnFailureListener(e -> error(onError, e));
    }

    public static void deleteData(@NonNull String path, @NonNull String id) {
        db().collection(path).document(id).delete();
    }

    public static void deleteData(@NonNull String path, @NonNull String id,
                                  @Nullable Consumer<Void> onSuccess,
                                  @Nullable Consumer<Throwable> onError) {
        db().collection(path).document(id).delete()
                .addOnSuccessListener(unused -> success(onSuccess, unused))
                .addOnFailureListener(e -> error(onError, e));
    }

    public static <T> void success(Consumer<T> consumer, T t) {
        if (consumer != null) {
            consumer.accept(t);
        }
    }

    public static void error(Consumer<Throwable> onError, Throwable error) {
        Log.e(TAG, "error: ",error );
        if (onError != null) {
            if (error == null)
                onError.accept(new RuntimeException("Error"));
            else onError.accept(error);
        }
    }

    private FirebaseUtils() {
    }
}
