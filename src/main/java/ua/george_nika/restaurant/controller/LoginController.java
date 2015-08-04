package ua.george_nika.restaurant.controller;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;
import ua.george_nika.restaurant.form.BackupPasswordForm;
import ua.george_nika.restaurant.form.EditAccountForm;
import ua.george_nika.restaurant.form.LoginForm;
import ua.george_nika.restaurant.form.RegisterNewAccountForm;
import ua.george_nika.restaurant.security.CurrentAccount;
import ua.george_nika.restaurant.security.SecurityUtils;
import ua.george_nika.restaurant.service.AccountService;
import ua.george_nika.restaurant.util.HandleRecaptcha;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
public class LoginController {

    private static String LOGGER_NAME = LoginController.class.getSimpleName();
    private static Map<Boolean, String> mainRolePage = new HashMap<Boolean, String>();

    static {
        mainRolePage.put(true, "/admin/mainPage");
        mainRolePage.put(false, "/guest/mainPage");
    }

    @Autowired
    private AccountService accountService;

    @Value("${fb.clientId}")
    private String facebookClientId;

    @Value("${fb.secretKey}")
    private String facebookSecretKey;

    @Value("${web.host}")
    private String applicationHost;

    @Value("${web.context}")
    private String applicationContext;

    private String fbReferrer;
    private String fbRedirectUri;

    //***************
    //  Page section
    //**************

    @RequestMapping("/loginPage")
    public String loginPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "account/loginPage";
    }

    @RequestMapping("/backupPasswordPage")
    public String backupPasswordPage(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("loginForm") LoginForm loginForm) {
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        model.addAttribute("backupPasswordForm", new BackupPasswordForm());
        model.addAttribute("loginForm", loginForm);
        return "account/backupPassword";
    }


    @RequestMapping("/registerNewAccountPage")
    public String registerNewAccountPage(HttpServletRequest request,
                                         HttpSession session,
                                         Model model,
                                         @ModelAttribute("loginForm") LoginForm loginForm) {
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        model.addAttribute("registerNewAccountForm", new RegisterNewAccountForm());
        model.addAttribute("loginForm", loginForm);
        return "account/registerNewAccount";
    }

    @RequestMapping("/taskPage")
    public String taskPage(HttpServletRequest request,
                           HttpSession session,
                           Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "account/taskPage";
    }

    // todo create error page
