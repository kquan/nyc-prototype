package com.nyc.prototype.api;

import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Kevin on 1/29/2015.
 */
public class RetrofitHelper {

    @SuppressWarnings("unused")
    private static final String TAG = RetrofitHelper.class.getSimpleName();

    public static RestAdapter.Builder getDefaultAdapterBuilder() {
        return new RestAdapter.Builder().setConverter(new GsonConverter(new GsonBuilder().create()));
    }

    public static PrototypeService createPrototypeService() {
        RestAdapter adapter = RetrofitHelper.getDefaultAdapterBuilder().setEndpoint(PrototypeService.ENDPOINT_URL).build();
        return adapter.create(PrototypeService.class);
    }

}
