package com.example.kiemtra;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Databases extends SQLiteOpenHelper {
    // insert, update, delete,...
    public void QueryData(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    // select
    public Cursor GetData(String query){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }
    public Databases(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

