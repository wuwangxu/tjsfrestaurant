package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class WelcomePage extends Activity {
	private ArrayList<View> arrayList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_page);
		ViewPager viewPager=(ViewPager) findViewById(R.id.view_pager);
		View view1=LayoutInflater.from(this).inflate(R.layout.welcome_page_1, null);
		View view2=LayoutInflater.from(this).inflate(R.layout.welcome_page_2, null);
		View view3=LayoutInflater.from(this).inflate(R.layout.welcome_page_3, null);
		arrayList=new ArrayList<View>();
		arrayList.add(view1);
		arrayList.add(view2);
		arrayList.add(view3);
		viewPager.setAdapter(new MyViewPagerAdapter());
		
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}


	class MyViewPagerAdapter extends PagerAdapter{
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(arrayList.get(position));
		}
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(arrayList.get(position), 0);
			if (position==2) {
				ImageView image=(ImageView) findViewById(R.id.wlecome_page_image_goto);
				image.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
			}
			return arrayList.get(position);
		}
		public int getCount() {
			return arrayList.size();
		}
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
	}
	
}
