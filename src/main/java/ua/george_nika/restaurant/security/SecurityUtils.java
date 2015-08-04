package ua.george_nika.restaurant.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.george_nika.restaurant.entity.AccountEntity;

public class SecurityUtils {

    public static long getCurrentIdAccount(){
        CurrentAccount currentAccount = getCurrentAccount();
        return currentAccount != null ? currentAccount.getIdAccount() : -1;
    }

    public static CurrentAccount getCurrentAccount (){
        Object principal;
        try {
            principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
        if (principal instanceof CurrentAccount){
            return (CurrentAccount)principal;
        }
        return null;
    }

    public static void authenticate (AccountEntity accountEntity){
        CurrentAccount currentAccount = new CurrentAccount(accountEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                currentAccount,
                accountEntity.getPassword(),
                AuthenticationService.convert(accountEntity.isAdmin()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
