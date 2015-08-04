package ua.george_nika.restaurant.security;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.george_nika.restaurant.dao.intface.AccountDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.util.RestaurantLogger;

import java.util.*;

/**
 * Created by George on 29.06.2015.
 */
@Service("accountAuthenticationService")
public class AuthenticationService implements UserDetailsService {

    private static String LOGGER_NAME = AuthenticationService.class.getSimpleName();

    private static final Map<Boolean, String> ROLES = new HashMap<Boolean, String>();

    static {
        ROLES.put(true, "ADMIN");
        ROLES.put(false, "GUEST");
    }

    static Collection<? extends GrantedAuthority> convert(boolean isAdmin) {
        Collection<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
        result.add(new SimpleGrantedAuthority(ROLES.get(isAdmin)));
        return result;
    }

    @Autowired
    private AccountDao accountDao;

    public UserDetails loadUserByUsername(String userName) {
        try {
            AccountEntity accountEntity = accountDao.getAccountByLogin(userName);
            return new CurrentAccount(accountEntity);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Account not found by id=" + userName);
            throw new UsernameNotFoundException("Account not found by id=" + userName);
        }
    }
}
