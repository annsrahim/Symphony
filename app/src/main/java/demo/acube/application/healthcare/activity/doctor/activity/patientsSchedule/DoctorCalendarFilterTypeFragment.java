package demo.acube.application.healthcare.activity.doctor.activity.patientsSchedule;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.Utils;


/**
 * Created by Anns on 29/06/17.
 */

public class DoctorCalendarFilterTypeFragment extends DialogFragment implements View.OnClickListener
{
    public static DoctorCalendarFilterTypeFragment newInsatnce()
    {
        return new DoctorCalendarFilterTypeFragment();
    }
    private View rootView;
    private ImageView ivMedicalCheck,ivBiometricCheck,ivCallCheck,ivVirtualVisitCheck,ivPhysicalVisitCheck;
    private RelativeLayout rlMedical,rlBiometric,rlCall,rlVirtualVisit,rlPhysicalVisit;
    private Boolean[] enabledTypeArray;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar_filter_type, container, false);
        initToolbar();
        initUI();
        enabledTypeArray = ((PatinetScheduleActivity)getActivity()).enabledTypesArray;
        checkMarkAssign();

        return rootView;
    }

    private void checkMarkAssign()
    {
        if(enabledTypeArray[0])
            ivMedicalCheck.setVisibility(View.VISIBLE);
        else
            ivMedicalCheck.setVisibility(View.GONE);
        if(enabledTypeArray[1])
            ivBiometricCheck.setVisibility(View.VISIBLE);
        else
            ivBiometricCheck.setVisibility(View.GONE);

        if(enabledTypeArray[2])
            ivCallCheck.setVisibility(View.VISIBLE);
        else
            ivCallCheck.setVisibility(View.GONE);

        if(enabledTypeArray[3])
            ivVirtualVisitCheck.setVisibility(View.VISIBLE);
        else
            ivVirtualVisitCheck.setVisibility(View.GONE);

        if(enabledTypeArray[4])
            ivPhysicalVisitCheck.setVisibility(View.VISIBLE);
        else
            ivPhysicalVisitCheck.setVisibility(View.GONE);

    }

    private void initUI()
    {
        ivMedicalCheck = (ImageView)rootView.findViewById(R.id.iv_medication_check);
        ivBiometricCheck = (ImageView)rootView.findViewById(R.id.iv_biometric_check);
        ivCallCheck = (ImageView)rootView.findViewById(R.id.iv_call_check);
        ivVirtualVisitCheck = (ImageView)rootView.findViewById(R.id.iv_virtual_visit_check);
        ivPhysicalVisitCheck = (ImageView)rootView.findViewById(R.id.iv_physical_visit_check);

        rlMedical = (RelativeLayout) rootView.findViewById(R.id.rl_medical);
        rlBiometric = (RelativeLayout) rootView.findViewById(R.id.rl_biometric);
        rlCall = (RelativeLayout) rootView.findViewById(R.id.rl_call);
        rlVirtualVisit = (RelativeLayout) rootView.findViewById(R.id.rl_virtual_visit);
        rlPhysicalVisit = (RelativeLayout) rootView.findViewById(R.id.rl_physical_visit);

        rlMedical.setOnClickListener(this);
        rlBiometric.setOnClickListener(this);
        rlCall.setOnClickListener(this);
        rlVirtualVisit.setOnClickListener(this);
        rlPhysicalVisit.setOnClickListener(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);
        setHasOptionsMenu(true);
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));

        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        TextView tvToolbarDone = (TextView)toolbar.findViewById(R.id.toolbar_tv_done);
        Utils.setFontForTextView(getActivity(),tvToolbarTitle);
        Utils.setFontForTextView(getActivity(),tvToolbarBack);
        Utils.setFontForTextView(getActivity(),tvToolbarDone);
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvToolbarDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

    }

    private void done()
    {
        ((PatinetScheduleActivity)getActivity()).enabledTypesArray = enabledTypeArray;
        ((PatinetScheduleActivity)getActivity()).updateFilterInCalendars();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_medical:
                    if(enabledTypeArray[0])
                    {
                        enabledTypeArray[0] = false;
                        ivMedicalCheck.setVisibility(View.GONE);
                    }
                    else
                    {
                        enabledTypeArray[0] = true;
                        ivMedicalCheck.setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.rl_biometric:
                    if(enabledTypeArray[1])
                    {
                        enabledTypeArray[1] = false;
                        ivBiometricCheck.setVisibility(View.GONE);
                    }
                    else
                    {
                        enabledTypeArray[1] = true;
                        ivBiometricCheck.setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.rl_call:
                    if(enabledTypeArray[2])
                    {
                        enabledTypeArray[2] = false;
                        ivCallCheck.setVisibility(View.GONE);
                    }
                    else
                    {
                        enabledTypeArray[2] = true;
                        ivCallCheck.setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.rl_virtual_visit:
                    if(enabledTypeArray[3])
                    {
                        enabledTypeArray[3] = false;
                        ivVirtualVisitCheck.setVisibility(View.GONE);
                    }
                    else
                    {
                        enabledTypeArray[3] = true;
                        ivVirtualVisitCheck.setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.rl_physical_visit:
                    if(enabledTypeArray[4])
                    {
                        enabledTypeArray[4] = false;
                        ivPhysicalVisitCheck.setVisibility(View.GONE);
                    }
                    else
                    {
                        enabledTypeArray[4] = true;
                        ivPhysicalVisitCheck.setVisibility(View.VISIBLE);
                    }
                break;
        }
    }
}
