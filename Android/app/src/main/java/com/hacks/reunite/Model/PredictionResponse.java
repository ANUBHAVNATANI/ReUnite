package com.hacks.reunite.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PredictionResponse {

    @SerializedName("prediction")
    @Expose
    private Integer prediction;

    public void setPrediction(int prediction){
        this.prediction = prediction;
    }

    public Integer getPrediction(){
        return prediction;
    }
}
