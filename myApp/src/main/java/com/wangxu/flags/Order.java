package com.wangxu.flags;

public class Order {
    public static String getOrderTime() {
        return "订单时间:"+orderTime;
    }

    public static void setOrderTime(String orderTime) {
        Order.orderTime = orderTime;
    }

    public static String getOrderCode() {
        return "订单号:"+orderCode;
    }

    public static void setOrderCode(String orderCode) {
        Order.orderCode = orderCode;
    }

    public static String getOrderPickNumber() {
        return "取货号:"+orderPickNumber;
    }

    public static void setOrderPickNumber(String orderPickNumber) {
        Order.orderPickNumber = orderPickNumber;
    }

    public static String orderTime;
    public static String orderCode;
    public static String orderPickNumber;
    public static String showOrder(){
        return "orderTime:"+orderTime+",orderCode:"+orderCode+",orderPickNumber:"+orderPickNumber;
    }
}
