package com.vari.varidetect.Fragment.Setting.Language;

import android.content.Context;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vari.varidetect.Activity.Home.HomeActivity;
import com.vari.varidetect.Adapter.SelectLanguageAdapter;
import com.vari.varidetect.Fragment.Home.HomeFragment;
import com.vari.varidetect.HelperClass.LanguageManager;
import com.vari.varidetect.HelperClass.PrefManager;
import com.vari.varidetect.Model.LanguageModel;
import com.vari.varidetect.R;

import java.util.ArrayList;
import java.util.List;


public class LanguageFragment extends Fragment implements SelectLanguageAdapter.OnLanguageSelectedListener{



    public LanguageFragment() {
        // Required empty public constructor
    }


    RecyclerView selectLangRV;
    List<LanguageModel> lang;
    SelectLanguageAdapter languageAdapter;
    ImageView backIV;

    private LanguageManager languageManager;
    private PrefManager prefManager;

    private String selectedLanguage = "Eng"; // default or update from user selection

    Button applyBTN,resetBTN;

    String savedLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_language, container, false);
        languageManager = new LanguageManager(getActivity());
        prefManager = new PrefManager(getActivity());
        savedLanguage = prefManager.getLanguage(); // get saved lang from PrefManager
        backIV = v.findViewById(R.id.backBtn);
        applyBTN = v.findViewById(R.id.applyBtn);
        resetBTN = v.findViewById(R.id.resetBtn);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        selectLangRV = v.findViewById(R.id.languageRecyclerView);
        selectLangRV.setHasFixedSize(true);
        selectLangRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        lang = new ArrayList<>();
        lang.add(new LanguageModel("English","Eng"));
        lang.add(new LanguageModel("Hindi","Hi"));
        lang.add(new LanguageModel("Punjabi","Pun"));
        languageAdapter = new SelectLanguageAdapter(lang,getActivity(),this::onSelected,savedLanguage);
        selectLangRV.setAdapter(languageAdapter);
        applyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageManager.setLocale(selectedLanguage);
                prefManager.saveLanguage(selectedLanguage);
                getActivity().recreate();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        resetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageManager.resetToDefault();
                prefManager.saveLanguage("Eng");
                getActivity().recreate();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return v;
    }

    @Override
    public void onSelected(String langCode) {
        selectedLanguage = langCode;
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