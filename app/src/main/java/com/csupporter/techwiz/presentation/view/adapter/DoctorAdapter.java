package com.csupporter.techwiz.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csupporter.techwiz.presentation.view.fragment.docters.nav.DtHomeFragment;
import com.csupporter.techwiz.presentation.view.fragment.docters.nav.DtProfileFragment;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.NavHistoryFragment;

public class DoctorAdapter extends FragmentStateAdapter {

    public DoctorAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new NavHistoryFragment();
            case 2:
                return new DtProfileFragment();
            case 0:
            default:
                return new DtHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
