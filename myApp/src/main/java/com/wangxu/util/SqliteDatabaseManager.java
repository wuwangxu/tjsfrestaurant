package com.wangxu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wangxu.flags.Flags;
import com.wangxu.flags.Menus;

import java.util.ArrayList;

//数据库要做什么？
//1、获取本地的菜单数据
//2、更新本地的菜单数据
//设计特点：只要服务器数据发生任何变化，所有数据必须全部删除重新添加
//使用单例模式进行管理manager
public class SqliteDatabaseManager {
	private final static String DB_NAME="FOOD_MENU_DATABASE";
	private Context mContext;
	private SQLiteDatabase mDatabase;
	private static SqliteDatabaseManager manager;
	//使用的时候传整个app的上下文进来,在其它地方使用manager时直接通过上下文获取实例，若实例不存在，则创建实例，在创建实例的过程中保证相关数据的初始化工作
	public static SqliteDatabaseManager getInstance(Context context){
		if (manager==null) {
			manager=new SqliteDatabaseManager(context);
		}
		return manager;
	}
	//就把第一次创建出来的数据库的Version当做是0
	private SqliteDatabaseManager(Context context) {
		mContext=context;
		MyDatabaseHelper helper=new MyDatabaseHelper(mContext, DB_NAME, null, 1);
//		mDatabase=SQLiteDatabase.openOrCreateDatabase(path, null);
		//当用户第一次使用这个软件的时候，即是第一次创建数据库表格的时候
		mDatabase=helper.getWritableDatabase();
	}
	//创建表
	private void createTable(SQLiteDatabase db){
		for (String menu : Menus.menuTitles) {
			db.execSQL("CREATE TABLE"+menu+"( foodName VARCHAR NOT NULL,foodPrice  INT NOT NULL, foodUnitPrice VARCHAR NOT NULL, windowId VARCHAR NOT NULL, foodId VARCHAR NOT NULL)");
		}
	}
	//更新数据库，将当前的所有表全部删除，重新创建,每次重新创建即计为数据库升级一次
	private void updataTable(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS "+Flags.DB_TABLES_NAME);
		db.execSQL("CREATE TABLE "+Flags.DB_TABLES_NAME+"( names VARCHAR PRIMARY KEY )");
		for (String menu : Menus.menuTitles) {
			db.execSQL("DROP TABLE IF EXISTS "+menu);
			db.execSQL("CREATE TABLE "+menu+"( foodName VARCHAR NOT NULL,foodPrice  INT NOT NULL, foodUnitPrice VARCHAR NOT NULL, windowId VARCHAR NOT NULL, foodId VARCHAR NOT NULL)");
		}
		
	}
	//获得数据
	public void getMenuData(){
		//为防止意外，先清空所有数据
		Menus.clearMenu();
		//先获得各表格的名称
		Cursor cursor2=mDatabase.query(Flags.DB_TABLES_NAME, new String[]{"names"}, null, null, null, null, null);
		while (cursor2.moveToNext()) {
			Menus.menuTitles.add(cursor2.getString(0));
		}
		//每次完成一个表格的录入
		for (String name : Menus.menuTitles) {
			Cursor cursor=mDatabase.query(name, new String[]{"foodName","foodPrice","foodUnitPrice","windowId","foodId"},
					null, null, null, null, null);
			ArrayList<FoodMenu> array=new ArrayList<FoodMenu>();
			while (cursor.moveToNext()) {
				FoodMenu menu=new FoodMenu(cursor.getString(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
				array.add(menu);
			}
			Menus.menus.put(name, array);
		}
	}
	//更新数据库数据，把原数据全部删除，重新创建表格添加数据
	public void updateDatabaseData(){
		//将所有表格全部删除重建
		updataTable(mDatabase);
		//录入表格名称表信息
		for (String tablename : Menus.menuTitles) {
			ContentValues values=new ContentValues();
			values.put("names", tablename);
			mDatabase.insert(Flags.DB_TABLES_NAME, null, values);
		}
		for (String tableName : Menus.menuTitles) {
			ArrayList<FoodMenu> array=Menus.menus.get(tableName);
			for (FoodMenu foodMenu : array) {
				ContentValues values=new ContentValues();
				values.put("foodName", foodMenu.getFoodName());
				values.put("foodPrice", foodMenu.getFoodPrice());
				values.put("foodUnitPrice", foodMenu.getFoodUnitPrice());
				values.put("windowId", foodMenu.getWindowId());
				values.put("foodId", foodMenu.getFoodId());
				mDatabase.insert(tableName, null, values);
			}
		}
		Log.i("Msg","OK");
	}
	
	class MyDatabaseHelper extends SQLiteOpenHelper{
		public MyDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//第一次创建时创建存放表格名称的表
			db.execSQL("CREATE TABLE "+Flags.DB_TABLES_NAME+"( names VARCHAR PRIMARY KEY )");
			for (String menu : Menus.menuTitles) {
				db.execSQL("CREATE TABLE "+menu+" (foodName VARCHAR NOT NULL,foodPrice  INT NOT NULL, foodUnitPrice VARCHAR NOT NULL, windowId VARCHAR NOT NULL, foodId VARCHAR NOT NULL)");
			}
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}
}
