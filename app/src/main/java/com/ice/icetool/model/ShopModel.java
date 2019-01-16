package com.ice.icetool.model;

/**
 * Created by IceWang on 2019/1/4.
 */

public class ShopModel {
    public enum TypeOne{breakfast,lunch,dinner}
    public enum TypeTwo{A,B,C,D}

    public TypeOne typeOne;
    public TypeTwo typeTwo;

    public int id;
    public String name;
    public String unit;
    public float price;
    public float remain;
    public float count;

    public ShopModel() {
    }
}
