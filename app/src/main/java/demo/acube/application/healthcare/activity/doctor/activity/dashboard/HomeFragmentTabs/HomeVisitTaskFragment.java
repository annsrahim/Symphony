package demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.activity.doctor.adapter.DoctorsSwipeTaskListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.adapter.DoctorsTaskListAdapter;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeVisitTaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<Datum> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DoctorsSwipeTaskListAdapter doctorsTaskListAdapter;
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

    public HomeVisitTaskFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_home_visit_task, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        tvNoDatas = (TextView)rootView.findViewById(R.id.tv_no_datas);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        globalApplication = (GlobalApplication)activity.getApplicationContext();
        doctorsTaskListAdapter = new DoctorsSwipeTaskListAdapter(taskList, activity, new ITaskListClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((DoctorHomeActivity)getActivity()).doctorTaskListBean = taskList.get(position);
                ((DoctorHomeActivity)getActivity()).selectedPosition = position;
                showScheduleDetail(position);
            }

            @Override
            public void relaodList() {
                reloadTaskList();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(doctorsTaskListAdapter);


        return rootView;
    }

    @Override
    public void onResume() {
        reloadTaskList();
        super.onResume();
    }

    public void reloadTaskList()
    {
        currentPage=0;
        taskList.clear();
        ((DoctorHomeActivity)getActivity()).getTaskCountForTab();
        getVisitTaskList();
    }
    public void displayNoDatas()
    {
        tvNoDatas.setVisibility(View.VISIBLE);
    }

    private void getVisitTaskList() {

        ((DoctorHomeActivity)activity).startLoadingScreen();
        String userID = String.valueOf(globalApplication.getUserId());
        int page = currentPage+1;
        Call<DoctorTaskListBean> taskCall = apiInterface.doGetDocotorCallTaskList(userID,4,page);
        taskCall.enqueue(new Callback<DoctorTaskListBean>() {
            @Override
            public void onResponse(Call<DoctorTaskListBean> call, Response<DoctorTaskListBean> response) {
                ((DoctorHomeActivity)activity).stopLoadingScreen();
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
                ((DoctorHomeActivity)activity).startLoadingScreen();
                displayNoDatas();
                Utils.showToast(activity,t.getLocalizedMessage());

            }
        });



    }



    public void displayDatas()
    {

        doctorsTaskListAdapter.notifyDataSetChanged();
        if(currentPage<totalPages)
        {
            getVisitTaskList();
        }
    }
    public void showScheduleDetail(int pos)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DoctorScheduleDetailFragment scheduleDetailFragment = DoctorScheduleDetailFragment.newInsatnce();
        scheduleDetailFragment.show(ft,"scheduleDetailFragment");
    }


    @Override
    public void onRefresh() {
      reloadTaskList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
