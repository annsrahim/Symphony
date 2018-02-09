package demo.acube.application.healthcare.activity.patient.acitivity.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorList.DoctorsListActiviy;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class ChatHomeActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    private TextView tvSelectCallerName;
    private Integer chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        initToolBar();
        initUI();

    }

    private void initUI()
    {
        tvSelectCallerName = (TextView)findViewById(R.id.tvSelectedCallerName);
        if(globalApplication.getPatSelectedCallerId()==0)
        {
            globalApplication.setPatSelectedCallerId(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getUserId());
            globalApplication.setPatSelectedCallerName(globalApplication.getUserDetailsBean().getData().getPrimaryDoctor().getName());

        }
        updateCallerId();
    }

    public void doShowDoctorsList(View view)
    {
        Intent intent = new Intent(this, DoctorsListActiviy.class);
        intent.putExtra(ValueUtils.isFrom,ValueUtils.CHAT_NOW_ACTIVITY);
        startActivity(intent);

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
    private void updateCallerId()
    {
        tvSelectCallerName.setText(globalApplication.getPatSelectedCallerName());
    }

    @Override
    protected void onResume() {
        updateCallerId();
        getChatInformation();
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recent,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_recent:
                    goToRecentChats();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToRecentChats() {
        Intent intent =  new Intent(this,RecentChatActivity.class);
        startActivity(intent);
    }

    public void getChatInformation()
    {
        progressDialog = LoadingDialog.show(this,"Fetching data...");
        UserDetailsBean userDetailBean = globalApplication.getUserDetailsBean();
        int patientId = userDetailBean.getData().getUserId();
        int doctorId = globalApplication.getPatSelectedCallerId();
        int creatorId = patientId;
        final Call<ChatInitiateModel> chatModel = apiInterface.doInitiateChatOperation(patientId,doctorId,creatorId);
        chatModel.enqueue(new Callback<ChatInitiateModel>() {
            @Override
            public void onResponse(Call<ChatInitiateModel> call, Response<ChatInitiateModel> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    chatId = response.body().getData().getChatId();
                }
                else
                {
                    Utils.showToast(ChatHomeActivity.this,"Unable to fetch data please try again");
                }
            }

            @Override
            public void onFailure(Call<ChatInitiateModel> call, Throwable t) {
                Utils.showToast(ChatHomeActivity.this,"Unable to fetch data :"+t.getLocalizedMessage());
            }
        });
    }

    public void initiateChatOperation(View view)
    {
        if(chatId!=null)
        {
            Intent intent = new Intent(this, StartChatOperationActivity.class);
            intent.putExtra(ValueUtils.ChatId,chatId);
            startActivity(intent);
        }
    }



}
