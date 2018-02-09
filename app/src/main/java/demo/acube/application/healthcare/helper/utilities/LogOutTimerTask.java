package demo.acube.application.healthcare.helper.utilities;

import android.content.Context;
import android.content.Intent;

import java.util.TimerTask;

import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;

/**
 * Created by Anns on 18/05/17.
 */

public class LogOutTimerTask extends TimerTask {
    private Context context;

    public LogOutTimerTask(Context context) {
        this.context = context;
    }

    @Override
    public void run()
    {
        GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();
        globalApplication.setUserLoggedIn(false);
        if(!globalApplication.getAppIdle())
        {
            Intent i = new Intent(context, LoginAcitivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }

    }
}
