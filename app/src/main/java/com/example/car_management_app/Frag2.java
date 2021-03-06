package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import android.util.Log;

import javax.xml.transform.Result;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Frag2 extends Fragment {
    TextView Car_inspection_Dday, EngineOil_Dday, Airconditioner_filter_Dday, CoolingWater_Dday,
            Wiper_blade_Dday, BreakOil_Dday;

    TextView Car_name, Car_Spec;
    private static String Car_name_st, Car_Spec_st;

    ProgressBar Car_inspection_progress, EngineOil_filter_progress, Airconditioner_filter_progress, CoolingWater_progress,
            Wiper_blade_progress, BreakOil_progress;

    ImageView imageView2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference("Car_Management");

    private FirebaseDatabase car_database = FirebaseDatabase.getInstance();
    private DatabaseReference car_databaseReference = car_database.getReference().child("Car_Spec");

    public static String kakaoID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_tab2_fragment,container,false);
        Bundle kakaoIDbundle = getArguments();
        kakaoID = kakaoIDbundle.getString("Name");

        Car_inspection_Dday = (TextView) v.findViewById(R.id.Car_inspection_Dday);
        EngineOil_Dday = (TextView) v.findViewById(R.id.EngineOil_Dday);
        Airconditioner_filter_Dday = (TextView) v.findViewById(R.id.Airconditioner_filter_Dday);
        CoolingWater_Dday = (TextView) v.findViewById(R.id.CoolingWater_Dday);
        Wiper_blade_Dday = (TextView) v.findViewById(R.id.Wiper_blade_Dday);
        BreakOil_Dday = (TextView) v.findViewById(R.id.BreakOil_Dday);

        Car_name = v.findViewById(R.id.Car_name);
        Car_Spec = v.findViewById(R.id.Car_Spec);

        imageView2 = (ImageView) v.findViewById(R.id.imageView2);

        Car_inspection_progress = (ProgressBar) v.findViewById(R.id.Car_inspection_progress);
        EngineOil_filter_progress = (ProgressBar) v.findViewById(R.id.EngineOil_filter_progress);
        Airconditioner_filter_progress = (ProgressBar) v.findViewById(R.id.Airconditioner_filter_progress);
        CoolingWater_progress = (ProgressBar) v.findViewById(R.id.CoolingWater_progress);
        Wiper_blade_progress = (ProgressBar) v.findViewById(R.id.Wiper_blade_progress);
        BreakOil_progress = (ProgressBar) v.findViewById(R.id.BreakOil_progress);

        Calendar cal = Calendar.getInstance();      //??????????????? ?????? ??????????????? ??????
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Calendar cal2 = Calendar.getInstance();


        imageView2.setOnClickListener(view -> {     //????????? ?????? ??????
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(intent);
        });

        databaseReference.child(kakaoID).child("1").child("Car_Information").child("Car_Kinds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override       //?????? ?????? ?????? ??????
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().equals("?????????")){
                    Car_name.setText("?????????");
                    Car_name_st = "Sorento";
                }
                else if(snapshot.getValue().equals("???????????????")){
                    Car_name.setText("???????????????");
                    Car_name_st = "Palisade";
                }
                Car_information(Car_name_st);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(kakaoID).child("1").child("Supplies").addValueEventListener(new ValueEventListener() {
            @Override       //????????? ??????????????? ?????? DB ???????????? ??? ?????? ?????? ??? ??????
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            double day = 0;
                            if(dataSnapshot3.getKey().equals("EngineOil")){     //??????????????? ?????? ?????? ??? ??????
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                EngineOil_filter_progress.setProgress((int) (day/365*100));
                                if((int)(365-day) == 365)
                                    EngineOil_Dday.setText("?????? ?????????????????????.(1??? ?????? ??????)");
                                else
                                    EngineOil_Dday.setText( (int)(365-day) + "??? ??????(1??? ?????? ??????)");
                            }
                            else if(dataSnapshot3.getKey().equals("CoolingWater")){     //???????????? ?????? ?????? ??? ??????
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                CoolingWater_progress.setProgress((int) (day/730*100));
                                if((int)(365-day) == 365)
                                    CoolingWater_Dday.setText("?????? ?????????????????????.(2??? ?????? ??????)");
                                else
                                    CoolingWater_Dday.setText((int)(730-day) + "??? ??????(2??? ?????? ??????)");
                            }
                            else if(dataSnapshot3.getKey().equals("Wiper_blade")){      //????????? ??????????????? ?????? ?????? ??? ??????
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                Wiper_blade_progress.setProgress((int) (day/365*100));
                                if((int)(365-day) == 365)
                                    Wiper_blade_Dday.setText("?????? ?????????????????????.(1??? ?????? ??????)");
                                else
                                    Wiper_blade_Dday.setText((int)(365-day) + "??? ??????(1??? ?????? ??????)");
                            }
                            else if(dataSnapshot3.getKey().equals("Airconditioner_filter")){        //????????? ????????? ?????? ?????? ??? ??????
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                Airconditioner_filter_progress.setProgress((int) (day/180*100));
                                if((int)(365-day) == 365)
                                    Airconditioner_filter_Dday.setText("?????? ?????????????????????.(6?????? ?????? ??????)");
                                else
                                    Airconditioner_filter_Dday.setText((int)(180-day) + "??? ??????(6?????? ?????? ??????");
                            }
                            else if(dataSnapshot3.getKey().equals("BreakOil")){     //???????????? ????????? ?????? ?????? ??? ??????
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                BreakOil_progress.setProgress((int) (day/730*100));
                                if((int)(365-day) == 365)
                                    BreakOil_Dday.setText("?????? ?????????????????????.(1????????? ??????)");
                                else
                                    BreakOil_Dday.setText((int)(730-day) + "??? ???????????????. (2??? ?????? ??????)");
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

    
    ActivityResultLauncher<Intent> launcher = registerForActivityResult     //?????? ????????? ????????? ?????? Intent ??????
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Log.e(null, "result : " + result);
                        Intent intent = result.getData();
                        Log.e(null, "intent : " + intent);
                        Uri uri = intent.getData();
                        Log.e(null, "uri : " + uri);
                        imageView2.setImageURI(uri);
                    }
                }
            });

    private void Car_information(String Car_information) {      //?????? ????????? ?????? ??????(DB?????? ?????????)
        car_databaseReference.child(Car_information).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Car_Spec_st = "????????? : " + snapshot.child("Displacement").getValue() + "\n"
                              + "?????? ?????? : " + snapshot.child("Fuel_efficiency").getValue() + "\n"
                              + "????????? ????????? : "  + snapshot.child("Tire_size").getValue();
                Car_Spec.setText(Car_Spec_st);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}