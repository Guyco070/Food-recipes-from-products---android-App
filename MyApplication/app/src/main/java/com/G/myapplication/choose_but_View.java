package com.G.myapplication;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class choose_but_View extends LinearLayout {
    public choose_but_View(Context context) {
        this(context, null);

    }

    public choose_but_View(Context context,
                        @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public choose_but_View(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }



    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.choose_but, this, true);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
