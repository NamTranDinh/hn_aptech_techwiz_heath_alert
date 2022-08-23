package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.presentation.internalmodel.AppointmentDetail;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.HistoryAppointmentPresenter;
import com.csupporter.techwiz.presentation.view.adapter.CustomSpinnerAdapter;
import com.csupporter.techwiz.presentation.view.adapter.HistoryAppointmentAdapter;
import com.csupporter.techwiz.presentation.view.dialog.AddAppointmentDialog;
import com.csupporter.techwiz.presentation.view.dialog.AlertDialog;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.SimpleCalendarListener;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.baseui.BaseOverlayLifecycle;
import com.mct.components.toast.ToastUtils;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NavHistoryFragment extends BaseNavFragment implements MainViewCallBack.GetAppointmentHistoryCallback, HistoryAppointmentAdapter.OnItemClickListener {


    private final String[] classifyName = {"All", "Waiting", "Scheduled", "Completed", "Cancelled"};
    private final int[] classifyIcon = {
            R.drawable.ic_all_circle,
            R.drawable.ic_wait_circle,
            R.drawable.ic_schedule_circle,
            R.drawable.ic_check_circle,
            R.drawable.ic_cancel_circle};
    private int classifySelected;
    private CollapsibleCalendar collapsibleCalendar;
    private Spinner classifyList;
    private View llLoading, llEmpty;
    private RecyclerView rcvHistoryMeet;
    private LoadingDialog dialog;
    private HistoryAppointmentAdapter historyAppointmentAdapter;
    private HistoryAppointmentPresenter historyAppointmentPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        historyAppointmentPresenter = new HistoryAppointmentPresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_history, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(@NonNull View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        collapsibleCalendar = view.findViewById(R.id.calendar_view);
        collapsibleCalendar.setSelectedDay(new Day(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1,
                calendar.get(Calendar.DAY_OF_MONTH)));
        classifyList = view.findViewById(R.id.status_appointment);
        llLoading = view.findViewById(R.id.ll_loading);
        llEmpty = view.findViewById(R.id.ll_empty);
        rcvHistoryMeet = view.findViewById(R.id.rcv_history_meet);
        collapsibleCalendar.setCalendarListener(new SimpleCalendarListener() {
            @Override
            public void onDaySelect() {
                loadAppointment();
            }
        });

        initRecyclerView();
        initSpinner();

    }

    private void initSpinner() {
        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(getActivity(), classifyIcon, classifyName);
        classifyList.setAdapter(customAdapter);
        classifyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classifySelected = i;
                loadAppointment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initRecyclerView() {
        historyAppointmentAdapter = new HistoryAppointmentAdapter(this);
        rcvHistoryMeet.setAdapter(historyAppointmentAdapter);
        rcvHistoryMeet.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    boolean isFirstCall;

    private void loadAppointment() {
        Day day = collapsibleCalendar.getSelectedDay();
        if (day == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(day.getYear(), isFirstCall ? day.getMonth() : day.getMonth() - 1, day.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        historyAppointmentPresenter.requestAppointments(getStatus(), calendar.getTimeInMillis(), this);
        showLoadingLocal();
        isFirstCall = true;
    }

    private List<Integer> getStatus() {
        switch (classifySelected) {
            default:
            case 0:
                return Arrays.asList(0, 1, 2, 3, 4, 5, 6);
            case 1:
                return Collections.singletonList(0);
            case 2:
                return Collections.singletonList(1);
            case 3:
                return Arrays.asList(5, 6);
            case 4:
                return Arrays.asList(2, 3, 4);
        }
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

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    private void showLoadingLocal() {
        handler.removeCallbacks(runnable);
        rcvHistoryMeet.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoadingLocal(boolean hasData) {
        llLoading.setVisibility(View.GONE);
        if (hasData) {
            rcvHistoryMeet.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
        } else {
            rcvHistoryMeet.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetHistorySuccess(@NonNull List<AppointmentDetail> appointmentDetails) {
        runnable = () -> hideLoadingLocal(!appointmentDetails.isEmpty());
        handler.postDelayed(runnable, 1000);
        historyAppointmentAdapter.setAppointmentDetails(appointmentDetails);
    }

    @Override
    public void onError(Throwable throwable) {
        runnable = () -> hideLoadingLocal(false);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onClickSetAgain(@NonNull AppointmentDetail detail, int position) {
        new AddAppointmentDialog(requireContext(), detail.getAcc(), (dialog, appointment, appointmentSchedule) ->
                historyAppointmentPresenter.createAppointment(appointment, appointmentSchedule, new MainViewCallBack.CreateAppointmentCallback() {
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
                })).create(null);
    }

    @Override
    public void onClickCancel(AppointmentDetail detail, int position) {
        new ConfirmDialog(requireContext(),
                ConfirmDialog.LAYOUT_HOLD_USER,
                R.drawable.ic_cancel_circle,
                "Are you sure to cancel this appointment?",
                new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                        historyAppointmentPresenter.updateAppointment(detail.getAppointment(), false, new MainViewCallBack.UpdateAppointmentCallback() {
                            @Override
                            public void onCreateSuccess() {
                                historyAppointmentAdapter.notifyItemChanged(position);
                                showToast("Update success!", ToastUtils.SUCCESS);
                                historyAppointmentAdapter.notifyItemChanged(position);
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
    public void onClickConfirm(AppointmentDetail detail, int position) {
        new ConfirmDialog(requireContext(),
                ConfirmDialog.LAYOUT_CONFIRM,
                R.drawable.ic_check_circle,
                "Click yes to confirm!",
                new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseOverlayDialog overlayDialog) {
                        overlayDialog.dismiss();
                        historyAppointmentPresenter.updateAppointment(detail.getAppointment(), true, new MainViewCallBack.UpdateAppointmentCallback() {
                            @Override
                            public void onCreateSuccess() {
                                historyAppointmentAdapter.notifyItemChanged(position);
                                showToast("Update success!", ToastUtils.SUCCESS);
                                historyAppointmentAdapter.notifyItemChanged(position);
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
    public void onClickItem(AppointmentDetail detail, int position) {

    }
}