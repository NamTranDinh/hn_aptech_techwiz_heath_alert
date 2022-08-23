package com.csupporter.techwiz.data.data_api;

import androidx.annotation.NonNull;

public class APIService {

    private static final String BASE_URL = "http://csupporter.tk/techwiz/";

    @NonNull
    public static DataService getService() {
        return APIRetrofitClient.getClient(BASE_URL).create(DataService.class);
    }

    @NonNull
    public static DataService getService(String token) {
        return APIRetrofitClient.getClient(BASE_URL, token).create(DataService.class);
    }

}
