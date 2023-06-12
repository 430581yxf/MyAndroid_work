package com.example.myapplicationui.myInterface;

//果然没用过,前面写的时候就不知道这个是啥
public interface OnViewPagerListener {
    void onInitComplete();
    /*释放的监听*/
    void onPageRelease(boolean isNext, int position);
    /*选中的监听以及判断是否滑动到底部*/
    void onPageSelected(int position, boolean isBottom);
}
