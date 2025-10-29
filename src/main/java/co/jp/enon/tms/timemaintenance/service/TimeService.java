package co.jp.enon.tms.timemaintenance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.enon.tms.common.BaseService;
import co.jp.enon.tms.timemaintenance.dao.PtWorkBreakDao;
import co.jp.enon.tms.timemaintenance.dao.PtWorkReportDao;
import co.jp.enon.tms.timemaintenance.dao.PtWorkSessionDao;
import co.jp.enon.tms.timemaintenance.dto.WorkBreakInsertDto;
import co.jp.enon.tms.timemaintenance.dto.WorkBreakUpdateDto;
import co.jp.enon.tms.timemaintenance.dto.WorkReportInsertDto;
import co.jp.enon.tms.timemaintenance.dto.WorkReportUpdateDto;
import co.jp.enon.tms.timemaintenance.entity.PtWorkBreak;
import co.jp.enon.tms.timemaintenance.entity.PtWorkReport;
import co.jp.enon.tms.timemaintenance.entity.PtWorkSession;

@Service
public class TimeService extends BaseService {
	final static Logger logger = LoggerFactory.getLogger(TimeService.class);

	@Autowired
    private PtWorkReportDao ptWorkReportDao;
	
	@Autowired
	private PtWorkSessionDao ptWorkSessionDao;
	
	@Autowired
	private PtWorkBreakDao ptWorkBreakDao;
	
	public void saveWorkReportWithSession(WorkReportInsertDto workReportInsertDto) throws Exception {
	    var reqHd = workReportInsertDto.getReqHd();
	    LocalDate workDate = LocalDate.parse(reqHd.getWorkDate());
	    int workReportId = 0;
	    try { 	
	    	//if pt-work_report already has a record for given user_id and date, then get the work_reprt_id
	    	PtWorkReport ptWorkReportOld = ptWorkReportDao.getWorkReportByUserIdAndDate(reqHd.getUserId(), workDate);
	    	if (ptWorkReportOld == null) {
	    		 PtWorkReport ptWorkReport = new PtWorkReport();
	    		 ptWorkReport.setUserId(reqHd.getUserId());
	    		 ptWorkReport.setWorkDate(workDate);
		    	//Insert into pt_work_report 
		    	workReportId = ptWorkReportDao.save(ptWorkReport);
	    	} else {
	    		workReportId = ptWorkReportOld.getWorkReportId();
	    	}
	    	
	    	// Insert into pt_work_session
	        PtWorkSession ptWorkSession = new PtWorkSession();
	        ptWorkSession.setWorkReportId(workReportId);
	        ptWorkSession.setStartTime(reqHd.getStartTime());
	        ptWorkSession.setEndTime(null); // if starting session only
	        ptWorkSession.setWorkTime(0);
	        ptWorkSession.setBreakTime(0);

	        int workSessionId = ptWorkSessionDao.save(ptWorkSession);
	        
	        var responseHd = workReportInsertDto.getResponseHd();
	        responseHd.setWorkReportId(workReportId);
	        responseHd.setWorkSessionId(workSessionId);
	        
	        workReportInsertDto.setResultCode("000");
	    } catch (Exception ex) {
	    	
	    	workReportInsertDto.setResultCode("002");
	    	workReportInsertDto.setResultMessage("（Method：insert, Table Name：pt_work_report / pt_work_session ,Exception：" + ex.getMessage() + "）");
	    }
	    return;
	}
	
