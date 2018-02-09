package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.chatHistoryModel.Datum;

/**
 * Created by Anns on 02/08/17.
 */

public class ChatHistoryRecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Datum> chatHistoryList;
    private Context mContext;
    private LayoutInflater inflater;
    private String userType;
    private String lastChatDateHour ="";
    private Date lastChatDate;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ChatHistoryRecyclerAdapter(List<Datum> chatHistoryList, Context mContext,String userType) {
        this.chatHistoryList = chatHistoryList;
        this.mContext = mContext;
        inflater =   (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userType = userType;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatContent;
        TextView tvChatDateTime;

        public MyViewHolder(View view) {
            super(view);
            tvChatContent = (TextView) view.findViewById(R.id.tv_list_chat_content);
            tvChatDateTime = (TextView) view.findViewById(R.id.tv_list_chat_date);

        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvLoadMoreMessage;
        public HeaderViewHolder(View header)
        {
            super(header);
            tvLoadMoreMessage = (TextView)header.findViewById(R.id.tv_load_more_message);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            View headerView  = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_header_view_item,parent,false);
            return new HeaderViewHolder(headerView);

        }
        else if(viewType == TYPE_ITEM)
        {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_chat_history, parent, false);

            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType + ".");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder)
        {
            boolean isMessageAvailable = ((StartChatOperationActivity)mContext).isMoreMessageAvailable();
            if(!isMessageAvailable)
                ((HeaderViewHolder)holder).tvLoadMoreMessage.setVisibility(View.GONE);
            else
                ((HeaderViewHolder)holder).tvLoadMoreMessage.setVisibility(View.GONE);
        }

        else if(holder instanceof MyViewHolder)
        {
            int currentPosition = position-1;
            long sentAtTimeMillis = chatHistoryList.get(currentPosition).getSentAt();
            String currentChatTime = getChatTime(sentAtTimeMillis);
            ((MyViewHolder)holder).tvChatContent.setText(chatHistoryList.get(currentPosition).getMessage());

            if(!lastChatDateHour.equals(currentChatTime))
            {
                ((MyViewHolder)holder).tvChatDateTime.setVisibility(View.VISIBLE);
                lastChatDateHour = currentChatTime;
                ((MyViewHolder)holder).tvChatDateTime.setText(Utils.getFormattableDate(sentAtTimeMillis*1000));

            }
            else
                ((MyViewHolder)holder).tvChatDateTime.setVisibility(View.GONE);
            if(chatHistoryList.get(currentPosition).getUser().getUserType().equals(userType))
            {
                ((MyViewHolder)holder).tvChatContent.setBackgroundColor(ContextCompat.getColor(mContext,R.color.light_grey));
                ((MyViewHolder)holder).tvChatDateTime.setBackgroundColor(ContextCompat.getColor(mContext,R.color.light_grey));
                ((MyViewHolder)holder).tvChatContent.setGravity(Gravity.END);

            }
            else
            {
                ((MyViewHolder)holder).tvChatContent.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White));
                ((MyViewHolder)holder).tvChatDateTime.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White));
                ((MyViewHolder)holder).tvChatContent.setGravity(Gravity.START);
            }
        }
    }



    @Override
    public int getItemCount() {
        return chatHistoryList.size()+1;
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
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
