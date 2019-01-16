package com.zlsk.zTool.chart;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.zlsk.zTool.R;
import com.zlsk.zTool.chart.model.ChartModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/9/12.
 */

public class BarAndLineChartActivity extends AChartBaseActivity{
    public static final String INTENT_DATA_CHART = "intent_data_chart";
    public static final String INTENT_DATA_CHART_NAME = "intent_data_chart_name";

    private LineChart lineChart;
    private BarChart barChart;

    private List<ChartModel> modelList = new ArrayList<>();
    private String labelName;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bar_line_chart;
    }

    @Override
    public String getTitleString() {
        return "统计";
    }

    @Override
    public String getActionString() {
        return null;
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void initUi() {
        super.initUi();
        initChart();
        setData();
        setBarData();
    }

    @Override
    protected void initData() {
        super.initData();

        modelList = (List<ChartModel>) getIntent().getSerializableExtra(INTENT_DATA_CHART);
        labelName = getIntent().getStringExtra(INTENT_DATA_CHART_NAME);
    }

    private void initChart(){
        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);
        setup(lineChart);
        setup(barChart);
    }

    private void setData() {
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    return modelList.get((int) value).getName();
                }catch (Exception exc){
                    return "";
                }

            }
        };

        lineChart.getXAxis().setValueFormatter(formatter);
        barChart.getXAxis().setValueFormatter(formatter);

        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int index = 0;index < modelList.size();index ++){
            values.add(new Entry(index, Float.parseFloat(modelList.get(index).getValue())));
        }

        LineDataSet lineDataSet = new LineDataSet(values,labelName);

        lineDataSet.setValueTextSize(12);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        lineChart.animateX(1000);
    }

    private void setBarData() {
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return modelList.get((int) value).getName();
            }
        };

        barChart.getXAxis().setValueFormatter(formatter);

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        for (int index = 0;index < modelList.size();index ++){
            values.add(new BarEntry(index, Float.parseFloat(modelList.get(index).getValue())));
        }

        BarDataSet barDataSet = new BarDataSet(values,labelName);
        barDataSet.setValueTextSize(12);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);

        barChart.setData(data);
        barChart.animateX(1000);
    }
}
