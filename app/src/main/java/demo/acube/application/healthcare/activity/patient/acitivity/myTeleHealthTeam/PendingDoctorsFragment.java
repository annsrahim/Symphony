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
import demo.acube.application.healthcare.activity.patient.adapters.TeleHealthDocotorListAdapter;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.secondaryDoctorsList.Datum;
import demo.acube.application.healthcare.model.secondaryDoctorsList.SecondaryDoctorsList;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingDoctorsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView pendingListView;
    private GlobalApplication globalApplication;
    private Activity activity;
    APIInterface apiInterface;
    private View rootView;
    LayoutInflater inflater;
    List<Datum> pendingDoctorsLists = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    public PendingDoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pending_doctors, container, false);
        globalApplication  = (GlobalApplication)activity.getApplicationContext();
        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        initUi();
        getPendingDoctorList();
        return rootView;
    }

    private void initUi() {
        pendingListView = (ListView)rootView.findViewById(R.id.pendingListView);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getPendingDoctorList() {

        pendingDoctorsLists.clear();
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

                }
            }

            @Override
            public void onFailure(Call<SecondaryDoctorsList> call, Throwable t) {

                Utils.showToast(activity,"Pending list failed "+t.getLocalizedMessage());
            }
        });
    }
    private void displayPendingResult()
    {
        inflater =   (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pendingListView.setAdapter(new TeleHealthDocotorListAdapter(activity,inflater,pendingDoctorsLists));

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
            getPendingDoctorList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
