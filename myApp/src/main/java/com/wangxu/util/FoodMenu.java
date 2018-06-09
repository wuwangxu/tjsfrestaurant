package com.wangxu.util;

public class FoodMenu {
    private String foodName;

    private String foodId;
    private int foodPrice;
    private String foodUnitPrice;
    private int foodCount;
    private String windowId;

    public FoodMenu() {
    }

    public FoodMenu(String foodName, int foodPrice, String foodUnitPrice, String windowId, String foodId) {
        super();
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodUnitPrice = foodUnitPrice;
        this.windowId = windowId;
        this.foodId=foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodUnitPrice() {
        return foodUnitPrice;
    }

    public void setFoodUnitPrice(String foodUnitPrice) {
        this.foodUnitPrice = foodUnitPrice;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public String getWindowId() { return windowId; }

    public void setWindowId(String windowId) { this.windowId = windowId; }

    public String getFoodId() { return foodId; }

    public void setFoodId(String foodId) { this.foodId = foodId; }

}
