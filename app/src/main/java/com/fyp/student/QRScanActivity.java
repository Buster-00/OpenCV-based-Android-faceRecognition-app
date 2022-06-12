package com.fyp.student;

import static com.fyp.utilities.QRCodeHelper.createQRCodeBitmap;
import static com.fyp.invariable.InVar.MAX_DISTANCE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fyp.R;
import com.fyp.utilities.GetDistanceUtils;
import com.fyp.utilities.QRCodeHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class QRScanActivity extends AppCompatActivity {

    TextView tv_distance;
    ImageView imageView_QRCode;
    Button btn_scan;

    //location
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tv_distance = findViewById(R.id.distance);
        imageView_QRCode = findViewById(R.id.QR_code);
        btn_scan = findViewById(R.id.btn_scan);

        double lati_1 = 0;
        double longti_1 = 0;


        location = getLocation();
        tv_distance.setText("longitude: "+ location.getLongitude()+" latitude: "+location.getLatitude());
        //imageView_QRCode.setImageBitmap(createQRCodeBitmap("latitude: " + location.getLatitude() + "longitude: " + location.getLongitude(), 640,640));

        //set scan button
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRScanActivity.this);
                intentIntegrator.setPrompt("Scan the QRcode");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLocation() {
        //检查定位权限
        ArrayList<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //get result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //do something to result
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //get json and object
                String json = result.getContents();
                ObjectMapper mapper = new JsonMapper();
                QRCodeHelper.QRInformation info = null;
                try {
                    //get distance
                    info = mapper.readValue(json, QRCodeHelper.QRInformation.class);
                    Location location = getLocation();
                    double distance = GetDistanceUtils.distanceInMi(info.getLatitude(), info.getLongitude(), location.getLatitude(), location.getLongitude());
                    Log.e("distance", " "+ info.getLatitude() + " "+ info.getLongitude() + " " +location.getLatitude() + " " + location.getLongitude());
                    Toast.makeText(this, "Scanned: " + distance, Toast.LENGTH_LONG).show();

                    //if distance more than certain value
                    if(distance >= MAX_DISTANCE){
                        Intent intent = new Intent(QRScanActivity.this, DistanceFailedActivity.class);
                        intent.putExtra("DISTANCE", distance);
                        startActivity(intent);
                    }
                    //go on to next step
                    else{
                        Intent intent = new Intent(QRScanActivity.this, DistanceSuccessActivity.class);
                        intent.putExtra("DISTANCE", distance);
                        intent.putExtra("JSON", json);
                        startActivity(intent);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}