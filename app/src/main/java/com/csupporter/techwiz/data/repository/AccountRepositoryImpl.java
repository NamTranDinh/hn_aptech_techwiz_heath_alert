package com.csupporter.techwiz.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.repository.AccountRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final static String DEFAULT_PATH = "accounts";

    @Override
    public void addAccount(Account account, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, account.getId(), account, onSuccess, onError);
    }

    @Override
    public void updateAccount(Account account, @Nullable Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.setData(DEFAULT_PATH, account.getId(), account, onSuccess, onError);
    }

    @Override
    public void checkUserNameAndPassword(String email, String password, @Nullable Consumer<Account> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        FirebaseUtils.success(onSuccess, null);
                    } else {
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Account account = snapshot.toObject(Account.class);
                        if (account != null) {
                            account.setId(snapshot.getId());
                            FirebaseUtils.success(onSuccess, account);
                        } else {
                            FirebaseUtils.success(onSuccess, null);
                        }
                    }
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void findAccountByEmail(String email, @Nullable Consumer<Account> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("email", email).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        FirebaseUtils.success(onSuccess, null);
                    } else {
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Account account = snapshot.toObject(Account.class);
                        if (account != null) {
                            account.setId(snapshot.getId());
                        }
                        FirebaseUtils.success(onSuccess, account);
                    }
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void findAccountById(String id, @Nullable Consumer<Account> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH).document(id).get()
                .addOnSuccessListener(snapshot -> {
                    Account account = snapshot.toObject(Account.class);
                    if (account != null) {
                        account.setId(snapshot.getId());
                    }
                    FirebaseUtils.success(onSuccess, account);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }


    @Override
    public void findAccountsByNameAndType(String name, int type, @Nullable Consumer<List<Account>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("type", type).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Account> accounts = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Account account = snapshot.toObject(Account.class);
                        if (account != null) {
                            account.setId(snapshot.getId());
                            accounts.add(account);
                        }
                    }
                    FirebaseUtils.success(onSuccess, accounts);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void filterDoctorByDepartment(int department, @Nullable Consumer<List<Account>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("department", department).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Account> accounts = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Account account = snapshot.toObject(Account.class);
                        if (account != null) {
                            account.setId(snapshot.getId());
                            accounts.add(account);
                        }
                    }
                    FirebaseUtils.success(onSuccess, accounts);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

    @Override
    public void getAllDoctor(@Nullable Consumer<List<Account>> onSuccess, @Nullable Consumer<Throwable> onError) {
        FirebaseUtils.db().collection(DEFAULT_PATH)
                .whereEqualTo("type", Account.TYPE_DOCTOR).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Account> accounts = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Account account = document.toObject(Account.class);
                        if (account != null) {
                            account.setId(document.getId());
                            accounts.add(account);
                        }
                    }
                    FirebaseUtils.success(onSuccess, accounts);
                }).addOnFailureListener(e -> FirebaseUtils.error(onError, e));
    }

}
