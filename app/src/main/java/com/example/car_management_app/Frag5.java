package com.example.car_management_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Frag5 extends Fragment {
    private String URL = "https://autowash.co.kr/goods/goods_list.php?cateCd=032";
    String tem;
    TextView textView23;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab5_fragment, container, false);

        textView23 = v.findViewById(R.id.text1231);

        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(URL).get();
                    Elements temele = doc.select(".item_tit_box");
                    boolean isEmpty = temele.isEmpty();
                    Log.d("Tag", "isNull?" + isEmpty);
                    if (isEmpty == false) {
                        tem = temele.get(0).text();
                        bundle.putString("temperature", tem);
                        Log.d("Tag", "isNull?" + tem);
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return v;
        }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bundle = msg.getData();
            textView23.setText(bundle.getString("temperature"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
            Log.d("Tag", bundle.getString("temperature"));
        }
    };

    }
