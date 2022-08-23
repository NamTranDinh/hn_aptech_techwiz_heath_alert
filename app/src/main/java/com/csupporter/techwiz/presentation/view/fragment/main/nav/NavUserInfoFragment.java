package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.NavUserInfoPresenter;
import com.csupporter.techwiz.presentation.view.activities.AuthenticateActivity;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.CropImageDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.presentation.view.fragment.main.prescription.AllPrescriptionFragment;
import com.csupporter.techwiz.presentation.view.fragment.profile.ProfileFragment;
import com.csupporter.techwiz.utils.PermissionUtils;
import com.csupporter.techwiz.utils.PickPictureResult;
import com.isseiaoki.simplecropview.CropImageView;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.io.ByteArrayOutputStream;


public class NavUserInfoFragment extends BaseNavFragment implements View.OnClickListener, MainViewCallBack.NavUserInfoCallBack {

    private View view;
    private TextView tvName, tvEmail;
    private RelativeLayout itemLogout, itemHealthTrack, itemProfile, itemPrescription;
    private ImageView btnOpenGallery;
    private ImageView imgAvatar;
    private LoadingDialog dialog;

    private NavUserInfoPresenter navUserInfoPresenter;

    private final ActivityResultLauncher<Void> mPickPictureResult =
            registerForActivityResult(new PickPictureResult(), uri -> {
                if (isContextNull() || uri == null) {
                    return;
                }
                new CropImageDialog(requireContext(), uri, CropImageView.CropMode.SQUARE, new CropImageDialog.CropImageListener() {

                    @Override
                    public void onCropSuccess(BaseOverlayDialog dialog, Bitmap bitmap) {
                        dialog.dismiss();
                        showLoading();
                        new Thread() {
                            @Override
                            public void run() {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                navUserInfoPresenter.uploadAvatar(App.getApp().getAccount(), stream.toByteArray(), new MainViewCallBack.UploadAvatarCallback() {
                                    @Override
                                    public void onSuccess(String url) {
                                        hideLoading();
                                        imgAvatar.post(() -> imgAvatar.setImageBitmap(bitmap));
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nav_user_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navUserInfoPresenter = new NavUserInfoPresenter(this);
        initView(view);
        eventClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.getApp().getAccount() != null) {
            Account account = App.getApp().getAccount();
            tvName.setText(account.getFullName());
            tvEmail.setText(account.getEmail());
            Glide.with(this).load(account.getAvatar())
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .into(imgAvatar);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.item_logout:
                if (getActivity() != null) {
                    new ConfirmDialog(getActivity(),
                            ConfirmDialog.LAYOUT_HOLD_USER,
                            R.drawable.ic_logout, getString(R.string.dialog_confirm_logout_msg),
                            new ConfirmDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm(BaseOverlayDialog overlayDialog) {
                                    navUserInfoPresenter.logOut();
                                    Intent intent = new Intent(getContext(), AuthenticateActivity.class);
                                    startActivity(intent);
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                }

                                @Override
                                public void onCancel(BaseOverlayDialog overlayDialog) {
                                    overlayDialog.dismiss();
                                }
                            }).create(null);

                }
                break;
            case R.id.item_health_track:
                replaceFragment(new NavHealthyTrackingFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
            case R.id.crl_open_gallery:
                PermissionUtils.requestReadStoragePermission(this, (allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        mPickPictureResult.launch(null);
                    }
                });
                break;
            case R.id.item_profile:
                Fragment fragment = new ProfileFragment();
                replaceFragment(fragment, true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
            case R.id.item_view_prescription:
                replaceFragment(new AllPrescriptionFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
        }
    }

    private void eventClick() {
        itemLogout.setOnClickListener(this);
        itemHealthTrack.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
        itemPrescription.setOnClickListener(this);
    }

    private void initView(@NonNull View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        itemLogout = view.findViewById(R.id.item_logout);
        itemHealthTrack = view.findViewById(R.id.item_health_track);
        itemProfile = view.findViewById(R.id.item_profile);
        btnOpenGallery = view.findViewById(R.id.crl_open_gallery);
        imgAvatar = view.findViewById(R.id.avatar_personal);
        itemPrescription = view.findViewById(R.id.item_view_prescription);
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