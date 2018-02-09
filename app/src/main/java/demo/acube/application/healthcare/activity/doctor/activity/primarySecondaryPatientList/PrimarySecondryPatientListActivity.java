package demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule.CalendarScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.MyProfileActivity;
import demo.acube.application.healthcare.activity.doctor.adapter.ExpandablePatientListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.primaryPatientList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.primaryPatientList.PrimaryPatientListBean;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class PrimarySecondryPatientListActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<Datum> patientList = new ArrayList<>();
    private GlobalApplication globalApplication;
    APIInterface apiInterface;
    private ProgressDialog progressDialog;
    private Boolean isPrimary =  true;
    ExpandablePatientListAdapter expandablePatientListAdapter;
    private TextView tvPrimaryHeader,tvSecondaryHeader;
    LayoutInflater inflater;
    private FrameLayout frameLayout;
    FloatingActionMenu circleMenu;
    ImageView ivFloatBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_secondry_patient_list);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        initUI();
        displayDatas();
        getPatientList();
        circleMenu();
    }


    private void initUI() {
        expandableListView = (ExpandableListView)findViewById(R.id.listView);
        globalApplication  = (GlobalApplication)getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        tvPrimaryHeader = (TextView)findViewById(R.id.tvPrimaryheader);
        tvSecondaryHeader = (TextView)findViewById(R.id.tvSecondaryHeader);
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();
    }
    private void circleMenu() {

        ivFloatBtn = (ImageView) findViewById(R.id.fab_circle_menu);
        ivFloatBtn.bringToFront();


        View patient = createViewForCircleMenu(getString(R.string.Patients));
        View tasks = createViewForCircleMenu(getString(R.string.Tasks));
        View calendar = createViewForCircleMenu(getString(R.string.Calendar));
        View profile = createViewForCircleMenu(getString(R.string.Profile));

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ;
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();

            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCalendarSchedule();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileActivity();
            }
        });


        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        patient.setLayoutParams(tvParams);
        tasks.setLayoutParams(tvParams);
        calendar.setLayoutParams(tvParams);
        profile.setLayoutParams(tvParams);
        SubActionButton.Builder subBuilder = new SubActionButton.Builder(this);

        circleMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(0) // A whole circle!
                .setEndAngle(-180)
                .setRadius(getResources().getDimensionPixelSize(R.dimen._75sdp))
                .addSubActionView(profile)
                .addSubActionView(patient)
                .addSubActionView(calendar)
                .addSubActionView(tasks)
                .attachTo(ivFloatBtn)
                .build();

        circleMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                onCircleMenuOpened();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                onCircleMenuClosed();
            }
        });

    }
    private void goToProfileActivity() {
        startActivity(new Intent(this, MyProfileActivity.class));
    }

    private void goToCalendarSchedule() {
        startActivity(new Intent(this,CalendarScheduleActivity.class));
    }

    private void goToHome() {
        startActivity(new Intent(this, DoctorHomeActivity.class));
    }
    public void onCircleMenuOpened()
    {
        frameLayout.setVisibility(View.VISIBLE);
        // ivFloatBtn.animate().rotation(90).start();
        ivFloatBtn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_menu_on));
    }
    public void onCircleMenuClosed()
    {
        frameLayout.setVisibility(View.GONE);
        // ivFloatBtn.animate().rotation(0).start();
        ivFloatBtn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_menu_default));
    }
    public View createViewForCircleMenu(String name)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_circle_menu_item, null);
        TextView tvMenuName = (TextView)view.findViewById(R.id.tv_menu_name);
        ImageView ivCircleMenu  = (ImageView)view.findViewById(R.id.iv_circle_menu_icon);
        tvMenuName.setText(name);
        if(name.equals(getString(R.string.Profile)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_profile_off));
        }
        else if(name.equals(getString(R.string.Patients)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_patients_on));
        }
        else if(name.equals(getString(R.string.Calendar)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_calendar_off));
        }
        else if(name.equals(getString(R.string.Tasks)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_tasks_off));
        }
        return view;

    }
    private void getPatientList() {
        patientList.clear();
        progressDialog = LoadingDialog.show(this,"Loading");
        int userID = globalApplication.getUserId();
        int pageNo = 1;
        String patientType = isPrimary? ValueUtils.primary:ValueUtils.secondary;
        final Call<PrimaryPatientListBean> primaryPatientListBeanCall = apiInterface.getPrimarySecondaryPatientList(userID,patientType,pageNo);
        primaryPatientListBeanCall.enqueue(new Callback<PrimaryPatientListBean>() {
            @Override
            public void onResponse(Call<PrimaryPatientListBean> call, Response<PrimaryPatientListBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    patientList.addAll(response.body().getData());
                    expandablePatientListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to get datas");
                }
            }

            @Override
            public void onFailure(Call<PrimaryPatientListBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to get datas : "+t.getLocalizedMessage());
            }
        });
    }

    private void displayDatas() {
        inflater =   (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        expandablePatientListAdapter = new ExpandablePatientListAdapter(patientList,this,inflater,true);
        expandableListView.setAdapter(expandablePatientListAdapter);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        expandableListView.setEmptyView(emptyText);
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
        int referId = patientList.get(groupPos).getPendingReferralRequests().get(childPos).getReferralId();
        progressDialog = LoadingDialog.show(this,"Loading");
        Call<Success> approveCall = apiInterface.declineRefferalRequest(referId,reason);
        approveCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                   rejectDialog.dismiss();
                    patientList.get(groupPos).getPendingReferralRequests().remove(childPos);
                    expandablePatientListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to post data please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to post data "+t.getLocalizedMessage());
            }
        });

    }

    public void approveRequest(final int groupPos, final int childPos)
    {
        final int referId = patientList.get(groupPos).getPendingReferralRequests().get(childPos).getReferralId();
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
                    patientList.get(groupPos).getPendingReferralRequests().remove(childPos);
                    expandablePatientListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to post data please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PrimarySecondryPatientListActivity.this,"Unable to post data "+t.getLocalizedMessage());
            }
        });
    }


    public void loadSecondaryList(View view) {
        isPrimary = false;
        tvPrimaryHeader.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
        tvPrimaryHeader.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        tvSecondaryHeader.setTextColor(ContextCompat.getColor(this,R.color.White));
        tvSecondaryHeader.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        getPatientList();
    }

    public void loadPrimaryList(View view) {
        isPrimary = true;
        tvPrimaryHeader.setTextColor(ContextCompat.getColor(this,R.color.White));
        tvPrimaryHeader.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        tvSecondaryHeader.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
        tvSecondaryHeader.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        getPatientList();
    }

    public void closeMenu(View view) {
        circleMenu.close(true);
    }
}
