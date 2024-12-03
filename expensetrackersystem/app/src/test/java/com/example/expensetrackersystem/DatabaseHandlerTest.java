package com.example.expensetrackersystem;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expensetrackersystem.model.incomeModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class DatabaseHandlerTest {

    private DatabaseHandler dbHandler;
    private SQLiteOpenHelper mockDbHelper;
    private SQLiteDatabase mockDb;

    @Before
    public void setUp() {
        // Mock the SQLiteOpenHelper and SQLiteDatabase
        mockDbHelper = mock(SQLiteOpenHelper.class);
        mockDb = mock(SQLiteDatabase.class);

        // Stub the behavior of getWritableDatabase to return the mocked SQLiteDatabase
        when(mockDbHelper.getWritableDatabase()).thenReturn(mockDb);

        // Create DatabaseHandler with the mocked SQLiteOpenHelper
        dbHandler = new DatabaseHandler(null) {  // Pass null context for unit testing
            @Override
            public SQLiteOpenHelper getDatabaseHelper() {
                return mockDbHelper;
            }
        };
    }

    @Test
    public void testAddData() {
        // Given
        String amount = "1000";
        String type = "Income";
        String note = "Salary";
        String date = "03/12/2024";  // Example date

        // When
        boolean result = dbHandler.addData(amount, type, note, date);

        // Then
        assertTrue("Data should be added successfully", result);

        // Verify that getWritableDatabase() was called
        verify(mockDbHelper).getWritableDatabase();
        verify(mockDb).insert(anyString(), anyString(), any());
    }

    @Test
    public void testUpdateData() {
        // Given
        String amount = "1000";
        String type = "Income";
        String note = "Salary";
        String date = "03/12/2024";
        dbHandler.addData(amount, type, note, date);  // Insert initial data

        // When
        String newAmount = "1500";
        String newNote = "Updated Salary";
        String newDate = "04/12/2024";
        String id = dbHandler.getAllIncome().get(0).getId();
        dbHandler.update(id, newAmount, type, newNote, newDate);

        // Then
        incomeModel updatedRecord = dbHandler.getAllIncome().get(0);
        assertEquals("Amount should be updated", newAmount, updatedRecord.getAmount());
        assertEquals("Note should be updated", newNote, updatedRecord.getNote());
        assertEquals("Date should be updated", newDate, updatedRecord.getDate());

        // Verify that getWritableDatabase() and update method were called
        verify(mockDbHelper).getWritableDatabase();
        verify(mockDb).update(anyString(), any(), any(), any(), any());
    }

    @Test
    public void testGetAllIncome() {
        // Given
        String amount = "1000";
        String type = "Income";
        String note = "Salary";
        String date = "03/12/2024";
        dbHandler.addData(amount, type, note, date);  // Insert record

        // When
        incomeModel record = dbHandler.getAllIncome().get(0);

        // Then
        assertNotNull("Record should not be null", record);
        assertEquals("Amount should match", amount, record.getAmount());
        assertEquals("Note should match", note, record.getNote());
        assertEquals("Date should match", date, record.getDate());

        // Verify that getWritableDatabase() was called
        verify(mockDbHelper).getWritableDatabase();
    }
}
