package com.nyc.prototype.api;

import android.util.Log;

import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
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

    public static void handleRetrofitError(RetrofitError error) {
        if (error == null) {
            return;
        }
        if (error.getResponse() != null) {
            Log.w(TAG, "RETROFIT ERROR for "+error.getResponse().getUrl()+" Status code was "+error.getResponse().getStatus()+" due to "+error.getResponse().getReason());
            return;
        }
        Log.w(TAG, "Unexpected RETROFIT error: "+error.toString());
        return;
    }

}
