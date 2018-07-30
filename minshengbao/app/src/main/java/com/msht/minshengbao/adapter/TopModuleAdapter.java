package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/6  
 */
public class TopModuleAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public TopModuleAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext=context;
        this.functionList=List;
    }
    @Override
    public int getCount() {
        if (functionList!=null){
            return functionList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (functionList!=null){
            return functionList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder =new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_topmodule_view, null);
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tv_name.setText(name);
        if (code.equals("shop")){
            holder.img_function.setImageResource(R.drawable.top_shopmall_xh);
        }else if (code.equals("gas_pay")){
            holder.img_function.setImageResource(R.drawable.top_gaspay_xh);
        }else if (code.equals("gas_meter")){
            holder.img_function.setImageResource(R.drawable.top_gas_writetable_xh);
        }else if (code.equals("gas_iccard")){
            holder.img_function.setImageResource(R.drawable.top_iccard_xh);
        }else if (code.equals("insurance")){
            holder.img_function.setImageResource(R.drawable.home_insurance_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView tv_name;
    }
}