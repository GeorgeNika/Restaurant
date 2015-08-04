package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.MenuGroupDao;
import ua.george_nika.restaurant.entity.MenuGroupEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantLogger;

@Service("menuGroupService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MenuGroupService extends AbstractSortAndRestrictService {

    private static String LOGGER_NAME = MenuGroupService.class.getSimpleName();

    @Autowired
    MenuGroupDao menuGroupDao;

    @Override
    AbstractDao getDao() {
        return menuGroupDao ;
    }

    public MenuGroupEntity getMenuGroupById(int idMenuGroup) {
        try {
            MenuGroupEntity resultMenuGroup = menuGroupDao.getMenuGroupById(idMenuGroup);
            return resultMenuGroup;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get menu group by id: " + idMenuGroup + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get menu group by id: " + idMenuGroup + " - " + ex.getMessage());
        }
    }

}
