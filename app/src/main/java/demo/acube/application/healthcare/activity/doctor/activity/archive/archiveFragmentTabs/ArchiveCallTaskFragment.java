package demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.DoctorScheduleDetailFragment;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.ITaskListClickListener;
import demo.acube.application.healthcare.activity.doctor.adapter.DoctorsTaskListAdapter;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveCallTaskFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private List<Datum> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DoctorsTaskListAdapter doctorsTaskListAdapter;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    int taskCount= 0;
    boolean _areTasksLoaded = false;
    private Activity activity;
    int totalPages = 0;
    int currentPage = 0;
    private TextView tvNoDatas;
    SwipeRefreshLayout swipeRefreshLayout;
    public ArchiveCallTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_call_task, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        tvNoDatas = (TextView)rootView.findViewById(R.id.tv_no_datas);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        doctorsTaskListAdapter = new DoctorsTaskListAdapter(taskList, activity, new ITaskListClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int type = taskList.get(position).getType();
                if(type==6)
                {

                }
                else
                {
                    ((DoctorHomeActivity)getActivity()).doctorTaskListBean = taskList.get(position);
                    ((DoctorHomeActivity)getActivity()).selectedPosition = position;
                    showScheduleDetail(position);
                }

            }

            @Override
            public void relaodList() {

            }
        });
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        globalApplication = (GlobalApplication)activity.getApplicationContext();
        getCallTaskList();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    private void getCallTaskList() {

        ((ArchiveHomeActivity)activity).startLoadingScreen();
        String userID = String.valueOf(globalApplication.getUserId());
        String taskType = "2,6";
        int page = currentPage+1;
        Call<DoctorTaskListBean> taskCall = apiInterface.doGetArchiveTypeTaskList(userID,taskType,page);
        taskCall.enqueue(new Callback<DoctorTaskListBean>() {
            @Override
            public void onResponse(Call<DoctorTaskListBean> call, Response<DoctorTaskListBean> response) {
                ((ArchiveHomeActivity)activity).stopLoadingScreen();
                if(response.code()==200)
                {
                    DoctorTaskListBean list = response.body();
                    totalPages = list.getMeta().getPagination().getTotalPages();
                    currentPage = list.getMeta().getPagination().getCurrentPage();
                    taskList.addAll(list.getData());
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
                displayDatas();
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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(doctorsTaskListAdapter);
        doctorsTaskListAdapter.notifyDataSetChanged();
        if(currentPage<totalPages)
        {
            getCallTaskList();
        }
    }


    @Override
    public void onClick(View v) {
        Log.d("TAG",v.getId()+"");
    }

    public void showScheduleDetail(int pos)
    {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DoctorScheduleDetailFragment scheduleDetailFragment = DoctorScheduleDetailFragment.newInsatnce();
        scheduleDetailFragment.show(ft,"scheduleDetailFragment");
    }

    private void goToCallStream()
    {
        Intent intent = new Intent(getActivity(), CallStreamActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onRefresh() {
        getCallTaskList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
