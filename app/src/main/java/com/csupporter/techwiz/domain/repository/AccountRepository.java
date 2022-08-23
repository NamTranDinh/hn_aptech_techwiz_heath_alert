package com.csupporter.techwiz.domain.repository;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.csupporter.techwiz.domain.model.Account;

import java.util.List;

public interface AccountRepository {

    void checkUserNameAndPassword(String email, String password,
                                  @Nullable Consumer<Account> onSuccess,
                                  @Nullable Consumer<Throwable> onError);

    void addAccount(Account account,
                    @Nullable Consumer<Void> onSuccess,
                    @Nullable Consumer<Throwable> onError);

    void updateAccount(Account account,
                       @Nullable Consumer<Void> onSuccess,
                       @Nullable Consumer<Throwable> onError);

    void findAccountByEmail(String email,
                            @Nullable Consumer<Account> onSuccess,
                            @Nullable Consumer<Throwable> onError);

    void findAccountById(String id,
                         @Nullable Consumer<Account> onSuccess,
                         @Nullable Consumer<Throwable> onError);

    void findAccountsByNameAndType(String name, int type,
                                   @Nullable Consumer<List<Account>> onSuccess,
                                   @Nullable Consumer<Throwable> onError);

    void filterDoctorByDepartment(int department,
                                  @Nullable Consumer<List<Account>> onSuccess,
                                  @Nullable Consumer<Throwable> onError);

    void getAllDoctor(@Nullable Consumer<List<Account>> onSuccess,
                      @Nullable Consumer<Throwable> onError);

}
