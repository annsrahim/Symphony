package demo.acube.application.healthcare.activity.patient.acitivity.scheduleAppointment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.Utils;

/**
 * Created by Anns on 07/06/17.
 */

public class UnitSelectDialogFragment extends DialogFragment implements View.OnClickListener {
    String[] mDosageInfo = new String[3];
    String[] unit_array;
    static UnitSelectDialogFragment newInstance()
    {
        UnitSelectDialogFragment unitSelectDialogFragment = new UnitSelectDialogFragment();
        return unitSelectDialogFragment;
    }
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_unit_select, container, false);
        mDosageInfo = ((MedicationRemainderActivity)getActivity()).mDosageInfo;
        initToolbar();
        initUI();
        if(mDosageInfo[0]!=null)
            assignCheckMark();

        return rootView;
    }

    private void assignCheckMark()
    {
        ImageView ivUnit1 = (ImageView)rootView.findViewById(R.id.iv_unit_item_1);
        ImageView ivUnit2 = (ImageView)rootView.findViewById(R.id.iv_unit_item_2);
        ImageView ivUnit3 = (ImageView)rootView.findViewById(R.id.iv_unit_item_3);
        ImageView ivUnit4 = (ImageView)rootView.findViewById(R.id.iv_unit_item_4);
        ImageView ivUnit5 = (ImageView)rootView.findViewById(R.id.iv_unit_item_5);
        ImageView ivUnit6 = (ImageView)rootView.findViewById(R.id.iv_unit_item_6);
        ImageView ivUnit7 = (ImageView)rootView.findViewById(R.id.iv_unit_item_7);
        ImageView ivUnit8 = (ImageView)rootView.findViewById(R.id.iv_unit_item_8);
        ImageView ivUnit9 = (ImageView)rootView.findViewById(R.id.iv_unit_item_9);
        ImageView ivUnit10 = (ImageView)rootView.findViewById(R.id.iv_unit_item_10);
        ImageView ivUnit11 = (ImageView)rootView.findViewById(R.id.iv_unit_item_11);
        ImageView ivUnit12 = (ImageView)rootView.findViewById(R.id.iv_unit_item_12);
        ImageView ivUnit13 = (ImageView)rootView.findViewById(R.id.iv_unit_item_13);

        ivUnit1.setVisibility(View.INVISIBLE);
        ivUnit2.setVisibility(View.INVISIBLE);
        ivUnit3.setVisibility(View.INVISIBLE);
        ivUnit4.setVisibility(View.INVISIBLE);
        ivUnit5.setVisibility(View.INVISIBLE);
        ivUnit6.setVisibility(View.INVISIBLE);
        ivUnit7.setVisibility(View.INVISIBLE);
        ivUnit8.setVisibility(View.INVISIBLE);
        ivUnit9.setVisibility(View.INVISIBLE);
        ivUnit10.setVisibility(View.INVISIBLE);
        ivUnit11.setVisibility(View.INVISIBLE);
        ivUnit12.setVisibility(View.INVISIBLE);
        ivUnit13.setVisibility(View.INVISIBLE);

        int dosageSlot = Integer.parseInt(mDosageInfo[0]);
        switch (dosageSlot)
        {
            case 1:
                ivUnit1.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivUnit2.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivUnit3.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivUnit4.setVisibility(View.VISIBLE);
                break;
            case 5:
                ivUnit5.setVisibility(View.VISIBLE);
                break;
            case 6:
                ivUnit6.setVisibility(View.VISIBLE);
                break;
            case 7:
                ivUnit7.setVisibility(View.VISIBLE);
                break;
            case 8:
                ivUnit8.setVisibility(View.VISIBLE);
                break;
            case 9:
                ivUnit9.setVisibility(View.VISIBLE);
                break;
            case 10:
                ivUnit10.setVisibility(View.VISIBLE);
                break;
            case 11:
                ivUnit11.setVisibility(View.VISIBLE);
                break;
            case 12:
                ivUnit12.setVisibility(View.VISIBLE);
                break;
            case 13:
                ivUnit13.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void initUI() {
        unit_array = getResources().getStringArray(R.array.unit_array);
        RelativeLayout rlUnit1 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_1);
        RelativeLayout rlUnit2 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_2);
        RelativeLayout rlUnit3 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_3);
        RelativeLayout rlUnit4 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_4);
        RelativeLayout rlUnit5 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_5);
        RelativeLayout rlUnit6 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_6);
        RelativeLayout rlUnit7 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_7);
        RelativeLayout rlUnit8 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_8);
        RelativeLayout rlUnit9 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_9);
        RelativeLayout rlUnit10 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_10);
        RelativeLayout rlUnit11 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_11);
        RelativeLayout rlUnit12 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_12);
        RelativeLayout rlUnit13 = (RelativeLayout)rootView.findViewById(R.id.rl_unit_item_13);



        rlUnit1.setOnClickListener(this);
        rlUnit2.setOnClickListener(this);
        rlUnit3.setOnClickListener(this);
        rlUnit4.setOnClickListener(this);
        rlUnit5.setOnClickListener(this);
        rlUnit6.setOnClickListener(this);
        rlUnit7.setOnClickListener(this);
        rlUnit8.setOnClickListener(this);
        rlUnit9.setOnClickListener(this);
        rlUnit10.setOnClickListener(this);
        rlUnit11.setOnClickListener(this);
        rlUnit12.setOnClickListener(this);
        rlUnit13.setOnClickListener(this);



    }
    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.select_unit));
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.toolbar_red, null));
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);

        tvToolbarBack.setTextColor(ResourcesCompat.getColor(getResources(),R.color.White, null));
        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),R.color.White, null));

        Utils.setFontForTextView(getActivity(),tvToolbarTitle);
        Utils.setFontForTextView(getActivity(),tvToolbarBack);

        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_unit_item_1:
                mDosageInfo[0] = "1";
                mDosageInfo[1] = unit_array[1];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_2:
                mDosageInfo[0] = "2";
                mDosageInfo[1] = unit_array[2];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_3:
                mDosageInfo[0] = "3";
                mDosageInfo[1] = unit_array[3];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_4:
                mDosageInfo[0] = "4";
                mDosageInfo[1] = unit_array[4];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_5:
                mDosageInfo[0] = "5";
                mDosageInfo[1] = unit_array[5];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_6:
                mDosageInfo[0] = "6";
                mDosageInfo[1] = unit_array[6];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_7:
                mDosageInfo[0] = "7";
                mDosageInfo[1] = unit_array[7];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_8:
                mDosageInfo[0] = "8";
                mDosageInfo[1] = unit_array[8];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_9:
                mDosageInfo[0] = "9";
                mDosageInfo[1] = unit_array[9];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_10:
                mDosageInfo[0] = "10";
                mDosageInfo[1] = unit_array[10];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_11:
                mDosageInfo[0] = "11";
                mDosageInfo[1] = unit_array[11];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_12:
                mDosageInfo[0] = "12";
                mDosageInfo[1] = unit_array[12];
                updateDosageInParent();
                break;
            case R.id.rl_unit_item_13:
                mDosageInfo[0] = "13";
                mDosageInfo[1] = unit_array[13];
                updateDosageInParent();
                break;

        }

    }

    private void updateDosageInParent()
    {
        ((MedicationRemainderActivity)getActivity()).mDosageInfo = mDosageInfo;
        DosageSelectorDialogFragment parentFrag = ((DosageSelectorDialogFragment)this.getParentFragment());
        parentFrag.updateDosageUnitLabel();
        Log.d("TAG",mDosageInfo[1]);
        dismiss();
    }
}
