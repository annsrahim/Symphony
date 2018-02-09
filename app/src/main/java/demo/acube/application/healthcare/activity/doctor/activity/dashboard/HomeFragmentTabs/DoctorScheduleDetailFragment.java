package demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Date;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;

import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;


/**
 * Created by Anns on 29/06/17.
 */

public class DoctorScheduleDetailFragment extends DialogFragment implements View.OnClickListener
{

   public static DoctorScheduleDetailFragment newInsatnce()
    {
        return new DoctorScheduleDetailFragment();
    }

    private View rootView;
    private Toolbar toolbar;
    TextView tvTaskType,tvTaskUserName,tvTaskCreatedBY,tvTaskDateTime,tvTaskFromTo,tvTaskAlert,tvTaskNotes;
    TextView tvLblAlert,tvLblNotes,tvCancelDelete;
    ImageView ivScheduleTypeIcon;
    Button btnExecuteTask;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    APIInterface apiInterface;
    Datum doctorTaskListBean;
    int selectedPosition = 0;
    boolean isMenuEditShown;
    String mTaskType = "";
    String mTaskUserName = "";
    String mTaskFromTo = "";
    private int userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_doctor_schedule_detail, container, false);
        globalApplication = (GlobalApplication)getActivity().getApplicationContext();
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        doctorTaskListBean = ((DoctorHomeActivity)getActivity()).doctorTaskListBean;
        selectedPosition = ((DoctorHomeActivity)getActivity()).selectedPosition;
        userID = globalApplication.getUserId();
        initToolbar();
        initUI();
        setAppointmentDetails();
        return rootView;
    }

    private void setAppointmentDetails()
    {


        Date appointmentEndDate = Utils.convertStringToDateTime(doctorTaskListBean.getTaskMeta().getStartsAt());
        int isDateCompleted = appointmentEndDate.compareTo(new Date());

        if(isDateCompleted<1)
        {
            tvCancelDelete.setVisibility(View.GONE);
            btnExecuteTask.setVisibility(View.GONE);
            isMenuEditShown = false;
        }
        else
        {
            tvCancelDelete.setVisibility(View.VISIBLE);
            btnExecuteTask.setVisibility(View.VISIBLE);
            isMenuEditShown = true;
        }
        int type = doctorTaskListBean.getType();
        int appointmentType = type;
        setTaskTypeName(type);

        tvTaskType.setText(mTaskType);

        mTaskUserName = doctorTaskListBean.getTaskMeta().getCreator().getName();
        tvTaskUserName.setText(mTaskUserName);
        int creatorId = doctorTaskListBean.getTaskMeta().getCreator().getUserId();

        if(userID==creatorId)
            tvTaskCreatedBY.setText(R.string.created_by_you);
        else
            tvTaskCreatedBY.setText(String.format("Created by %s", mTaskUserName));

//        CalendarDay selectedCalendarDay = ((MyScheduleActivity)getActivity()).materialCalendarView.getSelectedDate();
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(selectedCalendarDay.getDate());
       // tvTaskDateTime.setText(getTaskDate(startCal));
        String startsAt = doctorTaskListBean.getTaskMeta().getStartsAt();
        String endsAt = doctorTaskListBean.getTaskMeta().getEndsAt();
        String alertMinutes = doctorTaskListBean.getTaskMeta().getPatientAlertMinutes();

        mTaskFromTo = getTaskFromTo(startsAt,endsAt);
        tvTaskFromTo.setText(mTaskFromTo);
        if(alertMinutes!=null && !alertMinutes.equals("0"))
        {
            tvTaskAlert.setText(String.format("%s minutes before",alertMinutes));
        }
        else
        {
            tvTaskAlert.setVisibility(View.GONE);
            tvTaskAlert.setVisibility(View.GONE);
        }
        String appointmentNotes = doctorTaskListBean.getTaskMeta().getNotes();
        if(!appointmentNotes.equals(""))
            tvTaskNotes.setText(appointmentNotes);
        else
        {
            tvTaskNotes.setVisibility(View.GONE);
            tvLblNotes.setVisibility(View.GONE);
        }

    }
    private String getTaskFromTo(String startsAt, String endsAt)
    {
        String start = startsAt.substring(10);
        String end = endsAt.substring(10);
        return "From "+start+" to "+end;

    }
    private void initUI() {
        tvTaskType = (TextView)rootView.findViewById(R.id.tv_task_type);
        tvTaskUserName = (TextView)rootView.findViewById(R.id.tv_task_user_name);
        tvTaskCreatedBY = (TextView)rootView.findViewById(R.id.tv_task_created_by);
        tvTaskDateTime = (TextView)rootView.findViewById(R.id.tv_task_date_time);
        tvTaskFromTo = (TextView)rootView.findViewById(R.id.tv_task_from_to);
        tvTaskAlert = (TextView)rootView.findViewById(R.id.tv_task_alert);
        tvTaskNotes = (TextView)rootView.findViewById(R.id.tv_task_notes);
        btnExecuteTask = (Button)rootView.findViewById(R.id.btn_exceute_task);

        tvLblAlert = (TextView)rootView.findViewById(R.id.lbl_alert);
        tvLblNotes = (TextView)rootView.findViewById(R.id.lbl_notes);



        tvCancelDelete = (TextView)rootView.findViewById(R.id.tv_cancel_delete);

        ivScheduleTypeIcon = (ImageView)rootView.findViewById(R.id.iv_schedule_icon);

        btnExecuteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask();
            }
        });
        tvCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSchedulde();
            }
        });
    }

    private void deleteSchedulde() {
    }

    private void executeTask() {
    }


    @Override
    public void onClick(View v) {

    }
    private void initToolbar()
    {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.my_schedule);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_edit)
                {
//                    editSchedule();
                }
                return false;
            }
        });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setTaskTypeName(int type)
    {

         if(type==2)
        {
            mTaskType = getString(R.string.call_with);
            ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_home_call));
        }
        else if(type==3)
        {
            mTaskType = getString(R.string.virtual_visit_with);
            ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_home_virtual_visit));
        }
        else if(type==4)
        {
            mTaskType = getString(R.string.physical_visit_with);
            ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_physical_visit));
        }
    }
}
