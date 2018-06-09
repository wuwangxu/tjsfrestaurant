package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;
import com.wangxu.util.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderQueryActivity extends Activity {

    private TextView ttv,ntv,ctv,stv,mtv;
    private ListView orderlv;
    private JSONObject object = new JSONObject();
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderquery);

        findViewsById();
        getOrderData();

    }

    private void findViewsById() {
        ctv = (TextView) findViewById(R.id.qctextView);
        ttv = (TextView) findViewById(R.id.qttextView);
        ntv = (TextView) findViewById(R.id.qntextView);
        stv = (TextView) findViewById(R.id.qstextView);
        mtv = (TextView) findViewById(R.id.qmtextView);
        orderlv= (ListView) findViewById(R.id.ordersList);
    }
    private void getOrderData(){

        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest request = new MyStringRequest(Flags.GET_ORDER_LASTED+Menus.CustomerNumber, new Response.Listener<String>() {
            public void onResponse(String res) {
                Log.i("Msg", res);

                try {
                    object = new JSONObject(res).getJSONObject("data");
                    ctv.setText("订单号:"+object.getString("orderCode"));
                    ntv.setText("取餐号:"+object.getString("orderPickNumber"));
                    ttv.setText("订单时间:"+object.getString("orderTime"));
                    stv.setText("学号:" + Menus.CustomerNumber);
                    mtv.setText("订单总价:" + object.getInt("totalManey"));
                    list = new ArrayList<HashMap<String, Object>>();
                    HashMap<String, Object> map = null;
                    for (int i = 0; i < object.getJSONArray("menuOrderVos").length(); i++) {
                        JSONObject item = object.getJSONArray("menuOrderVos").getJSONObject(i);
                        map = new HashMap<String, Object>();
                        map.put("menuDishName", item.getString("menuDishName"));
                        map.put("orderDishNumber", item.getInt("orderDishNumber")+"");
                        map.put("windowName", item.getString("windowName"));
//                        map.put("img", R.drawable.special_spring_head2);
                        list.add(map);
                    }
                    adapter=new SimpleAdapter(OrderQueryActivity.this,list,R.layout.orderlist_item,
                            new String[]{"menuDishName","orderDishNumber","windowName"},
                            new int[]{R.id.order_name,R.id.order_number,R.id.order_wname});
                    orderlv.setAdapter(adapter);
//                    int versionda = (int) object.getDouble("data");
//                    version = String.valueOf(versionda);
//                    Log.i("Msg", version);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                //如果网络连接有异常，dialog消失，弹出提示
//                dialog.dismiss();
                Toast.makeText(OrderQueryActivity.this, "网络连接异常", 0).show();
            }
        });
        mQueue.add(request);
    }
}
