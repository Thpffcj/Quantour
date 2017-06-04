package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import businessLogicService.GetPlateDataService;
import po.StockPO;

public class PlateAction {

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
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate("concept");
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
		result = json.toString();
		return "success";
	}
	
	public String getIndustryPlate(){
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate("industry");
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
		result = json.toString();
		return "success";
	}

	public String getAreaPlate(){
		Map<String, String[]> platelist = new HashMap<>();
		platelist = getPlateDataService.GetPlate("area");
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
		result = json.toString();
		return "success";
	}
}
