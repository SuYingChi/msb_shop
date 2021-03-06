package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class OrdersGoodListAdapter extends MyHaveHeadViewRecyclerAdapter <ComfirmShopGoodBean>{

    private  OrdersListListener ordersListListener;

    public OrdersGoodListAdapter(Context context,  OrdersListListener ordersListListener) {
        super(context, R.layout.item_car_list);
        this.ordersListListener = ordersListListener;
    }

    @Override
    public void convert(RecyclerHolder holder, ComfirmShopGoodBean comfirmShopGoodBean, final int position) {
        RecyclerView rcl = holder.getView(R.id.rcl);
        if (rcl.getAdapter() == null) {
             OrdersGoodChildListAdapter childAdapter = new OrdersGoodChildListAdapter(context, new OrdersGoodChildListAdapter.OrdersChildListlistener() {
                @Override
                public void onMessaged(String message) {
                    ordersListListener.onMessaged(message,position);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            //自适应自身高度
            linearLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(linearLayoutManager);
            List<ComfirmShopGoodBean.GoodsBean> childlist = comfirmShopGoodBean.getGoods();
            childlist.add(0,new ComfirmShopGoodBean.GoodsBean(comfirmShopGoodBean.getStore_name()));
            childlist.add(null);
            childAdapter.setHead_layoutId(R.layout.item_order_child_list_head);
            childAdapter.setFoot_layoutId(R.layout.items_orders_child_car_list_foot);
            childAdapter.setDatas(childlist);
            rcl.setAdapter(childAdapter);
        } else if (rcl.getAdapter() instanceof OrdersGoodChildListAdapter) {
            OrdersGoodChildListAdapter childAdapter = (OrdersGoodChildListAdapter) rcl.getAdapter();
            childAdapter.setHead_layoutId(R.layout.item_order_child_list_head);
            List<ComfirmShopGoodBean.GoodsBean> childlist = comfirmShopGoodBean.getGoods();
            childlist.add(0,new ComfirmShopGoodBean.GoodsBean(comfirmShopGoodBean.getStore_name()));
            childAdapter.setDatas(childlist);
            childAdapter.notifyDataSetChanged();
        }
    }
    public interface  OrdersListListener{
        void onMessaged(String message,int position);
    }
}
