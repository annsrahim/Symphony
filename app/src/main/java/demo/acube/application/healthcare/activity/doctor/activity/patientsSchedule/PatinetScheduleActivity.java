package demo.acube.application.healthcare.activity.doctor.activity.patientsSchedule;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.mySchedule.EventDecorator;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.patientScheduleBean.PatientScheduleBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class PatinetScheduleActivity extends AppCompatActivity implements OnDateSelectedListener {

    public MaterialCalendarView materialCalendarView;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    private List<Integer> ltMonthsToCheck;
    List<CalendarDay> remainderDayList = new ArrayList<CalendarDay>();
    public GlobalApplication globalApplication;
    PatientScheduleBean patientSchedulesList;
    ListView lvScheduledTasks;
    public PatientScheduleBean selectedPatientSchedulesList;
    Integer scheduleFilterType = null;
    LinearLayout llScheduledTaskContainer;
    TextView tvNoEvents;
    Boolean[] enabledTypesArray = new Boolean[5];
    public Boolean isAppointmentSelected = true;
    public int selectedPosition = 0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        userId = getIntent().getExtras().getInt(ValueUtils.USER_ID);
        initUI();
        initCalendar();
        initToolbar();
        getSelectedDaySchedules();


    }

    private void getSelectedDaySchedules()
    {
        progressDialog = LoadingDialog.show(this,"Getting schedules");

        int month = materialCalendarView.getSelectedDate().getMonth();
        month += 1;
        int day = materialCalendarView.getSelectedDate().getDay();
        int year = materialCalendarView.getSelectedDate().getYear();
        String dateSelected = month+"-"+day+"-"+year;
        Call<PatientScheduleBean> scheduleCall = apiInterface.doGetPatientSchedules(userId,dateSelected);
        scheduleCall.enqueue(new Callback<PatientScheduleBean>() {
            @Override
            public void onResponse(Call<PatientScheduleBean> call, Response<PatientScheduleBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    selectedPatientSchedulesList = response.body();
                    if(selectedPatientSchedulesList.getData().getReminders().size()==0 && selectedPatientSchedulesList.getData().getAppointments().size()==0)
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
            public void onFailure(Call<PatientScheduleBean> call, Throwable t) {
                progressDialog.dismiss();
             
            }
        });
    }

    private void displayPatientSchedules()
    {
        tvNoEvents.setVisibility(View.GONE);
        llScheduledTaskContainer.removeAllViews();
        View childView;
        int appointMentSize =  selectedPatientSchedulesList.getData().getAppointments().size();
        int remainderSize = selectedPatientSchedulesList.getData().getReminders().size();
        if(appointMentSize!=0)
        {
            for(int i=0;i<appointMentSize;i++)
            {
                int type = selectedPatientSchedulesList.getData().getAppointments().get(i).getType();

                Boolean isTypeEnabled = false;
                if(type==2)
                    isTypeEnabled = enabledTypesArray[2];
                else if(type==3)
                    isTypeEnabled = enabledTypesArray[3];
                else if(type==4)
                    isTypeEnabled = enabledTypesArray[4];

                final int currentPosition = i;
                if(isTypeEnabled)
                {
                    childView = getLayoutInflater().inflate(R.layout.layout_schedule_slot,null);
                    TextView tvScheduleName = (TextView)childView.findViewById(R.id.tv_schedule_name);
                    TextView tvScheduleTime = (TextView)childView.findViewById(R.id.tv_schedule_time);
                    ImageView ivScheduleTypeIcon = (ImageView)childView.findViewById(R.id.iv_schedule_type_icon);

                    String mDoctorName = selectedPatientSchedulesList.getData().getAppointments().get(i).getDoctor().getName();
                    if(type==2)
                    {

                        tvScheduleName.setText("Call with "+mDoctorName);
                        ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_home_call));

                    }
                    else if(type==3)
                    {

                        tvScheduleName.setText("Virtual Visit with "+mDoctorName);
                        ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_home_virtual_visit));
                    }
                    else if(type==4)
                    {

                        tvScheduleName.setText("Physical Visit with "+mDoctorName);
                        ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_physical_visit));
                    }
                    String appointmentTime = selectedPatientSchedulesList.getData().getAppointments().get(i).getStartsAt();
                    tvScheduleTime.setText(appointmentTime.substring(11));
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isAppointmentSelected = true;
                            showScheduleDetail(currentPosition);
                        }
                    });
                    llScheduledTaskContainer.addView(childView);
                }


            }
        }
        if(remainderSize!=0)
        {
            for(int i=0;i<remainderSize;i++)
            {
                final int currentPosition = i;
                int type = selectedPatientSchedulesList.getData().getReminders().get(i).getType();
                Boolean isTypeEnabled = false;
                if(type==0)
                    isTypeEnabled = enabledTypesArray[0];
                else
                    isTypeEnabled = enabledTypesArray[1];

                if(isTypeEnabled)
                {
                    childView = getLayoutInflater().inflate(R.layout.layout_schedule_slot,null);
                    TextView tvScheduleName = (TextView)childView.findViewById(R.id.tv_schedule_name);
                    TextView tvScheduleTime = (TextView)childView.findViewById(R.id.tv_schedule_time);
                    ImageView ivScheduleTypeIcon = (ImageView)childView.findViewById(R.id.iv_schedule_type_icon);
                    tvScheduleName.setText(selectedPatientSchedulesList.getData().getReminders().get(i).getNotes());
                    if(type==0)
                    {
                        String mMedicineName = selectedPatientSchedulesList.getData().getReminders().get(i).getMedication().getMedicineName();
                        tvScheduleName.setText("Take "+mMedicineName);
                        ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_medication_name));
                    }
                    else
                    {
                        String mDeviceName = selectedPatientSchedulesList.getData().getReminders().get(i).getDevice().getName();
                        tvScheduleName.setText("Measure "+mDeviceName);
                        ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_health));
                    }
                    String startTime = selectedPatientSchedulesList.getData().getReminders().get(i).getTimes().get(0);
                    tvScheduleTime.setText(startTime);
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isAppointmentSelected  = false;
                            showScheduleDetail(currentPosition);
                        }
                    });
                    llScheduledTaskContainer.addView(childView);
                }


            }
        }


    }

    public void showScheduleDetail(int pos)
    {
        selectedPosition = pos;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PatientScheduleDetailFragment scheduleDetailFragment = PatientScheduleDetailFragment.newInsatnce();
        scheduleDetailFragment.show(ft,"scheduleDetailFragment");
    }

    private void initCalendar()
    {
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.setDateSelected(CalendarDay.today(),true);
        //materialCalendarView.setCurrentDate(CalendarDay.today());
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                Log.d("Current Month",date.getMonth()+"");
                getPatientSchedulesByMonthYear(date.getMonth(),date.getYear());
            }
        });
        materialCalendarView.setOnDateChangedListener(this);
        getPatientSchedulesByMonthYear(currentMonth,currentYear);
    }



    private void getPatientSchedulesByMonthYear(int month,int year)
    {
        Log.d("SelectedDate",materialCalendarView.getSelectedDate()+"");
        month+=1;
       // progressDialog = LoadingDialog.show(this,"Getting schedules");

        String dateSel = month+"-"+year;
        Call<PatientScheduleBean> scheduleCall = apiInterface.doGetPatientSchedulesByMonth(userId,dateSel,scheduleFilterType);
        scheduleCall.enqueue(new Callback<PatientScheduleBean>() {
            @Override
            public void onResponse(Call<PatientScheduleBean> call, Response<PatientScheduleBean> response) {
                //progressDialog.dismiss();
                if(response.code()==200)
                {
                    patientSchedulesList = response.body();
                    if(patientSchedulesList.getData().getAppointments()==null && patientSchedulesList.getData().getReminders()==null)
                    {

                    }
                    else
                    {
                        updateCalendarSchedules();
                    }

                }
            }

            @Override
            public void onFailure(Call<PatientScheduleBean> call, Throwable t) {
                   //progressDialog.dismiss();
                Utils.showToast(PatinetScheduleActivity.this,"Unable to fetch schedules "+t.getLocalizedMessage());
            }
        });

    }

    private void updateCalendarSchedules()
    {
        int appointMentSize =  patientSchedulesList.getData().getAppointments().size();
        int remainderSize = patientSchedulesList.getData().getReminders().size();
        for(int i=0;i<appointMentSize;i++)
        {
            int type = patientSchedulesList.getData().getAppointments().get(i).getType();
            Boolean isTypeEnabled = false;
            if(type==2)
                isTypeEnabled = enabledTypesArray[2];
            else if(type==3)
                isTypeEnabled = enabledTypesArray[3];
            else if(type==4)
                isTypeEnabled = enabledTypesArray[4];

            if(isTypeEnabled)
            {
                String mFromDate = patientSchedulesList.getData().getAppointments().get(i).getStartsAt();
                String mToDate = patientSchedulesList.getData().getAppointments().get(i).getEndsAt();
                try {
                    Date dateFromDate = Utils.convertStringToDate2(mFromDate);
                    Date dateToDate = Utils.convertStringToDate2(mToDate);
                    getDatesBetweenTwoDates(dateFromDate,dateToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        for(int i=0;i<remainderSize;i++)
        {

            int type = patientSchedulesList.getData().getReminders().get(i).getType();
            Boolean isTypeEnabled = false;
            if(type==0)
                isTypeEnabled = enabledTypesArray[0];
            else
                isTypeEnabled = enabledTypesArray[1];

            if(isTypeEnabled)
            {
                String mFromDate = patientSchedulesList.getData().getReminders().get(i).getStartsAt();
                String mToDate = patientSchedulesList.getData().getReminders().get(i).getEndsAt();
                try {
                    Date dateFromDate = Utils.convertStringToDate2(mFromDate);
                    Date dateToDate = Utils.convertStringToDate2(mToDate);
                    getDatesBetweenTwoDates(dateFromDate,dateToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

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

    private void initUI() {
        llScheduledTaskContainer = (LinearLayout)findViewById(R.id.ll_scheduled_task_container);
        tvNoEvents = (TextView)findViewById(R.id.tv_no_events);

        Arrays.fill(enabledTypesArray, Boolean.TRUE);//Enable all types while initializing

    }



    private void addCalendarMarks() {
          //  materialCalendarView.getLayoutParams().width = Utils.getScreenWidth(this);
        int red = ContextCompat.getColor(this,R.color.toolbar_blue);
        materialCalendarView.addDecorator(new EventDecorator(red,remainderDayList));

    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this,R.color.toolbar_blue));
        tvToolbarBack.setTextColor(ContextCompat.getColor(this,R.color.toolbar_blue));
        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarBack);
        tvToolbarTitle.setText(getString(R.string.patients_schedule));
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d("DateSelcted",date.getDate().toString());
        getSelectedDaySchedules();
    }

    public void getTodaysSchedule(View view) {
        materialCalendarView.clearSelection();
        materialCalendarView.setDateSelected(CalendarDay.today(),true);
        getSelectedDaySchedules();
    }

    public void showFilterTypes(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DoctorCalendarFilterTypeFragment calendarFilterTypeFragment = DoctorCalendarFilterTypeFragment.newInsatnce().newInsatnce();
        calendarFilterTypeFragment.show(ft,"calendarFilterDialog");
    }

    public void updateFilterInCalendars()
    {
        displayPatientSchedules();
        remainderDayList.clear();
        materialCalendarView.removeDecorators();
        updateCalendarSchedules();
    }
}
