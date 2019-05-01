package app.infogen.cs.com.retrofitandroid.retrofit.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 12/17/2017.
 */

public class ClientApi {

    private static Retrofit restfulAPI;

    public static Retrofit getClinet() {
        return restfulAPI = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/").addConverterFactory(GsonConverterFactory.create()).build();
    }

}
