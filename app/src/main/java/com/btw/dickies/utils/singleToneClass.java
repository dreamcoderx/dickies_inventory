package com.btw.dickies.utils;

public class singleToneClass {
        //String s;
    String userName;
    Integer intUserID;
    String strPlateNumber;
    String strUserType;
    int user_type;


    public String getStrUserType() {
        return strUserType;
    }

    public void setStrUserType(String strUserType) {
        this.strUserType = strUserType;
    }

    private static final singleToneClass ourInstance = new singleToneClass();
    public static singleToneClass getInstance() {
        return ourInstance;
    }
    private singleToneClass() {

    }
    public void setUserType(int i){
        this.user_type = i;
    }

    public int getUserType(){
        return this.user_type;
    }

    public void setUserID(Integer intUserID) {
        this.intUserID=intUserID;
    }

    public void setStrPlateNumber(String strPlateNumber) {
        this.strPlateNumber = strPlateNumber;
    }

    public String getStrPlateNumber() {
        return strPlateNumber;
    }

    public Integer getUserID() {
    return intUserID;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
