package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.AgreeTreayt;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demo class
 * @author hong
 * @date 2015/10/31
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText etPhoneNumber, etCode;
    private EditText etPassword;
    private ImageView gobackimg,clearimg,showimg;
    private Button    btnGetCode,Btnregister;
    private TextView  tv_treaty;
    private TimeCount time;
    private String    PhoneNo;
    private String    verifycode;
    private String    password;
    private int       requestCode=0;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<RegisterActivity> mWeakReference;
        public RequestHandler(RegisterActivity register) {
            mWeakReference = new WeakReference<RegisterActivity>(register);
        }
        @Override
        public void handleMessage(Message msg) {
            final RegisterActivity reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if(reference.customDialog.isShowing()&&reference.customDialog!=null) {
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestCode==1){
                                reference.registerRequest();
                            }
                        }else {
                            if (reference.requestCode==0) {
                                reference.btnGetCode.setText("获取验证码");
                                reference.btnGetCode.setClickable(true);
                            }
                            reference.failure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if(reference.customDialog.isShowing()&&reference.customDialog!=null) {
                        reference.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    }
    private void failure(String error) {
        new PromptDialog.Builder(this)
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;
        mPageName ="注册";
        customDialog=new CustomDialog(this, "正在加载");
        initView();
        initEvent();
        time=new TimeCount(120000,1000);
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {  //计时过程
            btnGetCode.setClickable(false);
            btnGetCode.setText(millisUntilFinished/1000+"秒");
        }
        @Override
        public void onFinish() {
            btnGetCode.setText("获取验证码");
            btnGetCode.setClickable(true);
        }
    }
    private  void initView() {
        gobackimg=(ImageView)findViewById(R.id.id_gobackimg);
        clearimg=(ImageView)findViewById(R.id.id_clearimg);
        showimg=(ImageView)findViewById(R.id.id_image_show);
        showimg.setTag(0);
        btnGetCode =(Button)findViewById(R.id.id_btn_getcode);
        Btnregister=(Button)findViewById(R.id.id_tijiao_regiser);
        etPhoneNumber =(EditText)findViewById(R.id.id_et_phonenumber);
        etCode =(EditText)findViewById(R.id.id_et_code);
        etPassword =(EditText)findViewById(R.id.id_et_password);
        tv_treaty=(TextView)findViewById(R.id.id_treaty);
        Btnregister.setEnabled(false);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etCode.addTextChangedListener(myTextWatcher);
        etPassword.addTextChangedListener(myTextWatcher);
        etPhoneNumber.addTextChangedListener(myTextWatcher);
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
                btnGetCode.setEnabled(false);
                btnGetCode.setTextColor(Color.parseColor("#FF545454"));
                btnGetCode.setBackgroundResource(R.drawable.shape_white_border_rectangle);
            } else {
                //有效点击，
                btnGetCode.setEnabled(true);
                btnGetCode.setTextColor(Color.parseColor("#ffffffff"));
                btnGetCode.setBackgroundResource(R.drawable.shape_redorange_corners_button);
            }
            if(TextUtils.isEmpty(etPhoneNumber.getText().toString())||TextUtils.isEmpty(etCode.getText().toString() )
                    ||TextUtils.isEmpty(etPassword.getText().toString())){
                Btnregister.setEnabled(false);

            }else {
                Btnregister.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    private void initEvent() {
        gobackimg.setOnClickListener(this);
        clearimg.setOnClickListener(this);
        tv_treaty.setOnClickListener(this);
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNo = etPhoneNumber.getText().toString().trim();
                if (isPhone(PhoneNo)) {
                    btnGetCode.setText("正在发送...");
                    time.start();
                    requestCode=0;
                    requestService();
                }
            }
        });
        Btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                requestCode=1;
                requestService();
            }
        });
        showimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        showimg.setImageResource(R.drawable.password_red);
                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        v.setTag(1);
                        break;
                    case 1:
                        showimg.setImageResource(R.drawable.password_gray);
                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        v.setTag(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void requestService() {
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("username",PhoneNo);
        if (requestCode==0){
            validateURL= UrlUtil.Captcha_CodeUrl;
        }else if (requestCode==1){
            verifycode= etCode.getText().toString().trim();
            password= etPassword.getText().toString().trim();
            validateURL=UrlUtil.Register_Url;
            textParams.put("password",password);
            textParams.put("code",verifycode);
        }
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private boolean isPhone(String phoneNo) {     //判断电话号码个格式
        Matcher matcher=NUMBER_PATTERN.matcher(phoneNo);
        if (matcher.matches()){
            return true;
        }else {
            new PromptDialog.Builder(this)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("您输入电话号码不正确")
                    .setButton1("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_gobackimg:
                finish();
                break;
            case R.id.id_clearimg:
                clearForm();
                break;
            case R.id.id_treaty:
                treaty();
                break;
            default:
                break;

        }
    }
    private void treaty() {
        Intent treaty=new Intent(RegisterActivity.this,AgreeTreayt.class);
        treaty.putExtra("idNo","0");
        startActivity(treaty);
    }
    private void clearForm() {   //清除
        etPhoneNumber.setText("");
    }
    private void registerRequest() {
        Intent intentSuccess=new Intent(RegisterActivity.this,RegisterSeccess.class);
        startActivity(intentSuccess);
        RegisterActivity.this.finish();
    }
    private void removeTimeout() {
        if (time!=null){
            time.cancel();
        }
    }
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        removeTimeout();
        super.onDestroy();
    }
}