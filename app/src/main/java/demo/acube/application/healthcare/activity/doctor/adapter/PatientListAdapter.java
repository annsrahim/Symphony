package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import demo.acube.application.healthcare.R;

import demo.acube.application.healthcare.activity.doctor.activity.patientList.PatientListActivity;
import demo.acube.application.healthcare.activity.doctor.models.patientList.PrimaryPatient;


import demo.acube.application.healthcare.helper.utilities.GlobalApplication;


/**
 * Created by Anns on 13/07/17.
 */

public class PatientListAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater=null;
    private List<PrimaryPatient> primaryPatient;
    private GlobalApplication globalApplication;
    public PatientListAdapter(Context context,List<PrimaryPatient> primaryPatient)
    {
        this.mContext = context;
        this.primaryPatient = primaryPatient;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        globalApplication = (GlobalApplication)context.getApplicationContext();
    }

    public class Holder
    {
        TextView mPatientName;
        ImageView mCheckMark;
        LinearLayout llPateintDetailsContainer;
    }

    @Override
    public int getCount() {
        return primaryPatient.size();
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

        Holder holder = new Holder();
        final int currentPositon = position;
        final PrimaryPatient patient = primaryPatient.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.patient_item_list,null);
        holder.mPatientName = (TextView)rowView.findViewById(R.id.list_tv_patient_name);
        holder.llPateintDetailsContainer = (LinearLayout)rowView.findViewById(R.id.llPatientDetailsContainer);
        holder.mCheckMark = (ImageView)rowView.findViewById(R.id.list_iv_checkmark_call);
        holder.mPatientName.setText(patient.getName());
        if(globalApplication.getPatSelectedCallerId()==patient.getUserId())
        {
            holder.mCheckMark.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mCheckMark.setVisibility(View.GONE);
        }
        holder.llPateintDetailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalApplication.setPatSelectedCallerId(patient.getUserId());
                globalApplication.setPatSelectedCallerName(patient.getName());
                ((PatientListActivity)mContext).finish();
            }
        });


        return rowView;
    }
}
