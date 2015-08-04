package ua.george_nika.restaurant.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.form.EditAccountForm;
import ua.george_nika.restaurant.service.AccountService;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@RequestMapping("/guest")
public class GuestController {
    private static String LOGGER_NAME = GuestController.class.getSimpleName();

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, HttpSession session, Model model) {
        AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
        model.addAttribute("idAccount", accountEntity.getIdAccount());
        return "guest/mainPage";
    }

    @RequestMapping("/editAccountPage")
    public String editAccountPage(HttpServletRequest request,
                                  HttpSession session,
                                  Model model) {
        try {
            AccountEntity currentAccount = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            AccountEntity editAccount = accountService.getAccountByIdWithoutCheck(currentAccount.getIdAccount());
            request.setAttribute("editAccount", editAccount);
            if (!model.containsAttribute("editAccountForm")) {
                model.addAttribute("editAccountForm", new EditAccountForm(editAccount));
            }
            return "guest/editAccount";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show account - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show account " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }


    // *****************
    //   Action section
    // *****************


    @RequestMapping("/editAccountAction")
    public String editAccountAction(HttpServletRequest request,
                                    HttpSession session,
                                    Model model,
                                    @ModelAttribute("editAccountForm") EditAccountForm editAccountForm) {
        try {
            AccountEntity currentAccount =
                    (AccountEntity) request.getSession().getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            AccountEntity tempAccountEntity = accountService.getAccountById(currentAccount, currentAccount.getIdAccount());
            editAccountForm.updateAccount(tempAccountEntity);
            accountService.updateAccount(tempAccountEntity);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Update successful.");
            return mainPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("editAccountForm", editAccountForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return "guest/editAccount";
        }
    }


}

