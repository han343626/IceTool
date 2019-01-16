package com.zlsk.zTool.baiduMap.model;

import com.baidu.mapapi.map.Overlay;

import java.util.List;

/**
 * Created by IceWang on 2018/12/3.
 */

public class MarkOverlay {
    private boolean visible;
    private List<Overlay> overlayList;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<Overlay> getOverlayList() {
        return overlayList;
    }

    public void setOverlayList(List<Overlay> overlayList) {
        this.overlayList = overlayList;
    }

    public MarkOverlay(boolean visible, List<Overlay> overlayList) {
        this.visible = visible;
        this.overlayList = overlayList;
    }
}
