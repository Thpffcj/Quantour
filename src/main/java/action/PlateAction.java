package action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.json.JSONObject;

import businessLogicService.GetPlateDataService;
import po.StockPO;
import vo.WebCookieVO;

public class PlateAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	
	private StockPO stock = new StockPO(); 
	public StockPO getModel() {
		return stock;
	}
	
	private GetPlateDataService getPlateDataService;
	public void setGetPlateDataService(GetPlateDataService getPlateDataService) {
		this.getPlateDataService = getPlateDataService;
	}

	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}	
	
	
	public String getConceptPlate(){
		String page = request.getParameter("page");
		
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate(page,"concept");
		JSONObject json = new JSONObject();
		json.put("index", platelist.get("index"));
		json.put("name", platelist.get("name"));
		json.put("fluctuation", platelist.get("fluctuation"));
		json.put("volume", platelist.get("volume"));
		json.put("up_num", platelist.get("up_num"));
		json.put("down_num", platelist.get("down_num"));
		json.put("price", platelist.get("price"));
		json.put("max_code", platelist.get("max_code"));
		json.put("max_price", platelist.get("max_price"));
		json.put("max_fluctuation", platelist.get("max_fluctuation"));
		json.put("length", platelist.get("length")[0]);
		result = json.toString();
		return "success";
	}
	
	public String getIndustryPlate(){
		String page = request.getParameter("page");
		
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate(page,"industry");
		JSONObject json = new JSONObject();
		json.put("index", platelist.get("index"));
		json.put("name", platelist.get("name"));
		json.put("fluctuation", platelist.get("fluctuation"));
		json.put("volume", platelist.get("volume"));
		json.put("up_num", platelist.get("up_num"));
		json.put("down_num", platelist.get("down_num"));
		json.put("price", platelist.get("price"));
		json.put("max_code", platelist.get("max_code"));
		json.put("max_price", platelist.get("max_price"));
		json.put("max_fluctuation", platelist.get("max_fluctuation"));
		json.put("length", platelist.get("length")[0]);
		System.out.println("getIndustryPlate----"+platelist.get("length")[0]);
		result = json.toString();
		return "success";
	}

	public String getAreaPlate(){
		String page = request.getParameter("page");
		
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate(page,"area");
		JSONObject json = new JSONObject();
		json.put("index", platelist.get("index"));
		json.put("name", platelist.get("name"));
		json.put("fluctuation", platelist.get("fluctuation"));
		json.put("volume", platelist.get("volume"));
		json.put("up_num", platelist.get("up_num"));
		json.put("down_num", platelist.get("down_num"));
		json.put("price", platelist.get("price"));
		json.put("max_code", platelist.get("max_code"));
		json.put("max_price", platelist.get("max_price"));
		json.put("max_fluctuation", platelist.get("max_fluctuation"));
		json.put("length", platelist.get("length")[0]);
		System.out.println("getAreaPlate----"+platelist.get("length")[0]);
		result = json.toString();
		return "success";
	}
	
	public String SavePlateName(){
		String platetype = request.getParameter("type");
		String platename = request.getParameter("plate");
		System.out.println(platename+platetype);
		WebCookieVO.setplatetype(platetype);
		WebCookieVO.setplatename(platename);
		return "success";
	}
	
	public String getPlateKGraphData(){
		String platetype = WebCookieVO.getplatetype();
		String platename = WebCookieVO.getplatename();
		if(platename.equals("")){
			platename = "上海";
			platetype = "area";
		}
		System.out.println(platename+platetype);
		
		Map<String, ArrayList<String>> KData = getPlateDataService.getPlateKGraphData(platetype, platename);
		
		ArrayList<String> date = KData.get("KDate");
		ArrayList<String> open = KData.get("KOpen");
		ArrayList<String> close = KData.get("KClose");
		ArrayList<String> lowest = KData.get("KLow");
		ArrayList<String> highest = KData.get("KHigh");
		ArrayList<String> volumn = KData.get("KVolume");
		
		int days = KData.get("KDate").size();
		String[] KDate = new String[days];
		Double[] KOpen = new Double[days];
		Double[] KClose = new Double[days];
		Double[] KLowest = new Double[days];
		Double[] KHighest = new Double[days];
		String[] KVolumn = new String[days];
		
		for(int i=0; i<days; i++){
			KDate[i] = date.get(i);
			KOpen[i] = Double.valueOf(open.get(i));
			KClose[i] = Double.valueOf(close.get(i));
			KLowest[i] = Double.valueOf(lowest.get(i));
			KHighest[i] = Double.valueOf(highest.get(i));
			KVolumn[i] = volumn.get(i);
		}
		
		DecimalFormat df = new DecimalFormat("0.00");
		JSONObject json = new JSONObject();
		
		json.put("LName", platename);
		json.put("LClose", KClose[days-2]);
		json.put("Average", KClose[days-1]);
		json.put("LOpen", KOpen[days-1]);
		json.put("LHighest",  KHighest[days-1]);
		json.put("LLowest",  KLowest[days-1]);
		json.put("LVolumn", KVolumn[days-1].substring(0, KVolumn[days-1].length()-3));
		json.put("LMoney",  df.format(Double.parseDouble(KVolumn[days-1])*KClose[days-1]/10000));
		json.put("LFluct", df.format((KClose[days-1]-KClose[days-2])/KClose[days-2]*100));
		json.put("Date", KDate);
		json.put("Open", KOpen);
		json.put("Close", KClose);
		json.put("Lowest", KLowest);
		json.put("Highest", KHighest);
		json.put("Volumn", KVolumn);
		result = json.toString();
		return "success";
	}
	
	public String getPlateChildrenData(){
		String platetype = WebCookieVO.getplatetype();
		String platename = WebCookieVO.getplatename();
		String page = request.getParameter("page");
		
		if(platename.equals("")){
			platename = "上海";
			platetype = "area";
		}
		
		Map<String, String[]> childrendata = getPlateDataService.getChildrenMessage(page, platetype, platename);
		JSONObject json = new JSONObject();
		json.put("index", childrendata.get("index"));
		json.put("name", childrendata.get("name"));
		json.put("code", childrendata.get("code"));
		json.put("open", childrendata.get("open"));
		json.put("high", childrendata.get("high"));
		json.put("low", childrendata.get("low"));
		json.put("close", childrendata.get("close"));
		json.put("fluct", childrendata.get("fluct"));
		json.put("volume", childrendata.get("volume"));
		int length = 0;
		if(childrendata.get("index").length%10==0){
			length = childrendata.get("index").length/10;
		}else{
			length = childrendata.get("index").length/10+1;
		}
		json.put("length", length);
		result = json.toString();
		return "success";
	}
}
