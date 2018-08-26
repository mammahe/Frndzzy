package com.example.android.frndzzy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Frend.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table rating " +
                        "(id integer primary key, rate integer,position integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertRating(int rate, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rate", rate);
        contentValues.put("position", position);
        int id = (int) db.insertWithOnConflict("rating", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            db.update("rating", contentValues, "_id=?", new String[]{"1"});  // number 1 is the _id here, update to variable for your code
        }
        return true;
    }

    public int getRating(int position) {
        int i;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select rate from rating where position=? order by id desc limit 1",new String[]{String.valueOf(position)});
        if (cursor.moveToFirst()) {
            i = cursor.getInt(cursor.getColumnIndex("rate"));
        } else {
            i = 0;
        }
        return i;
    }


}
