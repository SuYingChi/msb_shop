package com.msht.minshengbao.androidShop;

import com.msht.minshengbao.BuildConfig;

public class ShopConstants {
    public static final String BASE_URL = BuildConfig.DEBUG ? /*"http://dev.msbapp.cn/mobile/"*/ "http://shop.msbapp.cn:8090/mobile/": /*"http://dev.msbapp.cn/mobile/"*/"http://shop.msbapp.cn:8090/mobile/";
    public static final String BASE_WAP_URL = BuildConfig.DEBUG ? /*"http://dev.msbapp.cn/wap/"*/"http://shop.msbapp.cn:8090/wap/": "http://shop.msbapp.cn:8090/wap/";
    public static final String RELEASE_BASE_PAY_URL = "https://msbapp.cn";
    public static final String DEBUG_BASE_PAY_URL = "http://test.msbapp.cn:8080";
    public static final String SHOP_SP = "shop";
    public static final String FILE_NAME = "Image";
    public static final String SHOP_HOME = BASE_URL + "index.php?act=index";
    public static final String LOGIN_SHOP = BASE_URL + "index.php?act=login";
    public static final String GOODS_DETAIL = BASE_URL + "index.php?act=goods&op=goods_detail";
    public static final String GOODS_DETAIL_HTML = BASE_URL + "index.php?act=goods&op=goods_body&goods_id=";
    public static final String EVALUATION = BASE_URL + "index.php?act=goods&op=goods_evaluate";
    public static final String CLASS_DETAIL_RIGHT = BASE_URL + "index.php?act=goods&op=goods_list";
    public static final String CLASS_DETAIL_LEFT = BASE_URL + "index.php?act=goods_class";
    public static final String KEYWORD_LIST = BASE_URL + "index.php?act=goods&op=goods_list";
    public static final String DEFAULT_SEARCH = BASE_URL + "index.php?act=index&op=search_key_list";
    public static final String ADD_CAR = BASE_URL + "index.php?act=member_cart&op=cart_add";
    public static final String CAR_LIST = BASE_URL + "index.php?act=member_cart&op=cart_list";
    public static final String MODIFY_CAR_NUM = BASE_URL + "index.php?act=member_cart&op=cart_edit_quantity";
    public static final String DELETE_CAR_ITEM = BASE_URL + "index.php?act=member_cart&op=cart_batchdel";
    public static final String BUY_STEP_1 = BASE_URL + "index.php?act=member_buy&op=buy_step1";
    public static final String GET_ADDRESS_LIST = BASE_URL + "index.php?act=member_address&op=address_list";
    public static final String EDIT_ADDRESS = BASE_URL + "index.php?act=member_address&op=address_edit";
    public static final String ADD_ADDRESS = BASE_URL + "index.php?act=member_address&op=address_add";
    public static final String DELETE_ADDRESS = BASE_URL+"index.php?act=member_address&op=address_del";
    public static final String AREA_ID_LIST = BASE_URL + "index.php?act=area&op=area_list";
    public static final String INV_LIST = BASE_URL + "index.php?act=member_invoice&op=invoice_list";
    public static final String DELETE_INV = BASE_URL + "index.php?act=member_invoice&op=invoice_del";
    public static final String ADD_INV = BASE_URL + "index.php?act=member_invoice&op=invoice_add";
    public static final String INV_CONTENT_LIST = BASE_URL + "index.php?act=member_invoice&op=invoice_content_list";
    public static final String BUY_STEP2_CHANGE_ADDRESS = BASE_URL + "index.php?act=member_buy&op=change_address";
    public static final String BUY_STEP2 = BASE_URL + "index.php?act=member_buy&op=buy_step2";
    public static final String BUY_STEP3 = BASE_URL + "index.php?act=member_buy&op=pay";
    public static final String BUY_STEP4 = BASE_URL + "index.php?act=member_payment&op=pay_new";
    public static final String NATIVE_GET_PAY_LIST = BuildConfig.DEBUG ? RELEASE_BASE_PAY_URL +"/api/app/pay_method": RELEASE_BASE_PAY_URL + "/api/app/pay_method";
    public static final String SHOP_PAY_SOURCE_PARAMAS = "app_shop_pay_method";
    public static final String SHOP_ORDER_LIST = BASE_URL + "index.php?act=member_order&op=order_list";
    public static final String ORDER_DETAIL = BASE_URL + "index.php?act=member_order&op=order_info";
    public static final String DELETE_ORDER = BASE_URL + "index.php?act=member_order&op=order_delete";
    public static final String CANCEL_ORDER = BASE_URL + "index.php?act=member_order&op=order_cancel";
    public static final String RECEIVE_ORDER = BASE_URL + "index.php?act=member_order&op=order_receive";
    public static final String QUERY_ORDER_ROUTE = BASE_URL + "index.php?act=member_order&op=search_deliver";
    public static final String ORDER_QR_CODE = BASE_URL + "index.php?act=member_order&op=order_qrcode";
    public static final String REFUND_LIST = BASE_URL + "index.php?act=member_refund&op=get_refund_list";
    public static final String RETURN_GOOD_LIST = BASE_URL + "index.php?act=member_return&op=get_return_list";
    public static final String REFUND_ORDER_DETAIL = BASE_URL + "index.php?act=member_refund&op=get_refund_info";
    public static final String RETURN_GOOD_ORDER_DETAIL = BASE_URL + "index.php?act=member_return&op=get_return_info";
    public static final String REFUND_ALL_FORM = BASE_URL + "index.php?act=member_refund&op=refund_all_form";
    public static final String REFUND_UPLOAD_PIC = BASE_URL + "index.php?act=member_refund&op=upload_pic";
    public static final String REFUND_ALL_POST = BASE_URL + "index.php?act=member_refund&op=refund_all_post";
    public static final String REFUN_FORM = BASE_URL + "index.php?act=member_refund&op=refund_form";
    public static final String REFUN_MONEY_OR_GOOD = BASE_URL + "index.php?act=member_refund&op=refund_post";
    public static final String SEARCH_DELIVER = BASE_URL + "index.php?act=member_order&op=search_deliver";
    public static final String INIT_EVELUATE = BASE_URL + "index.php?act=member_evaluate&op=index";
    public static final String UPLOAD_EVALUATE_PIC = BASE_URL + "index.php?act=sns_album&op=file_upload";
    public static final String POST_EVELUATE_ALL = BASE_URL + "index.php?act=member_evaluate&op=save";
    public static final String POST_ADD_EVELUATE_ALL = BASE_URL + "index.php?act=member_evaluate&op=save_again";
    public static final String INIT_ADD_EVELUATE = BASE_URL + "index.php?act=member_evaluate&op=again";
    public static final String SHOP_FOOTPRINT = BASE_URL + "index.php?act=member_goodsbrowse&op=browse_list";
    public static final String CLEAR_SHOP_FOOTPRINT = BASE_URL + "index.php?act=member_goodsbrowse&op=browse_clearall";
    public static final String SHOP_COLLECTION = BASE_URL + "index.php?act=member_favorites&op=favorites_list";
    public static final String DELETE_COLLECTION = BASE_URL + "index.php?act=member_favorites&op=favorites_del";
    public static final String ADD_COLLECTION = BASE_URL + "index.php?act=member_favorites&op=favorites_add";
    public static final String GET_SHARE_URL = BASE_URL + "index.php?act=goods&op=share_goods";
    public static final String IM_CHAT = BASE_WAP_URL + "tmpl/member/chat_info.html?";
    public static final String MSH_COUNT = BASE_URL + "index.php?act=member_chat&op=get_msg_count";
    public static final String CHAT_USER_LIST = BASE_URL+"index.php?act=member_chat&op=get_user_list";
    public static final String DELETE_MSG_USER_ITEM = BASE_URL+"index.php?act=member_chat&op=del_msg";
    public static final String GUESS_LIKE = BASE_URL+"index.php?act=member_goodsbrowse&op=guesslike";

    public static String getImChatUrl(String t_id, String key) {
        return IM_CHAT + "t_id=" + t_id + "&key=" + key;
    }
}
