package com.drprajapati.android.plantclassification.ml;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private String mLabel;

    private List<String> mLabels;

    public Result(float[] result) {
        loadLabels();
        mLabel = mLabels.get(argmax(result));
    }

    private void loadLabels() {
        mLabels = new ArrayList<>();
        mLabels.add("Black-grass");
        mLabels.add("Charlock");
        mLabels.add("Cleavers");
        mLabels.add("Common Chickweed");
        mLabels.add("Common Wheat");
        mLabels.add("Fat Hen");
        mLabels.add("Loose Silky-Bent");
        mLabels.add("Maize");
        mLabels.add("Scentless Mayweed");
        mLabels.add("Shepherds Purse");
        mLabels.add("Small Flower Cranesbil");
        mLabels.add("Sugar beet");

    }

    public String getNumber() {
        return mLabel;
    }

    private int argmax(float[] probs) {
        int maxIdx = -1;
        float maxProb = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
