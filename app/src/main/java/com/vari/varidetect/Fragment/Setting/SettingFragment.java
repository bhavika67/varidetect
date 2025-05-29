package com.vari.varidetect.Fragment.Setting;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.vari.varidetect.Activity.Home.HomeActivity;
import com.vari.varidetect.Fragment.Setting.Language.LanguageFragment;
import com.vari.varidetect.HelperClass.PrefManager;
import com.vari.varidetect.R;




public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }




    TextView selectLang;

    RelativeLayout layoutLang;
    PrefManager prefManager;

    Switch modeSwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "theme_prefs";
    private static final String IS_DARK_MODE = "is_dark_mode";

    RelativeLayout layout_change;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        layoutLang = v.findViewById(R.id.layoutLanguage);
        selectLang = v.findViewById(R.id.selectLanguage);
        modeSwitch = v.findViewById(R.id.switchMode);
        layout_change = v.findViewById(R.id.layoutChange);
        prefManager = new PrefManager(getActivity());
        selectLang.setText(prefManager.getLanguage());
        layoutLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSelectLanguage();
            }
        });
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean isDarkMode = sharedPreferences.getBoolean(IS_DARK_MODE, false);
        modeSwitch.setChecked(isDarkMode);
        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(IS_DARK_MODE, true);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(IS_DARK_MODE, false);
            }
            editor.apply();
        });

        return v;
    }

    private void navigateToSelectLanguage(){
        LanguageFragment languageFragment = new LanguageFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,languageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        HomeActivity.layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.layoutBottomNavigation.setVisibility(View.VISIBLE);
    }
}