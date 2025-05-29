package com.vari.varidetect.Fragment.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vari.varidetect.Fragment.Upload.UploadFragment;
import com.vari.varidetect.R;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }


    RelativeLayout layoutUploadBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        layoutUploadBTN = v.findViewById(R.id.uploadBTN);
        layoutUploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToUploadFragment();
            }
        });
        return v;
    }

    private void navigateToUploadFragment(){
        UploadFragment uploadFragment = new UploadFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,uploadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}