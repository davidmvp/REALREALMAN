package com.example.davidmvp23.realrealflash;

/**
 * Created by davidmvp23 on 3/2/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class FlashCarddbAdapter {

    private SQLiteDatabase db;
    private FlashcardDBHelper dbHelper;
    private Context context = null;

    private static final String DB_NAME = "flash.db";
    private static final int DB_VERSION = 3;  // when you add or delete fields, you must update the version number!

    private static final String Card_TABLE = "flashcards";
    public static final String Card_ID = "card_id";   // column 0
    public static final String Card_subject = "card_subject";
    public static final String Card_question = "card_question";
    public static final String Card_answer = "card_answer";
    public static final String[] Card_COLS = {Card_ID, Card_subject, Card_question, Card_answer};

    public FlashCarddbAdapter(Context ctx) {
        context = ctx;
        dbHelper = new FlashcardDBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open() throws SQLiteException {
        try {

            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }
    public boolean removeCourse(Card card) {
        String qu = card.getQuestion();
        Cursor cursor = db.query(true, Card_TABLE, Card_COLS, Card_question+"="+qu, null, null, null, null, null);
        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No course items found for row: " + qu);
        }
        // must use column indices to get column values
        int whatIndex = cursor.getColumnIndex(Card_ID);


        return db.delete(Card_TABLE, Card_ID+"="+whatIndex, null) > 0;
    }
    public long insertCard(Card card) {
        // create a new row of values to insert
        ContentValues cvalues = new ContentValues();
        // assign values for each col

        cvalues.put(Card_question, card.getQuestion());
        cvalues.put(Card_subject, card.getCategory());
        cvalues.put(Card_answer, card.getAnswer());
        // add to course table in database
        return db.insert(Card_TABLE, null, cvalues);
    }




    public Cursor getAllCard() {
        return db.query(Card_TABLE, Card_COLS, null, null, null, null, null);
    }
    private static class FlashcardDBHelper extends SQLiteOpenHelper {

        // SQL statement to create a new database table
        private static final String DB_CREATE = "CREATE TABLE " + Card_TABLE
                + " (" + Card_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Card_subject +  " TEXT,"
                + Card_question  + " TEXT," + Card_answer + " TEXT);";




        public FlashcardDBHelper(Context context, String name, CursorFactory fct, int version) {
            super(context, name, fct, version);
        }

        @Override
        public void onCreate(SQLiteDatabase adb) {
            adb.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase adb, int oldVersion, int newVersion) {
            Log.w("GPAdb", "upgrading from version " + oldVersion + " to "
                    + newVersion + ", destroying old data");
            // drop old table if it exists, create new one
            // better to migrate existing data into new table
            adb.execSQL("DROP TABLE IF EXISTS " );
            onCreate(adb);
        }
    } // GPAdbHelper class



}
