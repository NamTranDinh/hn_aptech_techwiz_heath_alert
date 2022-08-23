package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.HealthTracking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mct.components.baseui.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DtCheckListRequiteFragment extends BaseFragment implements View.OnClickListener{

    private FloatingActionButton btnAdd;

    private List<HealthTracking> healthTrackingList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_healthy_tracking, container, false);
        init(view);
        return view;

    }

    private void init(View view) {
        btnAdd = view.findViewById(R.id.btn_add_track);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_track:

                break;
        }
    }

    private void createAddDialog(){
        Dialog dialogBottom = new Dialog(getActivity());
        ;
    }
}


