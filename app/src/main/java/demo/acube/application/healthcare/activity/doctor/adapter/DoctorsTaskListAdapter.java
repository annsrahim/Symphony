package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.ITaskListClickListener;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.helper.utilities.Utils;

/**
 * Created by Anns on 08/05/17.
 */

public class DoctorsTaskListAdapter extends RecyclerView.Adapter<DoctorsTaskListAdapter.MyViewHolder> {
    private List<Datum> taskList;
    private Context context;
    ITaskListClickListener taskItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,taskType,taskTime;
        public ImageView ivTaskUser,ivTaskType;
        LinearLayout llTaskImage,llTaskDetails;
        RelativeLayout rlDetailsContainer;

        public MyViewHolder(View view)
        {
            super(view);
            userName = (TextView)view.findViewById(R.id.list_user_name);
            taskType = (TextView)view.findViewById(R.id.list_task_type);
            taskTime = (TextView)view.findViewById(R.id.list_task_time);
            ivTaskUser = (ImageView)view.findViewById(R.id.iv_task_user);
            ivTaskType = (ImageView)view.findViewById(R.id.iv_task_type);
            llTaskImage = (LinearLayout)view.findViewById(R.id.ll_task_image);
            llTaskDetails = (LinearLayout)view.findViewById(R.id.ll_task_details);
            rlDetailsContainer = (RelativeLayout)view.findViewById(R.id.rlDetailsContainer);

        }
    }

    @Override
    public DoctorsTaskListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_task_list_item,parent,false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskItemClickListener.onItemClick(v,viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;

    }

    public DoctorsTaskListAdapter(List<Datum> taskList,Context context,ITaskListClickListener listClickListener)
    {
        this.taskList = taskList;
        this.context = context;
        this.taskItemClickListener = listClickListener;
    }

    @Override
    public void onBindViewHolder(DoctorsTaskListAdapter.MyViewHolder holder, int position) {

            final int currentPosition = position;
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






    }

    private void goToScheduleActivity(DoctorTaskListBean task,int pos) {

        Log.d("CurrentPos",task.getData().get(pos).getType()+"");
//        Intent intent = new Intent(context, DoctorScheduleActivity.class);
//        intent.putExtra("TASK_TYPE",task.getData().get(pos).getType());
//        intent.putExtra("TASK_TYPE_NAME",getTaskTypeName(task.getData().get(pos).getType()));
//        intent.putExtra("TASK_USER_NAME",task.getData().get(pos).getTaskMeta().getPatient().getName());
//        intent.putExtra("TASK_CREATED_BY",task.getData().get(pos).getTaskMeta().getCreator().getName());
//        intent.putExtra("TASK_START_DATE_TIME",task.getData().get(pos).getTaskMeta().getStartsAt());
//        intent.putExtra("TASK_END_DATE_TIME",task.getData().get(pos).getTaskMeta().getEndsAt());
//        intent.putExtra("TASK_ALERT",task.getData().get(pos).getTaskMeta().getPatientAlertMinutes());
//        intent.putExtra("TASK_NOTES",task.getData().get(pos).getTaskMeta().getNotes());
//        context.startActivity(intent);
    }
    public  String getTaskTypeName(int taskId)
    {
        switch(taskId)
        {
            case 1:
                return "Text";
            case 2:
                return "Call";
            case 3:
                return "Virtual Visit";
            case 4:
                return "Physical Visit";
            default:
                return "None";
        }
    }
    @Override
    public int getItemCount() {

        return taskList.size();
    }





}
