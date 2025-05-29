package com.vari.varidetect.Fragment.History;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vari.varidetect.Activity.Home.HomeActivity;
import com.vari.varidetect.Model.FilterOptions;
import com.vari.varidetect.Model.SharedReportViewModel;
import com.vari.varidetect.R;


public class SortByFragment extends Fragment {


    public SortByFragment() {
        // Required empty public constructor
    }



    SeekBar seekBar;
    TextView endTV;
    ImageView backIV;
    Button applyBtn,resetBtn;
    RadioGroup sortGroup,fileTypeGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sort_by, container, false);
        seekBar = v.findViewById(R.id.seekPointer);
        endTV = v.findViewById(R.id.endPoint);
        backIV = v.findViewById(R.id.backBtn);
        applyBtn = v.findViewById(R.id.applyButton);
        resetBtn = v.findViewById(R.id.resetButton);
        sortGroup = v.findViewById(R.id.sortBYGroup);
        fileTypeGroup = v.findViewById(R.id.fileTypeGroup);

        applyBtn.setOnClickListener(view -> {
            String sortBy = "date";
            String fileType = null;

            int sortId = sortGroup.getCheckedRadioButtonId();
            if(sortId != -1){
                RadioButton selected = v.findViewById(sortId);
                sortBy = selected.getText().toString().toLowerCase();
            }

            int typeId = fileTypeGroup.getCheckedRadioButtonId();
            if(typeId != -1){
                RadioButton selected = v.findViewById(typeId);
                fileType  = selected.getText().toString().toLowerCase();
            }
            int confidence = seekBar.getProgress();

            FilterOptions options = new FilterOptions(sortBy,fileType,confidence,false);
            new ViewModelProvider(requireActivity()).get(SharedReportViewModel.class)
                    .setFilterOptions(options);

            getParentFragmentManager().popBackStack();

        });

        resetBtn.setOnClickListener(view -> {
            FilterOptions options = new FilterOptions("date",null,0,true);
            new ViewModelProvider(requireActivity()).get(SharedReportViewModel.class)
                    .setFilterOptions(options);
            getParentFragmentManager().popBackStack();
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                endTV.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        HomeActivity.layoutBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.layoutBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeActivity.layoutBottomNavigation.setVisibility(View.VISIBLE);

    }
}