package com.msht.minshengbao.androidShop.presenter;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.activity.ShopOrderEveluateActivity;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailLeftBean;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.MyAddEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;
import com.msht.minshengbao.androidShop.shopBean.MyEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.shopBean.ShopkeywordBean;
import com.msht.minshengbao.androidShop.util.DataStringCallback;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IAddAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IAddCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.IAddCompanyNormalInvView;
import com.msht.minshengbao.androidShop.viewInterface.IAddInvView;
import com.msht.minshengbao.androidShop.viewInterface.IAddPersonalInvInvView;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep1View;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep2View;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep3GetPayListView;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep4CreatChargeView;
import com.msht.minshengbao.androidShop.viewInterface.ICancelOrderView;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IChangeAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IClearShopFootprintView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteCarItemView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteMsgUserItemView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IEvaluationView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAddressListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAreaListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetChatUserListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetMsgCountView;
import com.msht.minshengbao.androidShop.viewInterface.IGetShareUrlView;
import com.msht.minshengbao.androidShop.viewInterface.IGuessLikeGoodListView;
import com.msht.minshengbao.androidShop.viewInterface.IKeyWordListView;
import com.msht.minshengbao.androidShop.viewInterface.IModifyCarGoodNumView;
import com.msht.minshengbao.androidShop.viewInterface.IEditAddressView;
import com.msht.minshengbao.androidShop.viewInterface.INativGetPayListView;
import com.msht.minshengbao.androidShop.viewInterface.IOrderQrCodeView;
import com.msht.minshengbao.androidShop.viewInterface.IPostAddEvelateAllView;
import com.msht.minshengbao.androidShop.viewInterface.IPostEvelateAllView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundAllView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundPicView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundView;
import com.msht.minshengbao.androidShop.viewInterface.IQueryOrderRouteView;
import com.msht.minshengbao.androidShop.viewInterface.IReceivedOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundAllFormView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundFormView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundGoodDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundGoodView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundMoneyDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundMoneyView;
import com.msht.minshengbao.androidShop.viewInterface.ISearchDeliverView;
import com.msht.minshengbao.androidShop.viewInterface.IShopClassDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IGetShopFootprintView;
import com.msht.minshengbao.androidShop.viewInterface.IShopCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.IShopDeleteCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;
import com.msht.minshengbao.androidShop.viewInterface.ILoginShopView;
import com.msht.minshengbao.androidShop.viewInterface.IShopInitAddEveluateView;
import com.msht.minshengbao.androidShop.viewInterface.IShopInitEveluateView;
import com.msht.minshengbao.androidShop.viewInterface.IShopMainView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrderDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrdersNumView;
import com.msht.minshengbao.androidShop.viewInterface.IUploadEveluatePicView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrdersView;
import com.msht.minshengbao.androidShop.viewInterface.IShopSearchView;
import com.msht.minshengbao.androidShop.viewInterface.IdeleteInvItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ShopPresenter {

    public static void loginShop(String usrname, String password, final ILoginShopView iLoginShopView) {
        //测试账号
        OkHttpUtils.post().url(ShopConstants.LOGIN_SHOP).addParams("username",usrname).addParams("password", password).addParams("client", "android").build().execute(new DataStringCallback(iLoginShopView) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iLoginShopView.onLoginShopSuccess(s);
                }
            }

        });
    }

    public static void getGoodDetail(final IShopGoodDetailView iShopGoodDetailView) {
        OkHttpUtils.get().url(ShopConstants.GOODS_DETAIL).addParams("goods_id", iShopGoodDetailView.getGoodsid()).addParams("key", iShopGoodDetailView.getKey()).build().execute(new DataStringCallback(iShopGoodDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopGoodDetailView.onGetGoodDetailSuccess(s);
                }
            }
        });
    }

    public static void getEvaluation(final IEvaluationView iEvaluationView) {
        OkHttpUtils.get().url(ShopConstants.EVALUATION).addParams("goods_id", iEvaluationView.getGoodsId()).addParams("type", iEvaluationView.getType()).addParams("page", iEvaluationView.getPage()).addParams("curpage", iEvaluationView.getCurrenPage()).build().execute(new DataStringCallback(iEvaluationView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iEvaluationView.onSuccess(s);
                }
            }
        });
    }


    public static void getClassDetailLeft(final IShopClassDetailView iShopClassDetailView) {
        OkHttpUtils.get().url(ShopConstants.CLASS_DETAIL_LEFT).addParams("gc_id", iShopClassDetailView.getGcId()).build().execute(new DataStringCallback(iShopClassDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    ClassDetailLeftBean bean = JsonUtil.toBean(s, ClassDetailLeftBean.class);
                    List<ClassDetailLeftBean.DatasBean.ClassListBean> list = bean.getDatas().getClass_list();
                    ArrayList<MyClassListBean> myList = new ArrayList<MyClassListBean>();
                    MyClassListBean myClassListBean;
                    for (ClassDetailLeftBean.DatasBean.ClassListBean classListBean : list) {
                        myClassListBean = new MyClassListBean();
                        myClassListBean.setGc_name(classListBean.getGc_name());
                        myClassListBean.setGc_id(classListBean.getGc_id());
                        if (list.indexOf(classListBean) == 0) {
                            myClassListBean.setIsSelected(true);
                        } else {
                            myClassListBean.setIsSelected(false);
                        }
                        myList.add(myClassListBean);
                    }
                    iShopClassDetailView.onLeftSuccess(myList);
                }
            }
        });
    }

    public static void getClassDetailRight(final IShopClassDetailView iShopClassDetailView) {
        OkHttpUtils.get().url(ShopConstants.CLASS_DETAIL_RIGHT).addParams("gc_id", iShopClassDetailView.getRightGcId()).addParams("page", iShopClassDetailView.getRightCurrenPage()).addParams("curpage", iShopClassDetailView.getCurrenPage()).build().execute(new DataStringCallback(iShopClassDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    ClassDetailRightBean bean = JsonUtil.toBean(s, ClassDetailRightBean.class);
                    List<ClassDetailRightBean.DatasBean.GoodsListBean> list = bean.getDatas().getGoods_list();
                    iShopClassDetailView.onRightRclSuccess(list, bean.getPage_total());
                }
            }
        });
    }

    public static void getKeywordList(final IKeyWordListView iKeyWordListView) {
        OkHttpUtils.get().url(ShopConstants.KEYWORD_LIST).addParams("keyword", iKeyWordListView.getKeyword()).addParams("order", iKeyWordListView.order()).addParams("page", iKeyWordListView.getPage()).addParams("curpage", iKeyWordListView.getCurrenPage()).addParams("key", iKeyWordListView.orderKey()).build().execute(new DataStringCallback(iKeyWordListView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    ShopkeywordBean bean = JsonUtil.toBean(s, ShopkeywordBean.class);
                    List<ShopkeywordBean.DatasBean.GoodsListBean> list = bean.getDatas().getGoods_list();
                    iKeyWordListView.onSuccess(list, bean.getPage_total());
                }
            }
        });
    }

    public static void getSearchSuccess(final IShopSearchView iShopSearchView,String key) {
        OkHttpUtils.post().url(ShopConstants.DEFAULT_SEARCH).addParams("key", key).build().execute(new DataStringCallback(iShopSearchView) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopSearchView.onGetDefaultSuccess(s);
                }
            }

        });
    }
    public static void getSearchSuccess(final IShopSearchView iShopSearchView) {
        OkHttpUtils.post().url(ShopConstants.DEFAULT_SEARCH).build().execute(new DataStringCallback(iShopSearchView) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopSearchView.onGetDefaultSuccess(s);
                }
            }

        });
    }
    public static void addCar(final IShopGoodDetailView iShopGoodDetailView) {
        OkHttpUtils.post().url(ShopConstants.ADD_CAR).addParams("key", iShopGoodDetailView.getKey()).addParams("goods_id", iShopGoodDetailView.getGoodsid()).addParams("quantity", iShopGoodDetailView.getSelectedGoodNum() + "").build().execute(new DataStringCallback(iShopGoodDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopGoodDetailView.onAddCarSuccess(s);
                }
            }
        });
    }

    public static void getCarList(final ICarListView iCarListView, final boolean isShowLoadingDialog) {
        OkHttpUtils.post().url(ShopConstants.CAR_LIST).addParams("key", iCarListView.getKey()).build().execute(new DataStringCallback(iCarListView, isShowLoadingDialog) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iCarListView.onGetCarListSuccess(s);
                }
            }
        });
    }

    public static void getShopHome(final IShopMainView iShopMainView) {
        OkHttpUtils.get().url(ShopConstants.SHOP_HOME).build().execute(new DataStringCallback(iShopMainView) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopMainView.onGetShopHomeSuccess(s);
                }
            }

        });
    }

    public static void modifyGoodNum(final IModifyCarGoodNumView iModifyCarGoodNumView, final ICarListView iCarListView) {
        OkHttpUtils.post().url(ShopConstants.MODIFY_CAR_NUM).addParams("key", iModifyCarGoodNumView.getKey()).addParams("cart_id", iModifyCarGoodNumView.getCarId()).addParams("quantity", iModifyCarGoodNumView.getCarItemNum()).build().execute(new DataStringCallback(iModifyCarGoodNumView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iModifyCarGoodNumView.onModifyGoodNumSuccess(s);
                }
            }
        });
    }

    public static void deleteCarItems(final IDeleteCarItemView iDeleteCarItemView) {
        OkHttpUtils.post().url(ShopConstants.DELETE_CAR_ITEM).addParams("key", iDeleteCarItemView.getKey()).addParams("cartlist", iDeleteCarItemView.getSelectCartList()).build().execute(new DataStringCallback(iDeleteCarItemView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iDeleteCarItemView.onDeleteCarItemsSuccess(s);
                }
            }
        });
    }

    public static void buyStep1(final IBuyStep1View iBuyStep1View) {
        OkHttpUtils.post().url(ShopConstants.BUY_STEP_1).addParams("key", iBuyStep1View.getKey()).addParams("cart_id", iBuyStep1View.getCarId()).addParams("ifcart", iBuyStep1View.ifCarted()).addParams("ifpickup_self", iBuyStep1View.ifPickupSelf()).addParams("address_id", iBuyStep1View.getAddressid()).build().execute(new DataStringCallback(iBuyStep1View) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iBuyStep1View.onBuyStep1Success(s);
                }
            }
        });
    }

    public static void getAddressList(final IGetAddressListView iGetAddressListView, boolean isShowDialog) {
        OkHttpUtils.post().url(ShopConstants.GET_ADDRESS_LIST).addParams("key", iGetAddressListView.getKey()).build().execute(new DataStringCallback(iGetAddressListView, isShowDialog) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetAddressListView.onGetAddressListSuccess(s);
                }
            }
        });
    }

    public static void editAddress(final IEditAddressView iEditAddressView) {
        OkHttpUtils.post().url(ShopConstants.EDIT_ADDRESS).addParams("key", iEditAddressView.getKey())
                .addParams("address_id", iEditAddressView.getAddress_id())
                .addParams("true_name", iEditAddressView.getTrue_name())
                .addParams("city_id", iEditAddressView.getCity_id())
                .addParams("area_id", iEditAddressView.getArea_id())
                .addParams("area_info", iEditAddressView.getArea_info())
                .addParams("address", iEditAddressView.getAddress())
                .addParams("tel_phone", iEditAddressView.getTel_phone())
                .addParams("mob_phone", iEditAddressView.getMob_phone())
                .addParams("is_default", iEditAddressView.isDefault())
                .build().execute(new DataStringCallback(iEditAddressView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iEditAddressView.onEditAddressSuccess();
                }
            }
        });
    }

    public static void addAddress(final IAddAddressView iAddAddressView) {
        OkHttpUtils.post().url(ShopConstants.ADD_ADDRESS).addParams("key", iAddAddressView.getKey())
                .addParams("true_name", iAddAddressView.getTrue_name())
                .addParams("city_id", iAddAddressView.getCity_id())
                .addParams("area_id", iAddAddressView.getArea_id())
                .addParams("area_info", iAddAddressView.getArea_info())
                .addParams("address", iAddAddressView.getAddress())
                .addParams("tel_phone", iAddAddressView.getTel_phone())
                .addParams("mob_phone", iAddAddressView.getMob_phone())
                .addParams("is_default", iAddAddressView.isDefault())
                .build().execute(new DataStringCallback(iAddAddressView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iAddAddressView.onAddAddressSuccess(s);
                }
            }
        });
    }
    public static void deleteAddress(final IDeleteAddressView iDeleteAddressView) {
        OkHttpUtils.post().url(ShopConstants.DELETE_ADDRESS).addParams("key", iDeleteAddressView.getKey())
                .addParams("address_id", iDeleteAddressView.getDeleteAddressId())
                .build().execute(new DataStringCallback(iDeleteAddressView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iDeleteAddressView.onDeleteAddressSuccess(s);
                }
            }
        });
    }
   /* public static void getAreaList(final IGetAreaListView iGetAreaListView, final String  area_id,final int level,final int provincePosition,final int cityPosition) {
        OkHttpUtils.get().url(ShopConstants.AREA_ID_LIST).addParams("area_id",area_id).build().execute(new DataStringCallback(iGetAreaListView) {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LogUtils.e("onBefore","level=="+level);
            }

            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetAreaListView.onGetAreaListSuccess(s,area_id,level,provincePosition,cityPosition);
                }
            }

        });
    }*/

    public static void getProvinceAreaList(final IGetAreaListView iGetAreaListView) {
        OkHttpUtils.get().url(ShopConstants.AREA_ID_LIST).addParams("area_id", "").build().execute(new DataStringCallback(iGetAreaListView, false) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetAreaListView.onGetProviceListSuccess(s);
                }
            }

        });
    }

    public static void getCityList(final IGetAreaListView iGetAreaListView, String area_id) {
        OkHttpUtils.get().url(ShopConstants.AREA_ID_LIST).addParams("area_id", area_id).build().execute(new DataStringCallback(iGetAreaListView, false) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetAreaListView.onGetCityListSuccess(s);
                }
            }

        });
    }

    public static void getAreaList(final IGetAreaListView iGetAreaListView, String area_id) {
        OkHttpUtils.get().url(ShopConstants.AREA_ID_LIST).addParams("area_id", area_id).build().execute(new DataStringCallback(iGetAreaListView, false) {
            @Override
            public void onResponse(String s, int i) {
                //先继承再重写或重写覆盖请求错误的场景
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetAreaListView.onGetAreaListSuccess(s);
                }
            }

        });
    }

    public static void getInvList(final IGetInvListView iGetInvListView) {
        OkHttpUtils.post().url(ShopConstants.INV_LIST).addParams("key", iGetInvListView.getKey()).build().execute(new DataStringCallback(iGetInvListView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetInvListView.onGetInvList(s);
                }
            }
        });
    }

    public static void getInvContentList(final IGetInvContentView iGetInvContentView) {
        OkHttpUtils.post().url(ShopConstants.INV_CONTENT_LIST).addParams("key", iGetInvContentView.getKey()).build().execute(new DataStringCallback(iGetInvContentView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetInvContentView.onGetInvContentList(s);
                }
            }
        });
    }

    public static void deleteInvItem(final IdeleteInvItemView ideleteInvItemView) {
        OkHttpUtils.post().url(ShopConstants.DELETE_INV).addParams("key", ideleteInvItemView.getKey()).addParams("inv_id", ideleteInvItemView.getInvId()).build().execute(new DataStringCallback(ideleteInvItemView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    ideleteInvItemView.onDeleteInvSuccess(s);
                }
            }
        });
    }

    public static void addPersonalInvItem(final IAddPersonalInvInvView iAddInvView) {
        OkHttpUtils.post().url(ShopConstants.ADD_INV).addParams("key", iAddInvView.getKey())
                .addParams("inv_title_select", "person")
                .addParams("new_inv_type", "1")
                .addParams("new_inv_rec_id", iAddInvView.getRecId())
                .addParams("inv_content", iAddInvView.getInv_content())
                .build().execute(new DataStringCallback(iAddInvView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iAddInvView.onAddPersonalInvSuccess(s);
                }
            }
        });
    }

    public static void addCompanyNormalInvItem(final IAddCompanyNormalInvView iAddInvView) {
        OkHttpUtils.post().url(ShopConstants.ADD_INV).addParams("key", iAddInvView.getKey())
                .addParams("inv_title_select", "company")
                .addParams("new_inv_type", iAddInvView.getCompanyInvType())
                .addParams("new_inv_rec_id", iAddInvView.getCNRecId())
                .addParams("inv_title", iAddInvView.CNinv_title())
                .addParams("new_inv_sbh", iAddInvView.getCNSbh())
                .addParams("new_inv_bank", iAddInvView.getCNBank())
                .addParams("new_inv_banknum", iAddInvView.getCNBanknum())
                .addParams("new_inv_comtel", iAddInvView.getCNComtel())
                .addParams("new_inv_comaddr", iAddInvView.getCNComaddr())
                .addParams("inv_content", iAddInvView.getCNInv_content())
                .build().execute(new DataStringCallback(iAddInvView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iAddInvView.onAddCompanyNormalSuccess(s);
                }
            }
        });
    }

    public static void addCompanyZengZhiInvItem(final IAddInvView iAddInvView, List<File> files) {
        PostFormBuilder getBuilder = OkHttpUtils.post().url(ShopConstants.ADD_INV).addParams("key", iAddInvView.getKey())
                .addParams("inv_title_select ", "company")
                .addParams("new_inv_type", "2")
                .addParams("new_inv_rec_id", iAddInvView.getRecId())
                .addParams("CNinv_title", iAddInvView.getRecId())
                .addParams("new_inv_sbh", iAddInvView.getSbh())
                .addParams("new_inv_bank", iAddInvView.getBank())
                .addParams("new_inv_banknum", iAddInvView.getBanknum())
                .addParams("new_inv_comtel", iAddInvView.getComtel())
                .addParams("new_inv_comaddr", iAddInvView.getComaddr())
                .addParams("inv_content", iAddInvView.getInv_content());
        for (File file : files) {
            getBuilder = getBuilder.addFile("new_inv_license_image[]", file.getName(), file);
        }
        getBuilder.build().execute(new DataStringCallback(iAddInvView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iAddInvView.onAddCompanyNormalSuccess(s);
                }
            }
        });
    }

    public static void buyStep2ChangeAddress(final IChangeAddressView iChangeAddressView) {
        LogUtils.e(Log.getStackTraceString(new Throwable()));
        OkHttpUtils.post().url(ShopConstants.BUY_STEP2_CHANGE_ADDRESS).addParams("key", iChangeAddressView.getKey())
                .addParams("freight_hash", iChangeAddressView.getFreight_hash())
                .addParams("city_id", iChangeAddressView.getCityId())
                .addParams("area_id", iChangeAddressView.getAreaId())
                .build().execute(new DataStringCallback(iChangeAddressView, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iChangeAddressView.onChangeAddressSuccess(s);
                }
            }
        });
    }

    public static void buyStep2(final IBuyStep2View iBuyStep2View, String carid, String recommend_phone, String ifCarted, String ifpickup_self, String addressId, String vat_hash, String offpay_hash, String offpay_hash_batch) {
        OkHttpUtils.post().url(ShopConstants.BUY_STEP2).addParams("key", iBuyStep2View.getKey())
                .addParams("ifcart", ifCarted)
                .addParams("cart_id", carid)
                .addParams("address_id", addressId)
                .addParams("ifpickup_self", ifpickup_self)
                .addParams("delivery", iBuyStep2View.getDelivery())
                .addParams("recommend_phone", recommend_phone)
                .addParams("vat_hash", vat_hash)
                .addParams("offpay_hash", offpay_hash)
                .addParams("offpay_hash_batch", offpay_hash_batch)
                .addParams("pay_name", iBuyStep2View.getPayName())
                .addParams("invoice_id", iBuyStep2View.getInvoiceIds())
                .addParams("voucher", iBuyStep2View.getVoucher())
                .addParams("pd_pay", iBuyStep2View.getPd_pay())
                .addParams("password", iBuyStep2View.getPassword())
                .addParams("fcode", iBuyStep2View.getFcode())
                .addParams("rcb_pay", iBuyStep2View.getRcb_pay())
                .addParams("rpt", iBuyStep2View.getRpt())
                .addParams("pay_message", iBuyStep2View.getPay_message())
                .build().execute(new DataStringCallback(iBuyStep2View, false) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iBuyStep2View.onBuyStep2Success(s);
                }
            }
        });
    }

    public static void buyStep3(final IBuyStep3GetPayListView iBuyStep3GetPayListView,String paySn) {
        OkHttpUtils.post().url(ShopConstants.BUY_STEP3).addParams("key", iBuyStep3GetPayListView.getKey())
                .addParams("pay_sn",paySn)
                .build().execute(new DataStringCallback(iBuyStep3GetPayListView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iBuyStep3GetPayListView.onBuyStep3(s);
                }
            }
        });
    }

    public static void creatCharge(final IBuyStep4CreatChargeView iBuyStep4CreatChargeView) {
        OkHttpUtils.get().url(ShopConstants.BUY_STEP4).addParams("key", iBuyStep4CreatChargeView.getKey())
                .addParams("pay_sn", iBuyStep4CreatChargeView.getPay_sn())
                .addParams("payment_code", iBuyStep4CreatChargeView.getPayment_code())
                .addParams("password", iBuyStep4CreatChargeView.getLoginPassword())
                .addParams("userId", iBuyStep4CreatChargeView.getUserId())
                .addParams("rcb_pay", iBuyStep4CreatChargeView.getRcb_pay())
                .addParams("pd_pay", iBuyStep4CreatChargeView.getPd_pay())
                .addParams("pay_amount", iBuyStep4CreatChargeView.getPayAmount())
                .addParams("channel", iBuyStep4CreatChargeView.getChannel())
                .build().execute(new DataStringCallback(iBuyStep4CreatChargeView) {
            @Override
            public void onResponse(String s, int i) {
                if (isShowLoadingDialog) {
                    iView.dismissLoading();
                }
                if (TextUtils.isEmpty(s) || TextUtils.equals("\"\"", s)) {
                    isResponseSuccess = false;
                    iView.onError("接口返回空字符串");
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (obj.optString("result").equals("success")) {
                            iBuyStep4CreatChargeView.onCreatChargedSuccess(s);
                        } else {
                            iView.onError("创建charge失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getNativPayList(final INativGetPayListView iNativGetPayListView) {
        OkHttpUtils.post().url(ShopConstants.NATIVE_GET_PAY_LIST)
                .addParams("source", ShopConstants.SHOP_PAY_SOURCE_PARAMAS)
                .build().execute(new DataStringCallback(iNativGetPayListView) {
            @Override
            public void onResponse(String s, int i) {
                if (isShowLoadingDialog) {
                    iView.dismissLoading();
                }
                if (TextUtils.isEmpty(s) || TextUtils.equals("\"\"", s)) {
                    isResponseSuccess = false;
                    iView.onError("接口返回空字符串");
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (obj.optString("result").equals("success")) {
                            iNativGetPayListView.onGetNativePayListSuccess(s);
                        } else {
                            iView.onError("請求支付方式錯誤");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getShopOrdersList(final IShopOrdersView iShopOrdersView, boolean b) {
        OkHttpUtils.post().url(iShopOrdersView.getUrl()).addParams("key", iShopOrdersView.getKey())
                .addParams("state_type", iShopOrdersView.getState_type())
                .build().execute(new DataStringCallback(iShopOrdersView,b) {
            @Override
            public void onResponse(String s, int i) {
                if (isShowLoadingDialog) {
                    iView.dismissLoading();
                }
                if (TextUtils.isEmpty(s) || TextUtils.equals("\"\"", s)) {
                    isResponseSuccess = false;
                    iView.onError("接口返回空字符串");
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (obj.optString("code").equals("200")) {
                            iShopOrdersView.onGetShopOrdersList(s);
                        } else {
                            iView.onError("请求订单列表数据错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void getShopOrdersList(final IShopOrdersNumView iShopOrdersNumView, String state_type, DataStringCallback dataStringCallback) {
        OkHttpUtils.post().url(ShopConstants.SHOP_ORDER_LIST + "&page=1000&curpage="+1).addParams("key", iShopOrdersNumView.getKey())
                .addParams("state_type",state_type)
                .build().execute(dataStringCallback);
    }
    public static void getOrderDetail(final IShopOrderDetailView iShopOrderDetailView) {
        OkHttpUtils.get().url(ShopConstants.ORDER_DETAIL).addParams("key", iShopOrderDetailView.getKey())
                .addParams("order_id", iShopOrderDetailView.getOrderId())
                .build().execute(new DataStringCallback(iShopOrderDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopOrderDetailView.onGetDetailSuccess(s);
                }
            }
        });
    }

    public static void deleteOrder(final IDeleteOrderView iDeleteOrderView, final String orderId) {
        OkHttpUtils.post().url(ShopConstants.DELETE_ORDER).addParams("key", iDeleteOrderView.getKey())
                .addParams("order_id", orderId)
                .build().execute(new DataStringCallback(iDeleteOrderView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iDeleteOrderView.onDeleteOrderSuccess(orderId);
                }
            }
        });
    }

    public static void getRefundAllForm(final IRefundAllFormView iRefundAllFormView) {
        OkHttpUtils.get().url(ShopConstants.REFUND_ALL_FORM).addParams("key", iRefundAllFormView.getKey())
                .addParams("order_id", iRefundAllFormView.getOrderId())
                .build().execute(new DataStringCallback(iRefundAllFormView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundAllFormView.onGetRefundAllFormSuccess(s);
                }
            }
        });
    }

    public static void cancelOrder(final ICancelOrderView iCancelOrderView, String orderId) {
        OkHttpUtils.post().url(ShopConstants.CANCEL_ORDER).addParams("key", iCancelOrderView.getKey())
                .addParams("order_id", orderId)
                .build().execute(new DataStringCallback(iCancelOrderView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iCancelOrderView.onCancelOrderSuccess(s);
                }
            }
        });
    }

    public static void receivedOrder(final IReceivedOrderView iCancelOrderView, final String orderId) {
        OkHttpUtils.post().url(ShopConstants.RECEIVE_ORDER).addParams("key", iCancelOrderView.getKey())
                .addParams("order_id", orderId)
                .build().execute(new DataStringCallback(iCancelOrderView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iCancelOrderView.onReceiveOrderSuccess(s,orderId);
                }
            }
        });
    }

    public static void queryOrderRoute(final IQueryOrderRouteView iQueryOrderRouteView) {
        OkHttpUtils.post().url(ShopConstants.QUERY_ORDER_ROUTE).addParams("key", iQueryOrderRouteView.getKey())
                .addParams("order_id", iQueryOrderRouteView.getOrderId())
                .build().execute(new DataStringCallback(iQueryOrderRouteView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iQueryOrderRouteView.onQueryOrderRouteSuccess(s);
                }
            }
        });
    }

    public static void getReceiveQrCodeImage(final IOrderQrCodeView iOrderQrCodeView,String orderId) {
        OkHttpUtils.get().url(ShopConstants.ORDER_QR_CODE).addParams("key", iOrderQrCodeView.getKey())
                .addParams("order_id",orderId )
                .build().execute(new DataStringCallback(iOrderQrCodeView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iOrderQrCodeView.onGetOrderQrCodeSuccess(s);
                }
            }
        });
    }

    public static void getRefundMoneyList(final IRefundMoneyView iRefundMoneyView) {
        OkHttpUtils.get().url(ShopConstants.REFUND_LIST).addParams("key", iRefundMoneyView.getKey())
                .addParams("curpage", iRefundMoneyView.getCurpage())
                .addParams("page", "10")
                .build().execute(new DataStringCallback(iRefundMoneyView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundMoneyView.onGetRefundMoneyListSuccess(s);
                }
            }
        });
    }
    public static void getRefundMoneyList(final IShopOrdersNumView iRefundMoneyView,DataStringCallback dataStringCallback) {
        OkHttpUtils.get().url(ShopConstants.REFUND_LIST).addParams("key", iRefundMoneyView.getKey())
                .addParams("curpage", "1")
                .addParams("page", "1000")
                .build().execute(dataStringCallback);
    }
    public static void getRefundGoodList(final IRefundGoodView iRefundGoodView) {
        OkHttpUtils.get().url(ShopConstants.RETURN_GOOD_LIST).addParams("key", iRefundGoodView.getKey())
                .addParams("curpage", iRefundGoodView.getCurpage())
                .addParams("page", "10")
                .build().execute(new DataStringCallback(iRefundGoodView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundGoodView.onGetRetrunGoodListSuccess(s);
                }
            }
        });
    }
    public static void getRefundGoodList(final IShopOrdersNumView iRefundGoodView,DataStringCallback dataStringCallback) {
        OkHttpUtils.get().url(ShopConstants.RETURN_GOOD_LIST).addParams("key", iRefundGoodView.getKey())
                .addParams("curpage", "1")
                .addParams("page", "1000")
                .build().execute(dataStringCallback);
    }
    public static void getRefundMoneyDetail(final IRefundMoneyDetailView iRefundMoneyDetailView) {
        OkHttpUtils.get().url(ShopConstants.REFUND_ORDER_DETAIL).addParams("key", iRefundMoneyDetailView.getKey())
                .addParams("refund_id", iRefundMoneyDetailView.getRefund_id())
                .build().execute(new DataStringCallback(iRefundMoneyDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundMoneyDetailView.onGetRefundMoneyDetailSuccess(s);
                }
            }
        });
    }

    public static void getRefundGoodDetail(final IRefundGoodDetailView iRefundGoodDetailView) {
        OkHttpUtils.get().url(ShopConstants.RETURN_GOOD_ORDER_DETAIL).addParams("key", iRefundGoodDetailView.getKey())
                .addParams("return_id", iRefundGoodDetailView.getReturnGood_id())
                .build().execute(new DataStringCallback(iRefundGoodDetailView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundGoodDetailView.onGetRefundGoodDetailSuccess(s);
                }
            }
        });
    }

    public static void postRefundPic(final IPostRefundPicView iPostRefundPicViewView, File file) {
        OkHttpUtils.post().url(ShopConstants.REFUND_UPLOAD_PIC).addParams("key", iPostRefundPicViewView.getKey())
                .addFile("refund_pic", file.getName(), file)
                .build().execute(new DataStringCallback(iPostRefundPicViewView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iPostRefundPicViewView.onPostRefundPicSuccess(s);
                }
            }
        });
    }

    public static void postRefundAll(final IPostRefundAllView iPostRefundAllView, List<String> picList) {
        PostFormBuilder pfbuilder = OkHttpUtils.post().url(ShopConstants.REFUND_ALL_POST).addParams("key", iPostRefundAllView.getKey())
                .addParams("order_id", iPostRefundAllView.getOrderId())
                .addParams("reason_id", iPostRefundAllView.getReasonId())
                .addParams("buyer_message ", iPostRefundAllView.getBuyerMessage());
        for (int i = 0; i < picList.size(); i++) {
            pfbuilder.addParams("refund_pic[" + i + "]", picList.get(i));
        }
        pfbuilder.build().execute(new DataStringCallback(iPostRefundAllView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iPostRefundAllView.onPostRefundSuccess(s);
                }
            }
        });
    }

    public static void postRefund(final IPostRefundView iPostRefundView, List<String> picList) {
        PostFormBuilder pfbuilder = OkHttpUtils.post().url(ShopConstants.REFUN_MONEY_OR_GOOD).addParams("key", iPostRefundView.getKey())
                .addParams("order_id", iPostRefundView.getOrderId())
                .addParams("reason_id", iPostRefundView.getReasonId())
                .addParams("buyer_message ", iPostRefundView.getBuyerMessage())
                .addParams("order_goods_id", iPostRefundView.getOrder_goods_id())
                .addParams("refund_amount", iPostRefundView.getRefund_amount())
                .addParams("refund_type", iPostRefundView.getRefund_type());

        for (int i = 0; i < picList.size(); i++) {
            pfbuilder.addParams("refund_pic[" + i + "]", picList.get(i));
        }
        if(iPostRefundView.getRefund_type().equals("0")) {
            pfbuilder .addParams("goods_num ", iPostRefundView.getGoods_num());
        }
        pfbuilder.build().execute(new DataStringCallback(iPostRefundView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iPostRefundView.onPostRefundSuccess(s);
                }
            }
        });
    }

    public static void getRefundForm(final IRefundFormView iRefundFormView) {
        OkHttpUtils.get().url(ShopConstants.REFUN_FORM).addParams("key", iRefundFormView.getKey())
                .addParams("order_id", iRefundFormView.getOrderid())
                .addParams("order_goods_id", iRefundFormView.getRecGoodsid())
                .build().execute(new DataStringCallback(iRefundFormView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iRefundFormView.onGetRefundFormSuccess(s);
                }
            }
        });
    }

    public static void getOrderRoute(final ISearchDeliverView iSearchDeliverView) {
        OkHttpUtils.post().url(ShopConstants.SEARCH_DELIVER).addParams("key", iSearchDeliverView.getKey())
                .addParams("order_id", iSearchDeliverView.getOrderid())
                .build().execute(new DataStringCallback(iSearchDeliverView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iSearchDeliverView.onSearchDeliverSuccess(s);
                }
            }
        });
    }

    public static void getInitEveluate(final IShopInitEveluateView iShopInitEveluateView) {

        OkHttpUtils.get().url(ShopConstants.INIT_EVELUATE).addParams("key", iShopInitEveluateView.getKey())
                .addParams("order_id", iShopInitEveluateView.getOrderid())
                .build().execute(new DataStringCallback(iShopInitEveluateView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopInitEveluateView.onGetInitEveluateSuccess(s);
                }
            }
        });
    }

    public static void getInitAddEveluate(final IShopInitAddEveluateView iShopInitAddEveluateView, String orderId) {
        OkHttpUtils.get().url(ShopConstants.INIT_ADD_EVELUATE).addParams("key", iShopInitAddEveluateView.getKey())
                .addParams("order_id", orderId)
                .build().execute(new DataStringCallback(iShopInitAddEveluateView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopInitAddEveluateView.onGetInitAddEveluateSuccess(s);
                }
            }
        });
    }

    public static void uploadEvaluatePic(final IUploadEveluatePicView iUploadEveluatePicView, final File file, final String fileName) {
        OkHttpUtils.post().url(ShopConstants.UPLOAD_EVALUATE_PIC).addParams("key", iUploadEveluatePicView.getKey())
                .addFile("file", fileName, file)
                .build().execute(new DataStringCallback(iUploadEveluatePicView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iUploadEveluatePicView.onPostEvaluatePicSuccess(s);
                }
            }
        });
    }
    public static void postEvelateAll(final IPostEvelateAllView iPostEvelateAllView, List<MyEvaluateShopOrderBean> dataList) {
        PostFormBuilder pfbuilder = OkHttpUtils.post().url(ShopConstants.POST_EVELUATE_ALL)
                .addParams("key", iPostEvelateAllView.getKey())
                .addParams("order_id", iPostEvelateAllView.getOrderId());
        for (MyEvaluateShopOrderBean bean : dataList) {
            String recid = bean.getRecId();
            int starts = bean.getStarts();
            String text = bean.getEvaluateText();
            boolean isNiming = bean.isNiming();
            pfbuilder.addParams("goods[" + recid + "]" + "[score]", starts + "");
            pfbuilder.addParams("goods[" + recid + "]" + "[comment]", text);
            pfbuilder.addParams("goods[" + recid + "]" + "[anony]", isNiming ? "1" : "0");
            ArrayList<String> imageList = bean.getToUploadimagePathList();
            for (int i = 0; i < imageList.size(); i++) {
                pfbuilder.addParams("goods[" + recid + "]" + "[evaluate_image][" + i + "]", imageList.get(i));
            }
        }
        pfbuilder.build().execute(new DataStringCallback(iPostEvelateAllView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iPostEvelateAllView.onPostEvelateAllSuccess(s);
                }
            }
        });
    }

    public static void postAddEvelateAll(final IPostAddEvelateAllView iPostAddEvelateAllView, ArrayList<MyAddEvaluateShopOrderBean> dataList) {
        PostFormBuilder pfbuilder = OkHttpUtils.post().url(ShopConstants.POST_ADD_EVELUATE_ALL)
                .addParams("key", iPostAddEvelateAllView.getKey())
                .addParams("order_id", iPostAddEvelateAllView.getOrderId());
        for (MyAddEvaluateShopOrderBean bean : dataList) {
            String geval_id = bean.getGeval_id();
            String text = bean.getComment();
            pfbuilder.addParams("goods[" + geval_id + "]" + "[comment]", text);
            ArrayList<String> imageList = bean.getToUploadimagePathList();
            for (int i = 0; i < imageList.size(); i++) {
                pfbuilder.addParams("goods[" + geval_id + "]" + "[evaluate_image][" + i + "]", imageList.get(i));
            }
        }
        pfbuilder.build().execute(new DataStringCallback(iPostAddEvelateAllView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iPostAddEvelateAllView.onPostAddEvelateAllSuccess(s);
                }
            }
        });
    }

    public static void getFootprintList(final IGetShopFootprintView iGetShopFootprintView) {
        OkHttpUtils.get().url(ShopConstants.SHOP_FOOTPRINT).addParams("key", iGetShopFootprintView.getKey())
                .addParams("curpage", iGetShopFootprintView.getCurpage())
                .addParams("page", 50+"")
                .build().execute(new DataStringCallback(iGetShopFootprintView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetShopFootprintView.onGetClearFootprintSuccess(s);
                }
            }
        });
    }
    public static void getFootprintList(final IShopOrdersNumView iGetShopFootprintView,DataStringCallback dataStringCallback) {
        OkHttpUtils.get().url(ShopConstants.SHOP_FOOTPRINT).addParams("key", iGetShopFootprintView.getKey())
                .addParams("curpage", "1")
                .addParams("page", 1000+"")
                .build().execute(dataStringCallback);
    }
    public static void clearFootprintList(final IClearShopFootprintView iClearShopFootprintView) {
        OkHttpUtils.post().url(ShopConstants.CLEAR_SHOP_FOOTPRINT).addParams("key", iClearShopFootprintView.getKey())
                .build().execute(new DataStringCallback(iClearShopFootprintView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iClearShopFootprintView.onClearFootprintSuccess(s);
                }
            }
        });
    }

    public static void getCollectList(final IShopCollectionView iShopCollectionView) {
        OkHttpUtils.get().url(ShopConstants.SHOP_COLLECTION).addParams("key", iShopCollectionView.getKey())
                .addParams("curpage", iShopCollectionView.getCurpage())
                .addParams("page", 10+"")
                .build().execute(new DataStringCallback(iShopCollectionView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopCollectionView.onGetCollectSuccess(s);
                }
            }
        });
    }
    public static void getCollectList(final IShopOrdersNumView iShopCollectionView,DataStringCallback dataStringCallback) {
        OkHttpUtils.get().url(ShopConstants.SHOP_COLLECTION).addParams("key", iShopCollectionView.getKey())
                .addParams("curpage", "1")
                .addParams("page", "1000")
                .build().execute(dataStringCallback);
    }
    public static void deleteCollect(final IShopDeleteCollectionView iShopDeleteCollectionView,String fav_id,final int Position) {
        OkHttpUtils.post().url(ShopConstants.DELETE_COLLECTION).addParams("key", iShopDeleteCollectionView.getKey())
                .addParams("fav_id", fav_id)
                .build().execute(new DataStringCallback(iShopDeleteCollectionView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopDeleteCollectionView.onDeleteCollectSuccess(s,Position);
                }
            }
        });
    }

    public static void addCollection(final IAddCollectionView iAddCollectionView, String goodId) {
        OkHttpUtils.post().url(ShopConstants.ADD_COLLECTION).addParams("key", iAddCollectionView.getKey())
                .addParams("goods_id", goodId)
                .build().execute(new DataStringCallback(iAddCollectionView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iAddCollectionView.onAddCollectSuccess(s);
                }
            }
        });
    }
    public static void getShareUrl(final IGetShareUrlView iGetShareUrlView,final String type) {
        OkHttpUtils.get().url(ShopConstants.GET_SHARE_URL)
                .addParams("goods_id", iGetShareUrlView.getGoodId())
                .addParams("type", type)
                .build().execute(new DataStringCallback(iGetShareUrlView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetShareUrlView.onGetShareUrlSuccess(s,type);
                }
            }
        });
    }

    public static void deleteCollect(final IShopDeleteCollectionView iShopDeleteCollectionView, String favid) {
        OkHttpUtils.post().url(ShopConstants.DELETE_COLLECTION).addParams("key", iShopDeleteCollectionView.getKey())
                .addParams("fav_id", favid)
                .build().execute(new DataStringCallback(iShopDeleteCollectionView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iShopDeleteCollectionView.onDeleteCollectSuccess(s,0);
                }
            }
        });
    }
    public static void getMsgCount(final IGetMsgCountView iGetMsgCountView) {
        OkHttpUtils.get().url(ShopConstants.MSH_COUNT).addParams("key", iGetMsgCountView.getKey())
                .build().execute(new DataStringCallback(iGetMsgCountView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetMsgCountView.onGetMsgCountSuccess(s);
                }
            }
        });
    }

    public static void getChatUserList(final IGetChatUserListView iGetChatUserListView) {
        OkHttpUtils.post().url(ShopConstants.CHAT_USER_LIST).addParams("key", iGetChatUserListView.getKey())
                .build().execute(new DataStringCallback(iGetChatUserListView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGetChatUserListView.onGetChatUserListSuccess(s);
                }
            }
        });
    }

    public static void deleteMsgUserItem(final IDeleteMsgUserItemView iDeleteMsgUserItemView, String tId) {
        OkHttpUtils.post().url(ShopConstants.DELETE_MSG_USER_ITEM).addParams("key", iDeleteMsgUserItemView.getKey())
                .addParams("t_id", tId)
                .build().execute(new DataStringCallback(iDeleteMsgUserItemView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iDeleteMsgUserItemView.onDeleteMsgUserItemSuccess(s);
                }
            }
        });
    }

    public static void guessLikeGoodList(final IGuessLikeGoodListView iGuessLikeGoodListView) {
        OkHttpUtils.get().url(ShopConstants.GUESS_LIKE).addParams("key", iGuessLikeGoodListView.getKey())
                .build().execute(new DataStringCallback(iGuessLikeGoodListView) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    iGuessLikeGoodListView.onGetGuessLikeGoodSuccess(s);
                }
            }
        });
    }

}
