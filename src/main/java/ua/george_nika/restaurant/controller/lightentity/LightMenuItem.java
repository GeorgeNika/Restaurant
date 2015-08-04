package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.MenuItemEntity;

import java.sql.Timestamp;

// for send with ajax. Normal Entity has a many dependency
public class LightMenuItem {
    private Integer idMenuItem;
    private String menuItemName;
    private Integer weight;
    private Integer price;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;
    private String photo;

    public LightMenuItem() {
    }

    public LightMenuItem(MenuItemEntity menuItemEntity) {
        this.setActive(menuItemEntity.isActive());
        this.setIdMenuItem(menuItemEntity.getIdMenuItem());
        this.setMenuItemName(menuItemEntity.getMenuItemName());
        this.setWeight(menuItemEntity.getWeight());
        this.setPrice(menuItemEntity.getPrice());
        this.setCreated(menuItemEntity.getCreated());
        this.setUpdated(menuItemEntity.getUpdated());
        this.setPhoto(menuItemEntity.getPhoto());
    }

    public Integer getIdMenuItem() {
        return idMenuItem;
    }

    public void setIdMenuItem(Integer idMenuItem) {
        this.idMenuItem = idMenuItem;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
