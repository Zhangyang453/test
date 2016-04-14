package com.example.zhangyang.flipanim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>
 *     <ul>
 * <li><strong></>1、自定义View，实现一个固定宽度的圆圈或者圆圈一部分。可以设置最多MAX_ARC_COUNT段圆圈，每一段可以设定不同的始终点和颜色。所有圆圈都是相同的宽度、相同的动画设置</strong></li>
 * </br>
 * <li><strong>2、View 的高度宽度一致</strong></li>
 * </ul>
 * </p>
 */
public class CustomizedCircleView extends View {
    private final static int MAX_ARC_COUNT = 4;
    private static final int ANIM_DURATION = 1000;//动画时间
    private static final float TOTALINT = 100;
    private Paint mPaints;
    private RectF mOvals;
    private final static int ANIM_STEP = 10;
    private final static int ANIM_INTERNAL = 100;
    private boolean isArcHeader = false;
    private final static int EDGETOFFSET = 0;//10;
    private Data[] mData = new Data[MAX_ARC_COUNT];
    private ValueAnimator animator;
    private boolean isStart;//动画是否已经开始,true表示动画中
    private boolean needAnim;//是否需要动画

    private void init() {
        int index = MAX_ARC_COUNT;
        while (index > 0) {
            index--;
            mData[index] = new Data();
            mData[index].color = -1;
        }
        mData[0].color = 0x88ff0000;
        mData[0].start = -90;
        mData[0].finalSweep = -360;

        mOvals = new RectF(EDGETOFFSET, EDGETOFFSET, 0, 0);
        mPaints = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaints.setStyle(Paint.Style.STROKE);
        mPaints.setStrokeWidth(54);

        animator = ValueAnimator.ofFloat(TOTALINT);
        animator.addUpdateListener(updateListener);
        animator.addListener(listener);
        animator.setDuration(ANIM_DURATION);

    }

    @Deprecated
    public void setStartAndSweep(int start, int finalSweep) {
        setStartAndSweepAndColor(0, start, finalSweep, mData[0].color);
    }

    public void startAnim(){
        if(animator != null && !isStart){
            animator.start();
        }
    }
    public void cancelAnim(){
        if(animator != null && isStart){
            animator.cancel();
        }
    }
    public boolean isStart() {
        return isStart;
    }

    public boolean isNeedAnim() {
        return needAnim;
    }

    public void setNeedAnim(boolean needAnim) {
        this.needAnim = needAnim;
    }

