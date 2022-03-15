package com.fyp.Frgament;

import static com.fyp.databaseHelper.UserManager.getCurrentUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fyp.FaceRecognitionActivity;
import com.fyp.FaceRegisterActivity;
import com.fyp.MainActivity;
import com.fyp.R;
import com.fyp.ViewPagerActivity;
import com.fyp.databaseHelper.MariaDBconnector;
import com.fyp.databaseHelper.StudentAccountDB;
import com.fyp.databaseHelper.okHttpHelper;
import com.fyp.login.MTLogin;
import com.ramotion.foldingcell.FoldingCell;

import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Widget
    Button btn_testHttp_response;
    Button btn_testHttp;
    Button btn_register;
    Button btn_recognition;
    Button btn_viewPager;
    Button btn_delete;
    Button btn_testConnect;

    Handler handler;

    public Fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frgament_1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_home newInstance(String param1, String param2) {
        Fragment_home fragment = new Fragment_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Initiate widgets
        btn_testHttp_response = view.findViewById(R.id.btn_testHttp_response);
        btn_register = view.findViewById(R.id.btn_register);
        btn_recognition = view.findViewById(R.id.btn_recognition);
        btn_viewPager = view.findViewById(R.id.btn_viewPager);
        btn_delete = view.findViewById(R.id.btn_delete);
        btn_testConnect = view.findViewById(R.id.btn_testConnect);
        btn_testHttp = view.findViewById(R.id.btn_testHttp);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 100:
                        Log.e("mysql", (String)msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };

        btn_testHttp_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_testHttp_response();
            }
        });

        btn_testHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_testHttp();
            }
        });

        btn_testConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_testConnect();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_register();
            }
        });

        btn_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_recognition();
            }
        });

        btn_viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewPagerActivity.class));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Confirm delete")
                        .content("Do you want to delete your account data?")
                        .positiveText("YES")
                        .negativeText("NO")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteUserData();
                            }
                        }).show();
            }
        });

        return view;
    }

    protected void setBtn_register(){
        startActivity(new Intent(getActivity(), FaceRegisterActivity.class));
    }

    protected void setBtn_recognition(){
        startActivity(new Intent(getActivity(), FaceRecognitionActivity.class));
    }

    protected void setBtn_testConnect(){
        new Thread(){
            @Override
            public void run() {
                MariaDBconnector maria = new MariaDBconnector();
                String result = new String();
                try {
                    result = maria.connectNOSSL();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Message message = Message.obtain();
                message.what = 100;
                message.obj = result;

                handler.handleMessage(message);
            }
        }.start();
    }

    protected void setBtn_testHttp(){
        okHttpHelper httpHelper = new okHttpHelper();
        httpHelper.UploadFile(getActivity().getExternalCacheDir()+"/facerecOPCV/"+"train.xml");
    }

    protected void setBtn_testHttp_response(){
        okHttpHelper httpHelper = new okHttpHelper();
        httpHelper.downloadFile();
    }

    private void deleteUserData() {
        StudentAccountDB DB = new StudentAccountDB(getActivity());

        if(DB.deleteByID(getCurrentUser().getID())){
            Toast.makeText(getActivity(), "Delete account successfully", Toast.LENGTH_SHORT).show();
            DB.close();
            Intent intent = new Intent(getActivity(), MTLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(getActivity(), "Delete account failed", Toast.LENGTH_SHORT).show();
        }

    }


}