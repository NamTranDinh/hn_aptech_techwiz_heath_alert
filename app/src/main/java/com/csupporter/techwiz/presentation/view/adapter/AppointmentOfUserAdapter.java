package com.csupporter.techwiz.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;

import java.util.List;

public class AppointmentOfUserAdapter extends RecyclerView.Adapter<AppointmentOfUserAdapter.ViewHolder> {

    private List<Appointment> appointmentList;
    private OnItemClickListener mOnItemClickListener;
    private Account account;

    public AppointmentOfUserAdapter(Account account,OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
        this.account = account;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_appointment_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.setData(appointment, account);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appointmentsNameDoctor, appointmentsAddressDoctor, appointmentsTimeMeeting;
        private Button icCancel,icConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            appointmentsNameDoctor = itemView.findViewById(R.id.appointments_name_doctor);
            appointmentsAddressDoctor = itemView.findViewById(R.id.appointments_address_doctor);
            appointmentsTimeMeeting = itemView.findViewById(R.id.appointments_time_meeting);
            icCancel = itemView.findViewById(R.id.ic_cancel);
            icConfirm = itemView.findViewById(R.id.ic_confirm);
        }

        private void setData(Appointment data, Account account) {
            appointmentsNameDoctor.setText(String.valueOf(account.getFirstName()));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }
}
