package com.example.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangxu.dialog.UpdataDataDialog;
import com.wangxu.dialog.UpdataPromptDialog;
import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;
import com.wangxu.util.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SelectTableNumberActivity extends Activity {
    private RequestQueue mQueue;
    private Button btnSubmit;
    private EditText editNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        findViews();
        //创建网络传输队列
        mQueue = Volley.newRequestQueue(this);
        boolean isFirst = getApplicationContext().getSharedPreferences("isFirst", MODE_PRIVATE).getBoolean("IS_FIRST", true);
        //如果是第一次,则展示viewpager WelcomePage页面
        if (isFirst) {
            Intent intent = new Intent(this, WelcomePage.class);
            startActivity(intent);
            final UpdataDataDialog dialog = new UpdataDataDialog(this);
            StringRequest request = new MyStringRequest(Flags.GET_DATABASE_VERSION, new Listener<String>() {
                public void onResponse(String version) {
                    Log.i("Msg", version);
                    JSONObject object = new JSONObject();
                    try {
                        object = new JSONObject(version);
                        int versionda = (int) object.getDouble("data");
                        version = String.valueOf(versionda);
                        Log.i("Msg", version);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Menus.DatabaseVersion = Integer.parseInt(version);
                    getApplicationContext().getSharedPreferences("db_version", MODE_PRIVATE).
                            edit().putInt("DB_VERSION", Integer.parseInt(version)).commit();
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError arg0) {
                    //如果网络连接有异常，dialog消失，弹出提示
                    dialog.dismiss();
                    Toast.makeText(SelectTableNumberActivity.this, "网络连接异常", 0).show();
                }
            });
            mQueue.add(request);
            getApplicationContext().getSharedPreferences("isFirst", MODE_PRIVATE).edit().putBoolean("IS_FIRST", false).commit();
        }
        //接口
        btnSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editNumber.getText().length() != 0) {
                    final ProgressDialog dialog = new ProgressDialog(SelectTableNumberActivity.this);
                    dialog.setTitle("正在连接服务器，请稍等");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setCancelable(false);
                    dialog.show();

                    StringRequest request = new MyStringRequest(Flags.LOGIN+"?customerNumber="+editNumber.getText().toString(), new Listener<String>() {
                        public void onResponse(String version) {
                            dialog.dismiss();
                            String message="";
                            JSONObject object = new JSONObject();
                            try {
                                object = new JSONObject(version);
                                int versionda = (int) object.getDouble("data");
                                message = (String)object.getString("message");
                                version = String.valueOf(versionda);
                                Log.i("Msg", version);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (message.equals("登录成功")) {
                                int servletVersion = getApplicationContext().getSharedPreferences("db_version", MODE_PRIVATE).getInt("DB_VERSION", 0);
                                //如果经过核对，确认服务器数据库版本与客户端一直，则直接跳转
                                if (Integer.parseInt(version) == servletVersion) {
                                    Intent intent = new Intent(SelectTableNumberActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    //确定无误，进行跳转时，将座位号预存
                                    Menus.CustomerNumber = editNumber.getText().toString();
                                    finish();
                                }
                                //如果不一致，则需要进行更新
                                else {
                                    Menus.DatabaseVersion = Integer.parseInt(version);
                                    new UpdataPromptDialog(SelectTableNumberActivity.this);
                                }
                            }else{
                                Toast.makeText(SelectTableNumberActivity.this, "学号不存在！", 0).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError arg0) {
                            dialog.dismiss();
                            Toast.makeText(SelectTableNumberActivity.this, "网络连接异常", 0).show();
                        }
                    });
                    mQueue.add(request);
                } else {
                    Toast.makeText(SelectTableNumberActivity.this, "桌位号不能为空", 0).show();
                }
            }
        });


//	btnSubmit.setOnClickListener(new OnClickListener() {
//		public void onClick(View v) {
//			if (editNumber.getText().length()!=0) {
//				final ProgressDialog dialog=new ProgressDialog(SelectTableNumberActivity.this);
//				dialog.setTitle("正在连接服务器，请稍等");
//				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//				dialog.setCancelable(false);
//				dialog.show();
//				dialog.dismiss();
//				int servletVersion=getApplicationContext().getSharedPreferences("db_version", MODE_PRIVATE).getInt("DB_VERSION", 0);
//				Intent intent=new Intent(SelectTableNumberActivity.this, MainActivity.class);
//				startActivity(intent);
//							//确定无误，进行跳转时，将座位号预存
//				Menus.SeatNumber=Integer.parseInt(editNumber.getText().toString());
//				finish();
//			}else {
//				Toast.makeText(SelectTableNumberActivity.this, "桌位号不能为空", Toast.LENGTH_SHORT).show();
//			}
//
//		}
//	});

    }

    private void findViews() {
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        editNumber = (EditText) findViewById(R.id.edit_number);
    }
}
