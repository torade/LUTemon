package com.example.androidproject.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.activities.CreateLutemonActivity;
import com.example.androidproject.adapter.LutemonAdapter;
import com.example.androidproject.containers.Home;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeAreaFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private String selectedColor = null;
    private String searchQuery = "";
    private int currentSortOption = 0; // 0: Name, 1: Max Health, 2: Wins
    private Button clearFilterButton;
    private List<Lutemon> filteredLutemons = new ArrayList<>();

    public HomeAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_area, container, false);
    }

    public void onResume() {
        super.onResume();
        updateLutemons();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the sort spinner
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);
        String[] sortOptions = {"Name", "Max Health", "Wins"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortOption = position;
                updateLutemons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the filter button
        ImageButton filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> showColorFilterDialog());

        // Clear filter button
        clearFilterButton = view.findViewById(R.id.clearFilterButton);
        clearFilterButton.setOnClickListener(v -> {
            selectedColor = null;
            updateLutemons();
            clearFilterButton.setVisibility(View.GONE);
        });

        // Set up search by color functionality
        EditText searchEditText = view.findViewById(R.id.searchColorEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase().trim();
                updateLutemons();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<Lutemon> lutemons = Home.getInstance().getAllLutemons();
        filteredLutemons.addAll(lutemons);

        LutemonAdapter.OnItemClickListener listener = position -> {
            // Handle item click if needed
        };

        LutemonAdapter adapter = new LutemonAdapter(filteredLutemons, context, listener, "Home");
        recyclerView.setAdapter(adapter);

        // Handle "Create LUTemon" button click
        Button createLutemonButton = view.findViewById(R.id.createLutemonButton);
        createLutemonButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateLutemonActivity.class);
            startActivity(intent);
        });
    }

    private void showColorFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Filter by Color");

        // Using the colors available in the Lutemon class
        final String[] colors = {"White", "Green", "Pink", "Orange", "Blue"};
        builder.setItems(colors, (dialog, which) -> {
            selectedColor = colors[which].toLowerCase();
            updateLutemons();
            if (clearFilterButton != null) {
                clearFilterButton.setVisibility(View.VISIBLE);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateLutemons() {
        List<Lutemon> lutemons;
        if (recyclerView == null) {
            Log.d("HomeAreaFragment", "RecyclerView is null, skipping update");
            return;
        }

        try {
            lutemons = LutemonManager.getInstance(getContext()).getHome().getAllLutemons();
        } catch (Exception e) {
            Log.e("HomeAreaFragment", "Error updating lutemons: " + e.getMessage());
            return;
        }

        // Apply filtering and searching
        filteredLutemons.clear();
        for (Lutemon lutemon : lutemons) {
            boolean colorMatches = selectedColor == null || lutemon.getColor().equalsIgnoreCase(selectedColor);
            boolean searchMatches = searchQuery.isEmpty() ||
                    lutemon.getColor().toLowerCase().contains(searchQuery) ||
                    lutemon.getName().toLowerCase().contains(searchQuery);

            if (colorMatches && searchMatches) {
                filteredLutemons.add(lutemon);
            }
        }

        // Apply sorting based on the Lutemon class getters
        switch (currentSortOption) {
            case 0: // Sort by Name
                Collections.sort(filteredLutemons, (l1, l2) ->
                        l1.getName().compareToIgnoreCase(l2.getName()));
                break;
            case 1: // Sort by Max Health
                Collections.sort(filteredLutemons, (l1, l2) ->
                        Integer.compare(l2.getMaxHealth(), l1.getMaxHealth()));
                break;
            case 2: // Sort by Wins (winCount in the Lutemon class)
                Collections.sort(filteredLutemons, (l1, l2) ->
                        Integer.compare(l2.getWinCount(), l1.getWinCount()));
                break;
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new LutemonAdapter(filteredLutemons, getContext(), position -> {
            }, "Home"));
        } else {
            ((LutemonAdapter) recyclerView.getAdapter()).updateLutemons(filteredLutemons);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}