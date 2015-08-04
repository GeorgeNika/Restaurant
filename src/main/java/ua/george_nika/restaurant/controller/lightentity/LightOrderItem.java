package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.OrderItemEntity;

public class LightOrderItem {

    private Integer idOrderItem;
    private Integer idGroup;
    private Boolean menuGroup;
    private Integer idMenuItem;
    private String menuItemName;
    private Integer myQuantity;
    private Integer allQuantity;
    private Integer price;
    private Integer weight;

    public LightOrderItem() {
    }

    public LightOrderItem(OrderItemEntity orderItemEntity, Integer idAccount) {
        this.idOrderItem = orderItemEntity.getIdOrderItem();
        this.idGroup = orderItemEntity.getMenuItem().getMenuGroupEntity().getIdMenuGroup();
        this.menuGroup = false;
        this.idMenuItem = orderItemEntity.getMenuItem().getIdMenuItem();
        this.menuItemName = orderItemEntity.getMenuItem().getMenuItemName();
        if (orderItemEntity.getAccount().getIdAccount() == idAccount) {
            this.myQuantity = orderItemEntity.getQuantity();
        }else{
            this.myQuantity = 0;
        }
        this.allQuantity = orderItemEntity.getQuantity();
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

    public Integer getMyQuantity() {
        return myQuantity;
    }

    public void setMyQuantity(Integer myQuantity) {
        this.myQuantity = myQuantity;
    }

    public Integer getAllQuantity() {
        return allQuantity;
    }

    public void setAllQuantity(Integer allQuantity) {
        this.allQuantity = allQuantity;
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
