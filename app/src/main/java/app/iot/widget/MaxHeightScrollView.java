package app.iot.widget;
//

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import app.iot.common.util.ScreenUtils;

/**
 * Created by danbo on 2020-01-13.
 */
public class MaxHeightScrollView extends ScrollView {

    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ScreenUtils.INSTANCE.dip2px(400f), MeasureSpec.AT_MOST));
    }
}