package ua.george_nika.restaurant.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    @Autowired
    @Qualifier("accountAuthenticationService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    @Autowired
    @Qualifier("pwdEncoder")
    public void setPasswordEncoder(Object passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);
        CurrentAccount currentAccount = (CurrentAccount) userDetails;

    }
}
