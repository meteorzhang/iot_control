package app.iot.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import app.iot.R;

/**
 * Created by danbo on 2020/4/24.
 */
public class ClickView extends View {
    // Handler消息
    private static final int FLUSH = 0;
    // 画笔
    private Paint mPaint;
    // 内圆宽度
    private int strokeWidth;
    // 圆心x
    private int cx;
    // 圆心y
    private int cy;
    // 半径
    private int radius;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == FLUSH) {// 更改参数状态
                flush();
                // 刷新 执行我们的绘制方法
                invalidate();
                // 继续验证透明度,只要不为0就一直发送，直到透明
                if (mPaint.getAlpha() != 0) {
                    handler.sendEmptyMessageDelayed(FLUSH, 50);
                }
            }
        }
    };

    public ClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        // 初始化画笔
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设置颜色
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        // 设置填充
        mPaint.setStyle(Paint.Style.FILL);
        // 设置内圆的宽度
        mPaint.setStrokeWidth(strokeWidth);
        // 设置透明度 0-255
        mPaint.setAlpha(255);

        // 初始值
        strokeWidth = 0;
        radius = 0;
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆环
        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    public void click(float fromX, float fromY) {
        cx = (int) fromX;
        cy = (int) fromY;
        // 初始化
        init();
        // 发送
        handler.sendEmptyMessage(FLUSH);
    }

    /**
     * 刷新状态
     */
    private void flush() {
        // 半径每次+10
        radius += 5;
        // 线条的宽度每次都是半径的四分之一
        strokeWidth = radius / 4;
        // 重新设置给画笔
        mPaint.setStrokeWidth(strokeWidth);
        // 颜色渐变,每次减少20的色值
        int nextAlpha = mPaint.getAlpha() - 20;
        // 避免等于负数
        if (nextAlpha < 20) {
            // 直接设置为透明
            nextAlpha = 0;
        }
        // 继续重新设置给画笔
        mPaint.setAlpha(nextAlpha);
    }
}
