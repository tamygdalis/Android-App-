package com.example.amygdalis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class showimageActivity extends AppCompatActivity {
private DatabaseHelper objectDatabaseHelper;
private RecyclerView ObjectRecyclerView;

private RVAdapter objectRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        ObjectRecyclerView= findViewById(R.id.imagesRV);
        objectDatabaseHelper= new DatabaseHelper(this);
    }

    public void getData(View view){
           objectRVAdapter= new RVAdapter(objectDatabaseHelper.getAllImagesData());
           ObjectRecyclerView.setHasFixedSize(false);
           ObjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
           ObjectRecyclerView.setAdapter(objectRVAdapter);
    }
}