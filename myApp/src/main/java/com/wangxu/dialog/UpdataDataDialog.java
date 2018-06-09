package com.wangxu.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;
import com.wangxu.util.AnalysisData;
import com.wangxu.util.MyStringRequest;
import com.wangxu.util.SqliteDatabaseManager;
//专门用来更新数据的dialog,每次更新都会重新录入数据库
public class UpdataDataDialog{
	private ProgressDialog dialog=null;
	public UpdataDataDialog(final Context context) {
		dialog=new ProgressDialog(context);
		dialog.setTitle("更新中");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在更新，请稍等...");
		//不可强制取消更新
		dialog.setCancelable(false);
		dialog.show();
		// 请求队列
		RequestQueue mQueue = Volley.newRequestQueue(context);
		StringRequest request = new MyStringRequest(Flags.DOWNLOAD_DATA_URL,
				new Listener<String>() {
					// 如果请求成功，返回数据,这儿也是在ui主线程中
					public void onResponse(String values) {
						// 防止意外，先清空所有数据
						Menus.clearMenu();
						// 让缓存读取数据
						AnalysisData a = new AnalysisData(values);
						Log.e("在updataDialog", Menus.menuTitles.toString());
						//下载读取完成，即关闭dialog
						//每一次更改都完成数据数据库重新录入
						SqliteDatabaseManager.getInstance(context.getApplicationContext()).updateDatabaseData();
						//每次更新之后完成偏好设置的录入
						context.getApplicationContext().getSharedPreferences("db_version",Context.MODE_PRIVATE).
						edit().putInt("DB_VERSION", Menus.DatabaseVersion).commit();
						dialog.dismiss();
						Toast.makeText(context, "数据更新成功", 0).show();
					}
				}, new Response.ErrorListener() {
					// 如果请求失败
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(context, "网络连接异常", 0).show();
					}
				});
		mQueue.add(request);
	}
	public void dismiss(){
		dialog.dismiss();
	}
	
}
