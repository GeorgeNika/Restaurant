package ua.george_nika.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.entity.ClientEntity;
import ua.george_nika.restaurant.entity.RequestFMSEntity;
import ua.george_nika.restaurant.form.GroupForm;
import ua.george_nika.restaurant.form.RequestFMSForm;
import ua.george_nika.restaurant.service.ClientService;
import ua.george_nika.restaurant.service.RequestFMSService;
import ua.george_nika.restaurant.util.RestaurantConstant;
import ua.george_nika.restaurant.util.RestaurantLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@RequestMapping("/guest")
public class GuestGroupController {
    private static String LOGGER_NAME = GuestGroupController.class.getSimpleName();

    @Autowired
    GuestController guestController;

    @Autowired
    ClientService clientService;

    @Autowired
    RequestFMSService requestFMSService;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/listAllGroupPage")
    public String listAllGroupPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            clientService.clearAllEqualRestriction();
            clientService.addGroupManualActiveEqualRestriction(true);
            return "guest/listAllGroup";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show all group page:"
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show all group page:"
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/listIOwnerOfGroupPage")
    public String listIOwnerOfGroupPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            List<ClientEntity> groupList = clientService.getIOwnerOfGroupListByIdAccount(accountEntity.getIdAccount());

            model.addAttribute("groupList", groupList);
            return "guest/listIOwnerOfGroup";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show owner group page:"
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show owner group page:"
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/listIMemberOfGroupPage")
    public String listIMemberOfGroupPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            model.addAttribute("groupList", clientService.getIMemberOfGroupListByIdAccount(accountEntity.getIdAccount()));
            return "guest/listIMemberOfGroup";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show member group page:"
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show member group page:"
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    @RequestMapping("/createNewGroupPage")
    public String createNewGroupPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("groupForm", new GroupForm());
        return "guest/createGroup";
    }

    @RequestMapping("/editGroupPage/{idGroup}")
    public String editGroupPage(HttpServletRequest request, HttpSession session, Model model,
                                @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = clientService.getClientByOwnerById(accountEntity, idGroup);
            model.addAttribute("groupForm", new GroupForm(clientEntity));
            model.addAttribute("idGroup", clientEntity.getIdClient());
            return "guest/editGroup";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show edit group page:" + idGroup
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show edit group page:" + idGroup
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }


    @RequestMapping("/groupPage/{idGroup}")
    public String groupPage(HttpServletRequest request, HttpSession session, Model model,
                            @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = clientService.getClientByIdWithoutCheck(idGroup);

            Boolean iOwner = false;
            Boolean iMember = false;
            Boolean iSentRequestFMS = false;
            RequestFMSForm requestFMSForm = new RequestFMSForm();

            if (clientEntity.getAccountOwner().getIdAccount() == accountEntity.getIdAccount()) {
                // i owner this group
                iOwner = true;
            } else if (clientEntity.getMemberList().contains(accountEntity)) {
                // i member of this group
                iMember = true;
            } else {
                try {
                    requestFMSForm = new RequestFMSForm(requestFMSService.getRequestFMS(accountEntity.getIdAccount(),
                            clientEntity.getIdClient()));
                    iSentRequestFMS = true;
                    // i sent request for membership
                } catch (Exception ex) {
                    // i don`t sent request for membership
                }
            }

            model.addAttribute("idGroup", clientEntity.getIdClient());
            model.addAttribute("groupName", clientEntity.getClientName());
            model.addAttribute("ownerLogin", clientEntity.getAccountOwner().getLogin());
            model.addAttribute("iOwner", iOwner);
            model.addAttribute("iMember", iMember);
            model.addAttribute("iSentRequestFMS", iSentRequestFMS);
            model.addAttribute("requestFMSForm", requestFMSForm);

            return "guest/groupPage";
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't show group page:" + idGroup
                    + " - " + ex.getMessage());
            RestaurantLogger.error(LOGGER_NAME, "Can't show group page:" + idGroup
                    + " - " + ex.getMessage());
            return guestController.mainPage(request, session, model);
        }
    }

    // *****************
    //   Action section
    // *****************

    @RequestMapping("/createNewGroupAction")
    public String createNewGroupAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @ModelAttribute("groupForm") GroupForm groupForm) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity clientEntity = new ClientEntity();
            groupForm.updateGroup(clientEntity);
            clientEntity.setManualCreatedGroup(true);
            clientEntity.setActive(true);
            clientEntity.setAccountOwner(accountEntity);

            clientService.createNewClient(clientEntity);
            clientEntity.setManualCreatedGroup(true);
            clientService.updateGroup(clientEntity);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Group successfully created.");
            return listIOwnerOfGroupPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("groupForm", groupForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't create new group - " + ex.getMessage());
            return "guest/createGroup";
        }
    }

    @RequestMapping("/editGroupAction/{idGroup}")
    public String editGroupAction(HttpServletRequest request,
                                  HttpSession session,
                                  Model model,
                                  @ModelAttribute("groupForm") GroupForm groupForm,
                                  @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity tempClientEntity = clientService.getClientByOwnerById(accountEntity, idGroup);
            groupForm.updateGroup(tempClientEntity);
            clientService.updateGroup(tempClientEntity);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Update successful.");
            return listIOwnerOfGroupPage(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editGroupPage(request, session, model, idGroup);
        }
    }

    @RequestMapping("/sendRequestFMSAction/{idGroup}")
    public String sendRequestFMSAction(HttpServletRequest request,
                                    HttpSession session,
                                    Model model,
                                    @ModelAttribute("requestFMSForm") RequestFMSForm requestFMSForm,
                                    @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByIdWithoutCheck(idGroup);

            RequestFMSEntity requestFMSEntity;
            try {
                requestFMSEntity =
                        requestFMSService.getRequestFMS(accountEntity.getIdAccount(), group.getIdClient());
            } catch (Exception ex) {
                requestFMSEntity =
                        requestFMSService.createNewEmptyRequestFMS(accountEntity, group);
            }
            requestFMSForm.updateRequestFMS(requestFMSEntity);
            requestFMSService.updateRequestFMS(requestFMSEntity);

            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Request for membership successfully sent.");
            return guestController.mainPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("requestFMSForm", requestFMSForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't send request - " + ex.getMessage());
            return "guest/groupPage";
        }
    }

    @RequestMapping("/deleteRequestFMSAction/{idGroup}")
    public String deleteRequestFMSAction(HttpServletRequest request,
                                         HttpSession session,
                                         Model model,
                                         @ModelAttribute("requestFMSForm") RequestFMSForm requestFMSForm,
                                         @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            RequestFMSEntity requestFMSEntity = requestFMSService.getRequestFMS(accountEntity.getIdAccount(), idGroup);
            requestFMSService.deleteRequestFMS(requestFMSEntity);

            request.setAttribute(RestaurantConstant.REQUEST_INFO, "Request for membership successfully deleted.");
            return guestController.mainPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("requestFMSForm", requestFMSForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't delete request - " + ex.getMessage());
            return "guest/groupPage";
        }
    }

    @RequestMapping("/leaveGroupAction/{idGroup}")
    public String leaveGroupAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @ModelAttribute("requestFMSForm") RequestFMSForm requestFMSForm,
                                   @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByMemberById(accountEntity, idGroup);
            clientService.leaveGroup(accountEntity, group);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "You successfully leaved the group.");
            return guestController.mainPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("requestFMSForm", requestFMSForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't leave the group - " + ex.getMessage());
            return "guest/groupPage";
        }
    }

    @RequestMapping("/disbandGroupAction/{idGroup}")
    public String disbandGroupAction(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("requestFMSForm") RequestFMSForm requestFMSForm,
                                     @PathVariable("idGroup") int idGroup) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(RestaurantConstant.SESSION_ACCOUNT);
            ClientEntity group = clientService.getClientByOwnerById(accountEntity, idGroup);
            clientService.disbandGroup(group);
            request.setAttribute(RestaurantConstant.REQUEST_INFO, "You successfully disband the group.");
            return guestController.mainPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("requestFMSForm", requestFMSForm);
            request.setAttribute(RestaurantConstant.REQUEST_ERROR, "Can't disband the group - " + ex.getMessage());
            return "guest/groupPage";
        }
    }
}
