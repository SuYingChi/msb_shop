package com.msht.minshengbao.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DialogUtil {
	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}

	/**��ȡ��Ļ�ֱ��ʿ�*/
	public static int getScreenWidth(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		//((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
		Display display = wm.getDefaultDisplay();  
		display.getMetrics(dm);

		return dm.widthPixels;
	}
}
