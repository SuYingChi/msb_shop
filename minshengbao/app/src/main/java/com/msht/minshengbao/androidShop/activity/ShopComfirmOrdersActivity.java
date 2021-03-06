package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.OrdersGoodListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.BuyStep2SuccessBean;
import com.msht.minshengbao.androidShop.shopBean.BuyStep3PayListBean;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.InvItemBean;
import com.msht.minshengbao.androidShop.shopBean.RecommendBean;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep1View;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep2View;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep3GetPayListView;
import com.msht.minshengbao.androidShop.viewInterface.IChangeAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAddressListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopComfirmOrdersActivity extends ShopBaseActivity implements IGetAddressListView, IBuyStep1View, IChangeAddressView, IBuyStep2View, IBuyStep3GetPayListView {

    private static final int REQUEST_CODE_RECOMMEND = 200;
    private static final int REQUEST_CODE_INV_INFO = 300;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.phone_num)
    TextView tvPhone;
    @BindView(R.id.location)
    TextView tvLoac;
    @BindView(R.id.isdefault)
    ImageView iv;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.et_recommand)
    EditText etRecommand;
    @BindView(R.id.inv_info)
    TextView tvInv_info;
    @BindView(R.id.total_money)
    TextView tvTotal;
    @BindView(R.id.delivery)
    TextView tvDelivery;
    @BindView(R.id.good_num)
    TextView tvGoodNum;
    @BindView(R.id.total)
    TextView tvTotalGoodsSelf;
    private static final int REQUEST_CODE_ADDRESS = 100;
    private List<ComfirmShopGoodBean> list;
    private String ifCarted;
    private String isPickup_self;
    private String addressId;
    private HashMap<String, String> messageMap = new HashMap<String, String>();
    private JSONObject datas;
    private List<RecommendBean> recommandList = new ArrayList<RecommendBean>();
    private ShopAddressListBean.DatasBean.AddressListBean selectedAddressBean;
    private String amount;
    private String freight_hash;
    private double goods_freight = 0.0;
    private double goodsTotal = 0;
    private double goodsTotalSelf = 0;
    private double discount = 0;
    private double voucher = 0;
    private Map<String, Voucher> voucherInfoMap = new HashMap<String, Voucher>();
    private Map<String, List<JSONObject>> storeMansongMap = new HashMap<String, List<JSONObject>>();
    private Map<String, List<JSONObject>> voucherListMap = new HashMap<String, List<JSONObject>>();
    private Map<String, JSONObject> voucherListSingleMap = new HashMap<String, JSONObject>();
    private String rpacketTId;
    private JSONObject rpackeObj;
    private List<JSONObject> rptList = new ArrayList<JSONObject>();
    private Map<String, JSONObject> manSongMap = new HashMap<String, JSONObject>();
    private RecommendBean recommendBean;
    private String vat_hash;
    private String offpay_hash = "";
    private String offpay_hash_batch = "";
    private String allow_offpay;
    private InvItemBean invInfoBean;
    private double available_predeposit = 0;
    private String pd_pay;
    private String carIds;
    private String paySn;
    private String pdPassword;
    private int orderId;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;

    @Override
    protected void setLayout() {
        setContentView(R.layout.comfirm_shop_orders);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            list = (List<ComfirmShopGoodBean>) bundle.getSerializable("data");
            ifCarted = bundle.getString("ifCar");
            isPickup_self = bundle.getString("isPickup_self");
            int totalNum = 0;
            for (ComfirmShopGoodBean bean : list) {
                List<ComfirmShopGoodBean.GoodsBean> goodlist = bean.getGoods();
                for (ComfirmShopGoodBean.GoodsBean good : goodlist) {
                    totalNum += Integer.valueOf(good.getGoods_num());
                }
            }
            tvGoodNum.setText(String.format("共%d件商品", totalNum));
            if(TextUtils.equals(isPickup_self,"0")) {
                ShopPresenter.getAddressList(this, false);
            }else {
                PopUtil.toastInBottom("暂不支持自提商品购买");
            }
        } else {
            finish();
        }

    }


    @OnClick({R.id.rlt, R.id.back, R.id.rlt_recommend, R.id.rlt_inv_info, R.id.comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt:
                Intent intent = new Intent(this, ShopAddressListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADDRESS);
                break;
            case R.id.back:
                finish();
            case R.id.rlt_recommend:
                if (recommandList.size() == 0) {
                    PopUtil.toastInCenter("无更多推荐人可选");
                } else {
                    Intent intent1 = new Intent(this, RecommendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) recommandList);
                    intent1.putExtras(bundle);
                    startActivityForResult(intent1, REQUEST_CODE_RECOMMEND);
                }
                break;
            case R.id.rlt_inv_info:
                Intent intent2 = new Intent(this, InvInfoActivity.class);
                intent2.putExtra("data", datas.optString("order_amount"));
                intent2.putExtra("address", selectedAddressBean);
                startActivityForResult(intent2, REQUEST_CODE_INV_INFO);
                break;
            case R.id.comfirm:
                ShopPresenter.buyStep2(this, carIds, recommendBean.getRecommend_phone(), ifCarted, isPickup_self, addressId, vat_hash, offpay_hash, offpay_hash_batch);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == RESULT_OK) {
            if (data != null) {
                selectedAddressBean = (ShopAddressListBean.DatasBean.AddressListBean) data.getSerializableExtra("data");
                tvName.setText(selectedAddressBean.getTrue_name());
                tvPhone.setText(selectedAddressBean.getMob_phone());
                addressId = selectedAddressBean.getAddress_id();
                tvLoac.setText(String.format("%s%s", selectedAddressBean.getArea_info(), selectedAddressBean.getAddress()));
                if (selectedAddressBean.getIs_default().equals("1")) {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_selected));
                } else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_selected));
                }
                ShopPresenter.buyStep2ChangeAddress(this);
            }
        } else if (requestCode == REQUEST_CODE_RECOMMEND && resultCode == RESULT_OK) {
            if (data != null) {
                recommendBean = (RecommendBean) data.getSerializableExtra("data");
                String recommend_phone = recommendBean.getRecommend_phone();
                etRecommand.setText(recommend_phone);
            }
        } else if (requestCode == REQUEST_CODE_INV_INFO && resultCode == RESULT_OK) {
            if (data != null) {
                invInfoBean = (InvItemBean) data.getSerializableExtra("inv");
                String content = invInfoBean.getInv_title() + " " + (invInfoBean.getInv_code().equals("null") ? "" : invInfoBean.getInv_code()) + " " + invInfoBean.getInv_content();
                tvInv_info.setText(content);
            }
        }

    }

    @Override
    public void onGetAddressListSuccess(String s) {
        ShopAddressListBean.DatasBean.AddressListBean defaultAddress = null;
        ShopAddressListBean addressBean = JsonUtil.toBean(s, ShopAddressListBean.class);
        for (ShopAddressListBean.DatasBean.AddressListBean bean : addressBean.getDatas().getAddress_list()) {
            if (bean.getIs_default().equals("1")) {
                defaultAddress = bean;
                break;
            }
        }
        if (defaultAddress != null) {
            tvName.setText(defaultAddress.getTrue_name());
            tvPhone.setText(defaultAddress.getMob_phone());
            tvLoac.setText(String.format("%s%s", defaultAddress.getArea_info(), defaultAddress.getAddress()));
            iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_selected));
            addressId = defaultAddress.getAddress_id();
            selectedAddressBean = defaultAddress;
            ShopPresenter.buyStep1(this);
        } else if (addressBean.getDatas().getAddress_list().size() > 0) {
            defaultAddress = addressBean.getDatas().getAddress_list().get(0);
            tvName.setText(defaultAddress.getTrue_name());
            tvPhone.setText(defaultAddress.getMob_phone());
            tvLoac.setText(String.format("%s%s", defaultAddress.getArea_info(), defaultAddress.getAddress()));
            addressId = defaultAddress.getAddress_id();
            selectedAddressBean = defaultAddress;
            ShopPresenter.buyStep1(this);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_selected));
        } else {
            PopUtil.toastInBottom("您还没有收货地址，请添加默认地址");
        }

    }

    @Override
    public String getCarId() {
        // return "3713|1,3787|1";
        if (ifCarted.equals("1")) {
            StringBuilder sf = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                List<ComfirmShopGoodBean.GoodsBean> goods = list.get(i).getGoods();
                for (int ii = 0; ii < goods.size(); ii++) {
                    if (ii == 0 && i == 0) {
                        sf.append(goods.get(ii).getCart_id()).append("|").append(goods.get(ii).getGoods_num());
                    } else {
                        sf.append(",").append(goods.get(ii).getCart_id()).append("|").append(goods.get(ii).getGoods_num());
                    }
                }
            }
            carIds = sf.toString();
            return carIds;
        } else {
            StringBuilder sf = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                List<ComfirmShopGoodBean.GoodsBean> goods = list.get(i).getGoods();
                for (int ii = 0; ii < goods.size(); ii++) {
                    if (ii == 0 && i == 0) {
                        sf.append(goods.get(ii).getGoods_id()).append("|").append(goods.get(ii).getGoods_num());
                    } else {
                        sf.append(",").append(goods.get(ii).getGoods_id()).append("|").append(goods.get(ii).getGoods_num());
                    }
                }
            }
            carIds = sf.toString();
            return carIds;
        }
    }


    @Override
    public String getAddressid() {
        return addressId;
    }

    @Override
    public void onBuyStep1Success(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            datas = jsonObject.optJSONObject("datas");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            //scrollerview 嵌套recycleview，再嵌套recycleview ，使用nestedscrollerview，不能用Scrollerview,并且加上这行代码
            rcl.setNestedScrollingEnabled(false);
            rcl.setLayoutManager(linearLayoutManager);
            rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            OrdersGoodListAdapter adapter = new OrdersGoodListAdapter(this, new OrdersGoodListAdapter.OrdersListListener() {
                @Override
                public void onMessaged(String message, int position) {
                    String store_id = list.get(position).getStore_id();
                    messageMap.put(store_id, message);
                }
            });
            adapter.setDatas(list);
            rcl.setAdapter(adapter);
            JSONArray recommendinfo = datas.optJSONArray("recommend_info");
            for (int i = 0; i < recommendinfo.length(); i++) {
                JSONObject obj = recommendinfo.optJSONObject(i);
                recommandList.add(new RecommendBean(obj.optString("recommend_phone"), obj.optString("default")));
            }
            for (RecommendBean re : recommandList) {
                if (re.getDefaultX().equals("1")) {
                    if (!TextUtils.isEmpty(re.getRecommend_phone())) {
                        etRecommand.setText(re.getRecommend_phone());
                    }
                    recommendBean = re;
                    break;
                }
            }
            if (recommendBean == null) {
                recommendBean = new RecommendBean("", "1");
            }
            vat_hash = datas.optString("vat_hash");
            JSONObject obj = datas.optJSONObject("inv_info");
            invInfoBean = new InvItemBean(obj.optString("content"), true, obj.optString("inv_id"), obj.optString("inv_title"), obj.optString("inv_code"));
            String content = invInfoBean.getInv_title() + " " + (invInfoBean.getInv_code().equals("null") ? "" : invInfoBean.getInv_code()) + " " + invInfoBean.getInv_content();
            tvInv_info.setText(content);
            amount = datas.optString("order_amount");
            goodsTotal = Double.valueOf(amount);
            tvTotal.setText(String.format("合计：%s", StringUtil.getPriceSpannable12String(this, amount, R.style.big_money, R.style.big_money)));
            freight_hash = datas.optString("freight_hash");
            String predeposit = datas.optString("available_predeposit");
            if (!(TextUtils.isEmpty(predeposit) || TextUtils.equals(predeposit, "null"))) {
                available_predeposit = Double.valueOf(predeposit);
            }
            for (ComfirmShopGoodBean bean : list) {
                if (datas.opt("store_mansong_rule_list") instanceof JSONObject) {
                    JSONObject storeMansongObj = datas.optJSONObject("store_mansong_rule_list");
                    manSongMap.put(bean.getStore_id(), storeMansongObj);
                    discount += Double.valueOf(storeMansongObj.optString("discount"));
                } else if (datas.opt("store_mansong_rule_list") instanceof JSONArray && ((JSONArray) datas.opt("store_mansong_rule_list")).length() == 0 || datas.opt("store_mansong_rule_list") == null) {
                    LogUtils.e("店铺" + bean.getStore_name() + "无满减优惠");
                } else if (datas.opt("store_mansong_rule_list") instanceof JSONArray && ((JSONArray) datas.opt("store_mansong_rule_list")).length() > 0) {
                    JSONArray store_mansong_rule_list = datas.optJSONArray("store_mansong_rule_list");
                    List<JSONObject> storeMansongRuleList = new ArrayList<JSONObject>();
                    for (int i = 0; i < store_mansong_rule_list.length(); i++) {
                        JSONObject store_mansong_rule = store_mansong_rule_list.optJSONObject(i);
                        storeMansongRuleList.add(store_mansong_rule);
                    }
                    storeMansongMap.put(bean.getStore_id(), storeMansongRuleList);
                }

                if (datas.opt("store_voucher_info") instanceof JSONObject) {
                    JSONObject store_voucher_info = datas.optJSONObject("store_voucher_info");
                    voucher += Double.valueOf(store_voucher_info.optString("voucher_price"));
                    String voucher_t_id = store_voucher_info.optString("voucher_t_id");
                    Voucher voucherBean = new Voucher(voucher_t_id, store_voucher_info.optString("voucher_price"));
                    voucherInfoMap.put(bean.getStore_id(), voucherBean);
                } else if (datas.opt("store_voucher_info") instanceof JSONArray && ((JSONArray) datas.opt("store_voucher_info")).length() == 0 || datas.opt("store_voucher_info") == null) {
                    LogUtils.e("店铺" + bean.getStore_name() + "无代金券信息");
                } else if (datas.opt("store_voucher_info") instanceof JSONArray && ((JSONArray) datas.opt("store_voucher_info")).length() > 0) {
                    Map<String, List<JSONObject>> storeVoucherinfoMap = new HashMap<String, List<JSONObject>>();
                    JSONArray store_voucher_info = datas.optJSONArray("store_voucher_info");
                    List<JSONObject> storeVoucherinfoList = new ArrayList<JSONObject>();
                    for (int i = 0; i < store_voucher_info.length(); i++) {
                        JSONObject store_voucher = store_voucher_info.optJSONObject(i);
                        storeVoucherinfoList.add(store_voucher);
                    }
                    storeVoucherinfoMap.put(bean.getStore_id(), storeVoucherinfoList);
                }

                if (datas.opt("store_voucher_list") instanceof JSONObject) {
                    JSONObject store_voucher_list = datas.optJSONObject("store_voucher_list");
                    if (voucherInfoMap.containsKey(bean.getStore_id())) {
                        Voucher voucherBean = voucherInfoMap.get(bean.getStore_id());
                        if (store_voucher_list.has(voucherBean.voucher_t_id)) {
                            JSONObject voucherObj = store_voucher_list.optJSONObject(voucherBean.voucher_t_id);
                            voucherListSingleMap.put(voucherBean.voucher_t_id, voucherObj);
                        }
                    }
                } else if (datas.opt("store_voucher_list") instanceof JSONArray && ((JSONArray) datas.opt("store_voucher_list")).length() == 0 || datas.opt("store_voucher_list") == null) {
                    LogUtils.e("店铺" + bean.getStore_name() + "无代金券");
                } else if (datas.opt("store_voucher_list") instanceof JSONArray && ((JSONArray) datas.opt("store_voucher_list")).length() > 0) {
                    JSONArray store_voucher_list = datas.optJSONArray("store_voucher_list");
                    List<JSONObject> storeVoucherinfoList = new ArrayList<JSONObject>();
                    for (int i = 0; i < store_voucher_list.length(); i++) {
                        JSONObject store_voucher = store_voucher_list.optJSONObject(i);
                        storeVoucherinfoList.add(store_voucher);
                    }
                    voucherListMap.put(bean.getStore_id(), storeVoucherinfoList);
                }
                JSONObject store_cart_list = datas.optJSONObject("store_cart_list");
                JSONObject storeObj = store_cart_list.optJSONObject(bean.getStore_id());
                JSONArray goods_list = storeObj.optJSONArray("goods_list");
                for (int i = 0; i < goods_list.length(); i++) {
                    JSONObject objj = goods_list.optJSONObject(i);
                    goods_freight += Double.valueOf(objj.optString("goods_freight"));
                    goodsTotalSelf += Double.valueOf(objj.optString("goods_price")) * Integer.valueOf(objj.optString("goods_num"));
                }
            }
            List<String> rpacket_t_idList = new ArrayList<String>();
            if (datas.opt("rpt_info") instanceof JSONObject) {
                JSONObject rpt_info = datas.optJSONObject("rpt_info");
                rpacketTId = rpt_info.optString("rpacket_t_id");
            } else if (datas.opt("rpt_info") instanceof JSONArray && ((JSONArray) datas.opt("rpt_info")).length() == 0 || datas.opt("rpt_info") == null) {
                LogUtils.e("无优惠红包");
            } else if (datas.opt("rpt_info") instanceof JSONArray && ((JSONArray) datas.opt("rpt_info")).length() > 0) {
                JSONArray rpt_info = datas.optJSONArray("rpt_info");
                for (int i = 0; i < rpt_info.length(); i++) {
                    JSONObject rpt_info_obj = rpt_info.optJSONObject(i);
                    String rpacket_t_id = rpt_info_obj.optString("rpacket_t_id");
                    rpacket_t_idList.add(rpacket_t_id);
                }
            }

            if (datas.opt("rpt_list") instanceof JSONObject) {
                JSONObject rptObj = datas.optJSONObject("rpt_list");
                if (TextUtils.isEmpty(rpacketTId) && rptObj.has(rpacketTId)) {
                    rpackeObj = rptObj.optJSONObject(rpacketTId);
                } else if (rpacket_t_idList.size() > 0) {
                    for (int i = 0; i < rpacket_t_idList.size(); i++) {
                        String rpId = rpacket_t_idList.get(i);
                        rptList.add(rptObj.optJSONObject(rpId));
                    }
                }
            } else if (datas.opt("rpt_list") instanceof JSONArray && ((JSONArray) datas.opt("rpt_list")).length() == 0 || datas.opt("rpt_list") == null) {
                LogUtils.e("无优惠红包");
            } else if (datas.opt("rpt_list") instanceof JSONArray && ((JSONArray) datas.opt("rpt_list")).length() > 0) {
                JSONArray rpt_list = datas.optJSONArray("rpt_list");
                for (int i = 0; i < rpt_list.length(); i++) {
                    JSONObject rpt_obj = rpt_list.optJSONObject(i);
                    rptList.add(rpt_obj);
                }
            }

            goodsTotalSelf = goodsTotalSelf - discount - voucher;
            if (rpackeObj != null) {
                goodsTotalSelf -= Double.valueOf(rpackeObj.optString("rpacket_price"));
            } else if (rptList.size() > 0) {
                double rpacket = 0;
                for (JSONObject objj : rptList) {
                    rpacket += Double.valueOf(objj.optString("rpacket_price"));
                }
                goodsTotalSelf -= rpacket;
            }
            goodsTotal = goodsTotalSelf + goods_freight;
            tvTotalGoodsSelf.setText(StringUtil.getPriceSpannable12String(this, goodsTotalSelf + "", R.style.big_money, R.style.big_money));
            tvDelivery.setText(StringUtil.getPriceSpannable12String(this, goods_freight + "", R.style.big_money, R.style.big_money));
            tvTotal.setText(StringUtil.getPriceSpannable12String(this, goodsTotal + "", R.style.big_money, R.style.big_money));
            ShopPresenter.buyStep2ChangeAddress(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCityId() {
        return selectedAddressBean.getCity_id();
    }

    @Override
    public String getAreaId() {
        return selectedAddressBean.getArea_id();
    }

    @Override
    public String getFreight_hash() {
        return freight_hash;
    }

    @Override
    public void onChangeAddressSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datass = obj.optJSONObject("datas");
            JSONObject content = datass.optJSONObject("content");
            if(content==null){
                LogUtils.e(s);
            }else {
                goods_freight = 0;
                for (int i = 0; i < list.size(); i++) {
                    String store_id = list.get(i).getStore_id();
                    double deliverFare = Double.valueOf(TextUtils.isEmpty(content.optString(store_id)) ? "0" : content.optString(store_id));
                    goods_freight += deliverFare;
                }
            }
            offpay_hash = datass.optString("offpay_hash");
            offpay_hash_batch = datass.optString("offpay_hash_batch");
            allow_offpay = datass.optString("allow_offpay");
            goodsTotal = goodsTotalSelf + goods_freight;
            tvDelivery.setText(StringUtil.getPriceSpannable12String(this, goods_freight + "", R.style.big_money, R.style.big_money));
            tvTotal.setText(StringUtil.getPriceSpannable12String(this, goodsTotal + "", R.style.big_money, R.style.big_money));
            tvTotalGoodsSelf.setText(String.format("合计：%s", StringUtil.getPriceSpannable12String(this, goodsTotalSelf + "", R.style.big_money, R.style.big_money)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String ifCarted() {
        return ifCarted;
    }

    @Override
    public String ifPickupSelf() {
        return isPickup_self;
    }


    @Override
    public String getDelivery() {
        return "";
    }


    @Override
    public String getPayName() {
        if (TextUtils.equals(allow_offpay, "1")) {
            //暂且空着货到付款的逻辑
        }
        return "online";
    }

    @Override
    public String getInvoiceIds() {
        return invInfoBean.getInv_id();
    }

    @Override
    public String getVoucher() {
        if (!voucherListSingleMap.isEmpty()) {
            StringBuilder sf = new StringBuilder();
            for (Map.Entry<String, JSONObject> entry : voucherListSingleMap.entrySet()) {
                JSONObject obj = entry.getValue();
                if (TextUtils.isEmpty(sf.toString())) {
                    sf.append(obj.optString("voucher_id")).append("|").append(obj.optString("voucher_store_id")).append(obj.optString("voucher_price"));
                } else {
                    sf.append(",").append(obj.optString("voucher_id")).append("|").append(obj.optString("voucher_store_id")).append(obj.optString("voucher_price"));
                }
            }
            return sf.toString();
        }
        return "";
    }

    @Override
    public String getPd_pay() {
        if (available_predeposit > 0) {
            //可用预存款
        }
        return pd_pay = "0";
    }

    @Override
    public String getPassword() {
        if (TextUtils.equals(pd_pay, "0")) {
            //不使用预存款
        }

        return pdPassword = "";
    }

    @Override
    public String getFcode() {
        //F码支付暂且空着
        return "";
    }

    @Override
    public String getRcb_pay() {
        //不使用充值卡
        return "0";
    }

    @Override
    public String getRpt() {
        if (rptList.size() > 0) {
            StringBuilder sf = new StringBuilder();
            for (int i = 0; i < rptList.size(); i++) {
                JSONObject rpt = rptList.get(i);
                if (i == 0) {
                    sf.append(rpt.optString("rpacket_t_id")).append("|").append(rpt.optString("rpacket_price"));
                } else {
                    sf.append(",").append(rpt.optString("rpacket_t_id")).append("|").append(rpt.optString("rpacket_price"));
                }
            }
            return sf.toString();
        }
        return "";
    }

    @Override
    public String getPay_message() {
        if (!messageMap.isEmpty()) {
            StringBuilder sf = new StringBuilder();
            for (Map.Entry<String, String> entry : messageMap.entrySet()) {
                String message = entry.getValue();
                String storeId = entry.getKey();
                if (TextUtils.isEmpty(sf.toString())) {
                    sf.append(storeId).append("|").append(message);
                } else {
                    sf.append(",").append(storeId).append("|").append(message);
                }
            }
            return sf.toString();
        }
        return "";
    }

    @Override
    public void onBuyStep2Success(String s) {
        BuyStep2SuccessBean bean = JsonUtil.toBean(s, BuyStep2SuccessBean.class);
        paySn = bean.getDatas().getPay_sn();
        orderId = bean.getDatas().getOrder_id();
        ShopPresenter.buyStep3(this, paySn);

    }


    @Override
    public void onBuyStep3(String s) {
        Intent intent = new Intent(this, ShopPayOrderActivity.class);
        BuyStep3PayListBean buyStep3bean = JsonUtil.toBean(s, BuyStep3PayListBean.class);
        intent.putExtra("buyStep3", buyStep3bean);
        intent.putExtra("pdPassword", pdPassword);
        intent.putExtra("orderId", orderId + "");
        startActivity(intent);
        finish();
    }

    private class Voucher {
        public String voucher_price;
        public String voucher_t_id;

        public Voucher(String voucher_t_id, String voucher_price) {
            this.voucher_t_id = voucher_t_id;
            this.voucher_price = voucher_price;
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        finish();
    }
}
