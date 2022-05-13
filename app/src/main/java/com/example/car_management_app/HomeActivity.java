package com.example.car_management_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        CekSession();

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.view_pager);
        adapter=new FragmentAdapter(getSupportFragmentManager(),1);

        //FragmentAdapter에 컬렉션 담기
        adapter.addFragment(new Frag1());
        adapter.addFragment(new Frag2());
        adapter.addFragment(new Frag3());
        adapter.addFragment(new Frag4());
        adapter.addFragment(new Frag5());

        //ViewPager Fragment 연결
        viewPager.setAdapter(adapter);

        //ViewPager과 TabLayout 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("첫 번째");
        tabLayout.getTabAt(1).setText("두 번째");
        tabLayout.getTabAt(2).setText("세 번째");
        tabLayout.getTabAt(3).setText("네 번째");
        tabLayout.getTabAt(4).setText("다섯 번째");

        Intent intent = getIntent();
        //Log.d("tname", intent.getStringExtra("Name"));
    }

    private void CekSession() {
        Boolean Check = Boolean.valueOf(SharedPrefs2.readSharedSetting(HomeActivity.this, "CaptainCode", "true"));

        Intent introIntent = new Intent(HomeActivity.this, Car_Select_Activity.class);
        introIntent.putExtra("CaptainCode", Check);
        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
        if (Check) {
            Log.d("Check", String.valueOf(Check));
            startActivity(introIntent);
            finish();
        } //If no the Main Activity not Do Anything
    }
}