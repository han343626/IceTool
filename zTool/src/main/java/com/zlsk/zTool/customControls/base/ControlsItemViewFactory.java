package com.zlsk.zTool.customControls.base;

import com.zlsk.zTool.customControls.controls.ControlDbSearch;
import com.zlsk.zTool.customControls.controls.ControlsDate;
import com.zlsk.zTool.customControls.controls.ControlsImage;
import com.zlsk.zTool.customControls.controls.ControlsLocation;
import com.zlsk.zTool.customControls.controls.ControlsSingleSelect;
import com.zlsk.zTool.customControls.controls.ControlsSingleSelectInput;
import com.zlsk.zTool.customControls.controls.ControlsText;
import com.zlsk.zTool.customControls.controls.ControlsVideo;

import java.util.HashMap;
import java.util.Map;

public class ControlsItemViewFactory {
    private static final Map<ControlsItemType, Class<?>> controlsMap;
    private static final ControlsItemViewFactory instance;

    static {
        instance = new ControlsItemViewFactory();

        controlsMap = new HashMap<>();
        controlsMap.put(ControlsItemType.IMAGE, ControlsImage.class);
        controlsMap.put(ControlsItemType.TEXT, ControlsText.class);
        controlsMap.put(ControlsItemType.TEXT_NUM, ControlsText.class);
        controlsMap.put(ControlsItemType.LONGTEXT, ControlsText.class);
        controlsMap.put(ControlsItemType.LOCATION, ControlsLocation.class);
        controlsMap.put(ControlsItemType.DATE, ControlsDate.class);
        controlsMap.put(ControlsItemType.SINGLE_SELECT, ControlsSingleSelect.class);
        controlsMap.put(ControlsItemType.MUL_SELECT, ControlsSingleSelect.class);
        controlsMap.put(ControlsItemType.VIDEO, ControlsVideo.class);
        controlsMap.put(ControlsItemType.SINGLE_SELECT_INPUT, ControlsSingleSelectInput.class);
        controlsMap.put(ControlsItemType.MUL_SELECT_INPUT, ControlsSingleSelectInput.class);
        controlsMap.put(ControlsItemType.DB_SEARCH_SELECT_INPUT, ControlDbSearch.class);
    }

    public static ControlsItemViewFactory getInstance() {
        return instance;
    }

    public ABaseControlItemView getInspectItemView(ControlsItemType itemType) {
        Class<?> itemViewClass = controlsMap.get(itemType);
        ABaseControlItemView itemView;
        try {
            itemView = (ABaseControlItemView) itemViewClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        return itemView;
    }
}