    /**
     * 设置圆圈是否需要动画
     */
    public void setCustomizedAnimation() {
        if(needAnim){
            startAnim();
        }else{
            invalidate();
        }
    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        private float lastValue;
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            if (lastValue > value) {
                lastValue = 0;
            }
            boolean isDirty = false;
            for (Data data : mData) {
                if (data.color != -1 && data.sweep != data.finalSweep) {
                    data.offset = data.finalSweep / TOTALINT * (value - lastValue);
                    data.sweep += data.offset;
                if (Math.abs(data.sweep) > Math.abs(data.finalSweep)) {
                    data.sweep = data.finalSweep;
                    }
                    isDirty = true;
                }
            }
            if (isDirty) {
                invalidate();
            }
            lastValue = value;
        }
    };

    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isStart = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isStart = false;
            for (Data data : mData) {
                data.sweep = data.finalSweep;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * 设置每一段圆圈的起始角度，覆盖角度，颜色，@param index由调用者自己控制
     *
     * @param index      大于0且小于MAX_ARC_COUNT
     * @param start      12:00 is -90, 3:00 is 0
     * @param finalSweep finalSweep>0 when clockwise, finalSweep<0 when counterclockwise
     */
    public void setStartAndSweepAndColor(int index, float start, float finalSweep, int color) {
        if (index >= MAX_ARC_COUNT) {
            throw new RuntimeException("index must be smaller than " + MAX_ARC_COUNT);
        }

        mData[index].color = color;
        mData[index].start = start;
        mData[index].finalSweep = finalSweep;
        if(needAnim){
            mData[index].sweep = 0;
        }else{
            mData[index].sweep = finalSweep;
        }
        setCustomizedAnimation();
    }

    /**
     * 删除圆圈的一部分，索引值小于MAX_ARC_COUNT
     */
    public void removeSweep(int index) {
        if (index >= MAX_ARC_COUNT) {
            throw new RuntimeException("index must be smaller than " + MAX_ARC_COUNT);
        }
        mData[index].color = -1;
    }

    @Deprecated
    public void setColor(int color) {
        setStartAndSweepAndColor(0, mData[0].start, mData[0].finalSweep, color);
    }

    /**
     * 设置圆圈是否需要半圆形的起始和终点
     */
    public void setArcHeader(boolean isArcHeader) {
        this.isArcHeader = isArcHeader;
    }

    /**
     * 设置圆圈的宽度，单位为px
     */
    public void setCircleWidth(int width) {
        mPaints.setStrokeWidth(width);
    }

    /**
     * 目前只实现xml需要的构造方法
     */
    public CustomizedCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static void drawArcs(Canvas canvas, final RectF oval, boolean useCenter,
                                 final Paint paint, float start, float sweep, float finalSweep, int edgetOffset, boolean isArcHeader) {
        RectF temp = new RectF(oval);
        float widthOffset = (paint.getStrokeWidth()) / 2;
        temp.left += widthOffset;
        temp.top += widthOffset;
        temp.right -= widthOffset;
        temp.bottom -= widthOffset;
        if (Math.abs(sweep) < 360 && isArcHeader) {
            float bigRadius = (temp.right - temp.left + 1) / 2;
            float offDegree = (float) (Math.toDegrees(Math.atan(widthOffset / bigRadius)));
            if (finalSweep < 0) {
                offDegree = -offDegree;
            }
            if (Math.abs(sweep) < Math.abs(offDegree * 2) && Math.abs(sweep) > 0) {
                sweep = offDegree * 2 + 1;
            }
            canvas.drawArc(temp, start + offDegree /*+ (finalSweep > 0 ? -1 : 1)*/, sweep - offDegree * 2 /*+ (finalSweep > 0 ? 1 : -1)*/, useCenter, paint);

            //draw start half-circle
            Paint tailPaint = new Paint(paint);
            tailPaint.setStyle(Paint.Style.FILL);

            double cx = bigRadius + bigRadius * Math.cos(Math.toRadians(start + offDegree)) + edgetOffset + widthOffset;
            double cy = bigRadius + bigRadius * Math.sin(Math.toRadians(start + offDegree)) + edgetOffset + widthOffset;

            temp.left = (int) Math.floor(cx - widthOffset);
            temp.top = (int) Math.floor(cy - widthOffset);
            temp.right = temp.left + widthOffset * 2;
            temp.bottom = temp.top + widthOffset * 2;
            canvas.drawArc(temp, start + offDegree, finalSweep > 0 ? -180 : 180, true, tailPaint);

            //draw end half-circle
            cx = bigRadius + bigRadius * Math.cos(Math.toRadians(start + sweep - offDegree)) + edgetOffset + widthOffset;
            cy = bigRadius + bigRadius * Math.sin(Math.toRadians(start + sweep - offDegree)) + edgetOffset + widthOffset;
            temp.left = (int) Math.floor(cx - widthOffset);
            temp.top = (int) Math.floor(cy - widthOffset);
            temp.right = temp.left + widthOffset * 2;
            temp.bottom = temp.top + widthOffset * 2;
            canvas.drawArc(temp, start + sweep - offDegree, finalSweep > 0 ? 180 : -180, true, tailPaint);
        } else {
            canvas.drawArc(temp, start, sweep, useCenter, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getSize(heightMeasureSpec) > 0) {
            size = Math.min(size, MeasureSpec.getSize(heightMeasureSpec));
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(widthMeasureSpec)));
        mOvals.right = getMeasuredWidth() - EDGETOFFSET;
        mOvals.bottom = getMeasuredHeight() - EDGETOFFSET;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Data data : mData) {
            if (data.color != -1) {
                mPaints.setColor(data.color);
                drawArcs(canvas, mOvals, false, mPaints, data.start, data.sweep, data.finalSweep, EDGETOFFSET, isArcHeader);
            }
        }
    }

    public static void setSweep(boolean needAnim, CustomizedCircleView view, int start, int[] colors, long[] values, int min_sweep) {
        if (null == colors || 0 == colors.length || null == values || 0 == values.length) {
            return;
        }
        int[] sweep = reCalculateSweep(values, min_sweep);
        int loopIndex = Math.min(sweep.length, colors.length);
        while (loopIndex > 0) {
            loopIndex--;
            if (360 == sweep[loopIndex]) {
                min_sweep = 0;
            }
            if (0 != sweep[loopIndex]) {
                view.setNeedAnim(needAnim);
                view.setStartAndSweepAndColor(loopIndex, start , -(-(sweep[loopIndex]) + min_sweep / 2), colors[loopIndex]);
                start += sweep[loopIndex];
            }
        }
    }

    private static int[] reCalculateSweep(long[] originls, int min_sweep) {
        if (null == originls || 0 == originls.length) {
            return null;
        }
        double[] values = new double[originls.length];
        double[] sweeps = new double[values.length];
        int[] sweepsInt = new int[values.length];
        double sum = 0;
        int j = 0;
        while (j < values.length) {
            values[j] = originls[j];
            sum += values[j];
            j++;
        }

        while (true) {
            boolean innerLoop = false;
            j = 0;
            while (j < values.length) {
                sweeps[j] = 360d * values[j] / sum;
                if (sweeps[j] < min_sweep && sweeps[j] > 0) {
                    double xx = (sum * min_sweep - 360 * values[j])
                            / (360d - min_sweep);

                    values[j] += xx;
                    sum += xx;
                    innerLoop = true;
                    break;
                }
                sweepsInt[j] = (int) sweeps[j];
                j++;
            }
            if (!innerLoop) {
                break;
            }
        }
        return sweepsInt;
    }

    static class Data {
        float start;
        float sweep;
        float finalSweep;
        int color;
        float offset;
    }
}