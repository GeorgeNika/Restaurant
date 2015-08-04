package ua.george_nika.restaurant.form;

import ua.george_nika.restaurant.entity.ClientEntity;

import java.io.Serializable;

public class GroupForm implements Serializable {

    private String name;

    public GroupForm() {
    }

    public GroupForm(ClientEntity clientEntity) {
        this.name = clientEntity.getClientName();
    }

    public void updateGroup(ClientEntity clientEntity) {
        clientEntity.setClientName(getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
