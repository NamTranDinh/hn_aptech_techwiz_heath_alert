package com.csupporter.techwiz.presentation.view.fragment.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.NavUserInfoPresenter;
import com.csupporter.techwiz.presentation.presenter.authentication.ProfilePresenter;
import com.csupporter.techwiz.presentation.view.adapter.HomeCategoryDoctorAdapter;
import com.csupporter.techwiz.presentation.view.dialog.ChooseGenderDialog;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.CropImageDialog;
import com.csupporter.techwiz.presentation.view.dialog.EnterChangePasswordDialog;
import com.csupporter.techwiz.presentation.view.dialog.EnterLocationDialog;
import com.csupporter.techwiz.presentation.view.dialog.EnterNameDialog;
import com.csupporter.techwiz.presentation.view.dialog.EnterPhoneDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.utils.PermissionUtils;
import com.csupporter.techwiz.utils.PickPictureResult;
import com.isseiaoki.simplecropview.CropImageView;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, BaseActivity.OnBackPressed, MainViewCallBack.UpdateProfileCallback {

    private Account tmpAccount;
    private View mView;
    private TextView tvName, tvPhone, tvGender, tvLocation;
    private ProfilePresenter profilePresenter;
    private LoadingDialog dialog;
    private LinearLayoutCompat llDoctorInfo;
    private ImageView imvCertificate;
    private RecyclerView categoryDoctorList;

    private NavUserInfoPresenter navUserInfoPresenter;


    private final ActivityResultLauncher<Void> mPickPictureResult =
            registerForActivityResult(new PickPictureResult(), uri -> {
                if (isContextNull() || uri == null) {
                    return;
                }
                new CropImageDialog(requireContext(), uri, CropImageView.CropMode.FREE, new CropImageDialog.CropImageListener() {

                    @Override
                    public void onCropSuccess(BaseOverlayDialog dialog, Bitmap bitmap) {
                        dialog.dismiss();
                        showLoading();
                        new Thread() {
                            @Override
                            public void run() {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                navUserInfoPresenter.uploadCertificate(App.getApp().getAccount(), stream.toByteArray(), new MainViewCallBack.UpdateCertificate() {
                                    @Override
                                    public void onSuccess(String url) {
                                        hideLoading();
                                        imvCertificate.post(() -> imvCertificate.setImageBitmap(bitmap));
                                        showToast("Change success!", ToastUtils.SUCCESS, true);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        hideLoading();
                                        showToast("Change error!", ToastUtils.ERROR, true);
                                    }
                                });
                            }
                        }.start();
                    }

                }).create(null);
            });


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        profilePresenter = new ProfilePresenter(this);
        tmpAccount = App.getApp().getAccount().copy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        initUi();
        checkTypeShowLayout();
        navUserInfoPresenter = new NavUserInfoPresenter(this);
        return mView;
    }

    private void checkTypeShowLayout() {
        if (App.getApp().getAccount().getType() == Account.TYPE_USER) {
            llDoctorInfo.setVisibility(View.GONE);
        } else {
            llDoctorInfo.setVisibility(View.VISIBLE);
        }
    }

    private void initUi() {
        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_confirm);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> {
            if (isDataChange()) {
                App.getApp().setAccount(tmpAccount);
                profilePresenter.updateProfile(tmpAccount, this);
            }
            return false;
        });

        tvName = mView.findViewById(R.id.tv_name);
        tvPhone = mView.findViewById(R.id.tv_phone);
        tvGender = mView.findViewById(R.id.tv_gender);
        tvLocation = mView.findViewById(R.id.tv_location);
        llDoctorInfo = mView.findViewById(R.id.ll_doctor_info);
        imvCertificate = mView.findViewById(R.id.imv_certificate);
        categoryDoctorList = mView.findViewById(R.id.category_doctor_list);

        mView.findViewById(R.id.cv_name).setOnClickListener(this);
        mView.findViewById(R.id.cv_phone).setOnClickListener(this);
        mView.findViewById(R.id.cv_password).setOnClickListener(this);
        mView.findViewById(R.id.cv_gender).setOnClickListener(this);
        mView.findViewById(R.id.cv_location).setOnClickListener(this);
        imvCertificate.setOnClickListener(this);

        setData();
        setDataCategoryDoctor();
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public void onClick(@NonNull View v) {
        initTmpAcc();
        switch (v.getId()) {
            case R.id.cv_name:
                new EnterNameDialog(requireContext(), tmpAccount, (firstName, lastName) -> {
                    tmpAccount.setFirstName(firstName);
                    tmpAccount.setLastName(lastName);
                    setData();
                }).create(null);
                break;
            case R.id.cv_phone:
                new EnterPhoneDialog(requireContext(), tmpAccount, phone -> {
                    tmpAccount.setPhone(phone);
                    setData();
                }).create(null);
                break;
            case R.id.cv_password:
                new EnterChangePasswordDialog(requireContext(), tmpAccount, tmpAccount::setPassword).create(null);
                break;
            case R.id.cv_gender:
                new ChooseGenderDialog(requireContext(), gender -> {
                    tmpAccount.setGender(gender);
                    setData();
                }).create(null);
                break;
            case R.id.cv_location:
                new EnterLocationDialog(requireContext(), tmpAccount, location -> {
                    tmpAccount.setLocation(location);
                    setData();
                }).create(null);
                break;
            case R.id.imv_certificate:
                PermissionUtils.requestReadStoragePermission(this, (allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        mPickPictureResult.launch(null);
                    }
                });
                break;
        }
    }

    private void setData() {
        tvName.setText(tmpAccount.getFullName());
        tvPhone.setText(TextUtils.isEmpty(tmpAccount.getPhone()) ? "- - - - - -" : tmpAccount.getPhone());
        tvGender.setText(tmpAccount.getGender() == 0 ? "Male" : "Female");
        tvLocation.setText((tmpAccount.getLocation()));
        Glide.with(this).load(tmpAccount.getCertificationUrl())
                .placeholder(R.drawable.ic_camera)
                .error(R.drawable.ic_camera)
                .into(imvCertificate);
    }

    private void setDataCategoryDoctor() {

        HomeCategoryDoctorAdapter homeCategoryDoctorAdapter = new HomeCategoryDoctorAdapter(typeDoctor -> {
            initTmpAcc();
            tmpAccount.setDepartment(typeDoctor);
        }, R.layout.item_category_doctor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        categoryDoctorList.setLayoutManager(linearLayoutManager);
        homeCategoryDoctorAdapter.setCategoryDoctorList();
        categoryDoctorList.setAdapter(homeCategoryDoctorAdapter);
    }

    private boolean isDataChange() {
        if (tmpAccount == null) {
            return false;
        }
        Account realAcc = App.getApp().getAccount();
        if (!Objects.equals(realAcc.getFirstName(), tmpAccount.getFirstName())) {
            return true;
        }
        if (!Objects.equals(realAcc.getLastName(), tmpAccount.getLastName())) {
            return true;
        }
        if (!Objects.equals(realAcc.getPhone(), tmpAccount.getPhone())) {
            return true;
        }
        if (!Objects.equals(realAcc.getPassword(), tmpAccount.getPassword())) {
            return true;
        }

        if (!tmpAccount.isUser()) {
            if (!Objects.equals(realAcc.getLocation(), tmpAccount.getLocation())) {
                return true;
            }
            if (!Objects.equals(realAcc.getDepartment(), tmpAccount.getDepartment())) {
                return true;
            }
        }

        return !Objects.equals(realAcc.getGender(), tmpAccount.getGender());
    }

    private void initTmpAcc() {
        if (tmpAccount == null) {
            tmpAccount = App.getApp().getAccount().copy();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isDataChange()) {
            new ConfirmDialog(requireContext(),
                    ConfirmDialog.LAYOUT_HOLD_USER,
                    R.drawable.ic_discard,
                    "Do you want to discard change?",
                    new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm(BaseOverlayDialog overlayDialog) {
                            overlayDialog.dismiss();
                            popLastFragment();
                        }

                        @Override
                        public void onCancel(BaseOverlayDialog overlayDialog) {
                            overlayDialog.dismiss();
                        }
                    }).create(null);
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess() {
        tmpAccount = null;
        showToast("Saved!", ToastUtils.SUCCESS, true);
    }

    @Override
    public void onError(Throwable throwable) {
        showToast("Save error!", ToastUtils.ERROR, true);
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
}
