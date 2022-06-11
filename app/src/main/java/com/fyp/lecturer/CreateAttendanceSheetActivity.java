package com.fyp.lecturer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fyp.R;
import com.fyp.databaseHelper.LectureDB;
import com.fyp.databaseHelper.UserManager;
import com.fyp.utilities.QRCodeHelper;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class CreateAttendanceSheetActivity extends AppCompatActivity implements Validator.ValidationListener {

    //widget
    @NotEmpty
    EditText et_LectureID;

    @NotEmpty
    EditText et_LectureName;

    @NotEmpty
    EditText et_Venue;

    CalendarView calendarView;
    Button btn_create;
    Toolbar toolbar;

    //Validator
    Validator validator;

    //date
    final String[] date = {new String()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance_sheet);

        //Initialize Validator
        initValidator();

        //Initialize widget
        et_LectureID = findViewById(R.id.et_lectureID);
        et_LectureName = findViewById(R.id.et_lectureName);
        et_Venue = findViewById(R.id.et_Venue);
        calendarView = findViewById(R.id.calendar_view);
        btn_create = findViewById(R.id.btn_create);
        toolbar = findViewById(R.id.toolbar);

        //set widgets


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date[0] = year + "-" + month + "-" + day;

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {

        //Retrieve information
        String lecturerID = UserManager.getCurrentUser().getID();
        String lectureID = et_LectureID.getText().toString();
        String lectureName = et_LectureName.getText().toString();
        String venue = et_Venue.getText().toString();
        Toast.makeText(CreateAttendanceSheetActivity.this,
                lectureID + "-" + lectureName + "-" + venue + "\n" + date[0],
                Toast.LENGTH_LONG).show();

        //Create Json
        //get location
        Location location = getLocation();

        QRCodeHelper.QRInformation info = new QRCodeHelper.QRInformation();
        info.setDate(date[0]);
        info.setLectureID(lectureID);
        info.setLecturer(UserManager.getCurrentUser().getName());
        info.setLecturerID(lecturerID);
        info.setLectureName(lectureName);
        info.setVenue(venue);
        info.setLatitude(location.getLatitude());
        info.setLongitude(location.getLongitude());
        ObjectMapper mapper = new JsonMapper();
        String json = new String();
        try {
            json = mapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        //upload lecture Information to server
        LectureDB DB = new LectureDB(this);
        DB.insert(lectureID, lectureName, lecturerID, date[0], venue);

        //Turn to next activity
        Intent intent = new Intent(CreateAttendanceSheetActivity.this, LecturerQRCodeActivity.class);
        intent.putExtra("INFO", json);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    //Get Location
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLocation() {
        //检查定位权限
        ArrayList<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(CreateAttendanceSheetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(CreateAttendanceSheetActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        //判断
        if (permissions.size() == 0) {//有权限，直接获取定位
            return getLocationLL();
        } else {//没有权限，获取定位权限
            requestPermissions(permissions.toArray(new String[permissions.size()]), 2);
            return null;
        }
    }

    /**
     * 定位：获取经纬度
     */
    private Location getLocationLL() {
        Log.d("*************", "获取定位权限1 - 开始");
        Location location = getLastKnownLocation();
        if (location != null) {

            //日志
            String locationStr = "维度：" + location.getLatitude() + "\n"
                    + "经度：" + location.getLongitude();
            Log.e("*************", "经纬度：" + locationStr);
        } else {
            Toast.makeText(this, "位置信息获取失败", Toast.LENGTH_SHORT).show();
            Log.d("*************", "获取定位权限7 - " + "位置获取失败");
        }

        return location;
    }

    /**
     * 定位：得到位置对象
     *
     * @return
     */
    private Location getLastKnownLocation() {
        //获取地理位置管理器
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * 定位：权限监听
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2://定位
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("*************", "同意定位权限");

                } else {
                    Toast.makeText(this, "未同意获取定位权限", Toast.LENGTH_SHORT).show();

                }
                break;
            default:

        }


    }
}