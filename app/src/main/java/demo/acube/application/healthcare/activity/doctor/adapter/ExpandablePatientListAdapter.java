package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.patientInformation.PatientInformationActivity;
import demo.acube.application.healthcare.activity.doctor.activity.primarySecondaryPatientList.PrimarySecondryPatientListActivity;
import demo.acube.application.healthcare.activity.doctor.models.primaryPatientList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.primaryPatientList.PendingReferralRequest;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;

/**
 * Created by Anns on 25/07/17.
 */

public class ExpandablePatientListAdapter extends BaseExpandableListAdapter {

    List<Datum> patientList;
    private Context mContext;
    private LayoutInflater inflater=null;
    private boolean isPrimary = false;


    public ExpandablePatientListAdapter(List<Datum> patientList, Context mContext, LayoutInflater inflater, boolean isPrimary) {
        this.patientList = patientList;
        this.mContext = mContext;
        this.inflater = inflater;
        this.isPrimary = isPrimary;
    }

    public class Holder
    {
        TextView tvPatientName,tvRequestCount;
        ImageView ivExpandView,ivAvatar;
        LinearLayout llPatientDetailsContainer;


    }

    @Override
    public int getGroupCount() {
        return patientList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(patientList.get(groupPosition).getPendingReferralRequests().size()>0)
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
        holder.tvPatientName = (TextView) rowView.findViewById(R.id.list_tv_patient_name);
        holder.tvRequestCount = (TextView) rowView.findViewById(R.id.tv_request_count);
        holder.ivExpandView = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);
        holder.ivAvatar  = (ImageView)rowView.findViewById(R.id.list_iv_patient_icon);
        holder.tvPatientName.setText(patientList.get(groupPosition).getName());
        holder.llPatientDetailsContainer = (LinearLayout)rowView.findViewById(R.id.llPatientDetailsContainer);

        Picasso.with(mContext).load(patientList.get(groupPosition).getAvatar()).placeholder(R.drawable.gpc_placeholder_patient)
                .into(holder.ivAvatar);
        if(patientList.get(groupPosition).getPendingReferralRequests().size()==0)
        {
             holder.ivExpandView.setVisibility(View.GONE);
            holder.tvRequestCount.setVisibility(View.GONE);
        }
        else
        {
            holder.tvRequestCount.setText(String.valueOf(patientList.get(groupPosition).getPendingReferralRequests().size()));
        }
        if(isExpanded)
            holder.ivExpandView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_up_ref));
        else
            holder.ivExpandView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_down_ref));

        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientInformation(groupPosition);
            }
        });
        holder.llPatientDetailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientInformation(groupPosition);
            }
        });

        return rowView;
    }

    public void goToPatientInformation(int groupPos)
    {
        Intent intent = new Intent(mContext, PatientInformationActivity.class);
        intent.putExtra(ValueUtils.USER_ID,patientList.get(groupPos).getUserId());
        intent.putExtra(ValueUtils.USER_NAME,patientList.get(groupPos).getName());
        intent.putExtra(ValueUtils.USER_AGE,patientList.get(groupPos).getAge());
        intent.putExtra(ValueUtils.GENDER,patientList.get(groupPos).getGender());
        intent.putExtra(ValueUtils.USER_AVATAR,patientList.get(groupPos).getAvatar());
        intent.putExtra(ValueUtils.PRIMARY_DOCTOR_ID,patientList.get(groupPos).getPrimaryDoctor().getUserId());
        intent.putExtra(ValueUtils.PRIMARY_DOCTOR_SPECIALITY_TYPE,patientList.get(groupPos).getPrimaryDoctor().getSpecialty().getName());
        intent.putExtra(ValueUtils.PRIMARY_DOCTOR_AVATAR,patientList.get(groupPos).getPrimaryDoctor().getAvatar());
        intent.putExtra(ValueUtils.PRIMARY_DOCTOR_NAME,patientList.get(groupPos).getPrimaryDoctor().getName());
        mContext.startActivity(intent);
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
        PendingReferralRequest pendingReferralRequest = patientList.get(groupPosition).getPendingReferralRequests().get(childPosition);
        tvDoctorName.setText(pendingReferralRequest.getSpecialistDoctor().getName());
        tvDoctorType.setText(pendingReferralRequest.getSpecialistDoctor().getSpecialty().getName());
        tvRequestNote.setText(pendingReferralRequest.getRequestReason());
        tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrimarySecondryPatientListActivity)mContext).rejectRequest(groupPosition,childPosition);
            }
        });
        tvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrimarySecondryPatientListActivity)mContext).approveRequest(groupPosition,childPosition);
            }
        });

        Picasso.with(mContext).load(pendingReferralRequest.getSpecialistDoctor().getAvatar()).placeholder(R.drawable.gpc_placeholder_patient)
                .into(ivDoctorIcon);
        return childRowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
