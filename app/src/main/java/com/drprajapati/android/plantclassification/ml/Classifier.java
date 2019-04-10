package com.drprajapati.android.plantclassification.ml;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Classifier {

    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private static final String MODEL_PATH = "plant_seedling_classification_tflite.tflite";

    private static final int DIM_BATCH_SIZE = 1;
    public static final int DIM_IMG_SIZE_HEIGHT = 70;
    public static final int DIM_IMG_SIZE_WIDTH = 70;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int CATEGORY_COUNT = 12;

    private Interpreter mInterpreter;
    private final ByteBuffer mImgData;
    private final int[] mImagePixels = new int[DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH];
    private final float[][] mResult = new float[1][CATEGORY_COUNT];

    public Classifier(Activity activity) throws IOException {
        mInterpreter = new Interpreter(loadModel(activity));
        mImgData = ByteBuffer.allocateDirect( 4
                * DIM_BATCH_SIZE * DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH * DIM_PIXEL_SIZE);
        mImgData.order(ByteOrder.nativeOrder());
    }

    private MappedByteBuffer loadModel(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Result classify(Bitmap bitmap) {
        convertBitmapToByteBuffer(bitmap);
        long startTime = SystemClock.uptimeMillis();
        mInterpreter.run(mImgData, mResult);
        long endTime = SystemClock.uptimeMillis();
        long timeCost = endTime - startTime;
        Log.v(LOG_TAG, "run(): result = " + Arrays.toString(mResult[0])
                + ", timeCost = " + timeCost);
        return new Result(mResult[0]);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (mImgData == null) {
            return;
        }
        mImgData.rewind();

        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_WIDTH; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_HEIGHT; ++j) {
                final float val = mImagePixels[pixel++];
                mImgData.putFloat(val);
            }
        }

    }

}