	public void updateWorkReportWithSession(WorkReportUpdateDto workReportUpdateDto) throws Exception {
		 var reqHd = workReportUpdateDto.getReqHd();	 
		 try { 	 
			 //get all break times pt_work_break using session id where endtime is null
			 // calculate break time using start and end times
			 // update the end time and breakTime
			 PtWorkBreak ptWorkBreakOld = ptWorkBreakDao.getActiveBreakTimeUsingSessionId(reqHd.getWorkSessionId());
			 int totalBreakInMinutes = 0; 
			 if (ptWorkBreakOld != null) {
				if (!ptWorkBreakOld.getBreakStart().equals(LocalTime.MIDNIGHT)) {
					totalBreakInMinutes = calculateBreakMinutes(ptWorkBreakOld.getBreakStart(), reqHd.getBreakEnd());
				}
				PtWorkBreak ptWorkBreak = new PtWorkBreak();
			        
		        ptWorkBreak.setWorkBreakId(ptWorkBreakOld.getWorkBreakId());
		        ptWorkBreak.setWorkSessionId(ptWorkBreakOld.getWorkSessionId()); 
		        ptWorkBreak.setBreakEnd(reqHd.getBreakEnd());
		        ptWorkBreak.setBreakTime(totalBreakInMinutes);
		        
		        ptWorkBreakDao.update(ptWorkBreak);
			 }
			 // get sum of all the break times from the ptworkBreak table using workSession id
			 int breakTimeSum = ptWorkBreakDao.getTotalBreakTime(reqHd.getWorkSessionId());
			 // get start time from work session table
			 LocalTime sessionStartTime = ptWorkSessionDao.getStartTime(reqHd.getWorkSessionId(), reqHd.getWorkReportId());
			 
			// get the sum of all the work times and update the session table
			 int sessionWorkTime = calculateBreakMinutes(sessionStartTime, reqHd.getBreakEnd());
			// Update into pt_work_session
			 
	         PtWorkSession ptWorkSession = new PtWorkSession();
	         ptWorkSession.setWorkReportId(reqHd.getWorkReportId());
	         ptWorkSession.setWorkSessionId(reqHd.getWorkSessionId());
	         ptWorkSession.setEndTime(reqHd.getBreakEnd()); //  session end
	         ptWorkSession.setWorkTime(sessionWorkTime); // in minutes
	         ptWorkSession.setBreakTime(breakTimeSum);
	         
	         ptWorkSessionDao.updateWorkSession(ptWorkSession);
			 
			// get all the session for given work_report_id and add up and update the work_report table
	         int workTime = ptWorkSessionDao.getTotalWorkTime(reqHd.getWorkReportId());
	         int totalBreakTime = ptWorkSessionDao.getTotalBreakTime(reqHd.getWorkReportId());
	         int totalWorkTime = workTime - totalBreakTime;
	         if (totalWorkTime <= 0) {
	        	 totalWorkTime = 0;
	         } 	 
		    //get workReportId from pt_work_report 
			LocalDate workDate = LocalDate.parse(reqHd.getWorkDate());
			PtWorkReport ptWorkReport = new PtWorkReport();
			ptWorkReport.setUserId(reqHd.getUserId());
		    ptWorkReport.setWorkDate(workDate);
		    ptWorkReport.setWorkReportId(reqHd.getWorkReportId());
		    ptWorkReport.setTotalWorkTime(totalWorkTime);
		    ptWorkReport.setTotalBreakTime(totalBreakTime);
		  
		    ptWorkReportDao.updateWorkReport(ptWorkReport);
	        workReportUpdateDto.setResultCode("000");
		 } catch (Exception ex) {
			 workReportUpdateDto.setResultCode("002");
			 workReportUpdateDto.setResultMessage("（Method：update, Table Name：pt_work_report / pt_work_session ,Exception：" + ex.getMessage() + "）");
	    }
		return;
	}
	
	public void saveWorkBreakStart(WorkBreakInsertDto workBreakInsertDto) throws Exception {
		var reqHd = workBreakInsertDto.getReqHd();	
		try { 		    
			// save date into pt_work_break
	        PtWorkBreak ptWorkBreak = new PtWorkBreak();
	        
	        ptWorkBreak.setWorkSessionId(reqHd.getWorkSessionId()); 
	        ptWorkBreak.setBreakStart(reqHd.getBreakStart());
	        
	        int workBreakId = ptWorkBreakDao.save(ptWorkBreak);
	        
	        var responseHd = workBreakInsertDto.getResponseHd();	
	        responseHd.setWorkBreakId(workBreakId);
	        workBreakInsertDto.setResultCode("000");
		 } catch (Exception ex) {
			 workBreakInsertDto.setResultCode("002");
			 workBreakInsertDto.setResultMessage("（Method：insert, Table Name：pt_work_break ,Exception：" + ex.getMessage() + "）");
	    }
		return;		
	}
	
	public void updateWorkBreak(WorkBreakUpdateDto workBreakUpdateDto) throws Exception {
		var reqHd = workBreakUpdateDto.getReqHd();	
		try { 
			//get break start from  pt_work_break
			LocalTime breakStart = ptWorkBreakDao.getBreakStartTime(reqHd.getWorkBreakId(), reqHd.getWorkSessionId());
			
			int totalBreakInMinutes = calculateBreakMinutes(breakStart, reqHd.getBreakEnd());
			
			// update break end and break time in pt_work_break
	        PtWorkBreak ptWorkBreak = new PtWorkBreak();
	        
	        ptWorkBreak.setWorkBreakId(reqHd.getWorkBreakId());
	        ptWorkBreak.setWorkSessionId(reqHd.getWorkSessionId()); 
	        ptWorkBreak.setBreakEnd(reqHd.getBreakEnd());
	        ptWorkBreak.setBreakTime(totalBreakInMinutes);
	        
	        ptWorkBreakDao.update(ptWorkBreak);
	        
	        workBreakUpdateDto.setResultCode("000");
	       
		 } catch (Exception ex) {
			 workBreakUpdateDto.setResultCode("002");
			 workBreakUpdateDto.setResultMessage("（Method：update, Table Name：pt_work_break ,Exception：" + ex.getMessage() + "）");
	    }
		return;		
	}
	
	private int calculateBreakMinutes(LocalTime breakStart, LocalTime breakEnd) {
	    if (breakStart == null || breakEnd == null) {
	        return 0; // or handle appropriately
	    }

	    // Duration between the two times
	    Duration duration = Duration.between(breakStart, breakEnd);

	    // Return total minutes
	    return (int) duration.toMinutes();
	}
	
}
