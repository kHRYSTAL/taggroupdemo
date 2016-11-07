package me.khrystal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/**
 * usage: TagGroup 外部ScrollView 支持设置最大行数 通过TagGroup参数计算获得
 * author: kHRYSTAL
 * create time: 16/11/7
 * update time:
 * email: 723526676@qq.com
 */

public class TagScrollView extends ScrollView{
    public static int WITHOUT_MAX_HEIGHT_VALUE = 0;

    private int maxHeight = WITHOUT_MAX_HEIGHT_VALUE;

    public TagScrollView(Context context) {
        super(context);
    }

    public TagScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TagScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TagScrollView);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE
                    && heightSize > maxHeight) {
                heightSize = maxHeight;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            getLayoutParams().height = heightSize;
        } catch (Exception e) {
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
