package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation.DoctorsInformationActivity;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Datum;

/**
 * Created by Anns on 19/07/17.
 */

public class TeleHealthDocotorListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater=null;
    List<Datum> approvedDoctorList = new ArrayList<>();

    public TeleHealthDocotorListAdapter(Context mContext, LayoutInflater inflater, List<Datum> approvedDoctorList) {
        this.mContext = mContext;
        this.inflater = inflater;
        this.approvedDoctorList = approvedDoctorList;
    }



    public class Holder
    {
        TextView tvDoctorName,tvSpecialityType;
        ImageView ivExpandView,ivAvatar;
        LinearLayout llConatactFetaureContainer;


    }
    @Override
    public int getCount() {
        return approvedDoctorList.size();
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
        rowView = inflater.inflate(R.layout.layout_doctor_list_apporved,null);
        final Holder holder = new Holder();
        holder.tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_doctor_name);
        holder.tvSpecialityType = (TextView) rowView.findViewById(R.id.list_tv_speciality_type);
        holder.ivExpandView = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);



        holder.ivExpandView.setVisibility(View.GONE);
        holder.tvDoctorName.setText(String.format("Dr. %s", approvedDoctorList.get(position).getName()));
        holder.tvSpecialityType.setText(approvedDoctorList.get(position).getSpecialty().getName());
        holder.ivAvatar = (ImageView)rowView.findViewById(R.id.iv_doctors_icon);
        Picasso.with(mContext).load(approvedDoctorList.get(position).getAvatar()).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(holder.ivAvatar);

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
        intent.putExtra(ValueUtils.USER_REFERAL_STATUS,ValueUtils.DoctorPending);


        mContext.startActivity(intent);
    }
}
