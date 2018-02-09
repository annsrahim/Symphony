package demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.models.doctorDetails.DoctorDetailsBean;
import demo.acube.application.healthcare.activity.doctor.models.doctorDetails.Officehours;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorMyProfileFragment extends Fragment implements View.OnClickListener {


    private View rootView;
    private Context mContext;
    private TextView tvMondayTime,tvTuesdayTime,tvWednesdayTime,tvThursdayTime,tvFridayTime,tvSaturdayTime,tvSundayTime;
    private TextView tvDoctorName,tvDoctorSepciality,tvAddress1,tvAddress2,tvOfficeNumber,tvWebsiteUrl,tvUserEmail;
    GlobalApplication globalApplication;
    private APIInterface apiInterface;
    private DoctorDetailsBean doctorDetails;
    ProgressDialog progressDialog;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @BindView(R.id.ll_office_number_container)
    LinearLayout llOfficeNumberContainer;

    @BindView(R.id.ll_website_url_container)
    LinearLayout llWebsiteUrlContainer;



    public DoctorMyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this,rootView);
        globalApplication = (GlobalApplication)mContext.getApplicationContext();
        apiInterface = APIClient.getClient(mContext).create(APIInterface.class);
        doctorDetails = ((MyProfileActivity)mContext).doctorDetails;
        initUI();


        return rootView;
    }

    private void initUI() {
        TextView tvSettings = (TextView) rootView.findViewById(R.id.toolbar_tv_settings);
        TextView tvEdit = (TextView) rootView.findViewById(R.id.toolbar_tv_edit);

        tvSettings.setOnClickListener(this);
        tvEdit.setOnClickListener(this);

        tvMondayTime = (TextView)rootView.findViewById(R.id.tv_monday_time);
        tvTuesdayTime = (TextView)rootView.findViewById(R.id.tv_tuesday_time);
        tvWednesdayTime = (TextView)rootView.findViewById(R.id.tv_wednesday_time);
        tvThursdayTime = (TextView)rootView.findViewById(R.id.tv_thursday_time);
        tvFridayTime = (TextView)rootView.findViewById(R.id.tv_friday_time);
        tvSaturdayTime = (TextView)rootView.findViewById(R.id.tv_saturday_time);
        tvSundayTime = (TextView)rootView.findViewById(R.id.tv_sunday_time);

        tvDoctorName = (TextView)rootView.findViewById(R.id.tv_doctor_name);
        tvDoctorSepciality = (TextView)rootView.findViewById(R.id.tv_doctor_speciality);
        tvAddress1 = (TextView)rootView.findViewById(R.id.tv_address_1);
        tvAddress2 = (TextView)rootView.findViewById(R.id.tv_address_2);
        tvOfficeNumber = (TextView)rootView.findViewById(R.id.tv_office_number);
        tvWebsiteUrl = (TextView)rootView.findViewById(R.id.tv_website_url);
        tvUserEmail = (TextView)rootView.findViewById(R.id.tv_user_email);



    }

    public void goToSettings()
    {
        ((MyProfileActivity)mContext).goToSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.toolbar_tv_settings:
                goToSettings();
                break;
            case R.id.toolbar_tv_edit:
                goToEdit();
                break;
        }
    }

    private void goToEdit() {
        ((MyProfileActivity)mContext).goToEdit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void getDoctorDetails() {
        progressDialog = LoadingDialog.show(mContext,"Fetching Datas");
        final Call<DoctorDetailsBean> doctorDetailsBeanCall = apiInterface.getDoctorDetails(globalApplication.getUserId());
        doctorDetailsBeanCall.enqueue(new Callback<DoctorDetailsBean>() {
            @Override
            public void onResponse(Call<DoctorDetailsBean> call, Response<DoctorDetailsBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    doctorDetails = response.body();
                    ((MyProfileActivity)mContext).doctorDetails = doctorDetails;
                    displayDatas();
                }
                else
                {
                    Utils.showToast(mContext,"Unable to get data");

                }

            }

            @Override
            public void onFailure(Call<DoctorDetailsBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(mContext,"Unable to get data "+t.getLocalizedMessage());

            }
        });

    }

    @Override
    public void onResume() {
       // if(((MyProfileActivity)mContext).doctorDetails==null)
            getDoctorDetails();
//        else
//            displayDatas();
        super.onResume();
    }

    private void displayDatas() {
        tvDoctorName.setText(doctorDetails.getData().getName());
        tvDoctorSepciality.setText(doctorDetails.getData().getSpecialty().getName());
        tvAddress1.setText(doctorDetails.getData().getAddress().getStreet1());
        String address2 = doctorDetails.getData().getAddress().getCity();
        address2 += " "+doctorDetails.getData().getAddress().getState();
        address2 += " "+doctorDetails.getData().getAddress().getZip();
        address2 += " "+doctorDetails.getData().getAddress().getCountry();
        tvAddress2.setText(address2);
        tvOfficeNumber.setText(doctorDetails.getData().getPhone().getOffice());
        tvWebsiteUrl.setText(doctorDetails.getData().getWebsite());
        if(doctorDetails.getData().getPhone().getOffice().equals(""))
            llOfficeNumberContainer.setVisibility(View.GONE);
        if(doctorDetails.getData().getWebsite().equals(""))
            llWebsiteUrlContainer.setVisibility(View.GONE);
        tvUserEmail.setText(doctorDetails.getData().getEmail());
        String mProfileImagePath = doctorDetails.getData().getAvatar();
        Picasso.with(getActivity()).load(mProfileImagePath).placeholder(R.drawable.gpc_placeholder_physician_full)
                .into(ivAvatar);

        updateOfficeHours();

    }

    private void updateOfficeHours() {
       Officehours officeHours = doctorDetails.getData().getOfficehours();
        if(officeHours.getMonday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getMonday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getMonday().getEnd());
            tvMondayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvMondayTime.setText(getString(R.string.unavailable));
        }

        if(officeHours.getTuesday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getTuesday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getTuesday().getEnd());
            tvTuesdayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvTuesdayTime.setText(getString(R.string.unavailable));
        }

        if(officeHours.getWednesday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getWednesday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getWednesday().getEnd());
            tvWednesdayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvWednesdayTime.setText(getString(R.string.unavailable));
        }

        if(officeHours.getThursday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getThursday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getThursday().getEnd());
            tvThursdayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvThursdayTime.setText(getString(R.string.unavailable));
        }

        if(officeHours.getFriday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getFriday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getFriday().getEnd());
            tvFridayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvFridayTime.setText(getString(R.string.unavailable));
        }

        if(officeHours.getSaturday()!=null)
        {
            String startTime = Utils.convert24HourTo12Hour(officeHours.getSaturday().getStart());
            String endTime  =Utils.convert24HourTo12Hour(officeHours.getSaturday().getEnd());
            tvSaturdayTime.setText(startTime+" - "+endTime);
        }
        else
        {
            tvSaturdayTime.setText(getString(R.string.unavailable));
        }

//        if(officeHours.getSunday()!=null)
//        {
//            String startTime = Utils.convert24HourTo12Hour(officeHours.getSunday().getStart());
//            String endTime  =Utils.convert24HourTo12Hour(officeHours.getSunday().getEnd());
//            tvSundayTime.setText(startTime+" - "+endTime);
//        }
//        else
//        {
//            tvSundayTime.setText(getString(R.string.unavailable));
//        }




    }

}
