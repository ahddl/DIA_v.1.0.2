package com.example.dia_v102;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.FoodCal;

import java.util.List;

import com.example.dia_v102.utils.imgUtil;

public class DietItemAdapter extends RecyclerView.Adapter<DietItemAdapter.DietItemViewHolder> {

    private final List<FoodCal> foodCals;

    public DietItemAdapter(List<FoodCal> foodCal) {
        this.foodCals = foodCal;
    }

    @NonNull
    @Override
    public DietItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_item, parent, false);
        return new DietItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DietItemViewHolder holder, int position) {
        FoodCal foodCal = foodCals.get(position);
        holder.menuName.setText(foodCal.getFood());

        String nutriateText = "칼로리: " + foodCal.getCalories() + " kcal"+
                "\n 탄수화물: " + foodCal.getCarbohydrate() + " g" +
                "\n 단백질: " + foodCal.getProtein() + " g"+
                "\n 지방: " + foodCal.getFat() + "g"+
                "\n 콜레스테롤: " + foodCal.getCholesterol() + " mg"+
                "\n 나트륨: " + foodCal.getSodium() + " mg"+
                "\n 설탕 당: " + foodCal.getSugar() + " g";
        holder.nutritionInfo.setText(nutriateText);

        // 사진 URI를 ImageView에 set
        Context context = holder.itemView.getContext();
        String imgName = foodCal.getImgName();
        imgUtil.setImage(context, holder.photo, imgName);
    }

    @Override
    public int getItemCount() {
        return foodCals.size();
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
