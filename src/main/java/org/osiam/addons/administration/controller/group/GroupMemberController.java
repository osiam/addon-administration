package org.osiam.addons.administration.controller.group;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.model.command.GroupMemberCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.service.UserService;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is responsible for handling user list. This is the main page of the "groupview".
 */
@Controller
@RequestMapping(GroupMemberController.CONTROLLER_PATH)
public class GroupMemberController {

    public static final String CONTROLLER_PATH = EditGroupController.CONTROLLER_PATH + "/member";

    public static final String REQUEST_PARAMETER_GROUP_ID = "id";

    public static final String MODEL = "model";
    public static final String MODEL_USER_LIST = "userList";
    public static final String MODEL_SESSION_DATA = "sessionData";
    public static final String MODEL_PAGING_LINKS = "paging";

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleView(
            @RequestParam(value = REQUEST_PARAMETER_GROUP_ID) String id){

        ModelAndView modelAndView = new ModelAndView("group/editMember");

        Map<String, User> allUsers = getAllUsers();
        Group group = groupService.getGroup(id);

        modelAndView.addObject(MODEL, new GroupMemberCommand(group));
        modelAndView.addObject(MODEL_USER_LIST, allUsers);

        return modelAndView;
    }

    private Map<String, User> getAllUsers() {
        SCIMSearchResult<User> result = userService.searchUser("", 0, 0L, "userName", true, "id, userName");

        Map<String, User> idToUserMapping = new HashMap<String, User>();

        for(User user : result.getResources()){
            idToUserMapping.put(user.getId(), user);
        }

        return idToUserMapping;
    }



}
