package com.example.amygdalis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME= "USERS.DB";
    public static final String TABLE_NAME= "USER_TABLE";
    public static final String COL_ID= "ID";
    public static final String COL_NAME= "NAME";
    public static final String COL_PHONE= "PHONE";
    public static final String COL_AGE= "AGE";

    private static String CreateTableQuery="create table imageInfo (imageName TEXT" +
        ",image BLOB)";
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageinBytes;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PHONE TEXT, AGE INTEGER) ");
        db.execSQL(CreateTableQuery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean InsertUser(String name,String phone,String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME,name);
        contentValues.put(COL_PHONE,phone);
        contentValues.put(COL_AGE,age);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result ==-1)
            return false;
        else
            return true;

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public boolean UpdateData(String id,String name,String phone,String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,id);
        contentValues.put(COL_NAME,name);
        contentValues.put(COL_PHONE,phone);
        contentValues.put(COL_AGE,age);
        db.update(TABLE_NAME,contentValues, "ID = ?", new String[] { id } );
        return true;
    }

    public Integer DeleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] {id});
    }

    public  void storeImage(ModelClass objectModelClass){
        try{
            SQLiteDatabase objectSqLiteDatabase=this.getWritableDatabase();
            Bitmap imageToStoreBitmap=objectModelClass.getImage();
            objectByteArrayOutputStream= new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

            imageinBytes=objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues= new ContentValues();
            objectContentValues.put("imageName",objectModelClass.getImageName());
            objectContentValues.put("image",imageinBytes);
            long checkIfQueryRuns=objectSqLiteDatabase.insert("imageInfo",null,objectContentValues);
            if (checkIfQueryRuns!=0){
                Toast.makeText(context, "Data added into the table", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "Fail to add data", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<ModelClass> getAllImagesData(){
        SQLiteDatabase object=this.getReadableDatabase();
        ArrayList<ModelClass> objectModelClassList = new ArrayList<>();
        Cursor objectCursor=object.rawQuery("select * from imageInfo",null);
        if(objectCursor.getCount()!=0){
            while(objectCursor.moveToNext()){
                String nameofImage= objectCursor.getString(0);
                byte[] imageBytes=objectCursor.getBlob(1);
                Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                objectModelClassList.add(new ModelClass(nameofImage,objectBitmap));

            }
            return objectModelClassList;
        }else{
            Toast.makeText(context, "No values exists in Database", Toast.LENGTH_SHORT).show();
            return null;
        }
    }













}
