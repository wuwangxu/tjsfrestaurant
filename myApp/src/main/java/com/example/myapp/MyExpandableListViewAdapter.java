package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wangxu.flags.Menus;
import com.wangxu.myself_view.AddSubView;
import com.wangxu.myself_view.AddSubView.OnCountChangedListener;
import com.wangxu.util.FoodMenu;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
	private TextView mTxtOrder;
	private Context mContext;
	public MyExpandableListViewAdapter() {
	}
	public MyExpandableListViewAdapter(Context context,TextView textView) {
		this();
		mContext=context;
		mTxtOrder=textView;
	}
	//获取菜单大项的数量
	public int getGroupCount() {
		return Menus.menuTitles.size();
	}
	//通过父项的下标获得其子项的数量
	public int getChildrenCount(int groupPosition) {
		return Menus.menus.get(Menus.menuTitles.get(groupPosition)).size();
	}
	public Object getGroup(int groupPosition) {
		return Menus.menuTitles.get(groupPosition);
	}
	public Object getChild(int groupPosition, int childPosition) {
		return Menus.menus.get(Menus.menuTitles.get(groupPosition)).get(childPosition);
	}
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	public boolean hasStableIds() {
		return false;
	}
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_group, null);
		}
		((TextView)convertView.findViewById(R.id.group_title)).setText(Menus.menuTitles.get(groupPosition));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//获得当前大项当前小项的food对象
		final FoodMenu foodMenu=Menus.menus.get(Menus.menuTitles.get(groupPosition)).get(childPosition);
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_children, parent,false);
			viewHolder=new ViewHolder();
			viewHolder.foodName=(TextView) convertView.findViewById(R.id.txt_food_name);
			viewHolder.foodPrice=(TextView) convertView.findViewById(R.id.txt_food_price);
			viewHolder.foodUnitPrice=(TextView) convertView.findViewById(R.id.txt_food_unitprice);
			viewHolder.mAddSubView=(AddSubView) convertView.findViewById(R.id.add_and_sub_view);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.foodName.setText(foodMenu.getFoodName());
		viewHolder.foodPrice.setText(foodMenu.getFoodPrice()+"");
		viewHolder.foodUnitPrice.setText(foodMenu.getFoodUnitPrice());
		viewHolder.mAddSubView.setCount(foodMenu.getFoodCount());
		viewHolder.mAddSubView.addOnCountChangedListener(new OnCountChangedListener() {
			public void onCountChanged(int count) {
				foodMenu.setFoodCount(count);
				//如果菜品数量被调节，则会被菜单集合记录到， 如果变化为0则会
				if (count>0) {
					Menus.order.add(foodMenu);
				}else {
					Menus.order.remove(foodMenu);
				}
				int total=0;
				for (FoodMenu food : Menus.order) {
					total=total+food.getFoodPrice()*food.getFoodCount();
				}
				mTxtOrder.setText(total+"");
				Menus.total=total;
			}
		});
		return convertView;
	}
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	static class ViewHolder {
		TextView foodName;
		TextView foodPrice;
		TextView foodUnitPrice;
		AddSubView mAddSubView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	

}
