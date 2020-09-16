package com.exno.mygetwayrestaurant.model;





import com.exno.mygetwayrestaurant.LoginPojo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Testingdata
{
@SerializedName("status")
    private int status;
    public int getStatus()
    {
        return status;
    }
    @SerializedName("data")
    public List<LoginPojo> data = null;



    public List<LoginPojo> getData() {
        return data;
    }


}
