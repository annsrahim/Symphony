package demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import demo.acube.application.healthcare.R;

/**
 * Created by Anns on 16/08/17.
 */

public class SetAppointmentSlotsFragment extends DialogFragment {

    public static SetAppointmentSlotsFragment newInstance() {
        return new SetAppointmentSlotsFragment();
    }

    private ListView listView;
    private View rootView;
    String[] myResArray;
    List<String> myResArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragement_set_office_hours,container,false);
        myResArray = getResources().getStringArray(R.array.array_time_slots);
        myResArrayList = Arrays.asList(myResArray);
        initUI();

        return rootView;
    }

    private void initUI()
    {
         listView = (ListView)rootView.findViewById(R.id.lv_time_slot);
        listView.setAdapter(new TimeSlotAdapter(getContext()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    public class TimeSlotAdapter extends BaseAdapter
    {
        private LayoutInflater inflater=null;
        private Context mContext;

        public TimeSlotAdapter(Context mContext) {
            inflater = ( LayoutInflater )mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return myResArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return myResArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView;
            rowView = inflater.inflate(R.layout.time_slot_item_layout,null);
            TextView tvTimeSlotValue;
            final LinearLayout llLayoutItemContainer;
            llLayoutItemContainer = (LinearLayout)rowView.findViewById(R.id.llSlotItemContainer);
            tvTimeSlotValue = (TextView)rowView.findViewById(R.id.tv_item_time_slot);
            tvTimeSlotValue.setText(myResArrayList.get(position));
            return rowView;
        }
    }
}
