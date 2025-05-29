package com.vari.varidetect.Activity.OnBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vari.varidetect.Activity.Home.HomeActivity;
import com.vari.varidetect.Application.BaseActivity;
import com.vari.varidetect.Fragment.Home.HomeFragment;
import com.vari.varidetect.Fragment.OnBoard.TrustedFragment;
import com.vari.varidetect.Fragment.OnBoard.UploadAnalyzeFragment;
import com.vari.varidetect.Fragment.OnBoard.VerifyRealityFragment;
import com.vari.varidetect.R;

public class OnBoardActivity extends BaseActivity {

    Button nxtBtn,skpBtn,getStartBtn;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        inItWidgets();
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count==1){
                    nxtBtn.setVisibility(View.VISIBLE);
                    skpBtn.setVisibility(View.VISIBLE);
                    getStartBtn.setVisibility(View.GONE);
                    Position(new UploadAnalyzeFragment());
                }else if(count==2){
                    nxtBtn.setVisibility(View.GONE);
                    skpBtn.setVisibility(View.GONE);
                    getStartBtn.setVisibility(View.VISIBLE);
                    Position(new TrustedFragment());
                }
            }
        });
        skpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardActivity.this, HomeActivity.class));
                finish();
            }
        });
        getStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void inItWidgets(){
        nxtBtn = findViewById(R.id.nextBtn);
        skpBtn = findViewById(R.id.skipBtn);
        getStartBtn = findViewById(R.id.getStartBtn);
        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameOnBoard, new VerifyRealityFragment()).commit();
    }
    private void Position(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameOnBoard, fragment)
                    .commit();
        }
    }
}