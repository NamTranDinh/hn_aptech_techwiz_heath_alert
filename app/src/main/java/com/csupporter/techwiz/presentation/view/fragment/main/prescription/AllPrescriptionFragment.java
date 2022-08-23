package com.csupporter.techwiz.presentation.view.fragment.main.prescription;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.AllPrescriptionPresenter;
import com.csupporter.techwiz.presentation.view.adapter.PrescriptionAdapter;
import com.csupporter.techwiz.presentation.view.dialog.AddNewPrescriptionDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.AddNewPrescriptionFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.toast.ToastUtils;

import java.util.List;


public class AllPrescriptionFragment extends BaseFragment implements View.OnClickListener,
        MainViewCallBack.CreatePrescriptionCallBack,
        MainViewCallBack.GetAllPrescriptionCallBack,
        PrescriptionAdapter.OnItemClickListener {

    private View view;
    private RecyclerView rcvListPrescription;

    private LoadingDialog dialog;
    private AllPrescriptionPresenter allPrescriptionPresenter;
    private PrescriptionAdapter prescriptionAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        allPrescriptionPresenter = new AllPrescriptionPresenter(this);
        prescriptionAdapter = new PrescriptionAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_all_prescriotion, container, false);
        initView(view);
        allPrescriptionPresenter.getAllPrescription(this);

        return view;
    }


    private void initView(View view) {
        Toolbar toolbar = view.findViewById(R.id.tb_toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        toolbar.setOnMenuItemClickListener(item -> {
            new AddNewPrescriptionDialog(getActivity(), (prescription, dialog) -> {
                allPrescriptionPresenter.createPrescription(prescription, dialog, this);
            }).create(null);
            return false;
        });

        rcvListPrescription = view.findViewById(R.id.rcv_list_prescription);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getContext(), RecyclerView.VERTICAL, false);
        rcvListPrescription.setLayoutManager(linearLayoutManager);
        rcvListPrescription.setAdapter(prescriptionAdapter);
    }

    @Override
    public void onClick(View v) {

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
    public void onCreateSuccess(Prescription prescription) {
        prescriptionAdapter.addNewItemTrack(prescription);
        showToast("Create Prescription Success", ToastUtils.SUCCESS, true);
    }

    @Override
    public void onCreateFail() {
        showToast("Create Prescription Fail", ToastUtils.ERROR, true);
    }

    @Override
    public void allPrescriptions(List<Prescription> prescriptionList) {
        prescriptionAdapter.setPrescriptionsList(prescriptionList);
    }

    @Override
    public void onItemCLick(Prescription prescription, int pos) {
        replaceFragment(ListPrescriptionDetailFragment.newInstance(prescription), true, BaseActivity.Anim.TRANSIT_FADE);
    }
}