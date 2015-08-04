package ua.george_nika.restaurant.dao.intface;


import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.entity.OrderItemEntity;

public interface OrderDao extends AbstractDao<OrderEntity> {

    OrderEntity getOrderById(int id);

}
