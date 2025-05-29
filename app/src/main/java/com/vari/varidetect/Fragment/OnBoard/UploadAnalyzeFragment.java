package com.vari.varidetect.Fragment.OnBoard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vari.varidetect.R;


public class UploadAnalyzeFragment extends Fragment {



    public UploadAnalyzeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload_analyze, container, false);
        return v;
    }
}