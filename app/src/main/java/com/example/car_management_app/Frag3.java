package com.example.car_management_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.lang.ref.Reference;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Frag3 extends Fragment {
    private TableLayout tableLayout;
    static int count = 0;

    private static final String TAG = "Frag3";

    Spinner spinner;
    EditText editText;
    Button button;
    TextView time;

    BarChart barChart;
    ArrayList jsonList = new ArrayList<>(); // ArrayList 선언
    ArrayList<BarEntry> visitors = new ArrayList<>();
    Description description = new Description();

    public String kakaoID, Spinner_text;
    int price = 10000;
    String result;
    int edittext = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_tab3_fragment,container,false);
        Bundle kakaoIDbundle = getArguments();
        kakaoID = kakaoIDbundle.getString("Name");
        spinner = v.findViewById(R.id.spinner);
        editText = v.findViewById(R.id.editTextTextPersonName);
        button = v.findViewById(R.id.button12);
        result = NumberFormat.getCurrencyInstance(Locale.KOREA).format(price);
        time = v.findViewById(R.id.time);

        barChart = v.findViewById(R.id.cost_chart);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getInstance().getReference("Car_Management");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM월dd일");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        String getMonth = month.format(date);
        String getDay = day.format(date);

        String getTime = sdf.format(date);

        time.setText(getTime);

        initspinnerfooter();

        Log.d("카카오","시간체크3");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittext = Integer.parseInt(editText.getText().toString());
                databaseReference.child(kakaoID).child("1").child("Supplies").child(getMonth).child(getDay).child(Spinner_text).setValue(edittext);
            }
        });
        databaseReference.child(kakaoID).child("1").child("Supplies").child(getMonth).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout = (TableLayout) v.findViewById(R.id.tableLayout);
                tableLayout.removeAllViews();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        TableRow tableRow = new TableRow(v.getContext());
                        tableRow.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        for(int j = 0; j < 3; j++){
                            if(j == 0) {
                                TextView textView = new TextView(v.getContext());
                                textView.setText(snapshot.getKey() + " " + dataSnapshot.getKey());
                                textView.setTextSize(15);
                                textView.setTextColor(Color.parseColor("#000000"));
                                textView.setGravity(Gravity.CENTER);
                                tableRow.addView(textView);
                            }
                            if(j==1){
                                TextView textView = new TextView(v.getContext());
                                if(dataSnapshot2.getKey().equals("EngineOil")){
                                    textView.setText("엔진오일");
                                }
                                else if(dataSnapshot2.getKey().equals("CoolingWater")){
                                    textView.setText("냉각수");
                                }
                                else if(dataSnapshot2.getKey().equals("CarWash")){
                                    textView.setText("세차");
                                }
                                else if(dataSnapshot2.getKey().equals("Wiper_blade")){
                                    textView.setText("와이퍼 블레이드");
                                }
                                else if(dataSnapshot2.getKey().equals("Airconditioner_filter")){
                                    textView.setText("에어컨 필터");
                                }
                                else if(dataSnapshot2.getKey().equals("BreakOil")){
                                    textView.setText("브레이크 오일");
                                }
                                else
                                    textView.setText("옴뇽뇽뇽");
                                textView.setTextSize(15);
                                textView.setTextColor(Color.parseColor("#000000"));
                                textView.setGravity(Gravity.CENTER);
                                tableRow.addView(textView);
                            }
                            if(j==2){
                                TextView textView = new TextView(v.getContext());
                                textView.setText(dataSnapshot2.getValue().toString() + "원");
                                textView.setTextSize(15);
                                textView.setTextColor(Color.parseColor("#000000"));
                                textView.setGravity(Gravity.RIGHT);
                                tableRow.addView(textView);
                            }

                            if(tableRow.getParent() != null)
                                ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                            tableLayout.addView(tableRow);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(kakaoID).child("1").child("Supplies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                visitors.clear();
                int num = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int sum = 0;
                    jsonList.add((dataSnapshot.getKey())+ "월");
                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        int sum2 = 0;
                        for(DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            sum2 += Float.parseFloat(String.valueOf(dataSnapshot3.getValue()));
                        }
                        sum += sum2;
                    }
                    visitors.add(new BarEntry(num, sum));
                    num += 1;
                }
                BarDataSet barDataSet = new BarDataSet(visitors, "");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(20);

                BarData barData = new BarData(barDataSet);

                barChart.setData(barData);

                barChart.setDescription(null);
                barChart.animateY(2000);

                barChart.setTouchEnabled(false);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(jsonList));
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setTextSize(10f);
                xAxis.setDrawAxisLine(false);
                xAxis.setLabelCount(visitors.size());
                xAxis.setGranularityEnabled(true);

                YAxis yAxisRight = barChart.getAxisLeft(); //Y축의 오른쪽면 설정
                yAxisRight.setDrawLabels(false);
                yAxisRight.setDrawAxisLine(false);
                yAxisRight.setDrawGridLines(false);
                yAxisRight.setStartAtZero(true);

                Legend legend = barChart.getLegend();
                legend.setEnabled(false);

                barChart.setDragEnabled(false);
                barChart.setVisibleXRangeMaximum(5);

                float barSpace = 0.1f;
                float groupSpace = 0.5f;

                barData.setBarWidth(0.6f);

                barChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }


    private void initspinnerfooter() {
        String[] items = new String[]{"엔진오일","세차","냉각수","와이퍼 블레이드","에어컨 필터","브레이크 오일"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                 if(spinner.getSelectedItem().toString() == "엔진오일"){
                     Spinner_text = "EngineOil";
                }
                 else if(spinner.getSelectedItem().toString() == "세차"){
                    Spinner_text = "CarWash";
                }
                 else if(spinner.getSelectedItem().toString() == "냉각수"){
                     Spinner_text = "CoolingWater";
                 }
                 else if(spinner.getSelectedItem().toString() == "와이퍼 블레이드"){
                     Spinner_text = "Wiper_blade";
                 }
                 else if(spinner.getSelectedItem().toString() == "에어컨 필터"){
                     Spinner_text = "Airconditioner_filter";
                 }
                 else if(spinner.getSelectedItem().toString() == "브레이크 오일"){
                     Spinner_text = "BreakOil";
                 }
                 else{
                     Spinner_text = "CarWash";
                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}