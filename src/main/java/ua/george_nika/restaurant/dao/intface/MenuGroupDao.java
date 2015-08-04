package ua.george_nika.restaurant.dao.intface;


import ua.george_nika.restaurant.entity.MenuGroupEntity;

public interface MenuGroupDao extends AbstractDao<MenuGroupEntity> {

    MenuGroupEntity getMenuGroupById(int id);

}
