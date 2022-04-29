package com.example.car_management_app;

public class Car_Select_Helper {
    boolean isSelect;
    String Oil;

    public Car_Select_Helper(boolean isSelect, String Oil){
        this.isSelect = isSelect;
        this.Oil = Oil;
    }

    public boolean sSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getOil() {
        return Oil;
    }

    public void setOil(String oil) {
        Oil = oil;
    }

    public Car_Select_Helper() {
    }

}
