package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxu.dialog.ConfirmSubmitOrderDialog;
import com.wangxu.flags.Menus;
import com.wangxu.util.SqliteDatabaseManager;


public class MainActivity extends Activity {
	private static final int SHOW_SUB_ACTIVITY_ONE = 1;
	private static final int SHOW_SUB_ACTIVITY_TWO = 2;
	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	private ExpandableListView mListView;
	private ImageView mImageCall;
	private ImageView mImageSubmit;
	private TextView mTxtTotal;
	public MyExpandableListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到所有控件
        findViewsById();
//        //每次进入主页面，从本地数据库读取菜单
        Menus.clearMenu();
        SqliteDatabaseManager.getInstance(getApplicationContext()).getMenuData();
        //设置点击事件监听
        setClickListener();
        //为可拓展列表绑定数据
        adapter=new MyExpandableListViewAdapter(this,mTxtTotal);
        mListView.setAdapter(adapter);
    }
    private void findViewsById(){
    	mListView=(ExpandableListView) findViewById(R.id.listview_menu);
    	mImageCall=(ImageView) findViewById(R.id.image_download);
    	mImageSubmit=(ImageView) findViewById(R.id.image_submit);
    	mTxtTotal=(TextView) findViewById(R.id.txt_total);
    }
    private void setClickListener(){
    	mImageCall.setOnClickListener(new MyOnClickListener());
    	mImageSubmit.setOnClickListener(new MyOnClickListener());
    }
    class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_submit:
				new ConfirmSubmitOrderDialog(MainActivity.this,adapter);
				break;
			case R.id.image_download:
//				new CallWaiterDialog(MainActivity.this);
//				setOrderData();
				Intent intent = new Intent(MainActivity.this, OrderQueryActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
    }
	@Override
	protected void onDestroy() {
		//在页面退出时将所有数据全部清空
		Menus.DatabaseVersion=0;
		Menus.menus.clear();
		Menus.menuTitles.clear();
		Menus.order.clear();
		Menus.CustomerNumber="";
		super.onDestroy();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.i("Msg","hereaaaa");
//		switch(requestCode)
//		{
//			case (SHOW_SUB_ACTIVITY_ONE) :
//			{
//				if (resultCode == Activity.RESULT_OK)
//				{
//					Log.i("Msg","activity1");
//				}
//				break;
//			}
//			case (SHOW_SUB_ACTIVITY_TWO) :
//			{
//				if (resultCode == Activity.RESULT_OK)
//				{
//// TODO: Handle OK click.
//					Log.i("Msg","activity2");
//
//				}
//				break;
//			}
//		}
//	}

}
