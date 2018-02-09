package demo.acube.application.healthcare.activity.doctor.activity.editOfficeHours;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.SetAppointmentSlotsFragment;
import demo.acube.application.healthcare.activity.doctor.models.officeHours.Data;
import demo.acube.application.healthcare.activity.doctor.models.officeHours.OfficeHoursBean;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class EditOfficeHoursActivity extends AppCompatActivity implements View.OnClickListener {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    private String mSelectedTime;
    Data officeHours;
    private int selectedDay = 0;
    private boolean isStartTime = true;
    TimePickerDialog timePickerDialog;
    //Switches to on off the day
    @BindView(R.id.switch_monday) Switch switchMonday;
    @BindView(R.id.switch_tuesday) Switch switchTuesday;
    @BindView(R.id.switch_wednesday) Switch switchWednesday;
    @BindView(R.id.switch_thursday) Switch switchThursday;
    @BindView(R.id.switch_friday) Switch switchFriday;
    @BindView(R.id.switch_saturday) Switch switchSaturday;
    @BindView(R.id.switch_sunday) Switch switchSunday;

    //Start time Layout
    @BindView(R.id.rl_monday_start_time) RelativeLayout rlMondayStartTime;
    @BindView(R.id.rl_tuesday_start_time) RelativeLayout rlTuesdayStartTime;
    @BindView(R.id.rl_wednesday_start_time) RelativeLayout rlWednesdayStartTime;
    @BindView(R.id.rl_thursday_start_time) RelativeLayout rlThursdayStartTime;
    @BindView(R.id.rl_friday_start_time) RelativeLayout rlFridayStartTime;
    @BindView(R.id.rl_saturday_start_time) RelativeLayout rlSaturdayStartTime;
    @BindView(R.id.rl_sunday_start_time) RelativeLayout rlSundayStartTime;

    //TextView Start Time
    @BindView(R.id.tv_monday_start_time) TextView tvMondayStartTime;
    @BindView(R.id.tv_tuesday_start_time) TextView tvTuesdayStartTime;
    @BindView(R.id.tv_wednesday_start_time) TextView tvWednesdayStartTime;
    @BindView(R.id.tv_thursday_start_time) TextView tvThursdayStartTime;
    @BindView(R.id.tv_friday_start_time) TextView tvFridayStartTime;
    @BindView(R.id.tv_saturday_start_time) TextView tvSaturdayStartTime;
    @BindView(R.id.tv_sunday_start_time) TextView tvSundayStartTime;

    //End time Layout
    @BindView(R.id.rl_monday_end_time) RelativeLayout rlMondayEndTime;
    @BindView(R.id.rl_tuesday_end_time) RelativeLayout rlTuesdayEndTime;
    @BindView(R.id.rl_wednesday_end_time) RelativeLayout rlWednesdayEndTime;
    @BindView(R.id.rl_thursday_end_time) RelativeLayout rlThursdayEndTime;
    @BindView(R.id.rl_friday_end_time) RelativeLayout rlFridayEndTime;
    @BindView(R.id.rl_saturday_end_time) RelativeLayout rlSaturdayEndTime;
    @BindView(R.id.rl_sunday_end_time) RelativeLayout rlSundayEndTime;

    //TextView End Time
    @BindView(R.id.tv_monday_End_time) TextView tvMondayEndTime;
    @BindView(R.id.tv_tuesday_End_time) TextView tvTuesdayEndTime;
    @BindView(R.id.tv_wednesday_End_time) TextView tvWednesdayEndTime;
    @BindView(R.id.tv_thursday_End_time) TextView tvThursdayEndTime;
    @BindView(R.id.tv_friday_End_time) TextView tvFridayEndTime;
    @BindView(R.id.tv_saturday_End_time) TextView tvSaturdayEndTime;
    @BindView(R.id.tv_sunday_End_time) TextView tvSundayEndTime;

    //Set Apointment
    @BindView(R.id.rl_monday_set_appointment) RelativeLayout rlMondaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_tuedsay_set_appointment) RelativeLayout rlTuesdaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_wednesday_set_appointment) RelativeLayout rlWednesdaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_thursday_set_appointment) RelativeLayout rlThursdaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_friday_set_appointment) RelativeLayout rlFridaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_saturday_set_appointment) RelativeLayout rlSaturdaySetAppointmentRelativeLayout;
    @BindView(R.id.rl_sunday_set_appointment) RelativeLayout rlSundaySetAppointmentRelativeLayout;

    //Body Containers to visible and enable depends on switch

    @BindView(R.id.ll_monday_body_container) LinearLayout llMondayBodyContainer;
    @BindView(R.id.ll_tuesday_body_container) LinearLayout llTuesdayBodyContainer;
    @BindView(R.id.ll_wednesday_body_container) LinearLayout llWednesdayBodyContainer;
    @BindView(R.id.ll_thursday_body_container) LinearLayout llTursdayBodyContainer;
    @BindView(R.id.ll_friday_body_container) LinearLayout llFridayBodyContainer;
    @BindView(R.id.ll_saturday_body_container) LinearLayout llSaturdayBodyContainer;
    @BindView(R.id.ll_sunday_body_container) LinearLayout llSundayBodyContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_office_hours);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        ButterKnife.bind(this);
        initToolbar();
        initUi();
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
       getOfficeHours();
    }

    private void initToolbar()
    {

    }

    private void initUi() {
        rlMondaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlTuesdaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlWednesdaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlThursdaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlFridaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlSaturdaySetAppointmentRelativeLayout.setOnClickListener(this);
        rlSundaySetAppointmentRelativeLayout.setOnClickListener(this);

        tvMondayStartTime.setOnClickListener(this);
        tvTuesdayStartTime.setOnClickListener(this);
        tvWednesdayStartTime.setOnClickListener(this);
        tvThursdayStartTime.setOnClickListener(this);
        tvFridayStartTime.setOnClickListener(this);
        tvSaturdayStartTime.setOnClickListener(this);
        tvSundayStartTime.setOnClickListener(this);

        tvMondayEndTime.setOnClickListener(this);
        tvTuesdayEndTime.setOnClickListener(this);
        tvWednesdayEndTime.setOnClickListener(this);
        tvThursdayEndTime.setOnClickListener(this);
        tvFridayEndTime.setOnClickListener(this);
        tvSaturdayEndTime.setOnClickListener(this);
        tvSundayEndTime.setOnClickListener(this);

    }

    private void getOfficeHours()
    {
        progressDialog = LoadingDialog.show(this,getString(R.string.loading));
        Call<OfficeHoursBean> officeHoursBeanCall = apiInterface.getOfficeHours(globalApplication.getUserId());
        officeHoursBeanCall.enqueue(new Callback<OfficeHoursBean>() {
            @Override
            public void onResponse(Call<OfficeHoursBean> call, Response<OfficeHoursBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    officeHours = response.body().getData();
                    displayDatas();
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<OfficeHoursBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void displayDatas() {
        if(officeHours.getMonday()==null)
        {
            llMondayBodyContainer.setVisibility(View.GONE);
            switchMonday.setChecked(false);
        }
        else
        {
            switchMonday.setChecked(true);
            tvMondayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getMonday().getStart()));
            tvMondayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getMonday().getEnd()));
            llMondayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getTuesday()==null)
        {
            switchTuesday.setChecked(false);
            llTuesdayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchTuesday.setChecked(true);
            tvTuesdayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getTuesday().getStart()));
            tvTuesdayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getTuesday().getEnd()));
            llTuesdayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getWednesday()==null)
        {
            switchWednesday.setChecked(false);
            llWednesdayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchWednesday.setChecked(true);
            tvWednesdayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getWednesday().getStart()));
            tvWednesdayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getWednesday().getEnd()));
            llWednesdayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getThursday()==null)
        {
            switchThursday.setChecked(false);
            llTursdayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchThursday.setChecked(true);
            tvThursdayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getThursday().getStart()));
            tvThursdayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getThursday().getEnd()));
            llTursdayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getFriday()==null)
        {
            switchFriday.setChecked(false);
            llFridayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchFriday.setChecked(true);
            tvFridayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getFriday().getStart()));
            tvFridayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getFriday().getEnd()));
            llFridayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getSaturday()==null)
        {
            switchSaturday.setChecked(false);
            llSaturdayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchSaturday.setChecked(true);
            tvSaturdayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getSaturday().getStart()));
            tvSaturdayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getSaturday().getEnd()));
            llSaturdayBodyContainer.setVisibility(View.VISIBLE);
        }
        if(officeHours.getSunday()==null)
        {
            switchSunday.setChecked(false);
            llSundayBodyContainer.setVisibility(View.GONE);
        }
        else
        {
            switchSunday.setChecked(true);
            tvSundayStartTime.setText(Utils.convert24HourTo12Hour(officeHours.getSunday().getStart()));
            tvSundayEndTime.setText(Utils.convert24HourTo12Hour(officeHours.getSunday().getEnd()));
            llSundayBodyContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_monday_set_appointment:
                    selectedDay = 0;
                    showAppointmentSlot();
                break;
            case R.id.rl_tuedsay_set_appointment:
                selectedDay = 1;
                showAppointmentSlot();
                break;
            case R.id.rl_wednesday_set_appointment:
                selectedDay = 2;
                showAppointmentSlot();
                break;
            case R.id.rl_thursday_set_appointment:
                selectedDay = 3;
                showAppointmentSlot();
                break;
            case R.id.rl_friday_set_appointment:
                selectedDay = 4;
                showAppointmentSlot();
                break;
            case R.id.rl_saturday_set_appointment:
                selectedDay = 5;
                showAppointmentSlot();
                break;
            case R.id.rl_sunday_set_appointment:
                selectedDay = 6;
                showAppointmentSlot();
                break;

            //Days Start Time Picker

            case R.id.tv_monday_start_time:
                    selectedDay = 0;
                    isStartTime = true;
                    showTimePicker();
                break;
            case R.id.tv_tuesday_start_time:
                selectedDay = 1;
                isStartTime = true;
                showTimePicker();
                break;
            case R.id.tv_wednesday_start_time:
                selectedDay = 2;
                isStartTime = true;
                showTimePicker();
                break;
            case R.id.tv_thursday_start_time:
                selectedDay = 3;
                isStartTime = true;
                showTimePicker();
                break;
            case R.id.tv_friday_start_time:
                selectedDay = 4;
                isStartTime = true;
                showTimePicker();
                break;
            case R.id.tv_saturday_start_time:
                selectedDay = 5;
                isStartTime = true;
                showTimePicker();
                break;

            case R.id.tv_sunday_start_time:
                selectedDay = 6;
                isStartTime = true;
                showTimePicker();
                break;

            //Days EndTime Picker

            case R.id.tv_monday_End_time:
                selectedDay = 0;
                isStartTime = false;
                showTimePicker();
                break;
            case R.id.tv_tuesday_End_time:
                selectedDay = 1;
                isStartTime = false;
                showTimePicker();
                break;
            case R.id.tv_wednesday_End_time:
                selectedDay = 2;
                isStartTime = false;
                showTimePicker();
                break;
            case R.id.tv_thursday_End_time:
                selectedDay = 3;
                isStartTime = false;
                showTimePicker();
                break;
            case R.id.tv_friday_End_time:
                selectedDay = 4;
                isStartTime = false;
                showTimePicker();
                break;
            case R.id.tv_saturday_End_time:
                selectedDay = 5;
                isStartTime = false;
                showTimePicker();
                break;

            case R.id.tv_sunday_End_time:
                selectedDay = 6;
                isStartTime = false;
                showTimePicker();
                break;



        }
    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mSelectedTime = hourOfDay+":"+minute;
                updateTimeinSlot();
            }
        },hour,minute,false);
        if (isStartTime)
            timePickerDialog.setTitle("Select Start Time");
        else
            timePickerDialog.setTitle("Select End Time");
        timePickerDialog.show();
    }

    private void updateTimeinSlot() {
        switch(selectedDay)
        {
            case 0:
                     if(isStartTime)
                         tvMondayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                    else
                        tvMondayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 1:
                if(isStartTime)
                    tvTuesdayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvTuesdayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 2:
                if(isStartTime)
                    tvWednesdayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvWednesdayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 3:
                if(isStartTime)
                    tvThursdayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvThursdayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 4:
                if(isStartTime)
                    tvFridayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvFridayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 5:
                if(isStartTime)
                    tvSaturdayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvSaturdayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
            case 6:
                if(isStartTime)
                    tvSundayStartTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                else
                    tvSundayEndTime.setText(Utils.convert24HourTo12Hour(mSelectedTime));
                break;
        }
    }

    private void showAppointmentSlot()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SetAppointmentSlotsFragment setAppointmentSlotsFragment = SetAppointmentSlotsFragment.newInstance();
        setAppointmentSlotsFragment.show(fragmentTransaction,"Set Appointment Slot");
    }

    public void cancelActivity(View view) {
        onBackPressed();
    }

    public void saveActivity(View view) {
        onBackPressed();
    }
}
