package demo.acube.application.healthcare.activity.doctor.activity.archive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule.CalendarScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.MyProfileActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.activity.doctor.adapter.ArchivePageAdapter;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class ArchiveHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout llAllAlertContainer,llCallAlertContainer,llChatAlertContainer,llVideoAlertContainer,llVisitAlertContainer;
    public int selectedPosition = 0;
    @BindView(R.id.tv_archive_heading)
    TextView tvArchiveHeading;
    public ProgressDialog progressDialog;
    private FrameLayout frameLayout;
    FloatingActionMenu circleMenu;
    ImageView ivFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_home);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        ButterKnife.bind(this);
        initUI();
        circleMenu();
        Utils.setFontForTextView(this,tvArchiveHeading);

    }

    private void initUI() {

        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();
        llAllAlertContainer = (LinearLayout)findViewById(R.id.llAllAlertContianer);
        llCallAlertContainer = (LinearLayout)findViewById(R.id.llCallAlertContianer);
        llChatAlertContainer = (LinearLayout)findViewById(R.id.llChatAlertContianer);
        llVideoAlertContainer = (LinearLayout)findViewById(R.id.llVirtualVisitAlertContianer);
        llVisitAlertContainer = (LinearLayout)findViewById(R.id.llPhysicalVisitAlertContianer);

        llAllAlertContainer.setOnClickListener(this);
        llCallAlertContainer.setOnClickListener(this);
        llChatAlertContainer.setOnClickListener(this);
        llVideoAlertContainer.setOnClickListener(this);
        llVisitAlertContainer.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.pager);

        ArchivePageAdapter homePageAdapter = new ArchivePageAdapter(getSupportFragmentManager(),5,this);
        viewPager.setAdapter(homePageAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

    }
    private void goToProfileActivity() {
        startActivity(new Intent(this, MyProfileActivity.class));
    }

    private void goToCalendarSchedule() {
        startActivity(new Intent(this,CalendarScheduleActivity.class));
    }

    private void goToPatientList() {
        startActivity(new Intent(this, PrimarySecondryPatientListActivity.class));
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
    public void closeMenu(View view) {
        circleMenu.close(true);
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
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_patients_off));
        }
        else if(name.equals(getString(R.string.Calendar)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_calendar_off));
        }
        else if(name.equals(getString(R.string.Tasks)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_tasks_on));
        }
        return view;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llAllAlertContianer:
                showAllAlerts();
                break;
            case R.id.llCallAlertContianer:
                showCallALerts();
                break;
            case R.id.llChatAlertContianer:
                showChatAlerts();
                break;
            case R.id.llVirtualVisitAlertContianer:
                showVirtualVisitAlerts();
                break;
            case R.id.llPhysicalVisitAlertContianer:
                showPhysicalVisitAlerts();
                break;
        }
    }

    private void showAllAlerts()
    {
        llAllAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        llCallAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llChatAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVideoAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVisitAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));

        viewPager.setCurrentItem(0);



    }
    private void showCallALerts() {
        llAllAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llCallAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        llChatAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVideoAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVisitAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        viewPager.setCurrentItem(1);
    }
    private void showChatAlerts() {
        llAllAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llCallAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llChatAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        llVideoAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVisitAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        viewPager.setCurrentItem(2);
    }
    private void showVirtualVisitAlerts() {
        llAllAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llCallAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llChatAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVideoAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        llVisitAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        viewPager.setCurrentItem(3);
    }

    private void showPhysicalVisitAlerts() {
        llAllAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llCallAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llChatAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVideoAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
        llVisitAlertContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        viewPager.setCurrentItem(4);
    }

    public void startLoadingScreen()
    {
        if(progressDialog==null || !progressDialog.isShowing())
            progressDialog = LoadingDialog.show(this,"Loading...");
    }
    public void stopLoadingScreen()
    {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
    public void goBack(View view) {
        onBackPressed();
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
