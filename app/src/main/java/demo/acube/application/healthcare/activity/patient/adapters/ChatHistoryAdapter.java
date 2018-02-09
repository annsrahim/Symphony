package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.chatHistoryModel.Datum;

/**
 * Created by Anns on 02/08/17.
 */

public class ChatHistoryAdapter extends BaseAdapter {

    private List<Datum> chatHistoryList;
    private Context mContext;
    private LayoutInflater inflater;
    private String userType;
    private String lastChatDateHour ="";
    private Date lastChatDate;



    public ChatHistoryAdapter(List<Datum> chatHistoryList, Context mContext,String userType) {
        this.chatHistoryList = chatHistoryList;
        this.mContext = mContext;
        inflater =   (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userType = userType;
    }

    public class Holder
    {
        TextView tvChatContent;
        TextView tvChatDateTime;
    }



    @Override
    public int getCount() {
        return chatHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatHistoryList.get(position);
    }
    @Override
    public int getViewTypeCount() {
        if(getCount()<1)
            return 1;
        else
            return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

            convertView = inflater.inflate(R.layout.layout_chat_history,null);
             holder = new Holder();
            holder.tvChatContent = (TextView)convertView.findViewById(R.id.tv_list_chat_content);
            holder.tvChatDateTime= (TextView)convertView.findViewById(R.id.tv_list_chat_date);
            holder.tvChatContent.setText(chatHistoryList.get(position).getMessage());
            long sentAtTimeMillis = chatHistoryList.get(position).getSentAt();
            String currentChatTime = getChatTime(sentAtTimeMillis);
            Log.d("Check","Last "+ lastChatDateHour +" Current "+currentChatTime);
            if(!lastChatDateHour.equals(currentChatTime))
            {
                holder.tvChatDateTime.setVisibility(View.VISIBLE);
                lastChatDateHour = currentChatTime;
                holder.tvChatDateTime.setText(Utils.getFormattableDate(sentAtTimeMillis*1000));

            }
            else
                holder.tvChatDateTime.setVisibility(View.GONE);
            if(chatHistoryList.get(position).getUser().getUserType().equals(userType))
            {
                holder.tvChatContent.setBackgroundColor(ContextCompat.getColor(mContext,R.color.light_grey));
                holder.tvChatDateTime.setBackgroundColor(ContextCompat.getColor(mContext,R.color.light_grey));
                holder.tvChatContent.setGravity(Gravity.END);

            }
            else
            {
                holder.tvChatContent.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White));
                holder.tvChatDateTime.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White));
                holder.tvChatContent.setGravity(Gravity.START);
            }


        return convertView;
    }

    private String getChatTime(long timeInMillis) {
        timeInMillis = timeInMillis*1000;
        String chatTime = "";
        Calendar calendar = Utils.getDateTimeFromMilliseconds(timeInMillis);
        int Year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        lastChatDate = new Date();
        lastChatDate.before(calendar.getTime());
        chatTime = Year+"-"+month+"-"+dayOfMonth+"-"+hour;
        return chatTime;
    }



}
