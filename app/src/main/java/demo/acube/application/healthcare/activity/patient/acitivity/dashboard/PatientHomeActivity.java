package demo.acube.application.healthcare.activity.patient.acitivity.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import demo.acube.application.healthcare.service.CustomExceptionHandler;
import io.reactivex.Observable;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.callNow.CallNowActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.ChatHomeActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation.DoctorsInformationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.mySchedule.MyScheduleActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.MyTeleHealthTeamActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.SelectAppointmentTypeActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.videoCallNow.VideoCallNowAcitivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.notificationBadge.NotificationBadgeBean;
import demo.acube.application.healthcare.model.userDetails.PrimaryDoctor;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class PatientHomeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivCall;
    private Timer timer;
    APIInterface apiInterface;
    GlobalApplication globalApplication;
    ProgressDialog progressDialog;
    ImageView ivProfileImageView;
    String mProfileImagePath = "";
    TextView tvNotificationCall,tvNotificationText,tvNotificationVisit,tvNotificationAppointment;
    private Disposable disposable;
    int textNotification = 0,callNotification=0,visitNotification=0,appointmentNotification=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.updateDeviceToken(this);
        initUi();
        getUserDetails();

    }

    private void getNotificationBadge() {

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

       disposable =  Observable.interval(3,TimeUnit.SECONDS)
                .flatMap(aLong -> apiInterface.doGetNotificationBadge(globalApplication.getUserId()))
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateNotificationDatas, this::handleErrors);


    }

    private void handleErrors(Throwable error) {
        Utils.showToast(this,"Unable to get data "+error.getLocalizedMessage());
    }

    private void updateNotificationDatas(NotificationBadgeBean notificationBadgeBean) {

       if(notificationBadgeBean.getSuccess().getCode().equals(getString(R.string.badge_found)))
       {
            callNotification = notificationBadgeBean.getData().getCall();
            visitNotification = notificationBadgeBean.getData().getVisit();
            textNotification = notificationBadgeBean.getData().getText();
            appointmentNotification = notificationBadgeBean.getData().getAppointment();
           if(callNotification>0)
           {
               tvNotificationCall.setText(String.valueOf(callNotification));
               tvNotificationCall.setVisibility(View.VISIBLE);
           }
           else
               tvNotificationCall.setVisibility(View.GONE);
           if(visitNotification>0)
           {
               tvNotificationVisit.setText(String.valueOf(visitNotification));
               tvNotificationVisit.setVisibility(View.VISIBLE);
           }
           else
               tvNotificationVisit.setVisibility(View.GONE);
           if(textNotification>0)
           {
               tvNotificationText.setText(String.valueOf(textNotification));
               tvNotificationText.setVisibility(View.VISIBLE);
           }
           else
               tvNotificationText.setVisibility(View.GONE);
           if(appointmentNotification>0)
           {
               tvNotificationAppointment.setText(String.valueOf(appointmentNotification));
               tvNotificationAppointment.setVisibility(View.VISIBLE);
           }
           else
               tvNotificationAppointment.setVisibility(View.GONE);

       }

    }


    private void getUserDetails()
    {
        progressDialog = LoadingDialog.show(this,"Loading");
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Call<UserDetailsBean> userDetailsCall = apiInterface.doGetUserDetails(globalApplication.getUserId());
        userDetailsCall.enqueue(new Callback<UserDetailsBean>() {
            @Override
            public void onResponse(Call<UserDetailsBean> call, Response<UserDetailsBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    globalApplication.setUserDetailsBean(response.body());
                    mProfileImagePath = response.body().getData().getAvatar();
                    displayDatas();
                }
                else
                    Utils.showToast(PatientHomeActivity.this,"Unable to fetch user info");
            }

            @Override
            public void onFailure(Call<UserDetailsBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PatientHomeActivity.this,"Unable to fetch user info "+t.getLocalizedMessage());
            }
        });
    }

    private void displayDatas() {
        Picasso.with(this).load(mProfileImagePath).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(ivProfileImageView);
    }

    private void initUi() {
        tvNotificationCall = (TextView) findViewById(R.id.tv_notification_call);
        tvNotificationVisit = (TextView) findViewById(R.id.tv_notification_visit);
        tvNotificationText = (TextView) findViewById(R.id.tv_notification_text);
        tvNotificationAppointment = (TextView) findViewById(R.id.tv_notification_appointment);
        ivProfileImageView = (ImageView)findViewById(R.id.profile_image);
        ivProfileImageView.setOnClickListener(this);
        ivCall = (ImageView)findViewById(R.id.iv_home_call);
        ivCall.setOnClickListener(this);
        globalApplication = (GlobalApplication)this.getApplicationContext();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.iv_home_call:
                    onCallClick();
                break;
            case R.id.profile_image:
                    showDoctorsInformation();
                break;
        }
    }

    private void showDoctorsInformation() {
        PrimaryDoctor primaryDoctor = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor();
        Intent intent = new Intent(this,DoctorsInformationActivity.class);
        intent.putExtra(ValueUtils.USER_ID,primaryDoctor.getUserId());
        intent.putExtra(ValueUtils.USER_NAME,primaryDoctor.getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY,primaryDoctor.getSpecialty().getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY_ID,primaryDoctor.getSpecialty().getSpecialtyId());
        intent.putExtra(ValueUtils.USER_ADDRESS_1,primaryDoctor.getAddress().getStreet1());
        String address2 = primaryDoctor.getAddress().getCity();
        address2 += " "+primaryDoctor.getAddress().getState();
        address2 += " "+primaryDoctor.getAddress().getZip();
        address2 += " "+primaryDoctor.getAddress().getCountry();
        intent.putExtra(ValueUtils.USER_ADDRESS_2,address2);
        intent.putExtra(ValueUtils.USER_OFFICE_NUMBER,primaryDoctor.getPhone().getOffice());
        intent.putExtra(ValueUtils.USER_WEBSITE_URL,primaryDoctor.getWebsite());
        intent.putExtra(ValueUtils.USER_REFERAL_STATUS,ValueUtils.DoctorApproved);
        startActivity(intent);
    }


    public void doVideoCall(View view)
    {
        Utils.updateNotificationState(this,globalApplication.getUserId(),2);
        Intent intent = new Intent(this, VideoCallNowAcitivity.class);
        intent.putExtra(getString(R.string.VisitNotification),visitNotification);
        startActivity(intent);
    }

    private void onCallClick()
    {
        Utils.updateNotificationState(this,globalApplication.getUserId(),1);
        Intent intent = new Intent(this, CallNowActivity.class);
        intent.putExtra(getString(R.string.CallNotification),callNotification);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        getNotificationBadge();
        super.onResume();
    }

    @Override
    protected void onPause() {

        if(disposable!=null)
        {
            disposable.dispose();
        }
        super.onPause();


    }

    @Override
    public void onBackPressed() {

    }

    public void doScheduleAppointment(View view) {
        Intent intent = new Intent(this, SelectAppointmentTypeActivity.class);
        startActivity(intent);
    }

    public void doChatNow(View view) {
        Utils.updateNotificationState(this,globalApplication.getUserId(),3);
        Intent intent = new Intent(this, ChatHomeActivity.class);
        intent.putExtra(getString(R.string.TextNotification),textNotification);
        startActivity(intent);
    }

    public void showMySchedule(View view) {
        Intent intent = new Intent(this, MyScheduleActivity.class);
        startActivity(intent);
    }

    public void showTeleHealthTeam(View view) {
        startActivity(new Intent(this, MyTeleHealthTeamActivity.class));
    }
}
