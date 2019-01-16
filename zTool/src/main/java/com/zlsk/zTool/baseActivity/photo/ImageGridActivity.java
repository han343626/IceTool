package com.zlsk.zTool.baseActivity.photo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.ImageGridAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.helper.AlbumHelper;
import com.zlsk.zTool.model.photo.ImageItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ImageGridActivity extends ATitleBaseActivity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private String titleName;
    private int MAX_SELECT_PHOTO_COUNT = 9;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	private boolean isVideoSelect = false;
    private static IBtnConfirmOnClickCallback mIBtnConfirmOnClickCallback;

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					ZToast.getInstance().show("最多选择" + MAX_SELECT_PHOTO_COUNT + (isVideoSelect ? "个视频" : "张图片"));
					break;

				default:
					break;
			}
			return true;
		}
	});

	@Override
	public int getContentViewId() {
		return R.layout.activity_ui_image_grid;
	}

	@Override
	public String getTitleString() {
		return titleName;
	}

	@Override
	public String getActionString() {
		return "完成";
	}

	@Override
	public void onActionButtonClicked(View view) {
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}
		for (int i = 0; i < list.size(); i++) {
			if(isVideoSelect){
				if (MediaCacheManager.videoAddress.size() < 9) {
					MediaCacheManager.videoAddress.add(list.get(i));
				}
			}else {
				if (MediaCacheManager.imageAddress.size() < 9) {
					MediaCacheManager.imageAddress.add(list.get(i));
				}
			}
		}
		mIBtnConfirmOnClickCallback.OnBtnConfirmClicked();
		finish();
	}

	@Override
	public boolean showRightImg() {
		return false;
	}

	@Override
	protected void initData() {
		super.initData();
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		titleName = getIntent().getStringExtra(Constant.INTENT_FIELD_VIDEO_ALBUM);
		isVideoSelect = titleName.equals("选择视频");
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		MAX_SELECT_PHOTO_COUNT = getIntent().getIntExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT,9);
	}

	@Override
	protected void initUi() {
		super.initUi();
		gridView = findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		adapter.setLimitCount(MAX_SELECT_PHOTO_COUNT);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				setActionName("完成" + "(" + count + "/" + MAX_SELECT_PHOTO_COUNT + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

		int selectedImageCount = isVideoSelect ? MediaCacheManager.videoAddress.size() : MediaCacheManager.imageAddress.size();
		if (selectedImageCount != 0) {
			adapter.setSelectTotal(selectedImageCount);
			setActionName("完成" + "(" + selectedImageCount + "/" + MAX_SELECT_PHOTO_COUNT + ")");
		}
	}

    public static void setIBtnConfirmOnClickCallback(IBtnConfirmOnClickCallback iOnClickBtnConfirmCallback) {
        mIBtnConfirmOnClickCallback = iOnClickBtnConfirmCallback;
    }

    public interface IBtnConfirmOnClickCallback {
        void OnBtnConfirmClicked();
    }
}
