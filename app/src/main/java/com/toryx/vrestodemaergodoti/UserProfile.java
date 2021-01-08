package com.toryx.vrestodemaergodoti;

public class UserProfile {

    public String userN;
    public String userE;
    public String userT;


    public UserProfile(){

    }

    public UserProfile(String userN, String userE,String userT) {
        this.userN = userN;
        this.userE = userE;
        this.userT = userT;


    }



    public String getUserN() {
        return userN;
    }

    public void setUserN(String userN) {
        this.userN = userN;
    }

    public String getUserT() {
        return userT;
    }

    public void setUserT(String userT) {
        this.userT = userT;
    }

    public String getUserE() {
        return userE;
    }

    public void setUserE(String userE) {
        this.userE = userE;
    }



}
