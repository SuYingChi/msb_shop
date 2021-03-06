package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.GetAddressAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
  * Demo class
  * 〈一句话功能简述〉
  * 〈功能详细描述〉
  * @author hong
  * @date 2016/8/12 
  */
public class SelectCustomerNo extends BaseActivity {
    private Button btnAddress;
    private ListViewForScrollView addressList;
    private String userId,password;
    private int pos=-1;
    private JSONArray jsonArray;
    private static final int ADDRESS_CODE=1;
    private GetAddressAdapter adapter;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    private final  RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SelectCustomerNo> mWeakReference;
        public RequestHandler(SelectCustomerNo activity) {
            mWeakReference = new WeakReference<SelectCustomerNo>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelectCustomerNo activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveData();
                        }else {
                            if (!activity.isFinishing()){
                                activity.onFailure(error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String customerNo = jsonObject.getString("customerNo");
                HashMap<String, String> map = new HashMap<String, String>(3);
                map.put("id", id);
                map.put("name", name);
                map.put("customerNo", customerNo);
                houseList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (houseList.size()!=0){
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customerno);
        context=this;
        setCommonHeader("选择燃气用户号");
        userId = SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
        initFindViewById();
        initEvent();
        adapter=new GetAddressAdapter(this, houseList,pos);
        addressList.setAdapter(adapter);
        addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                String addressName=houseList.get(position).get("name");
                String customerNo=houseList.get(position).get("customerNo");
                String houseId=houseList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("addressname",addressName);
                name.putExtra("houseId",houseId);
                name.putExtra("customerNo",customerNo);
                setResult(2, name);
                finish();
            }
        });
        adapter.setRadioButtonClickListener(new GetAddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                pos=position;
                adapter.notifyDataSetChanged();
                String addressName=houseList.get(position).get("name");
                String customerNo=houseList.get(position).get("customerNo");
                String houseId=houseList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("addressname",addressName);
                name.putExtra("houseId",houseId);
                name.putExtra("customerNo",customerNo);
                setResult(2, name);
                finish();
            }
        });
        requestServer();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADDRESS_CODE:
                if(resultCode==1) {
                    /*清除原来数据  **/
                    houseList.clear();
                    adapter.notifyDataSetChanged();
                    requestServer();
                }
                break;
                default:
                    break;
        }
    }
    private void initFindViewById() {
        btnAddress =(Button)findViewById(R.id.id_btn_add_address);
        addressList = (ListViewForScrollView)findViewById(R.id.id_listview);
    }
    private void initEvent() {
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddCustomerNoActivity.class);
                intent.putExtra("addresscode", ADDRESS_CODE);
                startActivityForResult(intent, 1);
            }
        });
    }
    private void requestServer() {
        String validateURL = UrlUtil.SELECT_ADDRESS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(context.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
