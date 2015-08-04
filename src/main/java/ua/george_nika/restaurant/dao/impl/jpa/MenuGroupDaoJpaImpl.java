package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.MenuGroupDao;
import ua.george_nika.restaurant.entity.MenuGroupEntity;

@Repository
public class MenuGroupDaoJpaImpl extends AbstractDaoJpaImpl<MenuGroupEntity> implements MenuGroupDao {

    @Override
    protected Class getEntityClass() {
        return MenuGroupEntity.class;
    }

    public MenuGroupEntity getMenuGroupById(int id) {
        return getById(id);
    }
}
