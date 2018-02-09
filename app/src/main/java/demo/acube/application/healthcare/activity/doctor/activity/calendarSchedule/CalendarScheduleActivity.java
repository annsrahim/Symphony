package demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.acube.application.healthcare.activity.doctor.adapter.CalendarEventsAdapter;
import demo.acube.application.healthcare.activity.doctor.models.CalendarItemModel;
import demo.acube.application.healthcare.model.calendarSearchDoctor.Appointment;
import demo.acube.application.healthcare.model.calendarSearchDoctor.CalendarSearchDoctorBean;
import demo.acube.application.healthcare.model.calendarSearchDoctor.Data;
import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet.DoctorSelectAppointmentActivity;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.MyProfileActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.activity.doctor.activity.updateOfficeHours.UpdateOfficeHoursActivity;
import demo.acube.application.healthcare.activity.doctor.models.doctorSchedule.DoctorScheduleBean;
import demo.acube.application.healthcare.activity.patient.acitivity.mySchedule.EventDecorator;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class CalendarScheduleActivity extends Activity implements OnDateSelectedListener {

    public MaterialCalendarView materialCalendarView;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    Integer scheduleFilterType = null;
    List<CalendarDay> remainderDayList = new ArrayList<CalendarDay>();
    public GlobalApplication globalApplication;
    DoctorScheduleBean doctorScheduleBean;
    DoctorScheduleBean selectedDocotorSchedule;
    LinearLayout llScheduledTaskContainer;
    LinearLayout llSearchContainer;
    TextView tvNoEvents,tvSelectedDate;
    Boolean[] enabledTypesArray = new Boolean[5];
    private int selectedDay,selectedMonth,selectedYear;
    String dateSelected;
    Boolean isDateCompleted = true;
    private TextView tvUpdateOfficeHours;
    ImageView ivFloatBtn;
    FloatingActionMenu circleMenu;
    private FrameLayout frameLayout;
    EditText edSearchText;
    FrameLayout frameSearchContainer;
    private ListView lvSearchResults;
    private List<Data> calendarSearchEventList = new ArrayList<>();
    private CalendarEventsAdapter calendarEventsAdapter;
    private LinearLayout llMainContainer;
    private ArrayList<CalendarItemModel> calendarSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);
      //  //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initUi();
        initToolbar();
        initCalendar();
        circleMenu();
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
                goToHome();

            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        startActivity(new Intent(this, MyProfileActivity.class));
    }

    private void  goToHome() {
        startActivity(new Intent(this,DoctorHomeActivity.class));
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
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_profile_off));
        }
        else if(name.equals(getString(R.string.Patients)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_patients_off));
        }
        else if(name.equals(getString(R.string.Calendar)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_calendar_on));
        }
        else if(name.equals(getString(R.string.Tasks)))
        {
            ivCircleMenu.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_tasks_off));
        }
        return view;

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));

        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarTOday = (TextView)toolbar.findViewById(R.id.toolbar_tv_today);

        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarTOday);


    }

    private void initUi() {
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();
        frameSearchContainer  = (FrameLayout) findViewById(R.id.frameSearchContainer);

        lvSearchResults = (ListView)findViewById(R.id.lvSearchResult);

        llScheduledTaskContainer = (LinearLayout)findViewById(R.id.ll_scheduled_task_container);
        tvSelectedDate = (TextView)findViewById(R.id.tv_selected_date);
        tvNoEvents = (TextView)findViewById(R.id.tv_no_events);
        tvUpdateOfficeHours = (TextView)findViewById(R.id.tv_update_office_hours);
        Arrays.fill(enabledTypesArray, Boolean.TRUE);//Enable all types while initializing



        llSearchContainer = (LinearLayout)findViewById(R.id.ll_search_container);
        edSearchText = (EditText)findViewById(R.id.ed_search_text);
        edSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(v.getText().toString().length()>=2)
                        performSearch(v.getText().toString());
                    else
                        Utils.showToast(CalendarScheduleActivity.this,"Try atleast with two characters");
                    return true;
                }
                return false;
            }
        });

        llMainContainer = (LinearLayout)findViewById(R.id.llMainContainer);

        calendarEventsAdapter = new CalendarEventsAdapter(this,calendarSearchList);
        lvSearchResults.setAdapter(calendarEventsAdapter);
        calendarEventsAdapter.notifyDataSetChanged();

    }
    private void performSearch(String searchQuery) {
        calendarSearchEventList.clear();

        progressDialog = LoadingDialog.show(this,"Searching...");
        Call<CalendarSearchDoctorBean> calendarSearchDoctorBeanCall = apiInterface.searchCalendarEvents(globalApplication.getUserId(),searchQuery);
        calendarSearchDoctorBeanCall.enqueue(new Callback<CalendarSearchDoctorBean>() {
            @Override
            public void onResponse(Call<CalendarSearchDoctorBean> call, Response<CalendarSearchDoctorBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    calendarSearchEventList.add(response.body().getData());
                    getAllCategoriesEvents(response.body().getData());

                    displaySearchEventList();


                }
                else
                {
                    Utils.showToast(CalendarScheduleActivity.this,"Unable to get datas");
                }
            }

            @Override
            public void onFailure(Call<CalendarSearchDoctorBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(CalendarScheduleActivity.this,"Unable to get datas "+t.getLocalizedMessage());
            }
        });


    }

    private void getAllCategoriesEvents(Data data) {

        calendarSearchList.clear();

        int nameAppointmentCount = data.getName().getAppointment().size();
        int nameReminderCount = data.getName().getReminder().size();
        int appointmentCount = data.getAppointment().size();
        int medicationCount =  data.getMedication().size();

        if(nameAppointmentCount>0)
        {

            for(int i=0;i<nameAppointmentCount;i++)
            {
                CalendarItemModel calendarEventItem = new CalendarItemModel();
                calendarEventItem.setPatientName(data.getName().getAppointment().get(i).getPatient().getName());
                calendarEventItem.setNameAppointment(true);
                calendarEventItem.setStartsAt(data.getName().getAppointment().get(i).getStartsAt());
                calendarEventItem.setTaskType(data.getName().getAppointment().get(i).getType());
                calendarEventItem.setPosition(i);
                calendarEventItem.setTaskTypeName(Utils.getTaskTypeName(data.getName().getAppointment().get(i).getType()));

                calendarSearchList.add(calendarEventItem);
            }
        }
        if(nameReminderCount>0)
        {
            for(int i=0;i<nameReminderCount;i++)
            {
                CalendarItemModel calendarEventItem = new CalendarItemModel();
                calendarEventItem.setPatientName(data.getName().getReminder().get(i).getPatient().getName());
                calendarEventItem.setNameReminder(true);
                calendarEventItem.setStartsAt(data.getName().getReminder().get(i).getStartsAt());
                calendarEventItem.setTaskType(data.getName().getReminder().get(i).getType());
                calendarEventItem.setPosition(i);
                int metricValue = data.getName().getReminder().get(i).getMedication().getDosage().getMetricType();
                String dosage = data.getName().getReminder().get(i).getMedication().getDosage().getValue()+" "+Utils.getMetricNameFromId(metricValue);
                String taskName = data.getName().getReminder().get(i).getMedication().getMedicineName()+" - "+dosage;
                calendarEventItem.setTaskTypeName(taskName);

                calendarSearchList.add(calendarEventItem);
            }
        }
        if(appointmentCount>0)
        {
            for(int i=0;i<appointmentCount;i++)
            {
                CalendarItemModel calendarEventItem = new CalendarItemModel();
                calendarEventItem.setPatientName(data.getAppointment().get(i).getPatient().getName());
                calendarEventItem.setAppointment(true);
                calendarEventItem.setStartsAt(data.getAppointment().get(i).getStartsAt());
                calendarEventItem.setTaskType(data.getAppointment().get(i).getType());
                calendarEventItem.setPosition(i);
                calendarEventItem.setTaskTypeName(Utils.getTaskTypeName(data.getAppointment().get(i).getType()));
                calendarSearchList.add(calendarEventItem);
            }
        }
        if(medicationCount>0)
        {
            for(int i=0;i<medicationCount;i++)
            {
                CalendarItemModel calendarEventItem = new CalendarItemModel();
                calendarEventItem.setPatientName(data.getMedication().get(i).getPatient().getName());
                calendarEventItem.setReminder(true);
                calendarEventItem.setStartsAt(data.getMedication().get(i).getStartsAt());
                calendarEventItem.setTaskType(data.getMedication().get(i).getType());
                calendarEventItem.setPosition(i);
                int metricValue = data.getMedication().get(i).getMedication().getDosage().getMetricType();
                String dosage = data.getMedication().get(i).getMedication().getDosage().getValue()+" "+Utils.getMetricNameFromId(metricValue);
                String taskName = data.getMedication().get(i).getMedication().getMedicineName()+" - "+dosage;
                calendarEventItem.setTaskTypeName(taskName);
                calendarSearchList.add(calendarEventItem);
            }
        }
    }

    private void displaySearchEventList() {
        llMainContainer.setVisibility(View.INVISIBLE);
        Utils.hideKeyboard(this);
        frameSearchContainer.setVisibility(View.VISIBLE);
        calendarEventsAdapter.notifyDataSetChanged();
        if(calendarSearchEventList.size()>0)
           displayNoData();
    }
    public void displayNoData()
    {
        Utils.hideKeyboard(this);
        TextView tvNoDataFound = (TextView)findViewById(R.id.tv_no_datas);
        lvSearchResults.setEmptyView(tvNoDataFound);

    }
    public void closeSearchMenu(View view) {
        edSearchText.setText("");
        Utils.hideKeyboard(this);
        frameSearchContainer.setVisibility(View.GONE);
        llMainContainer.setVisibility(View.VISIBLE);
        llSearchContainer.setVisibility(View.GONE);
    }
    public void openSearchMenu(View view)
    {
        llSearchContainer.setVisibility(View.VISIBLE);
    }
    private void initCalendar()
    {
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        currentMonth+=1;
        dateSelected = currentMonth+"-"+currentDay+"-"+currentYear;

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.setDateSelected(CalendarDay.today(),true);
        tvSelectedDate.setText(R.string.today);

        //materialCalendarView.setCurrentDate(CalendarDay.today());
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                Log.d("Current Month",date.getMonth()+"");
                getDoctorSchedulesByMonthYear(date.getMonth(),date.getYear());
            }
        });
        materialCalendarView.setOnDateChangedListener(this);
        getDoctorSchedulesByMonthYear(currentMonth-1,currentYear);
    }

    private void getDoctorSchedulesByMonthYear(int month, int year) {
        Log.d("SelectedDate",materialCalendarView.getSelectedDate()+"");
        month+=1;
        // progressDialog = LoadingDialog.show(this,"Getting schedules");
        int userId = globalApplication.getUserId();
        String dateSel = month+"-"+year;
        Call<DoctorScheduleBean> scheduleCall = apiInterface.doGetDoctorSchedulesByMonth(userId,dateSel,scheduleFilterType);
        scheduleCall.enqueue(new Callback<DoctorScheduleBean>() {
            @Override
            public void onResponse(Call<DoctorScheduleBean> call, Response<DoctorScheduleBean> response) {
                //progressDialog.dismiss();
                if(response.code()==200)
                {
                    doctorScheduleBean = response.body();
                    if(doctorScheduleBean.getData()==null)
                    {

                    }
                    else
                    {
                        updateCalendarSchedules();
                    }

                }
            }

            @Override
            public void onFailure(Call<DoctorScheduleBean> call, Throwable t) {
                //progressDialog.dismiss();
                Utils.showToast(CalendarScheduleActivity.this,"Unable to fetch schedules "+t.getLocalizedMessage());
            }
        });
    }

    private void updateCalendarSchedules() {


        for(int i=0;i<doctorScheduleBean.getData().size();i++)
        {
//            int type = patientSchedulesList.getData().getAppointments().get(i).getType();
//            Boolean isTypeEnabled = false;
//            if(type==2)
//                isTypeEnabled = enabledTypesArray[2];
//            else if(type==3)
//                isTypeEnabled = enabledTypesArray[3];
//            else if(type==4)
//                isTypeEnabled = enabledTypesArray[4];
//
//            if(isTypeEnabled)
//            {
                String mFromDate = doctorScheduleBean.getData().get(i).getStartsAt();
                String mToDate = doctorScheduleBean.getData().get(i).getEndsAt();
                try {
                    Date dateFromDate = Utils.convertStringToDate2(mFromDate);
                    Date dateToDate = Utils.convertStringToDate2(mToDate);
                    getDatesBetweenTwoDates(dateFromDate,dateToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//            }

        }

        addCalendarMarks();
    }
    private void getDatesBetweenTwoDates(Date fromDate,Date toDate) {

        Calendar cal = Calendar.getInstance();
        CalendarDay calendarDay;
        cal.setTime(fromDate);
        calendarDay = CalendarDay.from(cal);
        remainderDayList.add(calendarDay);
        while (cal.getTime().before(toDate)) {
            cal.add(Calendar.DATE, 1);
            calendarDay = CalendarDay.from(cal);
            remainderDayList.add(calendarDay);
        }

    }
    public void getTodaysSchedule(View view) {
        materialCalendarView.clearSelection();
        materialCalendarView.setDateSelected(CalendarDay.today(),true);
        getSelectedDaySchedules();
    }
    private void addCalendarMarks() {
        //  materialCalendarView.getLayoutParams().width = Utils.getScreenWidth(this);
        int red = ContextCompat.getColor(this,R.color.light_blue);
        materialCalendarView.addDecorator(new EventDecorator(red,remainderDayList));

    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        Date currentDate = new Date();
        if(currentDate.before(date.getDate()) || currentDate.equals(date.getDate()))
        {
            isDateCompleted = true;
            tvUpdateOfficeHours.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
        }
        else
        {
            isDateCompleted = false;
            tvUpdateOfficeHours.setTextColor(ContextCompat.getColor(this,R.color.light_grey_3));
        }

        if(Utils.isToday(date.getDate()))
        {
            tvSelectedDate.setText(R.string.today);
        }
        else
        {
            tvSelectedDate.setText(Utils.getMonthFromNumber(date.getMonth())+" "+date.getDay()+" "+date.getYear());
        }
        getSelectedDaySchedules();
    }

    private void getSelectedDaySchedules() {
        progressDialog = LoadingDialog.show(this,"Getting schedules");
        int userId = globalApplication.getUserId();
        int month = materialCalendarView.getSelectedDate().getMonth();
        month += 1;
        int day = materialCalendarView.getSelectedDate().getDay();
        int year = materialCalendarView.getSelectedDate().getYear();
         dateSelected = month+"-"+day+"-"+year;
        Call<DoctorScheduleBean> scheduleCall = apiInterface.doGetDoctorSchedules(userId,dateSelected);
        scheduleCall.enqueue(new Callback<DoctorScheduleBean>() {
            @Override
            public void onResponse(Call<DoctorScheduleBean> call, Response<DoctorScheduleBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    selectedDocotorSchedule = response.body();
                    if(selectedDocotorSchedule.getData().size()==0)
                    {
                        llScheduledTaskContainer.removeAllViews();
                        tvNoEvents.setVisibility(View.VISIBLE);
                    }
                    else
                        displayPatientSchedules();
                }
                else
                    Log.d("ScheduleResponseSucc",response.code()+"");
            }

            @Override
            public void onFailure(Call<DoctorScheduleBean> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ScheduleResponse",t.getLocalizedMessage());
            }
        });
    }

    private void displayPatientSchedules()
    {
        tvNoEvents.setVisibility(View.GONE);
        llScheduledTaskContainer.removeAllViews();
        View childView;
        int appointMentSize =  selectedDocotorSchedule.getData().size();

        if(appointMentSize!=0)
        {
            for(int i=0;i<appointMentSize;i++)
            {
                int type = selectedDocotorSchedule.getData().get(i).getType();

                final int currentPosition = i;

                    childView = getLayoutInflater().inflate(R.layout.layout_doc_schedule_slot,null);
                    TextView tvScheduleName = (TextView)childView.findViewById(R.id.tv_schedule_name);
                    TextView tvScheduleTime = (TextView)childView.findViewById(R.id.tv_schedule_time);
                    TextView tvScheduleType = (TextView)childView.findViewById(R.id.tv_schedule_type);


                    String mDoctorName = selectedDocotorSchedule.getData().get(i).getPatient().getName();
                     tvScheduleName.setText(mDoctorName);
                    if(type==2)
                    {


                        tvScheduleType.setText(getString(R.string.call));

                    }
                    else if(type==3)
                    {

                        tvScheduleType.setText(getString(R.string.virtual_visit));

                    }
                    else if(type==4)
                    {

                        tvScheduleType.setText(getString(R.string.physical_visit));

                    }
                    String appointmentTime = doctorScheduleBean.getData().get(i).getStartsAt();
                    tvScheduleTime.setText(appointmentTime.substring(11));
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            isAppointmentSelected = true;
//                            showScheduleDetail(currentPosition);
                        }
                    });
                    llScheduledTaskContainer.addView(childView);



            }
        }



    }

    public void updatOfficeHours(View view) {
        if(isDateCompleted)
        {
            Intent intent = new Intent(this, UpdateOfficeHoursActivity.class);
            intent.putExtra(ValueUtils.DateForSlot,dateSelected);
            startActivity(intent);
        }

    }

    public void addAppointments(View view) {
      Intent intent = new Intent(this, DoctorSelectAppointmentActivity.class);
        intent.putExtra(ValueUtils.DateForSlot,dateSelected);
        startActivity(intent);
    }

    public void closeMenu(View view) {
        circleMenu.close(true);
    }

    @Override
    public void onBackPressed() {

    }
}
