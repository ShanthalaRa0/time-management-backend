package co.jp.enon.tms.timemaintenance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtWorkReport {
	
	private Integer workReportId;
	private Integer userId;
	private LocalDate workDate;        // work_date DATE NOT NULL
	private Integer totalWorkTime = 0; // total_work_time INT DEFAULT 0
	private Integer totalBreakTime = 0; // total_break_time INT DEFAULT 0

	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
}
