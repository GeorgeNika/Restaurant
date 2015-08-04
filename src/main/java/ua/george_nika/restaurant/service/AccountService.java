package ua.george_nika.restaurant.service;

import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.restaurant.dao.intface.AbstractDao;
import ua.george_nika.restaurant.dao.intface.AccountDao;
import ua.george_nika.restaurant.dao.intface.EmailVerificationDao;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.EmailVerificationEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.util.RestaurantCheckEmail;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.mail.internet.InternetAddress;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


@Service("accountService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountService extends AbstractSortAndRestrictService {
    private static String LOGGER_NAME = AccountService.class.getSimpleName();

    @Autowired
    private AccountDao accountDao;

    @Override
    AbstractDao getDao() {
        return accountDao;
    }

    @Autowired
    private EmailVerificationDao emailVerificationDao;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JavaMailSender defaultMailSender;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${web.host}")
    private String webHost;

    @Value("${web.context}")
    private String webContext;

    @Transactional(readOnly = false)
    public AccountEntity loginInSystemWithFacebook(User user) {
        try {
            AccountEntity resultAccount;
            if (accountDao.getCountAccountsWithLogin(user.getEmail()) > 0) {
                resultAccount = accountDao.getAccountByLogin(user.getEmail());
            } else {
                resultAccount = new AccountEntity();
                resultAccount.setLogin(user.getEmail());
                resultAccount.setEmail(user.getEmail());
                resultAccount.setFirstName(user.getFirstName());
                resultAccount.setLastName(user.getLastName());
                resultAccount.setMiddleName(user.getMiddleName());
                resultAccount.setAdmin(false);

                String password = UUID.randomUUID().toString();
                resultAccount.setPassword(password.substring(0, 7));
                createNewAccount(resultAccount);
                resultAccount.setActive(true);
                accountDao.update(resultAccount);
            }
            return resultAccount;
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can`t login in system with Facebook: " + user.getEmail());
            throw new UserWrongInputException("Can`t login in system with Facebook: " + user.getEmail());
        }

    }

    @Transactional(readOnly = false)
    public void createNewAccount(AccountEntity newAccount) {
        try {
            checkAllRequirements(newAccount);
            newAccount.setEmailVerified(false);
            newAccount.setActive(false);
            newAccount.setCreated(new Timestamp(new Date().getTime()));

            accountDao.save(newAccount);

            if (newAccount.getEmail() != null) {
                if (!newAccount.getEmail().isEmpty()) {
                    EmailVerificationEntity emailVerificationEntity = new EmailVerificationEntity();
                    emailVerificationEntity.setIdAccount(newAccount.getIdAccount());
                    emailVerificationEntity.setCode(UUID.randomUUID().toString());
                    emailVerificationDao.save(emailVerificationEntity);

                    sendRegistrationMessage(newAccount, emailVerificationEntity.getCode());
                }
            }

            ClientEntity newClient = new ClientEntity();
            newClient.setClientName(getClientNameByAccountName(newAccount.getLogin()));
            newClient.setAccountOwner(newAccount);
            clientService.createNewClient(newClient);

            RestaurantLogger.info(LOGGER_NAME, "Create new account " + newAccount.getLogin());
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't create account: " + newAccount.getLogin()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create account: " + newAccount.getLogin()
                    + " - " + ex.getMessage());
        }
    }

    private String getClientNameByAccountName(String name) {
        return "Client - " + name;
    }


    private void checkAllRequirements(AccountEntity newAccount) {
        if (newAccount.getLogin().isEmpty()) {
            throw new UserWrongInputException("Empty login");
        }
        if (!isUniqueLogin(newAccount, newAccount.getLogin())) {
            throw new UserWrongInputException("Login busy");
        }
        if (newAccount.getPassword().isEmpty()) {
            throw new UserWrongInputException("Empty password");
        }
        if (newAccount.getEmail() != null) {
            if (!newAccount.getEmail().isEmpty()) {
                if (!RestaurantCheckEmail.validate(newAccount.getEmail())) {
                    throw new UserWrongInputException("Incorrect email");
                }
                if (!isUniqueEmail(newAccount, newAccount.getEmail())) {
                    throw new UserWrongInputException("Email already exist");
                }
            }
        }
    }

    public Boolean isFreeLogin(String newLogin) {
        if (accountDao.getCountAccountsWithLogin(newLogin) != 0) {
            return false;
        }
        if (clientService.getCountClientsWithName(getClientNameByAccountName(newLogin)) != 0) {
            return false;
        }
        return true;
    }

    private Boolean isUniqueLogin(AccountEntity accountEntity, String newLogin) {
        int count = accountDao.getCountAccountsWithLogin(newLogin);
        if (count != 0) {
            AccountEntity tempAccount = accountDao.getAccountByLogin(newLogin);
            if (accountEntity.getIdAccount() != tempAccount.getIdAccount()) {
                return false;
            }
        }
        count = clientService.getCountClientsWithName(getClientNameByAccountName(newLogin));
        if (count != 0) {
            ClientEntity tempClient = clientService.getClientByName(getClientNameByAccountName(newLogin));
            if (accountEntity.getIdAccount() != tempClient.getAccountOwner().getIdAccount()) {
                return false;
            }
        }
        return true;
    }

    private Boolean isUniqueEmail(AccountEntity accountEntity, String newEmail) {
        int count = accountDao.getCountAccountsWithEmail(newEmail);
        if (count != 0) {
            AccountEntity tempAccount = accountDao.getAccountByEmail(newEmail);
            if (accountEntity.getIdAccount() != tempAccount.getIdAccount()) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void verifyAccount(String code) {
        try {
            if (emailVerificationDao.getCountRecordWithCode(code) != 1) {
                throw new UserWrongInputException("No such code in system " + code);
            }
            EmailVerificationEntity emailVerificationEntity = emailVerificationDao.getByCode(code);
            AccountEntity account = emailVerificationEntity.getAccount();
            emailVerificationDao.delete(emailVerificationEntity);

            account.setEmailVerified(true);
            account.setActive(true);
            accountDao.update(account);
        } catch (Exception ex) {
            RestaurantLogger.info(LOGGER_NAME, "Can't verify account - " + ex.getMessage());
            throw new UserWrongInputException("Can't verify account - " + ex.getMessage());
        }
    }

    public void backupPassword(String email) {
        try {
            if (!RestaurantCheckEmail.validate(email)) {
                throw new UserWrongInputException("Incorrect email");
            }
            //todo correct if email not in base - write what email sent
            if (accountDao.getCountAccountsWithEmail(email) != 1) {
                throw new UserWrongInputException("Unknown email");
            }
            AccountEntity account = accountDao.getAccountByEmail(email);
            sendRecoveryMessage(account);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't backup password - " + ex.getMessage());
            throw new UserWrongInputException("Can't backup password - " + ex.getMessage());
        }
    }

    public AccountEntity getAccountById(AccountEntity currentAccount, int idEditAccount) {
        try {
            checkAccountPermissionById(currentAccount, idEditAccount);
            AccountEntity resultAccount = accountDao.getAccountById(idEditAccount);
            return resultAccount;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get account by id: " + idEditAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get account by id: " + idEditAccount + " - " + ex.getMessage());
        }
    }

    public AccountEntity getAccountByIdWithoutCheck(int idAccount) {
        try {
            AccountEntity resultAccount = accountDao.getAccountById(idAccount);
            return resultAccount;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get account by id: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get account by id: " + idAccount + " - " + ex.getMessage());
        }
    }

    private void checkAccountPermissionById(AccountEntity currentAccount, int idEditAccount) {
        boolean result = false;
        if (currentAccount.getIdAccount() == idEditAccount) {
            result = true;
        } else if (currentAccount.isAdmin()) {
            result = true;
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO permission");
        }
    }


    @Transactional(readOnly = false)
    public void setEnableAccountById(int id) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(id);
            tempAccount.setActive(true);
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't set enable account by id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set enable account by id: " + id + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void setDisableAccountById(int id) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(id);
            tempAccount.setActive(false);
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't set disable account by id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set disable account by id: " + id + " - " + ex.getMessage());
        }
    }


    @Transactional(readOnly = false)
    public void updateAccount(AccountEntity account) {
        try {
            checkAllRequirements(account);

            accountDao.update(account);
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't update account id: " + account.getIdAccount()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update account id: " + account.getIdAccount()
                    + " - " + ex.getMessage());
        }
    }

    public List<AccountEntity> getPartOfAccount(int offset, int limit) {
        try {
            List<AccountEntity> resultList = accountDao.getFilteredAndSortedList(offset, limit, limitedSortAndRestrict);
            return resultList;
        } catch (Exception ex) {
            RestaurantLogger.error(LOGGER_NAME, "Can't get accounts offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get accounts offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }

    //todo refactor - move send message to util
    private void sendRegistrationMessage(AccountEntity accountEntity, String code) {
        try {
            Resource resource = applicationContext.getResource("resources/email/registration.email");
            String emailText = readFromResource(resource);
            Map<String, String> params = new HashMap<String, String>();
            params.put("user", accountEntity.getFirstName());
            params.put("password", accountEntity.getPassword());
            params.put("login", accountEntity.getLogin());
            params.put("host_context", webHost + "/" + webContext + "/verifyAccountAction");
            params.put("code", code);
            emailText = resolveVariables(emailText, params);

            MimeMessageHelper message = new MimeMessageHelper(defaultMailSender.createMimeMessage(), false);
            message.setSubject(RestaurantConstant.EMAIL_REGISTRATION);
            message.setTo(new InternetAddress(accountEntity.getEmail(), accountEntity.getFirstName()));
            message.setFrom(new InternetAddress(RestaurantConstant.EMAIL_ADDRESS, RestaurantConstant.EMAIL_NAME));
            message.setText(emailText, true);
            MimeMailMessage mimeMailMessage = new MimeMailMessage(message);
            defaultMailSender.send(mimeMailMessage.getMimeMessage());
            RestaurantLogger.info(LOGGER_NAME, "Sent registration email: " + accountEntity.getEmail());
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't sent registration email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't sent registration email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
        }
    }

    private void sendRecoveryMessage(AccountEntity accountEntity) {
        try {
            Resource resource = applicationContext.getResource("resources/email/recovery.email");
            String emailText = readFromResource(resource);
            Map<String, String> params = new HashMap<String, String>();
            params.put("user", accountEntity.getFirstName());
            params.put("password", accountEntity.getPassword());
            params.put("login", accountEntity.getLogin());
            emailText = resolveVariables(emailText, params);

            MimeMessageHelper message = new MimeMessageHelper(defaultMailSender.createMimeMessage(), false);
            message.setSubject(RestaurantConstant.EMAIL_RECOVERY);
            message.setTo(new InternetAddress(accountEntity.getEmail(), accountEntity.getFirstName()));
            message.setFrom(new InternetAddress(RestaurantConstant.EMAIL_ADDRESS, RestaurantConstant.EMAIL_NAME));
            message.setText(emailText, true);
            MimeMailMessage mimeMailMessage = new MimeMailMessage(message);
            defaultMailSender.send(mimeMailMessage.getMimeMessage());
            RestaurantLogger.info(LOGGER_NAME, "Sent recovery email: " + accountEntity.getEmail());
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't sent recovery email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't sent recovery email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
        }
    }

    private String readFromResource(Resource resource) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = resource.getFile();
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    stringBuilder.append(s);
                    stringBuilder.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    private String resolveVariables(String text, Map<String, String> params) {
        String result = text;
        for (Map.Entry<String, String> entity : params.entrySet()) {
            String var = "${" + entity.getKey() + "}";
            String value = entity.getValue() == null ? "" : entity.getValue();
            result = result.replace(var, value);
        }
        return result;
    }
}
