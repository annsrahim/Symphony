package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.helper.utilities.Utils;

/**
 * Created by Anns on 11/08/17.
 */

public class DoctorTaskSearchListAdapter extends BaseAdapter {
    private List<Datum> taskList;
    private Context context;
    private LayoutInflater inflater;

    public DoctorTaskSearchListAdapter(List<Datum> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView userName,taskType,taskTime;
        ImageView ivTaskUser,ivTaskType;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.doctor_task_list_item,null);
        holder.userName = (TextView)rowView.findViewById(R.id.list_user_name);
        holder.taskType = (TextView)rowView.findViewById(R.id.list_task_type);
        holder.taskTime = (TextView)rowView.findViewById(R.id.list_task_time);
        holder.ivTaskUser = (ImageView)rowView.findViewById(R.id.iv_task_user);
        holder.ivTaskType = (ImageView)rowView.findViewById(R.id.iv_task_type);
        holder.ivTaskUser.setVisibility(View.GONE);
        final Datum task = taskList.get(position);
        int taskType = 0;
        if(task.getType()!=null)
        {
            taskType = task.getType();
        }

        if(taskType==1||taskType==2||taskType==3||taskType==4)
        {
            holder.userName.setText(task.getTaskMeta().getPatient().getName());
            holder.taskType.setText(Utils.getTaskTypeName(task.getType()));
            holder.taskTime.setText(task.getTaskMeta().getStartsAt());
            if(task.getTaskMeta().getPatient().getAvatar()!=null)
            {
                String imagePath = task.getTaskMeta().getPatient().getAvatar();
                Picasso.with(context).load(imagePath).placeholder(R.drawable.gpc_placeholder_physician_full)
                        .into(holder.ivTaskUser);
            }

        }
        else if(taskType==6||taskType==7)
        {
            holder.userName.setText(task.getTaskMeta().getSender().getName());
            holder.taskType.setText(Utils.getTaskTypeName(task.getType()));
            holder.taskTime.setText(task.getTaskMeta().getStartsAt());
            if(task.getTaskMeta().getSender().getAvatar()!=null)
            {
                String imagePath = task.getTaskMeta().getSender().getAvatar();
                Picasso.with(context).load(imagePath).placeholder(R.drawable.gpc_placeholder_physician_full)
                        .into(holder.ivTaskUser);
            }
        }
        switch(taskType)
        {
            case 1:
                Picasso.with(context).load(R.drawable.btn_contact_text).into(holder.ivTaskType);
                break;
            case 2:
                Picasso.with(context).load(R.drawable.btn_contact_call).into(holder.ivTaskType);
                break;
            case 3:
                Picasso.with(context).load(R.drawable.btn_contact_video).into(holder.ivTaskType);
                break;
            case 4:
                Picasso.with(context).load(R.drawable.btn_contact_schedule_appointment).into(holder.ivTaskType);
                break;
            case 6:
                Picasso.with(context).load(R.drawable.btn_contact_call).into(holder.ivTaskType);
                break;
            case 7:
                Picasso.with(context).load(R.drawable.btn_contact_video).into(holder.ivTaskType);
                break;


        }


        return rowView;
    }
}