//    @RequestMapping("/error")
//    public String error(HttpServletRequest request,
//                        HttpSession session,
//                        Model model,
//                        @ModelAttribute("loginForm") LoginForm loginForm,
//                        @RequestParam("url") String url) {
//        model.addAttribute("loginForm", loginForm);
//        request.setAttribute(RestaurantConstant.REQUEST_ERROR, url);
//        return "account/error";
//    }

    //***************
    //  Action section
    //**************

    @RequestMapping("/welcomeAction")
    public String welcomeAction(HttpServletRequest request,
                                HttpSession session,
                                Model model) {
        try {
            CurrentAccount currentAccount = SecurityUtils.getCurrentAccount();
            AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(currentAccount.getIdAccount());

            setSessionProperty(session, accountEntity);
            return "redirect:" + mainRolePage.get(accountEntity.isAdmin());
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't enter - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't enter: - " + ex.getMessage());
            return loginPage(request, session, model);
        }
    }

    @RequestMapping("/logoutAction")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "redirect:/welcomeAction";
    }

    private void setSessionProperty(HttpSession session, AccountEntity accountEntity) {
        session.setAttribute(RestaurantConstant.SESSION_ACCOUNT, accountEntity);
    }

    @RequestMapping("/backupPasswordAction")
    public String backupPasswordAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @ModelAttribute("backupPasswordForm") BackupPasswordForm backupPasswordForm) {
        try {
            accountService.backupPassword(backupPasswordForm.getEmail());
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Email with information sent to you address");
            return loginPage(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't sent email - " + ex.getMessage());
            model.addAttribute("backupPasswordForm", backupPasswordForm);
            model.addAttribute("loginForm", new LoginForm());
            return "account/backupPassword";
        }
    }

    @RequestMapping("/registerNewAccountAction")
    public String registerNewAccountAction(HttpServletRequest request,
                                           HttpSession session,
                                           Model model,
                                           @ModelAttribute("RegisterNewAccountForm")
                                           RegisterNewAccountForm registerNewAccountForm) {
        try {
            AccountEntity accountEntity = new AccountEntity();
            registerNewAccountForm.updateAccount(accountEntity);

            accountService.createNewAccount(accountEntity);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Account successfully registered. Email sent.");
            return loginPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerNewAccountForm", registerNewAccountForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't register new account - " + ex.getMessage());
            return "account/registerNewAccount";
        }
    }

    @RequestMapping("/compareLoginAction")
    @ResponseBody
    public Boolean compareLogin (@RequestParam(value = "login", required = false) String login){
        return accountService.isFreeLogin(login);
    }

    @RequestMapping("/registerNewAccountWithoutEmailAction")
    public String registerNewAccountWithoutEmailAction(HttpServletRequest request,
                                                       HttpSession session,
                                                       Model model,
                                                       @ModelAttribute("RegisterNewAccountForm")
                                                       RegisterNewAccountForm registerNewAccountForm) {
        try {
            if (!HandleRecaptcha.isGoodCapcha(request, registerNewAccountForm.getReCaptchaData())) {
                throw new RuntimeException("Captcha not valid");
            }
            AccountEntity accountEntity = new AccountEntity();
            registerNewAccountForm.updateAccount(accountEntity);
            accountService.createNewAccount(accountEntity);
            accountService.setEnableAccountById(accountEntity.getIdAccount());
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Account successfully registered.");
            return loginPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerNewAccountForm", registerNewAccountForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't register new account - " + ex.getMessage());
            return "account/registerNewAccount";
        }
    }

    @RequestMapping("/verifyAccountAction/{code}")
    public String verifyAccount(HttpServletRequest request,
                                HttpSession session,
                                Model model,
                                @PathVariable("code") String code) {
        try {
            accountService.verifyAccount(code);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Account successfully registered");
        } catch (RuntimeException ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Account NOT registered. Wrong data or time");
        }
        return loginPage(request, session, model);
    }

    @RequestMapping("/fbLoginAction")
    public String facebookLoginAction(@ModelAttribute("loginForm") LoginForm loginForm) {
        fbRedirectUri = applicationHost + "/" + applicationContext + "/loginFromFbAction";
        fbReferrer = "https://graph.facebook.com/oauth/authorize?client_id=" + facebookClientId +
                "&redirect_uri=" + fbRedirectUri + "&scope=email";
        return "redirect:" + fbReferrer;
    }

    @RequestMapping("/loginFromFbAction")
    public String loginFromFacebookAction(HttpServletRequest request,
                                          HttpSession session,
                                          Model model,
                                          @RequestParam("code") String code) throws IOException {
        try {
            if (code == null) {
                throw new UserWrongInputException("Wrong facebook information");
            }
            User user = getFacebookUser(code);
            if (user.getEmail() == null) {
                throw new UserWrongInputException("Wrong facebook email information");
            }
            AccountEntity accountEntity = accountService.loginInSystemWithFacebook(user);
            SecurityUtils.authenticate(accountEntity);
            setSessionProperty(session, accountEntity);
            return "redirect:" + mainRolePage.get(accountEntity.isAdmin());
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can`t login from Facebook - " + ex.getMessage());
            return loginPage(request, session, model);
        }
    }

    protected User getFacebookUser(String code) throws IOException {
        String url = "https://graph.facebook.com/oauth/access_token?client_id=" + facebookClientId
                + "&redirect_uri=" + fbRedirectUri + "&client_secret=" + facebookSecretKey + "&code=" + code;
        URLConnection connection = new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//Z");
            String out = scanner.next();
            String[] auth1 = out.split("=");
            String[] auth2 = auth1[1].split("&");
            FacebookClient facebookClient = new DefaultFacebookClient(auth2[0], Version.VERSION_2_4);
            User user = facebookClient.fetchObject("me", User.class,
                    Parameter.with("fields", "id, name, first_name, last_name, middle_name, email"));
            return user;
        } catch (Exception ex) {
            RestaurantLogger.warn(LOGGER_NAME, "Can't get facebook user - " + ex.getMessage());
            throw new UserWrongInputException("Can't get facebook user");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}


