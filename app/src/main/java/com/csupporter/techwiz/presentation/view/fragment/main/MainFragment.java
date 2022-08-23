package com.csupporter.techwiz.presentation.view.fragment.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.view.adapter.MainAdapter;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.BaseNavFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.toast.ToastUtils;


public class MainFragment extends BaseNavFragment implements BaseActivity.OnBackPressed {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton btnMakeAppointment;
    private MainAdapter mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (getActivity() == null) {
            return null;
        }
        mainAdapter = new MainAdapter(getActivity());

        init(view);
        setDataViewPager2();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentPage = viewPager2.getCurrentItem();
        if (currentPage != 1) {
            Fragment fragment = findFragmentByIndex(currentPage);
            if (fragment != null) {
                fragment.onResume();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (viewPager2 != null && viewPager2.getCurrentItem() != 0) {
            changeTap(0, false);
            return true;
        }
        return false;
    }

    private void init(View view) {
        viewPager2 = view.findViewById(R.id.view_pager2);
        viewPager2.setOffscreenPageLimit(5);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        btnMakeAppointment = view.findViewById(R.id.btn_appointment);
    }

    @SuppressLint("NonConstantResourceId")
    private void setDataViewPager2() {

//        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.user_info);
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(5);

        viewPager2.setAdapter(mainAdapter);
        viewPager2.setUserInputEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.user_home:
                    viewPager2.setCurrentItem(0, false);
                    break;
                case R.id.user_search:
                    viewPager2.setCurrentItem(1, false);
                    break;
                case R.id.user_history:
                    viewPager2.setCurrentItem(3, false);
                    break;
                case R.id.user_info:
                    viewPager2.setCurrentItem(4, false);
                    break;
            }
            return true;
        });

        btnMakeAppointment.setOnClickListener(view -> {
            viewPager2.setCurrentItem(2, false);
        });
        bottomNavigationView.getMenu().findItem(R.id.item_placeholder).setEnabled(false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.user_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.user_search).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item_placeholder).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.user_history).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.user_info).setChecked(true);
                        break;
                }
            }
        });
    }

    public void setNavVisible(boolean visible) {
        if (bottomNavigationView != null) {
            ((View) bottomNavigationView.getParent()).setVisibility(visible ? View.VISIBLE : View.GONE);
            btnMakeAppointment.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setEnableViewPager(boolean enable) {
        if (viewPager2 != null) {
            viewPager2.setUserInputEnabled(enable);
        }
    }

    public void changePage(int index, boolean smooth) {
        if (viewPager2 != null) {
            viewPager2.setCurrentItem(index, smooth);
        }
    }

    @Nullable
    private Fragment findFragmentByIndex(int index) {
        if (getActivity() != null) {
            return getParentFragmentManager().findFragmentByTag("f" + index);
        }
        return null;
    }

}