package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.MenuItemEntity;
import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.form.MenuItemForm;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.MenuGroupService;
import ua.george_nika.restaurant.service.MenuItemService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@RequestMapping("/admin")
public class AdminController {
    private static String LOGGER_NAME = AdminController.class.getSimpleName();

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    MenuGroupService menuGroupService;

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, HttpSession session, Model model) {
        return "admin/mainPage";
    }

    @RequestMapping("/menuPage")
    public String menuPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            if (menuItemService.getMenuGroupEqualRestriction()!= null){
                model.addAttribute("menuGroupSelected",menuItemService.getMenuGroupEqualRestriction().getIdMenuGroup());
            }else {
                menuItemService.clearAllEqualRestriction();
                menuItemService.addMenuGroupEqualRestriction(menuGroupService.getMenuGroupById(1));
                model.addAttribute("menuGroupSelected",0);
            }
            return "admin/menuPage";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show menu page - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show menu page - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/menuItemPage/{idMenuItem}")
    public String menuItemPage(HttpServletRequest request, HttpSession session, Model model,
                               @PathVariable("idMenuItem") int idMenuItem) {
        try {
            MenuItemEntity menuItemEntity = menuItemService.getMenuItemById(idMenuItem);
            request.setAttribute("editMenuItem", menuItemEntity);
            if (!model.containsAttribute("editMenuItemForm")) {
                model.addAttribute("editMenuItemForm", new MenuItemForm(menuItemEntity));
            }
            return "admin/editMenuItem";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show menu item - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show menu item - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/allGroupPage")
    public String allGroupPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            clientService.clearAllEqualRestriction();
            clientService.addGroupManualActiveEqualRestriction(true);
            return "admin/allGroup";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show all group page - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show all group page - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/groupPage/{idGroup}")
    public String groupPage(HttpServletRequest request, HttpSession session, Model model,
                            @PathVariable("idGroup") int idGroup) {
        try {
            ClientEntity clientEntity = clientService.getClientByIdWithoutCheck(idGroup);

            model.addAttribute("idGroup", clientEntity.getIdClient());
            model.addAttribute("groupName", clientEntity.getClientName());
            model.addAttribute("ownerLogin", clientEntity.getAccountOwner().getLogin());

            return "admin/groupPage";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show group page: " + idGroup
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show group page: " + idGroup
                    + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/allAccountPage")
    public String allAccountPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            return "admin/allAccount";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show all account page - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show all group page - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/allOrderPage")
    public String allOrderPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            orderService.clearAllEqualRestriction();
            return "admin/allOrder";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show all account page - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show all group page - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/orderPage/{idOrder}")
    public String orderPage(HttpServletRequest request, HttpSession session, Model model,
                                 @PathVariable("idOrder") int idOrder) {
        try {
            request.setAttribute("idOrder", idOrder);
            return "admin/orderPage";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show order id: " + idOrder
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show group id: " + idOrder
                    + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    // *****************
    //   Action section
    // *****************


    @RequestMapping("/editMenuItemAction/{idMenuItem}")
    public String editMenuItemAction(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @PathVariable("idMenuItem") int idMenuItem,
                                     @ModelAttribute("editMenuItemForm") MenuItemForm editMenuItemForm) {
        try {
            MenuItemEntity menuItemEntity = menuItemService.getMenuItemById(idMenuItem);
            editMenuItemForm.updateMenuItem(menuItemEntity);
            menuItemService.updateMenuItem(menuItemEntity);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Update successful.");
            return menuPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("editMenuItemForm", editMenuItemForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't update menu item - " + ex.getMessage());
            return menuItemPage(request,session,model,idMenuItem);
        }
    }

    @RequestMapping("/addMenuItemAction")
    public String addMenuItemAction(HttpServletRequest request, HttpSession session, Model model) {
        try {
            MenuItemEntity newMenuItem = new MenuItemEntity();
            menuItemService.createNewMenuItem(newMenuItem);
            return menuItemPage(request, session, model, newMenuItem.getIdMenuItem());
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't create menu item - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't create menu item" + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

}
