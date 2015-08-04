package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.RequestFMSDao;
import ua.george_nika.restaurant.entity.RequestFMSEntity;

@Repository
public class RequestFMSDaoJpaImpl extends AbstractDaoJpaImpl<RequestFMSEntity>
        implements RequestFMSDao {

    @Override
    protected Class getEntityClass() {
        return RequestFMSEntity.class;
    }


}
