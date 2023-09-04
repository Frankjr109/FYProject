package com.example.projectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private ArrayList<FoodItem> foodItemArrayList;
    private BarChart expiredCategoriesChart;
    private PieChart pieChart;
    private Button dashboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dashboardBtn = findViewById(R.id.dashboardBtn);
        dashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        foodItemArrayList = new ArrayList<>();
        expiredCategoriesChart = findViewById(R.id.expiredCategoriesBarChart);
        pieChart = findViewById(R.id.CategoryPieChart);

        getItemsFromDB();

        Button  history = findViewById(R.id.expiredHistoryBtn);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(ReportActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

    }

    void setupExpiredCategoriesChart() {
        expiredCategoriesChart.getDescription().setEnabled(false);
        expiredCategoriesChart.setFitBars(true);
        expiredCategoriesChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        expiredCategoriesChart.getAxisRight().setEnabled(false);
        expiredCategoriesChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        expiredCategoriesChart.getAxisLeft().setGranularity(1f); // Set the granularity to 1 for integer values

        Map<String, Integer> expiredItemCountByCategory = new HashMap<>();
        for (FoodItem foodItem : foodItemArrayList) {
            if (foodItem.isExpired()) {
                Integer count = expiredItemCountByCategory.get(foodItem.getFoodCategory());
                expiredItemCountByCategory.put(foodItem.getFoodCategory(), count != null
                        ? count + Integer.parseInt(foodItem.getFoodQuantity())
                        : Integer.parseInt(foodItem.getFoodQuantity()));
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        int index = 0;
        List<String> categories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : expiredItemCountByCategory.entrySet()) {
            categories.add(entry.getKey()); // Collect all categories
            entries.add(new BarEntry(index++, entry.getValue()));
        }

        // Set the categories as x-axis labels
        XAxis xAxis = expiredCategoriesChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setLabelCount(categories.size()); // Set the number of x-axis labels to the total categories

        BarDataSet dataSet = new BarDataSet(entries, "Expired Item Count");

        // Set different colors for different categories
        int[] colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW}; // Add more colors if needed
        dataSet.setColors(colors);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value); // Display integer values on top of bars
            }
        });

        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        expiredCategoriesChart.setData(barData);
        expiredCategoriesChart.invalidate();
    }

    void setupPieChart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);

        Map<String, Integer> categoryCountMap = new HashMap<>();
        int totalSum = 0; // Total sum of all values

        for (FoodItem foodItem : foodItemArrayList) {
            String category = foodItem.getFoodCategory();
            int count = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                count = categoryCountMap.getOrDefault(category, 0);
            }
            int quantity = Integer.parseInt(foodItem.getFoodQuantity());
            categoryCountMap.put(category, count + quantity);
            totalSum += quantity; // Add to the total sum
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryCountMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Category Analysis");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        dataSet.setUsingSliceColorAsValueLineColor(true); // Add line color from slice color

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }



    void getItemsFromDB(){
        foodItemArrayList.clear();
        Utility.getCollectionReferenceForFoods().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshots) {

                    FoodItem item = new FoodItem(
                            documentSnapshot.getData().get("foodName").toString(),
                            documentSnapshot.getData().get("foodCategory").toString(),
                            documentSnapshot.getData().get("foodQuantity").toString(),
                            documentSnapshot.getData().get("foodExpiryDate").toString()
                    );

                    foodItemArrayList.add(item);

                }

                setupExpiredCategoriesChart();
                setupPieChart();

            }
        });


    }
}