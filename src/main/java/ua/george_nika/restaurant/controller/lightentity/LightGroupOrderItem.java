package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.OrderItemEntity;

public class LightGroupOrderItem {

    private Integer idOrderItem;
    private Integer idGroup;
    private Boolean menuGroup;
    private Integer idMenuItem;
    private String menuItemName;
    private Integer quantity;
    private Integer price;
    private Integer weight;

    public LightGroupOrderItem() {
    }

    public LightGroupOrderItem(OrderItemEntity orderItemEntity) {
        this.idOrderItem = orderItemEntity.getIdOrderItem();
        this.idGroup = orderItemEntity.getMenuItem().getMenuGroupEntity().getIdMenuGroup();
        this.menuGroup = false;
        this.idMenuItem = orderItemEntity.getMenuItem().getIdMenuItem();
        this.menuItemName = orderItemEntity.getMenuItem().getMenuItemName();
        this.quantity = orderItemEntity.getQuantity();
        if (orderItemEntity.getOrder().isDone()) {
            this.price = orderItemEntity.getPrice();
        }else{
            this.price = orderItemEntity.getMenuItem().getPrice();
        }
        this.weight = orderItemEntity.getMenuItem().getWeight();
    }

    public Integer getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(Integer idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public Boolean getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(Boolean menuGroup) {
        this.menuGroup = menuGroup;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getIdMenuItem() {
        return idMenuItem;
    }

    public void setIdMenuItem(Integer idMenuItem) {
        this.idMenuItem = idMenuItem;
    }
}
