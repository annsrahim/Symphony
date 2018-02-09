package demo.acube.application.healthcare.activity.stream;

import android.content.Context;
import android.media.AudioManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.Timer;
import java.util.TimerTask;

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

public class VideoCallStreamAcitivity extends AppCompatActivity
        implements Session.SessionListener,PublisherKit.PublisherListener,SubscriberKit.SubscriberListener {

    RelativeLayout rlCallingLayout,rlInCallLayout;
    private Session mSession;
    GlobalApplication globalApplication;
    public static final String LOG_TAG = CallStreamActivity.class.getSimpleName();
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private Boolean isCallingApi = false;
    private Timer timer;
    APIInterface apiInterface;
    private Integer currentCallState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_stream_acitivity);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
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
        Call<CallStateBean> stateCall = apiInterface.getVideoCallState(globalApplication.getOutGoingCallId());
        stateCall.enqueue(new Callback<CallStateBean>() {
            @Override
            public void onResponse(Call<CallStateBean> call, Response<CallStateBean> response) {
                isCallingApi = false;
                if(response.code()==200)
                {
                    int status = response.body().getData().getStatus();
                    currentCallState = status;
                    Log.d("VideoCallStatus","State "+currentCallState);
                    if(status== CallState.STATUS_DECLINED || status==CallState.STATUS_MISSED )
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

    private void initUI() {
        rlCallingLayout = (RelativeLayout)findViewById(R.id.rlCallingScreen);
        rlInCallLayout = (RelativeLayout)findViewById(R.id.rlInCallScreen);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        timer = new Timer();
    }

    private void initSession()
    {
        mSession = new Session.Builder(this, Config.TOKBOX_API_KEY,globalApplication.getSessionId()).build();
        mSession.setSessionListener(this);
        mSession.connect(globalApplication.getTokenNumber());
    }
    public void disconnectCall(View view)
    {
        mSession.disconnect();
        globalApplication.setAppIdle(false);
        if(currentCallState!=null)
        {
            if(currentCallState==CallState.STATUS_INCALL)
            {
                Utils.updateVideoCallState(apiInterface,globalApplication.getOutGoingCallId(),CallState.STATUS_COMPLETED);
            }
            else
            {
                Utils.updateVideoCallState(apiInterface,globalApplication.getOutGoingCallId(),CallState.STATUS_MISSED);
            }
        }

        finish();
    }
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        globalApplication.setAppIdle(true);
        mPublisher = new Publisher.Builder(this)
                .build();
        mPublisher.setPublisherListener(this);
        mPublisher.setPublishAudio(true);
        mPublisher.setPublishVideo(true);
        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        mSession.disconnect();
        globalApplication.setAppIdle(false);
        finish();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.i(LOG_TAG,"Stream Created"+stream.getName());
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

        showIncallScreen();
        mSubscriberViewContainer.addView(subscriber.getView());
    }

    private void showIncallScreen() {
        rlCallingLayout.setVisibility(View.GONE);
        rlInCallLayout.setVisibility(View.VISIBLE);
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

    public void doCameraFlip(View view) {
        mPublisher.cycleCamera();
    }

    public void toggleMicrophone(View view) {
        ImageView ivMicroPhone = (ImageView)findViewById(view.getId());
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        if (!audioManager.isMicrophoneMute()) {
            audioManager.setMicrophoneMute(true);
            Log.i(LOG_TAG,"MicroPhone Off");
            ivMicroPhone.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_mute,null));

        } else {
            audioManager.setMicrophoneMute(false);
            Log.i(LOG_TAG,"MicroPhone On");
            ivMicroPhone.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_unmute,null));

        }
    }
}
