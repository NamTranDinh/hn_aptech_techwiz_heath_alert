package com.csupporter.techwiz.presentation.view.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.Appointment;
import com.csupporter.techwiz.domain.model.AppointmentSchedule;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;
import com.mct.components.utils.SizeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddAppointmentDialog extends BaseOverlayDialog implements View.OnClickListener {
    private TextInputEditText txtDate;
    private TextInputEditText txtTime;
    private TextInputEditText txtDescription;
    private TextInputEditText lastEdtClick;
    private Toast mToast;
    private final Account doctor;
    private final OnBookNewAppointment onBookNewAppointment;
    private Calendar mDate;

    public AddAppointmentDialog(@NonNull Context context, Account doctor, OnBookNewAppointment onClickAddNewHealthTracking) {
        super(context);
        this.doctor = doctor;
        this.onBookNewAppointment = onClickAddNewHealthTracking;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_book_appointment, null);

        init(view);

        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Base_Animation_AppCompat_DropDownUp;
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected int getCornerRadius() {
        return 16;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {
        lastEdtClick = (TextInputEditText) view;
        switch (view.getId()) {
            case R.id.edt_date_end:
                showDatePicker(getDateFrom(lastEdtClick));
                break;
            case R.id.edt_time_end:
                showTimePicker(getTimeFrom(lastEdtClick));
                break;
        }
    }

    private void init(@NonNull View view) {
        txtDate = view.findViewById(R.id.edt_date_end);
        txtTime = view.findViewById(R.id.edt_time_end);
        txtDescription = view.findViewById(R.id.edt_event_description);
        AppCompatButton btnBook = view.findViewById(R.id.btn_book);
        initTime();
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        getTextInputLayout(txtDate).setEndIconOnClickListener(v -> txtDate.performClick());
        getTextInputLayout(txtTime).setEndIconOnClickListener(v -> txtTime.performClick());

        btnBook.setOnClickListener(v -> {
            long time = mDate.getTimeInMillis();
            if (time < System.currentTimeMillis()) {
                showToast("Time must be greater than present!", ToastUtils.WARNING);
                return;
            }
            Account account = App.getApp().getAccount();
            Appointment appointment = new Appointment();
            appointment.setId(FirebaseUtils.uniqueId());
            appointment.setUserId(account.getId());
            appointment.setDoctorId(doctor.getId());
            appointment.setDescription(txtDescription.getText().toString().trim());
            appointment.setTime(time);

            AppointmentSchedule appointmentSchedule = new AppointmentSchedule();
            appointmentSchedule.setId(appointment.getId());
            appointmentSchedule.setUserEmail(account.getEmail());
            appointmentSchedule.setDoctorEmail(doctor.getEmail());
            appointmentSchedule.setTime((int) (time / 1000));
            appointmentSchedule.setLocation(doctor.getLocation());
            appointmentSchedule.setStatus(appointment.getStatus());
            onBookNewAppointment.onBook(this, appointment, appointmentSchedule);
        });
    }

    private void initTime() {
        Date dateObj = new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000);
        mDate = Calendar.getInstance();
        mDate.setTime(dateObj);
        txtDate.setText(getFormat(true).format(dateObj));
        txtTime.setText(getFormat(false).format(dateObj));
    }


    private void showDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(context,
                this::onDateSet,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setCancelable(false);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getBackgroundColor());
        drawable.setCornerRadius(SizeUtils.dp2px(getCornerRadius()));
        dialog.getWindow().setBackgroundDrawable(new InsetDrawable(drawable, SizeUtils.dp2px(16)));
        dialog.show();
    }

    private void showTimePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TimePickerDialog dialog = new TimePickerDialog(context,
                this::onTimeSet,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        dialog.setCancelable(false);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getBackgroundColor());
        drawable.setCornerRadius(SizeUtils.dp2px(getCornerRadius()));
        dialog.getWindow().setBackgroundDrawable(new InsetDrawable(drawable, SizeUtils.dp2px(16)));
        dialog.show();
    }


    private void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mDate.set(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        onDateOrTimeSet(calendar.getTime(), true);
    }

    private void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mDate.set(Calendar.HOUR_OF_DAY, hour);
        mDate.set(Calendar.MINUTE, minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        onDateOrTimeSet(calendar.getTime(), false);
    }

    private void onDateOrTimeSet(Date date, boolean isDateFormat) {
        lastEdtClick.setText(getFormat(isDateFormat).format(date));
    }

    @NonNull
    private Date getDateFrom(TextInputEditText edt) {
        return parseDateOrTime(getText(edt), true);
    }

    @NonNull
    private Date getTimeFrom(TextInputEditText edt) {
        return parseDateOrTime(getText(edt), false);
    }

    @NonNull
    private Date parseDateOrTime(String text, boolean isDateFormat) {
        Date date = null;
        try {
            date = getFormat(isDateFormat).parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    private DateFormat getFormat(boolean isDateFormat) {
        return isDateFormat ? DateFormat.getDateInstance() : DateFormat.getTimeInstance(3);
    }

    protected String getText(@NonNull TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString();
    }

    protected TextInputLayout getTextInputLayout(@NonNull TextInputEditText editText) {
        ViewParent parent = editText.getParent();
        while (parent instanceof View) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    protected void showToast(String msg, int type) {
        if (context != null) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            mToast = ToastUtils.makeText(context, Toast.LENGTH_SHORT, type, msg, true);
            mToast.show();
        }
    }

    public interface OnBookNewAppointment {
        void onBook(BaseOverlayDialog dialog, Appointment appointment, AppointmentSchedule appointmentSchedule);
    }
}
