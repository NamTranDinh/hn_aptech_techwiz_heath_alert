package com.csupporter.techwiz.presentation.view.fragment.docters.nav;

import android.os.Bundle;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csupporter.techwiz.R;
import com.mct.components.baseui.BaseFragment;

public class MyUsersFragment extends BaseFragment {

    private RecyclerView rcvMyUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_user_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> {
            Toast.makeText(getActivity(), "dasdasdad", Toast.LENGTH_SHORT).show();
            return false;
        });
        rcvMyUserList = view.findViewById(R.id.rcv_my_user_list);
    }
}