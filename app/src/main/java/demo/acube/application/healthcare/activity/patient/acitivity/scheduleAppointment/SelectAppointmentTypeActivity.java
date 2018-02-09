package demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class SelectAppointmentTypeActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        initToolbar();

    }

    private void initToolbar()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Select Type");
        setSupportActionBar(toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    public void goToMedicationRemainder(View view)
    {
        Intent intent = new Intent(this,MedicationRemainderActivity.class);
        String isFrom = "";
        Intent receiveIntent = getIntent();
        if(receiveIntent!=null)
            isFrom = receiveIntent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null)
            intent.putExtra(ValueUtils.isFrom,ValueUtils.MySchedules);

        startActivity(intent);
    }

    public void goToCallRemainder(View view) {
        Intent intent = new Intent(this,CallAppointmentActivity.class);
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
    public void goToPhysicalVisitRemainder(View view) {
        Intent intent = new Intent(this,CallAppointmentActivity.class);
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
