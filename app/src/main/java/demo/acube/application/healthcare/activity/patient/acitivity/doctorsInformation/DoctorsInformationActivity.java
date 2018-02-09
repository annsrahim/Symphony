package demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.SelectAppointmentTypeActivity;
import demo.acube.application.healthcare.activity.patient.adapters.AppointementHistoryAdapter;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.appointmentHistoryBean.AppointmentHistoryBean;
import demo.acube.application.healthcare.model.appointmentHistoryBean.Datum;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;
import demo.acube.application.healthcare.model.accessToken.Success;

public class DoctorsInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLocation,tvHistory;
    private LinearLayout llMapContainer,llHistoryContainer,llNumberWebsiteContainer,llContactFeatureContainer;
    private LinearLayout llLocationHistoryContainer,llReferralRequestContainer;
    private ImageView ivAvatar;
    private TextView tvDoctorName,tvDoctorSepciality,tvAddress1,tvAddress2,tvOfficeNumber,tvWebsiteUrls;
    private TextView tvMapAddress1,tvMapAddress2;
    private TextView  tvReferralRequest;
    private GlobalApplication globalApplication;
    private int userId, userReferalStatus,userSpecialityId;
    private String userName,userSpeciality,userAddress1,userAddress2,userOfficeNumber,userWebsite;
    ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private int chatId;
    private Boolean isHistoryAvailable = false;
    private List<Datum> appointmentHistoryList = new ArrayList<>();
    private ListView lvHistoryCalls;
    AppointementHistoryAdapter appointementHistoryAdapter;
    int totalPages = 0;
    int currentPage = 0;
    boolean flag_loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_information);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)getApplicationContext();

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initToolbar();
        initUI();
        getDatasFromIntent();
    }

    private void getDatasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            userId = extras.getInt(ValueUtils.USER_ID,0);
            userName = extras.getString(ValueUtils.USER_NAME);
            userSpeciality = extras.getString(ValueUtils.USER_SPECIALITY);
            userAddress1 = extras.getString(ValueUtils.USER_ADDRESS_1);
            userAddress2 = extras.getString(ValueUtils.USER_ADDRESS_2);
            userOfficeNumber = extras.getString(ValueUtils.USER_OFFICE_NUMBER);
            userWebsite = extras.getString(ValueUtils.USER_WEBSITE_URL);
            userReferalStatus = extras.getInt(ValueUtils.USER_REFERAL_STATUS);
            userSpecialityId = extras.getInt(ValueUtils.USER_SPECIALITY_ID);
            displayDatas();
            checkReferalStatus();

        }

    }

    private void checkReferalStatus() {
        if(userReferalStatus == ValueUtils.DoctorApproved)
        {
            doctorApprovedView();
        }
        else if(userReferalStatus == ValueUtils.DoctorPending)
        {
            doctorPendingView();
        }
        else if(userReferalStatus == ValueUtils.DoctorExplore)
        {
            doctorExpolreView();
        }
    }

    private void doctorExpolreView() {

        llReferralRequestContainer.setVisibility(View.VISIBLE);
        tvReferralRequest.setText(R.string.send_referral_request);
        tvReferralRequest.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
    }

    private void doctorPendingView() {
        llReferralRequestContainer.setVisibility(View.VISIBLE);
        tvReferralRequest.setText(R.string.ReferralRequestSent);
        tvReferralRequest.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_grey_3));
    }

    private void doctorApprovedView() {
        llContactFeatureContainer.setVisibility(View.VISIBLE);
        llLocationHistoryContainer.setVisibility(View.VISIBLE);

    }

    private void displayDatas() {
        tvDoctorName.setText("Dr. "+userName);
        tvDoctorSepciality.setText(userSpeciality);
        tvAddress1.setText(userAddress1);
        tvAddress2.setText(userAddress2);
        tvOfficeNumber.setText(userOfficeNumber);
        tvWebsiteUrls.setText(userWebsite);
        tvMapAddress1.setText(userAddress1);
        tvMapAddress2.setText(userAddress2);

    }

    private void initUI() {
        tvDoctorName = (TextView)findViewById(R.id.tv_doctor_name);
        tvDoctorSepciality = (TextView)findViewById(R.id.tv_doctor_speciality);
        tvAddress1 = (TextView)findViewById(R.id.tv_address_1);
        tvAddress2 = (TextView)findViewById(R.id.tv_address_2);
        tvOfficeNumber = (TextView)findViewById(R.id.tv_office_number);
        tvWebsiteUrls = (TextView)findViewById(R.id.tv_website_url);
        tvReferralRequest = (TextView)findViewById(R.id.tvReferralRequest);
        tvMapAddress1 = (TextView)findViewById(R.id.tv_map_address_1);
        tvMapAddress2 = (TextView)findViewById(R.id.tv_map_address_2);


        tvHistory = (TextView)findViewById(R.id.tvHistory);
        tvLocation = (TextView)findViewById(R.id.tvLocation);
        tvHistory.setOnClickListener(this);
        tvLocation.setOnClickListener(this);

        llMapContainer =  (LinearLayout)findViewById(R.id.llMapContainer);
        llHistoryContainer =  (LinearLayout)findViewById(R.id.llHistoryConatiner);
        llNumberWebsiteContainer =  (LinearLayout)findViewById(R.id.ll_number_webiste_container);
        llContactFeatureContainer =  (LinearLayout)findViewById(R.id.llContactFetaureContainer);
        llLocationHistoryContainer =  (LinearLayout)findViewById(R.id.llLocationHistoryConatiner);
        llReferralRequestContainer =  (LinearLayout)findViewById(R.id.llReferralRequestContainer);

        ivAvatar = (ImageView)findViewById(R.id.iv_avatar);
        lvHistoryCalls = (ListView)findViewById(R.id.listView);
        lvHistoryCalls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!flag_loading)
                    {
                        flag_loading = true;
                        addHistoryDataitems();
                    }



                }
            }
        });
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appointementHistoryAdapter = new AppointementHistoryAdapter(appointmentHistoryList,this,inflater);
        lvHistoryCalls.setAdapter(appointementHistoryAdapter);
        appointementHistoryAdapter.notifyDataSetChanged();
    }

    private void addHistoryDataitems() {
        if(currentPage<totalPages)
        {
            getAppointmentHistory();
        }
        else
            isHistoryAvailable = true;
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Doctor Information");
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_blue, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvLocation:
                    showLocation();
                break;
            case R.id.tvHistory:
                    showHistory();
                break;
        }

    }

    private void showHistory() {
        llMapContainer.setVisibility(View.GONE);
        tvLocation.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        tvLocation.setTextColor(ContextCompat.getColor(this,R.color.light_blue));

        llHistoryContainer.setVisibility(View.VISIBLE);
        tvHistory.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        tvHistory.setTextColor(ContextCompat.getColor(this,R.color.White));

        if(!isHistoryAvailable)
            getAppointmentHistory();

    }

    private void getAppointmentHistory() {
        if(!flag_loading)
        {
            progressDialog = LoadingDialog.show(this,"Fetching history");
            flag_loading = true;
        }

        int patientId = globalApplication.getUserId();
        int pageNo = currentPage+1;
        final Call<AppointmentHistoryBean> appointmentHistoryBeanCall = apiInterface.getAppointmentHistory(patientId,userId,pageNo);
        appointmentHistoryBeanCall.enqueue(new Callback<AppointmentHistoryBean>() {
            @Override
            public void onResponse(Call<AppointmentHistoryBean> call, Response<AppointmentHistoryBean> response) {
                progressDialog.dismiss();
                flag_loading =false;
                if(response.code()==200)
                {
                    appointmentHistoryList.addAll(response.body().getData());
                    totalPages = response.body().getMeta().getPagination().getTotalPages();
                    currentPage = response.body().getMeta().getPagination().getCurrentPage();
                    displayHistoryDatas();
                }
                else
                {
                    Utils.showToast(DoctorsInformationActivity.this,"Unable to get the data ");
                }
            }

            @Override
            public void onFailure(Call<AppointmentHistoryBean> call, Throwable t) {
              progressDialog.dismiss();
                flag_loading =  false;
                Utils.showToast(DoctorsInformationActivity.this,"Unable to get the data "+t.getLocalizedMessage());
            }
        });


    }

    private void displayHistoryDatas() {

            appointementHistoryAdapter.notifyDataSetChanged();
    }

    private void showLocation() {
        llMapContainer.setVisibility(View.VISIBLE);
        tvLocation.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        tvLocation.setTextColor(ContextCompat.getColor(this,R.color.White));

        llHistoryContainer.setVisibility(View.GONE);
        tvHistory.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        tvHistory.setTextColor(ContextCompat.getColor(this,R.color.light_blue));

    }


    public void doCallOperation(View view) {
        int callerId = globalApplication.getUserId();
        int receiverId = userId;
        String receiverName = userName;
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(this, ValueUtils.CALL, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToCallStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(DoctorsInformationActivity.this,"Failed to call "+msg);
            }
        });
    }
    private void goToCallStream()
    {
        Intent intent = new Intent(this, CallStreamActivity.class);
        startActivity(intent);
    }
    public void doChatOperation(View view) {
        int receiverId = userId;
        String receiverName = userName;
        globalApplication.setPatSelectedCallerType(ValueUtils.patient);
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);

        getChatInformation();
    }
    public void getChatInformation()
    {

        progressDialog = LoadingDialog.show(this,"Fetching data...");

        int patientId = globalApplication.getUserId();
        int doctorId = globalApplication.getPatSelectedCallerId();
        int creatorId = patientId;
        final Call<ChatInitiateModel> chatModel = apiInterface.doInitiateChatOperation(patientId,doctorId,creatorId);
        chatModel.enqueue(new Callback<ChatInitiateModel>() {
            @Override
            public void onResponse(Call<ChatInitiateModel> call, Response<ChatInitiateModel> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    chatId =  response.body().getData().getChatId();
                    goToStartChatOperation();
                }
                else
                {
                    Utils.showToast(DoctorsInformationActivity.this,"Unable to fetch data please try again");
                }
            }

            @Override
            public void onFailure(Call<ChatInitiateModel> call, Throwable t) {
                Utils.showToast(DoctorsInformationActivity.this,"Unable to fetch data :"+t.getLocalizedMessage());
            }
        });


    }

    private void goToStartChatOperation() {
        Intent intent = new Intent(this, StartChatOperationActivity.class);
        intent.putExtra(ValueUtils.ChatId,chatId);
        startActivity(intent);
    }
    public void doVideoCallOperation(View view) {
        int callerId = globalApplication.getUserId();
        int receiverId = userId;
        String receiverName = userName;
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(this, ValueUtils.VISIT, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToVideoStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(DoctorsInformationActivity.this,"Failed to call "+msg);
            }
        });
    }

    private void goToVideoStream() {
        Intent intent = new Intent(this, VideoCallStreamAcitivity.class);
        startActivity(intent);
    }

    public void doCreateAppointment(View view) {
        Intent intent = new Intent(this, SelectAppointmentTypeActivity.class);
        startActivity(intent);
    }

    public void doReferralRequest(View view) {
        if(userReferalStatus==ValueUtils.DoctorExplore)
        {
            showReferralRequestDialog();
        }
    }

    private void showReferralRequestDialog() {

        final Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.layout_referral_request_dialog);
        dialog.show();


        final EditText userInput = (EditText) dialog.findViewById(R.id.ed_reason);
        final Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
        final Button btnSubmit = (Button)dialog.findViewById(R.id.btn_submit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInput.getText().toString().equals(""))
                {
                    Utils.showToast(DoctorsInformationActivity.this,"Invalid reason please fill the reason field");
                }
                else
                {
                    sendReferralRequest(userInput.getText().toString());
                }

            }
        });


    }

    private void sendReferralRequest(String reason) {
        progressDialog = LoadingDialog.show(this,"Sending request");
        int patientId = globalApplication.getUserId();
        final int primar_doctor_id = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId();

        Call<Success>  requestCall = apiInterface.doCreateReferralRequest(patientId,userSpecialityId,primar_doctor_id,reason);
        requestCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                        doctorPendingView();
                }
                else
                {
                    Utils.showToast(DoctorsInformationActivity.this,"Unable to send request please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable throwable) {
                progressDialog.dismiss();
                Utils.showToast(DoctorsInformationActivity.this,"Unable to send request "+throwable.getLocalizedMessage());
            }
        });
    }
}
