package demo.acube.application.healthcare.activity.patient.acitivity.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.adapters.ChatHistoryRecyclerAdapter;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.chatHistoryModel.ChatHistoryBean;
import demo.acube.application.healthcare.model.chatHistoryModel.Datum;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class StartChatOperationActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    ChatHistoryBean chatHistory;
    public int chatId;
    private TextView tvSend,tvMessageCount;
    private EditText edMessage;
    private int messageCount=0;
    private int totalMessageWordsCount =300;
    private boolean isCheckingMessage = false;
    private ScrollView scrollView;
    private Integer totalCount=0;
    ChatHistoryRecyclerAdapter chatHistoryAdapter;
    private List<Datum> chatHistoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;

    private boolean isFirstTime = true;
    private Disposable disposable;
    int totalPages = 0;
    int currentPages = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat_operation);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.disableKeyboardPopup(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        Intent intent = getIntent();
        chatId = intent.getIntExtra(ValueUtils.ChatId,0);
        initToolBar();
        initUI();

        updateMessageCount(messageCount);

    }




    private void updateMessageCount(int messageCount)
    {
        tvMessageCount.setText(messageCount+"/"+ totalMessageWordsCount);
    }

    private void initUI() {

        chatHistoryAdapter = new ChatHistoryRecyclerAdapter(chatHistoryList,this,globalApplication.getUserType());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatHistoryAdapter);


        tvSend = (TextView)findViewById(R.id.tvSend);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageCount>0 && messageCount<=300)
                {
                    sendMessage();
                }

            }
        });
        edMessage = (EditText)findViewById(R.id.tvMessage);
        tvMessageCount = (TextView)findViewById(R.id.tvMessageCount);
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                messageCount=count;
                updateMessageCount(count);
                if(count==0)
                    tvSend.setTextColor(ContextCompat.getColor(StartChatOperationActivity.this,R.color.light_grey_3));
                else
                    tvSend.setTextColor(ContextCompat.getColor(StartChatOperationActivity.this,R.color.orange_text_color));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sendMessage()
    {
        progressDialog = LoadingDialog.show(this,"posting");
        int userId = globalApplication.getUserId();
        final String message = edMessage.getText().toString();
        Call<Success> sendMessageCall = apiInterface.doPostChatHistory(chatId,userId,message);
        sendMessageCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {

                    edMessage.setText("");
                    messageCount = 0;
                }
                else
                {
                    Utils.showToast(StartChatOperationActivity.this,"Unable to post last message please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(StartChatOperationActivity.this,"Unable to post last message "+t.getLocalizedMessage());
            }
        });
    }

    public void getChatHistoryWithIntervals()
    {
        if(isFirstTime)
        {
            progressDialog = LoadingDialog.show(this,"Fetching datas");

        }

        Observable.interval(3, TimeUnit.SECONDS)
                .flatMap(aLong -> apiInterface.doGetChatHistoryPageByPage(chatId))
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayChatHistory, this::handleErrors);

    }

    private void handleErrors(Throwable throwable)
    {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
        Utils.showToast(this,"Unable to get data"+throwable.getLocalizedMessage());
    }

    private void displayChatHistory(ChatHistoryBean chatHistoryBean)
    {
        Log.d("Chat ID",chatId+" Chat");
        if(progressDialog!=null && progressDialog.isShowing())
        {
            isFirstTime = false;
            progressDialog.dismiss();
            chatHistoryList.addAll(chatHistoryBean.getData());
            chatHistoryAdapter.notifyDataSetChanged();
        }
        else
        {
            List<Datum> newChatList = new ArrayList<>();
            newChatList.addAll(chatHistoryBean.getData());
            int newChatListLastPos = newChatList.size()-1;
            int oldChatListLastPos = chatHistoryList.size()-1;
            int newChatListLastMessageId = newChatList.get(newChatListLastPos).getMessageId();
            int oldChatListLastMessageId = chatHistoryList.get(oldChatListLastPos).getMessageId();
            if(newChatListLastMessageId>oldChatListLastMessageId)
            {
                chatHistoryList.clear();
                chatHistoryList.addAll(chatHistoryBean.getData());
                chatHistoryAdapter.notifyDataSetChanged();
            }

        }


    }



    public void getChatHistory()
    {
            progressDialog = LoadingDialog.show(this,"Fetching datas");

        Call<ChatHistoryBean> chatHistoryBeanCall = apiInterface.doGetChatHistory(chatId);
        chatHistoryBeanCall.enqueue(new Callback<ChatHistoryBean>() {
            @Override
            public void onResponse(Call<ChatHistoryBean> call, Response<ChatHistoryBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    isCheckingMessage = false;
                    chatHistory = response.body();
                    if(chatHistory.getMeta().getPagination().getCount()>totalCount)
                    {
                        totalPages = chatHistory.getMeta().getPagination().getTotalPages();
                        currentPages = chatHistory.getMeta().getPagination().getCurrentPage();
                        chatHistoryList.addAll(chatHistory.getData());
                        displayChatHistory();

                    }
                }
            }

            @Override
            public void onFailure(Call<ChatHistoryBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void checkForUpdatedMessages() {

    }


    private void displayChatHistory()
    {
        Collections.reverse(chatHistoryList);
        chatHistoryAdapter.notifyDataSetChanged();
    }




    private void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(globalApplication.getPatSelectedCallerType().equals(ValueUtils.doctor))
            toolbar.setTitle("Dr. "+globalApplication.getPatSelectedCallerName());
        else
            toolbar.setTitle(globalApplication.getPatSelectedCallerName());
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_orange, null));
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.White,null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onPause() {
        if(disposable!=null)
        {
            disposable.dispose();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        getChatHistoryWithIntervals();
        super.onResume();

    }




//    public void getUpdatedChatHsitory()
//    {
//        isCheckingMessage = true;
//
//
//        Call<ChatHistoryBean> chatHistoryBeanCall = apiInterface.doGetChatHistory(chatId);
//        chatHistoryBeanCall.enqueue(new Callback<ChatHistoryBean>() {
//            @Override
//            public void onResponse(Call<ChatHistoryBean> call, Response<ChatHistoryBean> response) {
//
//                if(response.code()==200)
//                {
//                    isCheckingMessage = false;
//                    chatHistory = response.body();
//
//                        displayChatHistory();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChatHistoryBean> call, Throwable t) {
//                    Log.d("ExceptionChat",t.getLocalizedMessage());
//            }
//        });
//
//    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public boolean isMoreMessageAvailable()
    {
        return currentPages<totalPages;

    }
}
