package com.example.androidproject.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.androidproject.adapter.ViewPagerAdapter;
import com.example.androidproject.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    // Constants for tab titles
    private static final String TAB_TITLE_TRAINING = "Training";
    private static final String TAB_TITLE_HOME = "Home";
    private static final String TAB_TITLE_BATTLE = "Battle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, this::setTabTitle).attach();

    }
    /**
     * Sets the title for each tab based on its position.
     *
     * @param tab The tab to set the title for.
     * @param position The position of the tab.
     */
    private void setTabTitle(TabLayout.Tab tab, int position) {
        switch (position) {
            case 0:
                tab.setText(TAB_TITLE_HOME);
                break;
            case 1:
                tab.setText(TAB_TITLE_TRAINING);
                break;
            case 2:
                tab.setText(TAB_TITLE_BATTLE);
                break;
        }
    }
}