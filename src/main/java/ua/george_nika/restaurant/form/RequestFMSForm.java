package ua.george_nika.restaurant.form;

import ua.george_nika.restaurant.entity.RequestFMSEntity;

import java.io.Serializable;

public class RequestFMSForm implements Serializable {

    private String info;

    public RequestFMSForm() {
    }

    public RequestFMSForm(RequestFMSEntity requestFMSEntity) {
        this.info = requestFMSEntity.getInfo();
    }

    public void updateRequestFMS(RequestFMSEntity requestFMSEntity) {
        requestFMSEntity.setInfo(getInfo());
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
