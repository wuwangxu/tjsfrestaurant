package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;
import com.wangxu.flags.Order;
import com.wangxu.util.FoodMenu;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitActivity extends Activity {
    private static final String APPLICATION_JSON = "application/json";// POST 数据提交的方式

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private TextView cTextView,tTextView;// 学号 总价
    private ListView fListView;// 点餐的食品列表
    private Button sButton;// 提交按钮
    private SimpleAdapter adapter;// 点餐的食品列表数据适配器
    final JSONArray array = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        // 找到所有控件
        findViewsById();
        cTextView.setText("学号:" + Menus.CustomerNumber);
        tTextView.setText("订单总价:" + Menus.total);

        getAdapterData();
        fListView.setAdapter(adapter);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, String>() {// 异步任务
                    protected String doInBackground(Void... params) {
                        // 设置请求头
                        HttpClient client = new DefaultHttpClient();
                        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
                        HttpPost post = new HttpPost(Flags.SUBMIT_ORDER);
                        post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
//                        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                        // 包装数据，把数据设置为json格式
                        HashMap<String,Object> o = new HashMap<String,Object>();

                        o.put("customerNumber",(String)Menus.CustomerNumber);
                        o.put("menuOrderVos",array);
                        JSONObject object = new JSONObject(o);
                        // 另外一种数据格式
//                        BasicNameValuePair pair =new BasicNameValuePair("orderVo",object.toString());
//                        list.add(pair);
//                        Log.i("Msg",list.toString());
//                        BasicNameValuePair pair = new BasicNameValuePair("customerNumber", Menus.CustomerNumber + "");
//                        BasicNameValuePair pair2 = new BasicNameValuePair("menuOrderVos", array.toString());
//                        list.add(pair);
//                        list.add(pair2);
                        try {
//                            String encoderJson = URLEncoder.encode(object.toString(), HTTP.UTF_8);
//                            StringEntity se = new StringEntity(encoderJson);
//                            se.setContentType(CONTENT_TYPE_TEXT_JSON);
//                            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
//                            post.setEntity(se);
                            post.setEntity(new StringEntity(object.toString(),HTTP.UTF_8));
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        }
                        try {
                            HttpResponse response = client.execute(post);// 请求
                            Log.i("Msg",response.getStatusLine().toString());
                            if (response.getStatusLine().getStatusCode() != 200) {
                                return null;
                            }
//从接口返回的消息中获取数据
                            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
                            return str;
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    protected void onPostExecute(String result){
                        if (result != null && result.contains("ok")) {
//订单提交成功后，清除原订单内容
//                            for (FoodMenu foodMenu : Menus.order) {
//                                foodMenu.setFoodCount(0);
//                            }
//                            Menus.order.clear();
//                            SubmitActivity.this.adapter.notifyDataSetChanged();
//                            pro.dismiss();

                            JSONObject resultObject=new JSONObject();
                            try {
                                resultObject=new JSONObject(result).getJSONObject("data");
//                                Log.i("Msg",resultObject.getJSONObject("data").toString());
                                Order.orderCode=resultObject.getString("orderCode");
                                Order.orderTime=resultObject.getString("orderTime");
                                Order.orderPickNumber=resultObject.getString("orderPickNumber");
                                Log.i("Msg",Order.showOrder());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(SubmitActivity.this, "订单提交成功，请您按时取餐", 0).show();
                            Intent intent = new Intent(SubmitActivity.this, OrderActivity.class);
                            startActivity(intent);

                            finish();

                        } else {
//                            pro.dismiss();
                            Toast.makeText(SubmitActivity.this, "网络连接错误", 0).show();
                        }
                    }
                }.execute();
            }
        });

    }

    private void findViewsById() {
        cTextView = (TextView) findViewById(R.id.customerNumberTextView);
        tTextView = (TextView) findViewById(R.id.totalTextView);
        fListView = (ListView) findViewById(R.id.foodsListView);
        sButton= (Button) findViewById(R.id.submit_button);

    }

    private void getAdapterData() {
        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
// 将Menus.order里的数据转存到array和listitem里
        for (FoodMenu foodMenu : Menus.order) {
            JSONObject object = new JSONObject();
            Map<String, Object> showitem = new HashMap<String, Object>();
            try {
                object.put("menuDishName", foodMenu.getFoodName());
                object.put("menuDishPrice", foodMenu.getFoodPrice());
//                object.put("foodUnitPrice", foodMenu.getFoodUnitPrice());
                object.put("orderDishNumber", foodMenu.getFoodCount());
                object.put("windowId", foodMenu.getWindowId());
                object.put("menuId", foodMenu.getFoodId());

                showitem.put("name", foodMenu.getFoodName());
                showitem.put("number", foodMenu.getFoodCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);// 给接口用的
            listitem.add(showitem);// 给listview用的

        }

        adapter=new SimpleAdapter(this,listitem,R.layout.listview_item,new String[]{"name","number"},new int[]{R.id.name, R.id.number});
    }

}
