package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;

import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.presentation.internalmodel.AppointmentDetail;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.HistoryAppointmentPresenter;
import com.csupporter.techwiz.presentation.presenter.authentication.UserHomePresenter;
import com.csupporter.techwiz.presentation.view.adapter.UserHomeAppointmentsAdapter;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.mct.components.baseui.BaseActivity;
import com.csupporter.techwiz.presentation.view.adapter.HomeCategoryDoctorAdapter;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavHomeFragment extends BaseNavFragment implements
        HomeCategoryDoctorAdapter.OnClickCategoryItems, View.OnClickListener, MainViewCallBack.GetAppointmentHistoryCallback, UserHomeAppointmentsAdapter.OnclickListener {

    private TextView txtMyAppointment;
    private ImageView imvNothing;
    private RecyclerView categoryDoctor;

    private RecyclerView rclAppointmentList;
    private HistoryAppointmentPresenter historyAppointmentPresenter;
    private UserHomeAppointmentsAdapter userHomeAppointmentsAdapter;

    private HomeCategoryDoctorAdapter homeCategoryDoctorAdapter;
    private CircleImageView avatar;
    private TextView name;

    private LoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_home, container, false);
        historyAppointmentPresenter = new HistoryAppointmentPresenter(this);
        historyAppointmentPresenter.requestAppointmentsDoctor(this);
        homeCategoryDoctorAdapter = new HomeCategoryDoctorAdapter(this, R.layout.nav_home_category_items);
        userHomeAppointmentsAdapter = new UserHomeAppointmentsAdapter();
        userHomeAppointmentsAdapter.setOnclickListener(this);

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Account account = App.getApp().getAccount();
        Glide.with(this).load(account.getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(avatar);
        name.setText(account.getFullName());
    }

    private void init(View view) {
        categoryDoctor = view.findViewById(R.id.category_doctor_list);
        rclAppointmentList = view.findViewById(R.id.home_list_appointment_of_day);
        imvNothing = view.findViewById(R.id.imv_nothing);
        name = view.findViewById(R.id.tv_username);
        txtMyAppointment = view.findViewById(R.id.nav_home_text);

        avatar = view.findViewById(R.id.img_avatar);
        avatar.setOnClickListener(this);

        setDataForUI();
        setDataCategoryDoctor();
    }

    @SuppressLint("SetTextI18n")
    private void setDataForUI() {
        Account account = App.getApp().getAccount();
        setDataAppointmentList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_bar:

                break;
            case R.id.img_avatar:
                changeTap(4, false);
                break;
        }
    }

    private void setDataCategoryDoctor() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        categoryDoctor.setLayoutManager(linearLayoutManager);
        homeCategoryDoctorAdapter.setCategoryDoctorList();
        categoryDoctor.setAdapter(homeCategoryDoctorAdapter);
    }

    private void setDataAppointmentList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rclAppointmentList.setLayoutManager(linearLayoutManager);
        rclAppointmentList.setAdapter(userHomeAppointmentsAdapter);
    }


    @Override
    public void onClickCategoryItem(int typeDoctor) {
        replaceFragment(AddDoctorFragment.newInstance(typeDoctor), true, BaseActivity.Anim.TRANSIT_FADE);
    }


    @Override
    public void showLoading() {
        if (getContext() == null) return;
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new LoadingDialog(getContext());
        dialog.create(null);
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onGetHistorySuccess(List<AppointmentDetail> appointmentDetails) {
        userHomeAppointmentsAdapter.setDetailList(appointmentDetails);
        if (appointmentDetails.isEmpty()) {
            imvNothing.setVisibility(View.VISIBLE);
        } else {
            imvNothing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onConfirm(AppointmentDetail appointmentDetail, int pos) {
        new ConfirmDialog(requireContext(),
                ConfirmDialog.LAYOUT_CONFIRM,
                R.drawable.ic_check_circle,
                "Click ok to confirm!",
                new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                        historyAppointmentPresenter.updateAppointment(appointmentDetail.getAppointment(), true, new MainViewCallBack.UpdateAppointmentCallback() {
                            @Override
                            public void onCreateSuccess() {
                                showToast("Update success!", ToastUtils.SUCCESS);
                                userHomeAppointmentsAdapter.notifyItemChanged(pos);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                            }
                        });
                    }

                    @Override
                    public void onCancel(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                    }
                }).create(null);
    }

    @Override
    public void onCancel(AppointmentDetail appointmentDetail, int pos) {
        new ConfirmDialog(requireContext(),
                ConfirmDialog.LAYOUT_HOLD_USER,
                R.drawable.ic_cancel_circle,
                "Are you sure to cancel this appointment?",
                new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                        historyAppointmentPresenter.updateAppointment(appointmentDetail.getAppointment(), false, new MainViewCallBack.UpdateAppointmentCallback() {
                            @Override
                            public void onCreateSuccess() {
                                showToast("Update success!", ToastUtils.SUCCESS);
                                userHomeAppointmentsAdapter.removeItem(appointmentDetail);
                                if (userHomeAppointmentsAdapter.getItemCount() == 0) {
                                    imvNothing.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                            }
                        });
                    }

                    @Override
                    public void onCancel(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                    }
                }).create(null);
    }

}