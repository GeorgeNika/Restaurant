package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.george_nika.restaurant.controller.lightentity.LightOrder;
import ua.george_nika.restaurant.controller.lightentity.LightRequestFMS;
import ua.george_nika.restaurant.controller.util.SortAndRestrictUtil;
import ua.george_nika.restaurant.entity.*;
import ua.george_nika.restaurant.controller.lightentity.AjaxSendInformation;
import ua.george_nika.restaurant.service.*;
import ua.george_nika.restaurant.util.RestaurantConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
@RequestMapping("/guest")
public class GuestAjaxController {

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    RequestFMSService requestFMSService;

    @Autowired
    SortAndRestrictUtil sortAndRestrictUtil;

    @RequestMapping("/listRequestFMSAjax")
    @ResponseBody
    public List<LightRequestFMS>
    listRequestFMSAjax(HttpServletRequest request, HttpSession session, Model model,
                       @RequestParam(value = "idGroup", required = false) int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByOwnerById(accountEntity, idGroup);
            if (group.getAccountOwner().getIdAccount() == accountEntity.getIdAccount()) {
                List<RequestFMSEntity> tempRequestFMSList = group.getRequestFMSList();
                List<LightRequestFMS> resultList = new ArrayList<LightRequestFMS>();
                for (RequestFMSEntity tempRequestFMS : tempRequestFMSList) {
                    LightRequestFMS tempLightRequestFMS = new LightRequestFMS(tempRequestFMS);
                    resultList.add(tempLightRequestFMS);
                }
                return resultList;
            } else {
                return new ArrayList<LightRequestFMS>();
            }
        } catch (Exception ex) {
            return new ArrayList<LightRequestFMS>();
        }
    }

    @RequestMapping("/rejectRequestFMSAjax")
    @ResponseBody
    public Boolean rejectRequestFMSAjax(HttpServletRequest request, HttpSession session, Model model,
                                        @RequestParam(value = "idRequestFMS", required = false) int idRequestFMS) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            RequestFMSEntity requestFMSEntity = requestFMSService.getRequestFMSById(idRequestFMS);
            ClientEntity group = requestFMSEntity.getClientEntity();
            if (group.getAccountOwner().getIdAccount() == accountEntity.getIdAccount()) {
                // i owner this group
                requestFMSService.deleteRequestFMS(requestFMSEntity);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/acceptRequestFMSAjax")
    @ResponseBody
    public Boolean acceptRequestFMSAjax(HttpServletRequest request, HttpSession session, Model model,
                                        @RequestParam(value = "idRequestFMS", required = false) int idRequestFMS) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            RequestFMSEntity requestFMSEntity = requestFMSService.getRequestFMSById(idRequestFMS);
            ClientEntity group = requestFMSEntity.getClientEntity();
            if (group.getAccountOwner().getIdAccount() == accountEntity.getIdAccount()) {
                // i owner this group
                requestFMSService.acceptRequestFMS(requestFMSEntity);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/deleteMemberAjax")
    @ResponseBody
    public Boolean deleteMemberAjax(HttpServletRequest request, HttpSession session, Model model,
                                    @RequestParam(value = "idAccount", required = false) int idAccount,
                                    @RequestParam(value = "idGroup", required = false) int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByOwnerById(accountEntity, idGroup);
            AccountEntity deleteAccount = accountService.getAccountByIdWithoutCheck(idAccount);
            clientService.deleteMemberFromGroup(deleteAccount, group);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/listOrderAjax")
    @ResponseBody
    public AjaxSendInformation listOrderAjax(HttpServletRequest request, HttpSession session, Model model,
                                             @RequestParam(value = "sort", required = false) String sort,
                                             @RequestParam(value = "page", required = false) String pageDirection,
                                             @RequestParam(value = "idLike", required = false) String idLike,
                                             @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            sortAndRestrictUtil.setService(orderService);
            sortAndRestrictUtil.setSessionPageProperty(RestaurantConstant.SESSION_SHOW_LIST_MY_ORDER_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            AjaxSendInformation ajaxResult = new AjaxSendInformation();
            ajaxResult.setPage(showPage);
            ajaxResult.setIdLike(orderService.getIdLikeRestriction());
            ajaxResult.setNameLike(orderService.getNameLikeRestriction());

            List<OrderEntity> tempResultList = orderService.getPartOfOrder(
                    (showPage - 1) * RestaurantConstant.ROW_ON_PAGE, RestaurantConstant.ROW_ON_PAGE);
            List<LightOrder> resultList = new ArrayList<LightOrder>();
            for (OrderEntity tempOrder : tempResultList) {
                LightOrder tempLightOrder = new LightOrder(tempOrder);
                resultList.add(tempLightOrder);
            }
            ajaxResult.setEntityList(resultList);
            return ajaxResult;
        } catch (Exception ex) {
            return new AjaxSendInformation();
        }
    }

    @RequestMapping("/addMenuItemToOrderAjax")
    @ResponseBody
    public Boolean addMenuItemToOrderAjax(HttpServletRequest request, HttpSession session, Model model,
                                          @RequestParam(value = "idOrder", required = false) int idOrder,
                                          @RequestParam(value = "idMenuItem", required = false) int idMenuItem) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            MenuItemEntity menuItemEntity = menuItemService.getMenuItemById(idMenuItem);
            orderItemService.addNewOrderItem(accountEntity, menuItemEntity, orderEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/orderItemChangeQuantityAjax")
    @ResponseBody
    public Boolean orderItemChangeQuantityAjax(HttpServletRequest request, HttpSession session, Model model,
                                               @RequestParam(value = "idOrder", required = false) int idOrder,
                                               @RequestParam(value = "idMenuItem", required = false) int idMenuItem,
                                               @RequestParam(value = "quantity", required = false) int quantity) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            MenuItemEntity menuItemEntity = menuItemService.getMenuItemById(idMenuItem);
            if (quantity == 0) {
                orderItemService.deleteOrderItem(accountEntity, menuItemEntity, orderEntity);
            } else {
                orderItemService.changeOrderItemQuantity(accountEntity, menuItemEntity, orderEntity, quantity);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/orderTimeChangeAjax")
    @ResponseBody
    public Boolean orderTimeChangeAjax(HttpServletRequest request, HttpSession session, Model model,
                                       @RequestParam(value = "idOrder", required = false) int idOrder,
                                       @RequestParam(value = "orderTime", required = false) String orderTime) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            orderService.changeOrderTime(orderEntity, orderTime);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
