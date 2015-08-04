package ua.george_nika.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.ClientDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("clientService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ClientService extends AbstractSortAndRestrictService {
    private static String LOGGER_NAME = ClientService.class.getSimpleName();

    @Autowired
    ClientDao clientDao;

    @Override
    AbstractDao getDao() {
        return clientDao;
    }

    @Autowired
    AccountService accountService;


    @Transactional(readOnly = false)
    public void createNewClient(ClientEntity newClient) {
        try {
            checkAllRequirements(newClient);
            newClient.setActive(true);
            newClient.setManualCreatedGroup(false);
            newClient.setCreated(new Timestamp(new Date().getTime()));
            clientDao.save(newClient);

            RestaurantLogger.info(LOGGER_NAME, "Create new group " + newClient.getIdClient());
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create group: " + newClient.getClientName()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create group: " + newClient.getClientName()
                    + " - " + ex.getMessage());
        }
    }

    private void checkAllRequirements(ClientEntity clientEntity) {
        if (clientEntity.getClientName().isEmpty()) {
            throw new UserWrongInputException("Empty name");
        }
        if (!isUniqueClientName(clientEntity, clientEntity.getClientName())) {
            throw new UserWrongInputException("Name busy");
        }
    }

    private Boolean isUniqueClientName(ClientEntity clientEntity, String newClientName) {
        int count = clientDao.getCountClientsWithName(newClientName);
        if (count != 0) {
            ClientEntity tempClient = clientDao.getClientByName(newClientName);
            if (clientEntity.getIdClient() != tempClient.getIdClient()) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void updateGroup(ClientEntity group) {
        try {
            checkAllRequirements(group);
            clientDao.update(group);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't update group id: " + group.getIdClient()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update group id: " + group.getIdClient()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void addMemberToGroup(AccountEntity accountEntity, ClientEntity group) {
        try {
            group.addMember(accountEntity);
            clientDao.save(group);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't add member: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't add member: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteMemberFromGroup(AccountEntity accountEntity, ClientEntity group) {
        try {
            // todo check for book
            group.deleteMember(accountEntity);
            clientDao.save(group);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't delete member: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete member: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void leaveGroup(AccountEntity accountEntity, ClientEntity group) {
        try {
            // todo check for book
            group.deleteMember(accountEntity);
            clientDao.save(group);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't leave group: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't leave group: " + accountEntity.getIdAccount()
                    + " to group: " + group.getIdClient() + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void disbandGroup(ClientEntity group) {
        try {
            // todo check for book
            clientDao.delete(group);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't delete group: " + group.getIdClient()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete group: " + group.getIdClient()
                    + " - " + ex.getMessage());
        }
    }



    public ClientEntity getClientByOwnerById(AccountEntity accountEntity, int idClient) {
        try {
            checkAccountOwnerPermissionById(accountEntity, idClient);
            ClientEntity resultClient = clientDao.getClientById(idClient);
            return resultClient;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get client by id: " + idClient + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get client by id: " + idClient + " - " + ex.getMessage());
        }
    }
    private void checkAccountOwnerPermissionById(AccountEntity currentAccount, int idClient) {
        boolean result = false;
        ClientEntity clientEntity = clientDao.getClientById(idClient);
        if (currentAccount.getIdAccount() == clientEntity.getAccountOwner().getIdAccount()) {
            result = true;
        } else if (currentAccount.isAdmin()) {
            result = true;
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO permission");
        }
    }

    public ClientEntity getClientByMemberById(AccountEntity accountEntity, int idClient) {
        try {
            checkAccountMemberPermissionById(accountEntity, idClient);
            ClientEntity resultClient = clientDao.getClientById(idClient);
            return resultClient;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get client by id: " + idClient + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get client by id: " + idClient + " - " + ex.getMessage());
        }
    }
    private void checkAccountMemberPermissionById(AccountEntity currentAccount, int idClient) {
        boolean result = false;
        ClientEntity clientEntity = clientDao.getClientById(idClient);
        if (currentAccount.getIdAccount() == clientEntity.getAccountOwner().getIdAccount()) {
            // owner
            result = true;
        } else if (clientEntity.getMemberList().contains(currentAccount)) {
            // member
            result = true;
        } else if (currentAccount.isAdmin()) {
            // admin
            result = true;
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO permission");
        }
    }

    public ClientEntity getClientByIdWithoutCheck( int idClient) {
        try {
            ClientEntity resultClient = clientDao.getClientById(idClient);
            return resultClient;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get client by id: " + idClient + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get client by id: " + idClient + " - " + ex.getMessage());
        }
    }

    public int getCountClientsWithName(String name) {
        try {
            return clientDao.getCountClientsWithName(name);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get count client by name: " + name + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get count client by name: " + name + " - " + ex.getMessage());
        }
    }

    public ClientEntity getClientByName(String name) {
        try {
            return clientDao.getClientByName(name);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get client by name: " + name + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get client by name: " + name + " - " + ex.getMessage());
        }
    }

    public ClientEntity getMyPersonClientByIdAccount(int idAccount) {
        try {
            AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(idAccount);
            List<ClientEntity> tempList = accountEntity.getOwnerClientList();
            ClientEntity resultClient = null;

            // select client witch only for one my person
            for (ClientEntity tempClient : tempList ) {
                if (!tempClient.isManualCreatedGroup()) {
                    resultClient = tempClient;
                }
            }
            if (resultClient == null) {
                throw new RuntimeException("no needed client");
            } else {
                return resultClient;
            }
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get person client: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get person client: " + idAccount + " - " + ex.getMessage());
        }
    }

    public List<ClientEntity> getIOwnerOfGroupListByIdAccount(int idAccount) {
        try {
            AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(idAccount);
            List<ClientEntity> resultList = accountEntity.getOwnerClientList();

            // delete client witch only for one my person
            for (Iterator<ClientEntity> iterator = resultList.listIterator(); iterator.hasNext(); ) {
                ClientEntity tempClient = iterator.next();
                if (!tempClient.isManualCreatedGroup()) {
                    iterator.remove();
                    break;
                }
            }

            return resultList;
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get owner list: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get owner list: " + idAccount + " - " + ex.getMessage());
        }
    }

    public List<ClientEntity> getIMemberOfGroupListByIdAccount(int idAccount) {
        try {
            AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(idAccount);
            List<ClientEntity> resultList = accountEntity.getMemberClientList();
            return resultList;
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get member list: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get member list: " + idAccount + " - " + ex.getMessage());
        }
    }

    public void addGroupManualActiveEqualRestriction(Boolean active) {
        limitedSortAndRestrict.addGroupManualActiveEqualRestriction(active);
    }

    public void clearAllEqualRestriction() {
        limitedSortAndRestrict.clearAllEqualRestriction();
    }

    public List<ClientEntity> getPartOfClient(int offset, int limit) {
        try {
            List<ClientEntity> resultList = clientDao.getFilteredAndSortedList(offset, limit, limitedSortAndRestrict);
            return resultList;
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get clients offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get clients offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }
}
