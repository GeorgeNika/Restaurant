package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.service.RequestFMSService;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@RequestMapping("/guest")
public class GuestOrderController {
    private static String LOGGER_NAME = GuestOrderController.class.getSimpleName();

    @Autowired
    GuestController guestController;

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    RequestFMSService requestFMSService;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/personOrderPage/{idOrder}")
    public String personOrderPage(HttpServletRequest request, HttpSession session, Model model,
                                  @PathVariable("idOrder") int idOrder) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            request.setAttribute("editPersonOrder", orderEntity);
            request.setAttribute("done", orderEntity.isDone());
            return "guest/personOrder";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show person order id: "
                    + idOrder + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show person order id: "
                    + idOrder + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/groupOrderPage/{idOrder}")
    public String groupOrderPage(HttpServletRequest request, HttpSession session, Model model,
                                 @PathVariable("idOrder") int idOrder) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            request.setAttribute("editGroupOrder", orderEntity);
            request.setAttribute("done", orderEntity.isDone());
            return "guest/groupOrder";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show group order id: "
                    + idOrder + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show group group id: "
                    + idOrder + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/listMyOrderPage")
    public String listMyOrderPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            AccountEntity currentAccount = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = clientService.getMyPersonClientByIdAccount(currentAccount.getIdAccount());
            orderService.clearAllEqualRestriction();
            orderService.addOrderClientEqualRestriction(clientEntity);
            request.setAttribute("clientName", clientEntity.getClientName());
            request.setAttribute("idGroup", 0);
            return "guest/listClientOrder";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show my order page:"
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show my order page:"
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/listGroupOrderPage/{idGroup}")
    public String listGroupOrderPage(HttpServletRequest request, HttpSession session, Model model,
                                     @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = clientService.getClientByMemberById(accountEntity, idGroup);
            orderService.clearAllEqualRestriction();
            orderService.addOrderClientEqualRestriction(clientEntity);
            request.setAttribute("clientName", clientEntity.getClientName());
            request.setAttribute("idGroup", clientEntity.getIdClient());
            return "guest/listClientOrder";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show group order page id: " + idGroup
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show group order page id: " + idGroup
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    // *****************
    //   Action section
    // *****************

    @RequestMapping("/addNewPersonOrderAction")
    public String addNewPersonOrderAction(HttpServletRequest request, HttpSession session, Model model) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = new OrderEntity();
            orderService.createNewOrder(clientService.getMyPersonClientByIdAccount(accountEntity.getIdAccount()),
                    orderEntity);
            return personOrderPage(request, session, model, orderEntity.getIdOrder());
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't create person order - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't create person order - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/addNewGroupOrderAction/{idGroup}")
    public String addNewGroupOrderAction(HttpServletRequest request, HttpSession session, Model model,
                                         @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = clientService.getClientByMemberById(accountEntity, idGroup);
            OrderEntity orderEntity = new OrderEntity();
            orderService.createNewOrder(clientEntity, orderEntity);
            return groupOrderPage(request, session, model, orderEntity.getIdOrder());
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't create group order - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't create group order - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/sendOrderAction/{idOrder}")
    public String sendOrderAction(HttpServletRequest request, HttpSession session, Model model,
                                  @PathVariable("idOrder") int idOrder) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            orderService.sendOrderToDone(orderEntity);
            if (orderEntity.getClient().isManualCreatedGroup()) {
                return listGroupOrderPage(request, session, model, orderEntity.getClient().getIdClient());
            } else {
                return listMyOrderPage(request, session, model);
            }
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't send order id: "
                    + idOrder + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't send order id: "
                    + idOrder + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }
}
