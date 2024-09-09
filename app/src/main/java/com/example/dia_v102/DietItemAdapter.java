package com.example.dia_v102;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.entities.DietItem;

import java.util.List;

public class DietItemAdapter extends RecyclerView.Adapter<DietItemAdapter.DietItemViewHolder> {

    private List<DietItem> dietItems;

    public DietItemAdapter(List<DietItem> dietItems) {
        this.dietItems = dietItems;
    }

    @Override
    public DietItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_item, parent, false);
        return new DietItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DietItemViewHolder holder, int position) {
        DietItem dietItem = dietItems.get(position);
        holder.menuName.setText(dietItem.menuName);
        holder.nutritionInfo.setText(dietItem.nutritionInfo);
        // 사진 URI를 ImageView에 로드하는 코드 추가
    }

    @Override
    public int getItemCount() {
        return dietItems.size();
    }

    public static class DietItemViewHolder extends RecyclerView.ViewHolder {
        TextView menuName;
        TextView nutritionInfo;
        ImageView photo;

        public DietItemViewHolder(View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            nutritionInfo = itemView.findViewById(R.id.nutrition_info);
            photo = itemView.findViewById(R.id.photo);
        }
    }
}
