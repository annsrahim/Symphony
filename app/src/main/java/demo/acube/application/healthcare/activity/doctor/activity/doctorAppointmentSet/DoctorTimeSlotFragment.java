package demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import demo.acube.application.healthcare.R;

import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.doctorsSlot.DoctorsAvailabilitySlotBean;

/**
 * Created by Anns on 13/07/17.
 */


public class DoctorTimeSlotFragment extends DialogFragment {



    static DoctorTimeSlotFragment newInstance()
    {
        return new DoctorTimeSlotFragment();
    }
    private View rootView;

    private ListView listView;
    private String mSelectedDate;
    private DoctorsAvailabilitySlotBean doctorSlotAvailability;
    private DoctorTimeSlotFragment.TimeSlotListAdapter timeSlotListAdapter;
    public Integer selectedTimeSlot;
    private TextView tvTimeSlotheading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_time_slot, container, false);
        selectedTimeSlot = ((DoctorCallAppointmentActivity)getActivity()).selectedAppointmentSlot;
        mSelectedDate = ((DoctorCallAppointmentActivity)getActivity()).getSelectedDate();
        initToolbar();
        initUI();
        if(selectedTimeSlot!=null)
        {
            assignSelectedTimeInHeading();
        }
        return rootView;
    }

    private void assignSelectedTimeInHeading()
    {
        String startTime = doctorSlotAvailability.getData().get(selectedTimeSlot).getSlot().getStart();
        String endTime = doctorSlotAvailability.getData().get(selectedTimeSlot).getSlot().getEnd();
        tvTimeSlotheading.setText("Time Slot: "+startTime+" - "+endTime);
        tvTimeSlotheading.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.heading_green));
        tvTimeSlotheading.setTextColor(Color.WHITE);
    }

    private void initUI() {
        listView = (ListView)rootView.findViewById(R.id.lv_time_slot);
        doctorSlotAvailability = ((DoctorCallAppointmentActivity)getActivity()).doctorsAvailabilitySlotBean;
        tvTimeSlotheading = (TextView)rootView.findViewById(R.id.tvTimeSlotHeading);
        if(doctorSlotAvailability!=null)
        {
            displaySlotValues();
        }
    }

    private void displaySlotValues()
    {
        final int rejectedSlotCount = ((DoctorCallAppointmentActivity)getActivity()).rejectedSlotCount;
        timeSlotListAdapter = new DoctorTimeSlotFragment.TimeSlotListAdapter(getActivity(),doctorSlotAvailability,rejectedSlotCount);
        listView.setAdapter(timeSlotListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=rejectedSlotCount)
                    timeSlotSelected(position);
            }
        });
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        tvToolbarTitle.setText(mSelectedDate);
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);
        setHasOptionsMenu(true);
    }

    public void timeSlotSelected(int pos)
    {
        ((DoctorCallAppointmentActivity)getActivity()).updateSelectedTimeSlot(pos);

        dismiss();
    }

    public class TimeSlotListAdapter extends BaseAdapter
    {
        private Context mContext;
        private  LayoutInflater inflater=null;
        private DoctorsAvailabilitySlotBean mDoctorSlotList;
        private GlobalApplication globalApplication;
        private int rejectedSlotCount= 0;
        private int rejectedSlots = 0;


        public TimeSlotListAdapter(Context context, DoctorsAvailabilitySlotBean mDoctorSlotList,int rejectedSlotCount)
        {
            this.mContext = context;
            this.mDoctorSlotList = mDoctorSlotList;
            this.rejectedSlotCount = rejectedSlotCount;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            globalApplication = (GlobalApplication)context.getApplicationContext();
        }

        @Override
        public int getCount() {
            return mDoctorSlotList.getData().size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            final int currentPositon = position;
            View rowView;
            rowView = inflater.inflate(R.layout.time_slot_item_layout,null);
            TextView tvTimeSlotValue;
            LinearLayout llLayoutItemContainer;
            ImageView ivCheckMark;
            tvTimeSlotValue = (TextView)rowView.findViewById(R.id.tv_item_time_slot);
            llLayoutItemContainer = (LinearLayout)rowView.findViewById(R.id.llSlotItemContainer);
            ivCheckMark = (ImageView)rowView.findViewById(R.id.iv_check_mark);
            String mTimeSlotValue = mDoctorSlotList.getData().get(currentPositon).getSlot().getStart();
            mTimeSlotValue += " - "+mDoctorSlotList.getData().get(currentPositon).getSlot().getEnd();
            tvTimeSlotValue.setText(mTimeSlotValue);
            if(position<rejectedSlotCount)
            {
                llLayoutItemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey_2));
                tvTimeSlotValue.setTextColor(ContextCompat.getColor(mContext, R.color.light_grey_3));
                ivCheckMark.setVisibility(View.GONE);

            }
            if(selectedTimeSlot!=null && position==selectedTimeSlot)
            {
                tvTimeSlotValue.setTextColor(ContextCompat.getColor(mContext, R.color.toolbar_red));
                llLayoutItemContainer.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.border_background_layout_red));
                ivCheckMark.setVisibility(View.VISIBLE);
            }

            return rowView;
        }


    }



}
