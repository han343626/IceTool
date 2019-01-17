package com.ice.icetool;

import com.zlsk.zTool.utils.CommonToolUtil;
import com.zlsk.zTool.utils.system.CommonApplication;

/**
 * Created by IceWang on 2018/12/28.
 */

public class IceApplication extends CommonApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        CommonToolUtil.getInstance().init(getApplicationContext(),getPackageName(),null);
    }
}
