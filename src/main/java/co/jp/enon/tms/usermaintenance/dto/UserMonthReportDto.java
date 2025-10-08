package co.jp.enon.tms.usermaintenance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.enon.tms.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data

public class UserMonthReportDto extends BaseDto{

	// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public UserMonthReportDto() {
			reqHd = new RequestHd();
			resDt = new ArrayList<ResponseDt>();
	    	super.setTranId(this.getClass().getName());
	    }

		// プロパティ(メンバ変数)の宣言
		private RequestHd reqHd;
		private List<ResponseDt> resDt ;
		private Object resDtTitle;

		@Data
		public static class RequestHd implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public RequestHd() {}

			// プロパティ(メンバ変数)の宣言
		    private Integer companyId;
			private Integer  userId;
			private Integer constId;
			private LocalDate startDate;
			private LocalDate endDate;
			private Byte deleted;
		}
		@Data
		public static class ResponseDt implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public ResponseDt() {}

			// プロパティ(メンバ変数)の宣言
		    private Integer constId;
			private String constName;
		    private Integer privConstId;
			//private String privConstCode;
			private String privConstName;
			private Integer userId;
			private String userName;
			private Integer reportNo;
			private String personCode;
			private String reportCode;
			private LocalDateTime reportDate;
			private String detail;
			private String constType;
			private String staff;
			private String numPerson;
			private String hours;
			private String earlyHours;
			private String overHours;
			private String deleted;
			private LocalDateTime createdAt;
			private LocalDateTime updatedAt;
		}

	}


