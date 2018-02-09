package demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.SelectAppointmentTypeActivity;
import demo.acube.application.healthcare.activity.patient.adapters.TaskExpandableListAdapter;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Accessible;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Address;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Datum;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Phone;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Specialty;
import demo.acube.application.healthcare.model.userDetails.PrimaryDoctor;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApprovedDoctorsFragment extends Fragment implements IExpandableChildItemListener,SwipeRefreshLayout.OnRefreshListener {
    private GlobalApplication globalApplication;
    private Activity activity;
    private ExpandableListView expandableListView;
    APIInterface apiInterface;
    private View rootView;
    private ProgressDialog progressDialog;
    private PrimaryDoctor primaryDoctor;
    List<Datum> approvedDoctorList = new ArrayList<>();
    LayoutInflater inflater;
    TaskExpandableListAdapter taskExpandableListAdapter;
    private Integer chatId;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ApprovedDoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_approved_doctors, container, false);
        globalApplication  = (GlobalApplication)activity.getApplicationContext();
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        primaryDoctor = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor();
        initUI();
        getCategoryDoctorList();

        return rootView;
    }

    private void getCategoryDoctorList() {
        approvedDoctorList.clear();
        addPrimaryDoctorInList();
        getApprovedDoctorList();
    }

    private void initUI() {
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }
    private void getApprovedDoctorList()
    {

        final Call<SecondaryDoctorsList> secondaryDoctorsListCall = apiInterface.doGetSecondaryDoctorList(globalApplication.getUserId());
        secondaryDoctorsListCall.enqueue(new Callback<SecondaryDoctorsList>() {
            @Override
            public void onResponse(Call<SecondaryDoctorsList> call, Response<SecondaryDoctorsList> response) {

                if(response.code()==200)
                {
                    approvedDoctorList.addAll(response.body().getData());
                    taskExpandableListAdapter.notifyDataSetChanged();
                }
                else
                {
                    Utils.showToast(activity,"Unable to fetch datas");
                }
            }

            @Override
            public void onFailure(Call<SecondaryDoctorsList> call, Throwable t) {

                Utils.showToast(activity,"Unable to fetch datas "+t.getLocalizedMessage());
            }
        });

    }

    private void addPrimaryDoctorInList() {
        Phone phone =  new Phone();
        phone.setOffice(primaryDoctor.getPhone().getOffice());
        phone.setPersonal(primaryDoctor.getPhone().getPersonal());

        Accessible accessible = new Accessible();
        accessible.setCall(primaryDoctor.getAccessible().getCall());
        accessible.setText(primaryDoctor.getAccessible().getText());
        accessible.setVisit(primaryDoctor.getAccessible().getVisit());

        Address address = new Address();
        address.setAddressId(primaryDoctor.getAddress().getAddressId());
        address.setCity(primaryDoctor.getAddress().getCity());
        address.setCountry(primaryDoctor.getAddress().getCountry());
        address.setPrimary(primaryDoctor.getAddress().getPrimary());
        address.setState(primaryDoctor.getAddress().getState());
        address.setStreet1(primaryDoctor.getAddress().getStreet1());
        address.setStreet2(primaryDoctor.getAddress().getStreet2());
        address.setZip(primaryDoctor.getAddress().getZip());

        Specialty speciality = new Specialty();
        speciality.setPrimary(primaryDoctor.getSpecialty().getPrimary());
        speciality.setName(primaryDoctor.getSpecialty().getName());
        speciality.setSpecialtyId(primaryDoctor.getSpecialty().getSpecialtyId());

        Datum primaryDoctorItem  = new Datum(   primaryDoctor.getUserId(),
                primaryDoctor.getUserType(),
                primaryDoctor.getEmail(),
                primaryDoctor.getName(),
                primaryDoctor.getOpusEmail(),
                primaryDoctor.getDob(),
                primaryDoctor.getAge(),
                primaryDoctor.getAvatar(),
                primaryDoctor.getBio(),
                primaryDoctor.getDnd(),
                phone,
                primaryDoctor.getWebsite(),
                accessible,
                address,
                speciality
        );
        approvedDoctorList.add(primaryDoctorItem);
        displayApprovedResults();
    }
    @Override
    public void goToCallOperation(int groupPos) {
        int callerId = globalApplication.getUserId();
        int receiverId = approvedDoctorList.get(groupPos).getUserId();
        String receiverName = approvedDoctorList.get(groupPos).getName();
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(activity, ValueUtils.CALL, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToCallStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(activity,"Failed to call "+msg);
            }
        });
    }
    private void goToCallStream()
    {
        Intent intent = new Intent(activity, CallStreamActivity.class);
        activity.startActivity(intent);
    }



    @Override
    public void goToVideoOpeartion(int groupPos) {

        int callerId = globalApplication.getUserId();
        int receiverId = approvedDoctorList.get(groupPos).getUserId();
        String receiverName = approvedDoctorList.get(groupPos).getName();
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);
        Utils.callRequest(activity, ValueUtils.VISIT, callerId, receiverId, new ICallRequestListener() {
            @Override
            public void onCallRequestCompleted() {
                goToVideoStream();
            }

            @Override
            public void onCallRequestFailed(String msg) {
                Utils.showToast(activity,"Failed to call "+msg);
            }
        });

    }
    private void goToVideoStream() {
        Intent intent = new Intent(activity, VideoCallStreamAcitivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void goToScheduleAppointment(int groupPos) {
        Intent intent = new Intent(activity, SelectAppointmentTypeActivity.class);
        startActivity(intent);
    }
    @Override
    public void goToChatOperation(int groupPos) {

        int receiverId = approvedDoctorList.get(groupPos).getUserId();
        String receiverName = approvedDoctorList.get(groupPos).getName();
        globalApplication.setPatSelectedCallerType(ValueUtils.patient);
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);

        getChatInformation();
    }
    public void getChatInformation()
    {

        progressDialog = LoadingDialog.show(activity,"Fetching data...");

        int patientId = globalApplication.getUserId();
        int doctorId = globalApplication.getPatSelectedCallerId();
        int creatorId = patientId;
        final Call<ChatInitiateModel> chatModel = apiInterface.doInitiateChatOperation(patientId,doctorId,creatorId);
        chatModel.enqueue(new Callback<ChatInitiateModel>() {
            @Override
            public void onResponse(Call<ChatInitiateModel> call, Response<ChatInitiateModel> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    chatId =  response.body().getData().getChatId();
                    goToStartChatOperation();
                }
                else
                {
                    Utils.showToast(activity,"Unable to fetch data please try again");
                }
            }

            @Override
            public void onFailure(Call<ChatInitiateModel> call, Throwable t) {
                Utils.showToast(activity,"Unable to fetch data :"+t.getLocalizedMessage());
            }
        });


    }
    private void goToStartChatOperation() {
        Intent intent = new Intent(activity, StartChatOperationActivity.class);
        intent.putExtra(ValueUtils.ChatId,chatId);
        startActivity(intent);
    }
    private void displayApprovedResults() {
        inflater =   (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        taskExpandableListAdapter = new TaskExpandableListAdapter(approvedDoctorList,inflater,getContext(),this);
        expandableListView.setAdapter(taskExpandableListAdapter);
        taskExpandableListAdapter.notifyDataSetChanged();






//        approvedDocorAdapter = new ApprovedDocorAdapter();


    }

    @Override
    public void onRefresh() {
        getCategoryDoctorList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
