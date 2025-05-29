package com.vari.varidetect.Activity.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vari.varidetect.Application.BaseActivity;
import com.vari.varidetect.Fragment.FAQ.FAQFragment;
import com.vari.varidetect.Fragment.History.HistoryFragment;
import com.vari.varidetect.Fragment.Home.HomeFragment;
import com.vari.varidetect.Fragment.Setting.SettingFragment;
import com.vari.varidetect.Fragment.Upload.UploadFragment;
import com.vari.varidetect.R;

public class HomeActivity extends BaseActivity {

    FrameLayout frameLayout;

    // Image View
    ImageView homeBtn,uploadBtn,histroyBtn,settingBtn,questionBtn;

    public static LinearLayout layoutBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inItWidgets();
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Position(new HomeFragment());
                homeBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
                uploadBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                histroyBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                settingBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                questionBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Position(new UploadFragment());
                uploadBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
                homeBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                histroyBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                settingBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                questionBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
            }
        });
        histroyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Position(new HistoryFragment());
                histroyBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
                uploadBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                homeBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                settingBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                questionBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Position(new SettingFragment());
                settingBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
                uploadBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                histroyBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                homeBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                questionBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
            }
        });
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Position(new FAQFragment());
                questionBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
                uploadBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                histroyBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                settingBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
                homeBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
            }
        });




    }
    private void inItWidgets(){
        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();

        // Image View
        homeBtn = findViewById(R.id.homeMenu);
        uploadBtn = findViewById(R.id.uploadMenu);
        histroyBtn = findViewById(R.id.historyMenu);
        settingBtn = findViewById(R.id.settingMenu);
        questionBtn = findViewById(R.id.questionMenu);

        layoutBottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void Position(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        // Agar koi fragments backstack mein hain, to clear karo
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // HomeFragment reload karo
        Position(new HomeFragment());
        // Navigation menu UI update karo
        homeBtn.setBackground(getDrawable(R.drawable.bg_select_menu));
        uploadBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
        histroyBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
        settingBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
        questionBtn.setBackground(getDrawable(R.drawable.bg_unselect_menu));
    }
}