package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorList.DoctorsListActiviy;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;

/**
 * Created by Anns on 30/05/17.
 */

public class SecondaryDoctorListAdapter extends BaseAdapter
{
    private Context mContext;
    private static LayoutInflater inflater=null;
    private SecondaryDoctorsList mSecondaryDoctorsList;
    private GlobalApplication globalApplication;

    public SecondaryDoctorListAdapter(Context context,SecondaryDoctorsList secondaryDoctorsList)
    {
        this.mContext = context;
        this.mSecondaryDoctorsList = secondaryDoctorsList;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        globalApplication = (GlobalApplication)context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return mSecondaryDoctorsList.getMeta().getPagination().getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView mDocotorName,mDoctorSpeciality;
        ImageView mCheckMark;
        LinearLayout llDocotorDetailsContainer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        final int currentPositon = position;
        View rowView;
        rowView = inflater.inflate(R.layout.doctors_item_list,null);
        holder.mDocotorName = (TextView)rowView.findViewById(R.id.list_tv_doctor_name);
        holder.mDoctorSpeciality = (TextView)rowView.findViewById(R.id.list_tv_speciality_type);
        holder.llDocotorDetailsContainer = (LinearLayout)rowView.findViewById(R.id.llDoctorDetailsContainer);
        holder.mCheckMark = (ImageView)rowView.findViewById(R.id.list_iv_checkmark_call);

        holder.mDocotorName.setText(mSecondaryDoctorsList.getData().get(position).getName());
        holder.mDoctorSpeciality.setText(mSecondaryDoctorsList.getData().get(position).getSpecialty().getName());
        if(globalApplication.getPatSelectedCallerId()==mSecondaryDoctorsList.getData().get(position).getUserId())
        {
            holder.mCheckMark.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mCheckMark.setVisibility(View.GONE);
        }
        holder.llDocotorDetailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalApplication.setPatSelectedCallerId(mSecondaryDoctorsList.getData().get(currentPositon).getUserId());
                globalApplication.setPatSelectedCallerName(mSecondaryDoctorsList.getData().get(currentPositon).getName());
                ((DoctorsListActiviy)mContext).finish();
            }
        });


        return rowView;
    }


}
