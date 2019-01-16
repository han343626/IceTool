package com.zlsk.zTool.constant;

/**
 * Created by IceWang on 2018/5/23.
 */

public class Constant {
    public static final String IMAGE_SPLIT_STRING = "IMAGE_SPLIT_STRING";
    public static final String SELECT_VALUES_SPLIT_STRING = "SELECT_VALUES_SPLIT_STRING";

    public static final String INTENT_FIELD_IMAGE_PATH = "intent_field_image_path";
    public static final String INTENT_FIELD_CAN_EDIT = "intent_field_can_edit";
    public static final String INTENT_FIELD_CURRENT_POSITION = "intent_field_current_position";
    public static final String INTENT_FIELD_BACK_TO_IMAGE = "intent_field_back_to_image";
    public static final String INTENT_FIELD_IMAGE_LIMIT_COUNT = "intent_field_image_limit_count";
    public static final String INTENT_FIELD_VIDEO_ALBUM = "intent_field_video_album";

    private static final int BASE = 2000;

    public static final int SELECT_PHOTO = BASE + 1;
    public static final int EDIT_PHOTO = BASE + 2;
    public static final int CODE_TAKE_PHOTO = BASE + 3;
    public static final int CODE_TAKE_VIDEO = BASE + 4;
    public static final int CODE_EDIT_VIDEO = BASE + 5;
    public static final int CODE_SELECT_VIDEO = BASE + 6;
    public static final int CODE_SELECT_PHOTO_FROM_SCANNER = BASE + 7;
}
