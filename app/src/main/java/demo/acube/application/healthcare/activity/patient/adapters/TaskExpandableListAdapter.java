package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation.DoctorsInformationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.IExpandableChildItemListener;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Datum;

/**
 * Created by Anns on 19/07/17.
 */

public class TaskExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
    List<Datum> approvedDoctorList;
    private LayoutInflater inflater=null;
    private Context mContext;
    private Integer lastSelectedGroup = null;
    private IExpandableChildItemListener childItemListener;

    public TaskExpandableListAdapter(List<Datum> approvedDoctorList, LayoutInflater inflater, Context mContext,final IExpandableChildItemListener childItemListener) {
        this.childItemListener = childItemListener;
        this.approvedDoctorList = approvedDoctorList;
        this.inflater = inflater;
        this.mContext = mContext;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        return true;
    }


    public class Holder
    {
        TextView tvDoctorName,tvSpecialityType;
        ImageView ivContactFetures,ivAvatar;
        LinearLayout llConatactFetaureContainer,llDoctorDetailsContainer;

    }

    @Override
    public int getGroupCount() {
        return approvedDoctorList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        final View rowView;
        rowView = inflater.inflate(R.layout.layout_doctor_list_apporved,null);
        final Holder holder = new Holder();
        holder.tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_doctor_name);
        holder.tvSpecialityType = (TextView) rowView.findViewById(R.id.list_tv_speciality_type);
        holder.ivContactFetures = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);
        holder.ivAvatar  = (ImageView)rowView.findViewById(R.id.iv_doctors_icon);
        holder.ivContactFetures.setVisibility(View.VISIBLE);
        holder.llConatactFetaureContainer = (LinearLayout)rowView.findViewById(R.id.llContactFetaureContainer);
        holder.llDoctorDetailsContainer = (LinearLayout)rowView.findViewById(R.id.llDoctorDetailsContainer);

        holder.tvDoctorName.setText(String.format("Dr. %s", approvedDoctorList.get(groupPosition).getName()));
        holder.tvSpecialityType.setText(approvedDoctorList.get(groupPosition).getSpecialty().getName());
        Picasso.with(mContext).load(approvedDoctorList.get(groupPosition).getAvatar()).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(holder.ivAvatar);
        if(isExpanded)
        {
            holder.ivContactFetures.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_keyboard_arrow_up_grey_48dp));
        }
        else
        {
            holder.ivContactFetures.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_keyboard_arrow_down_grey_48dp));
        }
      holder.llDoctorDetailsContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              goToDoctorsInformation(groupPosition);
           }
       });
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorsInformation(groupPosition);
       }
        });
       return rowView;
    }

    private void goToDoctorsInformation(int pos)
    {

        Intent intent = new Intent(mContext,DoctorsInformationActivity.class);
        intent.putExtra(ValueUtils.USER_ID,approvedDoctorList.get(pos).getUserId());
        intent.putExtra(ValueUtils.USER_NAME,approvedDoctorList.get(pos).getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY,approvedDoctorList.get(pos).getSpecialty().getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY_ID,approvedDoctorList.get(pos).getSpecialty().getSpecialtyId());
        intent.putExtra(ValueUtils.USER_ADDRESS_1,approvedDoctorList.get(pos).getAddress().getStreet1());
        String address2 = approvedDoctorList.get(pos).getAddress().getCity();
        address2 += " "+approvedDoctorList.get(pos).getAddress().getState();
        address2 += " "+approvedDoctorList.get(pos).getAddress().getZip();
        address2 += " "+approvedDoctorList.get(pos).getAddress().getCountry();
        intent.putExtra(ValueUtils.USER_ADDRESS_2,address2);
        intent.putExtra(ValueUtils.USER_OFFICE_NUMBER,approvedDoctorList.get(pos).getPhone().getOffice());
        intent.putExtra(ValueUtils.USER_WEBSITE_URL,approvedDoctorList.get(pos).getWebsite());
        intent.putExtra(ValueUtils.USER_REFERAL_STATUS,ValueUtils.DoctorApproved);


        mContext.startActivity(intent);
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View childView;
        childView = inflater.inflate(R.layout.layout_expandable_list_item,null);
        ImageView btnCall = (ImageView)childView.findViewById(R.id.iv_call_btn);
        ImageView btnChat = (ImageView)childView.findViewById(R.id.iv_chat_btn);
        ImageView btnVideo = (ImageView)childView.findViewById(R.id.iv_video_btn);
        ImageView btnSchedule = (ImageView)childView.findViewById(R.id.iv_schedule_btn);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    childItemListener.goToCallOperation(groupPosition);
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childItemListener.goToChatOperation(groupPosition);
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childItemListener.goToVideoOpeartion(groupPosition);
            }
        });
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childItemListener.goToScheduleAppointment(groupPosition);
            }
        });



        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
