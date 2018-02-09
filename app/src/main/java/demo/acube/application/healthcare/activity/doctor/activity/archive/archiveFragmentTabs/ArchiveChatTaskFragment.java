package demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.archive.ArchiveHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.ITaskListClickListener;
import demo.acube.application.healthcare.activity.doctor.adapter.DoctorsTaskListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveChatTaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<Datum> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DoctorsTaskListAdapter doctorsTaskListAdapter;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    int taskCount= 0;
    int totalPages = 0;
    int currentPage = 0;
    boolean _areTasksLoaded = false;
    private Activity activity;
    private TextView tvNoDatas;
    SwipeRefreshLayout swipeRefreshLayout;
    public ArchiveChatTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_chat_task, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvNoDatas = (TextView)rootView.findViewById(R.id.tv_no_datas);
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        globalApplication = (GlobalApplication)activity.getApplicationContext();
        getChatTaskList();
        return rootView;
    }


    private void getChatTaskList() {
        ((ArchiveHomeActivity)activity).startLoadingScreen();
        String userID = String.valueOf(globalApplication.getUserId());
        int page = currentPage+1;
        Call<DoctorTaskListBean> taskCall = apiInterface.doGetArchiveTypeTaskList(userID,"1",page);
        taskCall.enqueue(new Callback<DoctorTaskListBean>() {
            @Override
            public void onResponse(Call<DoctorTaskListBean> call, Response<DoctorTaskListBean> response) {
                ((ArchiveHomeActivity)activity).stopLoadingScreen();
                if(response.code()==200)
                {
                    DoctorTaskListBean list = response.body();
                    taskList.addAll(list.getData());
                    totalPages = list.getMeta().getPagination().getTotalPages();
                    currentPage = list.getMeta().getPagination().getCurrentPage();
                    taskCount = list.getMeta().getPagination().getCount();
                    if(taskList.size()>0)
                    {
                        tvNoDatas.setVisibility(View.GONE);
                        displayDatas();
                    }
                    else
                        displayNoDatas();
                }
                else
                {
                    displayNoDatas();
                    Utils.showToast(activity,"Unable to fetch task");
                }
            }

            @Override
            public void onFailure(Call<DoctorTaskListBean> call, Throwable t) {
                ((ArchiveHomeActivity)activity).stopLoadingScreen();
                displayNoDatas();
                Utils.showToast(activity,t.getLocalizedMessage());

            }
        });



    }
    public void displayNoDatas()
    {
        tvNoDatas.setVisibility(View.VISIBLE);
    }
    public void displayDatas()
    {

        doctorsTaskListAdapter = new DoctorsTaskListAdapter(taskList, activity, new ITaskListClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int chatId = taskList.get(position).getTaskMeta().getChatId();
                globalApplication.setPatSelectedCallerType(ValueUtils.patient);
                String name = taskList.get(position).getTaskMeta().getPatient().getName();
                globalApplication.setPatSelectedCallerName(name);
                goToChatActivity(chatId);
            }

            @Override
            public void relaodList() {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(doctorsTaskListAdapter);
        doctorsTaskListAdapter.notifyDataSetChanged();
        if(currentPage<totalPages)
        {
            getChatTaskList();
        }
    }

    private void goToChatActivity(int chatId) {
        Intent intent = new Intent(getActivity(), StartChatOperationActivity.class);
        intent.putExtra(ValueUtils.ChatId,chatId);
        activity.startActivity(intent);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(menuVisible && activity!=null)
            getChatTaskList();
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void onRefresh() {
       getChatTaskList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
