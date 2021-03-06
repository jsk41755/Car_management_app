package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class HomeActivity extends AppCompatActivity {

    public static Activity homeActivity;
    private static final String TAG = "Main_Activity";
    private SharedPreferences preferences;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private Button settings;

    TextView textView;
    public static String kakaoID;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {        //프레그먼트의 부모 노드
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setVolumeControlStream(R.layout.activity_home);
        homeActivity = HomeActivity.this;

        kakaoID = getIntent().getStringExtra("Name");      //카카오ID에 대한 Intent 값 불러오기
        if(kakaoID != null)
            Log.d("카카오 이름", kakaoID);

        if(kakaoID == null)
        {
            pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            String savedName = pref.getString("KakaoName", null);
            kakaoID = savedName;
            if (kakaoID != null)
                Log.d("임시",kakaoID);
        }

        Bundle kakaoIDbundle = new Bundle();    //각 Fragment에 대한 번들 값 넘겨주기
        kakaoIDbundle.putString("Name", kakaoID);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        CekSession();

        settings = findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {        //셋팅 화면으로 넘기기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                intent.putExtra("로그아웃","로그아웃완료");
                intent.putExtra("KakaoID",kakaoID);
                startActivity(intent);
            }
        });

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.view_pager);
        adapter=new FragmentAdapter(getSupportFragmentManager(),1);

        Fragment Fragment1 = new Frag1();
        Fragment1.setArguments(kakaoIDbundle);
        Fragment Fragment2 = new Frag2();
        Fragment2.setArguments(kakaoIDbundle);
        Fragment Fragment3 = new Frag3();
        Fragment3.setArguments(kakaoIDbundle);
        Fragment Fragment4 = new Frag4();
        Fragment4.setArguments(kakaoIDbundle);
        Fragment Fragment5 = new Frag5();
        Fragment5.setArguments(kakaoIDbundle);


        //FragmentAdapter에 컬렉션 담기
        adapter.addFragment(Fragment1);
        adapter.addFragment(Fragment2);
        adapter.addFragment(Fragment3);
        adapter.addFragment(Fragment4);
        adapter.addFragment(Fragment5);

        //ViewPager Fragment 연결
        viewPager.setAdapter(adapter);

        //ViewPager과 TabLayout 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("홈");
        tabLayout.getTabAt(1).setText("차량정보");
        tabLayout.getTabAt(2).setText("카계부");
        tabLayout.getTabAt(3).setText("지도");
        tabLayout.getTabAt(4).setText("용품추천");

        //tabLayout에 아이콘 설정 부분
        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.book_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.chart_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.map_icon);
        tabLayout.getTabAt(4).setIcon(R.drawable.cart_icon);

    }

    private void CekSession() { //이전 페이지로 넘어가지 않게하는 기능
        Boolean Check = Boolean.valueOf(SharedPrefs2.readSharedSetting(HomeActivity.this, "CaptainCode", "true"));
        //CaptainCode가 true라면 이전 페이지로 넘어가지지 않음.
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