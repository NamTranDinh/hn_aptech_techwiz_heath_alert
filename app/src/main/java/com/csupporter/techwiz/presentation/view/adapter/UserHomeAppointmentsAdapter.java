package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.presentation.internalmodel.AppointmentDetail;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeAppointmentsAdapter extends RecyclerView.Adapter<UserHomeAppointmentsAdapter.UserHomeAppointments_ViewHolder> {


    private List<AppointmentDetail> detailList;

    private OnclickListener mOnclickListener;

    public void setDetailList(List<AppointmentDetail> detailList) {
        this.detailList = detailList;
        notifyDataSetChanged();
    }

    public void removeItem(AppointmentDetail detail){
        if (detailList != null){
            detailList.remove(detail);
            notifyDataSetChanged();
        }
    };

    public List<AppointmentDetail> getDetailList() {
        return detailList;
    }

    public void setOnclickListener(OnclickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public UserHomeAppointments_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_appointment_items, parent, false);

        return new UserHomeAppointmentsAdapter.UserHomeAppointments_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHomeAppointments_ViewHolder holder, int position) {
        AppointmentDetail detail = detailList.get(position);
        if(detail==null){
            return;
        }

        holder.name.setText(detail.getAcc().getFullName());
        holder.address.setText(detail.getAcc().getLocation());

        holder.icConfirm.setOnClickListener(v -> mOnclickListener.onConfirm(detail, holder.getAdapterPosition()));

        holder.icCancel.setOnClickListener(v -> mOnclickListener.onCancel(detail, holder.getAdapterPosition()));

        if (detail.getAppointment().getStatus() == 0 ){
            holder.icCancel.setVisibility(View.VISIBLE);
            holder.icConfirm.setVisibility(View.VISIBLE);
        }else {
            holder.icConfirm.setVisibility(View.GONE);
            holder.icCancel.setVisibility(View.VISIBLE);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        String stringDate= dateFormat.format(detail.getAppointment().getTime());

        holder.time.setText(stringDate);

    }


    @Override
    public int getItemCount() {
        if (detailList != null) {
            return detailList.size();
        }
        return 0;
    }



    public static class UserHomeAppointments_ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView avatar;
        private final TextView name;
        private final TextView address;
        private final TextView time;
        private ImageView icConfirm,icCancel;

        public UserHomeAppointments_ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.appointments_avatar_doctor);
            name = itemView.findViewById(R.id.appointments_name_doctor);
            address = itemView.findViewById(R.id.appointments_address_doctor);
            time = itemView.findViewById(R.id.appointments_time_meeting);
            icConfirm = itemView.findViewById(R.id.ic_confirm);
            icCancel = itemView.findViewById(R.id.ic_cancel);
        }
    }

    public interface OnclickListener {
        void onConfirm(AppointmentDetail appointmentDetail, int pos);

        void onCancel(AppointmentDetail appointmentDetail, int pos );
    }
}
