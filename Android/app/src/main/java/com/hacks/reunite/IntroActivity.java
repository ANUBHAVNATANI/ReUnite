package com.hacks.reunite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        AppCompatButton uploadButton = findViewById(R.id.upload);
        AppCompatButton checkButton = findViewById(R.id.check);

        uploadButton.setOnClickListener(view->{
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });

        checkButton.setOnClickListener(view->{
            Intent intent = new Intent(this, CheckActivity.class);
            startActivity(intent);
        });
    }
}
