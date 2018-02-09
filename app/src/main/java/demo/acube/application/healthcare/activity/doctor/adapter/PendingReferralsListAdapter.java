package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.patientInformation.PatientInformationActivity;

import demo.acube.application.healthcare.activity.doctor.models.referralBean.Datum;

/**
 * Created by Anns on 25/07/17.
 */

public class PendingReferralsListAdapter extends BaseExpandableListAdapter {

    List<Datum> patientList;
    private Context mContext;
    private LayoutInflater inflater=null;




    public PendingReferralsListAdapter(List<Datum> patientList, Context mContext, LayoutInflater inflater) {
        this.patientList = patientList;
        this.mContext = mContext;
        this.inflater = inflater;


    }

    public class Holder
    {
        TextView tvDoctorName,tvRequestCount,tvDoctorType;
        ImageView ivExpandView,ivAvatar;
        LinearLayout llPatientDetailsContainer;


    }

    @Override
    public int getGroupCount() {
        return patientList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      if(patientList.size()>0)
            return 1;
        else
            return 0;
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

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final View rowView;
        rowView = inflater.inflate(R.layout.layout_expandable_patient_list,null);
        final Holder holder = new Holder();
        holder.tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_patient_name);
        holder.tvRequestCount = (TextView) rowView.findViewById(R.id.tv_request_count);
        holder.ivExpandView = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);
        holder.ivAvatar  = (ImageView)rowView.findViewById(R.id.list_iv_patient_icon);
        holder.tvDoctorName.setText(patientList.get(groupPosition).getSpecialistDoctor().getName());
        holder.llPatientDetailsContainer = (LinearLayout)rowView.findViewById(R.id.llPatientDetailsContainer);
        holder.tvDoctorType = (TextView)rowView.findViewById(R.id.list_tv_speciality_type);

        holder.tvDoctorType.setVisibility(View.VISIBLE);
        holder.tvDoctorType.setText(patientList.get(groupPosition).getSpecialistDoctor().getSpecialty().getName());

        Picasso.with(mContext).load(patientList.get(groupPosition).getSpecialistDoctor().getAvatar()).placeholder(R.drawable.gpc_placeholder_patient)
                .into(holder.ivAvatar);

        if(isExpanded)
            holder.ivExpandView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_up_ref));
        else
            holder.ivExpandView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_down_ref));



        return rowView;
    }



    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View childRowView;
        childRowView = inflater.inflate(R.layout.layout_request_expand_item,null);
        TextView tvDoctorName = (TextView)childRowView.findViewById(R.id.list_child_doctor_name);
        TextView tvDoctorType = (TextView)childRowView.findViewById(R.id.list_child_doctor_type);
        TextView tvRequestNote = (TextView)childRowView.findViewById(R.id.list_child_request_notes);
        TextView tvReject = (TextView)childRowView.findViewById(R.id.list_tv_child_reject);
        TextView tvApprove = (TextView)childRowView.findViewById(R.id.list_tv_child_approve);
        ImageView ivDoctorIcon = (ImageView)childRowView.findViewById(R.id.list_child_doctor_icon);
        RelativeLayout rlDoctorDetailsContainer = (RelativeLayout)childRowView.findViewById(R.id.rl_doctor_details_container);

        rlDoctorDetailsContainer.setVisibility(View.GONE);


        tvRequestNote.setText(patientList.get(groupPosition).getRequestReason());
        tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PatientInformationActivity)mContext).rejectRequest(groupPosition,childPosition);
            }
        });
        tvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PatientInformationActivity)mContext).approveRequest(groupPosition,childPosition);
            }
        });


        return childRowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
