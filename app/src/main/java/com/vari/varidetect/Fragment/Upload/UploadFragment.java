package com.vari.varidetect.Fragment.Upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.vari.varidetect.Fragment.Upload.Result.ResultFragment;
import com.vari.varidetect.HelperClass.DeepfakeDetectionHelper;
import com.vari.varidetect.HelperClass.DeepfakeDetectionHelperAudio;
import com.vari.varidetect.HelperClass.Utils;
import com.vari.varidetect.R;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UploadFragment extends Fragment {


    public UploadFragment() {
        // Required empty public constructor
    }

    private LinearProgressIndicator linearProgressIndicator;
    private RelativeLayout SelectDataRL,showDataRL;

    private static final int PICK_MEDIA_REQUEST = 101;
    private ExecutorService executorService;

    private ImageView dataIcon;
    private TextView datanameTV,dataDateTV,procTV;

    private Button submitBTN;

    private Uri mediaUri;

    private String mediaName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload, container, false);
        executorService = Executors.newSingleThreadExecutor();
        linearProgressIndicator = v.findViewById(R.id.progress);
        SelectDataRL = v.findViewById(R.id.layoytSelectData);
        showDataRL = v.findViewById(R.id.layoutshowData);
        dataIcon = v.findViewById(R.id.dataIcon);
        datanameTV = v.findViewById(R.id.dataName);
        dataDateTV = v.findViewById(R.id.dataDate);
        procTV = v.findViewById(R.id.processingTV);
        submitBTN = v.findViewById(R.id.btnSubmit);
        SelectDataRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMediaPicker();
            }
        });
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaUri==null){
                    Toast.makeText(getContext(), "Select Media File", Toast.LENGTH_SHORT).show();
                }else {
                    linearProgressIndicator.setVisibility(View.VISIBLE);
                    procTV.setVisibility(View.VISIBLE);
                    processMediaFile();
                }


            }
        });

        return v;
    }

    private void processMediaFile(){
        executorService.execute(() ->{
            try {
                String mediaType = detectMediaType(mediaUri);

                float[] result;

                final float[] prediction = new float[1]; // <-- final array!

                if(mediaType.equals("audio")){
                    // Audio file detected
                    DeepfakeDetectionHelperAudio audioHelper = new DeepfakeDetectionHelperAudio(getContext());
                    result = audioHelper.detectDeepfake(mediaUri);
                    audioHelper.close();

                    // Since audio model output is [1][1]
                    prediction[0] = (result[0] > 0.5f) ? 1f : 0f;

                }else{
                    // Image or Video file detected
                    DeepfakeDetectionHelper imageHelper = new DeepfakeDetectionHelper(getContext());
                    result = imageHelper.detectDeepfake(mediaUri);
                    imageHelper.close();

                    // Image/Video model output is [1][2]
                    prediction[0] = (result[1] > result[0]) ? 1f : 0f;

                }

                // Switch to UI thread
                requireActivity().runOnUiThread(() ->{
                    linearProgressIndicator.setVisibility(View.GONE);
                    procTV.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        showDeterminateProgress();
                    }

                    // Navigate to result fragment with prediction
                    Bundle bundle = new Bundle();
                    bundle.putFloat("prediction", prediction[0]);
                    bundle.putString("mediaType", mediaType); // image, video, audio
                    bundle.putParcelable("mediaUri", mediaUri);
                    new Handler().postDelayed(()->{
                        ResultFragment resultFragment = new ResultFragment();
                        resultFragment.setArguments(bundle);

                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, resultFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    },2000);
                });


            }catch (IOException e){
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    // Method for indeterminate progress
    private void showIndeterminateProgress(){
        linearProgressIndicator.setIndeterminate(true); // Set indeterminate mode
        linearProgressIndicator.setVisibility(View.VISIBLE); // Make it visible

        // Simulate a task and hide the progress indicator after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearProgressIndicator.setVisibility(View.GONE);
            }
        },1000); // 1 seconds delay

    }

    // Method for determinate progress
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDeterminateProgress(){
        linearProgressIndicator.setIndeterminate(false); // Set to determinate mode
        linearProgressIndicator.setVisibility(View.VISIBLE); // Make it visible
        linearProgressIndicator.setProgress(0,true);// set progress to 0

        // Simulate progress update
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int progress = 0; // Increase progress by 10

            @Override
            public void run() {
                progress += 10; // Increase progress by 10
                linearProgressIndicator.setProgressCompat(progress,true); // Update progress
                if(progress < 100){
                    if(isAdded() && !isDetached()){
                        handler.postDelayed(this,500); // Update every 500 milliseconds
                    }else {
                        handler.removeCallbacks(this); // Remove any pending messages
                    }

                }else {
//                    linearProgressIndicator.setVisibility(View.GONE);
                }
            }
        };
        handler.post(runnable); // Start the progress updates
    }

    // Method to hide progress
    private void hideProgress(){
        linearProgressIndicator.setVisibility(View.GONE); // Make it invisible
    }

    // Method to change direction
    private void changeIndicatorDirection(){
        linearProgressIndicator.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
    }


    private void openMediaPicker(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // All media
        startActivityForResult(intent,PICK_MEDIA_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            mediaUri = data.getData();

            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            requireContext().getContentResolver()
                    .takePersistableUriPermission(
                            mediaUri, takeFlags);

            linearProgressIndicator.setVisibility(View.VISIBLE);
            procTV.setVisibility(View.VISIBLE);
            showDataRL.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDeterminateProgress();
            }
            new Handler().postDelayed(()->{
                executorService.execute(()-> {
                    String fileName = getFileName(mediaUri);
                    String mediaType = detectMediaType(mediaUri);
                    // Switch back to main thread for UI
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String date = Utils.getCurrentFormattedDate();
                            linearProgressIndicator.setVisibility(View.GONE);
                            procTV.setVisibility(View.GONE);

                            showDataRL.setVisibility(View.VISIBLE);
                            datanameTV.setText(fileName);
                            dataDateTV.setText(date);
                            if(mediaType.equals("image")){
                                dataIcon.setImageDrawable(getContext().getDrawable(R.drawable.image));
                            }else if(mediaType.equals("audio")){
                                dataIcon.setImageDrawable(getContext().getDrawable(R.drawable.sound_wave));
                            }else if(mediaType.equals("video")){
                                dataIcon.setImageDrawable(getContext().getDrawable(R.drawable.play_button));
                            }else{
                                dataIcon.setImageDrawable(getContext().getDrawable(R.drawable.question));
                            }
                        }
                    });
                });
            }, 2000); // 2 second delay


        }

    }



    private String getFileName(Uri uri){
        String result = null;

        if("content".equals(uri.getScheme())){
            try (Cursor cursor = requireContext().getContentResolver().query(uri,null,null,null,null)){
                if(cursor != null && cursor.moveToFirst()){
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }catch (SecurityException e){
                Log.e("FileName", "Access denied: " + e.getMessage());
                result = "Access denied file";
            }
        }
        if(result == null){
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private String detectMediaType(Uri uri){
        String mimeType = requireContext().getContentResolver().getType(uri);
        if (mimeType == null) return "unknown";

        if(mimeType.startsWith("image/")){
            return "image";
        } else if (mimeType.startsWith("video/")) {
            return "video";
        } else if (mimeType.startsWith("audio/")) {
            return "audio";
        }else {
            return "unknown";
        }
    }

    private float[] prepareInputFromMedia(Uri uri){
        // Dummy input until real preprocessing is added
        float[] input = new float[10]; // Suppose your model expects 10 floats
        for (int i = 0; i < 10; i++) {
            input[i] = 0.5f; // Dummy values
        }
        return input;
    }

}