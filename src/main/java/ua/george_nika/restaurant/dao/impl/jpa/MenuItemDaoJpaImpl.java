package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.MenuItemDao;
import ua.george_nika.restaurant.entity.MenuItemEntity;

@Repository
public class MenuItemDaoJpaImpl extends AbstractDaoJpaImpl<MenuItemEntity> implements MenuItemDao {

    @Override
    protected Class getEntityClass() {
        return MenuItemEntity.class;
    }

    public MenuItemEntity getMenuItemById(int id) {
        return getById(id);
    }
}
