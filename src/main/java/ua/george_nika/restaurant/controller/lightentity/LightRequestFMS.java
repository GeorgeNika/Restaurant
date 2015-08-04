package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.RequestFMSEntity;

import java.sql.Timestamp;

// for send with ajax. Normal Entity has a many dependency
public class LightRequestFMS {

    private int idRequestFMS;
    private String info;
    private Timestamp created;
    private String account;
    private String client;

    public LightRequestFMS() {
    }

    public LightRequestFMS(RequestFMSEntity requestFMSEntity) {
        this.setIdRequestFMS(requestFMSEntity.getIdRequestFMS());
        this.setInfo(requestFMSEntity.getInfo());
        this.setAccount(requestFMSEntity.getAccountEntity().getLogin());
        this.setClient(requestFMSEntity.getClientEntity().getClientName());
        this.setCreated(requestFMSEntity.getCreated());
    }

    public int getIdRequestFMS() {
        return idRequestFMS;
    }

    public void setIdRequestFMS(int idRequestFMS) {
        this.idRequestFMS = idRequestFMS;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
