package com.example.zhangyang.flipanim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


public class FlipAnimation implements Animation.AnimationListener {
    private boolean mShowingBack = false;
    private View mFront;
    private View mBack;
    private View mCurrentView;

    private Animation toMiddleAnim;
    private Animation fromMiddleAnim;

    public FlipAnimation(View front, View back) {
        mFront = front;//正面view
        mBack = back;//反面view
        mBack.setVisibility(View.GONE);
        mFront.setVisibility(View.VISIBLE);
        mCurrentView = mFront;//当前显示的view
        mShowingBack = false;//是否显示反面
        //建立scaleAnimation   x从1.0到0.0  y不变   中心点为Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f
        //也就是中心点是需要旋转的view的中心
        toMiddleAnim=new ScaleAnimation(1.0f,0.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        toMiddleAnim.setDuration(250);
        //建立scaleAnimation   x从0.0到1.0  y不变   中心点为Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f
        //也就是中心点是需要旋转的view的中心
        fromMiddleAnim=new ScaleAnimation(0.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        fromMiddleAnim.setDuration(250);
        toMiddleAnim.setAnimationListener(this);
        fromMiddleAnim.setAnimationListener(this);
    }

    public void flip() {
        Animation anim = mCurrentView.getAnimation();
        mCurrentView.clearAnimation();
        if (anim == null || anim.hasEnded()) {
            mCurrentView.setAnimation(toMiddleAnim);
            mCurrentView.startAnimation(toMiddleAnim);
            //
        }
    }

    public boolean ismShowingBack() {
        return mShowingBack;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == toMiddleAnim) {
            mCurrentView.clearAnimation();
            mCurrentView.setVisibility(View.GONE);
            if (mShowingBack) {
                mCurrentView = mFront;
            } else {
                mCurrentView = mBack;
            }

            mCurrentView.setVisibility(View.VISIBLE);
            mCurrentView.setAnimation(fromMiddleAnim);
            mCurrentView.startAnimation(fromMiddleAnim);
        } else {
            mShowingBack = !mShowingBack;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
}