package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.opensymphony.xwork2.ModelDriven;

import businessLogicService.GetBollDataService;
import businessLogicService.GetKDJStochasticDataService;
import businessLogicService.GetRSIDataService;
import businessLogicService.MeanReversionService;
import po.StockPO;

public class QuantifyAction extends SuperAction implements ModelDriven<StockPO>{

	private static final long serialVersionUID = 1L;
	
	private StockPO stock = new StockPO(); 
	public StockPO getModel() {
		return stock;
	}
	
	private GetRSIDataService getRSIDataService;
	public void setGetRSIDataService(GetRSIDataService getRSIDataService) {
		this.getRSIDataService = getRSIDataService;
	}
	
	private GetKDJStochasticDataService getKDJStochasticDataService;
	public void setGetKDJStochasticDataService(GetKDJStochasticDataService getKDJStochasticDataService) {
		this.getKDJStochasticDataService = getKDJStochasticDataService;
	}
	
	private GetBollDataService getBollDataService;
	public void setGetBollDataService(GetBollDataService getBollDataService) {
		this.getBollDataService = getBollDataService;
	}
	
	private MeanReversionService meanReversionService;
	public void setMeanReversionService(MeanReversionService meanReversionService) {
		this.meanReversionService = meanReversionService;
	}

	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getRSIGraphData(){
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> value = new ArrayList<>();
		
		data = getRSIDataService.getRSIGraphData("1", "2005-05-01","2005-07-01");
		date = data.get("date");
		value = data.get("value");
		
		int days = value.size();
		Double[] values = new Double[days];
		String[] dates = new String[days];
		for(int i=0; i<days; i++){
			values[i] = Double.parseDouble(value.get(i));
			dates[i] = date.get(i);
		}
		
		JSONObject json = new JSONObject();
		json.put("Name", "深发展A");
		json.put("Dates", dates);
		json.put("Values", values);
		result = json.toString();
		
		return SUCCESS;
	}
	
	public String getKDJStochasticGraph(){
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<String> K = new ArrayList<>();
		ArrayList<String> D = new ArrayList<>();
		ArrayList<String> J = new ArrayList<>();
		ArrayList<String> date = new ArrayList<>();
		
		data = getKDJStochasticDataService.getKDJStochasticData("1", "2005-05-01","2005-07-01");
		date = data.get("date");
		K = data.get("K");
		D = data.get("D");
		J = data.get("J");
		
		int days = date.size();
		Double[] KValue = new Double[days];
		Double[] DValue = new Double[days];
		Double[] JValue = new Double[days];
		String[] dates = new String[days];
		for(int i=0; i<days; i++){
			KValue[i] = Double.parseDouble(K.get(i));
			DValue[i] = Double.parseDouble(D.get(i));
			JValue[i] = Double.parseDouble(J.get(i));
			dates[i] = date.get(i);
		}
		
		JSONObject json = new JSONObject();
		json.put("Name", "深发展A");
		json.put("Dates", dates);
		json.put("K", KValue);
		json.put("D", DValue);
		json.put("J", JValue);
		result = json.toString();
		
		return SUCCESS;
	}
	
	public String getBollGraph(){
		
//		String beginDate = request.getParameter("begindate"); 
//		String endDate = request.getParameter("enddate"); 
//		int code = Integer.parseInt(request.getParameter("code")); 
		
		Map<String, ArrayList<String>> data = new HashMap<>();
//		KData = getKGraphDataService.getKData("1","2/1/05","7/6/05");
		ArrayList<String> date = data.get("KDate");
		ArrayList<String> open = data.get("KOpen");
		ArrayList<String> close = data.get("KClose");
		ArrayList<String> lowest = data.get("KLowest");
		ArrayList<String> highest = data.get("KHighest");
		ArrayList<String> volumn = data.get("KVolumn");
		
		int days = data.get("KDate").size();
		String[] KDate = new String[days];
		Double[] KOpen = new Double[days];
		Double[] KClose = new Double[days];
		Double[] KLowest = new Double[days];
		Double[] KHighest = new Double[days];
		Integer[] KVolumn = new Integer[days];
		
		for(int i=0; i<days; i++){
			KDate[i] = date.get(i);
			KOpen[i] = Double.valueOf(open.get(i));
			KClose[i] = Double.valueOf(close.get(i));
			KLowest[i] = Double.valueOf(lowest.get(i));
			KHighest[i] = Double.valueOf(highest.get(i));
			KVolumn[i] = Integer.valueOf(volumn.get(i));
		}
		
		JSONObject json = new JSONObject();
		json.put("Date", KDate);
		json.put("Open", KOpen);
		json.put("Close", KClose);
		json.put("Lowest", KLowest);
		json.put("Highest", KHighest);
		json.put("Volumn", KVolumn);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getMeanReversionGraph(){
		
		int days = 0;
		String[] date = new String[days];
		Double[] marketIncome = new Double[days];
		Double[] KClostrategicIncome = new Double[days];
		
		JSONObject json = new JSONObject();
		json.put("data1", new String[]{"-10%", "-7%", "-5%", "-2%", "0", "2%", "5%",
				"7%", "10%"});
		json.put("data2", new Integer[]{5, 10, 20, 30, 40, 20, 10, 10, 5});
		result = json.toString();
		return SUCCESS;
	}
}
