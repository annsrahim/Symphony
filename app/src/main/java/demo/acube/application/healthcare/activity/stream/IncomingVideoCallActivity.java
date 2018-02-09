package demo.acube.application.healthcare.activity.stream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.model.callDetailsBean.CallDetails;
import demo.acube.application.healthcare.model.callStateModel.CallStateBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class IncomingVideoCallActivity extends AppCompatActivity
        implements Session.SessionListener,PublisherKit.PublisherListener,SubscriberKit.SubscriberListener{
    RelativeLayout rlCallingLayout,rlInCallLayout;
    private Session mSession;
    GlobalApplication globalApplication;
    public static final String LOG_TAG = CallStreamActivity.class.getSimpleName();
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    String senderName="",senderAvatar="";
    String token="",sessionId = "";
    int callId = 0;
    private Vibrator vibrator;
    long[] vibratorPattern = {0, 300, 1000};
    private Boolean isCallingApi = false;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_video_call);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        globalApplication  = (GlobalApplication)this.getApplicationContext();
        globalApplication.setIncomingCallFrom(0);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initUI();
        checkCallState();
        getCallDetails();


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
        Call<CallStateBean> stateCall = apiInterface.getVideoCallState(callId);
        stateCall.enqueue(new Callback<CallStateBean>() {
            @Override
            public void onResponse(Call<CallStateBean> call, Response<CallStateBean> response) {
                isCallingApi = false;
                if(response.code()==200)
                {
                    int status = response.body().getData().getStatus();
                    Log.d("Timer","Status "+status);
                    if(status==CallState.STATUS_DECLINED || status==CallState.STATUS_MISSED )
                        callMissed();
                }
            }

            @Override
            public void onFailure(Call<CallStateBean> call, Throwable t) {
                isCallingApi = false;
                Log.d("Timer","Status "+t.getLocalizedMessage());
            }
        });

    }
    private void callMissed()
    {
        timer.cancel();
        finish();
    }

    private void initUI()
    {
        rlCallingLayout = (RelativeLayout)findViewById(R.id.rlCallingScreen);
        rlInCallLayout = (RelativeLayout)findViewById(R.id.rlInCallScreen);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        timer = new Timer();
    }

    private void getCallDetails()
    {
        progressDialog = LoadingDialog.show(this,"Loading");
         callId = globalApplication.getIncomingCallId();
        Call<CallDetails> callDetails = apiInterface.doGetCallDetailsList(callId,"visit");
        callDetails.enqueue(new Callback<CallDetails>() {
            @Override
            public void onResponse(Call<CallDetails> call, Response<CallDetails> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    senderName = response.body().getData().getSender().getName();
                    senderAvatar = response.body().getData().getSender().getAvatar();
                    token = response.body().getData().getTokbox().getToken();
                    sessionId = response.body().getData().getTokbox().getSessionId();

                  //  connectToCall();
                }
                else
                {
                    Utils.showToast(IncomingVideoCallActivity.this,"Unable to connect call");
                }
            }

            @Override
            public void onFailure(Call<CallDetails> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(IncomingVideoCallActivity.this,"Unable to connect call "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        if(timer!=null)
            timer.cancel();
        super.onStop();
    }

    private void showIncallScreen() {
        rlCallingLayout.setVisibility(View.GONE);
        rlInCallLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Utils.updateVideoCallState(apiInterface,globalApplication.getIncomingCallId(),CallState.STATUS_INCALL);
        Log.i(LOG_TAG,"Stream Created"+stream.getName());
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        mSession.disconnect();
        finish();
        Log.i(LOG_TAG,"Stream Destroyed"+stream.getName());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOG_TAG,"Error "+opentokError.getMessage());
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG,"Session Connected");
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
        Log.i(LOG_TAG,"Session disconnected");

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.i(LOG_TAG,"Stream Received");
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Dropped");
        mSession.disconnect();
        globalApplication.setAppIdle(false);
        finish();

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG,"Stream Error"+opentokError.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        mSubscriberViewContainer.addView(subscriberKit.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    public void callRejceted(View view) {
        Utils.updateVideoCallState(apiInterface,globalApplication.getIncomingCallId(), CallState.STATUS_DECLINED);
        finish();
    }

    public void callAccpeted(View view) {
        Utils.showToast(this,"Call "+globalApplication.getIncomingCallId());
        showIncallScreen();
        initSession();
    }

    private void initSession() {

        mSession = new Session.Builder(this, Config.TOKBOX_API_KEY,sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    public void disconnectCall(View view) {
        Utils.updateCallState(apiInterface,callId,CallState.STATUS_COMPLETED);
        mSession.disconnect();
        finish();
    }
}
