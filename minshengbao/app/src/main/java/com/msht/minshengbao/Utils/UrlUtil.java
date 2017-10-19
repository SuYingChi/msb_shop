package com.msht.minshengbao.Utils;

/**
 * Created by hong on 2016/11/15.
 */
public class UrlUtil {
  //  public static final String URL_HEADS ="https://test.msbapp.cn";
   // public static final String URL_HEAD="http://test.msbapp.cn";
    public static final String URL_SHOP="http://shop.msbapp.cn:8090";
    public static final String URL_HEADS="https://msbapp.cn";
   // public static final String URL_HTTP="http://msbapp.cn";


    //商城
    public static final String Shop_LoginHtml=URL_SHOP+"/wap/tmpl/member/login.html";
    public static final String Shop_HomeUrl= URL_SHOP +"/wap/index.html";
    public static final String Shop_Login= URL_SHOP +"/mobile/index.php?act=login&op=appindex";
    /*首页功能 */
    public static final String App_versionUrl= URL_HEADS +"/Gas/app/version";
    public static final String Function_Url= URL_HEADS +"/api/serve_city/user_app_serve";
    public static final String Weather_QueryUrl= URL_HEADS +"/pis/weather/query";
    public static final String Inform_Url= URL_HEADS +"/Gas/message/list";
    public static final String Inform_delect= URL_HEADS +"/Gas/message/delete";
    public static final String Inform_detail= URL_HEADS +"/Gas/message/detail";
    public static final String Message_unreadUrl= URL_HEADS +"/Gas/message/unRead";
    public static final String pushDeviceToken= URL_HEADS +"/Gas/user/pushDeviceToken";
    public static final String Userinfo_GasUrl= URL_HEADS +"/Gas/user/info";
    public static final String Read_searchUserhouse= URL_HEADS +"/Gas/usedHouse/search";
    public static final String Pre_deposit_history= URL_HEADS +"/Gas/payment/pre_deposit_history";
    /*燃气服务模块 */
    //自助抄表
    public static final String InstallServer_Url= URL_HEADS +"/Gas/workOrder/add";
    public static final String InstallType_Url= URL_HEADS +"/Gas/app/gas_install_type";
    public static final String GetTable_Url= URL_HEADS +"/Gas/meter/rqb";
    public static final String GasTable_data= URL_HEADS +"/Gas/meter/list";
    public static final String SendTable_dataUrl= URL_HEADS +"/Gas/meter/add";
    public static final String Searchbill_GasUrl= URL_HEADS +"/Gas/debts/search";
    public static final String SelectAddress_Url= URL_HEADS +"/Gas/usedHouse/list";
    //ic卡
    public static final String IcRecharge_BillUrl=URL_HEADS+"/Gas/payment/icRecharge";
    public static final String IcRechargeSearch_Url=URL_HEADS+"/Gas/payment/icRechargeSearch";
    public static final String IcRechargeHistory_Url=URL_HEADS+"/Gas/payment/icRechargeHistory";
    //抢险
    public static final String GasQiangxian_Url= URL_HEADS +"/Gas/app/rescue";
    /*HTML5Web*/
    //燃气须知
    public static final String Companyprofile_Url= URL_HEADS +"/Gas/xuzhi/company";
    public static final String GasJiaoNa_Url= URL_HEADS +"/Gas/xuzhi/gas_payment";
    public static final String Gasprice_Url= URL_HEADS +"/gas_h5/qijiashuoming.html";
    public static final String GasSafety_Url= URL_HEADS +"/Gas/xuzhi/safe";
    public static final String GasToolUse_Url= URL_HEADS +"/Gas/xuzhi/gas_appliance";
    public static final String GasYeWu_Url= URL_HEADS +"/Gas/xuzhi/business_guid";
    public static final String uninstall_Url= URL_HEADS +"/Gas/xuzhi/uninstall";
    public static final String ServicePromise_Url= URL_HEADS +"/Gas/xuzhi/service_promise";
 //柜台业务
    public static final String Guitai_Url= URL_HEADS +"/Gas/guitai/guitai";
    public static final String YingyeSite_Url= URL_HEADS +"/Gas/guitai/wangdian";
    //缴费
    public static final String AgreeTreayt_Url= URL_HEADS +"/repair_h5/regist_agreement.html";
    public static final String AutomataPay_Url= URL_HEADS +"/Gas/gasWithhold/list";
    public static final String Addautomate_AddUrl= URL_HEADS +"/Gas/gasWithhold/add";
    public static final String DelectAutopay_AddUrl= URL_HEADS +"/Gas/gasWithhold/delete";
    public static final String Mywallet_balanceUrl= URL_HEADS +"/Gas/wallet/balance";
    public static final String MyIncome_ExpenseUrl= URL_HEADS +"/Gas/wallet/history";
    public static final String PayfeeWay_Url= URL_HEADS +"/Gas/payment/createOrder";
    public static final String PayRecors_HistoryUrl= URL_HEADS +"/Gas/payment/customerno_history";
    public static final String PayCustomerNo_Url= URL_HEADS +"/Gas/payment/app_pay_customerno";
    public static final String Voucher_CanuseUrl= URL_HEADS +"/Gas/coupon/can_use_list";
    /*城市服务 */
    public static final String SelectCity_Url= URL_HEADS +"/api/serve_city/list";
    //违章查询

