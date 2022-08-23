package com.csupporter.techwiz.presentation.view.adapter;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAppointmentAdapter extends RecyclerView.Adapter<AddAppointmentAdapter.AddAppointmentViewHolder> {

    private List<Account> doctorList;
    private final OnClickBookAppointment onClickBookAppointment;

    public AddAppointmentAdapter(OnClickBookAppointment onClickBookAppointment) {
        this.onClickBookAppointment = onClickBookAppointment;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDoctorList(List<Account> doctorList) {
        this.doctorList = doctorList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_doctors_items, parent, false);
        return new AddAppointmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddAppointmentViewHolder holder, int position) {
        Account doctor = doctorList.get(position);

        Glide.with(App.getContext()).load(doctor.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(holder.doctorImage);

        holder.tvNameDoctor.setText(doctor.getFullName());
        if (TextUtils.isEmpty(doctor.getLocation())) {
            holder.tvLocation.setVisibility(View.GONE);
            holder.btnBookAppointment.setVisibility(View.GONE);
        } else {
            holder.tvLocation.setText(doctor.getLocation());
            holder.tvLocation.setVisibility(View.VISIBLE);
            holder.btnBookAppointment.setVisibility(View.VISIBLE);
        }
        int department = doctor.getDepartment();
        switch (department) {
            case 0:
                holder.img_major.setImageResource(R.drawable.dentist);
                break;
            case 1:
                holder.img_major.setImageResource(R.drawable.pediatrician_doctor);
                break;
            case 2:
                holder.img_major.setImageResource(R.drawable.cardiologist_doctor);
                break;
            case 3:
                holder.img_major.setImageResource(R.drawable.beauty_surgeon);
                break;
            case 4:
                holder.img_major.setImageResource(R.drawable.psycho_doctor);
                break;
            case 5:
                holder.img_major.setImageResource(R.drawable.obstetrical);
                break;
        }
        holder.itemView.setOnClickListener(v -> {
            if (onClickBookAppointment != null) {
                onClickBookAppointment.onClickItem(doctor);
            }
        });
        holder.btnBookAppointment.setOnClickListener(view -> {
            if (onClickBookAppointment != null) {
                onClickBookAppointment.onClickBookAppointment(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (doctorList != null) {
            return doctorList.size();
        }
        return 0;
    }

    public static class AddAppointmentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView doctorImage;
        private final TextView tvNameDoctor;
        private final TextView tvLocation;
        private final ImageView img_major;
        private final AppCompatButton btnBookAppointment;

        public AddAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.img_doctor_avatar);
            tvNameDoctor = itemView.findViewById(R.id.tv_name_doctor);
            tvLocation = itemView.findViewById(R.id.tv_doctor_location);
            img_major = itemView.findViewById(R.id.img_doctor_major);
            btnBookAppointment = itemView.findViewById(R.id.btn_book_appointment);
        }
    }

    public interface OnClickBookAppointment {

        void onClickItem(Account account);

        void onClickBookAppointment(Account doctor);
    }
}
