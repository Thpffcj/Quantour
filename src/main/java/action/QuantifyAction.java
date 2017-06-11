package action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.opensymphony.xwork2.ModelDriven;

import businessLogic.MomentumStrategy;
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
	
	/**
	 * 动量策略收益率图
	 * @return
	 */
	public String getMStrategyComparedGraph(){
		System.out.println("getMStrategyComparedGraph");

		String username = request.getParameter("loginUserName");
		String section = request.getParameter("section");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		int existTime = 30;
		int holdTime = 30;
		if(request.getParameter("formation") != null){
			existTime = Integer.parseInt(request.getParameter("formation"));
		}
		if(request.getParameter("holding") != null){
			holdTime = Integer.parseInt(request.getParameter("holding"));
		}
//		System.out.println("existTime "+existTime);
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		try {
			if(begin.equals("") || begin == null){
				data = momentumStrategyService.getMStrategyComparedGraph("username","主板","2013-01-20", "2014-04-29", 30, 30);
			}else{
				data = momentumStrategyService.getMStrategyComparedGraph(username, section, begin, end, existTime, holdTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> market = new ArrayList<>();
		ArrayList<String> strategic = new ArrayList<>();
		date = data.get("日期");
		market = data.get("基准收益率");
		strategic = data.get("策略收益率");
		
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
	
	/**
	 * 动量策略胜率图
	 * @return
	 * @throws ParseException 
	 */
	public String getMStrategyWinningGraph() throws ParseException{
		System.out.println("getMStrategyWinningGraph");

		String username = request.getParameter("loginUserName");
		String section = request.getParameter("section");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		int existTime = 30;
		int holdTime = 30;
		if(request.getParameter("formation") != null){
			existTime = Integer.parseInt(request.getParameter("formation"));
		}
		if(request.getParameter("holding") != null){
			holdTime = Integer.parseInt(request.getParameter("holding"));
		}
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = momentumStrategyService.getMStrategyWinningGraph("username","主板",false, 0, begin, end);
		}else{
			data = momentumStrategyService.getMStrategyWinningGraph(username, section, false, 0, begin, end);
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> winning = new ArrayList<>();
		date = data.get("天数");
		winning = data.get("策略胜率");
		
		int days = winning.size();
		//
//		System.out.println(days);
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
	
	/**
	 * 动量策略超额收益率图
	 * @return
	 * @throws ParseException 
	 */
	public String getMStrategyExtraProfitGraph() throws ParseException{
		System.out.println("getMStrategyExtraProfitGraph");
		
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = momentumStrategyService.getMStrategyExtraProfitGraph("username","主板",false, 0, begin, end);
		}else{
			data = momentumStrategyService.getMStrategyExtraProfitGraph("username","主板",false, 0, begin, end);
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> excess = new ArrayList<>();
		date = data.get("天数");
		excess = data.get("额外收益率");
		
		int days = excess.size();
		//
//		System.out.println(days);
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
	
	
	/**
	 * 均值回归收益率图
	 * @return
	 */
	public String getMeanReversionGraph(){
		System.out.println("getMeanReversionGraph");

		String username = request.getParameter("loginUserName");
		String section = request.getParameter("section");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		int moving = 20;
		int hold = 5;
		int shares = 1;
		if(request.getParameter("movingaverage") != null){
			moving = Integer.parseInt(request.getParameter("movingaverage"));
		}
		if(request.getParameter("holding2") != null){
			hold = Integer.parseInt(request.getParameter("holding2"));
		}
		if(request.getParameter("holding2") != null){
			shares = Integer.parseInt(request.getParameter("holding2"));
		}
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		try {
			if(begin.equals("") || begin == null){
				data = meanReversionService.getMeanReversionGraphData("创业板", null, 3, 1, 20, "2014-01-20", "2014-12-29");
			}else{
				data = meanReversionService.getMeanReversionGraphData(section, username, shares, hold, moving, begin, end);
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
	
	/**
	 * 均值回归超额收益率图
	 * @return
	 */
	public String getMeanReturnRateGraph(){
		System.out.println("getMeanReturnRateGraph");
		
		String username = request.getParameter("loginUserName");
		String section = request.getParameter("section");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		int moving = 20;
		int hold = 5;
		int shares = 1;
		if(request.getParameter("movingaverage") != null){
			moving = Integer.parseInt(request.getParameter("movingaverage"));
		}
		if(request.getParameter("holding2") != null){
			hold = Integer.parseInt(request.getParameter("holding2"));
		}
		if(request.getParameter("holding2") != null){
			shares = Integer.parseInt(request.getParameter("holding2"));
		}
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = meanReversionService.GetMeanReturnRateGraphData("创业板", 1, 1, 20, "2014-01-20", "2014-12-29");
		}else{
			data = meanReversionService.GetMeanReturnRateGraphData(section, shares, hold, moving, begin, end);
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
	
	/**
	 * 均值回归胜率图
	 * @return
	 */
	public String getMeanWinningPercentageGraph(){
		System.out.println("getMeanWinningPercentageGraph");
		
		String username = request.getParameter("loginUserName");
		String section = request.getParameter("section");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		int moving = 20;
		int hold = 5;
		int shares = 1;
		if(request.getParameter("movingaverage") != null){
			moving = Integer.parseInt(request.getParameter("movingaverage"));
		}
		if(request.getParameter("holding2") != null){
			hold = Integer.parseInt(request.getParameter("holding2"));
		}
		if(request.getParameter("holding2") != null){
			shares = Integer.parseInt(request.getParameter("holding2"));
		}
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(begin.equals("") || begin == null){
			data = meanReversionService.GetMeanWinningPercentageGraphData("创业板", 1, 1, 20, "2014-01-20", "2014-04-29");
		}else{
			data = meanReversionService.GetMeanWinningPercentageGraphData(section, shares, hold, moving, begin, end);
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
		
		String code = request.getParameter("code");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		System.out.println("RSI "+code+" "+begin+" "+end);
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(code.equals("") || code == null){
			data = getRSIDataService.getRSIGraphData("1", "2014-05-01","2014-07-01");
		}else{
			data = getRSIDataService.getRSIGraphData(code, begin, end);
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> value = new ArrayList<>();
		
		date = data.get("date");
		value = data.get("value");
		
		int days = value.size();
		Double[] values = new Double[days];
		String[] dates = new String[days];
		for(int i=0; i<days; i++){
			values[i] = Double.parseDouble(value.get(i));
			dates[i] = date.get(i);
		}
		
		String[] suggestion = getRSIDataService.getSuggest();
		JSONObject json = new JSONObject();
		json.put("Name", "深发展A");
		json.put("Dates", dates);
		json.put("Values", values);
		json.put("Suggestion", suggestion);
		result = json.toString();
		
		return SUCCESS;
	}
	
	public String getKDJStochasticGraph(){
		
		String code = request.getParameter("code");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		System.out.println("KDJ "+code+" "+begin+" "+end);
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		if(code.equals("") || code == null){
			data = getKDJStochasticDataService.getKDJStochasticData("1", "2014-05-01","2014-07-01");
		}else{
			data = getKDJStochasticDataService.getKDJStochasticData(code, begin, end);
		}
		
		ArrayList<String> K = new ArrayList<>();
		ArrayList<String> D = new ArrayList<>();
		ArrayList<String> J = new ArrayList<>();
		ArrayList<String> date = new ArrayList<>();
		
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
		
		String[] suggestion = getKDJStochasticDataService.getSuggest();
		JSONObject json = new JSONObject();
		json.put("Name", "深发展A");
		json.put("Dates", dates);
		json.put("K", KValue);
		json.put("D", DValue);
		json.put("J", JValue);
		json.put("Suggestion", suggestion);
		result = json.toString();
		
		return SUCCESS;
	}
	
	public String getBollGraph(){
	
		String code = request.getParameter("code");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		System.out.println("Boll "+code+" "+begin+" "+end);
		
		Map<String, ArrayList<String>> bollData = new HashMap<>();
		Map<String, ArrayList<Double>> bollData1 = new HashMap<>();
		if(code.equals("") || code == null){
			bollData = getBollDataService.getBollData("1", "2014-05-01","2014-07-01");
			bollData1 = getBollDataService.getAverageData("1", "2014-05-01","2014-07-01");
		}else{
			bollData = getBollDataService.getBollData(code, begin, end);
			bollData1 = getBollDataService.getAverageData(code, begin, end);
		}
		
		ArrayList<String> date = bollData.get("KDate");
		ArrayList<String> open = bollData.get("KOpen");
		ArrayList<String> close = bollData.get("KClose");
		ArrayList<String> lowest = bollData.get("KLowest");
		ArrayList<String> highest = bollData.get("KHighest");
		ArrayList<String> volumn = bollData.get("KVolumn");
		ArrayList<Double> value = bollData1.get("standardDeviation");
		
		int days = bollData.get("KDate").size();
		String[] KDate = new String[days];
		Double[] KOpen = new Double[days];
		Double[] KClose = new Double[days];
		Double[] KLowest = new Double[days];
		Double[] KHighest = new Double[days];
		String[] KVolumn = new String[days];
		Double[] KValue = new Double[days];
		
		for(int i=0; i<days; i++){
			KDate[i] = date.get(i);
			KOpen[i] = Double.valueOf(open.get(i));
			KClose[i] = Double.valueOf(close.get(i));
			KLowest[i] = Double.valueOf(lowest.get(i));
			KHighest[i] = Double.valueOf(highest.get(i));
			KVolumn[i] = volumn.get(i);
			KValue[i] = value.get(i);
		}
		
		String[] suggestion = getBollDataService.getSuggest();
		JSONObject json = new JSONObject();
		json.put("Date", KDate);
		json.put("Open", KOpen);
		json.put("Close", KClose);
		json.put("Lowest", KLowest);
		json.put("Highest", KHighest);
		json.put("Volumn", KVolumn);
		json.put("Value", KValue);
		json.put("Suggestion", suggestion);
		result = json.toString();
		return SUCCESS;
	}
}
