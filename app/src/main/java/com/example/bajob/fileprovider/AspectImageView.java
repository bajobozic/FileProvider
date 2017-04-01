package com.example.bajob.fileprovider;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by bajob on 4/1/2017.
 */

public class AspectImageView extends ImageView {


    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int aspectHeight = MeasureSpec.getSize(widthMeasureSpec) * 2 / 3;
        final int aspectHeightSpec = MeasureSpec.makeMeasureSpec(aspectHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, aspectHeightSpec);
    }
}

