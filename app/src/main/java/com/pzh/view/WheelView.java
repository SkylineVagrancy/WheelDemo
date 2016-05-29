package com.pzh.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pzh.wheeldemo.R;

import java.util.ArrayList;

/**
 * Created by pzh on 16/5/25.
 */
public class WheelView extends ScrollView {
    private LinearLayout wheelContainer;
    private ArrayList<String> wheelLists;
    private Context context;
    private int lineColor;
    private int selectTextColor;
    private int lineWidth;
    private int nomalTextColor;
    private int currentIndex;
    private int viewWidth;
    private Paint paint;
    private int pading;
    private float textSize;
    private float textHeight;
    private ArrayList<TextView> textViewList;

    public OnWheelViewSelected getOnWheelViewSelected() {
        return onWheelViewSelected;
    }

    public void setOnWheelViewSelected(OnWheelViewSelected onWheelViewSelected) {
        this.onWheelViewSelected = onWheelViewSelected;
    }

    public int getVisibleCount() {
        return visibleCount;
    }

    public void setVisibleCount(int visibleCount) {
        this.visibleCount = visibleCount;
    }

    public int getNomalTextColor() {
        return nomalTextColor;
    }

    public void setNomalTextColor(int nomalTextColor) {
        this.nomalTextColor = nomalTextColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    private OnWheelViewSelected onWheelViewSelected;


    public int getvisibleCount() {
        return visibleCount;
    }

    public void setvisibleCount(int visibleCount) {
        this.visibleCount = visibleCount;
    }

    private int visibleCount;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }


    public ArrayList<String> getWheelLists() {
        return wheelLists;
    }


    public WheelView(Context context) {
        this(context, null, 0);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        visibleCount = typedArray.getInteger(R.styleable.WheelView_visibleCount, 3);
        lineColor = typedArray.getColor(R.styleable.WheelView_lineColor, 0x83cde6);
        selectTextColor = typedArray.getColor(R.styleable.WheelView_selectTextColor, 0x0288ce);
        nomalTextColor = typedArray.getColor(R.styleable.WheelView_nomalTextColor, 0xbbbbbb);
        lineColor = int2color(lineColor);
        nomalTextColor = int2color(nomalTextColor);
        selectTextColor = int2color(selectTextColor);
        pading = typedArray.getInteger(R.styleable.WheelView_padding, 15);
        textSize = typedArray.getInt(R.styleable.WheelView_textSize, 20);
        textHeight = typedArray.getDimension(R.styleable.WheelView_textHeight, 40);
        lineWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        wheelContainer = new LinearLayout(context);
        wheelContainer.setOrientation(LinearLayout.VERTICAL);
        textViewList = new ArrayList<>();
    }

    public void setWheelLists(ArrayList<String> wheelLists) {
        clear();
        this.wheelLists = wheelLists;
        if (wheelContainer == null) {
            return;
        }
        wheelContainer.removeAllViews();
        for (int i = 0; i < visibleCount / 2; i++) {
            wheelLists.add(i, "");
            wheelLists.add("");
        }
        initData();
    }

    private void clear() {
        wheelContainer.removeAllViews();
        this.removeAllViews();
        currentIndex = 0;
        textViewList.clear();
    }

    private void initData() {
        WheelView.this.smoothScrollTo(0, 0);
        for (String tem : wheelLists) {
            TextView tv = getItemView(tem);
            wheelContainer.addView(tv);
            textViewList.add(tv);
        }
        this.addView(wheelContainer);
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * visibleCount));
        this.smoothScrollTo(0, 0);
        refreshItemView(0);
    }

    private int itemHeight = 0;

    private TextView getItemView(String item) {
        TextView tv = new TextView(context);
        tv.setSingleLine(true);
        tv.setTextSize(textSize);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        tv.setText(item, TextView.BufferType.SPANNABLE);
        tv.setPadding(0, pading, 0, pading);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (itemHeight == 0) {
            itemHeight = getViewMeasureHeight(tv);
        }
        return tv;
    }

    private int de2px(float value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale);
    }

    private int getViewMeasureHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int ex = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, ex);
        return view.getMeasuredHeight();
    }

    Runnable scrollTask = new Runnable() {
        @Override
        public void run() {
            wheelContainer.post(new Runnable() {
                @Override
                public void run() {
                    int index = getScrollY() / itemHeight;
                    int off = getScrollY() % itemHeight;
                    if (off > itemHeight / 2) {
                        index += 1;
                        WheelView.this.smoothScrollTo(0, (int) textViewList.get(index).getY());
                    } else {
                        WheelView.this.smoothScrollTo(0, (int) textViewList.get(index).getY());
                    }
                    if(onWheelViewSelected!=null){
                        onWheelViewSelected.onSelected(index, wheelLists.get(currentIndex + visibleCount / 2));
                    }
                }
            });

        }
    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        refreshItemView(t);
    }

    private void refreshItemView(int t) {
        int position = t / itemHeight;
        int offset = t % itemHeight;
        if (offset > itemHeight / 2) {
            currentIndex = position + 1;
        } else {
            currentIndex = position;
        }
        textViewList.get(currentIndex + visibleCount / 2).setTextColor(selectTextColor);
        textViewList.get(currentIndex + visibleCount / 2).setTextSize(textSize);
        textViewList.get(currentIndex + visibleCount / 2).setAlpha(1f);
        textViewList.get(currentIndex + visibleCount / 2).setScaleX(1f);
        textViewList.get(currentIndex + visibleCount / 2).setScaleY(1f);

        for (int i = 1; i <= visibleCount / 2; i++) {
            textViewList.get(currentIndex + visibleCount / 2 + i).setAlpha((float) (1 - 0.3 * i));
            textViewList.get(currentIndex + visibleCount / 2 + i).setScaleX((float) (1 - 0.2 * i));
            textViewList.get(currentIndex + visibleCount / 2 - i).setScaleX((float) (1 - 0.2 * i));
            textViewList.get(currentIndex + visibleCount / 2 + i).setScaleY((float) (1 - 0.2 * i));
            textViewList.get(currentIndex + visibleCount / 2 - i).setScaleY((float) (1 - 0.2 * i));
            textViewList.get(currentIndex + visibleCount / 2 + i).setTextColor(nomalTextColor);
            textViewList.get(currentIndex + visibleCount / 2 - i).setAlpha((float) (1 - 0.3 * i));
            textViewList.get(currentIndex + visibleCount / 2 - i).setTextColor(nomalTextColor);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            WheelView.this.postDelayed(scrollTask, 50);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (viewWidth == 0) {
           viewWidth=w;
        }
        if (paint == null) {
            paint = new Paint();
            paint.setColor(lineColor);
            paint.setStrokeWidth(de2px(2));
        }
        Drawable background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine((viewWidth - lineWidth) / 2, visibleCount / 2 * itemHeight, viewWidth / 2 + lineWidth / 2, visibleCount / 2 * itemHeight, paint);
                canvas.drawLine((viewWidth - lineWidth) / 2, (visibleCount / 2 + 1) * itemHeight, viewWidth / 2 + lineWidth / 2, (visibleCount / 2 + 1) * itemHeight, paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        super.setBackgroundDrawable(background);
    }

    public int int2color(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        color = Color.rgb(red, green, blue);
        return color;
    }

    public interface OnWheelViewSelected {
        public void onSelected(int index, String value);
    }


}
