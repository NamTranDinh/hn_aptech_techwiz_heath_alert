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
import com.csupporter.techwiz.domain.model.HealthTracking;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HealthTrackItemAdapter extends RecyclerView.Adapter<HealthTrackItemAdapter.ViewHolder> {

    private List<HealthTracking> trackingList;
    private OnCLickItemTrack mOnCLickItemTrack;

    public HealthTrackItemAdapter(OnCLickItemTrack mOnCLickItemTrack) {
        this.mOnCLickItemTrack = mOnCLickItemTrack;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrackingList(List<HealthTracking> trackingList) {
        this.trackingList = trackingList;
        notifyDataSetChanged();
    }

    public void deleteItemTrack(int pos) {
        notifyItemRemoved(pos);
        trackingList.remove(pos);
    }

    public void addNewItemTrack(HealthTracking tracking) {
        trackingList.add(0, tracking);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_healthy_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthTracking healthTracking = trackingList.get(position);
        holder.setData(healthTracking);
        holder.itemView.setOnClickListener(v -> mOnCLickItemTrack.onClickItem(healthTracking, holder.getAdapterPosition()));
        holder.icEditTrack.setOnClickListener(v -> mOnCLickItemTrack.onEditClick(healthTracking, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        if (trackingList != null) {
            return trackingList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDateCreated, tvHeartBeat, tvBloodSugar, tvBloodOPressure, tvTimeCreated;
        private ImageView icEditTrack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void setData(HealthTracking data) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);

            String strDate = dateFormatter.format(data.getCreateAt());
            String strTime = timeFormatter.format(data.getCreateAt());

            tvDateCreated.setText(strDate);
            tvTimeCreated.setText(strTime);
            tvHeartBeat.setText(String.valueOf(data.getHeartbeat()));
            tvBloodSugar.setText(String.valueOf(data.getBloodSugar()));
            tvBloodOPressure.setText(String.valueOf(data.getBloodPressure()));
        }

        private void initView(View itemView) {
            tvDateCreated = itemView.findViewById(R.id.tv_date_created);
            tvTimeCreated = itemView.findViewById(R.id.tv_time_created);
            tvHeartBeat = itemView.findViewById(R.id.tv_heart_beat);
            tvBloodSugar = itemView.findViewById(R.id.tv_blood_sugar);
            tvBloodOPressure = itemView.findViewById(R.id.tv_blood_pressure);
            icEditTrack = itemView.findViewById(R.id.ic_edit_track);
        }
    }

    public interface OnCLickItemTrack {
        void onClickItem(HealthTracking healthTracking, int pos);

        void onEditClick(HealthTracking healthTracking, int pos);
    }
}
