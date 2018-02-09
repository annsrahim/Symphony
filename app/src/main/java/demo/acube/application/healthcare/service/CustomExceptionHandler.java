package demo.acube.application.healthcare.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import javax.microedition.khronos.opengles.GL;

import demo.acube.application.healthcare.activity.splashScreen.MainActivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;

/**
 * Created by Anns on 21/08/17.
 */

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity activity;

    public CustomExceptionHandler(Activity act)
    {
        activity = act;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(GlobalApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) GlobalApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }

}
