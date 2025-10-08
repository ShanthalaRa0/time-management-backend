package co.jp.enon.tms.common;

import java.util.List;

public class BaseService {
	
    //constructor
	public BaseService() {
	}

	public void makeResponseTitle(BaseDto baseDto) throws Exception {
		makeResponseTitle(baseDto, null);
	}

	public void makeResponseTitle(BaseDto baseDto,List<String> listProgress) throws Exception {

//		String dtoClassName = baseDto.getClass().getSimpleName();

//		for (Field field : baseDto.getClass().getDeclaredFields()) {
//	        //field.setAccessible(true);
//	        String instanceName = field.getName();
//	        String typeName = field.getType().getSimpleName();
	        //String sname = field.get(baseDto).getClass().getSimpleName();	//if instance is null then field.get(baseDto)is null
	        //String sname = "";
	        //System.out.println("sname=" + sname + ", typeName=" + typeName + ", instanceName=" + instanceName);
//        	String dtoResClassName = null;
//	        if (typeName.equals("Object")) {
//	        	if (instanceName.equals("resHdTitle")) {
//	        		dtoResClassName = "ResponseHdTitle";
//	        	} else if (instanceName.equals("resDtTitle")) {
//	        		dtoResClassName = "ResponseDtTitle";
//	        	}
//	        }
//	        if (dtoResClassName != null) {
//				List<PvNamecollectionRepository> list = pvNamecollectionMapper.selectResponseDtTitel(dtoClassName, dtoResClassName, "true");
//
//				Object resTitle = null;
//
//				if (list.size() == 0) {
//					return;
//				}
//
//				StringBuilder sbResTitle = new StringBuilder("");
//
//				for (Iterator<PvNamecollectionRepository> it = list.iterator(); it.hasNext();) {
//					PvNamecollectionRepository pvNamecollRepo = it.next();
//					sbResTitle.append(sbResTitle.length() == 0 ? "{\"" : "\",\"");
//					sbResTitle.append(pvNamecollRepo.getNameAlpha()).append("\":\"").append(pvNamecollRepo.getNameShort());
//				}
//				sbResTitle.append("\"");
//				if (listProgress != null) {
//					StringBuilder sbProgTitle = new StringBuilder();
//					for (Iterator<String> it = listProgress.iterator(); it.hasNext();) {
//						String progress = it.next();
//						sbProgTitle.append(sbProgTitle.length() == 0 ? ",\"listProgress\":[\"" : "\",\"");
//						sbProgTitle.append(progress);
//					}
//					sbProgTitle.append("\"]");
//					sbResTitle.append(sbProgTitle.toString());
//				}
//				sbResTitle.append("}");
//
//				//JSON conversion class
//			    ObjectMapper mapper = new ObjectMapper();
//				resTitle = mapper.readValue(sbResTitle.toString(), Object.class);
//
//		        field.setAccessible(true);
//            	field.set(baseDto, resTitle);
//	        }
//		}

		return;

	}

}
