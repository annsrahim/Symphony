package demo.acube.application.healthcare.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;
import demo.acube.application.healthcare.helper.utilities.SharedPreferenceUtils;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;

/**
 * Created by Anns on 17/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    GlobalApplication globalApplication;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d("PushNoti",remoteMessage.getNotification().getBody());
        Log.d("RemoteMessage",remoteMessage.getData().toString());
        int callId=0;
        globalApplication = (GlobalApplication)getApplicationContext();
        globalApplication.setIncomingCallType(Integer.parseInt(remoteMessage.getData().get("type")));
        if(globalApplication.getIncomingCallType()==21)
        {
            int mCallId = Integer.parseInt(remoteMessage.getData().get("call_id"));
            int mMissedCallId = SharedPreferenceUtils.getIntDataInShare(getApplicationContext(),Config.Missed_CALL_ID);
            if(mCallId!=mMissedCallId)
            {
                globalApplication.setIncomingCallId(mCallId);
                incomingCallNotification();
            }

        }
        else if(globalApplication.getIncomingCallType()==22)
        {
            int mCallId = Integer.parseInt(remoteMessage.getData().get("call_id"));
            SharedPreferenceUtils.setIntDataInShare(getApplicationContext(), Config.Missed_CALL_ID,mCallId);
        }
        else if(globalApplication.getIncomingCallType()==23 || globalApplication.getIncomingCallType()==24)
        {
            globalApplication.setIncomingCallId(Integer.parseInt(remoteMessage.getData().get("visit_id")));
            incomingCallNotification();
        }
        else if(globalApplication.getIncomingCallType()==25)
        {
            Intent intent = new Intent(ValueUtils.ChatReplyFromDoctor);

            int chatId = Integer.parseInt(remoteMessage.getData().get("chat_id"));
            //put whatever data you want to send, if any
            intent.putExtra("chat_id",chatId);

            //send broadcast
            getApplicationContext().sendBroadcast(intent);
        }


        super.onMessageReceived(remoteMessage);
    }

    public void incomingCallNotification()
    {
        Log.d("TAGCheck2","FIREBAAS");
        if(globalApplication.getIsUserLoggedIn())
            Utils.showIncomingCallView(getApplicationContext(),1);
        else
        {
            globalApplication.setIncomingCallFrom(1);

            if(!LoginAcitivity.isActive)
            {

                Log.d("ActName",getApplicationContext().getClass().getSimpleName());
                Intent intent = new Intent(getApplicationContext(),LoginAcitivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        }
    }
}
