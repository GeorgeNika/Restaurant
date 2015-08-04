package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.OrderItemDao;
import ua.george_nika.restaurant.entity.OrderItemEntity;

@Repository
public class OrderItemDaoJpaImpl extends AbstractDaoJpaImpl<OrderItemEntity> implements OrderItemDao {

    @Override
    protected Class getEntityClass() {
        return OrderItemEntity.class;
    }
}
