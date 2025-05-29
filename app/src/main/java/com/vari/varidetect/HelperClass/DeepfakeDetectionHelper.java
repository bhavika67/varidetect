package com.vari.varidetect.HelperClass;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class DeepfakeDetectionHelper {
    private Interpreter interpreter;
    private Context context;
    private int imageSizeX;
    private int imageSizeY;


    public DeepfakeDetectionHelper(Context context) throws IOException {
        this.context = context;
        Interpreter.Options options = new Interpreter.Options();
        interpreter = new Interpreter(loadModelFile(context),options);

        // Get model input details dynamically
        int[] inputShape = interpreter.getInputTensor(0).shape();
        imageSizeX = inputShape[1];
        imageSizeY = inputShape[2];

        DataType inputDataType = interpreter.getInputTensor(0).dataType();
        Log.d("ModelInput", "Shape: " + Arrays.toString(inputShape) + ", Type: " + inputDataType);

    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException{
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("deepfake_model.tflite");
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    public float[] detectDeepfake(Uri mediaUri) throws IOException{
        // Load and preprocess the image
        Bitmap bitmap = loadImage(mediaUri);
        ByteBuffer inputBuffer = preprocessImage(bitmap);


        // Prepare output buffer
        float[][] output = new float[1][2]; // Assuming output shape (1,2) for binary classification

        // Run inference
        interpreter.run(inputBuffer, output);

        return output[0];
    }


    private ByteBuffer preprocessImage(Bitmap bitmap){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageSizeX, imageSizeY, true);

        int bytesPerChannel = 4; // FLOAT size
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 * imageSizeX * imageSizeY * 3 * bytesPerChannel);
        byteBuffer.order(ByteOrder.nativeOrder());

        for (int y = 0; y < imageSizeY; y++) {
            for (int x = 0; x < imageSizeX; x++) {
                int pixel = resizedBitmap.getPixel(x, y);

                float r = ((pixel >> 16) & 0xFF) / 255.0f;
                float g = ((pixel >> 8) & 0xFF) / 255.0f;
                float b = (pixel & 0xFF) / 255.0f;

                byteBuffer.putFloat(r);
                byteBuffer.putFloat(g);
                byteBuffer.putFloat(b);
            }
        }

        return byteBuffer;
    }

    private Bitmap loadImage(Uri mediaUri) throws IOException{

        Bitmap bitmap = null;
        // Check if the URI is a video
        String mimeType = context.getContentResolver().getType(mediaUri);


        // If it's a video, extract a frame
        if(mimeType != null && mimeType.startsWith("video/")){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context,mediaUri);

            // Extract a frame at time 0 (start of the video)
            bitmap = retriever.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            retriever.release();

        }
        // If it's an image, load it as a Bitmap
        else if (mimeType != null && mimeType.startsWith("image/")) {
            InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }else {
            throw new IOException("Unsupported file type: " + mimeType);
        }

        return bitmap;
    }


    public void close(){
        if (interpreter != null) {
            interpreter.close();
        }
    }

}
