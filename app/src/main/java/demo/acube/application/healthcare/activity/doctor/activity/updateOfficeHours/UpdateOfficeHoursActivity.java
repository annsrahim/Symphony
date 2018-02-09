package demo.acube.application.healthcare.activity.doctor.activity.updateOfficeHours;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
import demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule.CalendarScheduleActivity;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity.MyProfileActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.doctorsSlot.DoctorsAvailabilitySlotBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class UpdateOfficeHoursActivity extends AppCompatActivity {

   String dateForSlot="";
    ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private DoctorsAvailabilitySlotBean doctorsAvailabilitySlotBean;
    private GlobalApplication globalApplication;
    String[] myResArray;
    List<String> myResArrayList;
    List<String> availbaleSlots = new ArrayList<>();
    private ListView listView;
    TextView tvToolbarTitle;
    ImageView ivFloatBtn;
    FloatingActionMenu circleMenu;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_office_hours);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Bundle extras =  getIntent().getExtras();
        initUI();
        if(extras!=null)
        {
            myResArray = getResources().getStringArray(R.array.array_time_slots);
            myResArrayList = Arrays.asList(myResArray);
            dateForSlot = extras.getString(ValueUtils.DateForSlot);
            initToolBar();
            getDoctorsAvailablilitySlots();
        }
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
                goToTasksList();

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

    private void goToTasksList() {
        startActivity(new Intent(this, DoctorHomeActivity.class));
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



    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            Date dateSelectedDate = Utils.convertStringToDate2(dateForSlot);
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(dateSelectedDate);
            String dayOfWeek = Utils.getDayFromNumber(newCalendar.get(Calendar.DAY_OF_WEEK));
            dayOfWeek = dayOfWeek.substring(0,3);
            String monthName = Utils.getMonthFromNumber(newCalendar.get(Calendar.MONTH));
            int dayOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH);
            int year = newCalendar.get(Calendar.YEAR);
            String title = dayOfWeek+", "+monthName+" "+dayOfMonth+" "+year;
            tvToolbarTitle.setText(title);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);

        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.light_blue,null));


    }

    private void initUI() {
        listView = (ListView)findViewById(R.id.lv_time_slot);
        tvToolbarTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout_bg);
        frameLayout.bringToFront();
    }
    public void closeMenu(View view) {
        circleMenu.close(true);
    }
    private void getDoctorsAvailablilitySlots()
    {
        progressDialog = LoadingDialog.show(this,"Fetching details");

        Call<DoctorsAvailabilitySlotBean> docSlotCall = apiInterface.doGetDoctorAvailalitySlots(globalApplication.getUserId(),dateForSlot);
        docSlotCall.enqueue(new Callback<DoctorsAvailabilitySlotBean>() {
            @Override
            public void onResponse(Call<DoctorsAvailabilitySlotBean> call, Response<DoctorsAvailabilitySlotBean> response) {
                progressDialog.dismiss();
                if(response.code()==200) {
                    doctorsAvailabilitySlotBean = response.body();
                    displaySlots();
                }
            }

            @Override
            public void onFailure(Call<DoctorsAvailabilitySlotBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(UpdateOfficeHoursActivity.this,"Unable to get available slots "+t.getLocalizedMessage());
            }
        });
    }

    private void displaySlots() {
        for(int i=0;i<doctorsAvailabilitySlotBean.getData().size();i++)
        {
            String timeStart = doctorsAvailabilitySlotBean.getData().get(i).getSlot().getStart();
            String timeEnd = doctorsAvailabilitySlotBean.getData().get(i).getSlot().getEnd();
            String timeSlot =   timeStart+" - "+timeEnd;
            availbaleSlots.add(timeSlot);
        }
        listView.setAdapter(new AdapterDoctorTimeSlot(this,myResArrayList,availbaleSlots));
    }

    public void actionDone(View view) {
        finish();
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public class AdapterDoctorTimeSlot extends BaseAdapter
    {
        private LayoutInflater inflater=null;
        private Context mContext;
        List<String> availableSlots;
        private List<String> slotArrayList = new ArrayList<>();
        public AdapterDoctorTimeSlot(Context context, List<String> slotArrayList,List<String> availableSlots) {
            this.mContext = context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.slotArrayList = slotArrayList;
            this.availableSlots = availableSlots;
        }


        @Override
        public int getCount() {
            return slotArrayList.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView;
            rowView = inflater.inflate(R.layout.time_slot_item_layout,null);
            TextView tvTimeSlotValue;
            final LinearLayout llLayoutItemContainer;
               llLayoutItemContainer = (LinearLayout)rowView.findViewById(R.id.llSlotItemContainer);
            tvTimeSlotValue = (TextView)rowView.findViewById(R.id.tv_item_time_slot);
            tvTimeSlotValue.setText(slotArrayList.get(position));
            final Boolean[] slotEnabledState = new Boolean[slotArrayList.size()];
            if(availableSlots.contains(slotArrayList.get(position)))
            {
                tvTimeSlotValue.setTextColor(ContextCompat.getColor(mContext,R.color.light_blue));
                llLayoutItemContainer.setBackground(ContextCompat.getDrawable(mContext,R.drawable.border_background_layout_blue));
                slotEnabledState[position]=true;
            }
            else
            {
                tvTimeSlotValue.setTextColor(ContextCompat.getColor(mContext,R.color.light_grey_3));
                llLayoutItemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey_2));
                slotEnabledState[position]=false;
            }
           rowView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                if(slotEnabledState[position])
                {
                    llLayoutItemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey_2));
                    slotEnabledState[position] = false;
                }
                else
                {
                    llLayoutItemContainer.setBackground(ContextCompat.getDrawable(mContext,R.drawable.border_background_layout_blue));
                    slotEnabledState[position]=true;
                }
               }
           });

            return rowView;
        }
    }


    @Override
    public void onBackPressed() {

    }
}
