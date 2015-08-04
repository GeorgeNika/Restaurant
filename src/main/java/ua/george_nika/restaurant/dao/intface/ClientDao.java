package ua.george_nika.restaurant.dao.intface;


import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;

import java.util.List;

public interface ClientDao extends AbstractDao<ClientEntity> {

    int getCountClientsWithName(String login);

    ClientEntity getClientByName(String name);

    ClientEntity getClientById(int id);

    List<ClientEntity> getClientListByAccountOwner(AccountEntity accountEntity);
}
