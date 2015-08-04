package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.MenuItemDao;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.MenuItemEntity;
import ua.george_nika.restaurant.entity.MenuGroupEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("menuService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MenuItemService extends AbstractSortAndRestrictService {

    private static String LOGGER_NAME = MenuItemService.class.getSimpleName();

    @Autowired
    MenuItemDao menuItemDao;

    @Override
    AbstractDao getDao() {
        return menuItemDao;
    }

    public MenuItemEntity getMenuItemById(int idMenuItem) {
        try {
            MenuItemEntity resultMenuItem = menuItemDao.getMenuItemById(idMenuItem);
            return resultMenuItem;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get menu item by id: " + idMenuItem + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get menu item by id: " + idMenuItem + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void createNewMenuItem(MenuItemEntity newMenuItem) {
        try {
            MenuGroupEntity menuGroupEntity = getMenuGroupEqualRestriction();
            if (menuGroupEntity== null){
                throw new RuntimeException("no select menu group");
            }
            newMenuItem.setMenuGroupEntity(menuGroupEntity);
            newMenuItem.setMenuItemName("new menu item");
            newMenuItem.setActive(false);
            newMenuItem.setCreated(new Timestamp(new Date().getTime()));
            menuItemDao.save(newMenuItem);

            RestaurantLogger.info(LOGGER_NAME, "Create new menu item " + newMenuItem.getIdMenuItem());
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create menu item: - " + ex.getMessage());
            throw new UserWrongInputException("Can't create menu item: - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void changeEnableMenuItemById(int id) {
        try {
            MenuItemEntity tempMenuItem = menuItemDao.getMenuItemById(id);
            tempMenuItem.setActive(!tempMenuItem.isActive());
            menuItemDao.save(tempMenuItem);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't change enable menu item by id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't change enable menu item by id: " + id + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void updateMenuItem(MenuItemEntity menuItemEntity) {
        try {
            menuItemDao.update(menuItemEntity);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't update menu item id: " + menuItemEntity.getIdMenuItem()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update menu item id: " + menuItemEntity.getIdMenuItem()
                    + " - " + ex.getMessage());
        }
    }

    public void addMenuGroupEqualRestriction( MenuGroupEntity menuGroupEntity) {
        limitedSortAndRestrict.addMenuGroupEqualRestriction(menuGroupEntity);
    }
    public MenuGroupEntity getMenuGroupEqualRestriction( ) {
        return limitedSortAndRestrict.getMenuGroupEqualRestriction();
    }

    public void clearAllEqualRestriction() {
        limitedSortAndRestrict.clearAllEqualRestriction();
    }

    public List<MenuItemEntity> getPartOfMenu(int offset, int limit) {
        try {
            List<MenuItemEntity> resultList = menuItemDao.getFilteredAndSortedList(offset, limit, limitedSortAndRestrict);
            return resultList;
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get menu items  offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get menu items offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }
}
