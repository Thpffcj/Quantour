package action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.opensymphony.xwork2.ModelDriven;

import businessLogicService.GetBollDataService;
import businessLogicService.GetKDJStochasticDataService;
import businessLogicService.GetRSIDataService;
import businessLogicService.MeanReversionService;
import businessLogicService.MomentumStrategyService;
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

	private MomentumStrategyService momentumStrategyService;
	public void setMomentumStrategyService(MomentumStrategyService momentumStrategyService){
		this.momentumStrategyService = momentumStrategyService;
	}
	
	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getMeanReversionGraph(){
		
		ArrayList<String> name = new ArrayList<>();
		name.add("1");
		name.add("2151");
		name.add("21");
		name.add("37");
		name.add("2229");
		name.add("2308");
		name.add("39");
		name.add("2249");
		name.add("2191");
		name.add("18");
//		int shares = Integer.parseInt(request.getParameter("shares"));
//		int holdPeriod = Integer.parseInt(request.getParameter("holdPeriod"));
//		int formingPeriod = Integer.parseInt(request.getParameter("formingPeriod"));
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		try {
			if(begin.equals("") || begin == null){
				data = meanReversionService.getMeanReversionGraphData(null, name, 3, 1, 20, "2014-01-20", "2014-04-29");
			}else{
				data = meanReversionService.getMeanReversionGraphData(null, name, 3, 1, 20, begin, end);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> market = new ArrayList<>();
		ArrayList<String> strategic = new ArrayList<>();
		date = data.get("date");
		market = data.get("market");
		strategic = data.get("strategic");
		
		int days = market.size();
		String[] dates = new String[days];
		Double[] marketIncome = new Double[days];
		Double[] strategicIncome = new Double[days];
		
		for(int i=0; i<days; i++){
			dates[i] = date.get(i);
			marketIncome[i] = Double.parseDouble(market.get(i));
			strategicIncome[i] = Double.parseDouble(strategic.get(i));
		}
		
		JSONObject json = new JSONObject();
		json.put("Dates", dates);
		json.put("MarketIncome", marketIncome);
		json.put("StrategicIncome", strategicIncome);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getMeanReturnRateGraph(){
		
		ArrayList<String> name = new ArrayList<>();
		name.add("1");
		name.add("2151");
		name.add("21");
		name.add("37");
		name.add("2229");
		name.add("2308");
		name.add("39");
		name.add("2249");
		name.add("2191");
		name.add("18");
//		int shares = Integer.parseInt(request.getParameter("shares"));
//		int holdPeriod = Integer.parseInt(request.getParameter("holdPeriod"));
//		int formingPeriod = Integer.parseInt(request.getParameter("formingPeriod"));
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = meanReversionService.GetMeanReturnRateGraphData(null, name, 3, 1, 20, "2014-01-20", "2014-04-29");
		}else{
			data = meanReversionService.GetMeanReturnRateGraphData(null, name, 3, 1, 20, begin, end);
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> excess = new ArrayList<>();
		date = data.get("date");
		excess = data.get("excessGraph");
		
		int days = excess.size();
		//
		System.out.println(days);
		String[] dates = new String[days];
		String[] value = new String[days];
		
		for(int i=0; i<days; i++){
			dates[i] = date.get(i);
			value[i] = excess.get(i);
		}
		
		JSONObject json = new JSONObject();
		json.put("Dates", dates);
		json.put("Values", value);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getMeanWinningPercentageGraph(){
		
		ArrayList<String> name = new ArrayList<>();
		name.add("1");
		name.add("2151");
		name.add("21");
		name.add("37");
		name.add("2229");
		name.add("2308");
		name.add("39");
		name.add("2249");
		name.add("2191");
		name.add("18");
//		int shares = Integer.parseInt(request.getParameter("shares"));
//		int holdPeriod = Integer.parseInt(request.getParameter("holdPeriod"));
//		int formingPeriod = Integer.parseInt(request.getParameter("formingPeriod"));
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = meanReversionService.GetMeanWinningPercentageGraphData(null, name, 3, 1, 20, "2014-01-20", "2014-04-29");
		}else{
			data = meanReversionService.GetMeanWinningPercentageGraphData(null, name, 3, 1, 20, begin, end);
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> winning = new ArrayList<>();
		date = data.get("date");
		winning = data.get("winningGraph");
		
		int days = winning.size();
		//
		System.out.println(days);
		String[] dates = new String[days];
		String[] value = new String[days];
		
		for(int i=0; i<days; i++){
			dates[i] = date.get(i);
			value[i] = winning.get(i);
		}
		
		JSONObject json = new JSONObject();
		json.put("Dates", dates);
		json.put("Values", value);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getRSIGraphData(){
		System.out.println("RSI");
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
		System.out.println("KDJ");
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
		System.out.println("Boll");
//		String beginDate = request.getParameter("begindate"); 
//		String endDate = request.getParameter("enddate"); 
//		int code = Integer.parseInt(request.getParameter("code")); 
		
		Map<String, ArrayList<String>> bollData = new HashMap<>();
		bollData = getBollDataService.getBollData("1", "2005-05-01","2005-07-01");
		ArrayList<String> date = bollData.get("KDate");
		ArrayList<String> open = bollData.get("KOpen");
		ArrayList<String> close = bollData.get("KClose");
		ArrayList<String> lowest = bollData.get("KLowest");
		ArrayList<String> highest = bollData.get("KHighest");
		ArrayList<String> volumn = bollData.get("KVolumn");
		
		int days = bollData.get("KDate").size();
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
		
		String[] suggestion = getBollDataService.getSuggest();
		JSONObject json = new JSONObject();
		json.put("Date", KDate);
		json.put("Open", KOpen);
		json.put("Close", KClose);
		json.put("Lowest", KLowest);
		json.put("Highest", KHighest);
		json.put("Volumn", KVolumn);
		json.put("Suggestion", suggestion);
		result = json.toString();
		return SUCCESS;
	}
}
