package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation.DoctorsInformationActivity;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.Doctor;

/**
 * Created by Anns on 19/07/17.
 */

public class TeleHealthAllDoctorListAdapter extends BaseAdapter {
    private List<Doctor> doctorList;
    private LayoutInflater inflater;
    private Context mContext;
    private Integer previousspecialityId;

    public TeleHealthAllDoctorListAdapter(List<Doctor> doctorList, Context mContext) {
        this.doctorList = doctorList;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
    }

    public class Holder
    {
        TextView tvDoctorName,tvSpecialityType;
        ImageView ivAvatar;


    }
    @Override
    public int getCount() {
        return doctorList.size();
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
        rowView = inflater.inflate(R.layout.layout_all_doctor_list,null);
        final Holder holder = new Holder();
        holder.tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_doctor_name);
        holder.tvSpecialityType = (TextView) rowView.findViewById(R.id.list_tv_speciality_type);


        holder.tvDoctorName.setText(String.format("Dr. %s", doctorList.get(position).getName()));
        holder.ivAvatar = (ImageView) rowView.findViewById(R.id.iv_doctors_icon);
        Picasso.with(mContext).load(doctorList.get(position).getAvatar()).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(holder.ivAvatar);
        if(position==0)
        {
            previousspecialityId = doctorList.get(position).getSpecialty().getSpecialtyId();
            holder.tvSpecialityType.setText(doctorList.get(position).getSpecialty().getName());
            holder.tvSpecialityType.setVisibility(View.VISIBLE);
        }
        else
        {
            int currentSpecialityId = doctorList.get(position).getSpecialty().getSpecialtyId();
            if(previousspecialityId!=currentSpecialityId)
            {
                previousspecialityId=currentSpecialityId;
                holder.tvSpecialityType.setText(doctorList.get(position).getSpecialty().getName());
                holder.tvSpecialityType.setVisibility(View.VISIBLE);
            }
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorsInformation(currentPos);
            }
        });



        return rowView;
    }

    private void goToDoctorsInformation(int pos)
    {

        Intent intent = new Intent(mContext,DoctorsInformationActivity.class);
        intent.putExtra(ValueUtils.USER_ID,doctorList.get(pos).getUserId());
        intent.putExtra(ValueUtils.USER_NAME,doctorList.get(pos).getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY,doctorList.get(pos).getSpecialty().getName());
        intent.putExtra(ValueUtils.USER_SPECIALITY_ID,doctorList.get(pos).getSpecialty().getSpecialtyId());
        intent.putExtra(ValueUtils.USER_ADDRESS_1,doctorList.get(pos).getAddress().getStreet1());
        String address2 = doctorList.get(pos).getAddress().getCity();
        address2 += " "+doctorList.get(pos).getAddress().getState();
        address2 += " "+doctorList.get(pos).getAddress().getZip();
        address2 += " "+doctorList.get(pos).getAddress().getCountry();
        intent.putExtra(ValueUtils.USER_ADDRESS_2,address2);
        intent.putExtra(ValueUtils.USER_OFFICE_NUMBER,doctorList.get(pos).getPhone().getOffice());
        intent.putExtra(ValueUtils.USER_WEBSITE_URL,doctorList.get(pos).getWebsite());
        intent.putExtra(ValueUtils.USER_REFERAL_STATUS,ValueUtils.DoctorExplore);


        mContext.startActivity(intent);
    }
}
