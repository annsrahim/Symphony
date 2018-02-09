package demo.acube.application.healthcare.activity.doctor.activity.patientList;

import android.app.ProgressDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import demo.acube.application.healthcare.activity.doctor.adapter.PatientListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.patientList.PatientListBean;
import demo.acube.application.healthcare.activity.doctor.models.patientList.PrimaryPatient;
import demo.acube.application.healthcare.activity.doctor.models.patientList.SecondaryPatient;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class PatientListActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    private ListView listPatientPrimaryList;
    private List<PrimaryPatient> primaryPatientList = new ArrayList<>();
    private List<SecondaryPatient> secondaryPatient = new ArrayList<>();
    PatientListBean patientList;
    PatientListAdapter patientListAdapter;
    private LinearLayout llPrimaryHeader,llSecondaryHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)getApplicationContext();
        initToolbar();
        initUI();
        getPateintList();
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarCancel = (TextView)toolbar.findViewById(R.id.toolbar_tv_cancel);

        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarCancel);

        tvToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getPateintList()
    {
        progressDialog = LoadingDialog.show(this,"Fetching patient list");
        int userID = globalApplication.getUserId();
        Call<PatientListBean> patientListCall = apiInterface.getPatientList(userID);
        patientListCall.enqueue(new Callback<PatientListBean>() {
            @Override
            public void onResponse(Call<PatientListBean> call, Response<PatientListBean> response) {
               progressDialog.dismiss();
                if(response.code()==200)
                {
                    if(response.body().getData().getPrimaryPatients().size()>0)
                    {
                        primaryPatientList.addAll(response.body().getData().getPrimaryPatients());
                        displayPrimaryPatients();
                    }
                    else
                    {
                        llPrimaryHeader.setVisibility(View.GONE);
                    }
                    if(response.body().getData().getSecondaryPatients().size()>0)
                    {
                        secondaryPatient.addAll(response.body().getData().getSecondaryPatients());
                        displaySecondaryPatients();
                    }
                    else
                    {
                        llSecondaryHeader.setVisibility(View.GONE);
                    }

                }
                else
                {
                    Utils.showToast(PatientListActivity.this,"Unable to get list");
                }

            }

            @Override
            public void onFailure(Call<PatientListBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(PatientListActivity.this,"Unable to get list :"+t.getLocalizedMessage());
            }
        });
     }

    private void displaySecondaryPatients() {
    }

    private void displayPrimaryPatients()
    {

        listPatientPrimaryList.setAdapter(new PatientListAdapter(this,primaryPatientList));
    }

    private void initUI()
    {
        listPatientPrimaryList = (ListView)findViewById(R.id.list_primary_patients);
        llPrimaryHeader = (LinearLayout)findViewById(R.id.llPrimaryPatientHeader);
        llSecondaryHeader = (LinearLayout)findViewById(R.id.llSecondaryPatientHeader);
    }

    public void cancelActivity(View view) {

    }
}
