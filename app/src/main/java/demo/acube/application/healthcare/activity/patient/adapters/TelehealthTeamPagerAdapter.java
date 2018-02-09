package demo.acube.application.healthcare.activity.patient.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.AllDoctorsFragments;
import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.ApprovedDoctorsFragment;
import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.ExploreDoctorsFragment;
import demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam.PendingDoctorsFragment;

/**
 * Created by Anns on 18/07/17.
 */

public class TelehealthTeamPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private Context context;
    public TelehealthTeamPagerAdapter(FragmentManager fm, int count, Context mContext) {
        super(fm);
        this.context = mContext;
        this.tabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new AllDoctorsFragments();
            case 1:
                return new ApprovedDoctorsFragment();
            case 2:
                return new PendingDoctorsFragment();
            case 3:
                return new ExploreDoctorsFragment();

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
