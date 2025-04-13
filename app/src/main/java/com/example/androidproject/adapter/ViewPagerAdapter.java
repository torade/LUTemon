package com.example.androidproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidproject.fragments.BattleAreaFragment;
import com.example.androidproject.fragments.HomeAreaFragment;
import com.example.androidproject.fragments.StatisticsFragment;
import com.example.androidproject.fragments.TrainingAreaFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private HomeAreaFragment homeFragment;
    private TrainingAreaFragment trainingFragment;
    private BattleAreaFragment battleFragment;
    private StatisticsFragment statisticsFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        homeFragment = new HomeAreaFragment();
        trainingFragment = new TrainingAreaFragment();
        battleFragment = new BattleAreaFragment();
        statisticsFragment = new StatisticsFragment();

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return homeFragment;
            case 1:
                return trainingFragment;
            case 2:
                return battleFragment;
            case 3:
                return statisticsFragment;
            default:
                return homeFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        return itemId >= 0 && itemId < getItemCount();
    }
}