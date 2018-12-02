package com.sizov.vitaly.marsphotos.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PhotoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "photoDb";
    public static final String TABLE_OBJECTS = "photo";

    public static final String KEY_ID = "_id";
    public static final String KEY_ID_PHOTO = "id";
    public static final String KEY_SOL = "sol";
    public static final String KEY_IMG_SRC = "imgsrc";
    public static final String KEY_EARTH_DATE = "earthdate";

    public PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "
                + TABLE_OBJECTS + "("
                + KEY_ID + " integer primary key,"
                + KEY_ID_PHOTO + " TEXT,"
                + KEY_SOL + " TEXT,"
                + KEY_IMG_SRC + " TEXT,"
                + KEY_EARTH_DATE + " TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_OBJECTS);
        onCreate(sqLiteDatabase);
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_OBJECTS, null);
    }
}
