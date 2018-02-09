package demo.acube.application.healthcare.service;



import java.util.ArrayList;

import demo.acube.application.healthcare.model.calendarSearchDoctor.CalendarSearchDoctorBean;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import demo.acube.application.healthcare.activity.doctor.models.doctorDetails.DoctorDetailsBean;
import demo.acube.application.healthcare.activity.doctor.models.doctorSchedule.DoctorScheduleBean;
import demo.acube.application.healthcare.activity.doctor.models.officeHours.OfficeHoursBean;
import demo.acube.application.healthcare.activity.doctor.models.patientList.PatientListBean;
import demo.acube.application.healthcare.activity.doctor.models.primaryPatientList.PrimaryPatientListBean;
import demo.acube.application.healthcare.activity.doctor.models.referralBean.ReferralListBean;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.model.accessToken.AccessTokenBean;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.appointmentHistoryBean.AppointmentHistoryBean;
import demo.acube.application.healthcare.model.avatarUpload.AvatarUploadBean;
import demo.acube.application.healthcare.model.callDetailsBean.CallDetails;
import demo.acube.application.healthcare.model.callRequestBean.CallRequestDatasBean;
import demo.acube.application.healthcare.model.callStateModel.CallStateBean;
import demo.acube.application.healthcare.model.chatHistoryModel.ChatHistoryBean;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.model.codeAuthentication.CodeAuthenticatedBean;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.DoctorGroupSpecialityBean;
import demo.acube.application.healthcare.model.doctorsSlot.DoctorsAvailabilitySlotBean;
import demo.acube.application.healthcare.model.initialLogin.InitialLoginBean;
import demo.acube.application.healthcare.model.medicationImageUploadBean.MedicationImageUploadBean;
import demo.acube.application.healthcare.model.missedNotifications.MissedNotificationsBean;
import demo.acube.application.healthcare.model.notificationBadge.NotificationBadgeBean;
import demo.acube.application.healthcare.model.patientScheduleBean.PatientScheduleBean;
import demo.acube.application.healthcare.model.recentChat.RecentChatBean;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;
import demo.acube.application.healthcare.model.taskCountModel.TaskCountBean;
import demo.acube.application.healthcare.model.teleHealthTeamSearch.TeleHealthTeamSearchModel;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;

/**
 * Created by Anns on 27/04/17.
 */

public interface APIInterface {
    @FormUrlEncoded
    @POST("api/v1/auth/token")
    Call<AccessTokenBean> doCreateAccessToken(@Field("grant_type") String grant_type,
                                              @Field("client_id") String client_id,
                                              @Field("client_secret") String client_secret
                                            );

