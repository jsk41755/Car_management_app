package com.example.car_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Car_Select_Activity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    Button car1;
    Button car2;

    String kakaoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        Intent intent = getIntent();
        kakaoID = intent.getStringExtra("Name");


        car1 = findViewById(R.id.Car1);
        car2 = findViewById(R.id.Car2);

        car1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarSelect(true, "Gasoline");
            }
        });
    }

    public void CarSelect(boolean isSelect, String Oil){
        Car_Select_Helper car_select = new Car_Select_Helper(isSelect, Oil);

        databaseReference.child("Car_Management").child(kakaoID).setValue(Oil);
        Intent intent2 = new Intent(Car_Select_Activity.this, HomeActivity.class);
        intent2.putExtra("Name",kakaoID);
        startActivity(intent2);
    }
}