package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.enums.CallType;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.appointmentHistoryBean.Datum;

/**
 * Created by Anns on 24/07/17.
 */

public class AppointementHistoryAdapter extends BaseAdapter {
    private List<Datum> appointmentHistoryList;
    private Context mContext;
    private LayoutInflater inflater;

    public AppointementHistoryAdapter(List<Datum> appointmentHistoryList, Context mContext, LayoutInflater inflater) {
        this.appointmentHistoryList = appointmentHistoryList;
        this.mContext = mContext;
        this.inflater = inflater;
    }

    public class Holder
    {
        TextView tvAppointmentType,tvAppointmentdate,tvAppointmentTime;
        ImageView ivAppointmetnIcon;


    }

    @Override
    public int getCount() {
        return appointmentHistoryList.size();
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
        final View rowView;
        final int currentPos = position;
        rowView = inflater.inflate(R.layout.layout_adapter_appointment_history,null);
        final Holder holder = new Holder();
        holder.tvAppointmentType = (TextView)rowView.findViewById(R.id.tv_appointment_name);
        holder.tvAppointmentdate = (TextView)rowView.findViewById(R.id.tv_appointment_date);
        holder.tvAppointmentTime = (TextView)rowView.findViewById(R.id.tv_appointment_time);
        holder.ivAppointmetnIcon = (ImageView)rowView.findViewById(R.id.iv_appointment_icon);
        setAppointmentIconAndName(appointmentHistoryList.get(position).getType(),holder);
        Date startDateTime = Utils.convertStringToDateTime(appointmentHistoryList.get(position).getStartsAt());
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTime);
            String appointmentDate = "";
             String  appointmentDay = Utils.getDayFromNumber(cal.get(Calendar.DAY_OF_WEEK));
             String  appointmentMonth= Utils.getMonthFromNumber(cal.get(Calendar.MONTH));
             int appointmentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);

        appointmentDate = appointmentDay+", "+appointmentMonth+" "+appointmentDayOfMonth+" "+year;

        holder.tvAppointmentdate.setText(appointmentDate);
        holder.tvAppointmentTime.setText(appointmentHistoryList.get(position).getStartsAt().substring(11));
        return rowView;
    }

    private void setAppointmentIconAndName(Integer type,Holder holder)
    {
        switch (type)
        {
            case CallType.CALL:
                    holder.ivAppointmetnIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.btn_contact_call));
                    holder.tvAppointmentType.setText(R.string.Call);
                break;
            case CallType.VIRTUAL_VISIT:
                holder.ivAppointmetnIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.btn_contact_video));
                holder.tvAppointmentType.setText(R.string.virtual_visit);
                break;
            case CallType.PHYSICAL_VISIT:
                holder.ivAppointmetnIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.btn_contact_schedule_appointment));
                holder.tvAppointmentType.setText(R.string.physical_visit);
                break;
        }
    }
}