    @FormUrlEncoded
    @POST("api/v1/verification/code/validate")
    Call<CodeAuthenticatedBean> doCheckCodeAuthenticaton(
                                                         @Field("code") String code,
                                                         @Field("dob") String dob
                                                        );

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    Call<InitialLoginBean> doInitialAuthentication(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/v1/user")
    Call<UserDetailsBean> doUserRegistration(@Field("email") String email,
                                             @Field("password") String password,
                                             @Field("user_id") String userId,
                                             @Field("timezone") String timeZone);

    @FormUrlEncoded
    @POST("api/v1/schedule/reminder/medication")
    Call<Success> doAddMedicationRemainder(@Field("patient_id") int patientId,
                                 @Field("doctor_id") int doctorId,
                                 @Field("date_start") String dateStart,
                                 @Field("date_end") String dateEnd,
                                 @Field("alert_minutes") String alertMinutes,
                                 @Field("creator_id") int creatorId,
                                 @Field("medicine_name") String medicalName,
                                 @Field("times[]") ArrayList<String> values,
                                 @Field("images") String images,
                                 @Field("dosage_value") String dosageValue,
                                 @Field("dosage_metric_type") String dosageMetricType,
                                 @Field("notes") String notes
                                 );


    @FormUrlEncoded
    @POST("api/v1/schedule/appointment")
    Call<Success> doAddCallAPpointment(@Field("patient_id") int patientId,
                                           @Field("doctor_id") int doctorId,
                                           @Field("date") String date,
                                           @Field("time_start") String timeStart,
                                           @Field("time_end") String timeEnd,
                                           @Field("notes") String notes,
                                           @Field("alert_minutes") int alertMinutes,
                                           @Field("type") int type,
                                           @Field("creator_id") int creator_id

    );
    @FormUrlEncoded
    @POST("api/v1/chat")
    Call<ChatInitiateModel> doInitiateChatOperation(@Field("patient_id") int patientId,
                                                    @Field("doctor_id") int doctorId,
                                                    @Field("creator_id") int creator_id
                                                    );


    @FormUrlEncoded
    @POST("api/v1/notification/seen/user/{id}")
    Call<Success> updateNotificationSeen(@Path("id") int patientId,
                                          @Field("type[]") int type
    );


    @GET("/api/v1/calendar/doctor/{userId}/search")
    Call<CalendarSearchDoctorBean> searchCalendarEvents(@Path("userId") int userId,
                                                        @Query("q") String query);


    @FormUrlEncoded
    @POST("api/v1/{callType}")
    Call<CallRequestDatasBean> doGetCallRequestDatas(@Field("sender_id") String senderId,
                                                     @Field("receiver_id") String recevierId,
                                                     @Path("callType") String callType);

    @FormUrlEncoded
    @POST("api/v1/chat/message")
    Call<Success> doPostChatHistory(@Field("chat_id") int chatId,
                                                     @Field("user_id") int userId,
                                                     @Field("message") String message);


    @FormUrlEncoded
    @POST("api/v1/task/{taskId}/remind")
    Call<Success> remindTask(@Path("taskId") int taskId,
                                    @Field("minutes") int minutes);

    @PUT("api/v1/task/{taskId}/complete")
    Call<Success> markAsComplete(@Path("taskId") int taskId);


    @DELETE("api/v1/task/{taskId}")
    Call<Success> cancelTask(@Path("taskId") int taskId,
                             @Query("delete_reason") String deleteReason);

    @FormUrlEncoded
    @POST("api/v1/password/email")
    Call<Success> doResetPassword(@Field("email") String email);

    @Multipart
    @POST("api/v1/upload/avatar")
    Call<AvatarUploadBean> doAvatarUpload(@Part MultipartBody.Part file,
                                          @Part("user_id") RequestBody description);

    @Multipart
    @POST("api/v1/upload/image/medication")
    Call<MedicationImageUploadBean> doMedicalImageUpload(@Part MultipartBody.Part file);



    @GET("api/v1/tasks/user/{id}")
    Call<DoctorTaskListBean> doGetDocotorTaskList(@Path("id") String userId,@Query("page") int pageNo);

    @GET("api/v1/tasks/user/{id}/archived")
    Call<DoctorTaskListBean> doGetArchiveList(@Path("id") String userId,@Query("page") int pageNo);

    @GET("api/v1/chat/{id}/message")
    Call<ChatHistoryBean> doGetChatHistory(@Path("id") int chatID);

    @GET("api/v1/chat/{id}/message")
    Observable<ChatHistoryBean> doGetChatHistoryPageByPage(@Path("id") int chatID);

    @DELETE("api/v1/image/medication/{imgId}")
    Call<Success> doDeleteImageId(@Path("imgId") int imgId);

    @GET("api/v1/{callType}/{callId}")
    Call<CallDetails> doGetCallDetailsList(@Path("callId") int callId,
                                           @Path("callType") String callType);


    @GET("api/v1/tasks/user/{id}")
    Call<DoctorTaskListBean> doGetDocotorCallTaskList(@Path("id") String userId,
                                                      @Query("type") int type,
                                                        @Query("page") int pageNo);

    @GET("api/v1/tasks/user/{id}")
    Call<DoctorTaskListBean> doGetVideoCallTaskList(@Path("id") String userId,
                                                      @Query("type") String type,
                                                    @Query("page") int pageNo);

    @GET("api/v1/tasks/user/{id}")
    Call<DoctorTaskListBean> doGetVoiceCallTaskList(@Path("id") String userId,
                                                      @Query("type") String type,
                                                    @Query("page") int pageNo);

    @GET("api/v1/tasks/user/{id}/archived")
    Call<DoctorTaskListBean> doGetArchiveTypeTaskList(@Path("id") String userId,
                                                    @Query("type") String type,
                                                    @Query("page") int pageNo);

    @GET("api/v1/referral")
    Call<ReferralListBean> doGetReferrals(@Query("patient_id") int patient_id,
                                          @Query("doctor_id") int doctor_id,
                                                  @Query("status") int status,
                                                  @Query("page") int pageNo);


    @GET("api/v1/patient/{id}/doctor/secondary")
    Call<SecondaryDoctorsList> doGetSecondaryDoctorList(@Path("id") int userId);

    @GET("api/v1/referral/patient/{id}/pending/doctors")
    Call<SecondaryDoctorsList> doGetPendingDoctorList(@Path("id") int userId);

    @GET("api/v1/patient/{id}")
    Call<UserDetailsBean> doGetUserDetails(@Path("id") int userId);

    @GET("api/v1/schedule/doctor/{id}/slots")
    Call<DoctorsAvailabilitySlotBean> doGetDoctorAvailalitySlots(@Path("id") int userId,
                                                                 @Query("date") String date);
    @GET("api/v1/schedules/patient/{id}/date")
    Call<PatientScheduleBean> doGetPatientSchedules(@Path("id") int userId,
                                                    @Query("date") String date);

    @GET("api/v1/schedules/doctor/{id}/date")
    Call<DoctorScheduleBean> doGetDoctorSchedules(@Path("id") int userId,
                                                    @Query("date") String date);

    @GET("api/v1/chat/user/{id}")
    Call<RecentChatBean> doGetRecentChats(@Path("id") int userId);

    @GET("api/v1/notification/user/{id}/badge")
    Observable<NotificationBadgeBean> doGetNotificationBadge(@Path("id") int userId);

    @GET("api/v1/notification/user/{id}")
    Call<MissedNotificationsBean> doGetMissedNotifications(@Path("id") int userId,
                                                           @Query("type") int type);

    @GET("api/v1/schedules/patient/{id}/month")
    Call<PatientScheduleBean> doGetPatientSchedulesByMonth(@Path("id") int userId,
                                                            @Query("month") String date,
                                                           @Query("type") Integer type);

    @GET("api/v1/schedules/doctor/{id}/month")
    Call<DoctorScheduleBean> doGetDoctorSchedulesByMonth(@Path("id") int userId,
                                                         @Query("month") String date,
                                                         @Query("type") Integer type);

    @DELETE("api/v1/schedule/appointment/{id}")
    Call<Success> doDeleteAppointment(@Path("id") int appointmentId);

    @DELETE("api/v1/schedule/reminder/{id}")
    Call<Success> doDeleteReminder(@Path("id") int reminderId);

    @PUT("api/v1/call/{id}")
    Call<Success> doUpdateCallState(@Path("id") int callId,
                                    @Query("state") int stateId);

    @PUT("api/v1/visit/{id}")
    Call<Success> doUpdateVideoCallState(@Path("id") int callId,
                                    @Query("state") int stateId);

    @PUT("api/v1/doctor/{id}/address")
    Call<Success> updateDoctorAddress(@Path("id") int doctorId,
                                         @Query("street1") String street1,
                                         @Query("city") String city,
                                         @Query("state") String state,
                                         @Query("country") String country,
                                         @Query("zip") String zip
                                      );
    @PUT("api/v1/doctor/{id}")
    Call<Success> updateDoctorContactInfo(@Path("id") int doctorId,
                                      @Query("phone_office") String street1,
                                      @Query("website") String city
    );



    @GET("api/v1/call/{callId}/state")
    Call<CallStateBean> getCallState(@Path("callId") int callId);

    @GET("api/v1/visit/{callId}/state")
    Call<CallStateBean> getVideoCallState(@Path("callId") int callId);

    @GET("api/v1/tasks/user/{userId}/count")
    Call<TaskCountBean> getTaskCount(@Path("userId") int userId);

    @GET("api/v1/doctor/{id}/patient")
    Call<PatientListBean> getPatientList(@Path("id") int userId);


    @GET("api/v1/doctor/{id}")
    Call<DoctorDetailsBean> getDoctorDetails(@Path("id") int userId);

    @GET("api/v1/doctor/groupby/specialty")
    Call<DoctorGroupSpecialityBean> getDoctorGroupSpeciality(@Query("patient_id") int userId);

    @PUT("api/v1/referral/{referId}/approve")
    Call<Success> approveRefferalRequest(@Path("referId") int referId);

    @GET("api/v1/healthteam/search")
    Call<TeleHealthTeamSearchModel> searchTeleHealthTeam(@Query("q") String searchText);

    @GET("api/v1/tasks/user/{id}/search")
    Call<DoctorTaskListBean> searchTaskForDoctor(@Path("id") int userId,
                                                        @Query("q") String searchText);

    @GET("api/v1/doctor/{id}/officehours")
    Call<OfficeHoursBean> getOfficeHours(@Path("id") int userId);

    @PUT("api/v1/referral/{referId}/decline")
    Call<Success> declineRefferalRequest(@Path("referId") int referId,
                                         @Query("reason") String reason);

    @GET("api/v1/doctor/{id}/patient/{type}")
    Call<PrimaryPatientListBean> getPrimarySecondaryPatientList(@Path("id") int userId,
                                                                @Path("type") String type,
                                                                @Query("page") int page_no);

    @GET("api/v1/appointment/history")
    Call<AppointmentHistoryBean> getAppointmentHistory(@Query("patient_id") int patientId,
                                                       @Query("doctor_id") int doctorId,
                                                       @Query("page") int page_no);



    @FormUrlEncoded
    @POST("api/v1/user/{userId}/device")
    Call<Success> doUpdateTokenForPushNotification(
            @Path("userId") String userId,
            @Field("type") int type,
            @Field("token") String token,
            @Field("device_id") String device_id
    );

    @FormUrlEncoded
    @POST("api/v1/referral/request")
    Call<Success> doCreateReferralRequest(
            @Field("patient_id") int patientId,
            @Field("specialist_doctor_id") int specialityId,
            @Field("primary_doctor_id") int primaryDoctorId,
            @Field("reason") String reason
    );






}
