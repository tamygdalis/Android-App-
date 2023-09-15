package com.example.amygdalis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadRetrieveImage extends AppCompatActivity {
    EditText imagedetailsET;
    ImageView obejectimageview;
    private static final int pick_image_request = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    DatabaseHelper objectDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_retrieve_image);
        imagedetailsET = findViewById(R.id.imagenametext);
        obejectimageview = findViewById(R.id.image);
        objectDatabaseHelper= new DatabaseHelper(this);
    }

    public void chooseImage(View objectView) {
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, pick_image_request);


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == pick_image_request && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                obejectimageview.setImageBitmap(imageToStore);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void storeImage(View view){
        if(!imagedetailsET.getText().toString().isEmpty() && obejectimageview.getDrawable()!=null && imageToStore!=null){
            objectDatabaseHelper.storeImage(new ModelClass(imagedetailsET.getText().toString(),imageToStore));
        }
        else
            Toast.makeText(this, "Please select image name and image", Toast.LENGTH_SHORT).show();
    }
    public void moveToShowActivity(View view){
        startActivity(new Intent(this,showimageActivity.class));
    }
}