package com.zlsk.zTool.utils.system;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/4/26.
 */

public class PermissionUtil {
    public static PermissionUtil.Builder build(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {

        private Activity mActivity;
        private List<String> permissionList;

        public Builder(@NonNull Activity activity) {
            mActivity = activity;
            permissionList = new ArrayList<>();
        }

        public Builder addPermission(@NonNull String permission) {
            if (!permissionList.contains(permission)) {
                permissionList.add(permission);
            }
            return this;
        }

        public List<String> checkPermission() {
            List<String> list = new ArrayList<>();
            for (String permission : permissionList) {
                if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission);
                }
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(mActivity, list.toArray(new String[list.size()]), 1);
            }
            return list;
        }

    }
}
