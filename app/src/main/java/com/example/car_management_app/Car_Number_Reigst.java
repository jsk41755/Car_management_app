package com.example.car_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Car_Number_Reigst extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    EditText CarNum_Edit;
    Spinner Car_Kinds_Select;
    Button CarNum_button;

    String CarNum, Spinner_text;
    String[] items = {"아반떼", "소나타", "그랜져"};
    String[] items2 = {"쏘렌토", "팰리세이드", "투싼"};

    ArrayAdapter<String> adapter;
    public static Activity carreigstActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_number_reigst);
        carreigstActivity = Car_Number_Reigst.this;

        CarNum_Edit = findViewById(R.id.CarNum_Edit);
        Car_Kinds_Select = findViewById(R.id.Car_Kinds_Select);
        CarNum_button = findViewById(R.id.CarNum_button);

        Intent intent = getIntent();
        String KakaoId = intent.getExtras().getString("Name");
        String Oil = intent.getExtras().getString("Oil");


        if(Oil.equals("diesel")){        //경유 차량일 때
             adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,items2
            );
        }
        else{                   //휘발유 차량일 때
             adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,items
            );
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);      //adapter와 item 연결.
        Car_Kinds_Select.setAdapter(adapter);
        Car_Kinds_Select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {       //자동차 종류를 선택했을 때
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner_text = Car_Kinds_Select.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        CarNum_button.setOnClickListener(new View.OnClickListener() {       //자동차 정보 등록 버튼을 눌렀을 때 DB 등록
            @Override
            public void onClick(View view) {
                CarNum = CarNum_Edit.getText().toString();
                databaseReference.child("Car_Management").child(KakaoId).child("1").child("Car_Information").child("Car_number").setValue(CarNum);
                databaseReference.child("Car_Management").child(KakaoId).child("1").child("Car_Information").child("Car_Kinds").setValue(Spinner_text);
                databaseReference.child("Car_Management").child(KakaoId).child("1").child("memo").setValue(null);
                Intent intent2 = new Intent(Car_Number_Reigst.this, HomeActivity.class);
                Log.d("차 등록", KakaoId);
                SharedPrefs.saveSharedSetting(Car_Number_Reigst.this , "KakaoName", KakaoId);

                intent2.putExtra("Name",KakaoId);
                startActivity(intent2);
                finish();
            }
        });
    }
}