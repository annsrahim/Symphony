package demo.acube.application.healthcare.activity.doctor.activity.dashboard.HomeFragmentTabs;

import android.view.View;

/**
 * Created by Anns on 11/07/17.
 */

public interface ITaskListClickListener {
    public void onItemClick(View view , int position);
    public void relaodList();
}
