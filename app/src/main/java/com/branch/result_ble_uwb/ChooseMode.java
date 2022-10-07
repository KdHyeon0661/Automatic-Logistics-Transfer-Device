package com.branch.result_ble_uwb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ChooseMode extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy {
    //global variables
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);

        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout);
        titles = new ArrayList<String>();
        titles.add("BLE");
        titles.add("UWB");
        titles.add("CONNECTED"); // 페이지 타이틀 추가
        setViewPagerAdapter(); // 프래그먼트들 추가
        new TabLayoutMediator(tabLayout, viewPager2, this).attach();
    }

    public void setViewPagerAdapter() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BlueTooth());
        fragmentList.add(new UWB());
        fragmentList.add(new Connected()); // 프래그먼트 추가
        viewPager2Adapter.setData(fragmentList); // 뷰페이지 어댑터에 추가
        viewPager2.setAdapter(viewPager2Adapter); // 뷰페이지에 어댑터를 추가. 반영됨

    }


    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}