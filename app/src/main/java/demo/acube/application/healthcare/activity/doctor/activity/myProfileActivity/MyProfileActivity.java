package demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule.CalendarScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.activity.doctor.models.doctorDetails.DoctorDetailsBean;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class MyProfileActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private GlobalApplication globalApplication;
    private APIInterface apiInterface;
    public DoctorDetailsBean doctorDetails;
    private FrameLayout frameLayout;
    FloatingActionMenu circleMenu;
    ImageView ivFloatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentParentViewGroup,new DoctorMyProfileFragment())
                    .commit();
        }
        initUI();
        circleMenu();
    }

    private void initUI() {
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();
    }


    public void goToSettings()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParentViewGroup,new DoctorSettingsFragment())
                .commit();
    }
    public void goToEdit()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParentViewGroup,new DoctorEditFragment())
                .commit();
    }
    public void goToMyProfile()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParentViewGroup,new DoctorMyProfileFragment())
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//
//    }

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
                goToPatientList();
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTaskList();

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

    private void goToTaskList() {
        startActivity(new Intent(this,DoctorHomeActivity.class));
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

    private void goToProfileActivity() {

    }

    private void goToCalendarSchedule() {
        startActivity(new Intent(this,CalendarScheduleActivity.class));
    }

    private void goToPatientList() {
        startActivity(new Intent(this, PrimarySecondryPatientListActivity.class));
    }
    public View createViewForCircleMenu(String name)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_circle_menu_item, null);
        TextView tvMenuName = (TextView)view.findViewById(R.id.tv_menu_name);
        ImageView ivCircleMenu  = (ImageView)view.findViewById(R.id.iv_circle_menu_icon);
        tvMenuName.setText(name);
        if(name.equals(getString(R.string.Profile)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_profile_on));
        }
        else if(name.equals(getString(R.string.Patients)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_patients_off));
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
    public void closeMenu(View view) {
        circleMenu.close(true);
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
