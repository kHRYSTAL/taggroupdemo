package me.khrystal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/11/7
 * update time:
 * email: 723526676@qq.com
 */

public class TagGroup extends ViewGroup {

    private final int default_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_background_color = Color.WHITE;
    private final int default_input_hint_color = Color.argb(0x80, 0x00, 0x00, 0x00);
    private final int default_input_text_color = Color.argb(0xDE, 0x00, 0x00, 0x00);
    private final int default_checked_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_checked_text_color = Color.WHITE;
    private final int default_checked_background_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_pressed_background_color = Color.rgb(0xED, 0xED, 0xED);
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;

    /** The text to be displayed when the text of the INPUT tag is empty. */
    private CharSequence inputHint;

    /** The tag outline border color. */
    private int borderColor;

    /** The tag text color. */
    private int textColor;

    /** The tag background color. */
    private int backgroundColor;

    /** The  input tag hint text color. */
    private int inputHintColor;

    /** The input tag type text color. */
    private int inputTextColor;

    /** The checked tag outline border color. */
    private int checkedBorderColor;

    /** The check text color */
    private int checkedTextColor;

    /** The checked tag background color. */
    private int checkedBackgroundColor;

    /** The tag background color, when the tag is being pressed. */
    private int pressedBackgroundColor;

    /** The tag outline border stroke width, default is 0.5dp. */
    private float borderStrokeWidth;

    /** The tag text size, default is 13sp. */
    private float textSize;

    /** The horizontal tag spacing, default is 8.0dp. */
    private int horizontalSpacing;

    /** The vertical tag spacing, default is 4.0dp. */
    private int verticalSpacing;

    /** The horizontal tag padding, default is 12.0dp. */
    private int horizontalPadding;

    /** The vertical tag padding, default is 3.0dp. */
    private int verticalPadding;

    /** Listener used to dispatch tag change event. */
    private OnTagChangeListener mOnTagChangeListener;

    /** Listener used to dispatch tag click event. */
    private OnTagClickListener mOnTagClickListener;

    private int maxLines;

    /** Listener used to handle tag click event. */
    private InternalTagClickListener mInternalTagClickListener = new InternalTagClickListener();

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);
        default_horizontal_padding = dp2px(12.0f);
        default_vertical_padding = dp2px(3.0f);

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup, defStyleAttr, defStyleAttr);
        try {
            inputHint = a.getText(R.styleable.TagGroup_inputHint);
            borderColor = a.getColor(R.styleable.TagGroup_borderColor, default_border_color);
            textColor = a.getColor(R.styleable.TagGroup_textColor, default_text_color);
            backgroundColor = a.getColor(R.styleable.TagGroup_backgroundColor, default_background_color);
            inputHintColor = a.getColor(R.styleable.TagGroup_inputHintColor, default_input_hint_color);
            inputTextColor = a.getColor(R.styleable.TagGroup_inputTextColor, default_input_text_color);
            checkedBorderColor = a.getColor(R.styleable.TagGroup_checkedBorderColor, default_checked_border_color);
            checkedTextColor = a.getColor(R.styleable.TagGroup_checkedTextColor, default_checked_text_color);
            checkedBackgroundColor = a.getColor(R.styleable.TagGroup_checkedBackgroundColor, default_checked_background_color);
            pressedBackgroundColor = a.getColor(R.styleable.TagGroup_pressedBackgroundColor, default_pressed_background_color);
            borderStrokeWidth = a.getDimension(R.styleable.TagGroup_borderStrokeWidth, default_border_stroke_width);
            textSize = a.getDimension(R.styleable.TagGroup_textSize, default_text_size);
            horizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_horizontalSpacing, default_horizontal_spacing);
            verticalSpacing = (int) a.getDimension(R.styleable.TagGroup_verticalSpacing, default_vertical_spacing);
            horizontalPadding = (int) a.getDimension(R.styleable.TagGroup_horizontalPadding, default_horizontal_padding);
            verticalPadding = (int) a.getDimension(R.styleable.TagGroup_verticalPadding, default_vertical_padding);
            maxLines = a.getInteger(R.styleable.TagGroup_maxLines, 0);
        } finally {
            a.recycle();
        }

        setClipToPadding(true);

//      在末尾添加编辑框
        appendInputTag();

