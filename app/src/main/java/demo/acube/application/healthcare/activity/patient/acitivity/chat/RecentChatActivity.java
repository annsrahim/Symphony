package demo.acube.application.healthcare.activity.patient.acitivity.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.recentChat.Datum;
import demo.acube.application.healthcare.model.recentChat.RecentChatBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class RecentChatActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    List<Datum> recentChatList = new ArrayList<>();
    RecentChatListAdapter recentChatListAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        recentChatListAdapter = new RecentChatListAdapter(this);
        initToolBar();
        initUi();
        getRecentChats();
                
    }

    private void initUi() {
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(recentChatListAdapter);
        recentChatListAdapter.notifyDataSetChanged();
    }

    private void getRecentChats() {
        progressDialog = LoadingDialog.show(this,"Fetching data...");
        int userID = globalApplication.getUserId();
        Call<RecentChatBean> recentChatBeanCall = apiInterface.doGetRecentChats(userID);
        recentChatBeanCall.enqueue(new Callback<RecentChatBean>() {
            @Override
            public void onResponse(Call<RecentChatBean> call, Response<RecentChatBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    recentChatList.addAll(response.body().getData());
                    recentChatListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(RecentChatActivity.this,"Unable to get Data");
                }
            }

            @Override
            public void onFailure(Call<RecentChatBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(RecentChatActivity.this,"Unable to get Data "+t.getLocalizedMessage());

            }
        });
    }

    private void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Text");
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_orange, null));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public class RecentChatListAdapter extends BaseAdapter
    {
        Context mContext;
        LayoutInflater inflater;
        public RecentChatListAdapter(Context context) {
            this.mContext = context;
            inflater =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return recentChatList.size();
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
            rowView = inflater.inflate(R.layout.layout_recent_chat_item,null);
            TextView tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_doctor_name);
            TextView tvSpecialityType = (TextView) rowView.findViewById(R.id.list_tv_doctor_speciality);
            TextView tvChatTime = (TextView) rowView.findViewById(R.id.list_tv_chat_time);
            ImageView ivAvatar = (ImageView) rowView.findViewById(R.id.iv_doctors_icon);
            Long chatTimeMillis = recentChatList.get(position).getLastActivityAt().longValue();


            String chatTime = Utils.getTimeSpanAgo(chatTimeMillis);
            tvChatTime.setText(chatTime);
            tvDoctorName.setText(recentChatList.get(position).getDoctor().getName());
            tvSpecialityType.setText(recentChatList.get(position).getDoctor().getSpecialty().getName());

            String imagePath  = recentChatList.get(position).getDoctor().getAvatar();
            Picasso.with(mContext).load(imagePath).placeholder(R.drawable.gpc_placeholder_patient)
                    .into(ivAvatar);

            return rowView;

        }
    }
}
