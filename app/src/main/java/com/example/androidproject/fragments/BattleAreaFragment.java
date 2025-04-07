package com.example.androidproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.adapter.LutemonAdapter;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.List;

public class BattleAreaFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battle_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewLutemons);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        updateLutemons();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLutemons();
    }

    private void updateLutemons() {
        if (recyclerView == null) {
            Log.d("BattleAreaFragment", "RecyclerView is null, skipping update");
            return;
        }

        try {
            LutemonManager manager = LutemonManager.getInstance(getContext());
            if (manager.getBattleArea() == null) {
                manager.initializeContainers();
            }

            List<Lutemon> lutemons = manager.getBattleArea().getAllLutemons();

            LutemonAdapter.OnItemClickListener listener = position -> {
                Lutemon lutemon = lutemons.get(position);
                // BATTLE LOGIC HERE
            };

            if (recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(new LutemonAdapter(lutemons, context, listener, "Battle Area"));
            } else {
                ((LutemonAdapter) recyclerView.getAdapter()).updateLutemons(lutemons);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("BattleAreaFragment", "Error updating lutemons: " + e.getMessage());
        }
    }
}