package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.MyDoctor;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.AddAppointmentPresenter;
import com.csupporter.techwiz.presentation.view.adapter.AddAppointmentAdapter;
import com.csupporter.techwiz.presentation.view.dialog.AddAppointmentDialog;
import com.csupporter.techwiz.presentation.view.dialog.AlertDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseOverlayLifecycle;
import com.mct.components.toast.ToastUtils;

import java.util.List;

public class NavAddAppointmentFragment extends BaseNavFragment implements View.OnClickListener,
        MainViewCallBack.GetAllMyDoctorCallBack, AddAppointmentAdapter.OnClickBookAppointment {

    private View llNoData;
    private LoadingDialog dialog;
    private AddAppointmentAdapter addAppointmentAdapter;
    private AddAppointmentPresenter addAppointmentPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addAppointmentPresenter = new AddAppointmentPresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_add_appointment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addAppointmentPresenter.getAllMyDoctor(this);
    }

    private void init(@NonNull View view) {
        RecyclerView rcvListMyDoctor = view.findViewById(R.id.rcv_list_my_doctor);
        FloatingActionButton btnAddNew = view.findViewById(R.id.add_new_doctor);
        btnAddNew.setOnClickListener(this);
        llNoData = view.findViewById(R.id.ll_no_data);

        addAppointmentAdapter = new AddAppointmentAdapter(this);
        rcvListMyDoctor.setAdapter(addAppointmentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvListMyDoctor.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(@NonNull View view) {
        if (view.getId() == R.id.add_new_doctor) {
            replaceFragment(AddDoctorFragment.newInstance(-1), true, BaseActivity.Anim.TRANSIT_FADE);
        }
    }

    @Override
    public void allMyDoctor(@NonNull List<Account> myDoctorList) {
        llNoData.setVisibility(myDoctorList.isEmpty() ? View.VISIBLE : View.GONE);
        addAppointmentAdapter.setDoctorList(myDoctorList);
    }

    @Override
    public void onClickItem(Account account) {
        Fragment fragment = DoctorInfoFragment.newInstance(account);
        replaceFragment(fragment, true, BaseActivity.Anim.TRANSIT_FADE);
    }

    @Override
    public void onClickBookAppointment(Account doctor) {
        new AddAppointmentDialog(requireContext(), doctor, (dialog, appointment, appointmentSchedule) -> {
            addAppointmentPresenter.createAppointment(appointment, appointmentSchedule, new MainViewCallBack.CreateAppointmentCallback() {
                @Override
                public void onCreateSuccess() {
                    dialog.dismiss();
                    if (getContext() != null) {
                        new AlertDialog(getContext(),
                                R.drawable.ic_check_circle,
                                "Appointment sent to doctor successfully!",
                                BaseOverlayLifecycle::dismiss).create(null);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast("Book appointment error", ToastUtils.ERROR);
                }
            });
        }).create(null);
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

}