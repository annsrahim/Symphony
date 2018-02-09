package demo.acube.application.healthcare.activity.doctor.activity.calendarSchedule;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.acube.application.healthcare.R;

/**
 * Created by Anns on 17/07/17.
 */

public class CircularMenuFragemnt extends DialogFragment {

    public static CircularMenuFragemnt newInstance()
    {
        return new CircularMenuFragemnt();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_home_screen_items, container, false);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);
        setHasOptionsMenu(false);
    }
}
