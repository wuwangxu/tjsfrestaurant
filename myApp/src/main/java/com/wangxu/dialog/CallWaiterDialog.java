package com.wangxu.dialog;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.R;
import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;

public class CallWaiterDialog {
	private AlertDialog dialog;
	private EditText editText;
	public CallWaiterDialog(final Context context) {
		dialog=new AlertDialog.Builder(context).setPositiveButton("发送", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (editText.getText().length()==0) {
					Toast.makeText(context, "请输入需要后再提交", 0).show();
					return;
				}
				final ProgressDialog proDialog=new ProgressDialog(context);
				proDialog.setTitle("正在发送，请稍等");
				proDialog.setCancelable(false);
				proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				proDialog.show();
				new AsyncTask<Void, Void, String>(){
					protected String doInBackground(Void... params) {
						HttpClient client=new DefaultHttpClient();
						try {
							client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
							client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
							HttpPost post=new HttpPost(Flags.CALL_WAITER);
							ArrayList<NameValuePair> arrayLis=new ArrayList<NameValuePair>();
							BasicNameValuePair seatNumber=new BasicNameValuePair("CustomerNumber", Menus.CustomerNumber+"");
							BasicNameValuePair pair=new BasicNameValuePair("value", editText.getText().toString());
							arrayLis.add(pair);
							arrayLis.add(seatNumber);
							//设置实体
							post.setEntity(new UrlEncodedFormEntity(arrayLis,"UTF-8"));
							HttpResponse response=client.execute(post);
							if (response.getStatusLine().getStatusCode()!=200) {
								return null;
							}
							String str=EntityUtils.toString(response.getEntity(), "UTF-8");
							return str;
						} catch (ConnectTimeoutException e) {
						}catch (SocketTimeoutException e) {
						} catch (ClientProtocolException e) {
						} catch (IOException e) {
						}
						return null;
					}
					protected void onPostExecute(String result) {
						proDialog.dismiss();
						if (result!=null&&result.equals("OK")) {
							Toast.makeText(context, "提交成功，请稍等，服务员马上就到", 0).show();
						}else {
							Toast.makeText(context, "网络连接异常", 0).show();
						}
					}
				}.execute();
			}	
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create();
		dialog.setTitle("请输入您需要的服务");
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
		editText=(EditText) view.findViewById(R.id.edit_input);
		dialog.setView(view);
		dialog.show();
	}
}