    public static final String BrulesInfoma_Url= URL_HEADS +"/pis/peccancy/query";
    public static final String GdetailInfo_Url= URL_HEADS +"/pis/info/view";

    //Fragment
    public static final String Evalute_UrL= URL_HEADS +"/Gas/repairman/evaluate_list";
    public static final String maintainservise_Url= URL_HEADS +"/Gas/repairOrder/list";
    public static final String AllMyservice_Url= URL_HEADS +"/Gas/workOrder/userWorkOrder";
    public static final String CityDynamic_Url= URL_HEADS +"/pis/info/list";
    public static final String Counpon_Url= URL_HEADS +"/Gas/coupon/list";
    public static final String imgavatar_Url= URL_HEADS +"/Gas/app/headerImg";
    public static final String PayRecord_Url= URL_HEADS +"/Gas/payment/list";


    /*保险*/
    public static final String Insurance_buy_Url= URL_HEADS +"/Gas/insurance/create_order";
    public static final String Insurance_history_Url= URL_HEADS +"/Gas/invoice/history";
    /*myView   */
    public static final String AddAdress_Url= URL_HEADS +"/Gas/usedHouse/add";
    public static final String HouseSearch_Url= URL_HEADS +"/Gas/usedHouse/search";
    public static final String Address_delectUrl= URL_HEADS +"/Gas/usedHouse/delete";
    public static final String Evalute_workUrl= URL_HEADS +"/Gas/workOrder/eval";
    public static final String FeedbackIdea_Url= URL_HEADS +"/Gas/user/feedback";
    public static final String Captcha_CodeUrl= URL_HEADS +"/Gas/user/captcha";
    public static final String Resetpwd_Url= URL_HEADS +"/Gas/user/passwRecover";
    public static final String Login_Url= URL_HEADS +"/Gas/user/login";
    public static final String Servirce_detailUrl= URL_HEADS +"/Gas/workOrder/view";
    public static final String GasCancel_workUrl= URL_HEADS +"/Gas/workOrder/cancel";
    public static final String GasmodifyInfo_Url= URL_HEADS +"/Gas/user/modifyInfo";
    public static final String Register_Url= URL_HEADS +"/Gas/user/register";
    public static final String Shara_appUrl= URL_HEADS +"/Gas/coupon/share_add";
    public static final String BoundCustomerNo_URL= URL_HEADS +"/Gas/usedHouse/bind";
    public static final String AddressManage_Url= URL_HEADS +"/Gas/address/list";
    public static final String AddAddress_Url= URL_HEADS +"/Gas/address/add";
    public static final String ModifyAddress_Url= URL_HEADS +"/Gas/address/modify";
    public static final String DelectAddress_Url= URL_HEADS +"/Gas/address/delete";
    /*发票*/
    public static final String Invoice_explain= URL_HEADS +"/repair_h5/invoice_note.html";
    public static final String Invoice_applyUrl= URL_HEADS +"/Gas/invoice/apply";
    public static final String Invoice_getUrl= URL_HEADS +"/Gas/invoice/uninvoce_list";//获取未开发票列表
    /*维修服务*/
    public static final String Service_noteUrl= URL_HEADS +"/repair_h5/service_note.html";
    public static final String RepairOrder_detailUrl= URL_HEADS +"/Gas/repairOrder/view";
    public static final String RepairOrder_cancelUrl= URL_HEADS +"/Gas/repairOrder/cancel";
    public static final String MasterDetail_Url= URL_HEADS +"/Gas/repairman/detail";
    public static final String PublishOrder_Url= URL_HEADS +"/Gas/repairOrder/add";
    public static final String UploadImage_Url= URL_HEADS +"/Gas/repairOrder/addImage";
    public static final String RepairOrder_EvalUrl= URL_HEADS +"/Gas/repairOrder/eval";
    public static final String RepairOrder_QuestionUrl= URL_HEADS +"/Gas/repairCategoryProblem/problem_list";
    public static final String RepairMater_countUrl= URL_HEADS +"/Gas/repairman/evaluate_status_count";
    public static final String SecondService_Url= URL_HEADS +"/api/serve_city/sub_serve";
    public static final String LookEvalute_Url= URL_HEADS +"/Gas/repairOrder/eval_info";
    public static final String RefundApply_Url= URL_HEADS +"/Gas/repairOrder/refund";
    public static final String RefundImg_Url= URL_HEADS +"/Gas/repairOrder/refund_img";
    /*热门维修 */
    public static final String HotRepair_Url= URL_HEADS +"/api/serve_city/hot_repair";
    //HTML5页面
    public static final String Replacepay_agreeUrl= URL_HEADS +"/gas_h5/daikouxieyi.html";
}
