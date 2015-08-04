package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.RequestFMSDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.RequestFMSEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("requestFMSService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RequestFMSService extends AbstractSortAndRestrictService {
    private static String LOGGER_NAME = RequestFMSService.class.getSimpleName();

    @Autowired
    RequestFMSDao requestFMSDao;

    @Override
    AbstractDao getDao() {
        return requestFMSDao;
    }

    @Autowired
    ClientService clientService;


    @Transactional(readOnly = false)
    public RequestFMSEntity createNewEmptyRequestFMS(AccountEntity account, ClientEntity group) {
        try {
            RequestFMSEntity requestFMSEntity = new RequestFMSEntity();
            requestFMSEntity.setAccountEntity(account);
            requestFMSEntity.setClientEntity(group);
            requestFMSEntity.setCreated(new Timestamp(new Date().getTime()));
            requestFMSDao.save(requestFMSEntity);

            RestaurantLogger.info(LOGGER_NAME, "Create new request for membership "
                    + requestFMSEntity.getIdRequestFMS());
            return requestFMSEntity;
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create request for membership id: " + account.getIdAccount()
                    + " group: " + group.getIdClient() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create request for membership: - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void updateRequestFMS(RequestFMSEntity requestFMSEntity) {
        try {
            requestFMSDao.update(requestFMSEntity);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't update request id: "
                    + requestFMSEntity.getIdRequestFMS() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update request id: "
                    + requestFMSEntity.getIdRequestFMS() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteRequestFMS(RequestFMSEntity requestFMSEntity) {
        try {
            requestFMSDao.delete(requestFMSEntity);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't delete requestFMS id: " + requestFMSEntity.getIdRequestFMS()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete request - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void acceptRequestFMS(RequestFMSEntity requestFMSEntity) {
        try {
            clientService.addMemberToGroup(requestFMSEntity.getAccountEntity(), requestFMSEntity.getClientEntity());
            requestFMSDao.delete(requestFMSEntity);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't delete request id: " + requestFMSEntity.getIdRequestFMS()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete request - " + ex.getMessage());
        }
    }

    public RequestFMSEntity getRequestFMS(int idAccount, int idClient) {
        try {
            ClientEntity clientEntity = clientService.getClientByIdWithoutCheck(idClient);
            List<RequestFMSEntity> requestFMSList = clientEntity.getRequestFMSList();
            RequestFMSEntity resultRequest = null;
            for (RequestFMSEntity tempRequest : requestFMSList) {
                if (tempRequest.getAccountEntity().getIdAccount() == idAccount) {
                    resultRequest = tempRequest;
                    break;
                }
            }
            if (resultRequest == null) {
                throw new RuntimeException();
            }
            return resultRequest;
        } catch (Exception ex) {
            if (ex.getMessage()!= null) {
                // if we simple don`t found requestFMS then no log
                RestaurantLogger.warn(LOGGER_NAME, "Can't get requestFMS id: " + idAccount
                        + " group: " + idClient + " - " + ex.getMessage());
            }
            throw new UserWrongInputException("Can't get requestFMS - " + ex.getMessage());
        }
    }

    public RequestFMSEntity getRequestFMSById(int idRequestFMS) {
        try {
            RequestFMSEntity resultAccount = requestFMSDao.getById(idRequestFMS);
            return resultAccount;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get requestFMS id: " + idRequestFMS + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get requestFMS - " + ex.getMessage());
        }
    }
}
