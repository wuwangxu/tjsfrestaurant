package com.wangxu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

import com.example.myapp.MainActivity;
import com.example.myapp.MyExpandableListViewAdapter;
import com.example.myapp.SubmitActivity;
import com.wangxu.flags.Menus;

public class ConfirmSubmitOrderDialog {
	private AlertDialog dialog;
	private MyExpandableListViewAdapter adapter;
	public ConfirmSubmitOrderDialog(final Context context, MyExpandableListViewAdapter adapter) {
		this.adapter=adapter;
		dialog=new AlertDialog.Builder(context).setTitle("确认信息")
				.setMessage("是否确认提交订单？")
				.setPositiveButton("确认", new OnClickListener() {
					public void onClick(DialogInterface dia, int which) {
						//确认开始提交，关闭提示dialog
						dialog.dismiss();
						final ProgressDialog pro=new ProgressDialog(context);
						pro.setTitle("正在提交，请稍等");
						pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						//不可强制取消传送
						pro.setCancelable(false);
						pro.show();
						if (Menus.order.size()!=0) {

							Intent intent = new Intent(context, SubmitActivity.class);
							context.startActivity(intent);
							pro.dismiss();
//							final JSONArray array=new JSONArray();
//							for (FoodMenu foodMenu : Menus.order) {
//								JSONObject object=new JSONObject();
//								try {
//									object.put("foodName", foodMenu.getFoodName());
//									object.put("foodPrice", foodMenu.getFoodPrice());
//									object.put("foodUnitPrice", foodMenu.getFoodUnitPrice());
//									object.put("foodCount", foodMenu.getFoodCount());
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//								array.put(object);
//							}
//							new AsyncTask<Void, Void, String>(){
//								protected String doInBackground(Void... params) {
//									HttpClient client=new DefaultHttpClient();
//									client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
//									client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
//									HttpPost post=new HttpPost(Flags.SUBMIT_ORDER);
//									ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
//									BasicNameValuePair pair=new BasicNameValuePair("CustomerNumber", Menus.CustomerNumber+"");
//									BasicNameValuePair pair2=new BasicNameValuePair("submitOrder", array.toString());
//									list.add(pair);
//									list.add(pair2);
//									try {
//										post.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8));
//									} catch (UnsupportedEncodingException e2) {
//										e2.printStackTrace();
//									}
//									try {
//										HttpResponse response=client.execute(post);
//										if (response.getStatusLine().getStatusCode()!=200) {
//											return null;
//										}
//										//从实体中获取数据
//										String str=EntityUtils.toString(response.getEntity(), "UTF-8");
//										return str;
//									} catch (ClientProtocolException e) {
//										e.printStackTrace();
//									} catch (IOException e) {
//										e.printStackTrace();
//									}
//									return null;
//								}
//								protected void onPostExecute(String result) {
//									if (result!=null&&result.equals("OK")) {
//										//订单提交成功后，清除原订单内容
//										for (FoodMenu foodMenu : Menus.order) {
//											foodMenu.setFoodCount(0);
//										}
//										Menus.order.clear();
//										ConfirmSubmitOrderDialog.this.adapter.notifyDataSetChanged();
//										pro.dismiss();
//										Toast.makeText(context, "订单提交成功，服务员稍后为您服务", 0).show();
//									}else {
//										pro.dismiss();
//										Toast.makeText(context, "网络连接错误", 0).show();
//									}
//								}
//							}.execute();
						}else {
							pro.dismiss();
							Toast.makeText(context, "您尚未点菜，请选择菜品后再提交", 0).show();
						}
							
						}
				}).create();
		dialog.show();
	}
}
