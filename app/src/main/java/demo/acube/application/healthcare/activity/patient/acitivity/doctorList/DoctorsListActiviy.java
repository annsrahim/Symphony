package demo.acube.application.healthcare.activity.patient.acitivity.doctorList;

import android.app.ProgressDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.adapters.SecondaryDoctorListAdapter;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class DoctorsListActiviy extends AppCompatActivity {

    private ListView lvSecondartyDoctorList;
    private String isFrom = "";
    APIInterface apiInterface;
    ProgressDialog progressDialog;
   SecondaryDoctorsList secondaryDoctorsLists;
    SecondaryDoctorListAdapter secondaryDoctorListAdapter;
    GlobalApplication globalApplication;
    LinearLayout llDocotorDetails;
    ImageView ivCheckMarkCall;
    private TextView tvDoctorName,tvDoctorSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_activiy);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        isFrom = getIntent().getStringExtra(ValueUtils.isFrom);
        initUI();
        initToolbar();
        getSecondaryDoctorList();
        llDocotorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSelectedCaller();
            }
        });
    }
    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

           if(isFrom.equals(ValueUtils.CALL_NOW_ACTIVITY))
           {
               toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_call_blue, null));
               toolbar.setTitle(R.string.select_a_health_care_provider);
           }
           else if(isFrom.equals(ValueUtils.CHAT_NOW_ACTIVITY))
           {
               toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_orange, null));
               toolbar.setTitle(R.string.select_a_health_care_provider);
           }
           else if(isFrom.equals(ValueUtils.VIRTUAL_VISIT_ACTIVITY))
           {
               toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_video_call_blue, null));
               toolbar.setTitle(R.string.select_a_health_care_provider);
           }
           else
           {
               toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
               toolbar.setTitle(R.string.select_physician);
           }

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setToolBarBackgroundColor() {
    }

    private void updateSelectedCaller() {
        globalApplication.setPatSelectedCallerId(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId());
        globalApplication.setPatSelectedCallerName(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getName());
        finish();
    }

    private void getSecondaryDoctorList() {
        progressDialog = LoadingDialog.show(this,"Fetching doctors list");
        int userId = globalApplication.getUserId();
        Call<SecondaryDoctorsList> secondaryDoctorsListCall = apiInterface.doGetSecondaryDoctorList(userId);
        secondaryDoctorsListCall.enqueue(new Callback<SecondaryDoctorsList>() {
            @Override
            public void onResponse(Call<SecondaryDoctorsList> call, Response<SecondaryDoctorsList> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    secondaryDoctorsLists = response.body();
                    doDisplayDataList();
                    
                }

            }

            @Override
            public void onFailure(Call<SecondaryDoctorsList> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ResFailoe",t.getLocalizedMessage());
            }
        });
    }

    private void doDisplayDataList() {
        lvSecondartyDoctorList.setAdapter(new SecondaryDoctorListAdapter(this,secondaryDoctorsLists));
    }

    private void initUI()
    {
        lvSecondartyDoctorList = (ListView)findViewById(R.id.list_secondary_doctors);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        secondaryDoctorsLists = new SecondaryDoctorsList();
        globalApplication = (GlobalApplication)getApplicationContext();
        llDocotorDetails = (LinearLayout)findViewById(R.id.llDoctorDetailsContainer);
        ivCheckMarkCall = (ImageView)findViewById(R.id.list_iv_checkmark_call);
        tvDoctorName = (TextView)findViewById(R.id.list_tv_doctor_name);
        tvDoctorSpeciality = (TextView)findViewById(R.id.list_tv_speciality_type);
       assignCheckMark();
        displayPrimaryDoctor();

    }

    private void displayPrimaryDoctor()
    {
        tvDoctorName.setText(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getName());
        tvDoctorSpeciality.setText(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getSpecialty().getName());
    }


    private void assignCheckMark()
    {
        int primaryDoctorId = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId();
        if(globalApplication.getPatSelectedCallerId()==primaryDoctorId || globalApplication.getPatSelectedCallerId()==0)
        {
            ivCheckMarkCall.setVisibility(View.VISIBLE);
        }
        else
        {
                ivCheckMarkCall.setVisibility(View.GONE);

        }
    }


    public void goBack(View view) {
        onBackPressed();
    }
}
