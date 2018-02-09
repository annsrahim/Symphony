package demo.acube.application.healthcare.activity.registrationUser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.service.CustomExceptionHandler;

public class AcknowledgementActivity extends AppCompatActivity {
    Button btnIAgree;
    TextView tvUserName;
    GlobalApplication globalApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgement);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        btnIAgree = (Button)findViewById(R.id.btn_iAgree);
        tvUserName = (TextView)findViewById(R.id.tvUserName);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        tvUserName.setText(globalApplication.getUserName());
        btnIAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AcknowledgementActivity.this,UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
