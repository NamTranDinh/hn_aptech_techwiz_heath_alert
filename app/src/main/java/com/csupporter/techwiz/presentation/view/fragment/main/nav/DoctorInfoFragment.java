package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.mct.components.baseui.BaseFragment;


public class DoctorInfoFragment extends BaseFragment {

    private static final String KEY_DOCTOR_ARGS = "key_doctor";

    private Account doctor;
    ImageView imgAvatar;
    TextView tvName, tvPhone, tvLocation;
    ImageView imgCertificate;

    @NonNull
    public static DoctorInfoFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DOCTOR_ARGS, account);
        DoctorInfoFragment fragment = new DoctorInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        doctor = (Account) requireArguments().getSerializable(KEY_DOCTOR_ARGS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
    }

    private void initUi(@NonNull View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> popLastFragment());
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvName = view.findViewById(R.id.tv_name);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvLocation = view.findViewById(R.id.tv_location);
        imgCertificate = view.findViewById(R.id.img_certificate);

        Glide.with(this).load(doctor.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(imgAvatar);
        tvName.setText(doctor.getFullName());
        if (TextUtils.isEmpty(doctor.getPhone())) {
            tvPhone.setText("UNKNOWN");
        } else {
            tvPhone.setText(doctor.getPhone());
        }
        if (TextUtils.isEmpty(doctor.getLocation())) {
            tvLocation.setText("UNKNOWN");
        } else {
            tvLocation.setText(doctor.getLocation());
        }

        Glide.with(this).load(doctor.getCertificationUrl())
                .placeholder(R.drawable.image_place_holder)
                .error(R.drawable.image_place_holder)
                .into(imgCertificate);
    }
}