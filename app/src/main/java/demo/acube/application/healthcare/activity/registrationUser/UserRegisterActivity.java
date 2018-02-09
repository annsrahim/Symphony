package demo.acube.application.healthcare.activity.registrationUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.TimeZone;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.avatarUpload.AvatarUploadActivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class UserRegisterActivity extends AppCompatActivity {

    EditText tvEmail,tvPassword,tvConfirmPassword;
    Button btnCreateEmail;
    TextView tvNotYou;
    APIInterface apiInterface;
    GlobalApplication globalApplication;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        tvEmail = (EditText ) findViewById(R.id.tvEmail);
        tvPassword = (EditText)findViewById(R.id.tvPassword);
        tvConfirmPassword = (EditText)findViewById(R.id.tvConfirmPassword);
        btnCreateEmail = (Button) findViewById(R.id.btn_create_login);
        tvNotYou = (TextView)findViewById(R.id.tvNotYou);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        btnCreateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidationSuccess())
                {
                    doUserRegistration();
                }
            }
        });
    }

    private void doUserRegistration() {
        progressDialog = LoadingDialog.show(this,"Loading");
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        String userEmail = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        String userId = String.valueOf(globalApplication.getUserId());
        TimeZone tz = TimeZone.getDefault();
        Call<UserDetailsBean> userDetailsCall = apiInterface.doUserRegistration(userEmail,password,userId,tz.getID());
        userDetailsCall.enqueue(new Callback<UserDetailsBean>() {
            @Override
            public void onResponse(Call<UserDetailsBean> call, Response<UserDetailsBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    globalApplication.setUserDetailsBean(response.body());
                    goToAvatarUpload();
                }
                else
                {
                    Log.d("TAG",response.toString());
                    Utils.showToast(UserRegisterActivity.this,response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<UserDetailsBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(UserRegisterActivity.this,t.getLocalizedMessage());
            }
        });
    }

    private void goToAvatarUpload()
    {
        Intent intent = new Intent(UserRegisterActivity.this, AvatarUploadActivity.class);
        startActivity(intent);
    }


    private boolean isValidationSuccess()
    {


        if(!Utils.isValidEmail(tvEmail.getText()))
        {
            tvEmail.setText("");
            tvEmail.setHint("Please Enter Valid Email");
            tvEmail.setHintTextColor(getResources().getColor(R.color.dark_red));
            return false;
        }
        else
        {
            String password = tvPassword.getText().toString();
            String confirmPassword = tvConfirmPassword.getText().toString();
            if(password.equals("") || confirmPassword.equals(""))
            {
                tvPassword.setText("");
                tvConfirmPassword.setText("");
                Utils.showToast(this,"Passwords Cannot be left blank");
                return false;
            }
            else if(!password.equals(confirmPassword))
            {
                tvPassword.setText("");
                tvConfirmPassword.setText("");
                Utils.showToast(this,"Password does not match");
                return false;
            }
            else if(password.length()<6)
            {
                Utils.showToast(this,"Password should be minimum 6 characters");
               return false;
            }
            else
            {
                return true;
            }

        }



    }
}
