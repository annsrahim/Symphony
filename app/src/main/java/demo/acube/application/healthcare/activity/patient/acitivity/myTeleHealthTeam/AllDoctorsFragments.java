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
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;

import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.doctorsInformation.DoctorsInformationActivity;
import demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment.SelectAppointmentTypeActivity;
import demo.acube.application.healthcare.activity.patient.adapters.TaskExpandableListAdapter;
import demo.acube.application.healthcare.activity.patient.adapters.TeleHealthAllDoctorListAdapter;
import demo.acube.application.healthcare.activity.patient.adapters.TeleHealthDocotorListAdapter;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.chatModel.ChatInitiateModel;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.Doctor;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.DoctorGroupSpecialityBean;
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
public class AllDoctorsFragments extends Fragment implements IExpandableChildItemListener,SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout llApprovedDoctorContainer;
    private ExpandableListView expandableListView;
    private ListView pendingListView;
    private GlobalApplication globalApplication;
    private Activity activity;
    APIInterface apiInterface;
    private View rootView;
    PrimaryDoctor primaryDoctor ;
    List<Datum> approvedDoctorList = new ArrayList<>();
    private ProgressDialog progressDialog;
    ApprovedDocorAdapter approvedDocorAdapter;
    TaskExpandableListAdapter taskExpandableListAdapter;
    int previousGroup = -1;
    private Integer chatId;
    List<Datum> pendingDoctorsLists = new ArrayList<>();
    LayoutInflater inflater;
    List<Doctor> allDoctorsList = new ArrayList<>();
    private ListView allDoctorListView;
    private TextView tvHeaderPendingList;
    SwipeRefreshLayout swipeRefreshLayout;
    TeleHealthAllDoctorListAdapter allDoctorListAdapter;
    TeleHealthDocotorListAdapter pendingDoctorListAdapter;



    public AllDoctorsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_all_doctors, container, false);
        globalApplication  = (GlobalApplication)activity.getApplicationContext();
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        primaryDoctor = globalApplication.getUserDetailsBean().getData().getPrimaryDoctor();
        initUI();
        getCategoryDocotorsList();

        return rootView;
    }

    private void getCategoryDocotorsList() {
        addPrimaryDoctorInList();
        getApprovedDoctorList();
        getPendingDoctorList();
        getAllDoctorsList();
        progressDialog.dismiss();

    }

    private void getAllDoctorsList() {
        Call<DoctorGroupSpecialityBean> doctorGroupSpecialityBeanCall = apiInterface.getDoctorGroupSpeciality(globalApplication.getUserId());
        doctorGroupSpecialityBeanCall.enqueue(new Callback<DoctorGroupSpecialityBean>() {
            @Override
            public void onResponse(Call<DoctorGroupSpecialityBean> call, Response<DoctorGroupSpecialityBean> response) {
                if(response.code()==200)
                {
                    for(int i=0;i<response.body().getData().size();i++)
                        allDoctorsList.addAll(response.body().getData().get(i).getDoctors());
                    displayAllDoctorsList();
                }
                else
                {
                    Utils.showToast(activity,"Unable to fetch data");
                }
            }

            @Override
            public void onFailure(Call<DoctorGroupSpecialityBean> call, Throwable t) {
                Utils.showToast(activity,"Unable to fetch data "+t.getLocalizedMessage());
            }
        });
    }

    private void displayAllDoctorsList() {
        allDoctorListAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnItems(allDoctorListView);
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

    private void getApprovedDoctorList()
    {
        progressDialog = LoadingDialog.show(getActivity(),"Loading");
        final Call<SecondaryDoctorsList> secondaryDoctorsListCall = apiInterface.doGetSecondaryDoctorList(globalApplication.getUserId());
        secondaryDoctorsListCall.enqueue(new Callback<SecondaryDoctorsList>() {
            @Override
            public void onResponse(Call<SecondaryDoctorsList> call, Response<SecondaryDoctorsList> response) {

                if(response.code()==200)
                {
                    approvedDoctorList.addAll(response.body().getData());
                    taskExpandableListAdapter.notifyDataSetChanged();
                    setExpandableListViewHeightBasedOnItems(expandableListView);
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

    private void displayApprovedResults() {

        taskExpandableListAdapter = new TaskExpandableListAdapter(approvedDoctorList,inflater,getContext(),this);
        expandableListView.setAdapter(taskExpandableListAdapter);
        taskExpandableListAdapter.notifyDataSetChanged();

//        approvedDocorAdapter = new ApprovedDocorAdapter();


    }

    private  void setExpandableListViewHeightBasedOnItems(ExpandableListView expandableListView) {
        ExpandableListAdapter listAdapter = expandableListView.getExpandableListAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getGroupCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getGroupView(itemPos,false, expandableListView,null);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
                if(expandableListView.isGroupExpanded(itemPos))
                {
                    View childItem = listAdapter.getChildView(itemPos,0,false,expandableListView,null);
                    childItem.measure(0,0);
                    totalItemsHeight+= childItem.getMeasuredHeight();
                }

            }

            // Get total height of all item dividers.
            int totalDividersHeight = expandableListView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            expandableListView.setLayoutParams(params);
            expandableListView.requestLayout();


        }
    }

    private void goToDoctorsInformation() {
        startActivity(new Intent(activity, DoctorsInformationActivity.class));
    }

    private void getPendingDoctorList() {

        int userId = globalApplication.getUserId();
        final Call<SecondaryDoctorsList> pendingDoctorListCall = apiInterface.doGetPendingDoctorList(userId);
        pendingDoctorListCall.enqueue(new Callback<SecondaryDoctorsList>() {
            @Override
            public void onResponse(Call<SecondaryDoctorsList> call, Response<SecondaryDoctorsList> response) {

                if(response.code()==200)
                {
                    if(response.body().getData()!=null)
                    {
                        pendingDoctorsLists.addAll(response.body().getData());
                        displayPendingResult();
                    }

                }
                else
                {
                    tvHeaderPendingList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SecondaryDoctorsList> call, Throwable t) {
                tvHeaderPendingList.setVisibility(View.GONE);
                    Utils.showToast(activity,"Pending list failed "+t.getLocalizedMessage());
            }
        });
    }

    private void displayPendingResult()
    {
        pendingDoctorListAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnItems(pendingListView);
    }

    private void initUI() {
        inflater =   (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llApprovedDoctorContainer = (LinearLayout) rootView.findViewById(R.id.llApprovedDoctorsContainer);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.listView);
        tvHeaderPendingList = (TextView)rootView.findViewById(R.id.tv_header_pending_list);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
                setExpandableListViewHeightBasedOnItems(expandableListView);

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setExpandableListViewHeightBasedOnItems(expandableListView);
            }
        });
        pendingListView = (ListView)rootView.findViewById(R.id.pendingListView);

        allDoctorListView = (ListView)rootView.findViewById(R.id.allDoctorListView);

        approvedDocorAdapter = new ApprovedDocorAdapter();
        allDoctorListAdapter = new TeleHealthAllDoctorListAdapter(allDoctorsList,getContext());
        pendingDoctorListAdapter = new TeleHealthDocotorListAdapter(activity,inflater,pendingDoctorsLists);

        pendingListView.setAdapter(pendingDoctorListAdapter);
        setListViewHeightBasedOnItems(pendingListView);

        allDoctorListView.setAdapter(allDoctorListAdapter);



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

    @Override
    public void goToChatOperation(int groupPos) {

        int receiverId = approvedDoctorList.get(groupPos).getUserId();
        String receiverName = approvedDoctorList.get(groupPos).getName();
        globalApplication.setPatSelectedCallerType(ValueUtils.patient);
        globalApplication.setPatSelectedCallerId(receiverId);
        globalApplication.setPatSelectedCallerName(receiverName);

        getChatInformation();
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
    public void onRefresh() {

        onSwipeRefreshDoctorsList();

    }

    private void onSwipeRefreshDoctorsList() {

        approvedDoctorList.clear();
        approvedDocorAdapter.notifyDataSetChanged();
        pendingDoctorsLists.clear();
        pendingDoctorListAdapter.notifyDataSetChanged();
        allDoctorsList.clear();
        allDoctorListAdapter.notifyDataSetChanged();

        getCategoryDocotorsList();
        swipeRefreshLayout.setRefreshing(false);

    }


    public class ApprovedDocorAdapter extends BaseAdapter
    {

        private  LayoutInflater inflater=null;

        public ApprovedDocorAdapter() {
            inflater = ( LayoutInflater )activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        public class Holder
        {
            TextView tvDoctorName,tvSpecialityType;
            ImageView ivContactFetures,ivAvatar;
            LinearLayout llConatactFetaureContainer;


        }

        @Override
        public int getCount() {
            return approvedDoctorList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View rowView;
            rowView = inflater.inflate(R.layout.layout_doctor_list_apporved,null);
             final Holder holder = new Holder();
            holder.tvDoctorName = (TextView) rowView.findViewById(R.id.list_tv_doctor_name);
            holder.tvSpecialityType = (TextView) rowView.findViewById(R.id.list_tv_speciality_type);
            holder.ivContactFetures = (ImageView)rowView.findViewById(R.id.list_iv_expand_view);
            holder.llConatactFetaureContainer = (LinearLayout)rowView.findViewById(R.id.llContactFetaureContainer);


            holder.tvDoctorName.setText(String.format("Dr. %s", approvedDoctorList.get(position).getName()));
            holder.tvSpecialityType.setText(approvedDoctorList.get(position).getSpecialty().getName());




            return rowView;
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }

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

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }


}
