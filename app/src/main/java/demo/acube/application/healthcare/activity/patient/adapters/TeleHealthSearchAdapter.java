package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.model.userDetails.PrimaryDoctor;

/**
 * Created by Anns on 03/08/17.
 */

public class TeleHealthSearchAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PrimaryDoctor> doctorsList = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    public TeleHealthSearchAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addItem(final PrimaryDoctor item) {
        doctorsList.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        PrimaryDoctor doctorHeader = new PrimaryDoctor();
        doctorHeader.setName(item);
        doctorsList.add(doctorHeader);
        sectionHeader.add(doctorsList.size() - 1);
        notifyDataSetChanged();
    }

    public void clearAllDatas()
    {
        sectionHeader.clear();
        doctorsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return doctorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        SectionViewHolder sectionViewHolder = null;
        int rowType = getItemViewType(position);

        if(convertView==null)
        {
            holder = new ItemViewHolder();
            sectionViewHolder = new SectionViewHolder();
            switch (rowType)
            {
                case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.layout_doctor_list_apporved,null);
                        holder.tvDoctorName = (TextView) convertView.findViewById(R.id.list_tv_doctor_name);
                        holder.tvDoctorSpeciality = (TextView) convertView.findViewById(R.id.list_tv_speciality_type);
                        holder.ivExpandView = (ImageView)convertView.findViewById(R.id.list_iv_expand_view);
                        holder.ivDoctorAvatar = (ImageView)convertView.findViewById(R.id.iv_doctors_icon);

                        holder.ivExpandView.setVisibility(View.GONE);
                        holder.tvDoctorName.setText(doctorsList.get(position).getName());
                        holder.tvDoctorSpeciality.setText(doctorsList.get(position).getSpecialty().getName());
                        convertView.setTag(holder);
                    break;
                case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.list_section_view_item,null);
                        sectionViewHolder.tvSectionName = (TextView)convertView.findViewById(R.id.tv_section_name);
                        sectionViewHolder.tvSectionName.setText(doctorsList.get(position).getName());
                        convertView.setTag(sectionViewHolder);
                    break;
            }
        }
        else
        {
            switch (rowType)
            {
                case TYPE_ITEM:
                        holder = (ItemViewHolder)convertView.getTag();
                        holder.ivExpandView.setVisibility(View.GONE);
                        holder.tvDoctorName.setText(doctorsList.get(position).getName());
                        holder.tvDoctorSpeciality.setText(doctorsList.get(position).getSpecialty().getName());
                    break;
                case TYPE_SEPARATOR:
                         sectionViewHolder = (SectionViewHolder) convertView.getTag();
                        sectionViewHolder.tvSectionName = (TextView)convertView.findViewById(R.id.tv_section_name);
                        sectionViewHolder.tvSectionName.setText(doctorsList.get(position).getName());
                        convertView.setTag(sectionViewHolder);
                    break;
            }

        }

         return convertView;






    }

    public static class ItemViewHolder
    {
        public TextView tvDoctorName;
        public  TextView tvDoctorSpeciality;
        public ImageView ivDoctorAvatar,ivExpandView;
    }

    public static class SectionViewHolder
    {
        public TextView tvSectionName;
    }


}
