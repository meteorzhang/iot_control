package app.iot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import app.iot.R;

public class NumberProgressView extends View {
    /**
     * 进度条画笔的宽度（dp）
     */
    private int paintProgressWidth = 3;

    /**
     * 文字百分比的字体大小（sp）
     */
    private int paintTextSize = 16;

    /**
     * 左侧已完成进度条的颜色
     */
    private int paintLeftColor = 0xffFB5E40;

    /**
     * 右侧未完成进度条的颜色
     */
    private int paintRightColor = 0xffaaaaaa;

    /**
     * 百分比文字的颜色
     */
    private int paintTextColor = 0xffffffff;

    /**
     * Contxt
     */
    private Context context;

    /**
     * 主线程传过来进程 0 - 100
     */
    private int progress;

    /**
     * 得到自定义视图的宽度
     */
    private int viewWidth;

    /**
     * 得到自定义视图的Y轴中心点
     */
    private int viewCenterY;

    /**
     * 画左边已完成进度条的画笔
     */
    private Paint paintLeft = new Paint();

    /**
     * 画右边未完成进度条的画笔
     */
    private Paint paintRight = new Paint();

    /**
     * 画中间的百分比文字的画笔
     */
    private Paint paintText = new Paint();

    /**
     * 要画的文字的宽度
     */
    private int textWidth;

    /**
     * 包裹文字的矩形
     */
    private Rect rect = new Rect();

    /**
     * 文字总共移动的长度（即从0%到100%文字左侧移动的长度）
     */
    private int totalMovedLength;

    public NumberProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 构造器中初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //设置进度条画笔的宽度
        int paintProgressWidthPx = dip2px(context, paintProgressWidth);

        //设置百分比文字的尺寸
        int paintTextSizePx = sp2px(context, paintTextSize);

        // 已完成进度条画笔的属性
        paintLeft.setColor(paintLeftColor);
        paintLeft.setStrokeWidth(paintProgressWidthPx);
        paintLeft.setAntiAlias(true);
        paintLeft.setStyle(Paint.Style.FILL);

        // 未完成进度条画笔的属性
        paintRight.setColor(paintRightColor);
        paintRight.setStrokeWidth(paintProgressWidthPx);
        paintRight.setAntiAlias(true);
        paintRight.setStyle(Paint.Style.FILL);

        // 百分比文字画笔的属性
        paintText.setColor(paintTextColor);
        paintText.setTextSize(paintTextSizePx);
        paintText.setAntiAlias(true);
        paintText.setTypeface(Typeface.DEFAULT);

    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getWidthAndHeight();
    }

    /**
     * 得到视图等的高度宽度尺寸数据
     */
    private void getWidthAndHeight() {

        //得到包围文字的矩形的宽高
        paintText.getTextBounds("000%", 0, "000%".length(), rect);
        textWidth = rect.width();
        //textBottomY = viewCenterY + rect.height() + dip2px(context, 8);

        //得到自定义视图的高度
        int viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        viewCenterY = viewHeight / 2;
        totalMovedLength = viewWidth - textWidth;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //得到float型进度
        float progressFloat = progress / 100.0f;

        //当前文字移动的长度
        float currentMovedLength = totalMovedLength * progressFloat;

        //画左侧已经完成的进度条，长度为从Veiw左端到文字的左侧
        canvas.drawLine(0, viewCenterY, currentMovedLength, viewCenterY, paintLeft);

        GradientDrawable bitmap = (GradientDrawable) context.getResources().getDrawable(R.drawable.shape_accent);

        //画右侧未完成的进度条，这个进度条的长度不是严格按照百分比来缩放的，因为文字的长度会变化，所以它的长度缩放比例也会变化
        if (progress < 10) {
            canvas.drawLine(currentMovedLength + textWidth * 0.5f, viewCenterY, viewWidth, viewCenterY, paintRight);
            canvas.drawBitmap(convertToBitmap(bitmap, dip2px(context, 32), dip2px(context, 20)), currentMovedLength, dip2px(context, 4), paintText);


        } else if (progress < 100) {
            canvas.drawLine(currentMovedLength + textWidth * 0.75f, viewCenterY, viewWidth, viewCenterY, paintRight);
            canvas.drawBitmap(convertToBitmap(bitmap, dip2px(context, 38), dip2px(context, 20)), currentMovedLength, dip2px(context, 4), paintText);

        } else {
            canvas.drawLine(currentMovedLength + textWidth, viewCenterY, viewWidth, viewCenterY, paintRight);
        }

        //画文字(注意：文字要最后画，因为文字和进度条可能会有重合部分，所以要最后画文字，用文字盖住重合的部分)
        canvas.drawText(" " + progress + "%", currentMovedLength, dip2px(context, 20), paintText);

    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    /**
     * @param progress 外部传进来的当前进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}