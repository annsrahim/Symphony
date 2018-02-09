package demo.acube.application.healthcare.activity.doctor.activity.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class DoctorScheduleActivity extends AppCompatActivity {

    Toolbar toolbar;
    String task_type;
    String task_type_name;
    String task_user_name;
    String task_created_by;
    String task_start_date_time;
    String task_end_date_time;
    String task_alert;
    String task_notes;
    TextView tvTaskType,tvTaskUserName,tvTaskCreatedBY,tvTaskDateTime,tvTaskFromTo,tvTaskAlert,tvTaskNotes;
    Button btnExecuteTask;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        initToolBar();
        getBundledDatas();
        initUI();
        customizeExceuteButton();
    }

    private void customizeExceuteButton()
    {
        int type = Integer.parseInt(task_type);
        if(type==1)
        {
            btnExecuteTask.setText("Text Now");
        }
        else if(type==2)
        {
            btnExecuteTask.setText("Call Now");
        }
        else if(type==3)
        {
            btnExecuteTask.setText("Virtual Visit Now");
        }
        else if(type==4)
        {
            btnExecuteTask.setText("Physical Visit");
        }
    }

    private void initUI()
    {
        tvTaskType = (TextView)findViewById(R.id.tv_task_type);
        tvTaskUserName = (TextView)findViewById(R.id.tv_task_user_name);
        tvTaskCreatedBY = (TextView)findViewById(R.id.tv_task_created_by);
        tvTaskDateTime = (TextView)findViewById(R.id.tv_task_date_time);
        tvTaskFromTo = (TextView)findViewById(R.id.tv_task_from_to);
        tvTaskAlert = (TextView)findViewById(R.id.tv_task_alert);
        tvTaskNotes = (TextView)findViewById(R.id.tv_task_notes);
        btnExecuteTask = (Button)findViewById(R.id.btn_exceute_task);

    }

    private void getBundledDatas()
    {
        Bundle extras = getIntent().getExtras();


        if (extras != null)
        {
            task_type = extras.getString("TASK_TYPE");
            task_type_name = extras.getString("TASK_TYPE_NAME");
            task_user_name = extras.getString("TASK_USER_NAME");
            task_created_by = extras.getString("TASK_CREATED_BY");
            task_start_date_time = extras.getString("TASK_START_DATE_TIME");
            task_end_date_time = extras.getString("TASK_END_DATE_TIME");
            task_alert = extras.getString("TASK_ALERT");
            task_notes = extras.getString("TASK_NOTES");
            // and get whatever type user account id is
        }
    }

    private void initToolBar()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My Schedule");
        toolbar.setTitleTextColor(getResources().getColor(R.color.light_blue));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                    onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
