package demo.acube.application.healthcare.activity.patient.acitivity.videoCallNow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorList.DoctorsListActiviy;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.CallAppointmentActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.callRequestBean.CallRequestDatasBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class VideoCallNowAcitivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    Toolbar toolbar;
    private TextView tvSelectCallerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_now_acitivity);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        initToolbar();
        initUI();

    }

    private void initUI() {
        tvSelectCallerName = (TextView)findViewById(R.id.tvSelectedCallerName);
        if(globalApplication.getPatSelectedCallerId()==0)
        {
            globalApplication.setPatSelectedCallerId(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId());
            globalApplication.setPatSelectedCallerName(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getName());

        }
        updateCallerId();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.virtual_visit);
        setSupportActionBar(toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_blue, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void doVideoCallStream(View view)
    {
        Utils.getAudioVideoPermission(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if(permissionCheck==0)
            getCallRequestDatas();

    }
    public void goToVideoCallStream()
    {
        Intent intent = new Intent(this, VideoCallStreamAcitivity .class);
        startActivity(intent);
    }
    private void getCallRequestDatas()
    {
        progressDialog = LoadingDialog.show(this,"Loading");
        String userId = String.valueOf(globalApplication.getUserId());
        String selectedCallId = String.valueOf(globalApplication.getPatSelectedCallerId());
        Call<CallRequestDatasBean> callRequestCall = apiInterface.doGetCallRequestDatas(userId,selectedCallId,"visit");
        callRequestCall.enqueue(new Callback<CallRequestDatasBean>() {
            @Override
            public void onResponse(Call<CallRequestDatasBean> call, Response<CallRequestDatasBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    globalApplication.setTokenNumber(response.body().getData().getTokbox().getToken());
                    globalApplication.setSessionId(response.body().getData().getTokbox().getSessionId());
                    globalApplication.setOutGoingCallId(response.body().getData().getVisitId());
                    goToVideoCallStream();
                }
                else
                {
                    Utils.showToast(VideoCallNowAcitivity.this,"Unable to process request");
                }
            }

            @Override
            public void onFailure(Call<CallRequestDatasBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(VideoCallNowAcitivity.this,"Unable to process request "+t.getLocalizedMessage());
            }
        });
    }

    public void doShowDoctorsList(View view) {
        Intent intent = new Intent(this, DoctorsListActiviy.class);
        intent.putExtra(ValueUtils.isFrom,ValueUtils.VIRTUAL_VISIT_ACTIVITY);
        startActivity(intent);
    }
    private void updateCallerId()
    {
        tvSelectCallerName.setText(globalApplication.getPatSelectedCallerName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCallerId();
    }

    public void scheduleVideoCall(View view) {
        goToVirtualVisitRemainder();
    }
    public void goToVirtualVisitRemainder() {
        Intent intent = new Intent(this,CallAppointmentActivity.class);
        intent.putExtra(ValueUtils.AppointmentType,3);
        String isFrom = "";
        Intent receiveIntent = getIntent();
        if(receiveIntent!=null)
            isFrom = receiveIntent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null)
            intent.putExtra(ValueUtils.isFrom,ValueUtils.MySchedules);
        startActivity(intent);
    }
}