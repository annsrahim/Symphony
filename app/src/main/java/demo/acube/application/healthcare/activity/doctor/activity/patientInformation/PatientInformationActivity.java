package demo.acube.application.healthcare.activity.doctor.activity.patientInformation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet.DoctorSelectAppointmentActivity;
import demo.acube.application.healthcare.activity.doctor.activity.patientsSchedule.PatinetScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.adapter.ApprovedSpecialistAdapter;
import demo.acube.application.healthcare.activity.doctor.adapter.PendingReferralsListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.referralBean.ReferralListBean;
import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.activity.patient.adapters.AppointementHistoryAdapter;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.appointmentHistoryBean.AppointmentHistoryBean;
import demo.acube.application.healthcare.model.appointmentHistoryBean.Datum;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class PatientInformationActivity extends AppCompatActivity {

    private TextView tvPatientName,tvSexAge,tvPhysician,tvHistory;
    private TextView tvPrimaryDoctorName,tvPrimaryDocotorType;
    ImageView ivPrimaryDoctorIcon,ivPrimaryDoctorExpandView;
    ImageView ivAvatar;
    private LinearLayout llDocotorListContainer,llHistoryListContainer;
    String mUserName,mAvatar,mPrimaryDoctorName,mPrimaryDoctorAvatar, mPrimaryDoctorSpecialityType;
    int userId,userAge,userGender,mPrimaryDoctorId;
    private ListView lvHistoryCalls;
    private APIInterface apiInterface;
    private GlobalApplication globalApplication;
    ProgressDialog progressDialog;
    AppointementHistoryAdapter appointementHistoryAdapter;
    int totalPages = 0;
    int currentPage = 0;
    private List<Datum> appointmentHistoryList = new ArrayList<>();
    boolean flag_loading = false;
    private Boolean isHistoryAvailable = false;
    int chatId;
    private ExpandableListView lvPendingReferrals;
    private ListView lvApprovedList;
    private List<demo.acube.application.healthcare.activity.doctor.models.referralBean.Datum> patientList = new ArrayList<>();
    private PendingReferralsListAdapter pendingReferralsListAdapter;
    private ApprovedSpecialistAdapter approvedListAdapter;
    LayoutInflater inflater;
    private LinearLayout llPendingListContainer,llApprovedListContainer,llLocationHistoryConatiner;
    List<demo.acube.application.healthcare.model.secondaryDoctorsList.Datum> approvedDoctorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_information);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initUI();
        initToolbar();
        getExtrasFromIntent();
        getPendingReferrals();
        getApprovedDoctorList();
    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        ImageView ivPatientSchedule = (ImageView)toolbar.findViewById(R.id.toolbar_iv_patient_schedule);

        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarBack);

        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivPatientSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatientScheduleActivity();
            }
        });

    }

    private void showPatientScheduleActivity() {
        Intent intent = new Intent(this, PatinetScheduleActivity.class);
        intent.putExtra(ValueUtils.USER_ID,userId);
        startActivity(intent);
    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            mUserName = extras.getString(ValueUtils.USER_NAME);
            userId = extras.getInt(ValueUtils.USER_ID);
            userAge = extras.getInt(ValueUtils.USER_AGE);
            userGender = extras.getInt(ValueUtils.GENDER);
            mAvatar = extras.getString(ValueUtils.USER_AVATAR);

            mPrimaryDoctorName = extras.getString(ValueUtils.PRIMARY_DOCTOR_NAME);
            mPrimaryDoctorSpecialityType = extras.getString(ValueUtils.PRIMARY_DOCTOR_SPECIALITY_TYPE);
            mPrimaryDoctorAvatar = extras.getString(ValueUtils.PRIMARY_DOCTOR_AVATAR);
            mPrimaryDoctorId = extras.getInt(ValueUtils.PRIMARY_DOCTOR_ID);
            displayDatas();
        }
    }
    private void getApprovedDoctorList() {
        final Call<SecondaryDoctorsList> secondaryDoctorsListCall = apiInterface.doGetSecondaryDoctorList(userId);
        secondaryDoctorsListCall.enqueue(new Callback<SecondaryDoctorsList>() {
            @Override
            public void onResponse(Call<SecondaryDoctorsList> call, Response<SecondaryDoctorsList> response) {

                if(response.code()==200)
                {
                    approvedDoctorList.addAll(response.body().getData());
                    displayApprovedSpecialist();
                }
                else
                {
                    displayNoApprovedSpecialist();
                }
            }

            @Override
            public void onFailure(Call<SecondaryDoctorsList> call, Throwable t) {

                Utils.showToast(PatientInformationActivity.this,"Unable to fetch datas "+t.getLocalizedMessage());
            }
        });
    }

    private void displayNoApprovedSpecialist() {
            llApprovedListContainer.setVisibility(View.GONE);
    }

    private void displayApprovedSpecialist() {
        if(approvedDoctorList.size()>0)
            llApprovedListContainer.setVisibility(View.VISIBLE);
        approvedListAdapter.notifyDataSetChanged();
        Utils.setListViewHeightBasedOnChildren(lvApprovedList);
    }

    private void displayDatas() {
        tvPatientName.setText(mUserName);
        String gender;
        if(userGender==0)
            gender = getString(R.string.male);
        else
            gender = getString(R.string.female);
        tvSexAge.setText(getString(R.string.sex_age,gender,String.valueOf(userAge)));


        Picasso.with(this).load(mAvatar).placeholder(R.drawable.gpc_placeholder_patient)
                .into(ivAvatar);

        tvPrimaryDoctorName.setText(mPrimaryDoctorName);
        tvPrimaryDocotorType.setText(mPrimaryDoctorSpecialityType);

        Picasso.with(this).load(mPrimaryDoctorAvatar).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(ivPrimaryDoctorIcon);
    }

    private void initUI() {
        tvPatientName = (TextView)findViewById(R.id.tv_patient_name);
        tvSexAge = (TextView)findViewById(R.id.tv_sex_age);
        ivAvatar = (ImageView)findViewById(R.id.iv_avatar);
        tvPhysician = (TextView)findViewById(R.id.tvPhysicians);
        tvHistory = (TextView)findViewById(R.id.tvHistory);
        llPendingListContainer = (LinearLayout)findViewById(R.id.ll_pending_list_container);
        llDocotorListContainer = (LinearLayout)findViewById(R.id.llDoctorListContainer);
        llHistoryListContainer = (LinearLayout)findViewById(R.id.llHistoryConatiner);
        lvPendingReferrals = (ExpandableListView)findViewById(R.id.pendingReferralsListView);
        lvApprovedList = (ListView)findViewById(R.id.approvedListView);
        tvPrimaryDoctorName = (TextView)findViewById(R.id.list_tv_doctor_name);
        tvPrimaryDocotorType = (TextView)findViewById(R.id.list_tv_speciality_type);

        ivPrimaryDoctorIcon = (ImageView)findViewById(R.id.iv_doctors_icon);
        ivPrimaryDoctorExpandView = (ImageView)findViewById(R.id.list_iv_expand_view);
        ivPrimaryDoctorExpandView.setVisibility(View.GONE);
        inflater =   (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pendingReferralsListAdapter = new PendingReferralsListAdapter(patientList,this,inflater);

        lvPendingReferrals.setAdapter(pendingReferralsListAdapter);
        lvPendingReferrals.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                setExpandableListViewHeightBasedOnItems(lvPendingReferrals);
            }
        });
        lvPendingReferrals.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setExpandableListViewHeightBasedOnItems(lvPendingReferrals);
            }
        });



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
        llLocationHistoryConatiner = (LinearLayout)findViewById(R.id.llLocationHistoryConatiner);
        llApprovedListContainer = (LinearLayout)findViewById(R.id.ll_approved_list_container);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appointementHistoryAdapter = new AppointementHistoryAdapter(appointmentHistoryList,this,inflater);
        lvHistoryCalls.setAdapter(appointementHistoryAdapter);
        appointementHistoryAdapter.notifyDataSetChanged();

        approvedListAdapter = new ApprovedSpecialistAdapter(this,approvedDoctorList);
        lvApprovedList.setAdapter(approvedListAdapter);
        approvedListAdapter.notifyDataSetChanged();
    }
    private void addHistoryDataitems() {
        if(currentPage<totalPages)
        {
            getAppointmentHistory();
        }
        else
            isHistoryAvailable = true;
    }
    public void doCallOperation(View view) {
        int callerId = globalApplication.getUserId();
        int receiverId = userId;
        String receiverName = mUserName;
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(this, ValueUtils.CALL, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToCallStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(PatientInformationActivity.this,"Failed to call "+msg);
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
        String receiverName = mUserName;
        globalApplication.setPatSelectedCallerType(ValueUtils.patient);
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);

        getChatInformation();
    }
    public void getChatInformation()
    {

        progressDialog = LoadingDialog.show(this,"Fetching data...");
        int patientId = globalApplication.getPatSelectedCallerId();
        int doctorId = globalApplication.getUserId();
        int creatorId = doctorId;
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
                    Utils.showToast(PatientInformationActivity.this,"Unable to fetch data please try again");
                }
            }

            @Override
            public void onFailure(Call<ChatInitiateModel> call, Throwable t) {
                Utils.showToast(PatientInformationActivity.this,"Unable to fetch data :"+t.getLocalizedMessage());
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
        String receiverName = mUserName;
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(this, ValueUtils.VISIT, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToVideoStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(PatientInformationActivity.this,"Failed to call "+msg);
            }
        });
    }

    private void goToVideoStream() {
        Intent intent = new Intent(this, VideoCallStreamAcitivity.class);
        startActivity(intent);
    }


    public void doCreateAppointment(View view) {
        Intent intent = new Intent(this, DoctorSelectAppointmentActivity.class);
        startActivity(intent);
    }

    public void loadDoctorsList(View view) {

        llDocotorListContainer.setVisibility(View.VISIBLE);
        llHistoryListContainer.setVisibility(View.GONE);
        tvPhysician.setTextColor(ContextCompat.getColor(this,R.color.White));
        tvPhysician.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        tvHistory.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
        tvHistory.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llLocationHistoryConatiner.setBackground(ContextCompat.getDrawable(this,R.drawable.border_background_layout_curved_blue));

    }

    public void loadHistory(View view) {

        llDocotorListContainer.setVisibility(View.GONE);
        llHistoryListContainer.setVisibility(View.VISIBLE);
        tvPhysician.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
        tvPhysician.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        tvHistory.setTextColor(ContextCompat.getColor(this,R.color.White));
        tvHistory.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        llLocationHistoryConatiner.setBackground(ContextCompat.getDrawable(this,R.drawable.border_background_layout_curved_blue));
        if(!isHistoryAvailable)
            getAppointmentHistory();
    }
    private void getAppointmentHistory() {
        if(!flag_loading)
        {
            progressDialog = LoadingDialog.show(this,"Fetching history");
            flag_loading = true;
        }

        int patientId = userId;
        int pageNo = currentPage+1;
        int doctorId = globalApplication.getUserId();
        final Call<AppointmentHistoryBean> appointmentHistoryBeanCall = apiInterface.getAppointmentHistory(patientId,doctorId,pageNo);
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
                    appointementHistoryAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PatientInformationActivity.this,"Unable to get the data ");
                }
            }

            @Override
            public void onFailure(Call<AppointmentHistoryBean> call, Throwable t) {
                progressDialog.dismiss();
                flag_loading =  false;
                Utils.showToast(PatientInformationActivity.this,"Unable to get the data "+t.getLocalizedMessage());
            }
        });


    }
    private void getPendingReferrals() {
        if(progressDialog!=null && !progressDialog.isShowing())
        {
            progressDialog = LoadingDialog.show(this,"Fetching data...");
        }
        patientList.clear();
        progressDialog = LoadingDialog.show(this,"Loading");
        int doctor_id = globalApplication.getUserId();
        int paient_id = userId;
        int status = 0;
        int pageNo = 1;
        final String patientType =  ValueUtils.primary;
        Call<ReferralListBean> pendingListCall = apiInterface.doGetReferrals(paient_id,doctor_id,status,pageNo);
        pendingListCall.enqueue(new Callback<ReferralListBean>() {
            @Override
            public void onResponse(Call<ReferralListBean> call, Response<ReferralListBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    patientList.addAll(response.body().getData());
                    displayPendingReferrals();

                }
                else
                {
                    displayNoPendingdatas();
                }
            }

            @Override
            public void onFailure(Call<ReferralListBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PatientInformationActivity.this,"Unable to get data"+t.getLocalizedMessage());

            }
        });
    }

    private void displayPendingReferrals() {
        if(patientList.size()>0)
            llPendingListContainer.setVisibility(View.VISIBLE);
        pendingReferralsListAdapter.notifyDataSetChanged();
        setExpandableListViewHeightBasedOnItems(lvPendingReferrals);
    }

    private void displayNoPendingdatas()
    {
        llPendingListContainer.setVisibility(View.GONE);
    }


    private  void setExpandableListViewHeightBasedOnItems(ExpandableListView expandableListView) {
        ExpandableListAdapter listAdapter = expandableListView.getExpandableListAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getGroupCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getGroupView(itemPos,false, expandableListView,null);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
                if(expandableListView.isGroupExpanded(itemPos))
                {
                    View childItem = listAdapter.getChildView(itemPos,0,false,expandableListView,null);
                    childItem.measure(0,0);
                    totalItemsHeight+= childItem.getMeasuredHeight();
                }

            }

            // Get total height of all item dividers.
            int totalDividersHeight = expandableListView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            expandableListView.setLayoutParams(params);
            expandableListView.requestLayout();


        }
    }
    public void rejectRequest(final int groupPos, final int childPos)
    {
        final Dialog rejectDialog = new Dialog(this);
        rejectDialog.setContentView(R.layout.layout_request_reject_dialog);
        final EditText edMessage = (EditText)rejectDialog.findViewById(R.id.ed_message);
        final TextView tvCount = (TextView)rejectDialog.findViewById(R.id.tv_characrter_count);
        TextView tvConfirmSend = (TextView)rejectDialog.findViewById(R.id.tv_confirm_send);
        final TextView tvCancel = (TextView)rejectDialog.findViewById(R.id.tv_cancel);
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText(s.length()+"/250");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDialog.dismiss();
            }
        });
        tvConfirmSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineApprovalRequestServer(groupPos,childPos,edMessage.getText().toString(),rejectDialog);
            }
        });

        rejectDialog.show();

    }
    private void declineApprovalRequestServer(final int groupPos, final int childPos, String reason, final Dialog rejectDialog) {
        int referId = patientList.get(groupPos).getReferralId();
        progressDialog = LoadingDialog.show(this,"Loading");
        Call<Success> approveCall = apiInterface.declineRefferalRequest(referId,reason);
        approveCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    rejectDialog.dismiss();
                    patientList.remove(groupPos);
                    pendingReferralsListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PatientInformationActivity.this,"Unable to post data please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PatientInformationActivity.this,"Unable to post data "+t.getLocalizedMessage());
            }
        });

    }
    public void approveRequest(final int groupPos, final int childPos)
    {
        final int referId = patientList.get(groupPos).getReferralId();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        sendApprovalRequestToServer(dialog,referId,groupPos,childPos);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_request_appprove_msg).setPositiveButton(R.string.Yes, dialogClickListener)
                .setNegativeButton(R.string.No, dialogClickListener).show();

    }
    private void sendApprovalRequestToServer(final DialogInterface dialog, int referId,final int groupPos,final int childPos)
    {
        progressDialog = LoadingDialog.show(this,"Loading");
        Call<Success> approveCall = apiInterface.approveRefferalRequest(referId);
        approveCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    dialog.dismiss();
                    patientList.remove(groupPos);
                    pendingReferralsListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PatientInformationActivity.this,"Unable to post data please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PatientInformationActivity.this,"Unable to post data "+t.getLocalizedMessage());
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}
