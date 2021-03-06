package com.msht.minshengbao.functionActivity.repairService;

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

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.PhotoPickerAdapter;
import com.msht.minshengbao.adapter.appointAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.PriceMenuActivity;
import com.msht.minshengbao.functionActivity.Public.SelectAddressActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/19  
 */
public class PublishOrderActivity extends BaseActivity implements View.OnClickListener {
    private EditText etRecommend;
    private EditText etNeed;
    private Button   btnSendOrder;
    private TextView ttvName, tvPhone;
    private TextView tvAddress;
    private TextView appointmentData, appointmentTime;
    private MultiLineChooseLayout multiChoose;
    private String   textString="",recommend;
    private String   reid,userId,password,phone,id, userPhone;
    private String   mMainType,type,address,info, appointDate;
    private String   username, rawOrderId="",cityId="";
    private String   longitude="",latitude="";
    private GridView mPhotoGridView;
    private PhotoPickerAdapter mAdapter;
    private int k=0;
    private int pos=-1;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private int thisPosition =-1;
    private int requestType =0;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private static final String CITY_NAME="海口";
    private ArrayList<String> mDataList=new ArrayList<>();
    private ArrayList<String>multiResult=new ArrayList<>();
    private ArrayList<String> imgPaths = new ArrayList<>();
    private String[] appointTime={"08:30-11:30","11:30-14:30","14:30-17:30","17:30-20:30","20:30-23:30"};
    private static final long SPLASH_DELAY_MILLIS = 30000;
    private NoticeDialog noticeDialog;
    private CustomDialog customDialog;
    private final NoticeHandler mHandler=new NoticeHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final BitmapHandler bitmapHandler=new BitmapHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<PublishOrderActivity> mWeakReference;
        public RequestHandler(PublishOrderActivity activity) {
            mWeakReference = new WeakReference<PublishOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PublishOrderActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String  results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType ==0){
                                activity.customDialog.dismiss();
                                activity.jsonArray=object.optJSONArray("data");
                                activity.questionData();
                            }else if (activity.requestType ==1){
                                activity.jsonObject =object.optJSONObject("data");
                                activity.initShow();
                            }
                        }else {
                            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                                activity.customDialog.dismiss();
                            }
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class BitmapHandler extends Handler{
        private WeakReference<PublishOrderActivity> mWeakReference;
        private BitmapHandler(PublishOrderActivity activity) {
            mWeakReference = new WeakReference<PublishOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PublishOrderActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)){
                            activity.k++;
                            if(activity.k==activity.imgPaths.size()){
                                if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                                    activity.customDialog.dismiss();
                                }
                                String navigation="发布订单";
                                Intent success=new Intent(activity.context,PublishSuccessActivity.class);
                                success.putExtra("navigation",navigation);
                                activity.startActivity(success);
                                activity.btnSendOrder.setEnabled(true);
                                activity.finish();
                            }
                        }else {
                            activity.btnSendOrder.setEnabled(true);
                            activity.onFailure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.btnSendOrder.setEnabled(true);
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
        //显示数据
        multiChoose.setList(mDataList);
    }
    private void initShow() {
        id= jsonObject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            String navigation="发布订单";
            Intent success=new Intent(context,PublishSuccessActivity.class);
            success.putExtra("navigation",navigation);
            startActivity(success);
            btnSendOrder.setEnabled(true);
            finish();
        }
    }
    private void compressImg(final File files) {
        //压缩的图片
        Luban.with(this)
                .load(files)
                .setCompressListener(new OnCompressListener() {
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
                        Toast.makeText(PublishOrderActivity.this,"图片压缩失败!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).launch();
    }
    private void uploadImage(File file) {
        String validateURL = UrlUtil.UploadImage_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> fileParams = new HashMap<String, File>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", id);
        fileParams.put("img",file);
        SendRequestUtil.postFileToServer(textParams,fileParams,validateURL,bitmapHandler);
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
        mMainType =data.getStringExtra("mMainType");
        type=data.getStringExtra("name");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userPhone =SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        //状态栏View
        findViewById(R.id.id_status_view).setVisibility(View.GONE);
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
               // cityId=data.getStringExtra("cityId");
               // latitude=data.getStringExtra("latitude");
               // longitude=data.getStringExtra("longitude");
                if (TextUtils.isEmpty(name)){
                    ttvName.setText(phone);
                }else {
                    ttvName.setText(name);
                }
                tvPhone.setText(phone);
                tvAddress.setText(mAddress);
            }
        }
    }
    private void initJudge() {
        if (TextUtils.isEmpty(VariableUtil.City)||(!VariableUtil.City.equals(CITY_NAME))){
            noticeDialog=new NoticeDialog(context);
            noticeDialog.show();
            mHandler.sendEmptyMessageDelayed(1, SPLASH_DELAY_MILLIS);
        }
    }
    private static class NoticeHandler extends Handler{
        private WeakReference<PublishOrderActivity> mWeakReference;
        private NoticeHandler(PublishOrderActivity activity) {
            mWeakReference = new WeakReference<PublishOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PublishOrderActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case 1:
                    if (activity.noticeDialog!=null){
                        activity.noticeDialog.dismiss();  //关闭弹窗
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void initView() {
        btnSendOrder =(Button)findViewById(R.id.id_btn_sendorder);
        etRecommend =(EditText)findViewById(R.id.id_et_recommand);
        ttvName =(TextView)findViewById(R.id.id_tv_name);
        tvPhone =(TextView) findViewById(R.id.id_tv_phone);
        etNeed =(EditText)findViewById(R.id.id_et_info);
        tvAddress =(TextView) findViewById(R.id.id_tv_address);
        ((TextView)findViewById(R.id.id_tv_project_type)).setText(mMainType);
        ((TextView)findViewById(R.id.id_tv_type)).setText(type);
        tvPhone.setText(userPhone);
        multiChoose=(MultiLineChooseLayout)findViewById(R.id.id_multiChoose);
        mPhotoGridView =(GridView)findViewById(R.id.noScrollgridview);
        appointmentData =(TextView)findViewById(R.id.id_data);
        appointmentTime =(TextView)findViewById(R.id.id_time);
        btnSendOrder.setEnabled(false);
    }
    private void initData() {
        customDialog.show();
        requestType =0;
        String validateURL = UrlUtil.RepairOrder_QuestionUrl;
        String getUrl=validateURL+"?rc_id="+reid;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(getUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private void initExecute() {
        mAdapter = new PhotoPickerAdapter(imgPaths);
        mPhotoGridView.setAdapter(mAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= 23) {
                    thisPosition =position;
                    onRequestLimitPhoto(position);
                } else {
                    if (position == imgPaths.size()) {
                        PhotoPicker.builder()
                                .setPhotoCount(4)
                                .setShowCamera(true)
                                .setSelected(imgPaths)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(PublishOrderActivity.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(PublishOrderActivity.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
                }
            }
        });

    }
    private void onRequestLimitPhoto(int position) {
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
                        .start(PublishOrderActivity.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(PublishOrderActivity.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }
    private void onShowPhoto() {
        if (thisPosition == imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(4)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(PublishOrderActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position", thisPosition);
            Intent intent=new Intent(PublishOrderActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, thisPosition);
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
                onShowPhoto();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(context,"授权失败");
            }
        }
    };
    private void initEvent() {
        findViewById(R.id.id_re_price).setOnClickListener(this);
        findViewById(R.id.id_re_selectaddre).setOnClickListener(this);
        btnSendOrder.setOnClickListener(this);
        appointmentData.setOnClickListener(this);
        appointmentTime.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        tvPhone.addTextChangedListener(myTextWatcher);
        tvAddress.addTextChangedListener(myTextWatcher);
        multiChoose.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                StringBuilder sb=new StringBuilder();
                multiResult=multiChoose.getAllItemSelectedTextWithListArray();
                if (multiResult!=null&&multiResult.size()>0){
                    for (int i=0;i<multiResult.size();i++){
                        sb.append(multiResult.get(i));
                        sb.append(",");
                    }
                    textString=sb.toString();
                }else {
                    textString="";
                }
            }
        });
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(tvAddress.getText().toString())||TextUtils.isEmpty(tvPhone.getText().toString())) {
                btnSendOrder.setEnabled(false);
            }else {
                btnSendOrder.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_price:
                onStartPriceTable();
                break;
            case R.id.id_data:
                onSelectData();
                break;
            case R.id.id_time:
                onSelectTime();
                break;
            case R.id.id_re_selectaddre:
                onSelectAddress();
                break;
            case R.id.id_btn_sendorder:
                //非海口地区业务未开通
                if (!VariableUtil.City.equals("海口")){
                    noticeDialog.show();
                    mHandler.sendEmptyMessageDelayed(1, SPLASH_DELAY_MILLIS);
                }else {
                    onOrderSend();
                }
                break;
            default:
                break;
        }
    }
    private void onStartPriceTable() {
        Intent price=new Intent(context,PriceMenuActivity.class);
        price.putExtra("reid",reid);
        startActivity(price);
    }
    private void onSelectData() {
        LayoutInflater l = LayoutInflater.from(this);
        View mPickView = l.inflate(R.layout.item_pickerdata_dialog, null);
        DatePicker datePicker=(DatePicker) mPickView.findViewById(R.id.id_date_picker);
        Calendar mCurrent=Calendar.getInstance();
        datePicker.init(mCurrent.get(Calendar.YEAR), mCurrent.get(Calendar.MONTH),  mCurrent.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {//滑动日期进行对日期的方位进行判断
                Calendar mAfter=Calendar.getInstance();
                Calendar mBefore=Calendar.getInstance();
                mAfter.add(Calendar.DAY_OF_MONTH,6);
                if (isDateAfter(view)) {
                    view.init(mAfter.get(Calendar.YEAR),mAfter.get(Calendar.MONTH),mAfter.get(Calendar.DAY_OF_MONTH), this);
                }
                if (isDateBefore(view)) {
                    view.init(mBefore.get(Calendar.YEAR), mBefore.get(Calendar.MONTH),mBefore.get(Calendar.DAY_OF_MONTH), this);
                }
            }
        });
        final String appointmentDataText=datePicker.getYear() + "-"+ (datePicker.getMonth()+1) + "-"
                + datePicker.getDayOfMonth();
        new PromptDialog.Builder(this)
                .setTitle("选择预约日期")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setView(mPickView)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        appointmentData.setText(appointmentDataText);
                        appointmentTime.setText("08:30-11:30");
                        dialog.dismiss();
                    }
                })
                .show();

    }
    private boolean isDateBefore(DatePicker tempView) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar temCalendar = Calendar.getInstance();
        temCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
        return temCalendar.before(mCalendar);
    }
    private boolean isDateAfter(DatePicker tempView) {
        Calendar mCalendar=Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH,6);
        Calendar temCalendar=Calendar.getInstance();
        temCalendar.add(Calendar.DAY_OF_MONTH,6);
        temCalendar.set(tempView.getYear(),tempView.getMonth(),tempView.getDayOfMonth(),0,0,0);
        return temCalendar.after(mCalendar);
    }
    private void onSelectTime() {
        final SelectTable selectTable=new SelectTable(context);
        final TextView tvTitle=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tvTitle.setText("选择时间");
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
                appointmentTime.setText(time);
                selectTable.dismiss();
            }
        });
    }
    private void onSelectAddress() {
        Intent intent=new Intent(context, SelectAddressActivity.class);
        startActivityForResult(intent,4);
    }
    private void onOrderSend() {
        String date= appointmentData.getText().toString().trim();
        String time= appointmentTime.getText().toString().trim();
        String otherInfo= etNeed.getText().toString().trim();
        username= ttvName.getText().toString().trim();
        recommend= etRecommend.getText().toString().trim();
        phone= tvPhone.getText().toString().trim();
        address= tvAddress.getText().toString().trim();
        appointDate =date+"  "+time;
        info=textString+otherInfo;
        final EnsurePublish ensurePublish=new EnsurePublish(this);
        ensurePublish.setTitleText("确认信息");
        ensurePublish.setNameText(username);
        ensurePublish.setTypeText(mMainType);
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
                requestType =1;
                btnSendOrder.setEnabled(false);
                requestService();
            }
        });
        ensurePublish.show();
    }
    private void requestService() {
        String source="";
        String validateURL = UrlUtil.PublishOrder_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("cid",reid);
        textParams.put("phone",phone);
        textParams.put("address",address);
        textParams.put("info",info);
        textParams.put("appointDate", appointDate);
        textParams.put("recommend_code",recommend);
        textParams.put("source",source);
        textParams.put("rawOrderId", rawOrderId);
        textParams.put("username",username);
        textParams.put("cityId", cityId);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
