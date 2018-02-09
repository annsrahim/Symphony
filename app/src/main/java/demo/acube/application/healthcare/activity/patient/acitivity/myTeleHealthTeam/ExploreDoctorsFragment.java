package demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.adapters.TeleHealthAllDoctorListAdapter;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.Doctor;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.DoctorGroupSpecialityBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreDoctorsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private GlobalApplication globalApplication;
    private Activity activity;
    APIInterface apiInterface;
    private View rootView;
    private ListView allDoctorListView;
    List<Doctor> allDoctorsList = new ArrayList<>();
    LayoutInflater inflater;
    SwipeRefreshLayout swipeRefreshLayout;

    public ExploreDoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_explore_doctors, container, false);
        globalApplication  = (GlobalApplication)activity.getApplicationContext();
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        initUI();
        getAllDoctorsList();
        return rootView;
    }

    private void initUI() {
        allDoctorListView = (ListView)rootView.findViewById(R.id.allDoctorListView);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    private void getAllDoctorsList() {
        allDoctorsList.clear();
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

        allDoctorListView.setAdapter(new TeleHealthAllDoctorListAdapter(allDoctorsList,getContext()));

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    @Override
    public void onRefresh() {
        getAllDoctorsList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
