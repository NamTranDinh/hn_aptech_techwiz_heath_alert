package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.MyDoctor;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.UserAppointmentPresenter;
import com.csupporter.techwiz.presentation.view.adapter.AppointmentOfUserAdapter;
import com.csupporter.techwiz.presentation.view.adapter.DoctorListAdapter;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.google.type.DateTime;
import com.mct.components.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NavSearchFragment extends BaseNavFragment implements SearchView.OnQueryTextListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final int SEARCH_NO_TYPE = 0;
    private final int SEARCH_TYPE_DOCTOR = 1;
    private final int SEARCH_TYPE_PRESCRIPTION = 2;
    private final int SEARCH_TYPE_APPOINTMENT = 3;
    private int CURRENT_TYPE;

    private View view;
    private Spinner spinner;
    private RecyclerView rcvSearchSt;
    private LinearLayout layoutMsg;
    private LoadingDialog dialog;
    private Account account;
    private SearchView viewSearchBar;

    private DoctorListAdapter doctorListAdapter;
    private UserAppointmentPresenter userAppointmentPresenter;
    private AppointmentOfUserAdapter appointment;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nav_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initSpinner();
        userAppointmentPresenter = new UserAppointmentPresenter(this);
        if (getActivity() != null) {
            dialog = new LoadingDialog(getActivity());
        }
    }

    @SuppressLint("ResourceType")
    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.src_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void initView(View view) {
        spinner = view.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        rcvSearchSt = view.findViewById(R.id.rcv_search_st);
        layoutMsg = view.findViewById(R.id.layout_msg);
        viewSearchBar = view.findViewById(R.id.view_search_bar);
        viewSearchBar.setOnClickListener(this);
        viewSearchBar.setOnQueryTextListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case SEARCH_TYPE_PRESCRIPTION:
                CURRENT_TYPE = SEARCH_TYPE_PRESCRIPTION;
                layoutMsg.setVisibility(View.GONE);

                break;
            case SEARCH_TYPE_APPOINTMENT:
                CURRENT_TYPE = SEARCH_TYPE_APPOINTMENT;
                layoutMsg.setVisibility(View.GONE);
                showDatePickerDialog();

                setAdapterAppointment();
                break;
            default:
//                SEARCH_TYPE_DOCTOR
                CURRENT_TYPE = SEARCH_TYPE_DOCTOR;
                if (rcvSearchSt.getAdapter() == null) {
                    layoutMsg.setVisibility(View.VISIBLE);
                }

                setAdapterDoctor();

        }
    }

    private void setAdapterAppointment() {
       /* userAppointmentPresenter.getAllAppointmentOfUserByDate(App.getApp().getAccount(), System.currentTimeMillis() - 19 * 60 * 60 * 1000, this);
        rcvSearchSt.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointment = new AppointmentOfUserAdapter(account, appointment -> {

        });*/
    }

    private void setAdapterDoctor() {
//        userAppointmentPresenter.getAllDoctor(this);
//        rcvSearchSt.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        doctorListAdapter = new DoctorListAdapter(account -> {
//
//        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showToast("Choose at least a type to search!!", ToastUtils.WARNING);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.search_bar:
                viewSearchBar.setIconified(false);
                viewSearchBar.onActionViewExpanded();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (CURRENT_TYPE == SEARCH_TYPE_DOCTOR) {
            layoutMsg.setVisibility(View.GONE);
            rcvSearchSt.setAdapter(doctorListAdapter);
        } else if (CURRENT_TYPE == SEARCH_TYPE_APPOINTMENT) {
            layoutMsg.setVisibility(View.GONE);

        } else if (CURRENT_TYPE == SEARCH_TYPE_PRESCRIPTION) {
            layoutMsg.setVisibility(View.GONE);

        }
        return false;
    }

    private String showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {

                }, mYear, mMonth, mDay);
        datePickerDialog.show();

        return null;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}