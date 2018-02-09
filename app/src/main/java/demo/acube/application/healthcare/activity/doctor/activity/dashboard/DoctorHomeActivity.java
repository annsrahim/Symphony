package demo.acube.application.healthcare.activity.doctor.activity.dashboard;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.archive.ArchiveHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule.CalendarScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet.DoctorSelectAppointmentActivity;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.MyProfileActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.activity.doctor.adapter.DoctorHomePageAdapter;
import demo.acube.application.healthcare.activity.doctor.adapter.DoctorTaskSearchListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.taskCountModel.TaskCountBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class DoctorHomeActivity extends AppCompatActivity implements View.OnClickListener {

    float density;
    //This is our tablayout
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    public Datum doctorTaskListBean;
    public int selectedPosition = 0;
    LinearLayout llSearchContainer;
    EditText edSearchText;
    private List<Datum> taskList = new ArrayList<>();
    private DoctorTaskSearchListAdapter doctorTaskSearchListAdapter;
    private ListView lvSearchResults;
    FrameLayout frameSearchContainer;

    TextView tvNoResults;


    private int actionBarHeight;
    //This is our viewPager
    private ViewPager viewPager;
    private LinearLayout llAllAlertContainer,llCallAlertContainer,llChatAlertContainer,llVideoAlertContainer,llVisitAlertContainer;
    private TextView tvTabAllAlert,tvTabCallAlert,tvTabChatAlert,tvTabVideoAlert,tvTabVisitAlert;
    private FrameLayout frameLayout;
    FloatingActionMenu circleMenu;
    ImageView ivFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_home);
       // //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.updateDeviceToken(this);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initUI();
        initToolbar();
        getTaskCountForTab();
        circleMenu();


        DoctorHomePageAdapter homePageAdapter = new DoctorHomePageAdapter(getSupportFragmentManager(),5,this);
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        Utils.setFontForTextView(this,tvToolbarTitle);
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

    private void performSearch(String searchQuery) {
        taskList.clear();
        progressDialog = LoadingDialog.show(this,"Searching...");
        Call<DoctorTaskListBean> doctorTaskListBeanCall = apiInterface.searchTaskForDoctor(globalApplication.getUserId(),searchQuery);
        doctorTaskListBeanCall.enqueue(new Callback<DoctorTaskListBean>() {
            @Override
            public void onResponse(Call<DoctorTaskListBean> call, Response<DoctorTaskListBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    taskList.addAll(response.body().getData());
                    if(taskList.size() > 0)
                    {
                        displaySearchResults();
                    }
                    else
                    {
                        displayNoSearchResults();
                    }
                }
                else
                {
                    displayNoSearchResults();
                }
            }

            @Override
            public void onFailure(Call<DoctorTaskListBean> call, Throwable throwable) {
                progressDialog.dismiss();
                Utils.showToast(DoctorHomeActivity.this,"Unable to get data "+throwable.getLocalizedMessage());
                displayNoSearchResults();
            }
        });


    }

    private void displaySearchResults() {
        tvNoResults.setVisibility(View.GONE);
        frameSearchContainer.setVisibility(View.VISIBLE);
        doctorTaskSearchListAdapter.notifyDataSetChanged();
        Utils.hideKeyboard(this);

    }

    public void closeSearchMenu(View view) {
        edSearchText.setText("");
        Utils.hideKeyboard(this);
        frameSearchContainer.setVisibility(View.GONE);
        llSearchContainer.setVisibility(View.GONE);
    }
    private void displayNoSearchResults()
    {
        taskList.clear();
        doctorTaskSearchListAdapter.notifyDataSetChanged();
        Utils.hideKeyboard(this);
        tvNoResults.setVisibility(View.VISIBLE);
    }

    public void getTaskCountForTab()
    {
       // progressDialog = LoadingDialog.show(this,"loading...");
        int userId = globalApplication.getUserId();
        Call<TaskCountBean> taskCountCall = apiInterface.getTaskCount(userId);
        taskCountCall.enqueue(new Callback<TaskCountBean>() {
            @Override
            public void onResponse(Call<TaskCountBean> call, Response<TaskCountBean> response) {
               // progressDialog.dismiss();
                if(response.code()==200)
                {
                   assignTaskCount(response.body());
                }
                else
                {
                    Utils.showToast(DoctorHomeActivity.this,"Unable to fetch data ");
                }
            }

            @Override
            public void onFailure(Call<TaskCountBean> call, Throwable t) {
               // progressDialog.dismiss();
                Utils.showToast(DoctorHomeActivity.this,"Unable to fetch data "+t.getLocalizedMessage());
            }
        });
    }

    private void assignTaskCount(TaskCountBean body)
    {
        int totalCount = body.getData().getCall()+body.getData().getText()+body.getData().getVisit_physical()+body.getData().getVisit_virtual();
        setTabTitle(0,totalCount);
        setTabTitle(1,body.getData().getCall());
        setTabTitle(2,body.getData().getText());
        setTabTitle(3,body.getData().getVisit_virtual());
        setTabTitle(4,body.getData().getVisit_physical());
    }

    private void initUI()
    {
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();

        frameSearchContainer  = (FrameLayout) findViewById(R.id.frameSearchContainer);


        tvNoResults = (TextView)findViewById(R.id.tv_no_results);


        llAllAlertContainer = (LinearLayout)findViewById(R.id.llAllAlertContianer);
        llCallAlertContainer = (LinearLayout)findViewById(R.id.llCallAlertContianer);
        llChatAlertContainer = (LinearLayout)findViewById(R.id.llChatAlertContianer);
        llVideoAlertContainer = (LinearLayout)findViewById(R.id.llVirtualVisitAlertContianer);
        llVisitAlertContainer = (LinearLayout)findViewById(R.id.llPhysicalVisitAlertContianer);
        llSearchContainer = (LinearLayout)findViewById(R.id.ll_search_container);
        edSearchText = (EditText)findViewById(R.id.ed_search_text);
        edSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(v.getText().toString().length()>=2)
                        performSearch(v.getText().toString());
                    else
                        Utils.showToast(DoctorHomeActivity.this,"Try atleast with two characters");
                    return true;
                }
                return false;
            }
        });
        llAllAlertContainer.setOnClickListener(this);
        llCallAlertContainer.setOnClickListener(this);
        llChatAlertContainer.setOnClickListener(this);
        llVideoAlertContainer.setOnClickListener(this);
        llVisitAlertContainer.setOnClickListener(this);

        tvTabAllAlert = (TextView)findViewById(R.id.tvTabAllAlert);
        tvTabCallAlert = (TextView)findViewById(R.id.tvTablCallALert);
        tvTabChatAlert = (TextView)findViewById(R.id.tvTabChatALert);
        tvTabVideoAlert = (TextView)findViewById(R.id.tvTabVideoAlert);
        tvTabVisitAlert = (TextView)findViewById(R.id.tvTabVisitAlert);

        density = getResources().getDisplayMetrics().density;
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        lvSearchResults = (ListView)findViewById(R.id.lvSearchResult);
        doctorTaskSearchListAdapter = new DoctorTaskSearchListAdapter(taskList,this);
        lvSearchResults.setAdapter(doctorTaskSearchListAdapter);
        doctorTaskSearchListAdapter.notifyDataSetChanged();

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


    public void setTabTitle(int pos, int taskCount) {
        String totalTask = String.valueOf(taskCount);
        switch(pos)
        {
            case 0:
                    tvTabAllAlert.setText("All ("+totalTask+")");
                break;
            case 1:
                    tvTabCallAlert.setText(totalTask);
                break;
            case 2:
                    tvTabChatAlert.setText(totalTask);
                break;
            case 3:
                    tvTabVideoAlert.setText(totalTask);
                break;
            case 4:
                    tvTabVisitAlert.setText(totalTask);
                break;


        }


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


    public void selectAppointment(View view) {
        Intent intent = new Intent(this, DoctorSelectAppointmentActivity.class);
        startActivity(intent);
    }

    public void showDoctorCalendar(View view) {

    }

    public void closeMenu(View view) {
        circleMenu.close(true);
    }

    @Override
    public void onBackPressed() {

    }

    public void goToArchiveActivity(View view) {
        startActivity(new Intent(this, ArchiveHomeActivity.class));
    }

    public void openSearchMenu(View view)
    {

        llSearchContainer.setVisibility(View.VISIBLE);
    }
    public void goBack(View view) {
        onBackPressed();
    }


    //    public void showCircularMenu() {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        CircularMenuFragemnt circularMenuFragemnt = CircularMenuFragemnt.newInstance();
//        circularMenuFragemnt.show(ft,"circularMenuFragemnt");
//    }

    public void openSearchResults()
    {
        frameSearchContainer.setVisibility(View.VISIBLE);

    }


}
