package org.example.functions;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface MeasurementsApi {


    @GET("api/Measurements")
    Call<ResponseBody> getMeasurementsJson(
            @Query("$filter") String filter,
            @Query("$orderby") String orderBy
    );

    @POST("/")
    Call<Void> sendPostRequest(@Body int data);
}
