package ua.george_nika.restaurant.dao.impl.jpa;

import org.springframework.stereotype.Repository;
import ua.george_nika.restaurant.dao.intface.ClientDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;

import java.util.List;

@Repository
public class ClientDaoJpaImpl extends AbstractDaoJpaImpl<ClientEntity> implements ClientDao{
    @Override
    protected Class getEntityClass() {
        return ClientEntity.class;
    }

    public int getCountClientsWithName(String name) {
        return getCountRecordsWith("clientName", name);
    }

    public ClientEntity getClientByName(String name) {
        return getSingleBy("clientName", name);
    }

    public ClientEntity getClientById(int id) {
        return getById(id);
    }

    public List<ClientEntity> getClientListByAccountOwner(AccountEntity accountEntity) {
        return getListByObject("accountOwner", accountEntity);
    }



}
