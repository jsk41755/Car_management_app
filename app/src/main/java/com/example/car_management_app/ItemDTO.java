package com.example.car_management_app;

//세차 용품 사이트에서 크롤링하여 가져온 값 저장하기
public class ItemDTO {
    private String itemName;
    private String itemCost;
    private String itemImage;

    public ItemDTO(){
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemImage = itemImage;
    }

    public void setitemName (String itemName){
        this.itemName = itemName;
    }

    public String getitemName (){
        return itemName;
    }

    public void setitemCost (String itemCost){
        this.itemCost = itemCost;
    }

    public String getitemCost (){
        return itemCost;
    }

    public void setitemImage (String itemImage){
        this.itemImage = itemImage;
    }

    public String getitemImage (){
        return ("https://autowash.co.kr/" + itemImage);
    }

}
