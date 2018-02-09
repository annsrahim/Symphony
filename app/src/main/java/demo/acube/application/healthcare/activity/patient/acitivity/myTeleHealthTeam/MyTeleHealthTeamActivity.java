package demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam;

import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.patient.adapters.TeleHealthSearchAdapter;
import demo.acube.application.healthcare.activity.patient.adapters.TelehealthTeamPagerAdapter;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.model.doctorGroupSpeciality.DoctorGroupSpecialityBean;


import demo.acube.application.healthcare.model.teleHealthTeamSearch.Address__;
import demo.acube.application.healthcare.model.teleHealthTeamSearch.Data;
import demo.acube.application.healthcare.model.teleHealthTeamSearch.Name;

import demo.acube.application.healthcare.model.teleHealthTeamSearch.Specialty_;
import demo.acube.application.healthcare.model.teleHealthTeamSearch.TeleHealthTeamSearchModel;
import demo.acube.application.healthcare.model.userDetails.Accessible;
import demo.acube.application.healthcare.model.userDetails.Address;
import demo.acube.application.healthcare.model.userDetails.Phone;
import demo.acube.application.healthcare.model.userDetails.PrimaryDoctor;
import demo.acube.application.healthcare.model.userDetails.Specialty;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

public class MyTeleHealthTeamActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressDialog;
    GlobalApplication globalApplication;
    private TextView tvAllTeam,tvApprovedTeam,tvPendingTeam,tvExploreMenu;
    DoctorGroupSpecialityBean doctorGroupSpeciality;
    ViewPager viewPager;
    TelehealthTeamPagerAdapter telehealthTeamPagerAdapter;
    LinearLayout llSearchContainer;
    EditText edSearchText;
    FrameLayout frameSearchResults;
    TextView tvNoResults;
    ListView lvSearchResults;
    List<PrimaryDoctor> doctorsList = new ArrayList<>();
    TeleHealthSearchAdapter teleHealthSearchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tele_health_team);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        globalApplication = (GlobalApplication)this.getApplicationContext();
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        initToolbar();
        initUI();
        getDoctorGroupSpeciality();
    }



    private void getDoctorGroupSpeciality() {
        progressDialog = LoadingDialog.show(this,"Loading");
        final Call<DoctorGroupSpecialityBean> doctorGroupSpecialityBeanCall = apiInterface.getDoctorGroupSpeciality(globalApplication.getUserId());
        doctorGroupSpecialityBeanCall.enqueue(new Callback<DoctorGroupSpecialityBean>() {
            @Override
            public void onResponse(Call<DoctorGroupSpecialityBean> call, Response<DoctorGroupSpecialityBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    doctorGroupSpeciality  = response.body();
                    displayDatas();
                }
                else
                {
                    Utils.showToast(MyTeleHealthTeamActivity.this,"Unable to fetch datas");
                }


            }

            @Override
            public void onFailure(Call<DoctorGroupSpecialityBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(MyTeleHealthTeamActivity.this,"Unable to fetch datas "+t.getLocalizedMessage());
            }
        });
    }

    private void displayDatas()
    {

    }

    private void initUI() {
        tvNoResults = (TextView)findViewById(R.id.tv_no_results);
        lvSearchResults = (ListView)findViewById(R.id.lvSearchResult);
        teleHealthSearchAdapter = new TeleHealthSearchAdapter(this);

        frameSearchResults = (FrameLayout)findViewById(R.id.frameSearchResults);
        frameSearchResults.bringToFront();
        tvAllTeam = (TextView)findViewById(R.id.tv_all_team);
        tvAllTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        tvAllTeam.setTextColor(ContextCompat.getColor(this,R.color.White));
        tvApprovedTeam = (TextView)findViewById(R.id.tv_approved_team);
        tvPendingTeam = (TextView)findViewById(R.id.tv_pending_team);
        tvExploreMenu = (TextView)findViewById(R.id.tv_explore_menu);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TelehealthTeamPagerAdapter(getSupportFragmentManager(),4,this));
        llSearchContainer = (LinearLayout)findViewById(R.id.ll_search_container);
        edSearchText = (EditText)findViewById(R.id.ed_search_text);
        edSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(v.getText().toString().length()>=2)
                        performSearch(v.getText().toString());
                    else
                        Utils.showToast(MyTeleHealthTeamActivity.this,"Try atleast with two characters");
                    return true;
                }
                return false;
            }
        });

    }

    private void performSearch(String searchQuery) {

        progressDialog = LoadingDialog.show(this,"Searching...");
        Call<TeleHealthTeamSearchModel> teleHealthTeamSearchModelCall = apiInterface.searchTeleHealthTeam(searchQuery);
        teleHealthTeamSearchModelCall.enqueue(new Callback<TeleHealthTeamSearchModel>() {
            @Override
            public void onResponse(Call<TeleHealthTeamSearchModel> call, Response<TeleHealthTeamSearchModel> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    if(response.body().getSuccess().getCode().equals(getString(R.string.healthteam_found)))
                         addSearchResultsInList(response.body().getData());
                    else
                        displayNoSearchResults();
                }
                else
                {
                    Utils.showToast(MyTeleHealthTeamActivity.this,"Unable to get data ");
                }
            }

            @Override
            public void onFailure(Call<TeleHealthTeamSearchModel> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(MyTeleHealthTeamActivity.this,"Unable to get data "+t.getLocalizedMessage());
            }
        });



    }

    private void displayNoSearchResults() {
        teleHealthSearchAdapter.clearAllDatas();
        frameSearchResults.setVisibility(View.VISIBLE);
        tvNoResults.setVisibility(View.VISIBLE);


    }

    private void addSearchResultsInList(Data data) {
        teleHealthSearchAdapter.clearAllDatas();
        tvNoResults.setVisibility(View.GONE);
        frameSearchResults.setVisibility(View.VISIBLE);
        int nameResultSize = data.getName().size();
        int specialityResultSize = data.getSpecialty().size();
        int addressResultSize = data.getAddress().size();

        if(nameResultSize>0)
        {
            teleHealthSearchAdapter.addSectionHeaderItem("Result By Name");
            for(int i=0;i<nameResultSize;i++)
            {
                Name name =  data.getName().get(i);
                teleHealthSearchAdapter.addItem(addPrimaryDoctorInListByName(name));
            }
        }
        if(specialityResultSize>0)
        {
            teleHealthSearchAdapter.addSectionHeaderItem("Result By Speciality");
            for(int i=0;i<specialityResultSize;i++)
            {
                teleHealthSearchAdapter.addItem(addPrimaryDoctorInListBySearch(data.getSpecialty().get(i)));
            }
        }
        if(addressResultSize>0)
        {
            teleHealthSearchAdapter.addSectionHeaderItem("Result By Address");
            for(int i=0;i<addressResultSize;i++)
            {

                teleHealthSearchAdapter.addItem(addPrimaryDoctorInListByAdress(data.getAddress().get(i)));
            }
        }


        lvSearchResults.setAdapter(teleHealthSearchAdapter);

    }

    private PrimaryDoctor addPrimaryDoctorInListByAdress(Address__ nameData) {
        PrimaryDoctor primaryDoctor = new PrimaryDoctor();

        primaryDoctor.setName(nameData.getName());
        primaryDoctor.setUserType(nameData.getUserType());
        primaryDoctor.setEmail(nameData.getEmail());
        primaryDoctor.setOpusEmail(nameData.getOpusEmail());
        primaryDoctor.setDob(nameData.getDob());
        primaryDoctor.setAge(nameData.getAge());
        primaryDoctor.setAvatar(nameData.getAvatar());
        primaryDoctor.setBio(nameData.getBio());
        primaryDoctor.setDnd(nameData.getDnd());

        Phone phone =  new Phone();
        phone.setPersonal(nameData.getPhone().getPersonal());
        primaryDoctor.setPhone(phone);


        Accessible accessible = new Accessible();
        accessible.setCall(nameData.getAccessible().getCall());
        accessible.setText(nameData.getAccessible().getText());
        accessible.setVisit(nameData.getAccessible().getVisit());
        primaryDoctor.setAccessible(accessible);

        Address address = new Address();
        address.setAddressId(nameData.getAddress().getAddressId());
        address.setCity(nameData.getAddress().getCity());
        address.setCountry(nameData.getAddress().getCountry());
        address.setPrimary(nameData.getAddress().getPrimary());
        address.setState(nameData.getAddress().getState());
        address.setStreet1(nameData.getAddress().getStreet1());
        address.setStreet2(nameData.getAddress().getStreet2());
        address.setZip(nameData.getAddress().getZip());
        primaryDoctor.setAddress(address);

        Specialty speciality = new Specialty();
        speciality.setPrimary(nameData.getSpecialty().getPrimary());
        speciality.setName(nameData.getSpecialty().getName());
        speciality.setSpecialtyId(nameData.getSpecialty().getSpecialtyId());
        primaryDoctor.setSpecialty(speciality);


        return primaryDoctor;
    }

    private PrimaryDoctor addPrimaryDoctorInListBySearch(Specialty_ nameData) {
        PrimaryDoctor primaryDoctor = new PrimaryDoctor();

        primaryDoctor.setName(nameData.getName());
        primaryDoctor.setUserType(nameData.getUserType());
        primaryDoctor.setEmail(nameData.getEmail());
        primaryDoctor.setOpusEmail(nameData.getOpusEmail());
        primaryDoctor.setDob(nameData.getDob());
        primaryDoctor.setAge(nameData.getAge());
        primaryDoctor.setAvatar(nameData.getAvatar());
        primaryDoctor.setBio(nameData.getBio());
        primaryDoctor.setDnd(nameData.getDnd());

        Phone phone =  new Phone();
        phone.setPersonal(nameData.getPhone().getPersonal());
        primaryDoctor.setPhone(phone);


        Accessible accessible = new Accessible();
        accessible.setCall(nameData.getAccessible().getCall());
        accessible.setText(nameData.getAccessible().getText());
        accessible.setVisit(nameData.getAccessible().getVisit());
        primaryDoctor.setAccessible(accessible);

        Address address = new Address();
        address.setAddressId(nameData.getAddress().getAddressId());
        address.setCity(nameData.getAddress().getCity());
        address.setCountry(nameData.getAddress().getCountry());
        address.setPrimary(nameData.getAddress().getPrimary());
        address.setState(nameData.getAddress().getState());
        address.setStreet1(nameData.getAddress().getStreet1());
        address.setStreet2(nameData.getAddress().getStreet2());
        address.setZip(nameData.getAddress().getZip());
        primaryDoctor.setAddress(address);

        Specialty speciality = new Specialty();
        speciality.setPrimary(nameData.getSpecialty().getPrimary());
        speciality.setName(nameData.getSpecialty().getName());
        speciality.setSpecialtyId(nameData.getSpecialty().getSpecialtyId());
        primaryDoctor.setSpecialty(speciality);


        return primaryDoctor;
    }

    private PrimaryDoctor addPrimaryDoctorInListByName(Name nameData) {

        PrimaryDoctor primaryDoctor = new PrimaryDoctor();

        primaryDoctor.setName(nameData.getName());
        primaryDoctor.setUserType(nameData.getUserType());
        primaryDoctor.setEmail(nameData.getEmail());
        primaryDoctor.setOpusEmail(nameData.getOpusEmail());
        primaryDoctor.setDob(nameData.getDob());
        primaryDoctor.setAge(nameData.getAge());
        primaryDoctor.setAvatar(nameData.getAvatar());
        primaryDoctor.setBio(nameData.getBio());
        primaryDoctor.setDnd(nameData.getDnd());

        Phone phone =  new Phone();
        phone.setPersonal(nameData.getPhone().getPersonal());
        primaryDoctor.setPhone(phone);


        Accessible accessible = new Accessible();
        accessible.setCall(nameData.getAccessible().getCall());
        accessible.setText(nameData.getAccessible().getText());
        accessible.setVisit(nameData.getAccessible().getVisit());
        primaryDoctor.setAccessible(accessible);

        Address address = new Address();
        address.setAddressId(nameData.getAddress().getAddressId());
        address.setCity(nameData.getAddress().getCity());
        address.setCountry(nameData.getAddress().getCountry());
        address.setPrimary(nameData.getAddress().getPrimary());
        address.setState(nameData.getAddress().getState());
        address.setStreet1(nameData.getAddress().getStreet1());
        address.setStreet2(nameData.getAddress().getStreet2());
        address.setZip(nameData.getAddress().getZip());
        primaryDoctor.setAddress(address);

        Specialty speciality = new Specialty();
        speciality.setPrimary(nameData.getSpecialty().getPrimary());
        speciality.setName(nameData.getSpecialty().getName());
        speciality.setSpecialtyId(nameData.getSpecialty().getSpecialtyId());
        primaryDoctor.setSpecialty(speciality);


      return primaryDoctor;
    }





    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.my_telehealth_team);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setTextColor(ContextCompat.getColor(this,R.color.White));
        toolbarTitle.setTypeface(Utils.getFuturaFont(this));
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_blue, null));
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_search_icon:
                    openSearchMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showAllTeam(View view) {
        setSelectedColorForTab(0);
    }



    public void showApprovedTeam(View view) {
        setSelectedColorForTab(1);
    }

    public void showPendingTeam(View view) {
        setSelectedColorForTab(2);
    }

    public void showExploreScreen(View view) {
        setSelectedColorForTab(3);
    }

    private void setSelectedColorForTab(int pos)
    {
        switch (pos)
        {
            case 0:
                    tvAllTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
                    tvApprovedTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvPendingTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvExploreMenu.setBackgroundColor(ContextCompat.getColor(this,R.color.White));

                    tvAllTeam.setTextColor(ContextCompat.getColor(this,R.color.White));
                    tvApprovedTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvPendingTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvExploreMenu.setTextColor(ContextCompat.getColor(this,R.color.black));

                    viewPager.setCurrentItem(0);

                break;
            case 1:
                    tvAllTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvApprovedTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
                    tvPendingTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvExploreMenu.setBackgroundColor(ContextCompat.getColor(this,R.color.White));

                    tvAllTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvApprovedTeam.setTextColor(ContextCompat.getColor(this,R.color.White));
                    tvPendingTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvExploreMenu.setTextColor(ContextCompat.getColor(this,R.color.black));

                    viewPager.setCurrentItem(1);

                break;
            case 2:
                    tvAllTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvApprovedTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvPendingTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
                    tvExploreMenu.setBackgroundColor(ContextCompat.getColor(this,R.color.White));

                    tvAllTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvApprovedTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvPendingTeam.setTextColor(ContextCompat.getColor(this,R.color.White));
                    tvExploreMenu.setTextColor(ContextCompat.getColor(this,R.color.black));

                    viewPager.setCurrentItem(2);

                break;
            case 3:
                    tvAllTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvApprovedTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvPendingTeam.setBackgroundColor(ContextCompat.getColor(this,R.color.White));
                    tvExploreMenu.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));

                    tvAllTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvApprovedTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvPendingTeam.setTextColor(ContextCompat.getColor(this,R.color.black));
                    tvExploreMenu.setTextColor(ContextCompat.getColor(this,R.color.White));

                    viewPager.setCurrentItem(4);

                break;
        }
    }

    public void closeSearchMenu(View view) {
        edSearchText.setText("");
        frameSearchResults.setVisibility(View.GONE);
        llSearchContainer.setVisibility(View.GONE);
    }
    public void openSearchMenu()
    {

        llSearchContainer.setVisibility(View.VISIBLE);
    }
}
