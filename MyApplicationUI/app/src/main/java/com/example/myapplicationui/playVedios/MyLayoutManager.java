package com.example.myapplicationui.playVedios;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationui.myInterface.OnViewPagerListener;
//这个就是在网上找的,不知道怎设置滑动改变item的位置,现在来分析分析
//继承了布局
public class MyLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private int mDrift;//位移，用来判断移动方向

    //这个应该是已经封装的一个对象
    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;

    public MyLayoutManager(Context context) {
        super(context);
    }

    //这个构造函数传入三个参数一个是context我不知道是个啥,反正作用在context对象上,oritation就是布局方向水平或垂直,是否翻转对象,上下颠倒

    public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {

        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }
//当Item添加进来了  调用这个方法

    //view添加到屏幕的时候判断是不是第一个如果是第一个`就固定在屏幕上
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
//        播放视频操作 即将要播放的是上一个视频 还是下一个视频
        int position = getPosition(view);
        if (0 == position) {
            if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageSelected(getPosition(view), false);
            }

        }
    }
//就是绑定Listener
    public void setOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
        this.mOnViewPagerListener = mOnViewPagerListener;
    }

    //改变状态的时候到了
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
//            如果是闲置状态也就是不滑动,将视频固定在屏幕上
            case RecyclerView.SCROLL_STATE_IDLE:
                View view = mPagerSnapHelper.findSnapView(this);
                int position = getPosition(view);
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1);

                }
//                postion  ---回调 ----》播放


                break;
        }
        super.onScrollStateChanged(state);
    }
//看到这里我明白了我就说怎么recycelrview将item滑出去就暂停了原来是这个函数在作用
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
//暂停播放操作
        if (mDrift >= 0) {
            if (mOnViewPagerListener != null)
                mOnViewPagerListener.onPageRelease(true, getPosition(view));
        } else {
            if (mOnViewPagerListener != null)
                mOnViewPagerListener.onPageRelease(false, getPosition(view));
        }
    }

//这个就是获取向上或向下滑动的动作,牛啊牛啊
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }
//设置刻意上下滑动
    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
