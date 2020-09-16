package com.exno.mygetwayrestaurant;

class DriverList {
                String de_name;
                String de_reg;
                String vehicle_no;
                String contact_no;
                String email_id;
                String order_id;
                String de_id;

    public DriverList(String de_name, String de_reg, String vehicle_no, String contact_no, String email_id, String order_id,String de_id) {
        this.de_name = de_name;
        this.de_reg = de_reg;
        this.vehicle_no = vehicle_no;
        this.contact_no = contact_no;
        this.email_id = email_id;
        this.order_id = order_id;
        this.de_id = de_id;
    }

    public String getDe_id()
    {
        return de_id;
    }

    public String getDe_name() {
        return de_name;
    }

    public String getDe_reg() {
        return de_reg;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getOrder_id() {
        return order_id;
    }
}
