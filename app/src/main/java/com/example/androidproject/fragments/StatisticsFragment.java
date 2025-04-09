package com.example.androidproject.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import com.example.androidproject.R;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private View rootView;
    private AnyChartView chartView;
    private LinearLayout statsContainer;
    private boolean isChartInitialized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Use the simpler layout
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statsContainer = rootView.findViewById(R.id.radarChart);

        // Create a text header
        TextView headerView = new TextView(requireContext());
        headerView.setText("Lutemon Statistics");
        headerView.setTextSize(24);
        headerView.setPadding(0, 0, 0, 24);
        statsContainer.addView(headerView);

        // Create the chart view programmatically
        chartView = new AnyChartView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                900); // Fixed height
        chartView.setLayoutParams(params);
        statsContainer.addView(chartView);

        // Create a message view below the chart
        TextView noteView = new TextView(requireContext());
        noteView.setText("Note: Statistics are updated every time you view this tab.");
        noteView.setPadding(0, 16, 0, 0);
        statsContainer.addView(noteView);

        // Delay chart setup to ensure view is ready
        new Handler(Looper.getMainLooper()).postDelayed(this::updateStatistics, 500);
    }

    private void updateStatistics() {
        if (!isAdded() || getActivity() == null) return;

        try {
            // Instead of a radar chart, let's use a column chart which is more reliable
            Cartesian cartesian = AnyChart.column();
            cartesian.animation(true);
            cartesian.title("Lutemon Statistics");

            // Get all Lutemons
            LutemonManager manager = LutemonManager.getInstance(requireContext());
            List<Lutemon> lutemons = manager.getHome().getAllLutemons();

            List<DataEntry> data = new ArrayList<>();

            // Create column data for each Lutemon
            for (Lutemon lutemon : lutemons) {
                data.add(new ValueDataEntry(lutemon.getName() + "\nPower", lutemon.getPower()));
                data.add(new ValueDataEntry(lutemon.getName() + "\nDefense", lutemon.getDefense()));
                data.add(new ValueDataEntry(lutemon.getName() + "\nHealth", lutemon.getMaxHealth()));
                data.add(new ValueDataEntry(lutemon.getName() + "\nWins", lutemon.getWinCount()));
                data.add(new ValueDataEntry(lutemon.getName() + "\nBattles", lutemon.getBattleCount()));
            }

            if (data.isEmpty()) {
                // No Lutemons, show a message
                statsContainer.removeView(chartView);
                TextView emptyView = new TextView(requireContext());
                emptyView.setText("No Lutemons available. Create some Lutemons first!");
                emptyView.setTextSize(16);
                statsContainer.addView(emptyView, 1); // Add after header
                return;
            }

            Column column = cartesian.column(data);
            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}");

            cartesian.yScale().minimum(0d);
            cartesian.yAxis(0).title("Value");
            cartesian.xAxis(0).title("Lutemon Stats");
            cartesian.interactivity().hoverMode(HoverMode.BY_X);
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

            // Setup the chart
            chartView.setChart(cartesian);
            isChartInitialized = true;

        } catch (Exception e) {
            e.printStackTrace();
            // Display error message
            TextView errorView = new TextView(requireContext());
            errorView.setText("Error creating chart: " + e.getMessage());
            errorView.setPadding(16, 16, 16, 16);
            statsContainer.addView(errorView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear and update when resuming
        if (isAdded() && chartView != null) {
            // Use a handler to ensure UI is ready
            new Handler(Looper.getMainLooper()).post(() -> {
                // Clear existing chart
                if (isChartInitialized) {
                    chartView.clear();
                }

                // Update with new data
                updateStatistics();
            });
        }
    }
}