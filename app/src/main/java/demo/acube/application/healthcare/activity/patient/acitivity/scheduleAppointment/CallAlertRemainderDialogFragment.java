package demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import demo.acube.application.healthcare.R;

/**
 * Created by Anns on 06/06/17.
 */

public class CallAlertRemainderDialogFragment extends DialogFragment implements View.OnClickListener{
    static  CallAlertRemainderDialogFragment newInstance() {
        CallAlertRemainderDialogFragment f = new CallAlertRemainderDialogFragment();
        return f;
    }

    View rootView;
    private int alertNotifyType ;
    private ImageView ivNone,ivAtTime,iv5min,iv10Min,iv15Min,iv30Min,iv1Hour,iv2Hour,iv1Day,iv2Days,iv1Week;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alert_remainder, container, false);
        alertNotifyType =  ((CallAppointmentActivity)getActivity()).mAlertNotifyType;
        initToolbar();
        initUI();
        enableAlertCheckMark();
        return rootView;
    }

    private void enableAlertCheckMark()
    {

        switch (alertNotifyType)
        {
            case -1:
            {
                ivNone.setVisibility(View.VISIBLE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }

            case 0:
            {

                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.VISIBLE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 1:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.VISIBLE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 2:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.VISIBLE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 3:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.VISIBLE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 4:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.VISIBLE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 5:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.VISIBLE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 6:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.VISIBLE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 7:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.VISIBLE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 8:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.VISIBLE);
                iv1Week.setVisibility(View.GONE);
                break;
            }
            case 9:
            {
                ivNone.setVisibility(View.GONE);
                ivAtTime.setVisibility(View.GONE);
                iv5min.setVisibility(View.GONE);
                iv10Min.setVisibility(View.GONE);
                iv15Min.setVisibility(View.GONE);
                iv30Min.setVisibility(View.GONE);
                iv1Hour.setVisibility(View.GONE);
                iv2Hour.setVisibility(View.GONE);
                iv1Day.setVisibility(View.GONE);
                iv2Days.setVisibility(View.GONE);
                iv1Week.setVisibility(View.VISIBLE);
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
        RelativeLayout rl15MinBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_15_min);
        RelativeLayout rl30MinBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_30_min);
        RelativeLayout rl1HourBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_hour_before);
        RelativeLayout rl2HoursBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_2_hour_before);
        RelativeLayout rl1DayBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_day_before);
        RelativeLayout rl2DaysBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_2_days_before);
        RelativeLayout rl1WeekBefore = (RelativeLayout) rootView.findViewById(R.id.rl_alert_1_week_before);

        ivNone = (ImageView)rootView.findViewById(R.id.iv_alert_none);
        ivAtTime = (ImageView)rootView.findViewById(R.id.iv_alert_at_event_time);
        iv5min = (ImageView)rootView.findViewById(R.id.iv_alert_5_min_before);
        iv10Min = (ImageView)rootView.findViewById(R.id.iv_alert_10_min);
        iv15Min = (ImageView)rootView.findViewById(R.id.iv_alert_15_min);
        iv30Min = (ImageView)rootView.findViewById(R.id.iv_alert_30_min);
        iv1Hour = (ImageView)rootView.findViewById(R.id.iv_alert_1_hour_before);
        iv2Hour = (ImageView)rootView.findViewById(R.id.iv_alert_2_hour_before);
        iv1Day = (ImageView)rootView.findViewById(R.id.iv_alert_1_day_before);
        iv2Days = (ImageView)rootView.findViewById(R.id.iv_alert_2_days_before);
        iv1Week = (ImageView)rootView.findViewById(R.id.iv_alert_1_week_before);
        rlNone.setOnClickListener(this);
        rlAtEventTime.setOnClickListener(this);
        rl5Minbefore.setOnClickListener(this);
        rl10MinBefore.setOnClickListener(this);
        rl15MinBefore.setOnClickListener(this);
        rl30MinBefore.setOnClickListener(this);
        rl1HourBefore.setOnClickListener(this);
        rl1WeekBefore.setOnClickListener(this);
        rl2HoursBefore.setOnClickListener(this);
        rl1DayBefore.setOnClickListener(this);
        rl2DaysBefore.setOnClickListener(this);


    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Alert");
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = -1;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_at_event_time:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 0;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_5_min:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 1;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_10_min:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 2;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_15_min:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 3;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_30_min:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 4;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_1_hour_before:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 5;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_2_hour_before:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 6;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_1_day_before:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 7;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_2_days_before:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 8;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;
            case R.id.rl_alert_1_week_before:
                ((CallAppointmentActivity)getActivity()).mAlertNotifyType = 9;
                ((CallAppointmentActivity)getActivity()).updateAlertTime();
                dismiss();
                break;


        }

    }
}
