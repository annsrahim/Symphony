package demo.acube.application.healthcare.helper.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.opengles.GL;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.dashboard.PatientHomeActivity;
import demo.acube.application.healthcare.activity.stream.IncomingCallActivity;
import demo.acube.application.healthcare.activity.stream.IncomingVideoCallActivity;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.callRequestBean.CallRequestDatasBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;


/**
 * Created by Anns on 27/04/17.
 */

public class Utils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static String getTagName(Context context) {
        return context.getClass().getSimpleName();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showAlertDialog(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, null).show();
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static String getTaskTypeName(int taskId) {
        switch (taskId) {
            case 1:
                return "Text Message";
            case 2:
                return "Scheduled Phone Call";
            case 3:
                return "Scheduled Virtual Visit";
            case 4:
                return "Scheduled Appointment";
            case 6:
                return "Missed Phone Call";
            case 7:
                return "Missed Virtual Visit";
            default:
                return "None";
        }
    }

    public static void getAudioVideoPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1);

        }

    }


    public static void disableKeyboardPopup(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void updateDeviceToken(final Context context) {
        GlobalApplication globalApplication = (GlobalApplication) context.getApplicationContext();
        String userId = String.valueOf(globalApplication.getUserId());
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DeviceId", android_id);
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<Success> tokenCall = apiInterface.doUpdateTokenForPushNotification(userId, 3, refreshedToken, android_id);
        tokenCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if (response.code() != 200)
                    Log.d(getTagName(context), "Token Update Failed");

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Log.d(getTagName(context), "Token Update Failed " + t.getLocalizedMessage());
            }
        });


    }


    public static Date convertStringToDate(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date retDate = sdf.parse(strDate);
        return retDate;
    }

    public static Date convertStringToDate2(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date retDate = sdf.parse(strDate);
        return retDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertStringToDateTime(String strDate) {

        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static Boolean compareDate(Date stDate, Date edDate) {
        return edDate.after(stDate) || edDate.equals(stDate);
    }

    public static void showIncomingCallView(Context context, int incomingCallFrom) {
        GlobalApplication globalApplication = (GlobalApplication) context.getApplicationContext();
        int callType = globalApplication.getIncomingCallType();
        switch (callType) {
            case 21: {
                Intent intent = new Intent(context, IncomingCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
            case 23: {
                Intent intent = new Intent(context, IncomingVideoCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }

            default: {
                Intent intent = (globalApplication.getUserType().equals("doctor")) ? new Intent(context, DoctorHomeActivity.class) : new Intent(context, PatientHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

    public static String getDayFromNumber(int value) {
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday",
                "Friday", "Saturday"};
        value--;
        return strDays[value];
    }

    public static String getMonthFromNumber(int value) {
        switch (value) {
            case 0:
                return "January";
            case 1:
                return "Feburary";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "None";
        }
    }

    public static String convert24HourTo12Hour(String time) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);
            return new SimpleDateFormat("KK:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }

    public static String convert12hrTo24hr(String timevalue) throws ParseException {
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");

        return date24Format.format(date12Format.parse(timevalue));
    }



    public static Boolean checkCurrentTimeBetweenRange(String startTime, String endTime) {
        try {
            Calendar callCurrentDate = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(callCurrentDate.getTime());


            Date time1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(formattedDate + " " + startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);


            Date time2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(formattedDate + " " + endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);


            Date d = Calendar.getInstance().getTime();
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);


            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;

        }
    }

    public static Boolean isDateToday(String validUntil) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date strDate = sdf.parse(validUntil);

        return DateUtils.isToday(strDate.getTime());
    }

    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void convertTimeStampToDate(long millis) {
        Timestamp stamp = new Timestamp(millis);
        Date date = new Date(stamp.getTime());
        System.out.println(date);
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static void updateCallState(APIInterface apiInterface, int callId, int state) {
        Log.d("CallId", "ID " + callId);
        Call<Success> callDetails = apiInterface.doUpdateCallState(callId, state);
        callDetails.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {

            }
        });

    }

    public static void updateVideoCallState(APIInterface apiInterface, int callId, int state) {
        Log.d("CallId", "ID " + callId);
        Call<Success> callDetails = apiInterface.doUpdateVideoCallState(callId, state);
        callDetails.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {

            }
        });

    }

    public static void updateNotificationState(Context context,int userId,int type)
    {
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<Success> updateStateCall = apiInterface.updateNotificationSeen(userId,type);
        updateStateCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {

            }
        });
    }

    public static void updateMarkAsCompelte(Context context,int taskId)
    {

        APIInterface apiInterface;
        apiInterface = APIClient.getClient(context).create(APIInterface.class);
        final ProgressDialog progressDialog = LoadingDialog.show(context, "Loading");
        Call<Success> remindCall = apiInterface.markAsComplete(taskId);
        remindCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()!=200)
                {

                    Utils.showToast(context,"Unable to process request please try again later");
                }


            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(context,"Unable to process request please try again later");
            }
        });


    }

    public static void cancelTask(Context context,int taskId,String reason)
    {

        APIInterface apiInterface;
        apiInterface = APIClient.getClient(context).create(APIInterface.class);
        final ProgressDialog progressDialog = LoadingDialog.show(context, "Loading");
        Call<Success> remindCall = apiInterface.cancelTask(taskId,reason);
        remindCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()!=200)
                {

                    Utils.showToast(context,"Unable to process request please try again later");
                }


            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(context,"Unable to process request please try again later");
            }
        });


    }


    public static void updateTaskRemind(Context context,int taskId,int minutes)
    {

        APIInterface apiInterface;
        apiInterface = APIClient.getClient(context).create(APIInterface.class);
        final ProgressDialog progressDialog = LoadingDialog.show(context, "Updating...");
        Call<Success> remindCall = apiInterface.remindTask(taskId,minutes);
        remindCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()!=200)
                {

                    Utils.showToast(context,"Unable to process request please try again later");
                }


            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(context,"Unable to process request please try again later");
            }
        });


    }


    public static void callRequest(Activity activity, final String callType, int callerId, int receiverId, final ICallRequestListener callRequestListener) {
        Utils.getAudioVideoPermission(activity);
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == 0) {
            final GlobalApplication globalApplication = (GlobalApplication) activity.getApplicationContext();
            APIInterface apiInterface;
            apiInterface = APIClient.getClient(activity).create(APIInterface.class);
            final ProgressDialog progressDialog = LoadingDialog.show(activity, "Initiating call");
            String userId = String.valueOf(callerId);
            String selectedCallId = String.valueOf(receiverId);
            Call<CallRequestDatasBean> callRequestCall = apiInterface.doGetCallRequestDatas(userId, selectedCallId, callType);
            callRequestCall.enqueue(new Callback<CallRequestDatasBean>() {
                @Override
                public void onResponse(Call<CallRequestDatasBean> call, Response<CallRequestDatasBean> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        if (callType.equals(ValueUtils.CALL))
                            globalApplication.setOutGoingCallId(response.body().getData().getCallId());
                        else
                            globalApplication.setOutGoingCallId(response.body().getData().getVisitId());
                        globalApplication.setTokenNumber(response.body().getData().getTokbox().getToken());
                        globalApplication.setSessionId(response.body().getData().getTokbox().getSessionId());
                        callRequestListener.onCallRequestCompleted();
                    } else {
                        callRequestListener.onCallRequestFailed("Call request failed please try again later");
                    }
                }

                @Override
                public void onFailure(Call<CallRequestDatasBean> call, Throwable t) {
                    progressDialog.dismiss();
                    callRequestListener.onCallRequestFailed("Call request failed " + t.getLocalizedMessage());
                }
            });
        }

    }

    public static void logOut(Context context)
    {
        GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();
        Log.d("Logout","State "+globalApplication.getIsUserLoggedIn());
        if(globalApplication.getIsUserLoggedIn())
        {

            globalApplication.setUserLoggedIn(false);
            Intent intent = new Intent(context, LoginAcitivity.class);
            context.startActivity(intent);
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getTimeSpanAgo(long timeInMillis)
    {
       String timeDifference = "";
        timeInMillis = timeInMillis*1000;
        Long currentTimeMillis = System.currentTimeMillis();
        long totalMillis = currentTimeMillis-timeInMillis;
        int minutes =  ((int)(totalMillis / 1000) / 60) % 60;
        int hours = (int)(totalMillis / 1000) / 3600;

        if(hours<1)
        {
            timeDifference = minutes+"m ago";
        }
        else
        {
            if(hours<24)
            {
                timeDifference = hours+"h ago";
            }
            else
            {
                int days = (int) Math.floor(hours/24);
                timeDifference = days+"d ago";
            }
        }
        return timeDifference;
    }


    public static Calendar getDateTimeFromMilliseconds(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }



   public static String getFormattableDate(long timeInMillis)
   {
       String formattedDate = "";

       Calendar calChatDate = getDateTimeFromMilliseconds(timeInMillis);
       Date chatDate =  calChatDate.getTime();

       if(DateUtils.isToday(timeInMillis))
       {
           formattedDate = "Today "+getTimeFromMilliseconds(timeInMillis);
       }
       else if(isYesterday(chatDate))
       {
            formattedDate = "Yesterday "+getTimeFromMilliseconds(timeInMillis);
       }
       else
       {
           int month = calChatDate.get(Calendar.MONTH)+1;
           int year = calChatDate.get(Calendar.YEAR);
           int dayOfMonth = calChatDate.get(Calendar.DAY_OF_MONTH);

           formattedDate = month+"/"+dayOfMonth+"/"+year+" "+getTimeFromMilliseconds(timeInMillis);
       }

        return formattedDate;
   }

   public static String getTimeFromMilliseconds(long millis)
   {

       Date date = new Date(millis);
       DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
       return dateFormat.format(date);
   }

    public static boolean isYesterday(Date date)
    {
        Date yesterday  = setTimeToMidnight(yesterday());
        Date chatDate = setTimeToMidnight(date);

        if(chatDate.equals(yesterday))
            return true;
        else
            return false;
    }


    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Typeface getFuturaFont(Context context)
    {
        Typeface futura_font = Typeface.createFromAsset(context.getAssets(),  "fonts/FuturaPTMedium.otf");
        return futura_font;
    }
    public static void setFontForTextView(Context context,TextView textView)
    {
        Typeface font_face = getFuturaFont(context);
        textView.setTypeface(font_face);
    }

    public static String getMetricNameFromId(int value)
    {
        String[] mMetricNames = {"None","unit(s)","mg","ml","pill(s)","tbsp","tsp","drop(s)","piece(s)","pus","patch","mcg","iu","meq"};
        return mMetricNames[value];
    }



}
