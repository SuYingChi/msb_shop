package com.msht.minshengbao.FunctionView.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.PhotoPickerAdapter;
import com.msht.minshengbao.Adapter.appointAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.HtmlWeb.PriceMenu;
import com.msht.minshengbao.FunctionView.Public.SelectAddress;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.EnsurePublish;
import com.msht.minshengbao.ViewUI.Dialog.NoticeDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectTable;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PublishOrder extends BaseActivity implements View.OnClickListener {
    private EditText  Eneed,et_name, et_phone;
    private EditText  et_recommand;
    private Button    Bsendorder;
    private TextView  tv_address;
    private TextView  appointment_data,appointment_time;
    private MultiLineChooseLayout multiChoose;
    private DatePicker datePicker;
    private View pickview;
    private String textString="",recommend;
    private String reid,userId,password,phone,id,userphone;
    private String maintype,type,address,info,appoint_time;
    private String source="1",raw_order_id="",username,city_id="";
    private String longitude="",latitude="";
    private GridView photogridview;
    private PhotoPickerAdapter mAdapter;
    private int k=0;
    private int pos=-1;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private int thisposition=-1;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requesttype=0;
    private JSONObject jsonbject;   //数据解析
    private JSONArray jsonArray;
    private ArrayList<String> mDataList=new ArrayList<>();
    private ArrayList<String>multiResult=new ArrayList<>();
    private ArrayList<String> imgPaths = new ArrayList<>();
    private String[] appointTime={"08:30-11:30","11:30-14:30","14:30-17:30","17:30-20:30","20:30-23:30"};
    private static final long SPLASH_DELAY_MILLIS = 30000;
    private NoticeDialog noticeDialog;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requesttype==0){
                                customDialog.dismiss();
                                jsonArray=object.optJSONArray("data");
                                questionData();
                            }else if (requesttype==1){
                                jsonbject =object.optJSONObject("data");
                                initShow();
                            }
                        }else {
                            customDialog.dismiss();
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(PublishOrder.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler Bitmaphandler= new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        if (results.equals("success")){
                            k++;
                            if(k==imgPaths.size()){
                                customDialog.dismiss();
                                String navigation="发布订单";
                                Intent success=new Intent(PublishOrder.this,PublishSuccess.class);
                                success.putExtra("navigation",navigation);
                                startActivity(success);
                                Bsendorder.setEnabled(true);
                                finish();
                            }
                        }else {
                            Bsendorder.setEnabled(true);
                            faifure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Bsendorder.setEnabled(true);
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void faifure(String error) {
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
    private void questionData() {
        try{
            for (int i=0;i<jsonArray.length();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String content = json.getString("content");
                mDataList.add(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        multiChoose.setList(mDataList);      //显示数据
    }
    private void initShow() {
        id=jsonbject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            String navigation="发布订单";
            Intent success=new Intent(PublishOrder.this,PublishSuccess.class);
            success.putExtra("navigation",navigation);
            startActivity(success);
            Bsendorder.setEnabled(true);
            finish();
        }
    }
    private void compressImg(final File files) {
        Luban.with(this)
                .load(files)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {}
                    @Override
                    public void onSuccess(File file) {
                        uploadImage(file);
                    }
                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        uploadImage(files);
                        Toast.makeText(PublishOrder.this,"图片压缩失败!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).launch();
    }
    private void uploadImage(File file) {
        String validateURL = UrlUtil.UploadImage_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> fileparams = new HashMap<String, File>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", id);
        fileparams.put("img",file);
        HttpUrlconnectionUtil.multipleFileParameters(validateURL, textParams, fileparams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                Bitmaphandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj =fail;
                Bitmaphandler.sendMessage(msg);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_order);
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        context=this;
        setCommonHeader("发布订单");
        reid=data.getStringExtra("id");
        maintype=data.getStringExtra("maintype");
        type=data.getStringExtra("name");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userphone=SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        findViewById(R.id.id_status_view).setVisibility(View.GONE);//状态栏View
        initJudge();
        initView();
        initData();
        initExecute();
        initEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                imgPaths.clear();
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imgPaths.addAll(photos);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 3) {
            imgPaths.remove(requestCode);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode==4){
            if (resultCode==1){
                String mAddress=data.getStringExtra("mAddress");
                String name=data.getStringExtra("name");
                String phone=data.getStringExtra("phone");
                if (name.equals("")||name.equals("null")){
                    et_name.setText(phone);
                }else {
                    et_name.setText(name);
                }
                et_phone.setText(phone);
                tv_address.setText(mAddress);
            }
        }
    }
    private void initJudge() {
        if (!VariableUtil.City.equals("海口")){
            noticeDialog=new NoticeDialog(context);
            noticeDialog.show();
            mHandler.sendEmptyMessageDelayed(1, SPLASH_DELAY_MILLIS);
        }
    }
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (noticeDialog!=null){
                        noticeDialog.dismiss();  //关闭弹窗
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void initView() {
        Bsendorder=(Button)findViewById(R.id.id_btn_sendorder);
        et_recommand=(EditText)findViewById(R.id.id_et_recommand);
        et_name=(EditText)findViewById(R.id.id_et_name);
        et_phone =(EditText)findViewById(R.id.id_et_phone);
        Eneed=(EditText)findViewById(R.id.id_et_info);
        tv_address=(TextView) findViewById(R.id.id_tv_address);
        ((TextView)findViewById(R.id.id_tv_project_type)).setText(maintype);
        ((TextView)findViewById(R.id.id_tv_type)).setText(type);
        et_phone.setText(userphone);    //默认显示用户手机号
        multiChoose=(MultiLineChooseLayout)findViewById(R.id.id_multiChoose);
        photogridview=(GridView)findViewById(R.id.noScrollgridview);
        appointment_data=(TextView)findViewById(R.id.id_data);
        appointment_time=(TextView)findViewById(R.id.id_time);
        Bsendorder.setEnabled(false);

    }
    private void initData() {
        customDialog.show();
        requesttype=0;
        String validateURL = UrlUtil.RepairOrder_QuestionUrl;
        String geturl=validateURL+"?rc_id="+reid;
        HttpUrlconnectionUtil.executeGet(geturl, new ResultListener(){
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                requestHandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message message = new Message();
                message.what = FAILURE;
                message.obj = fail;
                requestHandler.sendMessage(message);
            }
        });

    }
    private void initExecute() {
        mAdapter = new PhotoPickerAdapter(imgPaths);
        photogridview.setAdapter(mAdapter);
        photogridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= 23) {
                    thisposition=position;
                    initphoto(position);
                } else {
                    if (position == imgPaths.size()) {
                        PhotoPicker.builder()
                                .setPhotoCount(4)
                                .setShowCamera(true)
                                .setSelected(imgPaths)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(PublishOrder.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(PublishOrder.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
                }
            }
        });

    }
    private void initphoto(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        }else {
            if (position == imgPaths.size()) {
                PhotoPicker.builder()
                        .setPhotoCount(4)
                        .setShowCamera(true)
                        .setSelected(imgPaths)
                        .setShowGif(true)
                        .setPreviewEnabled(true)
                        .start(PublishOrder.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(PublishOrder.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }
    private void showphoto() {
        if (thisposition== imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(4)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(PublishOrder.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position",thisposition);
            Intent intent=new Intent(PublishOrder.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,thisposition);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                showphoto();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                Toast.makeText(PublishOrder.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void initEvent() {
        findViewById(R.id.id_re_price).setOnClickListener(this);
        findViewById(R.id.id_re_selectaddre).setOnClickListener(this);
        Bsendorder.setOnClickListener(this);
        appointment_data.setOnClickListener(this);
        appointment_time.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(tv_address.getText().toString())||TextUtils.isEmpty(et_phone.getText().toString())) {
                    Bsendorder.setEnabled(false);
                }else {
                    Bsendorder.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        tv_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(tv_address.getText().toString())||TextUtils.isEmpty(et_phone.getText().toString())) {
                    Bsendorder.setEnabled(false);
                }else {
                    Bsendorder.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        multiChoose.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                multiResult=multiChoose.getAllItemSelectedTextWithListArray();
                if (multiResult!=null&&multiResult.size()>0){
                    String textselect ="";
                    for (int i=0;i<multiResult.size();i++){
                        textselect +=multiResult.get(i)+",";
                    }
                    textString=textselect;
                }else {
                    textString="";
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_price:
                pricetable();
                break;
            case R.id.id_data:
                selectdata();
                break;
            case R.id.id_time:
                selecttime();
                break;
            case R.id.id_re_selectaddre:
                selectAddr();
                break;
            case R.id.id_btn_sendorder:
                if (!VariableUtil.City.equals("海口")){    //非海口业务未开通
                    noticeDialog.show();
                    mHandler.sendEmptyMessageDelayed(1, SPLASH_DELAY_MILLIS);
                }else {
                    ordersend();
                }
                break;
            default:
                break;
        }
    }
    private void pricetable() {
        Intent price=new Intent(context,PriceMenu.class);
        price.putExtra("reid",reid);
        startActivity(price);
    }
    private void selectdata() {
        LayoutInflater l = LayoutInflater.from(this);
        pickview= l.inflate(R.layout.item_pickerdata_dialog, null);
        datePicker=(DatePicker) pickview.findViewById(R.id.datepicker);
        Calendar mcurrent=Calendar.getInstance();
        datePicker.init(mcurrent.get(Calendar.YEAR), mcurrent.get(Calendar.MONTH),  mcurrent.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {//滑动日期进行对日期的方位进行判断
                Calendar mAfter=Calendar.getInstance();
                Calendar mBefore=Calendar.getInstance();
                mAfter.add(mAfter.DAY_OF_MONTH,6);
                if (isDateAfter(view)) {
                    view.init(mAfter.get(Calendar.YEAR),mAfter.get(Calendar.MONTH),mAfter.get(Calendar.DAY_OF_MONTH), this);
                }
                if (isDateBefore(view)) {
                    view.init(mBefore.get(Calendar.YEAR), mBefore.get(Calendar.MONTH),mBefore.get(Calendar.DAY_OF_MONTH), this);
                }
            }
        });
        new PromptDialog.Builder(this)
                .setTitle("选择预约日期")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                //.setMessage("2016年")
                .setView(pickview)    //pickview  日期选择布局
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        appointment_data.setText(datePicker.getYear() + "-"
                                + (datePicker.getMonth()+1) + "-"
                                + datePicker.getDayOfMonth());
                        appointment_time.setText("08:30-11:30");
                        dialog.dismiss();
                    }
                })
                .show();

    }
    private boolean isDateBefore(DatePicker tempView) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar temCalendar = Calendar.getInstance();
        temCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
        if (temCalendar.before(mCalendar)) {     //2016年9月21 后
            return true;
        } else {
            return false;
        }
    }
    private boolean isDateAfter(DatePicker tempView) {
        Calendar mCalendar=Calendar.getInstance();
        mCalendar.add(mCalendar.DAY_OF_MONTH,6);
        Calendar temCalendar=Calendar.getInstance();
        temCalendar.add(temCalendar.DAY_OF_MONTH,6);
        temCalendar.set(tempView.getYear(),tempView.getMonth(),tempView.getDayOfMonth(),0,0,0);
        if (temCalendar.after(mCalendar)){  //2016年9月21 前
            return true;
        }else {
            return false;
        }
    }
    private void selecttime() {
        final SelectTable selectTable=new SelectTable(context);
        final TextView tv_title=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tv_title.setText("选择时间");
        final appointAdapter adapter=new appointAdapter(context,appointTime,pos);
        mListView.setAdapter(adapter);
        selectTable.show();
        selectTable.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTable.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                String time=appointTime[position];
                appointment_time.setText(time);
                selectTable.dismiss();
            }
        });
    }
    private void selectAddr() {
        Intent intent=new Intent(context, SelectAddress.class);
        startActivityForResult(intent,4);
    }
    private void ordersend() {
        String date=appointment_data.getText().toString().trim();
        String time=appointment_time.getText().toString().trim();
        String otherinfo=Eneed.getText().toString().trim();
        username=et_name.getText().toString().trim();
        recommend=et_recommand.getText().toString().trim();
        phone= et_phone.getText().toString().trim();
        address=tv_address.getText().toString().trim();
        appoint_time=date+"  "+time;
        info=textString+otherinfo;
        final EnsurePublish ensurePublish=new EnsurePublish(this);
        ensurePublish.setTitleText("确认信息");
        ensurePublish.setNameText(username);
        ensurePublish.setTypeText(maintype);
        ensurePublish.setPhoneText(phone);
        ensurePublish.setAddressText(address);
        ensurePublish.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensurePublish.dismiss();
            }
        });
        ensurePublish.setOnpositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensurePublish.dismiss();
                customDialog.show();
                requesttype=1;
                Bsendorder.setEnabled(false);
                requstSevices();
            }
        });
        ensurePublish.show();

    }

    private void requstSevices() {
        String validateURL = UrlUtil.PublishOrder_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("cid",reid);
        textParams.put("phone",phone);
        textParams.put("address",address);
        textParams.put("info",info);
        textParams.put("appoint_time",appoint_time);
        textParams.put("recommend_code",recommend);
        textParams.put("source",source);
        textParams.put("raw_order_id",raw_order_id);
        textParams.put("username",username);
        textParams.put("city_id",city_id);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
}