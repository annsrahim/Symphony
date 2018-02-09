package demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.Utils;


/**
 * Created by Anns on 13/07/17.
 */

public class DosageAlertReminderDialogFragment extends DialogFragment implements View.OnClickListener{
    static DosageAlertReminderDialogFragment newInstance() {
        return new DosageAlertReminderDialogFragment();
    }

    View rootView;
    private int alertNotifyType ;
    private ImageView ivNone,ivAtTime,iv5min,iv10Min,iv30Min,iv1Hour,iv2Hour,iv1Day,iv2Day,iv1Week;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alert_remainder, container, false);
        alertNotifyType =  ((DoctorMedicationReminderActivity)getActivity()).mAlertNotifyType;
        initToolbar();
        initUI();
        enableAlertCheckMark();
        return rootView;
    }

    private void enableAlertCheckMark()
    {

        switch (alertNotifyType)
        {
            case 0:
            {

                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.VISIBLE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                break;
            }
            case 1:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.VISIBLE);
                iv10Min.setVisibility(View.GONE);
                break;
            }
            case 2:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.VISIBLE);
                break;
            }


        }
    }

    private void initUI()
    {
        RelativeLayout rlNone = (RelativeLayout) rootView.findViewById(R.id.rl_alert_none);
        RelativeLayout rlAtEventTime = (RelativeLayout) rootView.findViewById(R.id.rl_alert_at_event_time);
        RelativeLayout rl5Minbefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_5_min);
        RelativeLayout rl10MinBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_10_min);
        ivNone = (ImageView)rootView.findViewById(R.id.iv_alert_none);
        ivAtTime = (ImageView)rootView.findViewById(R.id.iv_alert_at_event_time);
        iv5min = (ImageView)rootView.findViewById(R.id.iv_alert_5_min_before);
        iv10Min = (ImageView)rootView.findViewById(R.id.iv_alert_10_min);
        iv30Min = (ImageView)rootView.findViewById(R.id.iv_alert_30_min);
        iv1Hour = (ImageView)rootView.findViewById(R.id.iv_alert_1_hour_before);
        iv2Hour = (ImageView)rootView.findViewById(R.id.iv_alert_2_hour_before);
        iv1Day = (ImageView)rootView.findViewById(R.id.iv_alert_1_day_before);
        iv2Day = (ImageView)rootView.findViewById(R.id.iv_alert_2_days_before);
        iv1Week = (ImageView)rootView.findViewById(R.id.iv_alert_1_week_before);
        rlNone.setOnClickListener(this);
        rlAtEventTime.setOnClickListener(this);
        rl5Minbefore.setOnClickListener(this);
        rl10MinBefore.setOnClickListener(this);

        RelativeLayout rl30MinBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_30_min);
        RelativeLayout rl1HourBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_hour_before);
        RelativeLayout rl2HOurBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_2_hour_before);
        RelativeLayout rl1DayBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_day_before);
        RelativeLayout rl2DayBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_2_days_before);
        RelativeLayout rl1WeekBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_week_before);
        rl30MinBefore.setVisibility(View.GONE);
        rl1HourBefore.setVisibility(View.GONE);
        rl2HOurBefore.setVisibility(View.GONE);
        rl1DayBefore.setVisibility(View.GONE);
        rl2DayBefore.setVisibility(View.GONE);
        rl1WeekBefore.setVisibility(View.GONE);

        LinearLayout llLine15Min = (LinearLayout)rootView.findViewById(R.id.ll_line_15_min);
        LinearLayout llLine30Min = (LinearLayout)rootView.findViewById(R.id.ll_line_30_min);
        LinearLayout llLine1hour = (LinearLayout)rootView.findViewById(R.id.ll_line_1_hour);
        LinearLayout llLine2Hour = (LinearLayout)rootView.findViewById(R.id.ll_line_2_hour);
        LinearLayout llLine1Day = (LinearLayout)rootView.findViewById(R.id.ll_line_1_day);
        LinearLayout llLine2Day = (LinearLayout)rootView.findViewById(R.id.ll_line_2_day);

        llLine15Min.setVisibility(View.GONE);
        llLine30Min.setVisibility(View.GONE);
        llLine1hour.setVisibility(View.GONE);
        llLine2Hour.setVisibility(View.GONE);
        llLine1Day.setVisibility(View.GONE);
        llLine2Day.setVisibility(View.GONE);

        setAlertVisibility();

    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);

        tvToolbarBack.setTextColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_blue, null));
        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_blue, null));

        Utils.setFontForTextView(getActivity(),tvToolbarTitle);
        Utils.setFontForTextView(getActivity(),tvToolbarBack);

        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
    private void setAlertVisibility() {
        iv30Min.setVisibility(View.GONE);
        iv1Hour.setVisibility(View.GONE);
        iv2Hour.setVisibility(View.GONE);
        iv1Day.setVisibility(View.GONE);
        iv2Day.setVisibility(View.GONE);
        iv1Week.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rl_alert_none:
                ((DoctorMedicationReminderActivity)getActivity()).mAlertNotifyType = 0;
                ((DoctorMedicationReminderActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_at_event_time:
                ((DoctorMedicationReminderActivity)getActivity()).mAlertNotifyType = 0;
                ((DoctorMedicationReminderActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_5_min:
                ((DoctorMedicationReminderActivity)getActivity()).mAlertNotifyType = 1;
                ((DoctorMedicationReminderActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_10_min:
                ((DoctorMedicationReminderActivity)getActivity()).mAlertNotifyType = 2;
                ((DoctorMedicationReminderActivity)getActivity()).updateAlertTime();
                dismiss();
                break;

        }

    }
}

