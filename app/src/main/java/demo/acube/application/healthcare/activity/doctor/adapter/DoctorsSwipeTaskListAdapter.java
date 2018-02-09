package demo.acube.application.healthcare.activity.doctor.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
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

public class DoctorsSwipeTaskListAdapter extends RecyclerView.Adapter<DoctorsSwipeTaskListAdapter.MyViewHolder> {
    private List<Datum> taskList;
    private Context context;
    ITaskListClickListener taskItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,taskType,taskTime,swipeTvCancel,swipeTvRemind,swipeTvMarkAsComplete;
        public ImageView ivTaskUser,ivTaskType;
        LinearLayout llTaskImage,llTaskDetails;
        RelativeLayout rlDetailsContainer;
        SwipeRevealLayout swipeRevealLayout;


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
            swipeRevealLayout = (SwipeRevealLayout)view.findViewById(R.id.swipe_reveal_layout);
            swipeTvRemind = (TextView)view.findViewById(R.id.swipe_tv_remind);
            swipeTvCancel = (TextView)view.findViewById(R.id.swipe_tv_cancel);
            swipeTvMarkAsComplete = (TextView)view.findViewById(R.id.swipe_tv_mark_as_complete);
        }
    }

    @Override
    public DoctorsSwipeTaskListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_swipe_task_list_item,parent,false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskItemClickListener.onItemClick(v,viewHolder.getAdapterPosition());
            }
        });



        return viewHolder;

    }

    public DoctorsSwipeTaskListAdapter(List<Datum> taskList, Context context, ITaskListClickListener listClickListener)
    {
        this.taskList = taskList;
        this.context = context;
        this.taskItemClickListener = listClickListener;
    }

    @Override
    public void onBindViewHolder(DoctorsSwipeTaskListAdapter.MyViewHolder holder, int position) {

            holder.swipeRevealLayout.close(true);
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

        holder.rlDetailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskItemClickListener.onItemClick(v,holder.getAdapterPosition());
            }
        });
        holder.swipeTvMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateMarkAsCompelte(context,task.getTaskId());
                taskItemClickListener.relaodList();
            }
        });
        holder.swipeTvRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemindTaskDialog(task.getTaskId());
            }
        });
        holder.swipeTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelConfirmationDailog(task.getTaskId(),task.getTaskMeta().getPatient().getName(),task.getTaskMeta().getStartsAt());


            }
        });





    }

    private void showRemindTaskDialog(int taskId) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater  = dialog.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.remind_time_dialog, null);
        TextView tv10Min,tv30Min,tv1Hour,tv2Hours,tv3Hours;
        tv10Min = (TextView)rootView.findViewById(R.id.tv_in_10_mins);
        tv30Min = (TextView)rootView.findViewById(R.id.tv_in_30_mins);
        tv1Hour = (TextView)rootView.findViewById(R.id.tv_in_1_hour);
        tv2Hours = (TextView)rootView.findViewById(R.id.tv_in_2_hours);
        tv3Hours = (TextView)rootView.findViewById(R.id.tv_in_3_hours);

        tv10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateTaskRemind(context,taskId,10);
                dialog.dismiss();
            }
        });
        tv30Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateTaskRemind(context,taskId,30);
                dialog.dismiss();
            }
        });
        tv1Hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateTaskRemind(context,taskId,60);
                dialog.dismiss();
            }
        });
        tv2Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateTaskRemind(context,taskId,120);
                dialog.dismiss();
            }
        });
        tv3Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateTaskRemind(context,taskId,180);
                dialog.dismiss();
            }
        });
        dialog.setContentView(rootView);
        dialog.show();

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

    public void showCancelConfirmationDailog(Integer taskId, String name, String startsAt)
    {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Appointment")
                .setMessage("You are about to cancel this appointment and permanently delete this task.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        showCancelTaskDialog(taskId,name,startsAt);
                    }})
                .setNegativeButton(R.string.Nevermind, null).show();
    }
    public void showCancelTaskDialog(Integer taskId, String name, String startsAt)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater  = dialog.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.complete_task_request_dialog, null);
        TextView tvCharacterCount = (TextView)rootView.findViewById(R.id.tv_characrter_count);

        EditText edMessage =  (EditText)rootView.findViewById(R.id.ed_message);

        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharacterCount.setText(s.length()+"/250");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView tvConfirm  = (TextView)rootView.findViewById(R.id.tv_confirm_send);
        TextView tvCancel = (TextView)rootView.findViewById(R.id.tv_cancel);

        TextView tvCancelName = (TextView)rootView.findViewById(R.id.tv_cancel_name);
        TextView tvCancelStartTime = (TextView)rootView.findViewById(R.id.tv_cancel_start_time);
        tvCancelName.setText(String.format("%s%s", context.getString(R.string.cancel_call_with), name));
        tvCancelStartTime.setText(startsAt);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.cancelTask(context,taskId,edMessage.getText().toString());
                taskItemClickListener.relaodList();
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rootView);
        dialog.show();
    }




}
