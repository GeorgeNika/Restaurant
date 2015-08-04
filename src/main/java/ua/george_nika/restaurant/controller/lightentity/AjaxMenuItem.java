package ua.george_nika.restaurant.controller.lightentity;

import java.util.List;

public class AjaxMenuItem {

    private String menuItemName;
    private List<LightMenuItem> entityList;

    public AjaxMenuItem() {
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public List<LightMenuItem> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<LightMenuItem> entityList) {
        this.entityList = entityList;
    }
}
