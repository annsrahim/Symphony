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
import demo.acube.application.healthcare.model.secondaryDoctorsList.Datum;

/**
 * Created by Anns on 10/08/17.
 */

public class ApprovedSpecialistAdapter extends BaseAdapter {

    private List<Datum> approvedDoctors;
    private Context mContext;
    private LayoutInflater inflater;

    public ApprovedSpecialistAdapter(Context mContext,List<Datum> approvedDoctors) {
        this.approvedDoctors = approvedDoctors;
        this.mContext = mContext;
        inflater = (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class Holder
    {
        TextView tvDoctorName,tvDoctorSepcialityName;
        ImageView ivExpandView,ivDoctorsAvatar;

    }


    @Override
    public int getCount() {
        return approvedDoctors.size();
    }

    @Override
    public Object getItem(int position) {
        return approvedDoctors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.layout_doctor_list_apporved,null);
        holder.tvDoctorName = (TextView)rowView.findViewById(R.id.list_tv_doctor_name);
        holder.tvDoctorSepcialityName = (TextView)rowView.findViewById(R.id.list_tv_speciality_type);
        holder.ivExpandView = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);
        holder.ivDoctorsAvatar = (ImageView)rowView.findViewById(R.id.iv_doctors_icon);

        holder.tvDoctorName.setText(approvedDoctors.get(position).getName());
        holder.tvDoctorSepcialityName.setText(approvedDoctors.get(position).getSpecialty().getName());
        holder.ivExpandView.setVisibility(View.GONE);
        Picasso.with(mContext).load(approvedDoctors.get(position).getAvatar()).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(holder.ivDoctorsAvatar);

        return rowView;
    }
}
