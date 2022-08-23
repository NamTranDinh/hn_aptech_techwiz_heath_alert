package com.csupporter.techwiz.presentation.view.fragment.docters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.csupporter.techwiz.R;
import com.csupporter.techwiz.presentation.view.adapter.DoctorAdapter;
import com.csupporter.techwiz.presentation.view.adapter.UserHomeAppointmentsAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mct.components.baseui.BaseFragment;

public class DoctorFragment extends BaseFragment {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private UserHomeAppointmentsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setDataViewPager2();

    }

    @Override
    public void onResume() {
        super.onResume();
        int currentPage = viewPager2.getCurrentItem();
        if (currentPage == 0 || currentPage == 2) {
            Fragment fragment = findFragmentByIndex(currentPage);
            if (fragment != null) {
                fragment.onResume();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setDataViewPager2() {
        DoctorAdapter doctorAdapter = new DoctorAdapter(requireActivity());
        viewPager2.setAdapter(doctorAdapter);
        viewPager2.setUserInputEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home:
                    viewPager2.setCurrentItem(0, false);
                    break;
                case R.id.item_history_medical:
                    viewPager2.setCurrentItem(1, false);
                    break;
                case R.id.item_profile:
                    viewPager2.setCurrentItem(2, false);
                    break;
            }
            return true;
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.item_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.item_history_medical).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item_profile).setChecked(true);
                        break;
                }
            }
        });
    }

    private void initView(@NonNull View view) {
        adapter = new UserHomeAppointmentsAdapter();
        viewPager2 = view.findViewById(R.id.dt_view_pager2);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
    }

    @Nullable
    private Fragment findFragmentByIndex(int index) {
        if (getActivity() != null) {
            return getParentFragmentManager().findFragmentByTag("f" + index);
        }
        return null;
    }

    public void changePage(int index, boolean smooth) {
        if (viewPager2 != null) {
            viewPager2.setCurrentItem(index, smooth);
        }
    }


}
