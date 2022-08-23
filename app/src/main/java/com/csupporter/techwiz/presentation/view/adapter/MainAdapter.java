package com.csupporter.techwiz.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavAddAppointmentFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavHistoryFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavHomeFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavSearchFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavUserInfoFragment;

public class MainAdapter extends FragmentStateAdapter {

    public MainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new NavSearchFragment();
            case 2:
                return new NavAddAppointmentFragment();
            case 3:
                return new NavHistoryFragment();
            case 4:
                return new NavUserInfoFragment();
            case 0:
            default:
                return new NavHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
