package com.ice.icetool.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.ice.icetool.R;
import com.ice.icetool.model.ShopModel;
import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.other.AvoidDoubleClickListener;
import com.zlsk.zTool.customControls.other.SwitchView;
import com.zlsk.zTool.customControls.other.SwitchViewII;
import com.zlsk.zTool.utils.animation.AnimationHelper;
import com.zlsk.zTool.utils.string.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IceWang on 2019/1/4.
 */

public class SwitchAndShop extends ATitleBaseActivity{
    private RelativeLayout rootView;
    private ImageView img_slide;

    private List<ShopModel> shopModelList;
    private int[] typeOneCounts ;
    private Map<ShopModel.TypeOne,int[]> counts;

    private SwitchViewII switchViewII;

    private ShopModel.TypeOne typeOne = ShopModel.TypeOne.breakfast;
    private ShopModel.TypeTwo typeTwo = ShopModel.TypeTwo.A;
    private Adapter shopListAdapter;
    private Adapter selectListAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_switch_and_shop;
    }

    @Override
    public String getTitleString() {
        return getIntent().getStringExtra("title");
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
    protected void initUi() {
        super.initUi();
        rootView = findViewById(R.id.rootView);
        initSwitchView();
        initListView();
        initSlide();
    }

    @Override
    protected void initData() {
        super.initData();
        Float[] prices = new Float[]{8.5f,10.0f,12.5f,20.0f,50.0f,88.0f,100.0f};
        String[] units = new String[]{"个","杯","碗","盘","两"};

        shopModelList = new ArrayList<>();
        typeOneCounts = new int[]{0,0,0};
        counts = new HashMap<>();
        for (int index = 0; index < 100; index++) {
            ShopModel shopModel = new ShopModel();
            shopModel.id = index;
            shopModel.typeOne = ShopModel.TypeOne.values()[NumberUtil.getRandom(3,0)];
            shopModel.typeTwo = ShopModel.TypeTwo.values()[NumberUtil.getRandom(4,0)];
            shopModel.name = shopModel.typeOne.name() + "_" + shopModel.typeTwo.name() + "_" + index;
            shopModel.price = prices[NumberUtil.getRandom(prices.length - 1,0)];
            shopModel.unit = units[NumberUtil.getRandom(units.length - 1,0)];
            shopModel.remain = NumberUtil.getRandom(50,0);
            shopModel.count = 0;
            shopModelList.add(shopModel);

            typeOneCounts[shopModel.typeOne.ordinal()] ++;

            if(!counts.containsKey(shopModel.typeOne)){
                counts.put(shopModel.typeOne,new int[]{0,0,0,0});
            }else {
                counts.get(shopModel.typeOne)[shopModel.typeTwo.ordinal()] ++;
            }
        }

        selectListAdapter = new Adapter(context);
        selectListAdapter.setSelect(true);
        shopListAdapter = new Adapter(context);
    }

    private void initSwitchView(){
        LinearLayout layout_container = findViewById(R.id.layout_container);

        SwitchView switchView = new SwitchView(this,layout_container,new String[]{"早餐","中餐","晚餐"}, (position) -> {
            typeOne = ShopModel.TypeOne.values()[position];
            if(switchViewII != null){
                if(counts.containsKey(typeOne)){
                    switchViewII.setTotal(counts.get(typeOne));
                }else {
                    switchViewII.setTotal(new int[]{0,0,0,0});
                }
            }

            setShopListView();
        });
        switchView.setTotal(typeOneCounts);

        switchViewII = new SwitchViewII(this,layout_container,new String[]{"餐馆A","餐馆B","餐馆C","餐馆D"}, (position) -> {
            typeTwo = ShopModel.TypeTwo.values()[position];
            setShopListView();
        });
        switchViewII.setTotal(counts.get(typeOne));
    }

    private void initListView(){
        ListView shopListView = findViewById(R.id.shopListView);
        shopListView.setAdapter(shopListAdapter);
    }

    private void initSlide(){
        img_slide = findViewById(R.id.img_slide);
        SlidingDrawer slidingDrawer = findViewById(R.id.slidingDrawer);
        slidingDrawer.setOnDrawerCloseListener(() -> {
            img_slide.setImageResource(R.drawable.slide_up);
            shopListAdapter.notifyDataSetChanged();
        });
        slidingDrawer.setOnDrawerOpenListener(() -> {
            img_slide.setImageResource(R.drawable.slide_down);
            setSelectListView();
        });

        ListView listView_result = findViewById(R.id.listView_result);
        listView_result.setAdapter(selectListAdapter);
    }

    private void setShopListView(){
        List<ShopModel> result = new ArrayList<>();
        for (ShopModel shopModel : shopModelList){
            if(shopModel.typeOne == typeOne && shopModel.typeTwo == typeTwo){
                result.add(shopModel);
            }
        }
        shopListAdapter.setDataList(result);
    }

    private void setSelectListView(){
        List<ShopModel> result = new ArrayList<>();
        for (ShopModel shopModel : shopModelList){
            if(shopModel.count > 0){
                result.add(shopModel);
            }
        }
        selectListAdapter.setDataList(result);
    }

    class Adapter extends CommonAdapter<ShopModel>{
        private boolean isSelect = false;

        public Adapter(Context context) {
            super(context);
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        protected void initializeView(ViewHolder holder, int position) {
            ShopModel shopModel = dataList.get(position);

            TextView tv_name = holder.findViewById(R.id.tv_name);
            TextView tv_price = holder.findViewById(R.id.tv_price);
            TextView tv_remain = holder.findViewById(R.id.tv_remain);
            TextView tv_count = holder.findViewById(R.id.tv_count);

            tv_name.setText(shopModel.name);
            tv_price.setText("单        价："+ shopModel.price + " ￥ / 1 " + shopModel.unit);
            tv_remain.setText("剩余数量：" + shopModel.remain + " " + shopModel.unit);
            tv_count.setText(String.valueOf(shopModel.count));

            holder.findViewById(R.id.sub).setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void OnClick(View view) {
                    update(shopModel,shopModel.count - 1);
                }
            });

            holder.findViewById(R.id.add).setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void OnClick(View view) {
                    update(shopModel,shopModel.count + 1);
                    AnimationHelper.animation(context,rootView,view,img_slide,((ImageView)findViewById(R.id.img_add)).getDrawable());
                }
            });
        }

        private void update(ShopModel shopModel,float count){
            if(count > -1 && count <= shopModel.remain){
                shopModel.count = count;
                if(isSelect && count == 0){
                    dataList.remove(shopModel);
                }
                notifyDataSetChanged();
            }
        }

        @Override
        protected int getConvertViewId() {
            return R.layout.item_list_shop;
        }
    }
}
