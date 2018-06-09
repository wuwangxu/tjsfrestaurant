package com.wangxu.myself_view;
import com.example.myapp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class AddSubView extends LinearLayout{
	private int count;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private LinearLayout mLinearLayout;
	private ImageView mAdd,mSub;
	private TextView mText;
	private View mView;
	private OnCountChangedListener mCountChangedListener;
	//获得当前的数量
	public int getCount(){
		return count;
	}
	public AddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		mLayoutInflater=LayoutInflater.from(mContext);
		findView();
		setOnClick();
	}
	public AddSubView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public AddSubView(Context context) {
		this(context, null, 0);
	}
	public void setCount(int count){
		this.count=count;
		if (count!=0) {
			mText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
		}else {
			mText.setTextColor(getResources().getColor(android.R.color.black));
		}
		mText.setText(count+"");
	}
	private void setOnClick() {
		mSub.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (count==0) {
					Toast.makeText(mContext, "数量必须大于等于0", 0).show();
				}else {
					//当减少键按下的时候，如果当前数值为1，则说明颜色需要变化。
					if (count==1) {
						mText.setTextColor(getResources().getColor(android.R.color.black));
					} 
					count--;
					mText.setText(count+"");
					if (mCountChangedListener!=null) {
						mCountChangedListener.onCountChanged(count);
					}
				}
			}
		});
		mAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				count++;
				mText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
				mText.setText(count+"");
				if (mCountChangedListener!=null) {
					mCountChangedListener.onCountChanged(count);
				}
			}
		});
	}
	private void findView() {
		mView=mLayoutInflater.inflate(R.layout.layout_add_sub_view, this);
		mLinearLayout=(LinearLayout) mView.findViewById(R.id.layout_add_and_sub_view);
		mAdd=(ImageView) mLinearLayout.findViewById(R.id.add_and_sub_view_add);
		mSub=(ImageView) mLinearLayout.findViewById(R.id.add_and_sub_view_sub);
		mText=(TextView) mLinearLayout.findViewById(R.id.add_and_sub_view_txt);
	}
	
	public void addOnCountChangedListener(OnCountChangedListener mCountChangedListener){
		this.mCountChangedListener=mCountChangedListener;
	}
	public interface OnCountChangedListener{
		public void onCountChanged(int count);
	} 
}
