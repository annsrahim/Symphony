package demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.acube.application.healthcare.R;

import demo.acube.application.healthcare.helper.utilities.Utils;

/**
 * Created by Anns on 13/07/17.
 */

public class DoctorDosageSelectDialogFragment extends DialogFragment {

    static DoctorDosageSelectDialogFragment newInsatnce()
    {
        return new DoctorDosageSelectDialogFragment();
    }

    private View rootView;
    private TextView tvUnit;
    private ImageView ivUnitSelect;
    private EditText edDosageValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dosage_select, container, false);
        initToolbar();
        initUI();
        updateDosageUnitLabel();
        return rootView;
    }

    private void initUI()
    {
        tvUnit = (TextView)rootView.findViewById(R.id.tv_dosage_unit);
        ivUnitSelect = (ImageView) rootView.findViewById(R.id.iv_dosage_none);
        edDosageValue = (EditText)rootView.findViewById(R.id.ed_dosage);
        RelativeLayout rlSelectUnitContainer = (RelativeLayout)rootView.findViewById(R.id.rl_select_unit_container);
        rlSelectUnitContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUnitDialog();
            }
        });
    }
    public void selectUnitDialog()
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DoctorUnitSelectDialogFragment unitSelectDialogFragment = DoctorUnitSelectDialogFragment.newInstance();
        unitSelectDialogFragment.show(ft,"docUnitSelectDialogFragment");
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.provide_dosage));
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        TextView tvToolbarSave = (TextView)toolbar.findViewById(R.id.toolbar_tv_save);
        tvToolbarBack.setTextColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_blue, null));
        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_blue, null));
        tvToolbarSave.setTextColor(ResourcesCompat.getColor(getResources(),R.color.toolbar_blue, null));
        Utils.setFontForTextView(getActivity(),tvToolbarTitle);
        Utils.setFontForTextView(getActivity(),tvToolbarBack);
        Utils.setFontForTextView(getActivity(),tvToolbarSave);
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvToolbarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDosageValues();
            }
        });


    }

    private void saveDosageValues()
    {
        if(isValidSave())
        {
            ((DoctorMedicationReminderActivity)getActivity()).mDosageInfo[2] = edDosageValue.getText().toString();
            ((DoctorMedicationReminderActivity)getActivity()).updateDosageValues();
            dismiss();
        }

    }

    private boolean isValidSave()
    {

        if(((DoctorMedicationReminderActivity)getActivity()).mDosageInfo[1]==null && edDosageValue.getText().toString().equals(""))
        {
            Utils.showAlertDialog(getActivity(),"You forgot to select a unit for dosage \n you provided an invalid dosage");
            return false;
        }
        else if(((DoctorMedicationReminderActivity)getActivity()).mDosageInfo[1]==null)
        {
            Utils.showAlertDialog(getActivity(),"You forgot to select a unit for dosage");
            return false;
        }
        else if(edDosageValue.getText().toString().equals(""))
        {
            Utils.showAlertDialog(getActivity(),"you provided an invalid dosage");
            return false;
        }
        else
        {
            return true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);
        setHasOptionsMenu(true);
    }

    public void updateDosageUnitLabel()
    {
        if(((DoctorMedicationReminderActivity)getActivity()).mDosageInfo[1]!=null)
        {
            String dosageInfo = ((DoctorMedicationReminderActivity)getActivity()).mDosageInfo[1];
            tvUnit.setText(dosageInfo);
            tvUnit.setVisibility(View.VISIBLE);
            ivUnitSelect.setVisibility(View.GONE);
        }
        else
        {
            ivUnitSelect.setVisibility(View.VISIBLE);
            tvUnit.setVisibility(View.GONE);
        }



    }



}
