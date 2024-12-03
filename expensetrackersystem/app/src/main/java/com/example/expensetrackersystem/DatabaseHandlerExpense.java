package com.example.expensetrackersystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.expensetrackersystem.model.expenseModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandlerExpense extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "expense.db";
    public static final String TABLE_NAME = "expense_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "AMOUNT";
    public static final String COL3 = "TYPE";
    public static final String COL4 = "NOTE";
    public static final String COL5 = "DATE";

    public DatabaseHandlerExpense(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AMOUNT TEXT," + "TYPE TEXT," + "NOTE TEXT," + "DATE TEXT)";
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
        contentValues.put(COL5, date);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public void update(String id, String amount, String type, String note, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, amount);
        contentValues.put(COL3, type);
        contentValues.put(COL4, note);
        contentValues.put(COL5, date);

        database.update(TABLE_NAME, contentValues, "id=?", new String[]{id});
    }

    public List<expenseModel> getAllIncome() {
        List<expenseModel> expenseModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            expenseModel expense = new expenseModel();
            expense.setId(cursor.getString(0));
            expense.setAmount(cursor.getString(1));
            expense.setType(cursor.getString(2));
            expense.setNote(cursor.getString(3));
            expense.setDate(cursor.getString(4));

            expenseModels.add(expense);
        }
        return expenseModels;
    }
}
