package com.example.androidproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.util.List;

public class HomeAreaFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<Lutemon> lutemons = Home.getInstance().getAllLutemons();

        LutemonAdapter.OnItemClickListener listener = position -> {
            // Handle item click if needed
        };

        LutemonAdapter adapter = new LutemonAdapter(lutemons, context, listener, "Home");
        recyclerView.setAdapter(adapter);

        // Handle "Create LUTemon" button click
        Button createLutemonButton = view.findViewById(R.id.createLutemonButton);
        createLutemonButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateLutemonActivity.class);
            startActivity(intent);
        });
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
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new LutemonAdapter(lutemons, getContext(), position -> {
            }, "Home"));
        } else {
            ((LutemonAdapter) recyclerView.getAdapter()).updateLutemons(lutemons);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
