package co.jp.enon.tms.usermaintenance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.enon.tms.usermaintenance.dto.UserDeleteDto;
import co.jp.enon.tms.usermaintenance.dto.UserSearchAllDto;
import co.jp.enon.tms.usermaintenance.dto.UserSearchManyDto;
import co.jp.enon.tms.usermaintenance.dto.UserSearchOneDto;
import co.jp.enon.tms.usermaintenance.dto.UserUpdateDto;
import co.jp.enon.tms.usermaintenance.service.UserService;

@RestController
public class UserMaintenanceController {
	final static Logger logger = LoggerFactory.getLogger(UserMaintenanceController.class);

	//RoleService roleService;

	@Autowired
	UserService userService;

	@Autowired
	MessageSource msg;


	// All user search
	@RequestMapping(value = "/UserMaintenance/ReferUserMany", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public UserSearchManyDto referUserMany(
			@RequestParam(name = "active", required = false) Byte active) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchManyDto userSearchManyDto = new UserSearchManyDto();
		UserSearchManyDto.RequestHd regHd = userSearchManyDto.getReqHd();
		regHd.setActive(active);

		userService.searchMany(userSearchManyDto);

		return userSearchManyDto;
	}

	// Search 1 user
	@RequestMapping(value = "/UserMaintenance/ReferUserOne", method = RequestMethod.GET)
	public UserSearchOneDto referUserOne(@RequestParam(name = "email", required = true) String email)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchOneDto userSearchOneDto = new UserSearchOneDto();
		UserSearchOneDto.RequestHd reqHd = userSearchOneDto.getReqHd();

		reqHd.setEmail(email);

		userService.searchOne(userSearchOneDto);

		return userSearchOneDto;
	}

	// Change user Info
	@RequestMapping(value = "/UserMaintenance/ModifyUser", method = RequestMethod.PUT)
	public UserUpdateDto modifyUer(@RequestBody UserUpdateDto userUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.update(userUpdateDto);

		return userUpdateDto;
	}

	// Delete User
	@PutMapping("/UserMaintenance/RemoveUser")
	public UserDeleteDto removeUser(@RequestBody UserDeleteDto userDeleteDto) throws Exception {
	    logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

	    userService.delete(userDeleteDto);

	    return userDeleteDto;
	}

}
