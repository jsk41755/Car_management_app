package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Frag2 extends Fragment {
    TextView Car_inspection_Dday, EngineOil_Dday, Airconditioner_filter_Dday, CoolingWater_Dday,
            Wiper_blade_Dday, BreakOil_Dday;

    ProgressBar Car_inspection_progress, EngineOil_filter_progress, Airconditioner_filter_progress, CoolingWater_progress,
            Wiper_blade_progress, BreakOil_progress;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference("Car_Management");

    public static String kakaoID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_tab2_fragment,container,false);
        Bundle kakaoIDbundle = getArguments();
        kakaoID = kakaoIDbundle.getString("Name");
        //updateKakaoLoginUi();

        Car_inspection_Dday = (TextView) v.findViewById(R.id.Car_inspection_Dday);
        EngineOil_Dday = (TextView) v.findViewById(R.id.EngineOil_Dday);
        Airconditioner_filter_Dday = (TextView) v.findViewById(R.id.Airconditioner_filter_Dday);
        CoolingWater_Dday = (TextView) v.findViewById(R.id.CoolingWater_Dday);
        Wiper_blade_Dday = (TextView) v.findViewById(R.id.Wiper_blade_Dday);
        BreakOil_Dday = (TextView) v.findViewById(R.id.BreakOil_Dday);

        Car_inspection_progress = (ProgressBar) v.findViewById(R.id.Car_inspection_progress);
        EngineOil_filter_progress = (ProgressBar) v.findViewById(R.id.EngineOil_filter_progress);
        Airconditioner_filter_progress = (ProgressBar) v.findViewById(R.id.Airconditioner_filter_progress);
        CoolingWater_progress = (ProgressBar) v.findViewById(R.id.CoolingWater_progress);
        Wiper_blade_progress = (ProgressBar) v.findViewById(R.id.Wiper_blade_progress);
        BreakOil_progress = (ProgressBar) v.findViewById(R.id.BreakOil_progress);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Calendar cal2 = Calendar.getInstance();

        databaseReference.child(kakaoID).child("1").child("Supplies").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            double day = 0;
                            if(dataSnapshot3.getKey().equals("EngineOil")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                EngineOil_filter_progress.setProgress((int) (day/365*100));
                                if((int)(365-day) == 365)
                                    EngineOil_Dday.setText("오늘 교환하셨습니다.(1년 마다 교체)");
                                else
                                    EngineOil_Dday.setText( (int)(365-day) + "일 남음(1년 마다 교체)");
                            }
                            else if(dataSnapshot3.getKey().equals("CoolingWater")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                CoolingWater_progress.setProgress((int) (day/730*100));
                                if((int)(365-day) == 365)
                                    CoolingWater_Dday.setText("오늘 교환하셨습니다.(2년 마다 교체)");
                                else
                                    CoolingWater_Dday.setText((int)(730-day) + "일 남음(2년 마다 교체)");
                            }
                            else if(dataSnapshot3.getKey().equals("Wiper_blade")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                Wiper_blade_progress.setProgress((int) (day/365*100));
                                if((int)(365-day) == 365)
                                    Wiper_blade_Dday.setText("오늘 교환하셨습니다.(1년 마다 교체)");
                                else
                                    Wiper_blade_Dday.setText((int)(365-day) + "일 남음(1년 마다 교체)");
                            }
                            else if(dataSnapshot3.getKey().equals("Airconditioner_filter")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                Airconditioner_filter_progress.setProgress((int) (day/180*100));
                                if((int)(365-day) == 365)
                                    Airconditioner_filter_Dday.setText("오늘 교환하셨습니다.(6개월 마다 교체)");
                                else
                                    Airconditioner_filter_Dday.setText((int)(180-day) + "일 남음(6개월 마다 교체");
                            }
                            else if(dataSnapshot3.getKey().equals("BreakOil")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                BreakOil_progress.setProgress((int) (day/730*100));
                                if((int)(365-day) == 365)
                                    BreakOil_Dday.setText("오늘 교환하셨습니다.(1년마다 교체)");
                                else
                                    BreakOil_Dday.setText((int)(730-day) + "일 남았습니다. (2년 마다 교체)");
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}