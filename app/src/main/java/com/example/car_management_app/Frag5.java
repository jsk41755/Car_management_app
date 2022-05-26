package com.example.car_management_app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Frag5 extends Fragment {

    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    RecyclerAdapter adapter, adapter2, adapter3;
    private String eventURL = "https://autowash.co.kr/goods/goods_list.php?cateCd=032";
    private String shampooURL = "https://autowash.co.kr/goods/goods_list.php?cateCd=025";
    private String mittURL = "https://autowash.co.kr/goods/goods_list.php?cateCd=029";
    String tem, imgtem;
    TextView textView23;
    Bundle bundle = new Bundle();
    ImageView temimg;
    Context ct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab5_fragment, container, false);
        ct = container.getContext();

        recyclerView1 = v.findViewById(R.id.recyclerView);
        recyclerView2 = v.findViewById(R.id.recyclerView2);
        recyclerView3 = v.findViewById(R.id.recyclerView3);



        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ct);
        //recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ct);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(horizontalLayoutManager);

        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ct);
        horizontalLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(horizontalLayoutManager2);

        LinearLayoutManager horizontalLayoutManager3 = new LinearLayoutManager(ct);
        horizontalLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView3.setLayoutManager(horizontalLayoutManager3);



        adapter = new RecyclerAdapter();
        recyclerView1.setAdapter(adapter);
        adapter2 = new RecyclerAdapter();
        recyclerView2.setAdapter(adapter2);
        adapter3 = new RecyclerAdapter();
        recyclerView3.setAdapter(adapter3);

        getData();

/*
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
                    Document imgdoc = Jsoup.connect(URL).get();
                    Elements imgtemele = doc.select(".item_photo_box a img");
                    boolean imgEmpty = imgtemele.isEmpty();
                    Log.d("Tag", "isNull?" + imgEmpty);
                    if (imgEmpty == false) {
                        imgtem = imgtemele.get(0).attr("src");
                        Log.d("Tag", "이미지" + imgtem + "임");
                        Glide.with(temimg.getContext()).load(imgtem).into(temimg);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

 */

        return v;
    }
    private void getData(){
        ItemJsoup jsoupAsyncTask = new ItemJsoup();
        jsoupAsyncTask.execute();
    }

    private class ItemJsoup extends AsyncTask<Void, Void, Void> {
        ArrayList<String> listName = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName2 = new ArrayList<>();
        ArrayList<String> listCost2 = new ArrayList<>();
        ArrayList<String> listUrl2 = new ArrayList<>();
        ArrayList<String> listName3 = new ArrayList<>();
        ArrayList<String> listCost3 = new ArrayList<>();
        ArrayList<String> listUrl3 = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(eventURL).get();
                crawling(doc, listName, listCost, listUrl, adapter);
                Document doc2 = Jsoup.connect(shampooURL).get();
                crawling(doc2, listName2, listCost2, listUrl2, adapter2);
                Document doc3 = Jsoup.connect(mittURL).get();
                crawling(doc3, listName3, listCost3, listUrl3, adapter3);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    /*
Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        bundle = msg.getData();
        textView23.setText(bundle.getString("temperature"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
        Log.d("Tag", bundle.getString("temperature"));
    }
};


     */
    public void crawling(Document tempdoc, ArrayList<String> templistName, ArrayList<String> templistCost,
                         ArrayList<String> templistUrl, RecyclerAdapter temp){
        final Elements name_list = tempdoc.select(".item_tit_box");
        final Elements cost_list = tempdoc.select(".item_money_box");

        final Elements image_list = tempdoc.select(".item_photo_box a img.middle");
        Handler handler = new Handler(Looper.getMainLooper()); // 객체생성
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (Element element : name_list) {
                    templistName.add(element.text());
                }

                for (Element element : cost_list) {
                    templistCost.add(element.text());
                }

                for (Element element : image_list){
                    templistUrl.add(element.attr("src"));
                }

                for (int i = 0; i < 8 ; i++) {
                    ItemDTO data = new ItemDTO();
                    data.setitemName(templistName.get(i));
                    data.setitemImage(templistUrl.get(i));
                    data.setitemCost(templistCost.get(i));

                    temp.addItem(data);

                }
                temp.notifyDataSetChanged();
            }
        });
    }
}
