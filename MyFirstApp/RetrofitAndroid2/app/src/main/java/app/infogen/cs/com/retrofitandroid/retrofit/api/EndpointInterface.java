package app.infogen.cs.com.retrofitandroid.retrofit.api;

import app.infogen.cs.com.retrofitandroid.retrofit.api.response.ResponseLocationResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dell on 12/17/2017.
 */

public interface EndpointInterface {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBA5YLr05mmmCjtOPKNyi6zbJ-CoS05Kio")
    Call<ResponseLocationResult> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
