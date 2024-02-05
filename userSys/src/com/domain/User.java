package com.domain;

public class User {
    private String uid;
    private String uname;
    private String upwd;
    private String umajor;
    private String utype;

    public User(){};
    public User(String uid, String uname, String upwd, String umajor, String utype) {
        this.uid = uid;
        this.uname = uname;
        this.upwd = upwd;
        this.umajor = umajor;
        this.utype = utype;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUmajor() {
        return umajor;
    }

    public void setUmajor(String umajor) {
        this.umajor = umajor;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }
}
