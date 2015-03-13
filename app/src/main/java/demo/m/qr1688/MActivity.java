package demo.m.qr1688;

/**
 * Created by jyu on 15-3-12.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class MActivity extends Activity {
    private static final String TAG = "TestScrollerActivity";
    LinearLayout lay1, lay2, lay0;
    private Scroller mScroller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScroller = new Scroller(this);
        lay1 = new MyLinearLayout(this);
        lay2 = new MyLinearLayout(this);

        lay1.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_btn_radio_material));
        lay2.setBackgroundColor(this.getResources().getColor(
                android.R.color.white));
        lay0 = new ContentLinearLayout(this);
        lay0.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        this.setContentView(lay0, p0);

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        p1.weight = 1;
        lay0.addView(lay1, p1);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        p2.weight = 1;
        lay0.addView(lay2, p2);
        MyButton btn1 = new MyButton(this);

        MyButton btn2 = new MyButton(this);
        btn1.setText("btn in layout1");
        btn2.setText("btn in layout2");
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mScroller.startScroll(0, 0, -30, -30, 500);
                //开始滚动，设置初始位置--》结束位置 & 持续时间
            }
        });
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mScroller.startScroll(20, 20, -50, -50, 500);
            }
        });
        lay1.addView(btn1);
        lay2.addView(btn2);
    }

    class MyButton extends Button {
        public MyButton(Context ctx) {
            super(ctx);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.d(TAG, this.toString() + " onDraw------");
        }
    }

    class MyLinearLayout extends LinearLayout {
        public MyLinearLayout(Context ctx) {
            super(ctx);
        }

        @Override
        public void computeScroll() {
            Log.d(TAG, this.toString() + " computeScroll-----------");
            if (mScroller.computeScrollOffset())//如果mScroller没有调用startScroll，这里将会返回false。
            {
                //因为调用computeScroll函数的是MyLinearLayout实例，
                //所以调用scrollTo移动的将是该实例的孩子，也就是MyButton实例
                scrollTo(mScroller.getCurrX(), 0);
                Log.d(TAG, "getCurrX = " + mScroller.getCurrX());

                //继续让系统重绘
                getChildAt(0).invalidate();
            }
        }
    }

    class ContentLinearLayout extends LinearLayout {
        public ContentLinearLayout(Context ctx) {
            super(ctx);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            Log.d(TAG, "contentview dispatchDraw");
            super.dispatchDraw(canvas);
        }

        @Override
        public void computeScroll() {
        }
    }
}