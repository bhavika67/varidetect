package com.vari.varidetect.Fragment.Upload.Result;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vari.varidetect.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ResultFragment extends Fragment {



    public ResultFragment() {
        // Required empty public constructor
    }


    TextView percentageTV;
    LinearProgressIndicator progressIndicator;

    CardView cardView;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int finalPercentage = 0;
    private boolean isCounting = false;
    private boolean isAudioPlaying = false;
    private Runnable audioBarsUpdater;
    private LinearLayout audioBarsLayout;
    private Button saveBTN,shareBTN;
    private String mediaType,mediaName;
    private Uri mediaUri;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        percentageTV = v.findViewById(R.id.percentage);
        progressIndicator = v.findViewById(R.id.progress);
        firestore = FirebaseFirestore.getInstance();
        cardView = v.findViewById(R.id.dataCard);
        saveBTN = v.findViewById(R.id.saveBtn);
        shareBTN = v.findViewById(R.id.shareBtn);
        cardView.removeAllViews();
        // Prediction score receive karo bundle se
        Bundle bundle = getArguments();
        if(bundle != null){
            float prediction = bundle.getFloat("prediction", 0f); // e.g., 0.85
            mediaType = bundle.getString("mediaType", "image");
            mediaUri = bundle.getParcelable("mediaUri");

            finalPercentage = Math.round(prediction * 100); // 85

            animateProgressBar(progressIndicator,finalPercentage);
            animatePercentageText();

            setUploadedData(mediaType, mediaUri, cardView);

        }

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReportToFirestore();
            }
        });

        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareReport();
            }
        });

        return v;
    }


    private void saveReportToFirestore(){
        mediaName = getFileNameFromUri(mediaUri);
        // Save report details to Firestore
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("prediction", finalPercentage); // as Integer
        reportData.put("mediaType", mediaType);
        reportData.put("mediaName", mediaName);
        reportData.put("timestamp", System.currentTimeMillis());

        firestore.collection("Reports")
                .add(reportData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Report saved to Firestore", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    requireActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                })
                .addOnFailureListener(e ->{
                    Toast.makeText(getContext(), "Error saving to Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

    }

    private void shareReport(){
        try {
            // Create PDF
            File pdfFile = createPdfReport();

            // Share the PDF via Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            Uri fileUri = FileProvider.getUriForFile(
              requireContext(),
              requireContext().getPackageName() + ".provider",
                    pdfFile
            );
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // VERY IMPORTANT

            // Grant permissions manually to all apps that can receive the intent
            List<ResolveInfo> resInfoList = requireContext().getPackageManager()
                            .queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo: resInfoList){
                String packageName = resolveInfo.activityInfo.packageName;
                requireContext().grantUriPermission(packageName,fileUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(Intent.createChooser(shareIntent, "Share Report"));

        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error creating report: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File createPdfReport() throws IOException{
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();


        // Create a PageInfo for the PDF document (A4 size)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595,842,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Write the content (add text, images, etc.)
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(16);

        // Add Title
        canvas.drawText("Deepfake Detection Report", 50, 50, paint);

        // Add Prediction Data
        canvas.drawText("Prediction: " + finalPercentage, 50, 100, paint);

        // Add Media Type and Link
        canvas.drawText("Media Type: " + mediaType, 50, 150, paint);
        canvas.drawText("Media URL: " + mediaUri, 50, 200, paint);

        document.finishPage(page);

        // Save the PDF to storage
        File pdfFile = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "deepfake_report.pdf");
        document.writeTo(new FileOutputStream(pdfFile));
        document.close();

        return pdfFile;

    }

    private void animateProgressBar(LinearProgressIndicator progressIndicator, int targetProgress) {
        setProgressColor(progressIndicator, targetProgress);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressIndicator, "progress", 0, targetProgress);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void setProgressColor(LinearProgressIndicator progressIndicator, int percentage) {
        int color;
        if (percentage <= 30) {
            color = getResources().getColor(R.color.green);
        } else if (percentage <= 70) {
            color = getResources().getColor(R.color.orange);
        } else {
            color = getResources().getColor(R.color.purple);
        }
        progressIndicator.setIndicatorColor(color);
    }

    private void animatePercentageText(){
        if (isCounting) return;

        isCounting = true;
        new Thread(() ->{
            for (int i = 0; i <= finalPercentage; i++) {
                int current = i;
                handler.post(() -> percentageTV.setText(current + "% Fake"));
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isCounting = false;
        }).start();
    }


    private void setUploadedData(String mediaType, Uri mediaUri, CardView dataCard) {
        if (mediaUri == null) return;

        dataCard.removeAllViews();

        if (mediaType.equals("image")) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageURI(mediaUri);

            dataCard.addView(imageView);

        } else if (mediaType.equals("video")) {
            FrameLayout wrapper = new FrameLayout(getContext());
            wrapper.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            wrapper.setForegroundGravity(Gravity.CENTER);
            videoView = new VideoView(getContext());
            FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            videoParams.gravity = Gravity.CENTER;
            videoView.setLayoutParams(videoParams);

            videoView.setVideoURI(mediaUri);


            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                videoView.start();
            });

            wrapper.addView(videoView);
            dataCard.addView(wrapper);

        } else if (mediaType.equals("audio")) {
            audioBarsLayout = createAudioBars();
            dataCard.addView(audioBarsLayout);

            mediaPlayer = MediaPlayer.create(getContext(), mediaUri);
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                isAudioPlaying = true;
                startAudioBarsAnimation();
            });
        }
    }

    private LinearLayout createAudioBars() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        int bars = 5;
        for (int i = 0; i < bars; i++) {
            View bar = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 50);
            params.setMargins(10, 0, 10, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundColor(Color.WHITE);
            layout.addView(bar);
        }

        return layout;
    }


    private void startAudioBarsAnimation() {
        audioBarsUpdater = new Runnable() {
            Random random = new Random();

            @Override
            public void run() {
                if (!isAudioPlaying) return;

                for (int i = 0; i < audioBarsLayout.getChildCount(); i++) {
                    View bar = audioBarsLayout.getChildAt(i);
                    ViewGroup.LayoutParams params = bar.getLayoutParams();
                    params.height = random.nextInt(100) + 50; // Random height
                    bar.setLayoutParams(params);
                }

                handler.postDelayed(this, 300);
            }
        };
        handler.post(audioBarsUpdater);
    }

    private void stopAudioBarsAnimation() {
        isAudioPlaying = false;
        if (audioBarsUpdater != null) {
            handler.removeCallbacks(audioBarsUpdater);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseResources();
    }

    private void releaseResources() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
        stopAudioBarsAnimation();
        handler.removeCallbacksAndMessages(null);
    }


    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


}