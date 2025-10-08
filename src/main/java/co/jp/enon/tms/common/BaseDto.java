package co.jp.enon.tms.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseDto implements Serializable {
    //Requires static final long serialVersionUID
    private static final long serialVersionUID = 1L;

    // Defining a no-arg constructor
	public BaseDto() {
		this.reqDatetime = LocalDateTime.now();
	}

	private String tranId;
 	private String resultCode;
	private String resultMessage;
   	private String user;
    private String terminal;
    private LocalDateTime reqDatetime;
    private LocalDateTime resDatetime;

	public String getElapsedTime() {

		//SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss.SSS");
		if (this.resDatetime == null) {
			this.resDatetime = LocalDateTime.now();
		}

		java.time.Duration duration = java.time.Duration.between(this.reqDatetime, this.resDatetime);

		//Add Duration to 0:00
		java.time.LocalTime t = java.time.LocalTime.MIDNIGHT.plus(duration);
		String s = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(t);
		return s;
	}
}
