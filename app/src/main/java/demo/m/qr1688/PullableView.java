package demo.m.qr1688;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Random;

/**
 * Created by jyu on 15-3-10.
 */
public class PullableView extends ViewGroup {

    final Random r = new Random();
    float last_scroll_y;
    private View mHeaderView;
    private View mContentView;
    private float touch_y;//当前touch的y
    private int touch_y_0;//第一次touch的y
    private int y_0;//header view 初始y
    private int headHeight;
    private Scroller mScroller;
    private int cur_y;
    Runnable runnable = new Runnable() {


        @Override
        public void run() {
            if (mScroller.computeScrollOffset() && !mScroller.isFinished()) {

                float d = mScroller.getCurrY() - last_scroll_y;
                cur_y += d;
                last_scroll_y = mScroller.getCurrY();

                if (d != 0) {
                    scrollBy(0, (int) -d);
                    //mHeaderView.offsetTopAndBottom((int)d);
                    //mContentView.offsetTopAndBottom((int)d);
                    // y_0 += d;
                    //cy = mScroller.getCurrY();

                    postInvalidate();
                    //   Log.d("saa", "... " + mHeaderView.getY() + ":" + getY());

                }


                post(this);

            }
        }
    };
    private ValueAnimator animator;
    private int refreshStatus;
    private boolean headMoved = true;

    public PullableView(Context context) {
        super(context);
    }

    public PullableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        if (mHeaderView == null) {
            mHeaderView = getChildAt(0);
        }

        if (mContentView == null) {
            mContentView = getChildAt(1);
        }
        mScroller = new Scroller(getContext());

        super.onFinishInflate();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_y = event.getY();
                last_scroll_y = 0;

                // unfinished = mScroller.getFinalY() - mScroller.getCurrY();
                //如果之前的下拉没有结束则取消
                mScroller.forceFinished(true);

                touch_y_0 = (int) touch_y;
                if (refreshStatus == 2)
                    refreshStatus = 0;


                return true;
            case MotionEvent.ACTION_UP:
                final int ty = (int) event.getY();

                float distance = ty - touch_y_0;
                // cur_y+=distance;
                //  touch_y_0 = ty;

                int d = cur_y;
                Log.d("saa", "uping ..." + d);


                if (distance > headHeight && refreshStatus < 2) {
                    // keepHeader();
                    d = d - headHeight;

                    Log.d("saa", "start scroll with header..." + d);
                    startScroll(d);


                    headMoved = false;

                } else {
                    startScroll(d);

                    Log.d("saa", "start scroll ..." + d);

                }
                invalidate();
                return true;

            //  moveHeaderUp();
            case MotionEvent.ACTION_MOVE:

                int m = (int) (event.getY() - touch_y);

                cur_y += m;

                mHeaderView.offsetTopAndBottom(m);
                mContentView.offsetTopAndBottom(m);
                touch_y = event.getY();


                invalidate();
                Log.d("saa", "cur y..." + cur_y);
                if (touch_y - touch_y_0 > headHeight && refreshStatus == 0) {
                    refresh();
                }

                return true;


        }

        return super.dispatchTouchEvent(event);
    }

    public void computeScroll() {


    }

    private void startScroll(int d) {


        removeCallbacks(runnable);

//        if (animator != null) animator.cancel();
//        animator = ValueAnimator.ofFloat(0f, -d);
//        animator.setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            float last_y;
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//                Float y = (Float) animation.getAnimatedValue();
//                float d = y - last_y;
//                cur_y -= d;
//                last_y = y;
//
//                if (d != 0) {
//                    scrollBy(0, (int) -d);
//                    //cy = mScroller.getCurrY();
//
//                    Log.d("saa", "ann... " + cur_y + ":" + d);
//
//                    postInvalidate();
//                }
//            }
//        });
//        animator.start();

        mScroller.startScroll(0, 0, 0, -d, 1000);

        //   post(runnable);
    }

    private void refresh() {


        if (refreshStatus == 0) {


            refreshStatus = 1;//刷新中

            new AsyncTask<Void, Void, Void>() {
                boolean move;

                @Override
                protected Void doInBackground(Void[] params) {

                    try {
                        Log.d("saa", "refreshing............");
                        Thread.sleep(r.nextInt(800) + 100);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshStatus = 2;

                    if (!headMoved) {

                        while (!move) {

                            if (!headMoved && !mScroller.computeScrollOffset() && mScroller.isFinished()) {
                                Log.d("saa", "move heading......");
                                headMoved = true;
                                move = true;
                                break;
                            }
                        }

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (move) {
                        last_scroll_y = 0;
                        startScroll(headHeight);

                        invalidate();
                    }


                }
            }.execute();

            //updating tip
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        MarginLayoutParams hlp = (MarginLayoutParams) mHeaderView.getLayoutParams();


        int left = getPaddingLeft() + hlp.leftMargin;
        int right = left + mHeaderView.getMeasuredWidth();
        int top = -hlp.topMargin - mHeaderView.getMeasuredHeight();
        int bottom = top + mHeaderView.getMeasuredHeight();

        headHeight = mHeaderView.getMeasuredHeight() + hlp.topMargin + hlp.bottomMargin;


        mHeaderView.layout(left, top, right, bottom);

        MarginLayoutParams clp = (MarginLayoutParams) mContentView.getLayoutParams();

        left = getPaddingLeft() + clp.leftMargin;
        right = left + mContentView.getMeasuredWidth();
        top = getPaddingTop() + clp.topMargin;
        bottom = top + mContentView.getMeasuredHeight();

        mContentView.layout(left, top, right, bottom);

        Log.d("saa", "onLayout.................");

        y_0 = (int) mHeaderView.getY();

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PullableLayout(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new PullableLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new PullableLayout(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof PullableLayout;
    }

    public static class PullableLayout extends MarginLayoutParams {


        public PullableLayout(Context c, AttributeSet attrs) {
            super(c, attrs);

        }

        public PullableLayout(int width, int height) {
            super(width, height);
        }

        public PullableLayout(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
