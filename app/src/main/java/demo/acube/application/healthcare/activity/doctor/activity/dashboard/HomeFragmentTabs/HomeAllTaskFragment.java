package demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

import demo.acube.application.healthcare.activity.doctor.activity.dashboard.DoctorHomeActivity;
import demo.acube.application.healthcare.activity.doctor.models.taskList.Datum;
import demo.acube.application.healthcare.activity.doctor.models.taskList.DoctorTaskListBean;
import demo.acube.application.healthcare.activity.interfaces.ICallRequestListener;
import demo.acube.application.healthcare.activity.patient.acitivity.chat.StartChatOperationActivity;
import demo.acube.application.healthcare.activity.stream.CallStreamActivity;
import demo.acube.application.healthcare.activity.stream.VideoCallStreamAcitivity;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeAllTaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Activity activity;

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
    private Paint p = new Paint();
    private TextView tvNoDatas;
    SwipeRefreshLayout swipeRefreshLayout;




    public HomeAllTaskFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_all_task, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        tvNoDatas = (TextView)rootView.findViewById(R.id.tv_no_datas);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
                doctorsTaskListAdapter = new DoctorsSwipeTaskListAdapter(taskList, activity, new ITaskListClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Clicked Positon",position+" Pos");
                int itemType = taskList.get(position).getType();
                doActionOnItemClick(itemType,position);
            }

                    @Override
                    public void relaodList() {
                        reloadTaskList();
                    }
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(doctorsTaskListAdapter);



        apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        globalApplication = (GlobalApplication)activity.getApplicationContext();


       // initSwipe();
        return rootView;
    }

    private void initSwipe()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                if(direction==ItemTouchHelper.LEFT)
                {
                    Log.d("LEft ","Pos "+position);
                }
                else
                {
                    Log.d("Right ","Pos "+position);
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Paint textPaint = new Paint();
                textPaint.setColor(Color.WHITE);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getDimension(R.dimen._13sdp));
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 5;

                    if(dX > 0){

                        p.setColor(ContextCompat.getColor(getActivity(),R.color.swipe_blue));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        c.drawText("Mark as Complete", dX/3, (float)itemView.getTop()+((float)itemView.getHeight()/2), textPaint);
                    }
                    else {

                        p.setColor(ContextCompat.getColor(getActivity(),R.color.swipe_red));
                        RectF background = new RectF((float) itemView.getRight()/2 + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);

                        c.drawText("Remind", c.getWidth()/4, (float)itemView.getTop()+((float)itemView.getHeight()/2), textPaint);

                        p.setColor(ContextCompat.getColor(getActivity(),R.color.swipe_oragne));
                        RectF icon_dest = new RectF((float) itemView.getRight() / 2 ,(float) itemView.getTop(),(float) itemView.getRight(),(float)itemView.getBottom());
                        c.drawRect(icon_dest,p);

                        c.drawText("Cancel", c.getWidth()-(c.getWidth()/4), (float)itemView.getTop()+((float)itemView.getHeight()/2), textPaint);

                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close_white);
                        c.drawBitmap(bmp, c.getWidth()/6, (float)itemView.getTop()+((float)itemView.getHeight()/2), null);
                    }
                }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper  itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    private void getAllTaskList() {
        ((DoctorHomeActivity)activity).startLoadingScreen();
        String userId = String.valueOf(globalApplication.getUserId());
        int page = currentPage+1;
        Call<DoctorTaskListBean> taskCall = apiInterface.doGetDocotorTaskList(userId,page);
        Log.d("Service Call","Page "+currentPage+1);
        taskCall.enqueue(new Callback<DoctorTaskListBean>() {
            @Override
            public void onResponse(Call<DoctorTaskListBean> call, Response<DoctorTaskListBean> response) {
                ((DoctorHomeActivity)activity).stopLoadingScreen();
                if(response.code()==200)
                {
                    DoctorTaskListBean list = response.body();
                    totalPages = response.body().getMeta().getPagination().getTotalPages();
                    currentPage = response.body().getMeta().getPagination().getCurrentPage();
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
                ((DoctorHomeActivity)activity).startLoadingScreen();
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

        doctorsTaskListAdapter.notifyDataSetChanged();
        if(currentPage<totalPages)
        {
            getAllTaskList();
        }
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
        getAllTaskList();
    }

    public void doActionOnItemClick(int type, int pos)
    {
        if(type==1)
        {
            int chatId = taskList.get(pos).getTaskMeta().getChatId();
            globalApplication.setPatSelectedCallerType(ValueUtils.patient);
            String name = taskList.get(pos).getTaskMeta().getPatient().getName();
            globalApplication.setPatSelectedCallerName(name);
            goToChatActivity(chatId);
        }
        else if(type==2 || type==3 || type==4)
        {
            ((DoctorHomeActivity)getActivity()).doctorTaskListBean = taskList.get(pos);
            ((DoctorHomeActivity)getActivity()).selectedPosition = pos;
            showScheduleDetail(pos);
            Log.d("TaskType","Call");
        }

        else if(type==6)
        {
            int callerId = taskList.get(pos).getTaskMeta().getReceiver().getUserId();
            int receiverID = taskList.get(pos).getTaskMeta().getSender().getUserId();
            String receiverName = taskList.get(pos).getTaskMeta().getSender().getName();
            globalApplication.setPatSelectedCallerId(receiverID);
            globalApplication.setPatSelectedCallerName(receiverName);

           Utils.callRequest(getActivity(), ValueUtils.CALL,callerId,receiverID,new ICallRequestListener() {
               @Override
               public void onCallRequestCompleted() {
                   goToCallStream();
               }

               @Override
               public void onCallRequestFailed(String msg) {
                    Utils.showToast(getActivity(),msg);
               }
           });
        }
        else if(type==7)
        {
            int callerId = taskList.get(pos).getTaskMeta().getReceiver().getUserId();
            int receiverID = taskList.get(pos).getTaskMeta().getSender().getUserId();
            String receiverName = taskList.get(pos).getTaskMeta().getSender().getName();
            globalApplication.setPatSelectedCallerId(receiverID);
            globalApplication.setPatSelectedCallerName(receiverName);

            Utils.callRequest(getActivity(),ValueUtils.VISIT,callerId,receiverID,new ICallRequestListener() {
                @Override
                public void onCallRequestCompleted() {
                    goToVideoCallStream();
                }

                @Override
                public void onCallRequestFailed(String msg) {
                    Utils.showToast(getActivity(),msg);
                }
            });
        }
    }

    private void goToVideoCallStream() {
        Intent intent = new Intent(getActivity(), VideoCallStreamAcitivity.class);
        activity.startActivity(intent);
    }

    private void goToCallStream()
    {
        Intent intent = new Intent(getActivity(), CallStreamActivity.class);
        activity.startActivity(intent);
    }
    private void goToChatActivity(int chatId)
    {
        Intent intent = new Intent(getActivity(), StartChatOperationActivity.class);
        intent.putExtra(ValueUtils.ChatId,chatId);
        activity.startActivity(intent);
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
