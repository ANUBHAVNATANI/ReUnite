package com.hacks.reunite.Repository;

import android.util.Log;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hacks.reunite.Contract.ApiContract.BASE_URL;

public class UploadRepository {
    private final String TAG = getClass().getSimpleName();
    private static UploadRepository uploadRepository;
    private RetrofitInterface uploadInterface;

    public static UploadRepository getInstance(){
        if (uploadRepository == null)
            uploadRepository = new UploadRepository();

        return uploadRepository;
    }

    private UploadRepository(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        uploadInterface = retrofit.create(RetrofitInterface.class);
    }

    public void uploadImage(String input, MutableLiveData<Boolean> showLoading, MutableLiveData<Integer> predict, MutableLiveData<Integer> detectedId){
        showLoading.setValue(true);
        Log.d(TAG, "Starting upload...");
        uploadInterface.postImage(input).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    String jsonString = response.body().getAsJsonObject().toString().trim();
                    try {
                        JSONObject reader = new JSONObject(jsonString);
                        int prediction = (int) reader.get("prediction");
                        int id = (int) reader.get("id");
                        Log.d(TAG, "Prediction = " + prediction);
                        predict.setValue(prediction);
                        detectedId.setValue(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Response successful= " + jsonString);
                }else{
                    Log.d(TAG, "Upload1 failed...");
                    predict.setValue(null);
                }
                showLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                showLoading.setValue(false);
                t.printStackTrace();
                Log.d(TAG, "Upload failed...");
                predict.setValue(null);
            }
        });
    }
}
