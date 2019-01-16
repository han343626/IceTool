package com.zlsk.zTool.customControls.svg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jaredrummler.android.widget.AnimatedSvgView;
import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.svg.model.SvgModel;

/**
 * Created by IceWang on 2018/9/12.
 */

public class SvgView {
    private Context context;
    private SvgModel svgModel;
    private View svgView;

    public View getSvgView() {
        return svgView;
    }

    public SvgView(Context context, SvgModel svgModel) {
        this.context = context;
        this.svgModel = svgModel;
        initUi();
    }

    private void initUi(){
        svgView = LayoutInflater.from(context).inflate(R.layout.custom_svg_view,null);

        AnimatedSvgView animatedSvgView = svgView.findViewById(R.id.animated_svg_view);

        animatedSvgView.setGlyphStrings(svgModel.getGlyphs());
        animatedSvgView.setFillColors(svgModel.getColors());
        animatedSvgView.setViewportSize(svgModel.getWidth(), svgModel.getHeight());
        animatedSvgView.setTraceColors(svgModel.getColors());
        animatedSvgView.setTraceResidueColor(0x32000000);
        animatedSvgView.rebuildGlyphData();
        animatedSvgView.start();
    }
}
