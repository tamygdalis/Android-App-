package com.example.amygdalis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDatabase;
    EditText ename,ephone,eage,eid,imagedetailsET;
    Button btnAddData,btnViewAll,btnUpdate,btnDelete,btnphotos,btnmaps;
    ImageView obejectimageview;
    private static final int pick_image_request=100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase= new DatabaseHelper(this);
        eid=findViewById(R.id.editid);
        ename=findViewById(R.id.editName);
        ephone=findViewById(R.id.editPhone);
        eage=findViewById(R.id.editAge);
        btnAddData=findViewById(R.id.btnAdd);
        btnViewAll=findViewById(R.id.btnList);
        btnUpdate=findViewById(R.id.btnupdate);
        btnDelete=findViewById(R.id.btndelete);
        imagedetailsET=findViewById(R.id.imagenametext);
        obejectimageview=findViewById(R.id.image);
        btnphotos=findViewById(R.id.btnphotos);
        btnmaps=findViewById(R.id.btnmaps);




        btnphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UploadRetrieveImage.class));
            }
        });
        btnmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),currentlocation.class));
            }
        });

        AddData();
        viewAll();
        UpdateDate();
        DeleteUser();
    }
    public void UpdateDate(){
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated=myDatabase.UpdateData(eid.getText().toString(),
                                ename.getText().toString(),
                                ephone.getText().toString(),
                                eage.getText().toString());
                        if (isUpdated==true){
                            Toast.makeText(MainActivity.this, "User updates", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    public void AddData(){
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean isInserted = myDatabase.InsertUser(ename.getText().toString(),ephone.getText().toString(),eage.getText().toString());
               if( isInserted ==true)
                   Toast.makeText(MainActivity.this, "User Inserted", Toast.LENGTH_SHORT).show();
               else
                   Toast.makeText(MainActivity.this, "User not inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewAll(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Cursor res = myDatabase.getAllData();
                       if(res.getCount() == 0){
                           showMessage("Error","No Users found");
                         return;
                       }

                           StringBuffer buffer= new StringBuffer();
                           while(res.moveToNext()){
                               buffer.append("ID : "+ res.getString(0)+" \n");
                               buffer.append("NAME : "+ res.getString(1)+" \n");
                               buffer.append("PHONE : "+ res.getString(2)+" \n");
                               buffer.append("AGE : "+ res.getString(3)+" \n\n");


                       }
                        //Show all data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void DeleteUser(){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDatabase.DeleteData(eid.getText().toString());
                        if(deletedRows >0 ){
                                Toast.makeText(MainActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "User not Deleted", Toast.LENGTH_SHORT).show();
                            }

                    }
        });
    }

    public void chooseImage(View objectView){
        try{
            Intent objectIntent=new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,pick_image_request);



        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode==pick_image_request && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                imageFilePath=data.getData();
                imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);

                obejectimageview.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }
}
