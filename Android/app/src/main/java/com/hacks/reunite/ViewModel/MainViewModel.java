package com.hacks.reunite.ViewModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.hacks.reunite.Repository.UploadRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean isStarted = false;
    private UploadRepository uploadRepository;
    private MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
    private MutableLiveData<Integer> predict = new MutableLiveData<>();
    private MutableLiveData<Integer> detectedId = new MutableLiveData<>();
    private String data;

    public void init(){
        if (!isStarted){
            showLoading.setValue(false);
            isStarted = true;
            uploadRepository = UploadRepository.getInstance();
        }
    }

    public LiveData<Boolean> getShowLoading(){
        return showLoading;
    }

    public LiveData<Integer> getPrediction(){
        return predict;
    }

    public LiveData<Integer> getDetectedId(){
        return detectedId;
    }

    public void retryCheck(){
        uploadRepository.uploadImage(data, showLoading, predict, detectedId);
    }

    public void upload(Bitmap bitmap){
        int[] intArray = new int[10000];
        bitmap = scaleBitmap(bitmap, 100, 100);
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        float[] floatArray = new float[10000];
        for(int i = 0; i < 10000; i++){
            floatArray[i] = convertToGreyScale(intArray[i]);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        int counter = 0;
        for(int i = 0; i < 100; i++){
            stringBuilder.append("[");
            for (int j = 0; j < 100; j++){
                stringBuilder.append(floatArray[counter]);
                if (j != 99)
                    stringBuilder.append(",");
                counter++;
            }
            if (i == 99)
                stringBuilder.append("]");
            else
                stringBuilder.append("],");
        }

        stringBuilder.append("]");
        data = stringBuilder.toString();
        uploadRepository.uploadImage(data, showLoading, predict, detectedId);
        bitmap.recycle();
    }

    private float convertToGreyScale(int color) {
        return (((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + (color & 0xFF)) / 3.0f / 255.0f;
    }

    private static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());
        bitmap.recycle();
        return output;
    }
}
