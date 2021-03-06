package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopStoreBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarListAdapter extends MyHaveHeadViewRecyclerAdapter<JSONObject> {


    private String free_freight;
    private JSONArray voucher;
    private JSONArray mansong;
    private String freeFreight;
    private boolean editstatus = true;
    private boolean isSelectAll;
    private CarListListener carListListener;
    private boolean isNotifyAdapter=false;
    private boolean initUnselectState=false;


    public CarListAdapter(Context context) {
        super(context, R.layout.item_car_list);

    }

    @Override
    public void convert(RecyclerHolder holder, final JSONObject obj,final int position) {
        String store_name = obj.optString("store_name");
        String store_id = obj.optString("store_id");
        if (obj.has("free_freight")) {
            free_freight = obj.optString("free_freight");
        }
        if (obj.has("voucher")) {
            voucher = obj.optJSONArray("voucher");
        }
        if (obj.has("mansong")) {
            mansong = obj.optJSONArray("mansong");
        }
        if (obj.has("free_freight")) {
            freeFreight = obj.optString("free_freight");
        }
        RecyclerView rcl = holder.getView(R.id.rcl);
        if (rcl.getAdapter() == null) {
            final List<JSONObject> childList = new ArrayList<JSONObject>();
            final CarListChildAdapter childAdapter = new CarListChildAdapter(context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            //自适应自身高度
            linearLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(linearLayoutManager);
            final JSONArray goods = obj.optJSONArray("goods");
            for (int i = 0; i < goods.length(); i++) {
                childList.add(goods.optJSONObject(i));
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("store_name", store_name);
                jsonObject.put("store_id", store_id);
                childList.add(0, jsonObject);
                childAdapter.setHead_layoutId(R.layout.item_chid_car_list_head);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (editstatus) {
                childAdapter.editStatus();
            } else {
                childAdapter.finishStatus();
            }
            childAdapter.setDatas(childList);
            childAdapter.setCarListChildListener(new CarListChildAdapter.CarListChildListener() {
                @Override
                public void onUncheckGoodItem(JSONObject goodObject, ShopStoreBean store,int childPosition) {
                    carListListener.onUncheckItem(goodObject, store, childPosition,position);
                }

                @Override
                public void onUncheckStoreItem(ShopStoreBean store) {
                    carListListener.onUncheckStoreItem(store);
                }

                @Override
                public void onCheckStoreItem(ShopStoreBean store) {
                    carListListener.onCheckStoreItem(store);
                }

                @Override
                public void onCheckGoodItem(JSONObject goodObject, ShopStoreBean storeBean,int childPosition) {
                    carListListener.onCheckGoodItem(goodObject, storeBean, childPosition,position);
                }

                @Override
                public void onUnCheckGoodAndunCheckStoreItem(ShopStoreBean storeBean) {
                    carListListener.onUnCheckGoodAndUnCheckStoreItem(storeBean);
                }

                @Override
                public void onModifyItemNum(JSONObject goodObject) {
                    carListListener.onModifyGoodNum(goodObject);
                }

                @Override
                public void onGotoGoodDetail(String goodsid) {
                    carListListener.onGoodDetail(goodsid);
                }
            });
            rcl.setAdapter(childAdapter);
        } else if (rcl.getAdapter() instanceof CarListChildAdapter) {
            CarListChildAdapter childAdapter = (CarListChildAdapter) rcl.getAdapter();
            List<JSONObject> childList = childAdapter.getDatas();
            childList.clear();
            JSONArray goods = obj.optJSONArray("goods");
            for (int i = 0; i < goods.length(); i++) {
                childList.add(goods.optJSONObject(i));
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("store_name", store_name);
                jsonObject.put("store_id", store_id);
                childList.add(0, jsonObject);
                childAdapter.setHead_layoutId(R.layout.item_chid_car_list_head);
                childAdapter.setDatas(childList);
                if (editstatus) {
                    childAdapter.editStatus();
                } else {
                    childAdapter.finishStatus();
                }
                childAdapter.setAllSelectNotify(isSelectAll,isNotifyAdapter,initUnselectState);
               // childAdapter.initUnselectState(true);
                childAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void editStatus() {
        editstatus = true;
        notifyDataSetChanged();
    }

    public void finishStatus() {
        editstatus = false;
        notifyDataSetChanged();
    }

    public void isSelectAllAndNotify(boolean isSelectAll, boolean isNotifyAdapter,boolean initUnselectState) {
        this.isSelectAll = isSelectAll;
        this.isNotifyAdapter = isNotifyAdapter;
        this.initUnselectState = initUnselectState;
           if(isNotifyAdapter) {
               notifyDataSetChanged();
           }
    }


    public interface CarListListener {
        void onUncheckItem(JSONObject object, ShopStoreBean storebject, int childPosition, int position);

        void onUncheckStoreItem(ShopStoreBean storeObject);

        void onCheckStoreItem(ShopStoreBean storeObject);

        void onCheckGoodItem(JSONObject goodObject, ShopStoreBean storebject, int childPosition, int position);

        void onUnCheckGoodAndUnCheckStoreItem(ShopStoreBean storeBean);

        void onModifyGoodNum(JSONObject goodObject);

        void onGoodDetail(String goodId);
    }

    public void setCarListListener(CarListListener carListChildListener) {
        this.carListListener = carListChildListener;
    }
}

