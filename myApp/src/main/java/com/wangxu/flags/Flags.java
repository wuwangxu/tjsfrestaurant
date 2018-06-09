package com.wangxu.flags;

public class Flags {
	private static final String IP="http://10.0.2.2:8088/order-system/api";
	//专用做下载数据的服务器
	public static final String DOWNLOAD_DATA_URL=IP+"/menu/queryMenusByCondition";
	public static final String GET_DATABASE_VERSION=IP+"/menu/getMenuVersion";
	public static final String SUBMIT_ORDER=IP+"/order/saveCustomerOrder";
	public static final String CALL_WAITER=IP+"/call.do";
	public static final String DB_TABLES_NAME="表格名称表";
	public static final String LOGIN=IP+"/customer/loginCustomer";
	public static final String GET_ORDER_LASTED=IP+"/order/getCustomerOrders/";

	
}
