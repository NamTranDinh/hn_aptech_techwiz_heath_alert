package com.csupporter.techwiz.presentation.view.adapter;

import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.presentation.internalmodel.Departments;
import com.mct.components.toast.ToastUtils;


import de.hdodenhof.circleimageview.CircleImageView;

public class HomeCategoryDoctorAdapter extends RecyclerView.Adapter<HomeCategoryDoctorAdapter.HomeCategoryDoctorViewHolder> {

    private Departments[] departments;
    private final OnClickCategoryItems callback;
    private final int layout;
    private int currentType = App.getApp().getAccount().getDepartment();

    public HomeCategoryDoctorAdapter(OnClickCategoryItems callback, int layout) {
        this.callback = callback;
        this.layout = layout;
    }

    public void setCategoryDoctorList() {
        this.departments = Departments.values();
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }


    @NonNull
    @Override
    public HomeCategoryDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        return new HomeCategoryDoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryDoctorViewHolder holder, int position) {

        Departments depart = departments[position];

        String type = depart.getCategory();

        switch (type) {
            case "Dentist":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.blue), PorterDuff.Mode.MULTIPLY);
                break;
            case "Pediatrician":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.green_light), PorterDuff.Mode.MULTIPLY);
                break;
            case "Cardiologist":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.red_slight), PorterDuff.Mode.MULTIPLY);
                break;
            case "Beauty surgeon":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.purple_slight), PorterDuff.Mode.MULTIPLY);
                break;
            case "Psychologist":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.yellow_dark), PorterDuff.Mode.MULTIPLY);
                break;
            case "Obstetrician":
                holder.layout.getBackground().setColorFilter(ContextCompat.getColor(App.getContext(), R.color.gray), PorterDuff.Mode.MULTIPLY);
                break;
        }

        holder.image.setImageResource(depart.getResourceFile());
        holder.name.setText(depart.getCategory());

        int typeDoctor = getTypeByString(type);

        if (holder.cbCheckType != null) {
            holder.cbCheckType.setVisibility(typeDoctor == getCurrentType() ? View.VISIBLE : View.GONE);

        }

        holder.layout.setOnClickListener(view -> {
            if (typeDoctor >= 0) {
                if (getCurrentType() != typeDoctor) {
                    setCurrentType(typeDoctor);
                    notifyDataSetChanged();
                }
                callback.onClickCategoryItem(typeDoctor);
                Log.d("aaa", "onBindViewHolder: " + typeDoctor);
            }
        });
    }

    private int getTypeByString(String type) {
        if (type.equals("Dentist")) {
            return 0;
        } else if (type.equals("Pediatrician")) {
            return 1;
        } else if (type.equals("Cardiologist")) {
            return 2;
        } else if (type.equals("Beauty surgeon")) {
            return 3;
        } else if (type.equals("Psychologist")) {
            return 4;
        } else if (type.equals("Obstetrician")) {
            return 5;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return departments.length;
    }

    public static class HomeCategoryDoctorViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layout;
        private final ImageView image;
        private final TextView name;
        private final CheckBox cbCheckType;


        public HomeCategoryDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_doctor_image);
            name = itemView.findViewById(R.id.category_doctor_type);
            layout = itemView.findViewById(R.id.nav_home_category_items_layout);
            cbCheckType = itemView.findViewById(R.id.cb_check_type);
        }
    }

    public interface OnClickCategoryItems {
        void onClickCategoryItem(int typeDoctor);
    }
}
