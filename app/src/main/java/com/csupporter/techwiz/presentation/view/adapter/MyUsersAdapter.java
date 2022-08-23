package com.csupporter.techwiz.presentation.view.adapter;

import android.annotation.SuppressLint;
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

import java.util.List;

public class MyUsersAdapter extends RecyclerView.Adapter<MyUsersAdapter.MyUsersViewHolder> {

    private List<Account> myUserList;
    private OnClickViewDetail onClickViewDetail;

    public MyUsersAdapter(OnClickViewDetail onClickViewDetail) {
        this.onClickViewDetail = onClickViewDetail;
    }

    public void setMyUserList(List<Account> myUserList) {
        this.myUserList = myUserList;
    }

    @NonNull
    @Override
    public MyUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_user_items, parent, false);
        return new MyUsersAdapter.MyUsersViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyUsersViewHolder holder, int position) {
        Account account = myUserList.get(position);

        Glide.with(App.getContext()).load(account.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(holder.avatar);

        holder.username.setText(account.getLastName() + " " + account.getFirstName());

        holder.btnViewDetail.setOnClickListener(view -> {
            onClickViewDetail.onClickViewDetailPrescription();
        });

    }

    @Override
    public int getItemCount() {
        if (myUserList != null) {
            return myUserList.size();
        }
        return 0;
    }

    public static class MyUsersViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView username;
        private final AppCompatButton btnViewDetail;

        public MyUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_my_user);
            username = itemView.findViewById(R.id.tv_my_user_name);
            btnViewDetail = itemView.findViewById(R.id.btn_view_detail);
        }
    }

    public interface OnClickViewDetail {
        void onClickViewDetailPrescription();
    }
}
