package com.csupporter.techwiz.presentation.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.di.DataInjection;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.domain.model.MyDoctor;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.AddAppointmentPresenter;
import com.csupporter.techwiz.presentation.view.adapter.ChooseMyDoctorAdapter;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.baseui.BaseView;

import java.util.ArrayList;
import java.util.List;

public class AddNewPrescriptionDialog extends BaseOverlayDialog implements ChooseMyDoctorAdapter.myDoctorIsCheck {

    private EditText edtTitle;
    private RecyclerView rcvMyDoctorList;
    private AppCompatButton btnCreate;


    private ChooseMyDoctorAdapter chooseMyDoctorAdapter;
    private AddNewPrescriptionDialog.OnClickCreateNewPrescription onClickCreateNew;
    private String doctorId;

    public AddNewPrescriptionDialog(@NonNull Context context, AddNewPrescriptionDialog.OnClickCreateNewPrescription onClickCreateNew) {
        super(context);
        this.onClickCreateNew = onClickCreateNew;
    }

    @Override
    public void isMyDoctorCheck(Account account) {
        doctorId = account.getId();
    }


    public interface OnClickCreateNewPrescription {
        void onClickCreateNew(Prescription prescription,AddNewPrescriptionDialog dialog);
    }

    @NonNull
    @Override
    protected androidx.appcompat.app.AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.add_new_prescription_dialog, null);

        init(view);

        chooseMyDoctorAdapter = new ChooseMyDoctorAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getContext(), RecyclerView.VERTICAL, false);
        rcvMyDoctorList.setLayoutManager(linearLayoutManager);
        rcvMyDoctorList.setAdapter(chooseMyDoctorAdapter);

        setData();

        addEventAddClick();

        return new androidx.appcompat.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view);
    }

    private void setData() {

        Account acc = App.getApp().getAccount();

        DataInjection.provideRepository().myDoctor.getAllMyDoctor(acc, new Consumer<List<MyDoctor>>() {
            int count;
            List<Account> doctorList = new ArrayList<>();

            @Override
            public void accept(List<MyDoctor> myDoctorList) {

                for (MyDoctor data : myDoctorList) {

                    DataInjection.provideRepository().account.findAccountById(data.getDoctorId(), account -> {

                        if (account != null) {
                            doctorList.add(account);
                        }
                        ++count;
                        Log.e("ddd", "accept: " + count + myDoctorList.size());

                        if (count == myDoctorList.size()) {
                            Log.e("ddd", "EQUAL: " + doctorList.size());

                            chooseMyDoctorAdapter.setDoctorList(doctorList);

                        }

                    }, throwable -> {
                        ++count;
                        Log.e("ddd", "accept: " + count + myDoctorList.size());
                        if (count == myDoctorList.size()) {
                        }
                    });
                }
            }
        }, throwable -> {
        });
    }


    private void init(View view) {
        edtTitle = view.findViewById(R.id.edt_title_prescription);
        btnCreate = view.findViewById(R.id.btn_create_prescription);
        rcvMyDoctorList = view.findViewById(R.id.rcv_my_doctor);

    }

    private void addEventAddClick() {
        Account acc = App.getApp().getAccount();
        btnCreate.setOnClickListener(view -> {
            String title = edtTitle.getText().toString().trim();
            Prescription prescription = new Prescription();
            prescription.setId(FirebaseUtils.uniqueId());
            prescription.setDoctorId(doctorId);
            prescription.setUserId(acc.getId());
            prescription.setTitlePrescription(title);
            prescription.setCreateAt(System.currentTimeMillis());
            prescription.setUserCreate(true);
            onClickCreateNew.onClickCreateNew(prescription,this);
        });

    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().

                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().

                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().

                getAttributes().windowAnimations = androidx.appcompat.R.style.Base_Animation_AppCompat_DropDownUp;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
    }

}
