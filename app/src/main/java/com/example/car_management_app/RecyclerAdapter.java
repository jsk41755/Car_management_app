package com.example.car_management_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    Context ct;
    private ArrayList<ItemDTO> listData = new ArrayList<>();

    public interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    private RecyclerAdapter.OnItemClickLister listener;

    public void setOnSimpleItemClickListener(RecyclerAdapter.OnItemClickLister listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ct = ct = viewGroup.getContext();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new
                ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    void addItem(ItemDTO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName, itemCost;
        private ImageView itemImg;



        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemCost = itemView.findViewById(R.id.itemCost);
            itemImg = itemView.findViewById(R.id.imageView);

                    itemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ct);
                    dlg.setTitle("다른 사이트와의 비교(오토워시)");
                    dlg.setView(R.layout.wash_cost);
                    dlg.setPositiveButton("닫기", null);
                    dlg.show();
                }
            });

        }

        void onBind(ItemDTO data){
            itemName.setText(data.getitemName());
            itemCost.setText(data.getitemCost());
            Glide.with(itemView.getContext()).load(data.getitemImage()).into(itemImg);

        }
    }
}