//      如果编辑框中有字 点击外部自动生成标签
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTag();
            }
        });
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (getParent() instanceof TagScrollView) {
                    TagScrollView scrollView = (TagScrollView) getParent();
                    float[] rowHeightAndSpacing = getRowHeightAndSpacing();
                    scrollView.setChildHeightAndSpacing((int) rowHeightAndSpacing[0], (int) rowHeightAndSpacing[1]);
                    if (maxLines != 0)
                        scrollView.setMaxLines(maxLines);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    /**
     * 暴露给外部ScrollView 支持 setMaxHeight;
     * @return
     */
    public float[] getRowHeightAndSpacing() {
        Log.d("TextSize", "" + textSize);
        return new float[]{verticalPadding * 3 + textSize, verticalSpacing};
    }

    public void submitTag() {
        final TagView inputTag = getInputTag();
        if (inputTag != null && inputTag.isInputAvailable()) {
//            改变样式为普通标签
            inputTag.endInput();
            if (mOnTagChangeListener != null) {
                mOnTagChangeListener.onAppend(TagGroup.this, inputTag.getText().toString());
            }
//            再添加一个样式为编辑的标签
            appendInputTag();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0; // 记录行数
        int rowWidth = 0; // 计算行宽
        int rowMaxHeight = 0; // 计算tag的最大高度

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) {
                    rowWidth = childWidth;
                    height += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight;
                    row ++;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += horizontalSpacing;
            }
        }

        height += rowMaxHeight;
        height += getPaddingTop() + getPaddingBottom();

        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) {
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                childLeft += width + horizontalSpacing;
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.tags = getTagsByArray();
        if (getInputTag() != null) {
            ss.input = getInputTag().getText().toString();
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof  SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setTags(ss.tags);
        TagView checkedTagView = getTagAt(ss.checkedPosition);
        if (checkedTagView != null)
            checkedTagView.setChecked(true);
        if (getInputTag() != null) {
            getInputTag().setText(ss.input);
        }
    }

    protected void appendInputTag() {
        appendInputTag(null);
    }

    protected void appendInputTag(String tag) {
        final TagView previousInputTag = getInputTag();
        if (previousInputTag != null) {
            throw new IllegalStateException("Already has a INPUT tag in group!");
        }

        final TagView newInputTag = new TagView(getContext(), TagView.STATE_INPUT, tag);
        newInputTag.setOnClickListener(mInternalTagClickListener);
        addView(newInputTag); // 添加view 自动重绘
    }

    public List<String> getTags() {
        final int count = getChildCount();
        final List<String> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final TagView tagView = getTagAt(i);
            if (tagView.mState == TagView.STATE_NORMAL) {
                tagList.add(tagView.getText().toString());
            }
        }
        return tagList;
    }

    public String[] getTagsByArray() {
        List<String> tags = getTags();
        return tags.toArray(new String[tags.size()]);
    }

    public void setTags(List<String> tagList) {
        setTags(true, tagList);
    }

    public void setTags(String ... tags) {
        setTags(true, tags);
    }

    public void setTags(boolean needRemoveAll, List<String> tagList) {
        setTags(needRemoveAll, tagList.toArray(new String[tagList.size()]));
    }

    public void setTags(boolean needRemoveAll, String ... tags) {
        if (needRemoveAll)
            removeAllViews();
        else
            removeView(getInputTag());
        for (final String tag : tags) {
            appendTag(tag);
        }
        appendInputTag();

    }

    /**
     * 获取编辑标签
     * @return
     */
    public TagView getInputTag() {
        final int inputTagIndex = getChildCount() - 1;
        final TagView inputTag = getTagAt(inputTagIndex);
        if (inputTag != null && inputTag.mState == TagView.STATE_INPUT) {
            return inputTag;
        } else
            return null;
    }

    public String getInputTagText() {
        final TagView inputTagView = getInputTag();
        if (inputTagView != null) {
            return inputTagView.getText().toString();
        }
        return null;
    }

    protected TagView getTagAt(int index) {
        return (TagView) getChildAt(index);
    }

    protected TagView getCheckedTag() {
        final int checkedTagIndex = getCheckedTagIndex();
        if (checkedTagIndex != -1) {
            return getTagAt(checkedTagIndex);
        }
        return null;
    }

    protected int getCheckedTagIndex() {
        final int count = getChildCount() - 1;
        for (int i = 0; i < count; i++) {
            final TagView tag = getTagAt(i);
            if (tag.isChecked) {
                return  i;
            }
        }
        return -1;
    }

    public void deleteTag(TagView tagView) {
        removeView(tagView);
        if (mOnTagChangeListener != null) {
            mOnTagChangeListener.onDelete(TagGroup.this, tagView.getText().toString());
        }
    }

    protected TagView getLastNormalTagView() {
        // 去除输入框
        final int lastNormalTagIndex = getChildCount() - 2;
        TagView lastNormalTagView = getTagAt(lastNormalTagIndex);
        return lastNormalTagView;
    }

    public void setOnTagClickListener(OnTagClickListener l) {
        mOnTagClickListener = l;
    }

    public void setOnTagChangeListener(OnTagChangeListener l) {
        mOnTagChangeListener = l;
    }

    protected void appendTag(CharSequence tag) {
        final TagView newTag = new TagView(getContext(), TagView.STATE_NORMAL, tag);
        newTag.setOnClickListener(mInternalTagClickListener);
        addView(newTag);
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }





    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(Parcel source) {
                        return new SavedState(source);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        int tagCount;
        String[] tags;
        int checkedPosition;
        String input;

        public SavedState(Parcel source) {
            super(source);
            tagCount = source.readInt();
            tags = new String[tagCount];
            source.readStringArray(tags);
            checkedPosition = source.readInt();
            input = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            tagCount = tags.length;
            out.writeInt(tagCount);
            out.writeStringArray(tags);
            out.writeInt(checkedPosition);
            out.writeString(input);
        }
    }

    /**
     * 中间件 内部进行样式处理后向外部回调onTagClick事件
     */
    class InternalTagClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            final TagView tag = (TagView) v;
            if (tag.mState == TagView.STATE_INPUT) {
                final TagView checkedTag = getCheckedTag();
                if (checkedTag != null) {
                    checkedTag.setChecked(false);
                }
            } else {
                final TagView checkTag = getCheckedTag();
                if (checkTag != null && checkTag != tag) {
                    checkTag.setChecked(false);
                }
                boolean check = !tag.isChecked;
                tag.setChecked(check);
                if (mOnTagClickListener != null) {
                    mOnTagClickListener.onTagClick(tag, tag.getText().toString(), !check);
                }
            }
        }
    }


    public interface OnTagChangeListener {
        void onAppend(TagGroup tagGroup, String tag);
        void onDelete(TagGroup tagGroup, String tag);
    }

    public interface OnTagClickListener {
        void onTagClick(TagView tagView, String tag, boolean isChecked);
    }

    public class TagView extends TextView {

        public static final int STATE_NORMAL = 1;
        public static final int STATE_INPUT  = 2;
        private int mState;
        private boolean isChecked = false;
        private boolean isPressed = false;

        private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private RectF mCornerRectF = new RectF();
        private RectF mHorizontalBlankFillRectF = new RectF();
        private RectF mVerticalBlankFillRectF = new RectF();

        private Rect mOutRect = new Rect();
        private Path mBorderPath = new Path();

        {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(borderStrokeWidth);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
        }


        public TagView(Context context, final  int state, CharSequence text) {
            super(context);
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
            setLayoutParams(new TagGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            ));

            setGravity(Gravity.CENTER);
            setText(text);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            mState = state;

            setClickable(true);
            setFocusable(state == STATE_INPUT);
            setFocusableInTouchMode(state == STATE_INPUT); // 支持软键盘编辑
            setHint(state == STATE_INPUT ? inputHint : null);
            setMovementMethod(state == STATE_INPUT ? ArrowKeyMovementMethod.getInstance() : null);

            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return state != STATE_INPUT;
                }
            });

            if (state == STATE_INPUT) {
                requestFocus();

//                点击软键盘回车
                setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_NULL
                                && (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN)) {
                            if (isInputAvailable()) {
                                endInput();
                                if (mOnTagChangeListener != null) {
                                    mOnTagChangeListener.onAppend(TagGroup.this, getText().toString().trim());
                                }
                                appendInputTag();
                            }
                            return true;
                        }
                        return false;
                    }
                });

                setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
                            TagView lastNormalTagView = getLastNormalTagView();
                            if (lastNormalTagView != null) {
                                // 点击删除键 如果没有选中 则对标签进行选中
                                // 如果已经选中 则直接删除
                                if (lastNormalTagView.isChecked) {
                                    removeView(lastNormalTagView);
                                    if (mOnTagChangeListener != null) {
                                        mOnTagChangeListener.onDelete(TagGroup.this, lastNormalTagView.getText().toString());
                                    }
                                } else {
                                    final TagView checkedTagView = getCheckedTag();
                                    if (checkedTagView != null) {
                                        checkedTagView.setChecked(false);
                                    }
                                    lastNormalTagView.setChecked(true);
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });

                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        final TagView checkedTagView = getCheckedTag();
                        if (checkedTagView != null) {
                            checkedTagView.setChecked(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            invalidatePaint();
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
            invalidatePaint();
        }

        private void endInput() {
            setFocusable(false);
            setFocusableInTouchMode(false);
            setHint(null);
            setMovementMethod(null);
            mState = STATE_NORMAL;
            invalidatePaint();
            requestLayout();
        }

        /**
         * 编辑框是否有字符 去除首位空格
         * @return true 获取到字符串 false 没有字符串
         */
        public boolean isInputAvailable() {
            return (!TextUtils.isEmpty(getText().toString().trim()))
                    && getText().length() > 0;
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
        }

        private void invalidatePaint() {
            if (mState == STATE_INPUT) {
                mBorderPaint.setColor(Color.TRANSPARENT);
                mBackgroundPaint.setColor(Color.TRANSPARENT);
                setHintTextColor(inputHintColor);
                setTextColor(inputTextColor);
            } else {
                mBorderPaint.setPathEffect(null);
                if (isChecked) {
                    mBorderPaint.setColor(checkedBorderColor);
                    mBackgroundPaint.setColor(checkedBackgroundColor);
                    setTextColor(checkedTextColor);
                } else {
                    mBorderPaint.setColor(borderColor);
                    mBackgroundPaint.setColor(backgroundColor);
                    setTextColor(textColor);
                }
                if (isPressed) {
                    mBackgroundPaint.setColor(pressedBackgroundColor);
                }
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int left = (int) borderStrokeWidth;
            int top = (int) borderStrokeWidth;
            int right = (int) (left + w - borderStrokeWidth * 2);
            int bottom = (int) (top + h - borderStrokeWidth * 2);
            int d = bottom - top;

            mCornerRectF.set(left, top, right, bottom);
            mBorderPath.reset(); // notice
            mBorderPath.addRoundRect(mCornerRectF, 20, 20, Path.Direction.CW);
            int l = (int) (d / 2.0f);
            mBorderPath.moveTo(left + l, top);
            mBorderPath.lineTo(right - l, top);

            mBorderPath.moveTo(left + l, bottom);
            mBorderPath.lineTo(right -l, bottom);

            mBorderPath.moveTo(left, top + l);
            mBorderPath.lineTo(left, bottom - l);

            mBorderPath.moveTo(right, top + l);
            mBorderPath.lineTo(right, bottom - l);

            mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
            mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(mHorizontalBlankFillRectF, mBackgroundPaint);
//          todo  2,3参数需要提取为自定义参数
            canvas.drawRoundRect(mCornerRectF, 20, 20, mBackgroundPaint);
            canvas.drawRect(mVerticalBlankFillRectF, mBackgroundPaint);
            canvas.drawPath(mBorderPath, mBorderPaint);
            super.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (mState == STATE_INPUT) {
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    getDrawingRect(mOutRect);
                    isPressed = true;
                    invalidatePaint();
//                    postInvalidate();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (!mOutRect.contains((int) event.getX(), (int) event.getY())) {
                        isPressed = false;
                        invalidatePaint();
//                        postInvalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    isPressed = false;
                    invalidatePaint();
                    postInvalidate();
                    break;
                }
            }
            return super.onTouchEvent(event);
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
            return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
        }

        /**
         * Solve edit text delete(backspace) key detect, see<a href="http://stackoverflow.com/a/14561345/3790554">
         * Android: Backspace in WebView/BaseInputConnection</a>
         */
        private class ZanyInputConnection extends InputConnectionWrapper {
            public ZanyInputConnection(android.view.inputmethod.InputConnection target, boolean mutable) {
                super(target, mutable);
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
                if (beforeLength == 1 && afterLength == 0) {
                    // backspace
                    return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                            && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }
}
