package demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class DoctorSelectAppointmentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select_appointment);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        initToolbar();
    }

    private void initToolbar()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.select_type);
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarCancel = (TextView)toolbar.findViewById(R.id.toolbar_tv_cancel);

        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarCancel);

        tvToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next,menu);
        return true;
    }

    public void goToMedicationRemainder(View view) {
        Intent intent = new Intent(this,DoctorMedicationReminderActivity.class);
        startActivity(intent);
    }

    public void goToCallRemainder(View view) {
        Intent intent = new Intent(this,DoctorCallAppointmentActivity.class);
        intent.putExtra(ValueUtils.AppointmentType,2);
        String isFrom = "";
        Intent receiveIntent = getIntent();
        if(receiveIntent!=null)
            isFrom = receiveIntent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null)
            intent.putExtra(ValueUtils.isFrom,ValueUtils.MySchedules);
        startActivity(intent);
    }
    public void goToVirtualVisitRemainder(View view) {
        Intent intent = new Intent(this,DoctorCallAppointmentActivity.class);
        intent.putExtra(ValueUtils.AppointmentType,3);
        String isFrom = "";
        Intent receiveIntent = getIntent();
        if(receiveIntent!=null)
            isFrom = receiveIntent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null)
            intent.putExtra(ValueUtils.isFrom,ValueUtils.MySchedules);
        startActivity(intent);
    }
    public void goToPhysicalVisitRemainder(View view) {
        Intent intent = new Intent(this,DoctorCallAppointmentActivity.class);
        intent.putExtra(ValueUtils.AppointmentType,4);
        String isFrom = "";
        Intent receiveIntent = getIntent();
        if(receiveIntent!=null)
            isFrom = receiveIntent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null)
            intent.putExtra(ValueUtils.isFrom,ValueUtils.MySchedules);
        startActivity(intent);
    }
}
