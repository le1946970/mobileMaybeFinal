package com.example.expensetrackersystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetrackersystem.model.expenseModel;
import com.example.expensetrackersystem.model.incomeModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartFull extends AppCompatActivity {

    private PieChartView pieChartView;
    private Button btnSendEmail;

    private DatabaseHandlerExpense databaseHandlerExpense;
    private DatabaseHandler databaseHandlerIncome;

    private HashMap<String, String> expenseMap;
    private HashMap<String, String> incomeMap;

    private List<SliceValue> pieData;

    private float totalExpense = 0;
    private float totalIncome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_activity_pie_chart);

        pieChartView = findViewById(R.id.chart);
        btnSendEmail = findViewById(R.id.btn_send_email);

        databaseHandlerExpense = new DatabaseHandlerExpense(PieChartFull.this);
        databaseHandlerIncome = new DatabaseHandler(PieChartFull.this);

        addData();
        getEntries();

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenses & Income").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        // Set the listener for the send email button
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChartByEmail();
            }
        });
    }

    private void addData() {
        List<expenseModel> expenseModelList = databaseHandlerExpense.getAllIncome();
        List<incomeModel> incomeModelList = databaseHandlerIncome.getAllIncome();

        expenseMap = new HashMap<>();
        incomeMap = new HashMap<>();

        // Process expenses
        for (expenseModel model : expenseModelList) {
            addToMap(model.getType(), model.getAmount(), "expense");
        }

        // Process incomes
        for (incomeModel model : incomeModelList) {
            addToMap(model.getType(), model.getAmount(), "income");
        }
    }

    private void addToMap(String type, String amount, String category) {
        float amountValue = Float.parseFloat(amount);
        if (category.equals("expense")) {
            totalExpense += amountValue;
            if (expenseMap.containsKey(type)) {
                float existingAmount = Float.parseFloat(expenseMap.get(type));
                expenseMap.put(type, String.valueOf(existingAmount + amountValue));
            } else {
                expenseMap.put(type, amount);
            }
        } else if (category.equals("income")) {
            totalIncome += amountValue;
            if (incomeMap.containsKey(type)) {
                float existingAmount = Float.parseFloat(incomeMap.get(type));
                incomeMap.put(type, String.valueOf(existingAmount + amountValue));
            } else {
                incomeMap.put(type, amount);
            }
        }
    }

    private void getEntries() {
        pieData = new ArrayList<>();
        int i = 0;

        // Define colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ef0148"));  // Expense color
        colors.add(Color.parseColor("#1ac831")); // Income color (green hex #1ac831)

        // Format the total amounts as currency with 2 decimal places
        String expenseLabel = String.format("Total Expenses ($%.2f)", totalExpense);
        String incomeLabel = String.format("Total Income ($%.2f)", totalIncome);

        // Add single expense slice with total expense
        pieData.add(new SliceValue(totalExpense, colors.get(0)).setLabel(expenseLabel));

        // Add single income slice with total income
        pieData.add(new SliceValue(totalIncome, colors.get(1)).setLabel(incomeLabel));
    }

    // Capture the pie chart as a Bitmap image
    private Bitmap getPieChartBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(pieChartView.getWidth(), pieChartView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        pieChartView.draw(canvas);
        return bitmap;
    }

    // Send the pie chart via email
    private void sendChartByEmail() {
        // Capture the chart as a Bitmap
        Bitmap bitmap = getPieChartBitmap();

        // Convert the Bitmap to a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Set up email intent
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/octet-stream");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@example.com"}); // Set recipient email address
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pie Chart Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attached pie chart report.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, byteArray);  // Attach the chart image

        startActivity(Intent.createChooser(emailIntent, "Send Pie Chart"));
    }
}
