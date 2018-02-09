package demo.acube.application.healthcare.activity.doctor.activity.doctorAppointmentSet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.acube.application.healthcare.service.CustomExceptionHandler;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.R;
import demo.acube.application.healthcare.activity.doctor.activity.patientList.PatientListActivity;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;
import demo.acube.application.healthcare.helper.utilities.LoadingDialog;
import demo.acube.application.healthcare.helper.utilities.Utils;
import demo.acube.application.healthcare.helper.utilities.ValueUtils;
import demo.acube.application.healthcare.model.accessToken.Success;
import demo.acube.application.healthcare.model.medicationImageUploadBean.MedicationImageUploadBean;
import demo.acube.application.healthcare.model.userDetails.UserDetailsBean;
import demo.acube.application.healthcare.service.APIClient;
import demo.acube.application.healthcare.service.APIError;
import demo.acube.application.healthcare.service.APIInterface;

public class DoctorMedicationReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,NumberPicker.OnValueChangeListener{

    private String mSelectedPatientName = "";
    private int mSelectedPatientId = 0;
    private TextView tvPatientName;


    private String userChoosenTask = "";
    private ImageView ivAddImage;
    private DatePickerDialog datePicker;
    private int startYear,startMonth,startDay,endYear,endMonth,endDay;
    private TextView tvStartDate,tvEndDate;
    private String selectedDateType = "";
    private TextView tvTimesPerDay,tvAlertTime;
    private LinearLayout llTimeSlotContainer;
    private int totalTimesPerDay = 1;
    private TextView[] tvTimeSlotTimeArray;
    TimePickerDialog timePickerDialog;
    private int mSelectedTimeSlot = 1;
    private String mSelectedTime;
    public String[] mDosageInfo = new String[3];//0->dosage slot,1->dosage_unit,2->dosage_quantity
    public int mAlertNotifyType = 1;//0-At eventtime,1- 5 minutes before,2- 10 minutes before
    private TextView tvDosageValue;
    private ImageView ivImageDelete;
    ArrayList<Bitmap> bitmapDosagePicArray= new ArrayList<Bitmap>();
    List<Integer> dosageImgIdList = new ArrayList<Integer>();
    private APIInterface apiInterface;
    private ProgressDialog progressDialog;
    private EditText edMedicaName;
    private String mSelectedSartDate,mSelectedEnddate;
    private Boolean isValidDate=true;
    private GlobalApplication globalApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_medication_reminder);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        Utils.disableKeyboardPopup(this);
        globalApplication = (GlobalApplication)this.getApplicationContext();
        initToolbar();
        initUi();
        assignDateValues();
        addTimeSlots();

    }

    private void initUi() {
        tvPatientName = (TextView)findViewById(R.id.tv_patient_name);
        ivAddImage  = (ImageView)findViewById(R.id.iv_add_photo);
        tvStartDate = (TextView)findViewById(R.id.tv_start_date);
        tvEndDate = (TextView)findViewById(R.id.tv_end_date);
        tvTimesPerDay = (TextView)findViewById(R.id.tv_times_per_day);
        llTimeSlotContainer = (LinearLayout)findViewById(R.id.ll_time_slot_container);
        tvAlertTime = (TextView)findViewById(R.id.tv_alert_time);
        tvDosageValue = (TextView)findViewById(R.id.tv_dosage_value);
        ivImageDelete = (ImageView)findViewById(R.id.iv_image_close);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        edMedicaName = (EditText)findViewById(R.id.ed_medic_name);

    }

    private void updatePatientName() {
        mSelectedPatientId = globalApplication.getPatSelectedCallerId();
        mSelectedPatientName = globalApplication.getPatSelectedCallerName();
        tvPatientName.setText(mSelectedPatientName);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.White, null));
        setSupportActionBar(toolbar);
        TextView tvToolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_tv_title);
        TextView tvToolbarBack = (TextView)toolbar.findViewById(R.id.toolbar_tv_back);
        TextView tvToolbarDone = (TextView)toolbar.findViewById(R.id.toolbar_tv_done);
        Utils.setFontForTextView(this,tvToolbarTitle);
        Utils.setFontForTextView(this,tvToolbarBack);
        Utils.setFontForTextView(this,tvToolbarDone);
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvToolbarDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeicationRemainder();
            }
        });
    }


    private void assignDateValues()
    {
        String isFrom = "";
        Intent intent = getIntent();
        if(intent!=null)
            isFrom = intent.getStringExtra(ValueUtils.isFrom);
        if(isFrom!=null && isFrom.equals(ValueUtils.MySchedules))
        {
            CalendarDay calendarDay = globalApplication.getScheduledDate();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(calendarDay.getDate());

            startYear = startCal.get(Calendar.YEAR);
            startDay = startCal.get(Calendar.DAY_OF_MONTH);
            startMonth = startCal.get(Calendar.MONTH);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(calendarDay.getDate());
            endCal.add(Calendar.DATE,1);
            endDay = endCal.get(Calendar.DAY_OF_MONTH);
            endYear = endCal.get(Calendar.YEAR);
            endMonth = endCal.get(Calendar.MONTH);


        }
        else
        {
            startYear = Calendar.getInstance().get(Calendar.YEAR);
            startMonth = Calendar.getInstance().get(Calendar.MONTH);
            startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            Calendar endCal = Calendar.getInstance();
            endCal.add(Calendar.DATE,1);
            endDay = endCal.get(Calendar.DAY_OF_MONTH);
            endYear = endCal.get(Calendar.YEAR);
            endMonth = endCal.get(Calendar.MONTH);
        }

        String convertedStartDate = convertDateToUserFormat(startYear,startMonth,startDay);
        String convertedEndDate = convertDateToUserFormat(endYear,endMonth,endDay);

        tvStartDate.setText(convertedStartDate);
        tvEndDate.setText(convertedEndDate);
        mSelectedSartDate = startMonth+"-"+startDay+"-"+startYear;
        mSelectedEnddate = endMonth+"-"+endDay+"-"+endYear;
    }
    public String convertDateToUserFormat(int year,int month,int dayOfMonth)
    {
        String rawDate = year+"-"+month+"-"+dayOfMonth;
        Log.d("TTYT",rawDate);
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try
        {
            date = form.parse(rawDate);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd, yyyy");
        String newDateStr = postFormater.format(date);
        return newDateStr;
    }
    private void addTimeSlots()
    {
        tvTimeSlotTimeArray = new TextView[totalTimesPerDay];
        RelativeLayout[] arrayRlTimeSlotItemContainer = new RelativeLayout[totalTimesPerDay];
        llTimeSlotContainer.removeAllViews();
        for(int i=0;i<totalTimesPerDay;i++)
        {
            final int currentValue = i;
            View childView = getLayoutInflater().inflate(R.layout.layout_time_slot,null);
            ImageView ivTimeSlotIcon = (ImageView) childView.findViewById(R.id.iv_time_slot_icon);
            ivTimeSlotIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_clock_physician));
            TextView tvTimeSlotLabel = (TextView)childView.findViewById(R.id.tvTimeSlotLabel);
            tvTimeSlotTimeArray[i] = (TextView)childView.findViewById(R.id.tv_time_slot_time);
            arrayRlTimeSlotItemContainer[i] = (RelativeLayout)childView.findViewById(R.id.rlTimeSlotItemContainer);
            arrayRlTimeSlotItemContainer[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSelectedTimeSlot = currentValue;
                    showTimePickerDialog();
                }
            });
            tvTimeSlotLabel.setText("Time "+(i+1));
            llTimeSlotContainer.addView(childView);
        }
    }

    private void showTimePickerDialog()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mSelectedTime = hourOfDay+":"+minute;
                updateTimeValueInTimeSlot();
            }
        },hour,minute,false);
        timePickerDialog.setTitle("Time "+mSelectedTimeSlot);
        timePickerDialog.show();
    }
    private void updateTimeValueInTimeSlot()
    {
        tvTimeSlotTimeArray[mSelectedTimeSlot].setText(Utils.convert24HourTo12Hour(mSelectedTime));
    }
    public void doShowPatientList(View view) {
        Intent intent  = new Intent(this, PatientListActivity.class);
        startActivity(intent);
    }
    public void updateDosageValues()
    {
        tvDosageValue.setText(mDosageInfo[2]+" "+mDosageInfo[1]);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updatePatientName();
    }

    public void showDosageSelectDialog(View view)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DoctorDosageSelectDialogFragment alDosFrag = DoctorDosageSelectDialogFragment.newInsatnce();
        alDosFrag.show(ft,"docDosageDialog");
    }

    public void selectImage(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorMedicationReminderActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utils.checkPermission(DoctorMedicationReminderActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Config.CAMERA_REQUEST);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),Config.GALLERY_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Config.GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == Config.CAMERA_REQUEST)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmapDosagePicArray.add(bm);
        addImageToView();
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        bitmapDosagePicArray.add(thumbnail);
        addImageToView();

    }
    private void addImageToView()
    {
        uploadImageToServer();

    }
    private File savebitmap(String filename) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, filename + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            Bitmap bitmap = bitmapDosagePicArray.get(bitmapDosagePicArray.size()-1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;

    }
    private void uploadImageToServer()
    {
        progressDialog = LoadingDialog.show(this,"Uploading Image...");
        File file = savebitmap("temp");
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<MedicationImageUploadBean> medicalImageUploadCall = apiInterface.doMedicalImageUpload(body);
        medicalImageUploadCall.enqueue(new Callback<MedicationImageUploadBean>() {
            @Override
            public void onResponse(Call<MedicationImageUploadBean> call, Response<MedicationImageUploadBean> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    dosageImgIdList.add(response.body().getData().getImageId());
                    ivAddImage.setImageBitmap(bitmapDosagePicArray.get(bitmapDosagePicArray.size()-1));
                    ivImageDelete.setVisibility(View.VISIBLE);
                }
                else if(response.code()==400)
                {
                    Gson gson = new GsonBuilder().create();
                    APIError mError=new APIError();
                    try {
                        mError= gson.fromJson(response.errorBody().string(),APIError.class);
                        Utils.showToast(DoctorMedicationReminderActivity.this,mError.getError());
                    } catch (IOException e) {
                        // handle failure to read error
                    }
                }
                else
                {

                    Utils.showToast(DoctorMedicationReminderActivity.this,"Image upload failed please try again");
                    doDeleteImage();
                }
            }

            @Override
            public void onFailure(Call<MedicationImageUploadBean> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(DoctorMedicationReminderActivity.this,"Image upload failed "+t.getLocalizedMessage()+" please try again ");
                doDeleteImage();
            }
        });
    }
    public void deleteCurrentImage(View view)
    {
        deleteImageFromServer();
        // doDeleteImage();

    }
    private void deleteImageFromServer() {
        progressDialog = LoadingDialog.show(this,"Removing Image...");
        int imgId = dosageImgIdList.get(dosageImgIdList.size()-1);
        Call<Success>  deleteMedicalImageCall = apiInterface.doDeleteImageId(imgId);
        deleteMedicalImageCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    dosageImgIdList.remove(dosageImgIdList.size()-1);
                    doDeleteImage();
                }
                else
                {
                    Utils.showToast(DoctorMedicationReminderActivity.this,"Unable to remove image please try again");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(DoctorMedicationReminderActivity.this,"Unable to remove image "+t.getLocalizedMessage());
            }
        });
    }
    private void doDeleteImage()
    {
        if(bitmapDosagePicArray.size()>0)
        {
            bitmapDosagePicArray.remove(bitmapDosagePicArray.size()-1);
            if(bitmapDosagePicArray.size()>0)
            {
                ivAddImage.setImageBitmap(bitmapDosagePicArray.get(bitmapDosagePicArray.size()-1));
            }
            else
            {
                ivAddImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.gpc_thumbnail_default));
                ivImageDelete.setVisibility(View.GONE);
            }

        }
    }


    public void selectStartDate(View view)
    {
        selectedDateType = "start";
        datePicker = new DatePickerDialog(this,this,startYear,startMonth,startDay);

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePicker.show();
    }

    public void selectEndDate(View view)
    {
        selectedDateType = "end";
        datePicker = new DatePickerDialog(this,this,startYear,startMonth,startDay);
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear,startMonth,startDay);
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis()-1000);
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        if(selectedDateType.equals("start"))
        {
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;

        }
        else
        {
            endYear = year;
            endMonth = month;
            endDay = dayOfMonth;
        }
        updateDateValue();
        checkDateValidation();

    }

    private void checkDateValidation()
    {
        String strStartDate = startYear+"-"+startMonth+"-"+startDay;
        String strEnddate = endYear+"-"+endMonth+"-"+endDay;
        try
        {
            Date stDate = Utils.convertStringToDate(strStartDate);
            Date edDate = Utils.convertStringToDate(strEnddate);
            isValidDate = Utils.compareDate(stDate, edDate);
            if(isValidDate)
            {
                tvEndDate.setTextColor(Color.RED);
            }
            else
            {
                tvEndDate.setTextColor(ContextCompat.getColor(this,R.color.default_text_color));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void updateDateValue()
    {
        if(selectedDateType.equals("start"))
        {
            String updatedDate = convertDateToUserFormat(startYear,startMonth,startDay);
            tvStartDate.setText(updatedDate);
        }
        else
        {
            String updatedDate = convertDateToUserFormat(endYear,endMonth,endDay);
            tvEndDate.setText(updatedDate);
        }


    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public void showNumberPicker(View view)
    {
        final Dialog d = new Dialog(DoctorMedicationReminderActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvTimesPerDay.setText(String.valueOf(np.getValue()));
                totalTimesPerDay = np.getValue();
                addTimeSlots();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
    public void updateAlertTime()
    {
        switch (mAlertNotifyType)
        {
            case 0:
                tvAlertTime.setText(getString(R.string.lbl_alert_at_time_event));
                break;
            case 1:
                tvAlertTime.setText(getString(R.string.lbl_alert_5_min_before));
                break;
            case 2:
                tvAlertTime.setText(getString(R.string.lbl_alert_10_min_before));
                break;

        }
    }
    public void showAlertRemainderDialog(View view)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DosageAlertReminderDialogFragment alRemFrag = DosageAlertReminderDialogFragment.newInstance();
        alRemFrag.show(ft,"alertRemainderDialog");
    }




    private void saveMeicationRemainder()
    {
        if(isValid())
        {
            updateMedicatonRemainder();
        }
    }
    private boolean isValid()
    {
        String medicName = edMedicaName.getText().toString();
        if(!isValidDate)
        {
            Utils.showToast(this,"Invalid start and end date");
            return false;
        }
        else if(medicName.equals("") && mDosageInfo[0]==null)
        {
            Utils.showToast(this,"Please enter Medication Name and select dosage");
            return false;
        }
        else if(medicName.equals(""))
        {
            Utils.showToast(this,"Please enter medication name");
            return false;
        }
        else if(mDosageInfo[0]==null)
        {
            Utils.showToast(this,"Please select dosage");
            return false;
        }
        else
        {
            return true;
        }

    }
    private String getImageIdAsString() {
        String ids = "";
        if(dosageImgIdList.size()>0)
        {
            for(int i=0;i<dosageImgIdList.size();i++)
            {
                if(i==0)
                    ids = String.valueOf(dosageImgIdList.get(i));
                else
                {
                    ids = ","+String.valueOf(dosageImgIdList.get(i));
                }
            }
        }
        return ids;
    }
    private ArrayList<String> getSelectedTimeList() {
        ArrayList<String> itemList = new ArrayList<>();
        for (TextView aTvTimeSlotTimeArray : tvTimeSlotTimeArray)
            itemList.add(aTvTimeSlotTimeArray.getText().toString());
        return itemList;
    }
    private void updateMedicatonRemainder()
    {
        progressDialog = LoadingDialog.show(this,"Creating Remainder...");
        UserDetailsBean userDetailBean = globalApplication.getUserDetailsBean();
        int patientId = globalApplication.getPatSelectedCallerId();
        int doctorId = globalApplication.getUserId();
        int creatorId = doctorId;
        String alertMiuntes = "";
        ArrayList<String> arraySelectedTimes = getSelectedTimeList();

        if(mAlertNotifyType==1)
            alertMiuntes = "5";
        else if(mAlertNotifyType==2)
            alertMiuntes= "10";
        else
            alertMiuntes="0";
        String medicName = edMedicaName.getText().toString();
        String imgId = getImageIdAsString();

        final Call<Success> updateMedicationCall = apiInterface.doAddMedicationRemainder(patientId,
                doctorId,
                mSelectedSartDate,
                mSelectedEnddate,
                alertMiuntes,
                creatorId,
                medicName,
                arraySelectedTimes,
                imgId,
                mDosageInfo[2],
                mDosageInfo[0],
                "This is dummy text"
        );
        updateMedicationCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                progressDialog.dismiss();
                if(response.code()==200)
                {
                    finish();

                }
                else if(response.code()==400)
                {
                    Utils.showToast(DoctorMedicationReminderActivity.this,"Something went wrong please try again...");

                }
                else
                {
                    Utils.showToast(DoctorMedicationReminderActivity.this,"Something went wrong please try again...");
                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showToast(DoctorMedicationReminderActivity.this,t.getLocalizedMessage()+"");
            }
        });
    }


}
