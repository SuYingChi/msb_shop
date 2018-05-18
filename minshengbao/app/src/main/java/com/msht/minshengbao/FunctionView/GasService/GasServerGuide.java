package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionView.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;

public class GasServerGuide extends BaseActivity implements View.OnClickListener {
    private RelativeLayout Rwangdian,Rbaoxiuyewu;
    private RelativeLayout Rjiaona,Rpromise,Rnotopen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_server_guide);
        context=this;
        setCommonHeader("服务指南");
        Rwangdian=(RelativeLayout)findViewById(R.id.id_layout_wangdian);
        Rbaoxiuyewu=(RelativeLayout)findViewById(R.id.id_layout_yewu);
        Rjiaona=(RelativeLayout)findViewById(R.id.id_layout_jiaona);
        Rpromise=(RelativeLayout)findViewById(R.id.id_layout_promise);
        Rnotopen=(RelativeLayout)findViewById(R.id.id_layout_notopen);
        initEvent();
    }
    private void initEvent() {
        Rwangdian.setOnClickListener(this);
        Rbaoxiuyewu.setOnClickListener(this);
        Rjiaona.setOnClickListener(this);
        Rpromise.setOnClickListener(this);
        Rnotopen.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_layout_wangdian:
                wangdian();
                break;
            case R.id.id_layout_yewu:
                baoxiuyewu();
                break;
            case R.id.id_layout_jiaona:
                jiaona();
                break;
            case R.id.id_layout_promise:
                promise();
                break;
            case R.id.id_layout_notopen:
                notopen();
                break;
            default:
                break;
        }
    }
    private void wangdian() {
        String navigate="营业网点";
        String url= UrlUtil.YingyeSite_Url;
        Intent yingye=new Intent(GasServerGuide.this,HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void baoxiuyewu() {
        String navigate="居民燃气报装业务";
        String url= UrlUtil.GasYeWu_Url;
        Intent yingye=new Intent(GasServerGuide.this,HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void jiaona() {
        String navigate="气费缴纳";
        String url= UrlUtil.GasJiaoNa_Url;
        Intent yingye=new Intent(GasServerGuide.this,HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void promise() {
        String navigate="服务承诺";
        String url= UrlUtil.ServicePromise_Url;
        Intent yingye=new Intent(GasServerGuide.this,HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void notopen() {
        String navigate="常见未能正常开通情况";
        String url= UrlUtil.uninstall_Url;
        Intent yingye=new Intent(GasServerGuide.this,HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
}