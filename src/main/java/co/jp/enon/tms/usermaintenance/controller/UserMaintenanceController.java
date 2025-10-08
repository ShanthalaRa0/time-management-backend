package co.jp.enon.tms.usermaintenance.controller;


import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.enon.tms.usermaintenance.dto.UserDeleteDto;
import co.jp.enon.tms.usermaintenance.dto.UserInsertDto;
import co.jp.enon.tms.usermaintenance.dto.UserMonthOrderDto;
import co.jp.enon.tms.usermaintenance.dto.UserMonthReportDto;
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

	// User Registration
//	@RequestMapping(value = "/UserMaintenance/RegisterUser", method = RequestMethod.POST)
//	//@PreAuthorize("hasRole('PM_ADMIN')")
//	public UserInsertDto registUer(@RequestBody UserInsertDto userInsertDto) throws Exception {
//		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		userService.insert(userInsertDto);
//
//		return userInsertDto;
//	}

	// Change user Info
	@RequestMapping(value = "/UserMaintenance/ModifyUer", method = RequestMethod.PUT)
	public UserUpdateDto modifyUer(@RequestBody UserUpdateDto userUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.update(userUpdateDto);

		return userUpdateDto;
	}

	// Delete User
	@RequestMapping(value = "/UserMaintenance/RemoveUer", method = RequestMethod.DELETE)
	public UserDeleteDto removetUer(@RequestBody UserDeleteDto userDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.delete(userDeleteDto);

		return userDeleteDto;
	}

//	// 社員一覧
//	@RequestMapping(value = "/User/ReferUserAll", method = RequestMethod.GET)
//	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
//	public UserSearchAllDto referUserAll(@RequestParam(name = "userId", required = false) Integer userId,
//			@RequestParam(name = "loginUser", required = false) String loginUser,
//			@RequestParam(name = "userName", required = false) String userName) throws Exception {
//
//		UserSearchAllDto userSearchAllDto = new UserSearchAllDto();
//		UserSearchAllDto.RequestHd reqHd = userSearchAllDto.getReqHd();
//		reqHd.setUserId(userId);
//		reqHd.setLoginUser(loginUser);
//		reqHd.setUserName(userName);
//
//		userService.searchAllUsers(userSearchAllDto);
//
//		return userSearchAllDto;
//	}
//
//	// 社員月別日報一覧 UserMonthReport
//	@RequestMapping(value = "/User/ReferUserMonthReportMany", method = RequestMethod.GET)
//	public UserMonthReportDto referUserMonthReportMany(@RequestParam(name = "companyId", required = true) Integer companyId,
//			@RequestParam(name = "userId", required = true) Integer userId,
//			@RequestParam(name = "constId", required = false) Integer constId,
//			 @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
//			 DateTimeFormat.ISO.DATE) LocalDate startDate,
//			 @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
//			 DateTimeFormat.ISO.DATE) LocalDate endDate,
//			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
//		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		UserMonthReportDto userMonthReportDto = new UserMonthReportDto();
//		UserMonthReportDto.RequestHd regHd = userMonthReportDto.getReqHd();
//		regHd.setCompanyId(companyId);
//		regHd.setUserId(userId);
//		regHd.setConstId(constId);
//		regHd.setStartDate(startDate);
//		regHd.setEndDate(endDate);
//		if (deleted != null) {
//			if (deleted.equalsIgnoreCase("FALSE")) {
//				regHd.setDeleted((byte) 0);
//			} else if (deleted.equalsIgnoreCase("TRUE")) {
//				regHd.setDeleted((byte) 1);
//			}
//		} else {
//			regHd.setDeleted((byte) 2);
//		}
//		userService.searchManyUserMonthReport(userMonthReportDto);
//		return userMonthReportDto;
//	}
//
//	// 社員月別作業一覧 UserMonthOrder
//	@RequestMapping(value = "/User/ReferUserMonthOrderMany", method = RequestMethod.GET)
//	public UserMonthOrderDto referUserMonthOrderMany(@RequestParam(name = "companyId", required = true) Integer companyId,
//			@RequestParam(name = "userId", required = true) Integer userId,
//			@RequestParam(name = "constId", required = false) Integer constId,
//			 @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
//			 DateTimeFormat.ISO.DATE) LocalDate startDate,
//			 @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
//			 DateTimeFormat.ISO.DATE) LocalDate endDate,
//			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
//		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		UserMonthOrderDto userMonthOrderDto = new UserMonthOrderDto();
//		UserMonthOrderDto.RequestHd regHd = userMonthOrderDto.getReqHd();
//
//		regHd.setCompanyId(companyId);
//		regHd.setUserId(userId);
//		regHd.setConstId(constId);
//		regHd.setStartDate(startDate);
//		regHd.setEndDate(endDate);
//		if (deleted != null) {
//			if (deleted.equalsIgnoreCase("FALSE")) {
//				regHd.setDeleted((byte) 0);
//			} else if (deleted.equalsIgnoreCase("TRUE")) {
//				regHd.setDeleted((byte) 1);
//			}
//		} else {
//			regHd.setDeleted((byte) 2);
//		}
//		userService.searchManyUserMonthOrder(userMonthOrderDto);
//		return userMonthOrderDto;
//	}

}
