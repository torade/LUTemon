package com.example.androidproject.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Radar;
import com.anychart.core.radar.series.Line;
import com.anychart.enums.Align;
import com.anychart.enums.MarkerType;
import com.example.androidproject.R;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        AnyChartView radarChart = findViewById(R.id.radarChart);
        setupRadarChart(radarChart);
    }

    /**
     * Sets up the radar chart for displaying lutemon battle statistics.
     *
     * @param chartView AnyChartView
     */
    private void setupRadarChart(AnyChartView chartView) {
        Radar radar = AnyChart.radar();
        radar.title("Lutemon Battle Statistics");

        List<DataEntry> data = new ArrayList<>();
        LutemonManager manager = LutemonManager.getInstance(this);
        List<Lutemon> lutemons = manager.getHome().getAllLutemons();

        for (Lutemon lutemon : lutemons) {
            data.add(new CustomDataEntry(
                    lutemon.getName(),
                    lutemon.getPower(),
                    lutemon.getDefense(),
                    lutemon.getMaxHealth(),
                    lutemon.getWinCount(),
                    lutemon.getBattleCount()
            ));
        }

        Line series = radar.line(data);
        series.name("Stats");
        series.markers()
                .enabled(true)
                .type(MarkerType.CIRCLE)
                .size(3d);

        radar.yScale().minimum(0d);
        radar.yScale().ticks().interval(5d);
        radar.legend()
                .align(Align.CENTER)
                .enabled(true);

        chartView.setChart(radar);
    }

    /**
     * Custom data entry class for the radar chart.
     */
    public static class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String name, Integer power, Integer defense,
                               Integer maxHealth, Integer wins, Integer battles) {
            super(name, power);
            setValue("defense", defense);
            setValue("maxHealth", maxHealth);
            setValue("wins", wins);
            setValue("battles", battles);
        }
    }
}