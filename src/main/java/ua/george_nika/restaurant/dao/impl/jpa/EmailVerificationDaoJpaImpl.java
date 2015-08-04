package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.EmailVerificationDao;
import ua.george_nika.restaurant.entity.EmailVerificationEntity;

@Repository
public class EmailVerificationDaoJpaImpl
        extends AbstractDaoJpaImpl<EmailVerificationEntity>
        implements EmailVerificationDao {

    @Override
    protected Class getEntityClass() {
        return EmailVerificationEntity.class;
    }

    public EmailVerificationEntity getByCode(String code) {
        return getSingleBy("code", code);
    }

    public int getCountRecordWithCode(String code) {
        return getCountRecordsWith("code", code);
    }
}
