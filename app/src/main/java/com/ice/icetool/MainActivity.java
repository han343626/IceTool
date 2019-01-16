package com.ice.icetool;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.other.AvoidDoubleClickListener;
import com.zlsk.zTool.helper.YamlConfigHelper;
import com.zlsk.zTool.utils.system.PermissionUtil;

import java.util.List;
import java.util.Map;

public class MainActivity extends ATitleBaseActivity {
    @Override
    protected boolean supportSlideBack() {
        return false;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public String getTitleString() {
        return "ice tool";
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
    public boolean showBackButton() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        PermissionUtil.build(this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                addPermission(Manifest.permission.INTERNET).
                checkPermission();
    }

    @Override
    protected void initUi() {
        super.initUi();
        List<Map<String,Object>> mapList = (List<Map<String,Object>>) YamlConfigHelper.getInstance().getConfig("config").get("menu");

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new Adapter(context,mapList));
    }

    class Adapter extends CommonAdapter<Map<String,Object>>{

        public Adapter(Context context, List<Map<String, Object>> dataList) {
            super(context, dataList);
        }

        @Override
        protected void initializeView(ViewHolder holder, int position) {
            Map<String,Object> map = dataList.get(position);

            TextView tv_name = holder.findViewById(R.id.tv_name);
            TextView tv_description = holder.findViewById(R.id.tv_description);

            tv_name.setText(map.get("name") + "");
            tv_description.setText(map.get("description") + "");

            holder.getConvertView().setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void OnClick(View view) {
                    try {
                        Intent intent = new Intent(context.getApplicationContext(), Class.forName(String.valueOf(map.get("activity"))));
                        intent.putExtra("title",(String)map.get("name"));
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        protected int getConvertViewId() {
            return R.layout.item_list_menu;
        }
    }
}
