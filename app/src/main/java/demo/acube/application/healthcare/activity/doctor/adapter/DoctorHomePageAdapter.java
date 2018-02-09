package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.HomeAllTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.HomeCallTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.HomeChatTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.HomeVideoTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs.HomeVisitTaskFragment;

/**
 * Created by Anns on 05/05/17.
 */

public class DoctorHomePageAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private Context context;
    public DoctorHomePageAdapter(FragmentManager fm,int count,Context mContext) {
        super(fm);
        this.context = mContext;
        this.tabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new HomeAllTaskFragment();
            case 1:
                return new HomeCallTaskFragment();
            case 2:
                return new HomeChatTaskFragment();
            case 3:
                return new HomeVideoTaskFragment();
            case 4:
                return new HomeVisitTaskFragment();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
