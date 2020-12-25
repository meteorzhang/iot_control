package app.iot.adapter;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.viewpager.widget.ViewPager;

import app.iot.R;
import app.iot.widget.tab.buildins.UIUtil;
import app.iot.widget.tab.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import app.iot.widget.tab.buildins.commonnavigator.abs.IPagerIndicator;
import app.iot.widget.tab.buildins.commonnavigator.abs.IPagerTitleView;
import app.iot.widget.tab.buildins.commonnavigator.indicators.LinePagerIndicator;
import app.iot.widget.tab.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeNavigatorAdapter extends CommonNavigatorAdapter {
    private List<String> titles;
    private ViewPager viewPager;

    private HomeNavigatorAdapter() {
    }

    public HomeNavigatorAdapter(@NotNull ViewPager viewPager, List<String> titles) {
        this.titles = titles;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
        titleView.setText(titles.get(index));
        titleView.setTextSize(16f);
        titleView.getPaint().setFakeBoldText(true);
        titleView.setNormalColor(context.getResources().getColor(R.color.tab_normal_color));
        titleView.setSelectedColor(context.getResources().getColor(R.color.tab_selected_color));
        titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));

        // reload custom layout
//        ColorTransitionPagerTitleView titleView = (ColorTransitionPagerTitleView) LayoutInflater.from(context).inflate(R.layout.common_navigator_item, null);
//        titleView.setText(titles.get(index));
//        titleView.setNormalColor(context.getResources().getColor(R.color.grey_light));
//        titleView.setSelectedColor(context.getResources().getColor(R.color.colorPrimary));
//        titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));

        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 3));
        indicator.setLineWidth(UIUtil.dip2px(context, 10));
        indicator.setRoundRadius(UIUtil.dip2px(context, 3));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(context.getResources().getColor(R.color.tab_indicator_color));
        return indicator;
    }
}
