package demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.dashboard.PatientHomeActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorList.DoctorsListActiviy;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValidationUtils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.doctorsSlot.DoctorsAvailabilitySlotBean;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class CallAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView tvPhysicianName;
        private String mSelectedPhysicianName = "";
        private int mSelectedPhysicianId;
    private DatePickerDialog datePicker;
    private int callYear,callMonth,callDay;
    private TextView tvCallDate,tvTimeSlotValue,tvAlertTime,tvAppoinementHeader;
    private APIInterface apiInterface;
    private ProgressDialog progressDialog;
    private GlobalApplication globalApplication;
    public DoctorsAvailabilitySlotBean doctorsAvailabilitySlotBean;
    private Boolean slotAvailability = false;
    public int rejectedSlotCount = 0;
    public Integer selectedAppointmentSlot;
    public int mAlertNotifyType = 1;
    public int mAlertNotifyMinute = 0;
    public EditText edNotesHere;
    private Integer appointmentType;
    private String isComingFrom = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_appointment);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.disableKeyboardPopup(this);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Intent intent = getIntent();
        appointmentType = intent.getIntExtra(ValueUtils.AppointmentType,0);
        isComingFrom = intent.getStringExtra(ValueUtils.isFrom);
        initToolbar();
        initUI();
       getDoctorsAvailablilitySlots();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_done)
        {
            saveAppointment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAppointment()
    {
        if(isValid())
        {
            createAppointmentActivity();
        }
    }

    private void createAppointmentActivity()
    {
        progressDialog = LoadingDialog.show(this,"Creating Remainder...");
        UserDetailsBean userDetailBean = globalApplication.getUserDetailsBean();
        int patientId = userDetailBean.getData().getUserId();
        int doctorId = userDetailBean.getData().getPrimaryDoctor().getUserId();
        String selectedDate = callMonth+"-"+callDay+"-"+callYear;
        String timeStart = doctorsAvailabilitySlotBean.getData().get(selectedAppointmentSlot).getSlot().getStart();
        String timeEnd = doctorsAvailabilitySlotBean.getData().get(selectedAppointmentSlot).getSlot().getEnd();
        String notes = edNotesHere.getText().toString();

        int creatorId = patientId;

        Call<Success> createAppointmentCall = apiInterface.doAddCallAPpointment(patientId,
                                                                                doctorId,
                                                                                selectedDate,
                                                                                timeStart,
                                                                                timeEnd,
                                                                                notes,
                                                                                mAlertNotifyMinute,
                                                                                appointmentType,
                                                                                creatorId);
        createAppointmentCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    Intent intent = new Intent(CallAppointmentActivity.this, PatientHomeActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Utils.showToast(CallAppointmentActivity.this,"Something went wrong please try again...");

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(CallAppointmentActivity.this,t.getLocalizedMessage()+"");
            }
        });
    }

    private boolean isValid() {
        if(selectedAppointmentSlot==null)
        {
            Utils.showToast(this,"Please select an appointment");
            return false;
        }
        else
        {
            return true;
        }

    }

    private void getDoctorsAvailablilitySlots()
    {
        progressDialog = LoadingDialog.show(this,"Fetching details");
        final String dateForSlot = callMonth+"-"+callDay+"-"+callYear;
        Call<DoctorsAvailabilitySlotBean> docSlotCall = apiInterface.doGetDoctorAvailalitySlots(mSelectedPhysicianId,dateForSlot);
        docSlotCall.enqueue(new Callback<DoctorsAvailabilitySlotBean>() {
            @Override
            public void onResponse(Call<DoctorsAvailabilitySlotBean> call, Response<DoctorsAvailabilitySlotBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    doctorsAvailabilitySlotBean = response.body();
                    try {
                        if(Utils.isDateToday(dateForSlot))
                                checkSlotTimeValid();
                        else
                        {
                            //checkSlotTimeValid();
                            slotAvailability = true;
                            tvTimeSlotValue.setText("");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    slotAvailability = false;
                   slotUnavailable();
                }
            }

            @Override
            public void onFailure(Call<DoctorsAvailabilitySlotBean> call, Throwable t) {
               progressDialog.dismiss();
                Utils.showToast(CallAppointmentActivity.this,"Unable to get available slots "+t.getLocalizedMessage());
            }
        });
    }
    public void slotUnavailable()
    {
        tvTimeSlotValue.setText(R.string.no_slots_available);
        tvTimeSlotValue.setTextColor(ContextCompat.getColor(this,R.color.dark_red));
    }
    private void checkSlotTimeValid()
    {

        for(int i=0;i<doctorsAvailabilitySlotBean.getData().size();i++)
        {
            String startTime = doctorsAvailabilitySlotBean.getData().get(i).getSlot().getStart();
            String endTime = doctorsAvailabilitySlotBean.getData().get(i).getSlot().getEnd();
            try {
                startTime = Utils.convert12hrTo24hr(startTime);
                endTime = Utils.convert12hrTo24hr(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
             Log.d("Converted Time",startTime+" "+endTime);
            if(!Utils.checkCurrentTimeBetweenRange(startTime,endTime))
               rejectedSlotCount++;
            else
                break;
        }
        if(rejectedSlotCount==doctorsAvailabilitySlotBean.getData().size())
        {
            slotAvailability = false;
            slotUnavailable();

        }
        else
        {
            slotAvailability= true;
            tvTimeSlotValue.setText(R.string.select_a_time_slot);
        }

    }

    private void initUI() {

        mSelectedPhysicianName = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getName();
        mSelectedPhysicianId = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId();
        globalApplication.setPatSelectedCallerName(mSelectedPhysicianName);
        globalApplication.setPatSelectedCallerId(mSelectedPhysicianId);
        tvPhysicianName = (TextView)findViewById(R.id.tv_physician_name);
        tvCallDate = (TextView)findViewById(R.id.tv_call_date);
        tvTimeSlotValue = (TextView)findViewById(R.id.tv_time_slot_value);
        tvTimeSlotValue.setText(R.string.select_a_time_slot);
        tvAppoinementHeader = (TextView)findViewById(R.id.tv_appointement_type_header);
        if(appointmentType==2)
            tvAppoinementHeader.setText(R.string.Call);
        else if(appointmentType==3)
            tvAppoinementHeader.setText(R.string.VIRTUAL_VISIT);
        else if(appointmentType==4)
            tvAppoinementHeader.setText(R.string.PHYSICAL_VISIT);
        String isFrom = "";
        Intent intent = getIntent();
        if(intent!=null)
            isFrom = intent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null && isFrom.equals(ValueUtils.MySchedules))
        {
            CalendarDay calendarDay = globalApplication.getScheduledDate();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(calendarDay.getDate());

            callYear = startCal.get(Calendar.YEAR);
            callMonth = startCal.get(Calendar.MONTH)+1;
            callDay = startCal.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            callYear = Calendar.getInstance().get(Calendar.YEAR);
            callMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
            callDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }


        tvAlertTime = (TextView)findViewById(R.id.tv_alert_time);
        updateDateValue();
        tvPhysicianName.setText(String.format("Dr.%s", mSelectedPhysicianName));
        edNotesHere  = (EditText)findViewById(R.id.ed_notes_here);
        ValidationUtils.allowOnlyAlphabets(edNotesHere);

    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Set Details");
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
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

    public void doShowDoctorsList(View view) {
        Intent intent = new Intent(this, DoctorsListActiviy.class);
        intent.putExtra(ValueUtils.isFrom,ValueUtils.CALL_APPOINTMENT_ACTIVITY);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updatePhysicianName();
    }

    private void updatePhysicianName()
    {
        mSelectedPhysicianName = globalApplication.getPatSelectedCallerName();
        mSelectedPhysicianId = globalApplication.getPatSelectedCallerId();
        tvPhysicianName.setText(String.format("Dr.%s", mSelectedPhysicianName));
    }

    public void selectCallDate(View view) {

        datePicker = new DatePickerDialog(this,this,callYear,(callMonth-1),callDay);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       if(view.isShown())
       {
           callYear = year;
           callMonth = month+1;
           callDay = dayOfMonth;
           rejectedSlotCount = 0;
           selectedAppointmentSlot = null;
           getDoctorsAvailablilitySlots();
           updateDateValue();
       }


    }

    private void updateDateValue()
    {
        String callDate = convertDateToUserFormat(callYear,callMonth,callDay);
        tvCallDate.setText(callDate);
    }
    public String convertDateToUserFormat(int year,int month,int dayOfMonth)
    {
        String rawDate = year+"-"+month+"-"+dayOfMonth;
        SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd, yyyy",Locale.US);
        try {
            Date callDate = Utils.convertStringToDate(rawDate);
            return postFormater.format(callDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }



    }

    public void showAvailableTimeSlot(View view) {
        if(slotAvailability)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            TimeSlotFragment alDosFrag = TimeSlotFragment.newInstance();
            alDosFrag.show(ft,"timeSlotDialog");
        }

    }

    public void showAlertRemainderDialog(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CallAlertRemainderDialogFragment alRemFrag = CallAlertRemainderDialogFragment.newInstance();
        alRemFrag.show(ft,"alertRemainderDialog");
    }

    public void updateSelectedTimeSlot(int position)
    {
        selectedAppointmentSlot = position;
        String startTime = doctorsAvailabilitySlotBean.getData().get(position).getSlot().getStart();
        String endTime = doctorsAvailabilitySlotBean.getData().get(position).getSlot().getEnd();
        tvTimeSlotValue.setText(startTime+" - "+endTime);
    }
    public String getSelectedDate()
    {
        return convertDateToUserFormat(callYear,callMonth,callDay);
    }

    public void updateAlertTime()
    {
        switch (mAlertNotifyType)
        {
            case -1:
                tvAlertTime.setText(getString(R.string.None));
                mAlertNotifyMinute = 0;
                break;
            case 0:
                tvAlertTime.setText(getString(R.string.lbl_alert_at_time_event));
                mAlertNotifyMinute = 0;
                break;
            case 1:
                tvAlertTime.setText(getString(R.string.lbl_alert_5_min_before));
                mAlertNotifyMinute = 5;
                break;
            case 2:
                tvAlertTime.setText(getString(R.string.lbl_alert_10_min_before));
                mAlertNotifyMinute = 10;
                break;
            case 3:
                tvAlertTime.setText(getString(R.string.lbl_alert_15_min_before));
                mAlertNotifyMinute = 15;
                break;
            case 4:
                tvAlertTime.setText(getString(R.string.lbl_alert_30_min_before));
                mAlertNotifyMinute = 30;
                break;
            case 5:
                tvAlertTime.setText(getString(R.string.lbl_alert_1_hour_before));
                mAlertNotifyMinute = 60;
                break;
            case 6:
                tvAlertTime.setText(getString(R.string.lbl_alert_2_hours_before));
                mAlertNotifyMinute = 60*2;
                break;
            case 7:
                tvAlertTime.setText(getString(R.string.lbl_alert_1_day_before));
                mAlertNotifyMinute = 60*(24*1);
                break;
            case 8:
                tvAlertTime.setText(getString(R.string.lbl_alert_2_days_before));
                mAlertNotifyMinute = 60*(24*2);
                break;
            case 9:
                tvAlertTime.setText(getString(R.string.lbl_alert_1_week_before));
                mAlertNotifyMinute = 60*(24*7);
                break;

        }
    }
}
