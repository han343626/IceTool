package com.zlsk.zTool.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewHelper {
    public static void setViewWidthHeight(View view, int width, int height){
        if(view == null){
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(width >= 0){
            lp.width = width;
        }
        if(height >= 0){
            lp.height = height;
        }
        view.setLayoutParams(lp);
    }

    public static int getListViewHeight(ListView listView){
        if(listView == null){
            return  0;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return 0;
        }
        int th = 0;
        int h = 0;
        int c = listAdapter.getCount();
        for(int i = 0;i < c;i++){
            View view = listAdapter.getView(i, null, listView);
            view.measure(0, 0);
            h += view.getMeasuredHeight();
        }

        th = h + listView.getDividerHeight() * (c - 1);
        return th;
    }

    public static int getGridViewHeight(GridView gridView, int columnCount){
        if(gridView == null){
            return 0;
        }
        ListAdapter la = gridView.getAdapter();
        if(la == null){
            return 0;
        }
        int th = 0;
        int c = la.getCount();
        if(c == 0){
            return 0;
        }
        int rc = c / columnCount;
        if((c % columnCount) != 0){
            rc++;
        }

        View v = la.getView(0, null, gridView);
        v.measure(0, 0);
        int h = v.getMeasuredHeight();
        th = h * rc + gridView.getVerticalSpacing() * (rc - 1);

        return th;
    }
}
