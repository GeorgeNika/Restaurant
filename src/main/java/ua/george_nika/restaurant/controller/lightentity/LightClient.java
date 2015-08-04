package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.ClientEntity;

import java.sql.Timestamp;

// for send with ajax. Normal Entity has a many dependency
public class LightClient {
    private int idClient;
    private String clientName;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;
    private String clientOwner;
    private int groupSize;

    public LightClient() {
    }

    public LightClient(ClientEntity clientEntity) {
        this.setActive(clientEntity.isActive());
        this.setIdClient(clientEntity.getIdClient());
        this.setClientName(clientEntity.getClientName());
        this.setClientOwner(clientEntity.getAccountOwner().getLogin());
        this.setCreated(clientEntity.getCreated());
        this.setGroupSize(clientEntity.getMemberList().size());
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getClientOwner() {
        return clientOwner;
    }

    public void setClientOwner(String clientOwner) {
        this.clientOwner = clientOwner;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}
