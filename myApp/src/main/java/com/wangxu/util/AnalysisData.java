package com.wangxu.util;

import com.wangxu.flags.Menus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//把从服务器获得的字符串解析到menu里
public class AnalysisData {
	public AnalysisData(String str) {
		try {
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("data");
//			Log.i("Msg", array.toString());
			boolean have=false;
			// 完成菜单大项的录入
			for (int i = 0; i <array.length(); i++) {
				if (array.getJSONObject(i).getString("windowName").equals("酒水饮料")) {
					have=true;
				}else {
//					Log.i("Msg",array.getJSONObject(i).getString("windowName"));
					Menus.menuTitles.add(array.getJSONObject(i).getString("windowName"));
				}
			}
			if (have) {
				Menus.menuTitles.add("酒水饮料");
			}
			for (int j=0;j<array.length();j++) {
				ArrayList<FoodMenu> arrayList = new ArrayList<FoodMenu>();
				JSONArray a = array.getJSONObject(j).getJSONArray("menus");
//				Log.i("Msg",a.toString());
				for (int i = 0; i < a.length(); i++) {
					JSONObject o = a.getJSONObject(i);
					FoodMenu food = new FoodMenu(o.getString("menuName"),
							o.getInt("menuDishPrice"), "/份",o.getString("windowId"),o.getString("menuId"));
					arrayList.add(food);
				}
				Menus.menus.put(array.getJSONObject(j).getString("windowName"), arrayList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
