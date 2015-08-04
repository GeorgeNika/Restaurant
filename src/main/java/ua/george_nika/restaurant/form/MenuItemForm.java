package ua.george_nika.restaurant.form;

import ua.george_nika.restaurant.entity.MenuItemEntity;

import java.io.Serializable;

public class MenuItemForm implements Serializable {

    private String menuItemName;
    private String weight;
    private String price;
    private String photo;

    public MenuItemForm() {
    }

    public MenuItemForm(MenuItemEntity menuItemEntity) {
        this.menuItemName = menuItemEntity.getMenuItemName();
        this.weight = ""+menuItemEntity.getWeight();
        this.price = ""+menuItemEntity.getPrice();
        this.photo = menuItemEntity.getPhoto();
    }

    public void updateMenuItem(MenuItemEntity menuItemEntity) {
        menuItemEntity.setMenuItemName(getMenuItemName());
        menuItemEntity.setWeight(Integer.parseInt(getWeight()));
        menuItemEntity.setPrice(Integer.parseInt(getPrice()));
        menuItemEntity.setPhoto(getPhoto());
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
