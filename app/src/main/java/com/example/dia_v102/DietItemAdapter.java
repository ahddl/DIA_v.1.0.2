package com.example.dia_v102;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.FoodCal;

import java.util.List;

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
        // 아래 부분은 영양성분 전체를 클래스 하나에 저장하게 만들어야 구현 가능.
        // holder.nutritionInfo.setText(dietItem.nutritionInfo);
        // 사진 URI를 ImageView에 로드하는 코드 추가
        String nutriateText = "칼로리: " + foodCal.getCalories() +
                "\n 탄수화물: " + foodCal.getCarbohydrate() +
                "\n 단백질: " + foodCal.getProtein() +
                "\n 지방: " + foodCal.getFat() +
                "\n 콜레스테롤: " + foodCal.getCholesterol() +
                "\n 나트륨: " + foodCal.getSodium() +
                "\n 설탕 당: " + foodCal.getSugar();
        holder.nutritionInfo.setText(nutriateText);
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
