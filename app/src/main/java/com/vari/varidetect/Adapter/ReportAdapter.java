package com.vari.varidetect.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vari.varidetect.Model.Reports;
import com.vari.varidetect.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<Reports> reportsList;
    private Context context;

    private ProgressDialog progressDialog;

    public ReportAdapter(List<Reports> reportsList, Context context) {
        this.reportsList = reportsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_data,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        Reports reports = reportsList.get(position);

        holder.dataName.setText(reports.getMediaName());
        holder.dataDate.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date(reports.getTimestamp())));

        if(reports.getMediaType().equals("image")){
            holder.dataIcon.setImageDrawable(context.getDrawable(R.drawable.image));
        } else if (reports.getMediaType().equals("video")) {
            holder.dataIcon.setImageDrawable(context.getDrawable(R.drawable.play_button));
        } else if (reports.getMediaType().equals("audio")) {
            holder.dataIcon.setImageDrawable(context.getDrawable(R.drawable.sound_wave));
        }
        holder.downIcon.setVisibility(View.GONE);

//        holder.downIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                downloadMedia(reports.getMediaName(), reports.getMediaType());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  dataName, dataDate;
        ImageView dataIcon,downIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataName = itemView.findViewById(R.id.dataName);
            dataDate = itemView.findViewById(R.id.dataDate);
            dataIcon = itemView.findViewById(R.id.dataIcon);
            downIcon = itemView.findViewById(R.id.donwloadIcon);
        }
    }

    private String getFileNameFromUri(Uri uri){
        String result = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query( uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()){
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1){
                    result = cursor.getString(nameIndex);
                }
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (result == null){
            // Fallback: Get last path segment
            result = uri.getLastPathSegment();
        }

        return result;
    }


    private void downloadMedia(String uriString, String mediaType){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(()->{
            try {
                Uri uri = Uri.parse(uriString);
                ContentResolver resolver = context.getContentResolver();
                InputStream inputStream = resolver.openInputStream(uri);

                if (inputStream == null){
                    ((Activity) context).runOnUiThread(()->
                                    Toast.makeText(context, "Media not found!", Toast.LENGTH_SHORT).show()
                            );
                    return;
                }

                String fileName = "DeepFake_" + System.currentTimeMillis();
                String extension = mediaType.equals("image") ? ".jpg" :
                        mediaType.equals("video") ? ".mp4" : ".mp3";
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName+extension);

                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int len;
                while ((len = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer,0,len);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                MediaScannerConnection.scanFile(context,
                        new String[]{file.getAbsolutePath()},
                        null,
                        (path,scannedUri) -> Log.d("Download", "Scanned into media store: " + path));

                ((Activity) context).runOnUiThread(() ->{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Downloaded: "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                });

            }catch (IOException e){
                e.printStackTrace();
                ((Activity) context).runOnUiThread(()->{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Download failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

        }).start();
    }


}
