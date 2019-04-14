package com.drprajapati.android.plantclassification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drprajapati.android.plantclassification.ml.Classifier;
import com.drprajapati.android.plantclassification.ml.TensorFlowImageClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class MainActivity extends AppCompatActivity {

    private static final int INPUT_SIZE = 70;
    private static final boolean QUANT = false;
    private static final String MODEL_PATH= "plant_seedling_classification_tflite.tflite";
    private static final String LABEL_PATH = "labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private Button btnDetectObject,mRandomButton;
    private ImageView imageViewResult;
    private AppCompatImageView mImageView;
    private List<Integer> mImages;
    private int mIndex;
    public static Intent getIntent(Context packageContext) {
        return new Intent(packageContext, MainActivity.class);
    }

    private void loadImages() {
        mImages = new ArrayList<>();
        mImages.add(R.drawable.test0);
        mImages.add(R.drawable.test672);
        mImages.add(R.drawable.test1937);
        mImages.add(R.drawable.test3643);
        mImages.add(R.drawable.test4529);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#1faa00\">" + getString(R.string.app_name) + "</font>"));
        setContentView(R.layout.activity_main);
        loadImages();
        windUpWidgets();

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageResource(mImages.get(mIndex));
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mImages.get(mIndex));
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

                imageViewResult.setImageBitmap(bitmap);

                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                textViewResult.setText(results.toString());
            }
        });
        mRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                mIndex = random.nextInt(mImages.size());
                mImageView.setImageResource(mImages.get(mIndex));
            }
        });


        initTensorFlowAndLoadModel();
    }

    private void windUpWidgets() {
        mRandomButton = findViewById(R.id.random_detect);
        mImageView = findViewById(R.id.imageView);
        imageViewResult = findViewById(R.id.imageViewResult);
        textViewResult = findViewById(R.id.textViewResult);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
        btnDetectObject = findViewById(R.id.button_detect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }


}
