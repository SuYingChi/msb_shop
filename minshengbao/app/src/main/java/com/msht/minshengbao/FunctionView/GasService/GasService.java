package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.GasServiceAdapter;
import com.msht.minshengbao.Adapter.OtherRepairAdapter;
import com.msht.minshengbao.Adapter.SanitaryAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.Myview.LoginView;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class GasService extends BaseActivity {
    private MyNoScrollGridView mGridView;
    private GasServiceAdapter homeAdapter;
    private String pid;
    private String cityId;
    private   boolean lstate=false;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private CustomDialog customDialog;
    private static final String my_url= UrlUtil.Service_noteUrl;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    Handler getfunctionhandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String Error = object.optString("error");
                        JSONArray json =object.getJSONArray("data");
                        if(result.equals("success")) {
                            initFunction(json);
                        }else {
                            Toast.makeText(context, Error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initFunction(JSONArray json) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String code = jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("code", code);
                functionList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (functionList.size() != 0) {
            homeAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_service);
        context=this;
        setCommonHeader("燃气业务");
        lstate= SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        cityId=data.getStringExtra("city_id");
        initView();
        homeAdapter=new GasServiceAdapter(context,functionList);
        mGridView.setAdapter(homeAdapter);
        initData();
        initStartActivity();

    }
    private void initStartActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lstate){
                    String codes=functionList.get(position).get("code");
                    switch (codes){
                        case "gas_repair":
                            gasrepair();
                            break;
                        case "gas_meter":
                            writetable();
                            break;
                        case "gas_pay":
                            payfee();
                            break;
                        case "gas_install":
                            installDevice();
                            break;
                        case "gas_rescue":
                            qianxian();
                            break;
                        case "gas_introduce":
                            introduce();
                            break;
                        default:
                            break;
                    }
                }else {
                    Intent login=new Intent(context, LoginView.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
    private void initView() {
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_function_view);
    }
    private void initData() {
        customDialog.show();
        String functionUrl= UrlUtil.SecondService_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&pid="+URLEncoder.encode(pid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpUrlconnectionUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                getfunctionhandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                getfunctionhandler.sendMessage(msg);
            }
        });
    }
    private void payfee() {
        Intent selete=new Intent(context,GasPayfee.class);
        startActivity(selete);
    }

    private void qianxian() {
        Intent selete=new Intent(context,Gasqianxian.class);
        startActivity(selete);
    }
    private void introduce() {
        Intent selete=new Intent(context,GasIntroduce.class);
        startActivity(selete);
    }
    private void gasrepair() {
        Intent selete=new Intent(context,GasRepair.class);
        startActivity(selete);
    }
    private void writetable() {
        Intent selete=new Intent(context,GasWriteTable.class);
        startActivity(selete);
    }
    private void installDevice() {
        Intent selete=new Intent(context,GasInstall.class);
        startActivity(selete);
    }
}
