package demo.acube.application.healthcare.activity.splashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.codeRegister.CodeRegisterActivity;
import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.model.accessToken.AccessTokenBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;
import demo.acube.application.healthcare.helper.utilities.SharedPreferenceUtils;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    APIInterface apiInterface;
    GlobalApplication globalApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        Bundle startingIntent = getIntent().getExtras();
        if(startingIntent!=null)
        {
            if(startingIntent.getString("type")!=null)
            {
                globalApplication.setIncomingCallType(Integer.parseInt(startingIntent.getString("type")));
                globalApplication.setIncomingCallId(Integer.parseInt(startingIntent.getString("call_id")));
                actionForNotification();
            }

        }

        if(Utils.isNetworkConnected(this))
        {
            isNetworkAvailable();
        }
        else
        {
            isNetworkUnAvailable();
        }


    }

    private void actionForNotification()
    {
        if(globalApplication.getIncomingCallType()==21)
        {
            int mCallId = globalApplication.getIncomingCallId();
            int mMissedCallId = SharedPreferenceUtils.getIntDataInShare(getApplicationContext(),Config.Missed_CALL_ID);
            if(mCallId!=mMissedCallId)
            {
                globalApplication.setIncomingCallId(mCallId);
                incomingCallNotification();
            }
        }
        else if(globalApplication.getIncomingCallType()==22)
        {
            int mCallId = globalApplication.getIncomingCallId();
            SharedPreferenceUtils.setIntDataInShare(getApplicationContext(), Config.Missed_CALL_ID,mCallId);
        }

    }

    public void isNetworkAvailable()
    {

        Call<AccessTokenBean> accessTokenBeanCall = apiInterface.doCreateAccessToken(Config.grantType,Config.clientId,Config.clientSecret);

        accessTokenBeanCall.enqueue(new Callback<AccessTokenBean>() {
            @Override
            public void onResponse(Call<AccessTokenBean> call, Response<AccessTokenBean> response) {
                
                if(response.code()==200)
                {
                    tokenSuccess(response);
                }
                else
                {
                    tokenFailed(response.errorBody());
                }
                
               
            }

            @Override
            public void onFailure(Call<AccessTokenBean> call, Throwable t) {
                Log.d("Response",t.getLocalizedMessage()+"");
                showAlertUnableToConnect();

            }
        });
    }

    private void tokenFailed(ResponseBody response) {
        Log.d("Error Body",response.toString()+"" );
    }

    private void tokenSuccess(Response<AccessTokenBean> response) {
        Log.d("Response",response+"");

        AccessTokenBean accessTokenBean = response.body();

        globalApplication.setAccessToken(accessTokenBean.getData().getAccessToken());
        globalApplication.setAccessTokenTime(String.valueOf(accessTokenBean.getData().getExpiresIn()));
        globalApplication.setAccessTokenType(accessTokenBean.getData().getTokenType());

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences prefs = getSharedPreferences(Config.MY_PREFS_NAME, MODE_PRIVATE);
                boolean isCodeRegistered = prefs.getBoolean(Config.IS_CODE_REGISTERED,false);
                if(isCodeRegistered)
                    goToLoginScreen();
                else
                    goToCodeRegisterScreen();


            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void goToLoginScreen() {
        Intent mainIntent = new Intent(MainActivity.this,LoginAcitivity.class);
        startActivity(mainIntent);
    }
    private void goToCodeRegisterScreen() {
        Intent mainIntent = new Intent(MainActivity.this,CodeRegisterActivity.class);
        startActivity(mainIntent);
    }
    public void showAlertUnableToConnect()
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.str_error))
                .setMessage(getResources().getString(R.string.str_unable_to_connect_to_server))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }})
                .show();
    }
    public void isNetworkUnAvailable()
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.str_warning))
                .setMessage(getResources().getString(R.string.str_unable_to_connect_internet))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        recheckInternetConnection();
                    }})
                .show();
    }

    public void recheckInternetConnection()
    {
        if(Utils.isNetworkConnected(this))
        {
            isNetworkAvailable();
        }
        else
        {
            isNetworkUnAvailable();
        }
    }
    public void incomingCallNotification()
    {
        Log.d("TAGCheck","FromMainAct");
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

    @Override
    protected void onStop() {
        super.onStop();
        Utils.logOut(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.logOut(this);
    }
}
