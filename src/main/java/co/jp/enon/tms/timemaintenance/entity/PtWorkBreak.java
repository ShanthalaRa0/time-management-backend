package co.jp.enon.tms.timemaintenance.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtWorkBreak {
	
	private Integer workBreakId;
	private Integer workSessionId;
	private LocalTime breakStart;       // corresponds to break_start, NOT NULL
    private LocalTime breakEnd = LocalTime.of(0, 0); // corresponds to break_end, default 0:00
    private Integer breakTime = 0;  

	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;	    

}
