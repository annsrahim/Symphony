package demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.model.avatarUpload.AvatarUploadBean;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.editOfficeHours.EditOfficeHoursActivity;
import demo.acube.application.healthcare.activity.doctor.models.doctorDetails.Data;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;
import demo.acube.application.healthcare.model.accessToken.Success;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorEditFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Context mContext;
    private TextView tvCancel,tvSave;
    @BindView(R.id.tv_doctor_first_name)
    TextView tvDoctorFirstName;
    @BindView(R.id.tv_doctor_last_name)
    TextView tvDoctorLastName;
    @BindView(R.id.tv_doctor_speciality_name)
    TextView tvDoctorSpecialityName;

    @BindView(R.id.ed_street_1)
    EditText edStreet1;
    @BindView(R.id.ed_country)
    EditText edCountry;
    @BindView(R.id.ed_country_code)
    EditText edCountryCode;
    @BindView(R.id.ed_postal_code)
    EditText edPostalCode;

    @BindView(R.id.ed_office_number)
    EditText edOfficeNumber;
    @BindView(R.id.ed_website)
    EditText edWebsite;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.rlSetOfficeHoursContainer)
    RelativeLayout rlSetOfficeHoursContainer;

    private GlobalApplication globalApplication;
    private APIInterface apiInterface;

    Data doctorDatas;
    private boolean isImageUploaded = false;

    private String userChoosenTask = "";
    private Bitmap newProfileImage = null;
    ProgressDialog progressDialog;

    public DoctorEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_doctor_edit, container, false);
        globalApplication = (GlobalApplication)getActivity().getApplicationContext();
        ButterKnife.bind(this,rootView);
        doctorDatas = ((MyProfileActivity)getActivity()).doctorDetails.getData();
        apiInterface = APIClient.getClient(mContext).create(APIInterface.class);
        initUI();
        displayDatas();
        return rootView;
    }

    private void displayDatas() {

        String doctorName = doctorDatas.getName();
        String[] splited = doctorName.split("\\s+");
        tvDoctorFirstName.setText(splited[0]);
        tvDoctorLastName.setText(splited[1]);

        tvDoctorSpecialityName.setText(doctorDatas.getSpecialty().getName());
        edStreet1.setText(doctorDatas.getAddress().getStreet1());
        edCountry.setText(doctorDatas.getAddress().getCity());
        edCountryCode.setText(doctorDatas.getAddress().getState());
        edPostalCode.setText(doctorDatas.getAddress().getZip());

        edOfficeNumber.setText(doctorDatas.getPhone().getOffice());
        edWebsite.setText(doctorDatas.getWebsite());



        Picasso.with(getActivity()).load(doctorDatas.getAvatar()).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(profileImage);


    }

    private void initUI()
    {
        tvCancel = (TextView)rootView.findViewById(R.id.toolbar_tv_cancel);
        tvSave = (TextView)rootView.findViewById(R.id.toolbar_tv_save);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        rlSetOfficeHoursContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOfficeHours();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void setOfficeHours() {
            startActivity(new Intent(getActivity(), EditOfficeHoursActivity.class));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utils.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Config.CAMERA_REQUEST);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),Config.GALLERY_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Config.GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == Config.CAMERA_REQUEST)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImageToView(bm);
    }
    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        addImageToView(thumbnail);

    }
    private void addImageToView(Bitmap bm)
    {
        isImageUploaded = true;
        newProfileImage = bm;
        profileImage.setImageBitmap(bm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.toolbar_tv_save:
                goToSave();
                break;
            case R.id.toolbar_tv_cancel:
                goToMyProfile();
                break;
        }
    }

    private void goToSave() {
        progressDialog = LoadingDialog.show(mContext,"Fetching Datas");
        if(isContactInfoChanged())
        {

            updateContactInfo();
        }
        else if(isAddressChanged())
        {
            updateAddress();
        }
        else if(isImageUploaded)
            uploadProfileImage();
        else
            goToMyProfile();





    }

    private void uploadProfileImage() {

        File file = savebitmap("temp");

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        String userID = String.valueOf(globalApplication.getUserId());
        RequestBody userIDBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, userID);
        Call<AvatarUploadBean> avatarServiceCall = apiInterface.doAvatarUpload(body,userIDBody);
        avatarServiceCall.enqueue(new Callback<AvatarUploadBean>() {
            @Override
            public void onResponse(Call<AvatarUploadBean> call, Response<AvatarUploadBean> response) {

                if(response.code()==200) {
                        goToMyProfile();
                }
                else
                    Utils.showToast(getActivity(),"Unable to Upload Image Please try again");

            }

            @Override
            public void onFailure(Call<AvatarUploadBean> call, Throwable t) {

                Utils.showToast(getActivity(),"Unable to Upload Image Please try again "+t.getLocalizedMessage());
            }
        });
    }
    private File savebitmap(String filename) {
        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, filename + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);

            newProfileImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;

    }
    private void updateAddress() {
        String street1 = edStreet1.getText().toString();
        String country = edCountry.getText().toString();
        String countryCode = edCountryCode.getText().toString();
        String zip =  edPostalCode.getText().toString();
        Call<Success> updateAddressCall = apiInterface.updateDoctorAddress(doctorDatas.getUserId(),
                                                                            street1,country,countryCode,country,zip);
        updateAddressCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.code()==200)
                {
                    if(isImageUploaded)
                        uploadProfileImage();
                    else
                        goToMyProfile();
                }
                else
                    Utils.showToast(getActivity(),"Update Failed");
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Utils.showToast(getActivity(),"Update Failed "+t.getLocalizedMessage());
            }
        });
    }

    private void updateContactInfo() {
        String officeNum = edOfficeNumber.getText().toString();
        String websiteUrl = edWebsite.getText().toString();
        Call<Success> updateContactInfoCall = apiInterface.updateDoctorContactInfo(doctorDatas.getUserId(),
                                                                                    officeNum,
                                                                                    websiteUrl);
        updateContactInfoCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.code()==200)
                {
                    if(isAddressChanged())
                    {
                        updateAddress();
                    }
                    else
                    {
                        if(isImageUploaded)
                            uploadProfileImage();
                        else
                            goToMyProfile();
                    }
                }
                else
                    Utils.showToast(getActivity(),"Update Failed");
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                    Utils.showToast(getActivity(),"Update Failed "+t.getLocalizedMessage());
            }
        });
    }

    private boolean isContactInfoChanged() {
        String officeNum = edOfficeNumber.getText().toString();
        String websiteUrl = edWebsite.getText().toString();
        return !(officeNum.equals(doctorDatas.getPhone().getOffice()) || websiteUrl.equals(doctorDatas.getWebsite()));
    }
    private boolean isAddressChanged() {
        String street1 = edStreet1.getText().toString();
        String country = edCountry.getText().toString();
        String countryCode = edCountryCode.getText().toString();
        String zip =  edPostalCode.getText().toString();

        String prevStreet1 = doctorDatas.getAddress().getStreet1();
        String prevCountry = doctorDatas.getAddress().getCity();
        String prevCountryCode = doctorDatas.getAddress().getState();
        String prevZip = doctorDatas.getAddress().getZip();

        if(!street1.equals(prevStreet1) || !country.equals(prevCountry) || !prevCountryCode.equals(countryCode) || !prevZip.equals(zip))
            return true;
        else
            return false;
    }

    private void goToMyProfile() {
        if(progressDialog!=null && progressDialog.isShowing())
             progressDialog.dismiss();
        ((MyProfileActivity)mContext).goToMyProfile();
    }


}
