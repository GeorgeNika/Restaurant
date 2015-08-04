package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.OrderDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.entity.OrderItemEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.sql.Timestamp;
import java.util.*;

@Service("orderService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrderService extends AbstractSortAndRestrictService {
    private static String LOGGER_NAME = OrderService.class.getSimpleName();

    @Autowired
    OrderDao orderDao;

    @Override
    AbstractDao getDao() {
        return orderDao;
    }

    @Autowired
    ClientService clientService;


    public OrderEntity getOrderById(AccountEntity currentAccount, int idEditOrder) {
        try {
            OrderEntity resultOrder = orderDao.getOrderById(idEditOrder);
            checkOrderPermissionByAccount(currentAccount, resultOrder);
            return resultOrder;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get order by id: " + idEditOrder + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get order by id: " + idEditOrder + " - " + ex.getMessage());
        }
    }

    private void checkOrderPermissionByAccount(AccountEntity currentAccount, OrderEntity editOrder) {
        boolean result = false;
        if (editOrder.getClient().getAccountOwner().equals(currentAccount)) {
            // i owner
            result = true;
        } else if (editOrder.getClient().getMemberList().contains(currentAccount)) {
            // i member
            result = true;
        } else if (currentAccount.isAdmin()) {
            // i admin
            result = true;
        }
        if (!result) {
            throw new UserWrongInputException("Have NO permission");
        }
    }


    @Transactional(readOnly = false)
    public void createNewOrder(ClientEntity clientEntity, OrderEntity newOrderEntity) {
        try {

            newOrderEntity.setClient(clientEntity);
            newOrderEntity.setStatus(0);
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTime(new Date());
            calendar.get(Calendar.YEAR);
            String month = "" + (calendar.get(Calendar.MONTH) + 1);
            String day = "" + (calendar.get(Calendar.DAY_OF_WEEK) + 1);
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                day = "0" + day;
            }
            if ((calendar.get(Calendar.MONTH) + 1) < 10) {
                month = "0" + month;
            }
            newOrderEntity.setOrderTime("" + calendar.get(Calendar.YEAR) + "/" + month + "/" + day + " 12:00");
            newOrderEntity.setCreated(new Timestamp(new Date().getTime()));
            orderDao.save(newOrderEntity);

            RestaurantLogger.info(LOGGER_NAME, "Create new person order id: " + newOrderEntity.getIdOrder());
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create person order client id: " + clientEntity.getIdClient()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create person order client id: " + clientEntity.getIdClient()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void changeOrderTime(OrderEntity orderEntity, String orderTime) {
        try {
            orderEntity.setOrderTime(orderTime);
            checkOrderTime(orderEntity.getOrderTime());
            orderDao.update(orderEntity);
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't change time order id: " + orderEntity.getIdOrder()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't change time order id: " + orderEntity.getIdOrder()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void sendOrderToDone(OrderEntity orderEntity) {
        try {
            checkOrderTime(orderEntity.getOrderTime());
            if (orderEntity.getOrderItemList().size() == 0) {
                throw new RuntimeException("no item in order");
            }
            Integer resultCost = 0;
            List<OrderItemEntity> tempOrderItemList = orderEntity.getOrderItemList();
            for (OrderItemEntity tempOrderItem : tempOrderItemList) {
                tempOrderItem.setPrice(tempOrderItem.getMenuItem().getPrice());
                resultCost += tempOrderItem.getPrice() * tempOrderItem.getQuantity();
            }
            orderEntity.setStatus(RestaurantConstant.ORDER_STATUS_DONE);
            orderEntity.setCost(resultCost);
            orderDao.update(orderEntity);

        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't send order to done id: " + orderEntity.getIdOrder()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't send order to done id: " + orderEntity.getIdOrder()
                    + " - " + ex.getMessage());
        }
    }

    private void checkOrderTime(String orderTime) {
        //   YYYY/MM/DD HH:MM
        try {
            Integer orderYear = Integer.parseInt(orderTime.substring(0, 4));
            Integer orderMonth = Integer.parseInt(orderTime.substring(5, 7));
            Integer orderDay = Integer.parseInt(orderTime.substring(8, 10));
            Integer orderHour = Integer.parseInt(orderTime.substring(11, 13));
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTime(new Date());
            Integer currentYear = calendar.get(Calendar.YEAR);
            Integer currentMonth = (calendar.get(Calendar.MONTH) + 1);
            Integer currentDay = (calendar.get(Calendar.DAY_OF_WEEK) + 1);
            Integer currentHour = (calendar.get(Calendar.HOUR_OF_DAY) + 1);
            if (orderYear > currentYear) {
                return;
            }
            if (orderMonth > currentMonth) {
                return;
            }
            if (orderDay > currentDay) {
                return;
            }
            if (orderHour > currentHour) {
                return;
            }
            throw new RuntimeException("Wrong order data:time");
        } catch (Exception ex) {
            throw new RuntimeException("data error");
        }
    }

    public void addOrderClientEqualRestriction(ClientEntity clientEntity) {
        limitedSortAndRestrict.addOrderClientEqualRestriction(clientEntity);
    }

    public void clearAllEqualRestriction() {
        limitedSortAndRestrict.clearAllEqualRestriction();
    }

    public List<OrderEntity> getPartOfOrder(int offset, int limit) {
        try {
            return orderDao.getFilteredAndSortedList(offset, limit, limitedSortAndRestrict);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get orders offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get orders offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }
}
