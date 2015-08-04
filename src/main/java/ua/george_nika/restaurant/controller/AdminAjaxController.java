package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.george_nika.restaurant.controller.lightentity.LightAccount;
import ua.george_nika.restaurant.controller.lightentity.LightOrder;
import ua.george_nika.restaurant.controller.util.SortAndRestrictUtil;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.controller.lightentity.AjaxSendInformation;
import ua.george_nika.restaurant.service.AccountService;
import ua.george_nika.restaurant.service.MenuItemService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.util.RestaurantConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
@RequestMapping("/admin")
public class AdminAjaxController {
    private static String LOGGER_NAME = AdminAjaxController.class.getSimpleName();

    @Autowired
    AccountService accountService;

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    OrderService orderService;

    @Autowired
    SortAndRestrictUtil sortAndRestrictUtil;

    @RequestMapping("/addAdminAction")
    @ResponseBody
    public Boolean addAdminAction(@RequestParam(value = "login") String login,
                                  @RequestParam(value = "password") String password) {
        try {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setLogin(login);
            accountEntity.setPassword(password);
            accountEntity.setActive(true);
            accountEntity.setAdmin(true);
            accountService.createNewAccount(accountEntity);
            accountService.setEnableAccountById(accountEntity.getIdAccount());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/changeEnableMenuItemAjax")
    @ResponseBody
    public Boolean changeEnableMenuItemAjax(@RequestParam(value = "idMenuItem") int idMenuItem) {
        try {
            menuItemService.changeEnableMenuItemById(idMenuItem);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/listAllAccountAjax")
    @ResponseBody
    public AjaxSendInformation listAllGroupAjax(HttpServletRequest request, HttpSession session, Model model,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "page", required = false) String pageDirection,
                                                @RequestParam(value = "idLike", required = false) String idLike,
                                                @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            sortAndRestrictUtil.setService(accountService);
            sortAndRestrictUtil.setSessionPageProperty(RestaurantConstant.SESSION_SHOW_LIST_ACCOUNT_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            AjaxSendInformation ajaxResult = new AjaxSendInformation();
            ajaxResult.setPage(showPage);
            ajaxResult.setIdLike(accountService.getIdLikeRestriction());
            ajaxResult.setNameLike(accountService.getNameLikeRestriction());

            List<AccountEntity> tempResultList = accountService.getPartOfAccount(
                    (showPage - 1) * RestaurantConstant.ROW_ON_PAGE, RestaurantConstant.ROW_ON_PAGE);
            List<LightAccount> resultList = new ArrayList<LightAccount>();
            for (AccountEntity tempAccount : tempResultList) {
                LightAccount tempLightAccount = new LightAccount(tempAccount);
                resultList.add(tempLightAccount);
            }
            ajaxResult.setEntityList(resultList);
            return ajaxResult;
        } catch (Exception ex) {
            return new AjaxSendInformation();
        }
    }

    @RequestMapping("/listAllOrderAjax")
    @ResponseBody
    public AjaxSendInformation listAllOrderAjax(HttpServletRequest request, HttpSession session, Model model,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "page", required = false) String pageDirection,
                                                @RequestParam(value = "idLike", required = false) String idLike,
                                                @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            sortAndRestrictUtil.setService(orderService);
            sortAndRestrictUtil.setSessionPageProperty(RestaurantConstant.SESSION_SHOW_LIST_ORDER_PAGE);
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


}
