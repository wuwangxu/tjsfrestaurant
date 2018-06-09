package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wangxu.flags.Menus;
import com.wangxu.flags.Order;

public class OrderActivity extends Activity {

    private TextView ttv,ntv,ctv,stv,mtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        findViewsById();
        ctv.setText(Order.getOrderCode());
        ntv.setText(Order.getOrderPickNumber());
        ttv.setText(Order.getOrderTime());
        stv.setText("学号:" + Menus.CustomerNumber);
        mtv.setText("订单总价:" + Menus.total);
    }

    private void findViewsById() {
        ctv = (TextView) findViewById(R.id.ctextView);
        ttv = (TextView) findViewById(R.id.ttextView);
        ntv = (TextView) findViewById(R.id.ntextView);
        stv = (TextView) findViewById(R.id.stextView);
        mtv = (TextView) findViewById(R.id.mtextView);
    }
}
