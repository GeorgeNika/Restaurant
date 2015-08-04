package ua.george_nika.restaurant.dao.util;

import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.MenuGroupEntity;
import ua.george_nika.restaurant.service.AccountService;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.MenuItemService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.util.HashMap;
import java.util.Map;

public class LimitedSortAndRestriction extends SortAndRestrictForEntity {

    private static String LOGGER_NAME = LimitedSortAndRestriction.class.getSimpleName();

    public static Map<Class, String> idMap = new HashMap<Class, String>();
    public static Map<Class, String> nameMap = new HashMap<Class, String>();

    static {
        idMap.put(AccountService.class, NameOfFieldConstants.ACCOUNT_ID);
        idMap.put(ClientService.class, NameOfFieldConstants.CLIENT_ID);
        idMap.put(MenuItemService.class, NameOfFieldConstants.MENU_ITEM_ID);
        idMap.put(OrderService.class, NameOfFieldConstants.ORDER_ITEM_ID);

        nameMap.put(AccountService.class, NameOfFieldConstants.ACCOUNT_LOGIN);
        nameMap.put(ClientService.class, NameOfFieldConstants.CLIENT_NAME);
        nameMap.put(MenuItemService.class, NameOfFieldConstants.MENU_ITEM_NAME);
        nameMap.put(OrderService.class, NameOfFieldConstants.ORDER_ITEM_NAME);
    }

    private String getIdFieldName(Class typeClass) {
        if (idMap.containsKey(typeClass)) {
            return idMap.get(typeClass);
        } else {
            RestaurantLogger.error(LOGGER_NAME, "Wrong class for Id :" + typeClass);
            throw new RuntimeException("Wrong class for Id :" + typeClass);
        }
    }

    private String getNameFieldName(Class typeClass) {
        if (nameMap.containsKey(typeClass)) {
            return nameMap.get(typeClass);
        } else {
            RestaurantLogger.error(LOGGER_NAME, "Wrong class for Name :" + typeClass);
            throw new RuntimeException("Wrong class for Name :" + typeClass);
        }
    }


    // Like Block


    public void addIdLikeRestriction(Class typeClass, String likeRestriction) {
        addLikeRestriction(getIdFieldName(typeClass), likeRestriction);
    }

    public void addNameLikeRestriction(Class typeClass, String likeRestriction) {
        addLikeRestriction(getNameFieldName(typeClass), likeRestriction);
    }

    public void deleteIdLikeRestriction(Class typeClass) {
        deleteLikeRestriction(getIdFieldName(typeClass));
    }

    public void deleteNameLikeRestriction(Class typeClass) {
        deleteLikeRestriction(getNameFieldName(typeClass));
    }

    public String getIdLikeRestriction(Class typeClass) {
        String result = getLikeRestrictionForEntity().get(getIdFieldName(typeClass));
        if (result != null) {
            return result;
        } else {
            return "";
        }
    }

    public String getNameLikeRestriction(Class typeClass) {
        String result = getLikeRestrictionForEntity().get(getNameFieldName(typeClass));
        if (result != null) {
            return result;
        } else {
            return "";
        }
    }

    //Sort Block

    public void nextIdSort(Class typeClass) {
        nextSort(getIdFieldName(typeClass));
    }

    public void nextNameSort(Class typeClass) {
        nextSort(getNameFieldName(typeClass));
    }


    // Equal Block

    public void addGroupManualActiveEqualRestriction(Boolean active) {
        addEqualRestriction(NameOfFieldConstants.CLIENT_MANUAL_GROUP, active);
    }

    public void addMenuGroupEqualRestriction(MenuGroupEntity menuGroupEntity) {
        addEqualRestriction(NameOfFieldConstants.MENU_GROUP, menuGroupEntity);
    }

    public MenuGroupEntity getMenuGroupEqualRestriction() {
        return (MenuGroupEntity)getEqualRestrictionForEntity().get(NameOfFieldConstants.MENU_GROUP);
    }

    public void addOrderClientEqualRestriction(ClientEntity clientEntity) {
        addEqualRestriction(NameOfFieldConstants.ORDER_CLIENT, clientEntity);
    }

}
