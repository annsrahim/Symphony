package demo.acube.application.healthcare.activity.stream;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opentok.android.AudioDeviceManager;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.squareup.picasso.Picasso;

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

import static demo.acube.application.healthcare.activity.stream.CallStreamActivity.LOG_TAG;

public class IncomingCallActivity extends AppCompatActivity
        implements Session.SessionListener,PublisherKit.PublisherListener,SubscriberKit.SubscriberListener {

    LinearLayout llCallAcceptRejcet,llEndCall;
    TextView tvCallStatus,tvCallerName;
    GlobalApplication globalApplication;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    String senderName="",senderAvatar="";
    String token="",sessionId = "";
    ImageView ivProfileImage;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private Stream stream;
    private AudioManager audioManager;
    private boolean isLoudSpeaker = true;
    private boolean isMicroPhoneMute = false;
    private Chronometer chronoCallerTime;
    private int callId=0;
    private Boolean isCallingApi = false;
    private Timer timer;
    private Vibrator vibrator;

    // Start without a delay
// Vibrate for 100 milliseconds
// Sleep for 1000 milliseconds
    long[] vibratorPattern = {0, 300, 1000};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication  = (GlobalApplication)this.getApplicationContext();
        globalApplication.setIncomingCallFrom(0);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        initUI();
        getCallDetails();
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
        Call<CallStateBean> stateCall = apiInterface.getCallState(callId);
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
        cancelVibration();
        timer.cancel();
        finish();
    }


    private void getCallDetails()
    {
        progressDialog = LoadingDialog.show(this,"Loading");
         callId = globalApplication.getIncomingCallId();
        Call<CallDetails> callDetails = apiInterface.doGetCallDetailsList(callId,"call");
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
                    connectToCall();
                }
                else
                {
                    Utils.showToast(IncomingCallActivity.this,"Unable to connect call");
                }
            }

            @Override
            public void onFailure(Call<CallDetails> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(IncomingCallActivity.this,"Unable to connect call "+t.getLocalizedMessage());
            }
        });
    }

    private void connectToCall()
    {
        vibrator.vibrate(vibratorPattern,0);
        tvCallerName.setText(senderName);
        if(!senderAvatar.equals(""))
            Picasso.with(this).load(senderAvatar).placeholder(R.drawable.gpc_placeholder_patient).into(ivProfileImage);
        initSession();
    }

    private void initSession() {

    }

    private void initUI()
    {
        llCallAcceptRejcet = (LinearLayout)findViewById(R.id.ll_call_accept_reject);
        llEndCall = (LinearLayout)findViewById(R.id.ll_end_call);
        tvCallStatus = (TextView)findViewById(R.id.tv_call_status);
        ivProfileImage = (ImageView)findViewById(R.id.profile_image);
        tvCallerName = (TextView)findViewById(R.id.tv_caller_name);
        chronoCallerTime = (Chronometer) findViewById(R.id.chronometerCallerTime);
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        timer = new Timer();

    }
    public void callAccpeted(View view)
    {
        cancelVibration();
        llCallAcceptRejcet.setVisibility(View.GONE);
        llEndCall.setVisibility(View.VISIBLE);
        tvCallStatus.setText("Call with");
        mSession = new Session.Builder(this, Config.TOKBOX_API_KEY,sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);

    }
    public void cancelVibration()
    {
        if(vibrator.hasVibrator())
            vibrator.cancel();
    }
    public void callRejceted(View view)
    {
        cancelVibration();
        Utils.updateCallState(apiInterface,callId, CallState.STATUS_DECLINED);

        finish();
    }
    public void disconnectCall(View view)
    {
        cancelVibration();
        Utils.updateCallState(apiInterface,callId,CallState.STATUS_COMPLETED);
        mSession.disconnect();
            finish();
    }
    @Override
    public void onBackPressed() {

    }


    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.i(LOG_TAG,"Stream Created"+stream.getName());
        AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);
        chronoCallerTime.setBase(SystemClock.elapsedRealtime());
        chronoCallerTime.setVisibility(View.VISIBLE);
        chronoCallerTime.start();
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
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        Utils.updateCallState(apiInterface,callId,CallState.STATUS_INCALL);
        mPublisher = new Publisher.Builder(this)
                .build();
        mPublisher.setPublisherListener(this);
        mPublisher.setPublishAudio(true);
        mPublisher.setPublishVideo(false);
        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(Session session) {
        finish();
        Log.i(LOG_TAG, "Session DisConnected");

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        timer.cancel();
        Log.i(LOG_TAG, "Stream Recieved");
        if (mSubscriber == null)
        {

            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);

        }
    }

    @Override
    protected void onStop() {
        cancelVibration();
        timer.cancel();
        super.onStop();
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        chronoCallerTime.stop();
        mSession.disconnect();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG, "Session Error "+opentokError.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {


        Log.i(LOG_TAG, "Subscriber Connected");
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.i(LOG_TAG, "Subscriber Disconnected");
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.i(LOG_TAG, "Subscriber Error "+opentokError.getMessage());
    }

    public void audioToggle(View view) {
        ImageView ivSpeaker = (ImageView)findViewById(view.getId());
        if (isLoudSpeaker) {
            isLoudSpeaker = false;
            AudioDeviceManager.getAudioDevice().setOutputMode(

                    BaseAudioDevice.OutputMode.Handset);
            ivSpeaker.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_speaker_off,null));
            Log.i(LOG_TAG,"switched to handset");
        } else {
            isLoudSpeaker = true;
            AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.SpeakerPhone);
            ivSpeaker.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_speaker_on,null));
            Log.e(LOG_TAG,"switched to loudspeaker");
        }
    }

    public void toggleMicrophone(View view)
    {
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
