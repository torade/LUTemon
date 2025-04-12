package com.example.androidproject.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
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
import com.anychart.charts.Radar;
import com.anychart.core.radar.series.Line;
import com.anychart.enums.Align;
import com.anychart.enums.MarkerType;

import com.example.androidproject.R;
import com.example.androidproject.activities.StatisticsActivity;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

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
        statsContainer = rootView.findViewById(R.id.statsContainer);

        // Create the chart view programmatically
        chartView = new AnyChartView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                900); // Fixed height
        chartView.setLayoutParams(params);
        statsContainer.addView(chartView);

        // Delay chart setup to ensure view is ready
        new Handler(Looper.getMainLooper()).postDelayed(this::updateStatistics, 1000); // Increased delay
    }

    private void updateStatistics() {
        if (!isAdded() || getActivity() == null || statsContainer == null) {
            Log.d("StatisticsFragment", "Fragment not ready to draw chart");
            return;
        }
        
        try {
            // Create new chart view each time to avoid stale references
            if (chartView != null) {
                statsContainer.removeView(chartView);
            }
            
            chartView = new AnyChartView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    900);
            chartView.setLayoutParams(params);
            statsContainer.addView(chartView, 1);
            
            // Wait for the chart view to be laid out before initializing
            chartView.post(() -> {
                if (!isAdded() || getActivity() == null) return;
                
                try {
                    // Get all Lutemons first
                    LutemonManager manager = LutemonManager.getInstance(requireContext());
                    List<Lutemon> lutemons = manager.getHome().getAllLutemons();
                    
                    // Check if we have data
                    if (lutemons.isEmpty()) {
                        statsContainer.removeView(chartView);
                        TextView emptyView = new TextView(requireContext());
                        emptyView.setText("No Lutemons available. Create some Lutemons first!");
                        emptyView.setTextSize(16);
                        statsContainer.addView(emptyView, 1);
                        return;
                    }
                    
                    // Create radar chart (similar to StatisticsActivity implementation)
                    // Replace the radar chart creation code in updateStatistics()

// Create radar chart with combat focus
                    Radar radar = AnyChart.radar();
                    radar.title("Combat Capabilities");

// Better scale for just ATK and DEF
                    radar.yScale().minimum(0d);
                    radar.yScale().maximum(15d);
                    radar.yScale().ticks().interval(1d);
                    radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

// Configure appearance using available methods
                    radar.background().fill("#F8F8F8", 0.3);

// Prepare data for each Lutemon - focused on combat stats
                    for (Lutemon lutemon : lutemons) {
                        List<DataEntry> lutemonData = new ArrayList<>();

                        // Only include attack and defense stats
                        lutemonData.add(new ValueDataEntry("Attack", lutemon.getPower()));
                        lutemonData.add(new ValueDataEntry("Defense", lutemon.getDefense()));
                        lutemonData.add(new ValueDataEntry("Health", lutemon.getMaxHealth() / 3));

                        // Create a radar series for this Lutemon
                        Line series = radar.line(lutemonData);
                        series.name(lutemon.getName() + " (" + lutemon.getColor() + ")");

                        // Make markers larger for better visibility
                        series.markers()
                                .enabled(true)
                                .type(MarkerType.CIRCLE)
                                .size(6d);

                        // Use different colors for different Lutemons with single-parameter version
                        switch (lutemon.getColor()) {
                            case "White":
                                series.stroke("#DDDDDD");
                                break;
                            case "Green":
                                series.stroke("#00FF00");
                                break;
                            case "Pink":
                                series.stroke("#FF69B4");
                                break;
                            case "Orange":
                                series.stroke("#FFA500");
                                break;
                            case "Black":
                                series.stroke("#9900CC");
                                break;
                            default:
                                series.stroke("#0000FF");
                        }
                    }

                    radar.legend()
                            .enabled(true)
                            .fontSize(12)
                            .align(Align.CENTER)
                            .position("bottom")
                            .itemsLayout("horizontal-expandable")
                            .padding(10d, 0d, 10d, 0d);

                    radar.tooltip()
                            .format("{%seriesName}: {%Value}");

                    // Set the chart to view
                    if (chartView != null && isAdded()) {
                        chartView.setChart(radar);
                        isChartInitialized = true;
                    }

                    // Add game statistics below the chart
                    displayGameStatistics(lutemons);
                    
                } catch (Exception e) {
                    Log.e("StatisticsFragment", "Error creating chart after layout: " + e.getMessage(), e);
                    displayErrorMessage(e.getMessage());
                }
            });
            
        } catch (Exception e) {
            Log.e("StatisticsFragment", "Error in updateStatistics: " + e.getMessage(), e);
            displayErrorMessage(e.getMessage());
        }
    }

    private void displayErrorMessage(String message) {
        if (!isAdded() || statsContainer == null) return;

        statsContainer.post(() -> {
            TextView errorView = new TextView(requireContext());
            errorView.setText("Error creating chart: " + message);
            errorView.setPadding(16, 16, 16, 16);
            statsContainer.addView(errorView);
        });
    }

    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String name, int power, int defense, int health, int wins, int battles) {
            super(name, power);
            setValue("defense", defense);
            setValue("health", health);
            setValue("wins", wins);
            setValue("battles", battles);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear and update when resuming with increased delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                updateStatistics();
            }
        }, 1000); // Increased delay for better reliability
    }
    
    /**
     * Displays game statistics below the radar chart
     */
    private void displayGameStatistics(List<Lutemon> lutemons) {
        if (!isAdded() || statsContainer == null || lutemons.isEmpty()) return;
        
        // Remove previous stats view if it exists (find by tag)
        View existingStats = statsContainer.findViewWithTag("game_stats");
        if (existingStats != null) {
            statsContainer.removeView(existingStats);
        }
        
        // Calculate statistics
        Lutemon mostBattles = null;
        Lutemon mostWins = null;
        Lutemon bestWinRate = null;
        int totalBattles = 0;
        int totalWins = 0;
        
        for (Lutemon lutemon : lutemons) {
            totalBattles += lutemon.getBattleCount();
            totalWins += lutemon.getWinCount();
            
            // Most battles
            if (mostBattles == null || lutemon.getBattleCount() > mostBattles.getBattleCount()) {
                mostBattles = lutemon;
            }
            
            // Most wins
            if (mostWins == null || lutemon.getWinCount() > mostWins.getWinCount()) {
                mostWins = lutemon;
            }
            
            // Best win rate (must have at least 1 battle)
            if (lutemon.getBattleCount() > 0) {
                float winRate = (float) lutemon.getWinCount() / lutemon.getBattleCount();
                if (bestWinRate == null) {
                    bestWinRate = lutemon;
                } else {
                    float currentBestRate = (float) bestWinRate.getWinCount() / bestWinRate.getBattleCount();
                    if (winRate > currentBestRate) {
                        bestWinRate = lutemon;
                    }
                }
            }
        }
        
        // Create stats view
        TextView statsView = new TextView(requireContext());
        statsView.setTag("game_stats");
        statsView.setPadding(16, 32, 16, 16);
        statsView.setTextSize(16);
        
        // Build stats text
        StringBuilder statsBuilder = new StringBuilder();
        statsBuilder.append("<h1>📊 GAME STATISTICS 📊</h1><br>");

        // Total stats
        statsBuilder.append("<b>Total Battles:</b> ").append(totalBattles).append("<br>");
        statsBuilder.append("<b>Total Wins:</b> ").append(totalWins).append("<br>");

        // Most battles
        if (mostBattles != null && mostBattles.getBattleCount() > 0) {
            statsBuilder.append("<b>Most Battles:</b> ").append(mostBattles.getName()) // Only shows up after a battle or if >0.
                    .append(" (").append(mostBattles.getBattleCount()).append(" battles)<br>");
        }
        
        // Most wins
        if (mostWins != null && mostWins.getWinCount() > 0) {
            statsBuilder.append("<b>Top Winner:</b> ").append(mostWins.getName())
                    .append(" (").append(mostWins.getWinCount()).append(" wins)<br>");
        }
        
        // Best win rate
        if (bestWinRate != null && bestWinRate.getBattleCount() > 0) {
            float winRate = (float) bestWinRate.getWinCount() / bestWinRate.getBattleCount() * 100;
            statsBuilder.append("<b>Best Win Rate:</b> ").append(bestWinRate.getName())
                    .append(" (").append(String.format("%.1f", winRate)).append("%)<br>");
        }
        
        // Most powerful
        Lutemon mostPowerful = lutemons.stream().max((a, b) -> Integer.compare(a.getPower(), b.getPower())).orElse(null);
        if (mostPowerful != null) {
            statsBuilder.append("<b>Most Powerful:</b> ").append(mostPowerful.getName())
                    .append(" (Power: ").append(mostPowerful.getPower()).append(")<br>");
        }
        
        // Best defense
        Lutemon bestDefense = lutemons.stream().max((a, b) -> Integer.compare(a.getDefense(), b.getDefense())).orElse(null);
        if (bestDefense != null) {
            statsBuilder.append("<b>Best Defense:</b> ").append(bestDefense.getName())
                    .append(" (Defense: ").append(bestDefense.getDefense()).append(")<br>");
        }
        
        // Most health
        Lutemon mostHealth = lutemons.stream().max((a, b) -> Integer.compare(a.getMaxHealth(), b.getMaxHealth())).orElse(null);
        if (mostHealth != null) {
            statsBuilder.append("<b>Highest Health:</b> ").append(mostHealth.getName())
                    .append(" (HP: ").append(mostHealth.getMaxHealth()).append(")<br>");
        }
        
        statsView.setText(Html.fromHtml(statsBuilder.toString(), Html.FROM_HTML_MODE_COMPACT));
        
        // Add to container (at the end, after chart and note)
        statsContainer.addView(statsView);
    }
}