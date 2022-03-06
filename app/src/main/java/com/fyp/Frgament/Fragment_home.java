package com.fyp.Frgament;

import static com.fyp.databaseHelper.UserManager.getCurrentUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.fyp.databaseHelper.StudentAccountDB;
import com.fyp.login.MTLogin;

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
    Button btn_register;
    Button btn_recognition;
    Button btn_viewPager;
    Button btn_delete;

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
        btn_register = view.findViewById(R.id.btn_register);
        btn_recognition = view.findViewById(R.id.btn_recognition);
        btn_viewPager = view.findViewById(R.id.btn_viewPager);
        btn_delete = view.findViewById(R.id.btn_delete);

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