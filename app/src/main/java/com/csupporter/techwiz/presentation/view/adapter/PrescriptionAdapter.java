package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {

    private List<Prescription> prescriptionsList;
    private OnItemClickListener mOnItemClickListener;

    public PrescriptionAdapter( OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPrescriptionsList(List<Prescription> prescriptionsList){
        this.prescriptionsList = prescriptionsList;
        notifyDataSetChanged();
    }
    public void addNewItemTrack(Prescription prescription) {
        prescriptionsList.add(0, prescription);
        notifyItemInserted(0);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription prescription = prescriptionsList.get(position);
        holder.setData(prescription);
        holder.itemView.setOnClickListener(v-> mOnItemClickListener.onItemCLick(prescription, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        if (prescriptionsList != null) {
            return prescriptionsList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitlePres, tvDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvTitlePres = itemView.findViewById(R.id.tv_title_pres);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
        }

        private void setData(Prescription model){
            tvTitlePres.setText(String.valueOf(model.getTitlePrescription()));
                    tvDateTime.setText(DateFormat.getDateTimeInstance().format(new Date(model.getCreateAt())));
        }

    }

    public interface OnItemClickListener {
        void onItemCLick(Prescription prescription, int pos);
    }
}
