package com.csupporter.techwiz.di;

import androidx.annotation.NonNull;

import com.csupporter.techwiz.data.data_api.APIService;
import com.csupporter.techwiz.data.data_api.DataService;
import com.csupporter.techwiz.data.repository.AccountRepositoryImpl;
import com.csupporter.techwiz.data.repository.RepositoryImpl;
import com.csupporter.techwiz.domain.repository.AccountRepository;
import com.csupporter.techwiz.domain.repository.Repository;
import com.csupporter.techwiz.utils.SettingPreferences;

public class DataInjection {

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static DataService provideDataService() {
        return provideDataService(null);
    }

    @NonNull
    public static DataService provideDataService(String token) {
        return APIService.getService(token);
    }

    ///////////////////////////////////////////////////////////////////////////
    // DB & USE_CASES
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static Repository provideRepository() {
        return RepositoryImpl.getInstance();
    }

    ///////////////////////////////////////////////////////////////////////////
    // UTILS
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static SettingPreferences provideSettingPreferences() {
        return SettingPreferences.getInstance();
    }
}
