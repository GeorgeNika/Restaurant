package ua.george_nika.restaurant.dao.intface;


import ua.george_nika.restaurant.entity.EmailVerificationEntity;

public interface EmailVerificationDao extends AbstractDao<EmailVerificationEntity> {

    EmailVerificationEntity getByCode(String code);

    int getCountRecordWithCode(String code);
}
