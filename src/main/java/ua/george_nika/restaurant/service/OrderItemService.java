package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.OrderItemDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.MenuItemEntity;
import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.entity.OrderItemEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("orderItemService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrderItemService extends AbstractSortAndRestrictService {
    private static String LOGGER_NAME = OrderItemService.class.getSimpleName();

    @Autowired
    OrderItemDao orderItemDao;

    @Override
    AbstractDao getDao() {
        return orderItemDao;
    }

    @Transactional(readOnly = false)
    public void deleteOrderItem(AccountEntity accountEntity, MenuItemEntity menuItemEntity,
                                OrderEntity orderEntity) {
        try {
            if (orderEntity.isDone()) {
                throw new UserWrongInputException("Order is done");
            }
            OrderItemEntity existOrderItem = getOrderItem(accountEntity, menuItemEntity, orderEntity);
            orderItemDao.delete(existOrderItem);
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't delete order item account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete order account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void changeOrderItemQuantity(AccountEntity accountEntity, MenuItemEntity menuItemEntity,
                                        OrderEntity orderEntity, int quantity) {
        try {
            if (orderEntity.isDone()) {
                throw new UserWrongInputException("Order is done");
            }
            OrderItemEntity existOrderItem = getOrderItem(accountEntity, menuItemEntity, orderEntity);
            existOrderItem.setQuantity(quantity);
            orderItemDao.update(existOrderItem);
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't change quantity order item account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't change quantity account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void addNewOrderItem(AccountEntity accountEntity, MenuItemEntity menuItemEntity,
                                OrderEntity orderEntity) {
        try {
            if (orderEntity.isDone()) {
                throw new UserWrongInputException("Order is done");
            }
            try {
                OrderItemEntity existOrderItem = getOrderItem(accountEntity, menuItemEntity, orderEntity);
                existOrderItem.setQuantity(existOrderItem.getQuantity() + 1);
                orderItemDao.update(existOrderItem);
            } catch (Exception ex) {
                OrderItemEntity newOrderItem = new OrderItemEntity();
                newOrderItem.setAccount(accountEntity);
                newOrderItem.setOrder(orderEntity);
                newOrderItem.setMenuItem(menuItemEntity);
                newOrderItem.setQuantity(1);
                newOrderItem.setPrice(0);
                newOrderItem.setCreated(new Timestamp(new Date().getTime()));
                orderItemDao.save(newOrderItem);
                RestaurantLogger.info(LOGGER_NAME, "Create new order item id: " + newOrderItem.getIdOrderItem());
            }
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create new order item account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create person order account id: "
                    + accountEntity.getIdAccount() + " - " + ex.getMessage());
        }
    }

    private OrderItemEntity getOrderItem(AccountEntity accountEntity, MenuItemEntity menuItemEntity,
                                         OrderEntity orderEntity) {
        List<OrderItemEntity> existOrderItemList = orderEntity.getOrderItemList();
        for (OrderItemEntity tempOrderItem : existOrderItemList) {
            if ((tempOrderItem.getMenuItem().equals(menuItemEntity))
                    && (tempOrderItem.getAccount().equals(accountEntity))) {
                return tempOrderItem;
            }
        }
        throw new UserWrongInputException("no such order item");
    }


}
