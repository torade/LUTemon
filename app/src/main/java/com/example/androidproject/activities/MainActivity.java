package com.example.androidproject.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.androidproject.R;
import com.example.androidproject.adapter.ViewPagerAdapter;
import com.example.androidproject.containers.Home;
import com.example.androidproject.model.GreenLutemon;
import com.example.androidproject.model.WhiteLutemon;
import com.example.androidproject.model.Lutemon;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create and add lutemons to Home -- remove later on when create a lutemon button works
        Lutemon green = new GreenLutemon("Greeny");
        Lutemon white = new WhiteLutemon("Snowy");
        Home.getInstance().addLutemon(green);
        Home.getInstance().addLutemon(white);

        // Set up ViewPager and TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            break;
                        case 1:
                            tab.setText("Training");
                            break;
                        case 2:
                            tab.setText("Battle");
                            break;
//                      case 3:
//                           tab.setText("Statistics"); LET'S NOT FORGET ABOUT STATS
//                           break;
                    }
                }
        ).attach();
    }
}
