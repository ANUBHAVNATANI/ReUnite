package com.hacks.reunite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hacks.reunite.Model.Profile;

public class ReportActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText contactEditText;
    private EditText addressEditText;
    private ImageView pictureView;
    private int REQUEST_IMAGE_CAPTURE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        AppCompatButton addButton = findViewById(R.id.add_button);
        idEditText = findViewById(R.id.field_id);
        nameEditText = findViewById(R.id.field_name);
        ageEditText = findViewById(R.id.field_age);
        contactEditText = findViewById(R.id.field_contact_no);
        addressEditText = findViewById(R.id.field_address);
        pictureView = findViewById(R.id.picture_view);
        pictureView.setClipToOutline(true);

        pictureView.setOnClickListener(view->{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        addButton.setOnClickListener(view->{
            addToDatabase();
        });
    }

    private void addToDatabase(){
        try {
            int id = Integer.valueOf(idEditText.getText().toString().trim());
            String name = nameEditText.getText().toString().trim();
            int age = Integer.valueOf(ageEditText.getText().toString().trim());
            String contact = contactEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            Profile profile = new Profile(id, name, age, contact, address);
            DatabaseReference databaseProfiles = FirebaseDatabase.getInstance().getReference("profiles");
            String key = databaseProfiles.push().getKey();
            databaseProfiles.child(key).setValue(profile);
            Toast.makeText(this, "Reported", Toast.LENGTH_SHORT);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pictureView.setImageBitmap(imageBitmap);
            pictureView.setPadding(0, 0, 0, 0);
        }
    }
}
