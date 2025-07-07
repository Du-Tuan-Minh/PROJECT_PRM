package com.example.project_prm.utils;

import android.content.res.Resources;

public class DimensionUtils {

    public static int dpToPx(Resources resources , int dp) {
        float density = resources.getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
