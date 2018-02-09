package demo.acube.application.healthcare.helper.utilities;

import android.app.Application;
import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;

/**
 * Created by Anns on 27/04/17.
 */

public class GlobalApplication extends Application {
    private String accessToken;
    private int userId=0;
    private String userType = "";
    private String userName = "";
    private String userEmail = "";
    private Boolean signedUp = false;
    private UserDetailsBean userDetailsBean;
    private String userAvatar = "";
    private String tokenNumber = "";
    private boolean isUserLoggedIn = false;
    private int incomingCallFrom = 0;
    private int incomingCallType = 0;
    private String primaryDoctorName = "";
    private int patSelectedCallerId = 0;
    private String patSelectedCallerType = "";
    private int outGoingCallId = 0;
    public static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    public static GlobalApplication getInstance()
    {
        return instance;
    }

    public boolean isCallRejected() {
        return isCallRejected;
    }

    public void setCallRejected(boolean callRejected) {
        isCallRejected = callRejected;
    }

    private boolean isCallRejected = false;

    public CalendarDay getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(CalendarDay scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    private CalendarDay scheduledDate;



    public int getPatSelectedCallerId() {
        return patSelectedCallerId;
    }

    public void setPatSelectedCallerId(int patSelectedCallerId) {
        this.patSelectedCallerId = patSelectedCallerId;
    }

    public String getPatSelectedCallerName() {
        return patSelectedCallerName;
    }

    public void setPatSelectedCallerName(String patSelectedCallerName) {
        this.patSelectedCallerName = patSelectedCallerName;
    }

    private String patSelectedCallerName = "";

    public String getPrimaryDoctorName() {
        return primaryDoctorName;
    }

    public void setPrimaryDoctorName(String primaryDoctorName) {
        this.primaryDoctorName = primaryDoctorName;
    }

    public String getPrimaryDoctorSpeciality() {
        return primaryDoctorSpeciality;
    }

    public void setPrimaryDoctorSpeciality(String primaryDoctorSpeciality) {
        this.primaryDoctorSpeciality = primaryDoctorSpeciality;
    }

    private String primaryDoctorSpeciality = "";

    public Boolean getAppIdle() {
        return isAppIdle;
    }

    public void setAppIdle(Boolean appIdle) {
        isAppIdle = appIdle;
    }

    private Boolean isAppIdle = false;

    public int getIncomingCallType() {
        return incomingCallType;
    }

    public void setIncomingCallType(int incomingCallType) {
        this.incomingCallType = incomingCallType;
    }

    public int getIncomingCallId() {
        return incomingCallId;
    }

    public void setIncomingCallId(int incomingCallId) {
        this.incomingCallId = incomingCallId;
    }

    private int incomingCallId = 0;

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private String sessionId = "";
    public Boolean getSignedUp() {
        return signedUp;
    }

    public void setSignedUp(Boolean signedUp) {
        this.signedUp = signedUp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDob() {
        return userDob;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    private String userDob = "";
    private int userAge = 0;
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessTokenTime() {
        return accessTokenTime;
    }

    public void setAccessTokenTime(String accessTokenTime) {
        this.accessTokenTime = accessTokenTime;
    }

    private String accessTokenType;
    private String accessTokenTime;

    public UserDetailsBean getUserDetailsBean() {
        return userDetailsBean;
    }

    public void setUserDetailsBean(UserDetailsBean userDetailsBean) {
        this.userDetailsBean = userDetailsBean;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public int getIncomingCallFrom() {
        return incomingCallFrom;
    }

    public void setIncomingCallFrom(int incomingCallFrom) {
        this.incomingCallFrom = incomingCallFrom;
    }

    public int getOutGoingCallId() {
        return outGoingCallId;
    }

    public void setOutGoingCallId(int outGoingCallId) {
        this.outGoingCallId = outGoingCallId;
    }

    public String getPatSelectedCallerType() {
        return patSelectedCallerType;
    }

    public void setPatSelectedCallerType(String patSelectedCallerType) {
        this.patSelectedCallerType = patSelectedCallerType;
    }


}
