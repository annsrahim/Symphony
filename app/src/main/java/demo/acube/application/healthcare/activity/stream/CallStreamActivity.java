package demo.acube.application.healthcare.activity.stream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.enums.CallState;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.callStateModel.CallStateBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

import android.util.Log;

import com.opentok.android.AudioDeviceManager;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.opentok.android.OpentokError;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class CallStreamActivity extends AppCompatActivity
        implements Session.SessionListener,PublisherKit.PublisherListener,SubscriberKit.SubscriberListener {

    private Session mSession;
    GlobalApplication globalApplication;
    public static final String LOG_TAG = CallStreamActivity.class.getSimpleName();
    private TextView tvCallerName,tvCountDownTimer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    APIInterface apiInterface;
    private Boolean isCallingApi = false;
    private Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_stream);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        initUI();
        initSession();
        checkCallState();

    }

    private void checkCallState() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!isCallingApi)
                    callRequestToGetCallState();
            }
        }, 0, 3000);
    }

    private void callRequestToGetCallState()
    {
        isCallingApi = true;
        Call<CallStateBean> stateCall = apiInterface.getCallState(globalApplication.getOutGoingCallId());
        Log.d("OutgoingCallId","Cal Id "+globalApplication.getOutGoingCallId());
        stateCall.enqueue(new Callback<CallStateBean>() {
            @Override
            public void onResponse(Call<CallStateBean> call, Response<CallStateBean> response) {
                isCallingApi = false;
                if(response.code()==200)
                {
                    int status = response.body().getData().getStatus();
                    Log.d("Tag","Status "+status);
                    if(status==CallState.STATUS_DECLINED || status==CallState.STATUS_MISSED )
                        callDeclined();
                }
            }

            @Override
            public void onFailure(Call<CallStateBean> call, Throwable t) {
                isCallingApi = false;
            }
        });

    }
    public void callDeclined()
    {
        timer.cancel();
        finish();
    }
    private void initUI()
    {
        tvCallerName = (TextView)findViewById(R.id.tv_caller_name);
        tvCountDownTimer = (TextView)findViewById(R.id.tv_count_down_timer);
        tvCallerName.setText(globalApplication.getPatSelectedCallerName());
        timer = new Timer();
    }


    private void initSession()
    {
        globalApplication.setAppIdle(false);
        mSession = new Session.Builder(this, Config.TOKBOX_API_KEY,globalApplication.getSessionId()).build();
        mSession.setSessionListener(this);
        mSession.connect(globalApplication.getTokenNumber());
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this)
                .build();
        mPublisher.setPublisherListener(this);
        mPublisher.setPublishAudio(true);
        mPublisher.setPublishVideo(false);
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");
        timer.cancel();
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        mSession.disconnect();
        finish();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        startTimer();
        Log.i(LOG_TAG,"Stream Created"+stream.getName());
    }

    private void startTimer()
    {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG,"Stream Destroyed"+stream.getName());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOG_TAG, "Stream Error "+opentokError.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriber) {


    }

    @Override
    public void onDisconnected(SubscriberKit subscriber) {
        Log.i(LOG_TAG, "Subscriber Disconnected");
    }

    @Override
    public void onError(SubscriberKit subscriber, OpentokError opentokError) {
        Log.e(LOG_TAG, "Subscriber error: " + opentokError.getMessage());
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {

        timer.cancel();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void endCall(View view) {
        Utils.updateCallState(apiInterface,globalApplication.getOutGoingCallId(), CallState.STATUS_MISSED);
        mSession.disconnect();
        finish();
    }


}
