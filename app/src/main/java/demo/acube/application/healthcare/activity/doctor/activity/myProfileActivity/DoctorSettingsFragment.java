package demo.acube.application.healthcare.activity.doctor.activity.myProfileActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.registrationUser.LoginAcitivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorSettingsFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.switch_do_not_disturb) Switch switchDoNotDisturb;
    @BindView(R.id.tv_account_email) TextView tvAccountEmail;
    @BindView(R.id.rl_account_container) RelativeLayout rlAccountContainer;
    @BindView(R.id.rl_terms_container) RelativeLayout rlTermsContainer;
    @BindView(R.id.rl_logout_container) RelativeLayout rlLogoutContainer;

    private View rootView;
    private Context mContext;
    public DoctorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_doctor_settings, container, false);
        ButterKnife.bind(this,rootView);
        initUI();
        return rootView;
    }

    private void initUI() {
        TextView tvMyProfile = (TextView) rootView.findViewById(R.id.toolbar_tv_myprofile);
        tvMyProfile.setOnClickListener(this);

        rlAccountContainer.setOnClickListener(this);
        rlLogoutContainer.setOnClickListener(this);
        rlTermsContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.toolbar_tv_myprofile:
                goToMyProfile();
                break;
            case R.id.rl_account_container:
                 onAccountClick();
                break;
            case R.id.rl_terms_container:
                onTermsClick();
                break;
            case R.id.rl_logout_container:
                onLogoutClick();
                break;
        }

    }

    private void onLogoutClick() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm")
                .setMessage(R.string.logout_confirm_msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        logoutUser();
                    }})
                .setNegativeButton(R.string.Nevermind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void logoutUser()
    {
        GlobalApplication globalApplication = (GlobalApplication)getActivity().getApplicationContext();
        globalApplication.setUserLoggedIn(false);
        startActivity(new Intent(getActivity(), LoginAcitivity.class));
    }

    private void onTermsClick() {
        String url = "http://ctrlhealth.com/privacy-policy/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void onAccountClick() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.requires_action)
                .setMessage(R.string.requires_action_msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.support_email) });

                        startActivity(Intent.createChooser(intent, ""));
                    }})
                .setNegativeButton(R.string.Nevermind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void goToMyProfile() {
        ((MyProfileActivity)mContext).goToMyProfile();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
