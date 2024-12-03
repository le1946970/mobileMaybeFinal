package com.example.expensetrackersystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.expensetrackersystem.model.incomeModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "income.db";
    public static final String TABLE_NAME = "income_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "AMOUNT";
    public static final String COL3 = "TYPE";
    public static final String COL4 = "NOTE";
    public static final String COL5 = "DATE";  // Date column

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "AMOUNT TEXT,"
                + "TYPE TEXT,"
                + "NOTE TEXT,"
                + "DATE TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String a = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(a);
        onCreate(db);
    }

    public boolean addData(String amount, String type, String note, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, amount);
        contentValues.put(COL3, type);
        contentValues.put(COL4, note);
        contentValues.put(COL5, date);  // Store date as text

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public void update(String id, String amount, String type, String note, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, amount);
        contentValues.put(COL3, type);
        contentValues.put(COL4, note);
        contentValues.put(COL5, date);  // Update date

        long result = database.update(TABLE_NAME, contentValues, "ID=?", new String[]{id});
        // Handle result if needed
    }

    public List<incomeModel> getAllIncome() {
        List<incomeModel> incomeModelList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                // Adjust the constructor if necessary to accept the date field
                incomeModelList.add(new incomeModel(
                        data.getString(0),  // ID
                        data.getString(1),  // AMOUNT
                        data.getString(2),  // TYPE
                        data.getString(3),  // NOTE
                        data.getString(4)   // DATE
                ));
            }
        }

        return incomeModelList;
    }

}
