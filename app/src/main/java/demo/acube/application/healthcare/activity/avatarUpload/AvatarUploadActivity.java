package demo.acube.application.healthcare.activity.avatarUpload;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.acube.application.healthcare.service.CustomExceptionHandler;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.dashboard.PatientHomeActivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.model.avatarUpload.AvatarUploadBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class AvatarUploadActivity extends AppCompatActivity {

    CircleImageView profileImage;
    ProgressDialog progressDialog;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;
    Button btnAddPicture;
    APIInterface apiInterface;
    GlobalApplication globalApplication;
    TextView tvChangePic;
    /**
     * Image path to send
     */
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_upload);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
/**
 * Permission Checker Initialized
 */
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        checker = new PermissionsChecker(this);
        btnAddPicture = (Button)findViewById(R.id.btn_add_pic);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        profileImage = (CircleImageView)findViewById(R.id.profile_image);
        tvChangePic = (TextView)findViewById(R.id.tv_change_pic);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(v);
            }
        });
    }
    private void uploadImage()
    {
        progressDialog = LoadingDialog.show(this,"Uploading Image...");
        //File creating from selected URL
        File file = new File(imagePath);

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
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    globalApplication.setUserAvatar(response.body().getData().getAvatar());
                    gotoDashboard();
                }
                else
                {
                    Utils.showToast(AvatarUploadActivity.this,"Unable to Upload Image Please try again");
                }
            }

            @Override
            public void onFailure(Call<AvatarUploadBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(AvatarUploadActivity.this,t.getLocalizedMessage());
            }
        });


    }
    /**
     * Showing Image Picker
     */
    public void showImagePopup(View view) {


        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
            startActivityForResult(chooserIntent, 1010);
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }
    private void gotoDashboard()
    {
        if(globalApplication.getUserType().equals("doctor"))
        {
            Intent intent = new Intent(this, DoctorHomeActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, PatientHomeActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010)
        {
            if (data == null) {
                Utils.showToast(this,getResources().getString(R.string.string_unable_to_pick_image));
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                Picasso.with(this).load(new File(imagePath))
                        .into(profileImage);
                tvChangePic.setVisibility(View.VISIBLE);

                cursor.close();


            } else {
                Utils.showToast(this,getResources().getString(R.string.string_unable_to_load_image));

            }
        }
    }
}
