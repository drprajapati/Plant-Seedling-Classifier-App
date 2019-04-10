package com.drprajapati.android.plantclassification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.drprajapati.android.plantclassification.ml.Classifier;
import com.drprajapati.android.plantclassification.ml.Result;
import com.drprajapati.android.plantclassification.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Classifier mClassifier;
    private int mIndex;
    private AppCompatImageView mImageView;
    private Button mUploadButton, mRandomButton;
    private List<Integer> mImages;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wireUpWidgets();
        loadImages();
        setUpClassifier();

        setListeners();

    }

    private void loadImages() {
        mImages = new ArrayList<>();
        mImages.add(R.drawable.test0);
        mImages.add(R.drawable.test672);
        mImages.add(R.drawable.test1937);
        mImages.add(R.drawable.test3643);
        mImages.add(R.drawable.test4529);
    }

    private void setUpClassifier() {
        try {
            mClassifier = new Classifier(this);
        } catch (Exception ex) {
            Toast.makeText(this, "Failed to create classifier...", Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "setUpClassifier(): Failed to create tflite model", ex);
        }
    }

    private void setListeners() {

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Bitmap image = BitmapFactory.decodeResource(getResources(),mImages.get(mIndex));
                Bitmap scaled = Bitmap.createScaledBitmap(image, 70,70,true);
                mImageView.setImageResource(mImages.get(mIndex));
                Result result = mClassifier.classify(scaled);
                mResultTextView.setText("Result: " + result.getNumber());
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
    }


    private void wireUpWidgets() {
        mImageView = findViewById(R.id.imageView);
        mUploadButton = findViewById(R.id.button_detect);
        mResultTextView = findViewById(R.id.text_result);
        mRandomButton = findViewById(R.id.random_detect);
    }

}
