package demo.acube.application.healthcare.activity.patient.acitivity.mySchedule;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.CallAppointmentActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.MedicationRemainderActivity;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.callRequestBean.CallRequestDatasBean;
import demo.acube.application.healthcare.model.patientScheduleBean.Appointment;
import demo.acube.application.healthcare.model.patientScheduleBean.PatientScheduleBean;
import demo.acube.application.healthcare.model.patientScheduleBean.Reminder;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;


/**
 * Created by Anns on 29/06/17.
 */

public class ScheduleDetailFragment extends DialogFragment implements View.OnClickListener
{
    static ScheduleDetailFragment newInsatnce()
    {
        return new ScheduleDetailFragment();
    }
    private View rootView;
    TextView tvTaskType,tvTaskUserName,tvTaskCreatedBY,tvTaskDateTime,tvTaskFromTo,tvTaskAlert,tvTaskNotes;
    TextView tvLblAlert,tvLblNotes,tvCancelDelete;
    ImageView ivScheduleTypeIcon;
    LinearLayout llAppointmentContainer,llRemainderContainer;
    LinearLayout llRemBiometricContainer,llRemMedicationContainer;
    Button btnExecuteTask;
    private Boolean isAppointmentSelected = true;
    private int selectedPosition = 0;
    private PatientScheduleBean selectedPatientSchedulesList;
    String mTaskType = "";
    String mTaskUserName = "";
    String mCreatedBY = "";
    String mTaskCreatedBy = "";
    String mTaskFromTo = "";
    String mTaskAlert = "";
    String mTaskNotes = "";
    private int userID;
    private Boolean isMenuEditShown = false;
    Toolbar toolbar;
    private int appointmentType = 0;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    APIInterface apiInterface;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedule_detail, container, false);

        initUI();
        globalApplication = (GlobalApplication)getActivity().getApplicationContext();
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        isAppointmentSelected = ((MyScheduleActivity)getActivity()).isAppointmentSelected;
        selectedPosition = ((MyScheduleActivity)getActivity()).selectedPosition;
        selectedPatientSchedulesList = ((MyScheduleActivity)getActivity()).selectedPatientSchedulesList;
        userID = ((MyScheduleActivity)getActivity()).globalApplication.getUserId();
        if(isAppointmentSelected)
            appointmentDetail();
        else
            remainderDetail();

        initToolbar();

        return rootView;
    }

    private void remainderDetail()
    {

        tvCancelDelete.setText(R.string.delete_reminder);
        llAppointmentContainer.setVisibility(View.GONE);
        llRemainderContainer.setVisibility(View.VISIBLE);
        Reminder remainder = selectedPatientSchedulesList.getData().getReminders().get(selectedPosition);

        try
        {
            Date remainderEndDate = Utils.convertStringToDate2(remainder.getEndsAt());
            if(remainderEndDate.compareTo(new Date())<1)
            {
                tvCancelDelete.setVisibility(View.GONE);
                isMenuEditShown = false;

            }
            else
            {
                tvCancelDelete.setVisibility(View.VISIBLE);
                isMenuEditShown = true;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        int type = remainder.getType();
        setTaskTypeName(type);
        TextView tvRemTaskType = (TextView)rootView.findViewById(R.id.tv_rem_task_type);
        tvRemTaskType.setText(mTaskType);

        TextView tvRemTaskFrom = (TextView)rootView.findViewById(R.id.tv_rem_task_from);
        TextView tvRemTaskTo = (TextView)rootView.findViewById(R.id.tv_rem_task_to);

        TextView tvRemTaskAlert = (TextView)rootView.findViewById(R.id.tv_rem_task_alert);
        TextView tvRemTaskNotes = (TextView)rootView.findViewById(R.id.tv_rem_task_notes);

        TextView lblRemAlert = (TextView)rootView.findViewById(R.id.lbl_rem_alert);
        TextView lblRemNotes = (TextView)rootView.findViewById(R.id.lbl_rem_notes);



        String mStrStartDate = remainder.getStartsAt().substring(0,10);
        String mStrEndDate = remainder.getEndsAt().substring(0,10);

        try {
            Date startDate = Utils.convertStringToDate2(mStrStartDate);
            Date endDate = Utils.convertStringToDate2(mStrEndDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            tvRemTaskFrom.setText(String.format("From %s", getTaskDate(cal)));

            cal.setTime(endDate);
            tvRemTaskTo.setText(String.format("to %s", getTaskDate(cal)));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(remainder.getAlertMinutes()!=null && !remainder.getAlertMinutes().equals("0"))
        {
            tvRemTaskAlert.setText(String.format("%s minutes before", remainder.getAlertMinutes()));
        }
        else
        {
            tvRemTaskAlert.setVisibility(View.GONE);
            lblRemAlert.setVisibility(View.GONE);
        }

        if(!remainder.getNotes().equals(""))
            tvRemTaskNotes.setText(remainder.getNotes());
        else
        {
            tvRemTaskNotes.setVisibility(View.GONE);
            lblRemNotes.setVisibility(View.GONE);
        }

        if(type==0)
        {
            fillMedicationRemainderValues(remainder);
        }
        else
        {
            fillBiometricValues(remainder);
        }



    }

    private void fillMedicationRemainderValues(Reminder remainder)
    {
        llRemBiometricContainer.setVisibility(View.GONE);
        llRemMedicationContainer.setVisibility(View.VISIBLE);
        TextView tvRemMedicName = (TextView)rootView.findViewById(R.id.tv_rem_medic_name);
        tvRemMedicName.setText(remainder.getMedication().getMedicineName());

        ImageView ivRemMedicImg = (ImageView)rootView.findViewById(R.id.iv_rem_med_img);
        if(remainder.getMedication().getImages().size()!=0)
        {
            Picasso.with(getActivity())
                    .load(remainder.getMedication().getImages().get(0).getSmall())
                    .into(ivRemMedicImg);
        }

    }

    private void fillBiometricValues(Reminder remainder) {

        llRemBiometricContainer.setVisibility(View.VISIBLE);
        llRemMedicationContainer.setVisibility(View.GONE);
        TextView tvRemTaskName = (TextView)rootView.findViewById(R.id.tv_rem_task_name);
        tvRemTaskName.setText(remainder.getDevice().getName());
    }

    private void appointmentDetail()
    {
        tvCancelDelete.setText(R.string.cancel_appointment);
        llAppointmentContainer.setVisibility(View.VISIBLE);
        llRemainderContainer.setVisibility(View.GONE);
        Appointment appointment = selectedPatientSchedulesList.getData().getAppointments().get(selectedPosition);
        Date appointmentEndDate = Utils.convertStringToDateTime(appointment.getStartsAt());
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

        int type = appointment.getType();
        appointmentType = type;
        setTaskTypeName(type);
        tvTaskType.setText(mTaskType);
        mTaskUserName = appointment.getDoctor().getName();
        tvTaskUserName.setText(String.format("Dr.%s", mTaskUserName));
        if(userID==appointment.getCreator().getUserId())
            tvTaskCreatedBY.setText(R.string.created_by_you);
        else
            tvTaskCreatedBY.setText(String.format("Created by %s", appointment.getCreator().getName()));

             CalendarDay selectedCalendarDay = ((MyScheduleActivity)getActivity()).materialCalendarView.getSelectedDate();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(selectedCalendarDay.getDate());
         tvTaskDateTime.setText(getTaskDate(startCal));

        mTaskFromTo = getTaskFromTo(appointment.getStartsAt(),appointment.getEndsAt());
        tvTaskFromTo.setText(mTaskFromTo);
        if(appointment.getPatientAlertMinutes()!=null && !appointment.getPatientAlertMinutes().equals("0"))
        {
            tvTaskAlert.setText(String.format("%s minutes before", appointment.getPatientAlertMinutes()));
        }
        else
        {
            tvTaskAlert.setVisibility(View.GONE);
            tvTaskAlert.setVisibility(View.GONE);
        }

        if(!appointment.getNotes().equals(""))
            tvTaskNotes.setText(appointment.getNotes());
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

    private String getTaskDate(Calendar startCal)
    {
        String formattedDate = "";
        String day_week = Utils.getDayFromNumber(startCal.get(Calendar.DAY_OF_WEEK));
        String month = Utils.getMonthFromNumber(startCal.get(Calendar.MONTH));
        int day_month = startCal.get(Calendar.DAY_OF_MONTH);
        int year = startCal.get(Calendar.YEAR);
        formattedDate = day_week+" "+month+" "+day_month+","+year;

        return formattedDate;
    }


    private void initUI()
        {
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

            llAppointmentContainer = (LinearLayout)rootView.findViewById(R.id.ll_appointment_container);
            llRemainderContainer = (LinearLayout)rootView.findViewById(R.id.ll_remainder_container);

            llRemBiometricContainer = (LinearLayout)rootView.findViewById(R.id.ll_rem_biometric_container);
            llRemMedicationContainer = (LinearLayout)rootView.findViewById(R.id.ll_rem_medication_container);

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
        if(isAppointmentSelected)
        {
            doDeleteAppointment();
        }
        else
        {
            doDeleteReminder();
        }
    }

    private void doDeleteAppointment()
    {
        int appointmentId = selectedPatientSchedulesList.getData().getAppointments().get(selectedPosition).getAppointmentId();
        progressDialog = LoadingDialog.show(getActivity(),"Deleting Appointment");

        Call<Success> callRequestCall = apiInterface.doDeleteAppointment(appointmentId);
        callRequestCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    dismiss();
                }
                else
                {
                    Utils.showToast(getActivity(),"Unable to process request");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(getActivity(),"Unable to process request "+t.getLocalizedMessage());
            }
        });

    }

    private void doDeleteReminder()
    {
        int reminderId = selectedPatientSchedulesList.getData().getReminders().get(selectedPosition).getReminderId();
        progressDialog = LoadingDialog.show(getActivity(),"Deleting Appointment");

        Call<Success> callRequestCall = apiInterface.doDeleteReminder(reminderId);
        callRequestCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    dismiss();
                }
                else
                {
                    Utils.showToast(getActivity(),"Unable to process request");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(getActivity(),"Unable to process request "+t.getLocalizedMessage());
            }
        });
    }

    private void executeTask()
    {
       int appointmentDocotorId = selectedPatientSchedulesList.getData().getAppointments().get(selectedPosition).getDoctor().getUserId();
       String appointmentDocotorName = selectedPatientSchedulesList.getData().getAppointments().get(selectedPosition).getDoctor().getName();
        globalApplication.setPatSelectedCallerId(appointmentDocotorId);
        globalApplication.setPatSelectedCallerName(appointmentDocotorName);

            doCallStream();



    }

    public void doCallStream()
    {
        Utils.getAudioVideoPermission(getActivity());
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO);
        if(permissionCheck==0)
            getCallRequestDatas();

    }

    private void getCallRequestDatas()
    {
        progressDialog = LoadingDialog.show(getActivity(),"Initiating call");
        String userId = String.valueOf(globalApplication.getUserId());
        String selectedCallId = String.valueOf(globalApplication.getPatSelectedCallerId());
        Call<CallRequestDatasBean> callRequestCall = apiInterface.doGetCallRequestDatas(userId,selectedCallId,"call");
        callRequestCall.enqueue(new Callback<CallRequestDatasBean>() {
            @Override
            public void onResponse(Call<CallRequestDatasBean> call, Response<CallRequestDatasBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    globalApplication.setTokenNumber(response.body().getData().getTokbox().getToken());
                    globalApplication.setSessionId(response.body().getData().getTokbox().getSessionId());
                    goToCallStream();
                }
                else
                {
                    Utils.showToast(getActivity(),"Unable to process request");
                }
            }

            @Override
            public void onFailure(Call<CallRequestDatasBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(getActivity(),"Unable to process request "+t.getLocalizedMessage());
            }
        });
    }
    public void goToCallStream()
    {
        if(appointmentType==2)
        {
            Intent intent = new Intent(getActivity(), CallStreamActivity.class);
            startActivity(intent);
        }
        else if(appointmentType==3)
        {
            Intent intent = new Intent(getActivity(), VideoCallStreamAcitivity.class);
            startActivity(intent);
        }

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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));

        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        TextView tvToolbarEdit = (TextView)toolbar.findViewById(R.id.toolbar_tv_edit);

        tvToolbarTitle.setTextColor(ContextCompat.getColor(getActivity(),R.color.White));
        tvToolbarBack.setTextColor(ContextCompat.getColor(getActivity(),R.color.White));
        tvToolbarEdit.setTextColor(ContextCompat.getColor(getActivity(),R.color.White));
        Utils.setFontForTextView(getActivity(),tvToolbarTitle);
        Utils.setFontForTextView(getActivity(),tvToolbarBack);
        Utils.setFontForTextView(getActivity(),tvToolbarEdit);
        tvToolbarTitle.setText(getString(R.string.patients_schedule));
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(isMenuEditShown)
            tvToolbarEdit.setVisibility(View.VISIBLE);
        else
            tvToolbarEdit.setVisibility(View.GONE);
        tvToolbarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSchedule();
            }
        });

    }

    private void editSchedule()
    {
        if(isAppointmentSelected)
        {
            editAppointment();
        }
        else
        {
            editReminder();
        }
    }

    public void editAppointment()
    {
        Intent intent =  new Intent(getActivity(), CallAppointmentActivity.class);
        startActivity(intent);
    }
    public void editReminder()
    {
        Intent intent =  new Intent(getActivity(), MedicationRemainderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {


    }

    private void setTaskTypeName(int type)
    {
        if(type==0)
        {
            mTaskType = getString(R.string.medication_reminder);
            ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_medication_name));
        }
        else if(type==1)
        {
            mTaskType = getString(R.string.biometric_reading);
            ivScheduleTypeIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_health));
        }
        else if(type==2)
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
