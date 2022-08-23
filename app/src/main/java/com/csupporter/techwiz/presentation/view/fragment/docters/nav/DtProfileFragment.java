package com.csupporter.techwiz.presentation.view.fragment.docters.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.domain.model.Account;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.NavUserInfoPresenter;
import com.csupporter.techwiz.presentation.presenter.doctor.DoctorProfilePresenter;
import com.csupporter.techwiz.presentation.view.activities.AuthenticateActivity;
import com.csupporter.techwiz.presentation.view.dialog.ConfirmDialog;
import com.csupporter.techwiz.presentation.view.dialog.CropImageDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.presentation.view.fragment.profile.ProfileFragment;
import com.csupporter.techwiz.utils.PermissionUtils;
import com.csupporter.techwiz.utils.PickPictureResult;
import com.isseiaoki.simplecropview.CropImageView;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class DtProfileFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView tvName, tvEmail;
    private RelativeLayout itemProfile, itemLogout, itemPrescription;
    private CircleImageView crlOpenGallery, avatarPersonal;
    private LoadingDialog dialog;

    private DoctorProfilePresenter doctorProfilePresenter;


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
                                        avatarPersonal.post(() -> avatarPersonal.setImageBitmap(bitmap));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dt_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        eventClick();
        doctorProfilePresenter = new DoctorProfilePresenter(this);
        navUserInfoPresenter = new NavUserInfoPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Account account = App.getApp().getAccount();
        tvName.setText(account.getFullName());
        tvEmail.setText(account.getEmail());
        Glide.with(this).load(App.getApp().getAccount().getAvatar())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(avatarPersonal);
    }

    private void eventClick() {
        itemProfile.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        crlOpenGallery.setOnClickListener(this);
        itemPrescription.setOnClickListener(this);
    }

    private void initView(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);

        itemProfile = view.findViewById(R.id.item_profile);
        itemLogout = view.findViewById(R.id.item_logout);
        itemPrescription = view.findViewById(R.id.prescription_layout);

        crlOpenGallery = view.findViewById(R.id.crl_open_gallery);
        avatarPersonal = view.findViewById(R.id.avatar_personal);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_profile:
                replaceFragment(new ProfileFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
            case R.id.item_logout:
                if (getActivity() != null) {
                    logOut();
                    break;
                }
            case R.id.crl_open_gallery:
                PermissionUtils.requestReadStoragePermission(this, (allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        mPickPictureResult.launch(null);
                    }
                });
                break;
            case R.id.prescription_layout:
                replaceFragment(new MyUsersFragment(), true, BaseActivity.Anim.RIGHT_IN_LEFT_OUT);
                break;
        }
    }

    private void logOut() {
        if (getActivity() != null) {
            new ConfirmDialog(getActivity(),
                    ConfirmDialog.LAYOUT_HOLD_USER,
                    R.drawable.ic_logout, getString(R.string.dialog_confirm_logout_msg),
                    new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm(BaseOverlayDialog overlayDialog) {
                            doctorProfilePresenter.logOut();
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