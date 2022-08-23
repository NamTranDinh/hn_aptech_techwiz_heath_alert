package com.csupporter.techwiz.presentation.view.fragment.main.nav;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.csupporter.techwiz.App;
import com.csupporter.techwiz.R;
import com.csupporter.techwiz.data.firebase_source.FirebaseUtils;
import com.csupporter.techwiz.domain.model.Prescription;
import com.csupporter.techwiz.domain.model.PrescriptionDetail;
import com.csupporter.techwiz.presentation.presenter.MainViewCallBack;
import com.csupporter.techwiz.presentation.presenter.authentication.AddNewPrescriptionPresenter;
import com.csupporter.techwiz.presentation.view.dialog.CropImageDialog;
import com.csupporter.techwiz.presentation.view.dialog.LoadingDialog;
import com.csupporter.techwiz.presentation.view.fragment.main.nav.BaseNavFragment;
import com.csupporter.techwiz.utils.PermissionUtils;
import com.csupporter.techwiz.utils.PickPictureResult;
import com.isseiaoki.simplecropview.CropImageView;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.toast.ToastUtils;

import java.io.ByteArrayOutputStream;


public class AddNewPrescriptionFragment extends BaseNavFragment implements View.OnClickListener,
        MainViewCallBack.EditPrescriptionDetail {

    private EditText edtTimePerDay, edtTimePerWeek, edtQuantity, edtMedicineName;

    private ImageView imgMedicine;
    private AppCompatButton btnChooseImage;
    private AppCompatButton btnCreateNewPrescription;
    private AppCompatButton btnEdit;
    private AddNewPrescriptionPresenter addNewPrescriptionPresenter;

    private LoadingDialog dialog;
    private Prescription prescription;
    private PrescriptionDetail prescriptionDetail = null;

    public static AddNewPrescriptionFragment newInstance(Prescription prescription) {

        Bundle args = new Bundle();
        args.putSerializable("pres", prescription);
        AddNewPrescriptionFragment fragment = new AddNewPrescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddNewPrescriptionFragment transferPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
        Bundle args = new Bundle();
        args.putSerializable("prescription", prescriptionDetail);
        AddNewPrescriptionFragment fragment = new AddNewPrescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            prescription = (Prescription) bundle.getSerializable("pres");
            prescriptionDetail = (PrescriptionDetail) bundle.getSerializable("prescription");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addNewPrescriptionPresenter = new AddNewPrescriptionPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_prescription, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.tb_toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        edtTimePerWeek = view.findViewById(R.id.edt_time_per_a_week);
        edtTimePerDay = view.findViewById(R.id.edt_time_per_a_day);
        edtQuantity = view.findViewById(R.id.edt_quantity);
        edtMedicineName = view.findViewById(R.id.edt_medicine_name);
        imgMedicine = view.findViewById(R.id.img_medicine);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(this);

        btnEdit = view.findViewById(R.id.btn_edit_new_prescription);
        btnEdit.setOnClickListener(this);

        btnCreateNewPrescription = view.findViewById(R.id.btn_create_new_prescription);
        btnCreateNewPrescription.setOnClickListener(this);

        if (prescriptionDetail != null) {
            edtMedicineName.setText(prescriptionDetail.getMedicineName());
            edtTimePerDay.setText(String.valueOf(prescriptionDetail.getTimePerADay()));
            edtTimePerWeek.setText(String.valueOf(prescriptionDetail.getTimePerWeek()));
            edtQuantity.setText(String.valueOf(prescriptionDetail.getQuantity()));

            btnEdit.setVisibility(View.VISIBLE);
            btnCreateNewPrescription.setVisibility(View.GONE);
            Glide.with(this).load(prescriptionDetail.getImageUrl())
                    .placeholder(R.drawable.image_place_holder)
                    .error(R.drawable.image_place_holder)
                    .into(imgMedicine);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChooseImage:
                PermissionUtils.requestReadStoragePermission(this, (allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        mPickPictureResult.launch(null);
                    }
                });
                break;
            case R.id.btn_create_new_prescription:
                if (stream == null) {
                    showToast("Please select image", ToastUtils.WARNING);
                    return;
                }
                PrescriptionDetail prescriptionDetail = getPrescriptionDetail();

                addNewPrescriptionPresenter.createPrescriptionDetail(prescriptionDetail, stream.toByteArray(), new MainViewCallBack.CreatePrescriptionDetailCallBack() {

                    @Override
                    public void createPrescriptionDetailSuccess() {
                        popLastFragment();
                        ToastUtils.makeSuccessToast(getActivity(), Toast.LENGTH_SHORT, "success", true).show();
                    }

                    @Override
                    public void createPrescriptionFail() {
                        showToast("Add False!", ToastUtils.ERROR);
                    }
                });
                break;
        }
    }

    @Override
    public void editPrescriptionDetailSuccess(PrescriptionDetail prescriptionDetail) {

    }

    @Override
    public void editPrescriptionDetailFail() {

    }

    private PrescriptionDetail getPrescriptionDetail() {
        int timePerDay = Integer.parseInt(edtTimePerDay.getText().toString().trim());
        int timePerWeek = Integer.parseInt(edtTimePerWeek.getText().toString().trim());
        int quantity = Integer.parseInt(edtQuantity.getText().toString().trim());
        String nameMedicine = edtMedicineName.getText().toString().trim();

        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setId(FirebaseUtils.uniqueId());
        if (prescription != null) {
            prescriptionDetail.setPrescriptionId(prescription.getId());
        }
        prescriptionDetail.setMedicineName(nameMedicine);
        prescriptionDetail.setQuantity(quantity);
        prescriptionDetail.setTimePerADay(timePerDay);
        prescriptionDetail.setTimePerWeek(timePerWeek);
        return prescriptionDetail;
    }

    private ByteArrayOutputStream stream;
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
                                stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                imgMedicine.post(() -> {
                                    hideLoading();
                                    imgMedicine.setImageBitmap(bitmap);
                                });
                            }
                        }.start();
                    }
                }).create(null);
            });

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