package demo.acube.application.healthcare.activity.doctor.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs.ArchiveAllTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs.ArchiveCallTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs.ArchiveChatTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs.ArchiveVideoTaskFragment;
import demo.acube.application.healthcare.activity.doctor.activity.archive.archiveFragmentTabs.ArchiveVisitTaskFragment;

/**
 * Created by Anns on 05/05/17.
 */

public class ArchivePageAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private Context context;
    public ArchivePageAdapter(FragmentManager fm, int count, Context mContext) {
        super(fm);
        this.context = mContext;
        this.tabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ArchiveAllTaskFragment();
            case 1:
                return new ArchiveCallTaskFragment();
            case 2:
                return new ArchiveChatTaskFragment();
            case 3:
                return new ArchiveVideoTaskFragment();
            case 4:
                return new ArchiveVisitTaskFragment();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
