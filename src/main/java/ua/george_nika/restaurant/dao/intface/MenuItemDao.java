package ua.george_nika.restaurant.dao.intface;


import ua.george_nika.restaurant.entity.MenuItemEntity;

public interface MenuItemDao extends AbstractDao<MenuItemEntity> {

    MenuItemEntity getMenuItemById(int id);
}
