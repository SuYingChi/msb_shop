package com.msht.minshengbao.adapter;

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
 * Created by hong on 2017/5/8.
 */

public class GasServiceAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public GasServiceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            convertView = mInflater.inflate(R.layout.item_second_service, null);
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tv_name.setText(name);
        if (code.equals("gas_repair")){
            holder.img_function.setImageResource(R.drawable.gas_repair_xh);
        }else if (code.equals("gas_meter")){
            holder.img_function.setImageResource(R.drawable.gas_meter_xh);
        }else if (code.equals("gas_pay")){
            holder.img_function.setImageResource(R.drawable.gas_pay_xh);
        }else if (code.equals("gas_install")){
            holder.img_function.setImageResource(R.drawable.gas_install_xh);
        }else if (code.equals("gas_rescue")){
            holder.img_function.setImageResource(R.drawable.gas_rescue_xh);
        }else if (code.equals("gas_introduce")){
            holder.img_function.setImageResource(R.drawable.gas_introduce_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView  tv_name;
    }
}
