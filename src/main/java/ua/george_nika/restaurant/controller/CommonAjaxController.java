package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.george_nika.restaurant.controller.lightentity.*;
import ua.george_nika.restaurant.controller.util.SortAndRestrictUtil;
import ua.george_nika.restaurant.entity.*;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.MenuGroupService;
import ua.george_nika.restaurant.service.MenuItemService;
import ua.george_nika.restaurant.service.OrderService;
import ua.george_nika.restaurant.util.RestaurantConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
@RequestMapping("/ajax")
public class CommonAjaxController {

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    SortAndRestrictUtil sortAndRestrictUtil;

    @RequestMapping("/listAllGroupAjax")
    @ResponseBody
    public AjaxSendInformation listAllGroupAjax(HttpServletRequest request, HttpSession session, Model model,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "page", required = false) String pageDirection,
                                                @RequestParam(value = "idLike", required = false) String idLike,
                                                @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            sortAndRestrictUtil.setService(clientService);
            sortAndRestrictUtil.setSessionPageProperty(RestaurantConstant.SESSION_SHOW_LIST_GROUP_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            AjaxSendInformation ajaxResult = new AjaxSendInformation();
            ajaxResult.setPage(showPage);
            ajaxResult.setIdLike(clientService.getIdLikeRestriction());
            ajaxResult.setNameLike(clientService.getNameLikeRestriction());

            List<ClientEntity> tempResultList = clientService.getPartOfClient(
                    (showPage - 1) * RestaurantConstant.ROW_ON_PAGE, RestaurantConstant.ROW_ON_PAGE);
            List<LightClient> resultList = new ArrayList<LightClient>();
            for (ClientEntity tempClient : tempResultList) {
                LightClient tempLightClient = new LightClient(tempClient);
                resultList.add(tempLightClient);
            }
            ajaxResult.setEntityList(resultList);
            return ajaxResult;
        } catch (Exception ex) {
            return new AjaxSendInformation();
        }
    }

    @RequestMapping("/listMenuItemAjax")
    @ResponseBody
    public AjaxSendInformation listMenuItemAjax(HttpServletRequest request, HttpSession session, Model model,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "page", required = false) String pageDirection,
                                                @RequestParam(value = "idLike", required = false) String idLike,
                                                @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            sortAndRestrictUtil.setService(menuItemService);
            sortAndRestrictUtil.setSessionPageProperty(RestaurantConstant.SESSION_SHOW_LIST_MENU_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            AjaxSendInformation ajaxResult = new AjaxSendInformation();
            ajaxResult.setPage(showPage);
            ajaxResult.setIdLike(menuItemService.getIdLikeRestriction());
            ajaxResult.setNameLike(menuItemService.getNameLikeRestriction());

            List<MenuItemEntity> tempResultList = menuItemService.getPartOfMenu(
                    (showPage - 1) * RestaurantConstant.ROW_ON_PAGE, RestaurantConstant.ROW_ON_PAGE);
            List<LightMenuItem> resultList = new ArrayList<LightMenuItem>();
            for (MenuItemEntity tempMenuItem : tempResultList) {
                LightMenuItem tempLightMenuItem = new LightMenuItem(tempMenuItem);
                resultList.add(tempLightMenuItem);
            }
            ajaxResult.setEntityList(resultList);
            return ajaxResult;
        } catch (Exception ex) {
            return new AjaxSendInformation();
        }
    }

    @RequestMapping("/changeMenuGroupAjax")
    @ResponseBody
    public Boolean changeMenuGroupAjax(@RequestParam(value = "idMenuGroup") int idMenuGroup) {
        try {
            menuItemService.addMenuGroupEqualRestriction(menuGroupService.getMenuGroupById(idMenuGroup));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/listMembersOfGroupAjax")
    @ResponseBody
    public List<LightAccount> listMembersOfGroupAjax(HttpServletRequest request, HttpSession session, Model model,
                                                     @RequestParam(value = "idGroup", required = false) int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByMemberById(accountEntity, idGroup);

            List<AccountEntity> tempMemberList = group.getMemberList();
            List<LightAccount> resultList = new ArrayList<LightAccount>();
            for (AccountEntity tempAccount : tempMemberList) {
                LightAccount tempLightAccount = new LightAccount(tempAccount);
                resultList.add(tempLightAccount);
            }
            return resultList;
        } catch (Exception ex) {
            return new ArrayList<LightAccount>();
        }
    }

    @RequestMapping("/getMenuGroupItemListAjax")
    @ResponseBody
    public AjaxMenuItem getMenuGroupItemListAjax(@RequestParam(value = "idMenuGroup") int idMenuGroup) {
        try {
            MenuGroupEntity menuGroupEntity = menuGroupService.getMenuGroupById(idMenuGroup);
            List<MenuItemEntity> tempResultList = menuGroupEntity.getMenuItemList();
            List<LightMenuItem> resultList = new ArrayList<LightMenuItem>();
            for (MenuItemEntity tempMenuItem : tempResultList) {
                if (tempMenuItem.isActive()) {
                    // get only active menuItem
                    LightMenuItem tempLightMenuItem = new LightMenuItem(tempMenuItem);
                    resultList.add(tempLightMenuItem);
                }
            }
            AjaxMenuItem resultAjaxMenuItem = new AjaxMenuItem();
            resultAjaxMenuItem.setMenuItemName(menuGroupEntity.getMenuGroupName());
            resultAjaxMenuItem.setEntityList(resultList);
            return resultAjaxMenuItem;
        } catch (Exception ex) {
            return new AjaxMenuItem();
        }
    }

    @RequestMapping("/getOrderAjax")
    @ResponseBody
    public AjaxOrder getOrderAjax(HttpServletRequest request, HttpSession session, Model model,
                                  @RequestParam(value = "idOrder", required = false) int idOrder) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            OrderEntity orderEntity = orderService.getOrderById(accountEntity, idOrder);
            ClientEntity group = clientService.getClientByMemberById(accountEntity, orderEntity.getClient().getIdClient());
            AjaxOrder ajaxOrder = new AjaxOrder(orderEntity, accountEntity.getIdAccount());
            return ajaxOrder;
        } catch (Exception ex) {
            return new AjaxOrder();
        }
    }
}
