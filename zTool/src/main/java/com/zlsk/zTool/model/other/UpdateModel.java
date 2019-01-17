package com.zlsk.zTool.model.other;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by IceWang on 2019/1/14.
 */

public class UpdateModel {
    public String version_code;
    public String version_name;
    public String size;
    @SerializedName("infos")
    public List<UpdateInformationModel> updateInformation;
}
