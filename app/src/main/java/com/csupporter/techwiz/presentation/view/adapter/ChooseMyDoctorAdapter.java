package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.csupporter.techwiz.domain.model.Prescription;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseMyDoctorAdapter extends RecyclerView.Adapter<ChooseMyDoctorAdapter.ChooseMyDoctorViewHolder> {

    private List<Account> doctorList;
    private ArrayList<Integer> selectCheck = new ArrayList<>();
    private myDoctorIsCheck isCheck;

    public ChooseMyDoctorAdapter(myDoctorIsCheck isCheck) {
        this.isCheck = isCheck;
    }

    public void deleteItemTrack(int pos) {
        notifyItemRemoved(pos);
        doctorList.remove(pos);
    }

    public void addNewItemTrack(Account account) {
        doctorList.add(0, account);
        notifyItemInserted(0);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDoctorList(List<Account> doctorList) {
        this.doctorList = doctorList;

        for (int i = 0; i < doctorList.size(); i++) {
            selectCheck.add(0);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChooseMyDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_for_prescription_item, parent, false);
        return new ChooseMyDoctorAdapter.ChooseMyDoctorViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ChooseMyDoctorViewHolder holder, int position) {

        Account doctor = doctorList.get(position);

        if (selectCheck.get(position) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(view -> {
            for (int k = 0; k < selectCheck.size(); k++) {
                if (k == position) {
                    selectCheck.set(k, 1);
                } else {
                    selectCheck.set(k, 0);
                }
            }
            notifyDataSetChanged();

        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isCheck.isMyDoctorCheck(doctor);
                }
            }
        });
        Glide.with(App.getContext()).load(doctor.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(holder.doctorAvatar);

        holder.doctorName.setText(doctor.getFullName());
        holder.doctorLocation.setText(doctor.getLocation());
        int department = doctor.getDepartment();
        switch (department) {
            case 0:
                holder.doctorMajor.setImageResource(R.drawable.dentist);
                break;
            case 1:
                holder.doctorMajor.setImageResource(R.drawable.pediatrician_doctor);
                break;
            case 2:
                holder.doctorMajor.setImageResource(R.drawable.cardiologist_doctor);
                break;
            case 3:
                holder.doctorMajor.setImageResource(R.drawable.beauty_surgeon);
                break;
            case 4:
                holder.doctorMajor.setImageResource(R.drawable.psycho_doctor);
                break;
            case 5:
                holder.doctorMajor.setImageResource(R.drawable.obstetrical);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (doctorList != null) {
            return doctorList.size();
        }
        return 0;
    }

    public static class ChooseMyDoctorViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView doctorAvatar;
        private TextView doctorName;
        private TextView doctorLocation;
        private ImageView doctorMajor;
        private CheckBox checkBox;

        public ChooseMyDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorAvatar = itemView.findViewById(R.id.doctor_prescription_avatar);
            doctorName = itemView.findViewById(R.id.name_prescription_doctor);
            doctorLocation = itemView.findViewById(R.id.doctor_prescription_location);
            doctorMajor = itemView.findViewById(R.id.doctor_prescription_major);
            checkBox = itemView.findViewById(R.id.check_doctor);

        }
    }

    public interface myDoctorIsCheck {
        void isMyDoctorCheck(Account account);
    }
}
