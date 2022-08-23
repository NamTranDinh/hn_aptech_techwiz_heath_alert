package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.internalmodel.AppointmentDetail;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAppointmentAdapter extends RecyclerView.Adapter<HistoryAppointmentAdapter.HistoryAppointmentViewHolder> {

    private List<AppointmentDetail> appointmentDetails;
    private final OnItemClickListener listener;

    public HistoryAppointmentAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAppointmentDetails(List<AppointmentDetail> appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_appointment_items, parent, false);
        return new HistoryAppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAppointmentViewHolder holder, int position) {
        AppointmentDetail detail = appointmentDetails.get(position);
        if (detail == null) {
            return;
        }
        Account account = detail.getAcc();
        holder.tvName.setText(account.getFullName());
        holder.tvAddress.setText(account.getLocation());
        holder.tvTime.setText(DateFormat.getDateTimeInstance().format(new Date(detail.getAppointment().getTime())));
        Glide.with(holder.itemView.getContext()).load(account.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(holder.imgAvatar);

        View view;
        switch (detail.getAppointment().getStatus()) {
            case 0:
                view = account.isUser() ? holder.btnConfirm : holder.btnCancel;
                break;
            case 1:
                view = holder.btnCancel;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                view = holder.btnSetAgain;
                break;
            default:
                view = null;
        }
        holder.btnSetAgain.setVisibility(View.GONE);
        holder.btnCancel.setVisibility(View.GONE);
        holder.btnConfirm.setVisibility(View.GONE);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        holder.imgStatus.setImageResource(getImageByStatus(detail.getAppointment().getStatus()));
        holder.btnSetAgain.setOnClickListener(v -> {
            if (listener != null) listener.onClickSetAgain(detail, position);
        });
        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onClickCancel(detail, position);
        });
        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onClickConfirm(detail, position);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(detail, position);
        });
    }

    @Override
    public int getItemCount() {
        if (appointmentDetails != null) {
            return appointmentDetails.size();
        }
        return 0;
    }

    private int getImageByStatus(int status) {
        switch (status) {
            case 0:
                return R.drawable.ic_wait_circle;
            case 1:
                return R.drawable.ic_schedule_circle;
            case 2:
            case 3:
            case 4:
                return R.drawable.ic_cancel_circle;
            case 5:
            case 6:
                return R.drawable.ic_check_circle;
        }
        return 0;
    }

    static class HistoryAppointmentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, imgStatus;
        TextView tvName, tvAddress, tvTime;
        Button btnSetAgain, btnCancel, btnConfirm;

        public HistoryAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            imgStatus = itemView.findViewById(R.id.img_status);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnSetAgain = itemView.findViewById(R.id.btn_set_again);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }

    public interface OnItemClickListener {
        void onClickSetAgain(AppointmentDetail detail, int position);

        void onClickCancel(AppointmentDetail detail, int position);

        void onClickConfirm(AppointmentDetail detail, int position);

        void onClickItem(AppointmentDetail detail, int position);
    }
}
