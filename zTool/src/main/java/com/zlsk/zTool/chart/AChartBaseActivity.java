package com.zlsk.zTool.chart;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.zlsk.zTool.baseActivity.ATitleBaseFragmentActivity;

/**
 * Created by IceWang on 2018/9/12.
 */

public abstract class AChartBaseActivity extends ATitleBaseFragmentActivity {
    protected Typeface mTf;

    protected void setup(Chart<?> chart){
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        BarLineChartBase mChart = (BarLineChartBase) chart;

        mChart.setDrawGridBackground(false);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTypeface(mTf);
        leftAxis.setTextSize(8f);
        leftAxis.setTextColor(Color.DKGRAY);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.DKGRAY);

        mChart.getAxisRight().setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setLabelCount(5);
        mChart.getXAxis().setGranularity(1f);
    }
}
