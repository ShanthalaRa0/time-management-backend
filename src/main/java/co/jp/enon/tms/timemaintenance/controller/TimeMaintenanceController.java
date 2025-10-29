package co.jp.enon.tms.timemaintenance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.jp.enon.tms.timemaintenance.dto.WorkBreakInsertDto;
import co.jp.enon.tms.timemaintenance.dto.WorkBreakUpdateDto;
import co.jp.enon.tms.timemaintenance.dto.WorkReportInsertDto;
import co.jp.enon.tms.timemaintenance.dto.WorkReportUpdateDto;
import co.jp.enon.tms.timemaintenance.service.TimeService;


@RestController
public class TimeMaintenanceController {
	
	final static Logger logger = LoggerFactory.getLogger(TimeMaintenanceController.class);
	
	@Autowired
	TimeService timeService;
	
	// Start button click
	@PostMapping("/start-work-session")
    public WorkReportInsertDto startSession(@RequestBody WorkReportInsertDto workReportInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		timeService.saveWorkReportWithSession(workReportInsertDto);
		
		return workReportInsertDto;     
    }
	
	// end button click
	@PostMapping("/end-work-session")
    public WorkReportUpdateDto endSession(@RequestBody WorkReportUpdateDto workReportUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		timeService.updateWorkReportWithSession(workReportUpdateDto);
		
		return workReportUpdateDto;     
    }
	
	// Start button click
	@PostMapping("/start-break")
    public WorkBreakInsertDto startBreak(@RequestBody WorkBreakInsertDto workBreakInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		timeService.saveWorkBreakStart(workBreakInsertDto);
		
		return workBreakInsertDto;     
    }
	// Start button click
	@PostMapping("/end-break")
    public WorkBreakUpdateDto endBreak(@RequestBody WorkBreakUpdateDto workBreakUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		timeService.updateWorkBreak(workBreakUpdateDto);
		
		return workBreakUpdateDto;     
    }
	
	
	

}
