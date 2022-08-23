package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csupporter.techwiz.presentation.view.fragment.main.MainFragment;
import com.mct.components.baseui.BaseFragment;

public abstract class BaseNavFragment extends BaseFragment {

    public void setTabVisible(boolean visible) {
        MainFragment mainFragment = getMainParentFragment();
        if (mainFragment == null) {
            return;
        }
        mainFragment.setNavVisible(visible);
    }

    public void setEnableViewPager(boolean enable) {
        MainFragment mainFragment = getMainParentFragment();
        if (mainFragment == null) {
            return;
        }
        mainFragment.setEnableViewPager(enable);
    }

    public void changeTap(int index, boolean smooth) {
        MainFragment mainFragment = getMainParentFragment();
        if (mainFragment == null) {
            return;
        }
        mainFragment.changePage(index, smooth);
    }

    @Nullable
    private MainFragment getMainParentFragment() {
        Fragment fragment = getParentFragmentManager().findFragmentByTag(MainFragment.class.getName());
        if (fragment instanceof MainFragment) {
            return (MainFragment) fragment;
        }
        return null;
    }

}
