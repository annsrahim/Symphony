package demo.acube.application.healthcare.activity.doctor.activity.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class DoctorNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_notes);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
}
