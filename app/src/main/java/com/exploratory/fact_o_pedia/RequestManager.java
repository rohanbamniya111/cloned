package com.exploratory.fact_o_pedia;

import android.content.Context;
import android.widget.Toast;

import com.exploratory.fact_o_pedia.Models.FactApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://factchecktools.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getClaimsList(OnFetchDataListener listener, String query, String lang) {
        CallFactApi callFactApi = retrofit.create(CallFactApi.class);
        Call<FactApiResponse> call = callFactApi.callFacts(context.getString(R.string.api_key), query, lang);

        try {
            call.enqueue(new Callback<FactApiResponse>() {
                @Override
                public void onResponse(Call<FactApiResponse> call, Response<FactApiResponse> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        listener.onFetchData(response.body().getClaims(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<FactApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public RequestManager(Context context) {
        this.context = context;
    }

    public interface CallFactApi {
        @GET("v1alpha1/claims:search")
        Call<FactApiResponse> callFacts(
                @Query("key") String api_key,
                @Query("query") String query,
                @Query("languageCode") String lang
        );
    }
}