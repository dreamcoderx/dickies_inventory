package com.btw.dickies.model;

public class Product {
    private String item_no;
    private String item_name;
    private String brand;
    private String department;
    private String sub_department;
    private String category;
    private String sub_category;
    private String style_code;
    private String size;
    private String child_size;
    private String color;
    private String child_color;
    private String itemcode;

    public Product() {
    }

    public Product(String item_no, String item_name, String brand, String department,
                   String sub_department, String category, String sub_category, String style_code,
                   String size, String child_size, String color, String child_color, String itemcode) {

        this.item_no = item_no;
        this.item_name = item_name;
        this.brand = brand;
        this.department = department;
        this.sub_department = sub_department;
        this.category = category;
        this.sub_category = sub_category;
        this.style_code = style_code;
        this.size = size;
        this.child_size = child_size;
        this.color = color;
        this.child_color = child_color;
        this.itemcode = itemcode;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSub_department() {
        return sub_department;
    }

    public void setSub_department(String sub_department) {
        this.sub_department = sub_department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getStyle_code() {
        return style_code;
    }

    public void setStyle_code(String style_code) {
        this.style_code = style_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getChild_size() {
        return child_size;
    }

    public void setChild_size(String child_size) {
        this.child_size = child_size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChild_color() {
        return child_color;
    }

    public void setChild_color(String child_color) {
        this.child_color = child_color;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }
}
