package demo.acube.application.healthcare.activity.registrationUser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.dashboard.PatientHomeActivity;
import demo.acube.application.healthcare.helper.utilities.SharedPreferenceUtils;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.ValidationUtils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.initialLogin.InitialLoginBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class LoginAcitivity extends AppCompatActivity {

    EditText tvEmail,tvPassword;
    String userEmail;
    String userPassword;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    GlobalApplication globalApplication;
    public static boolean isActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);
        ////Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.disableKeyboardPopup(this);
        tvEmail = (EditText)findViewById(R.id.tvEmail);
        tvPassword = (EditText)findViewById(R.id.tvPassword);
        String lastLoggedUser = SharedPreferenceUtils.getStringDataInShare(this,Config.LAST_LOGGED_USER);
        tvEmail.setText(lastLoggedUser);
        tvPassword.setText("123456");

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
    }
    public void doCheckLogin(View view)
    {

        userEmail = tvEmail.getText().toString();
        userPassword = tvPassword.getText().toString();
        if(isValidationSuccess())
        {
            doLoginAuthentication();
        }

    }
    public void doForgotPassword(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.forgot_password)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetPassword();
                    }
                })
                .setNegativeButton(R.string.cancel,null)
                .show();
    }
    public void resetPassword()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText input = new EditText(LoginAcitivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitEmailAddress(input.getText().toString());
            }
        });
        alertDialog.setNegativeButton(R.string.cancel,null);
        alertDialog.show();
    }
    public void submitEmailAddress(String mEmail)
    {
            if(ValidationUtils.isValidEmail(mEmail))
            {
                progressDialog = LoadingDialog.show(this,"Loading");
                Call<Success> resetPassword = apiInterface.doResetPassword(mEmail);
                resetPassword.enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                       progressDialog.dismiss();
                        if(response.code()==200)
                        {
                            emailSendSuccess();
                        }
                        else
                        {
                            emailSendFailed();
                        }

                    }

                    @Override
                    public void onFailure(Call<Success> call, Throwable throwable) {
                        progressDialog.dismiss();
                        emailSendFailed();
                    }
                });
            }
            else
            {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.invalid_email_address)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(R.string.invalid_email_id_msg)
                        .setPositiveButton(android.R.string.ok,null)
                        .show();
            }

    }
    public void emailSendSuccess()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.password_reset_email_sent)
                .setMessage("An email has been sent to your account where you can reset your password!")
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }
    public void emailSendFailed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Email")
                .setMessage("The email address is invalid")
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }
    public void doUnableToLoginProcess(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.lbl_unable_to_login))
                .setMessage(getResources().getString(R.string.lbl_unable_to_login_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, null).show();

    }



    private void doLoginAuthentication()
    {
        progressDialog = LoadingDialog.show(this,"Loading");
        Call<InitialLoginBean> initialLoginCall = apiInterface.doInitialAuthentication(userEmail,userPassword);
        initialLoginCall.enqueue(new Callback<InitialLoginBean>() {
            @Override
            public void onResponse(Call<InitialLoginBean> call, Response<InitialLoginBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    InitialLoginBean codeBean = response.body();
                    globalApplication.setUserId(codeBean.getData().getUserId());
                    globalApplication.setUserName(codeBean.getData().getName());
                    globalApplication.setUserEmail(codeBean.getData().getOpusEmail());
                    globalApplication.setUserDob(codeBean.getData().getDob());
                    globalApplication.setUserAge(codeBean.getData().getAge());
                    globalApplication.setUserType(codeBean.getData().getUserType());
                    globalApplication.setUserLoggedIn(true);
                    if(globalApplication.getUserType().equals(ValueUtils.doctor))
                    {

//                        globalApplication.setPrimaryDoctorName(codeBean.g);
                    }
                    if(globalApplication.getIncomingCallFrom()==0)
                        gotoDashboard();
                    else
                    {
                            Utils.showIncomingCallView(LoginAcitivity.this,globalApplication.getIncomingCallFrom());

                    }

                }
                else
                {
                    Utils.showAlertDialog(LoginAcitivity.this,"Login Failed Invalid Credential");
                }
            }

            @Override
            public void onFailure(Call<InitialLoginBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showAlertDialog(LoginAcitivity.this,t.getLocalizedMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        if(globalApplication.getIsUserLoggedIn())
        {
            gotoDashboard();
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        isActive = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isActive = false;
        super.onStop();
    }

    @Override
    public void onBackPressed() {

    }

    private void gotoDashboard()
    {
        SharedPreferenceUtils.setStringDataInShare(this,Config.LAST_LOGGED_USER,tvEmail.getText().toString());
        //get Permissions;

        /*if(globalApplication.get
        UserType().equals("doctor"))
        {
            Intent intent = new Intent(this, DoctorHomeActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, PatientHomeActivity.class);
            startActivity(intent);
        }*/

        Intent intent = (globalApplication.getUserType().equals("doctor")) ? new Intent(this, DoctorHomeActivity.class) : new Intent(this, PatientHomeActivity.class);
        startActivity(intent);
    }

    private boolean isValidationSuccess() {
        if(!Utils.isValidEmail(userEmail))
        {
            Utils.showToast(this,"Please enter valid email");
            return false;
        }
        else
        {
            if(userPassword.equals(""))
            {
                Utils.showToast(this,"Password field should not be null");
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
