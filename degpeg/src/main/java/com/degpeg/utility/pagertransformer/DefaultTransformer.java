package com.degpeg.utility.pagertransformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

//https://medium.com/codex/33-viewpager2-transformers-for-your-android-uis-bbdab801eb2b
public class DefaultTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        //nothing
    }
}