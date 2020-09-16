package com.exno.mygetwayrestaurant;

import com.google.gson.annotations.SerializedName;

public class LoginPojo {
    @SerializedName("rid")
    public String rid;
    @SerializedName("res_auth_id")
    public String res_auth_id;
    @SerializedName("rname")
    public String rname;
    @SerializedName("email")
    public String email;
    @SerializedName("contact_no")
    public String contact_no;
    @SerializedName("last_login")
    public String last_login;
    @SerializedName("on_off")
    public String on_off;

    public String getOn_off() {
        return on_off;
    }

    public String getRid() {
        return rid;
    }

    public String getRes_auth_id() {
        return res_auth_id;
    }

    public String getRname() {
        return rname;
    }

    public String getEmail() {
        return email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getLast_login() {
        return last_login;
    }
}
