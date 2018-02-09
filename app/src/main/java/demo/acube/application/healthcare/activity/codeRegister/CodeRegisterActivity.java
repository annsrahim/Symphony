package demo.acube.application.healthcare.activity.codeRegister;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.registrationUser.AcknowledgementActivity;
import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.model.codeAuthentication.CodeAuthenticatedBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CodeRegisterActivity extends AppCompatActivity {

    private EditText tvCode;
    private TextView tvDateOfBirth;
    Calendar myCalendar;
    Button btnDone;
    private ImageView ivQuestion;
    Date currDate;
    DatePickerDialog.OnDateSetListener date;
    GlobalApplication globalApplication;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    String mStrCode = "";
    String mStrDob = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_register);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)getApplicationContext();

        btnDone = (Button)findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCodeAuthentication();
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 updateLabel(year,month,dayOfMonth);
            }
        };
        tvCode = (EditText) findViewById(R.id.tvCode);
        //tvCode.setText("4m15wR4j");
        tvDateOfBirth = (TextView)findViewById(R.id.tvDateOfBirth);
        tvDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        ivQuestion = (ImageView)findViewById(R.id.iv_question);
        ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink();
            }
        });

    }
    public Boolean isValidationSucess()
    {
        return !(mStrCode.equals("") || mStrDob.equals(""));
    }
    private void doCodeAuthentication()
    {
        mStrCode = tvCode.getText().toString();
        if(isValidationSucess())
        {
            progressDialog = LoadingDialog.show(this,"Loading");
            Call<CodeAuthenticatedBean> codeAuthenticationCall = apiInterface.doCheckCodeAuthenticaton(mStrCode,mStrDob);
            codeAuthenticationCall.enqueue(new Callback<CodeAuthenticatedBean>() {
                @Override
                public void onResponse(Call<CodeAuthenticatedBean> call, Response<CodeAuthenticatedBean> response)
                {


                    if(response.code()==200)
                    {
                        progressDialog.dismiss();
                        CodeAuthenticatedBean codeBean = response.body();
                        globalApplication.setUserId(codeBean.getData().getUserId());
                        globalApplication.setUserName(codeBean.getData().getName());
                        globalApplication.setUserEmail(codeBean.getData().getOpusEmail());
                        globalApplication.setUserDob(codeBean.getData().getDob());
                        globalApplication.setUserAge(codeBean.getData().getAge());
                        globalApplication.setUserType(codeBean.getData().getUserType());
                        globalApplication.setSignedUp(codeBean.getData().getSignedUp());
                        SharedPreferences.Editor editor = getSharedPreferences(Config.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(Config.IS_CODE_REGISTERED, true);
                        editor.commit();
                        confirmUser();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        loginFailed(response.message());
                    }

                }
                @Override
                public void onFailure(Call<CodeAuthenticatedBean> call, Throwable t)
                {
                    loginFailed(t.getLocalizedMessage());
                    Log.d("COdeRes",t.getLocalizedMessage()+"");
                }
            });
        }
        else
        {
            String textMessage = "";
            if(mStrCode.equals("") && mStrDob.equals(""))
                textMessage = "Please Enter the Code and Date of Birth";
            else if(mStrCode.equals(""))
                textMessage = "Please enter the code";
            else
                textMessage = "Please enter the Date of birth";

            new AlertDialog.Builder(this)
                    .setTitle("Alert!")
                    .setMessage(textMessage)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, null).show();
        }

    }

    private void loginFailed(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, null).show();
    }

    private void openLink()
    {
        String url = Config.urlQuestion;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void confirmUser()
    {
        progressDialog.dismiss();
        Resources res = getResources();
        String userName = globalApplication.getUserName();

        String alertMessage = String.format(res.getString(R.string.str_alert_confirm_message), userName);
        Log.d("DDDD",alertMessage);
        new AlertDialog.Builder(this)
                .setTitle(R.string.str_alert_confirmation)
                .setMessage(alertMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(globalApplication.getSignedUp())
                            goToLoginScreen();
                        else
                            goToRegisterScreen();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void goToRegisterScreen()
    {
        Intent intent = new Intent(CodeRegisterActivity.this, AcknowledgementActivity.class);
        startActivity(intent);
    }
    private void goToLoginScreen()
    {
        Intent intent = new Intent(this, LoginAcitivity.class);
        startActivity(intent);

    }

    private void updateLabel(int year, int month, int dayOfMonth)
    {
        myCalendar.set(Calendar.YEAR,year);
        myCalendar.set(Calendar.MONTH,month);
        myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tvDateOfBirth.setText("");
        String myLabelFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myLabelFormat, Locale.US);
        tvDateOfBirth.setText(sdf.format(myCalendar.getTime()));
        String myDataFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf2 = new SimpleDateFormat(myDataFormat, Locale.US);
        mStrDob = sdf2.format(myCalendar.getTime());

    }

    private void showDatePicker()
    {
        myCalendar = Calendar.getInstance();
         new DatePickerDialog(CodeRegisterActivity.this,date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public void onBackPressed()
    {

    }
}
