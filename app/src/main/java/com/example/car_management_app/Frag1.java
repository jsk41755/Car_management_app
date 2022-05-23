package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Frag1 extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private FirebaseDatabase car_database = FirebaseDatabase.getInstance();
    private DatabaseReference car_databaseReference = car_database.getReference().child("Car_Spec");

    TextView engine_oil_Dday, wiper_blade_Dday, Cost, Drive_tip;
    EditText textEdit;
    String TempText, Text, Car_name_st;
    public static String kakaoID;

    ImageView Brand_logo;

    LinearLayout DriveLayout;

    int max_num_value = 5;
    int min_num_value = 0;

    Random random = new Random();

    int randomNum = random.nextInt(max_num_value - min_num_value + 1) + min_num_value;
    List Quote_list = new ArrayList();

    int cost = 0;
    Context ct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View v=inflater.inflate(R.layout.activity_tab1_fragment,container,false);
        ct = container.getContext();
        Bundle kakaoIDbundle = getArguments();
        kakaoID = kakaoIDbundle.getString("Name");
        textEdit = v.findViewById(R.id.editTextTextPersonName2);

        engine_oil_Dday = v.findViewById(R.id.engine_oil_Dday);
        wiper_blade_Dday = v.findViewById(R.id.wiper_blade_Dday);
        Cost = v.findViewById(R.id.Cost);
        Brand_logo = v.findViewById(R.id.Brand_logo);
        Drive_tip = v.findViewById(R.id.Drive_tip);
        DriveLayout = v.findViewById(R.id.DriveLayout);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Calendar cal2 = Calendar.getInstance();

        Quote_list.add("자동차 정기 검사 언제 해야 할까?");
        Quote_list.add("자동차 검사를 받지 않는다면?");
        Quote_list.add("자동차 검사 수수료는 얼마인가요?");
        Quote_list.add("비탈진 좁은 길에서 \n누가 양보해야 할까?");
        Quote_list.add("졸음운전은 음주운전과도 같다?!");
        Quote_list.add("방향지시등 점등하셨나요? \n깜빡이를 잊지 말아주세요.");

        Drive_tip.setText(Quote_list.get(randomNum).toString());

        DriveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                View v = getLayoutInflater().inflate(R.layout.tip_activity, null);
                dlg.setView(v);
                dlg.setTitle(Quote_list.get(randomNum).toString());
                dlg.setPositiveButton("닫기", null);
                dlg.show();

                List<SlideModel> slideModels = new ArrayList<>();
                slideModels.add(new SlideModel(R.drawable.drive1, ScaleTypes.CENTER_CROP));
                slideModels.add(new SlideModel(R.drawable.drive2, ScaleTypes.CENTER_CROP));
                slideModels.add(new SlideModel(R.drawable.drive3, ScaleTypes.CENTER_CROP));
                ImageSlider imageSlider = v.findViewById(R.id.imageSlider);
                imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);

                Log.d("Num", String.valueOf(slideModels.isEmpty()));
            }
        });

        databaseReference.child("Car_Management").child(kakaoID).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TempText = snapshot.child("memo").getValue(String.class);
                textEdit.setText(TempText);

                if(snapshot.child("Car_Information").child("Car_Kinds").getValue().equals("쏘렌토")){
                    Car_name_st = "Audi.png";
                }
                else{

                }
                Car_information(Car_name_st);

                for(DataSnapshot dataSnapshot : snapshot.child("Supplies").getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            double day = 0;
                            if (dataSnapshot3.getKey().equals("EngineOil")) {
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey()) - 1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                                if ((int) (365 - day) == 365)
                                    engine_oil_Dday.setText("엔진오일 교환 완료");
                                else
                                    engine_oil_Dday.setText("엔진오일 D + " + (int) (365 - day));
                            }
                            else if(dataSnapshot3.getKey().equals("Wiper_blade")){
                                cal2.set(2022, Integer.parseInt(dataSnapshot.getKey())-1, Integer.parseInt(dataSnapshot2.getKey()));
                                day = (cal.getTimeInMillis() - cal2.getTimeInMillis())/(24*60*60*1000);
                                if((int)(365-day) == 365)
                                    wiper_blade_Dday.setText("와이퍼 교환 완료");
                                else
                                    wiper_blade_Dday.setText("와이퍼 D-" + (int)(365-day));
                            }
                            cost += Integer.parseInt(String.valueOf(dataSnapshot3.getValue()));
                            Cost.setText("이번 달" + cost + "원 지출");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Text = textEdit.getText().toString();
                databaseReference.child("Car_Management").child(kakaoID).child("1").child("memo").setValue(Text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    private void Car_information(String car_name_st) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child(car_name_st);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Frag1.this).load(uri).into(Brand_logo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}