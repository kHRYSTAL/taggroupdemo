package me.khrystal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * usage: TagGroup 外部ScrollView 支持设置最大行数 通过TagGroup参数计算获得
 * author: kHRYSTAL
 * create time: 16/11/7
 * update time:
 * email: 723526676@qq.com
 */

public class TagScrollView extends ScrollView {
    public static int WITHOUT_MAX_LINES = 0;

    private int maxLines = WITHOUT_MAX_LINES;

    private int childHeight;

    private int childVerticalSpacing;

    public TagScrollView(Context context) {
        this(context, null);
    }

    public TagScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TagScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TagScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (maxLines != WITHOUT_MAX_LINES ) {
                heightSize = (maxLines + 1) * childHeight + (maxLines + 1) * childVerticalSpacing ;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            getLayoutParams().height = heightSize;
        } catch (Exception e) {
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public void setChildHeightAndSpacing(int childHeight, int childVerticalSpacing) {
        this.childHeight = childHeight;
        this.childVerticalSpacing = childVerticalSpacing;
    }

}
