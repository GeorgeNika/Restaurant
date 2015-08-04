package ua.george_nika.restaurant.security;

import org.springframework.security.core.userdetails.User;
import ua.george_nika.restaurant.entity.AccountEntity;

public class CurrentAccount extends User {

    private final int idAccount;

    public CurrentAccount (AccountEntity accountEntity){
        super(accountEntity.getLogin(), accountEntity.getPassword(), accountEntity.isActive(), true, true, true,
                AuthenticationService.convert(accountEntity.isAdmin()));
        this.idAccount = accountEntity.getIdAccount();
    }

    public int getIdAccount(){
        return idAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CurrentAccount that = (CurrentAccount) o;

        return idAccount == that.idAccount;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + idAccount;
        return result;
    }
}
