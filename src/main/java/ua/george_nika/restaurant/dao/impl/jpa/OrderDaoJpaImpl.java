package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.OrderDao;
import ua.george_nika.restaurant.entity.OrderEntity;

@Repository
public class OrderDaoJpaImpl extends AbstractDaoJpaImpl<OrderEntity> implements OrderDao {

    @Override
    protected Class getEntityClass() {
        return OrderEntity.class;
    }

    public OrderEntity getOrderById(int id) {
        return getById(id);
    }
}