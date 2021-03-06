package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class EvaluationBean {

    /**
     * code : 200
     * hasmore : false
     * page_total : 1
     * datas : {"goods_eval_list":[{"geval_scores":"5","geval_content":"不错哦","geval_addtime":"1533996985","geval_frommemberid":"223889","geval_frommembername":"13***","geval_explain":null,"geval_content_again":"","geval_addtime_again":"0","geval_explain_again":"","member_avatar":"http://shop.msbapp.cn:8090/data/upload/shop/common/default_user_portrait.gif","geval_addtime_date":"2018-08-11","geval_image_240":[],"geval_image_1024":[],"geval_addtime_again_date":"1970-01-01","geval_image_again_240":[],"geval_image_again_1024":[]}]}
     */

    private int code;
    private boolean hasmore;
    private int page_total;
    private DatasBean datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isHasmore() {
        return hasmore;
    }

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private List<GoodsEvalListBean> goods_eval_list;

        public List<GoodsEvalListBean> getGoods_eval_list() {
            return goods_eval_list;
        }

        public void setGoods_eval_list(List<GoodsEvalListBean> goods_eval_list) {
            this.goods_eval_list = goods_eval_list;
        }

        public static class GoodsEvalListBean {
            /**
             * geval_scores : 5
             * geval_content : 不错哦
             * geval_addtime : 1533996985
             * geval_frommemberid : 223889
             * geval_frommembername : 13***
             * geval_explain : null
             * geval_content_again :
             * geval_addtime_again : 0
             * geval_explain_again :
             * member_avatar : http://shop.msbapp.cn:8090/data/upload/shop/common/default_user_portrait.gif
             * geval_addtime_date : 2018-08-11
             * geval_image_240 : []
             * geval_image_1024 : []
             * geval_addtime_again_date : 1970-01-01
             * geval_image_again_240 : []
             * geval_image_again_1024 : []
             */

            private String geval_scores;
            private String geval_content;
            private String geval_addtime;
            private String geval_frommemberid;
            private String geval_frommembername;
            private Object geval_explain;
            private String geval_content_again;
            private String geval_addtime_again;
            private String geval_explain_again;
            private String member_avatar;
            private String geval_addtime_date;
            private String geval_addtime_again_date;
            private List<String> geval_image_240;
            private List<String> geval_image_1024;
            private List<String> geval_image_again_240;
            private List<String> geval_image_again_1024;

            public String getGeval_scores() {
                return geval_scores;
            }

            public void setGeval_scores(String geval_scores) {
                this.geval_scores = geval_scores;
            }

            public String getGeval_content() {
                return geval_content;
            }

            public void setGeval_content(String geval_content) {
                this.geval_content = geval_content;
            }

            public String getGeval_addtime() {
                return geval_addtime;
            }

            public void setGeval_addtime(String geval_addtime) {
                this.geval_addtime = geval_addtime;
            }

            public String getGeval_frommemberid() {
                return geval_frommemberid;
            }

            public void setGeval_frommemberid(String geval_frommemberid) {
                this.geval_frommemberid = geval_frommemberid;
            }

            public String getGeval_frommembername() {
                return geval_frommembername;
            }

            public void setGeval_frommembername(String geval_frommembername) {
                this.geval_frommembername = geval_frommembername;
            }

            public Object getGeval_explain() {
                return geval_explain;
            }

            public void setGeval_explain(Object geval_explain) {
                this.geval_explain = geval_explain;
            }

            public String getGeval_content_again() {
                return geval_content_again;
            }

            public void setGeval_content_again(String geval_content_again) {
                this.geval_content_again = geval_content_again;
            }

            public String getGeval_addtime_again() {
                return geval_addtime_again;
            }

            public void setGeval_addtime_again(String geval_addtime_again) {
                this.geval_addtime_again = geval_addtime_again;
            }

            public String getGeval_explain_again() {
                return geval_explain_again;
            }

            public void setGeval_explain_again(String geval_explain_again) {
                this.geval_explain_again = geval_explain_again;
            }

            public String getMember_avatar() {
                return member_avatar;
            }

            public void setMember_avatar(String member_avatar) {
                this.member_avatar = member_avatar;
            }

            public String getGeval_addtime_date() {
                return geval_addtime_date;
            }

            public void setGeval_addtime_date(String geval_addtime_date) {
                this.geval_addtime_date = geval_addtime_date;
            }

            public String getGeval_addtime_again_date() {
                return geval_addtime_again_date;
            }

            public void setGeval_addtime_again_date(String geval_addtime_again_date) {
                this.geval_addtime_again_date = geval_addtime_again_date;
            }

            public List<String> getGeval_image_240() {
                return geval_image_240;
            }

            public void setGeval_image_240(List<String> geval_image_240) {
                this.geval_image_240 = geval_image_240;
            }

            public List<String> getGeval_image_1024() {
                return geval_image_1024;
            }

            public void setGeval_image_1024(List<String> geval_image_1024) {
                this.geval_image_1024 = geval_image_1024;
            }

            public List<String> getGeval_image_again_240() {
                return geval_image_again_240;
            }

            public void setGeval_image_again_240(List<String> geval_image_again_240) {
                this.geval_image_again_240 = geval_image_again_240;
            }

            public List<String> getGeval_image_again_1024() {
                return geval_image_again_1024;
            }

            public void setGeval_image_again_1024(List<String> geval_image_again_1024) {
                this.geval_image_again_1024 = geval_image_again_1024;
            }
        }
    }
}
