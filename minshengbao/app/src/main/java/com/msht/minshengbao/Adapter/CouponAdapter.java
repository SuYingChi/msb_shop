package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/1.
 */

public class CouponAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, String>> haveuseList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
    public OnItemSelectListener listener;
    public void SetOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        void ItemSelectClick(View view,int thisposition);
    }
    public CouponAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.haveuseList=List;
    }
    @Override
    public int getCount() {
        return haveuseList.size();
    }
    @Override
    public Object getItem(int position) {
        return haveuseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final int thisposition=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
            holder.Layoutback=(RelativeLayout)convertView.findViewById(R.id.id_layout_back);
            holder.cn_name=(TextView)convertView.findViewById(R.id.id_title_name);
            holder.cn_scope=(TextView) convertView.findViewById(R.id.id_scope);
            holder.cn_amount=(TextView) convertView.findViewById(R.id.id_amount);
            holder.cn_use_limit=(TextView) convertView.findViewById(R.id.id_use_limit);
            holder.cn_time=(TextView)convertView.findViewById(R.id.id_time);
            holder.btn_use=(Button)convertView.findViewById(R.id.id_btn_use);
           // holder.cn_start_date=(TextView) convertView.findViewById(R.id.id_start_date);
            holder.cn_end_date=(TextView) convertView.findViewById(R.id.id_end_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String type=haveuseList.get(position).get("type");
        String remainder_days=haveuseList.get(position).get("remainder_days");
        if (type.equals("1")){
            holder.btn_use.setBackgroundResource(R.drawable.selector_yellow_button);
            holder.cn_amount.setTextColor(Color.parseColor("#F5BC33"));
            holder.cn_name.setTextColor(Color.parseColor("#F5BC33"));
            holder.Layoutback.setBackgroundResource(R.drawable.dicount_coupon_2xh);
        }else if (type.equals("2")){
            holder.btn_use.setBackgroundResource(R.drawable.shape_gray_corner_ingray);
            holder.cn_amount.setTextColor(Color.parseColor("#FF383838"));
            holder.cn_name.setTextColor(Color.parseColor("#FF383838"));
            holder.Layoutback.setBackgroundResource(R.mipmap.coupon_haveused_2xh);
        }else if(type.equals("3")){
            holder.btn_use.setBackgroundResource(R.drawable.shape_gray_corner_ingray);
            holder.cn_amount.setTextColor(Color.parseColor("#FF383838"));
            holder.cn_name.setTextColor(Color.parseColor("#FF383838"));
            holder.Layoutback.setBackgroundResource(R.mipmap.coupon_exceed_2xh);
        }
        if (!remainder_days.equals("")){
            holder.cn_time.setVisibility(View.VISIBLE);
            holder.cn_time.setText("剩"+remainder_days+"天");
        }else {
            holder.cn_time.setVisibility(View.GONE);
        }
        String limit_use="买满"+haveuseList.get(position).get("use_limit")+"元可用";
        holder.cn_name.setText(haveuseList.get(position).get("name"));
        holder.cn_scope.setText(haveuseList.get(position).get("scope"));
        holder.cn_amount.setText("¥"+haveuseList.get(position).get("amount"));
        holder.cn_use_limit.setText(limit_use);
      //  holder.cn_start_date.setText(haveuseList.get(position).get("start_date"));
        holder.cn_end_date.setText(haveuseList.get(position).get("end_date"));
        holder.btn_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("1")){
                    if (listener!=null){
                        listener.ItemSelectClick(v,thisposition);
                    }
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        RelativeLayout Layoutback;
        TextView  cn_name;
        TextView  cn_scope;
        TextView  cn_amount;
        TextView  cn_use_limit;
       // public TextView  cn_start_date;
        TextView  cn_time;
        TextView  cn_end_date;
        Button    btn_use;
    }
}