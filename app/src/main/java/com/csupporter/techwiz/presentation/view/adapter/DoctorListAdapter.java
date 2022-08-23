package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    private List<Account> doctorList;
    private final OnItemCLickListener mOnItemCLickListener;

    public DoctorListAdapter(OnItemCLickListener mOnItemCLickListener) {
        this.mOnItemCLickListener = mOnItemCLickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list_items, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDoctorList(List<Account> doctorList) {
        this.doctorList = doctorList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account doctorModel = doctorList.get(position);
        holder.setData(doctorModel , position);
        holder.itemView.setOnClickListener(v -> mOnItemCLickListener.onItemClick(doctorModel));
    }

    @Override
    public int getItemCount() {
        if (doctorList != null) {
            return doctorList.size();
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorAvatar;
        private TextView doctorName;
        private ImageView imgLike;
        private ImageView doctor_major;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }

        public void initView(@NonNull View itemView) {
            doctorAvatar = itemView.findViewById(R.id.img_avatar);
            doctorName = itemView.findViewById(R.id.doctor_name);
            imgLike = itemView.findViewById(R.id.like_doctor);
            doctor_major = itemView.findViewById(R.id.doctor_major);
        }

        public void setData(@NonNull Account doctorModel , int position) {

            Glide.with(App.getContext()).load(doctorModel.getAvatar())
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .into(doctorAvatar);

            doctorName.setText(doctorModel.getFirstName());

            int department = doctorModel.getDepartment();
            switch (department) {
                case 0:
                    doctor_major.setImageResource(R.drawable.dentist);
                    break;
                case 1:
                    doctor_major.setImageResource(R.drawable.pediatrician_doctor);
                    break;
                case 2:
                    doctor_major.setImageResource(R.drawable.cardiologist_doctor);
                    break;
                case 3:
                    doctor_major.setImageResource(R.drawable.beauty_surgeon);
                    break;
                case 4:
                    doctor_major.setImageResource(R.drawable.psycho_doctor);
                    break;
                case 5:
                    doctor_major.setImageResource(R.drawable.obstetrical);
                    break;
            }

            imgLike.setOnClickListener(view -> {

                mOnItemCLickListener.onClickLike(doctorModel, position);
            });

        }
    }

    public interface OnItemCLickListener {
        void onItemClick(Account account );

        void onClickLike(Account account, int position);

    }
}
