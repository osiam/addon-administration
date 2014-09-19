package org.osiam.addons.administration.controller.user;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is responsible for edit the group membership of an user
 */
@Controller
@RequestMapping(EditGroupMembershipController.CONTROLLER_PATH)
public class EditGroupMembershipController extends GenericController {

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/group";

    public static final String REQUEST_PARAMETER_ID = "id";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleEditGroupMembership(
            @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

        ModelAndView modelAndView = new ModelAndView("user/groupMembership");

        return modelAndView;
    }
}
