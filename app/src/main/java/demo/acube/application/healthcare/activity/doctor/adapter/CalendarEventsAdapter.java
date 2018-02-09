package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.models.CalendarItemModel;
import demo.acube.application.healthcare.model.calendarSearchDoctor.Data;

/**
 * Created by Anns on 15/09/17.
 */

public class CalendarEventsAdapter extends BaseAdapter {

    private ArrayList<CalendarItemModel> calendarEventList;
    private Context context;
    private LayoutInflater inflater;
    private int nameAppointmentCount,nameReminderCount,appointmentCount,medicationCount;

    public CalendarEventsAdapter(Context context, ArrayList<CalendarItemModel> calendarEventList) {
        this.calendarEventList = calendarEventList;
        this.context = context;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView userName,taskType,taskTime;
        ImageView ivTaskUser,ivTaskType;
    }

    @Override
    public int getCount() {

        return calendarEventList.size();
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
        Holder holder;

        View rowView  = convertView;


        if(rowView==null)
        {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.doctor_task_list_item,null);
            holder.userName = (TextView)rowView.findViewById(R.id.list_user_name);
            holder.taskType = (TextView)rowView.findViewById(R.id.list_task_type);
            holder.taskTime = (TextView)rowView.findViewById(R.id.list_task_time);
            holder.ivTaskUser = (ImageView)rowView.findViewById(R.id.iv_task_user);
            holder.ivTaskType = (ImageView)rowView.findViewById(R.id.iv_task_type);
            holder.ivTaskUser.setVisibility(View.VISIBLE);
            holder.ivTaskType.setVisibility(View.VISIBLE);
            rowView.setTag(holder);

        }
        else
        {
            holder = (Holder)rowView.getTag();
            holder.userName.setText("");
            holder.taskType.setText("");
            holder.taskTime.setText("");
//            holder.ivTaskUser.setVisibility(View.GONE);
//            holder.ivTaskType.setVisibility(View.GONE);
        }

        holder.userName.setText(calendarEventList.get(position).getPatientName());
        holder.taskType.setText(calendarEventList.get(position).getTaskTypeName());
        holder.taskTime.setText(calendarEventList.get(position).getStartsAt());
        switch(calendarEventList.get(position).getTaskType())
        {
            case 0:
                Picasso.with(context).load(R.drawable.ic_medication_name).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.ic_medication_name)
//                        .into(holder.ivTaskUser);
                break;
            case 1:
                Picasso.with(context).load(R.drawable.btn_contact_text).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;
            case 2:
                Picasso.with(context).load(R.drawable.btn_contact_call).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;
            case 3:
                Picasso.with(context).load(R.drawable.btn_contact_video).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;
            case 4:
                Picasso.with(context).load(R.drawable.btn_contact_schedule_appointment).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;
            case 6:
                Picasso.with(context).load(R.drawable.btn_contact_call).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;
            case 7:
                Picasso.with(context).load(R.drawable.btn_contact_video).into(holder.ivTaskType);
//                Picasso.with(context).load("").placeholder(R.drawable.gpc_placeholder_physician_full)
//                        .into(holder.ivTaskUser);
                break;


        }


        return rowView;
    }
}
