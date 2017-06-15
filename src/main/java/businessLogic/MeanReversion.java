package businessLogic;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import businessLogicService.MeanReversionService;
import dataService.StockDataService;
import dataService.UsersDao;
import po.BasePO;
import po.StockPO;
import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;
import vo.quantify.MeanReversionVO;

public class MeanReversion implements MeanReversionService{

	static String yearRateOfReturn;
	static String benchmarkYearRateOfReturn;
	static String maximumRetracement;
	static String alpha;
	static double beta;
	static double sharpeRatio;

	static ArrayList<String> calculationCycle = new ArrayList<>();
	static ArrayList<String> excessIncome = new ArrayList<>();
	static ArrayList<String> winningPercentage = new ArrayList<>();

	static double winDays;
	static double loseDays;
	static double winPercentage;
	
	//偏离度
	static Map<String,ArrayList<Double>> deviationDegree;
	//选中的股票
	static ArrayList<String> selectShares;
	//选中日期
	static ArrayList<String> dates;
	//新股票池
	static ArrayList<String> newStockPool;
	//市场收益率
	static ArrayList<Double> marketIncome;
	//策略收益率
	static ArrayList<Double> strategicIncome;

	
	MeanReversionUtil util = new MeanReversionUtil();
	MovingAverage movingAverage = new MovingAverage();
	ParameterCalculation parameterCalculation = new ParameterCalculation(); 
	
	private Map<String, ArrayList<String>> meanReversionData;
	private ArrayList<String> stockPool;
	private Map<String, ArrayList<StockPO>> stocks;
	private String section;
	private String userName;
	private int MIN = 1000;
	private String MINCODE = null;
	private String lastSection = null; 
	private int lastShares = 0;
	private int lastHoldPeriod = 0; 
	private int lastFormingPeriod = 0; 
	private String lastBegin = null;
	private String lastEnd = null;

	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	private UsersDao usersDao;
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	/**
	 * 均值回归所需数据
	 * holdPeriod持有期
	 * formingPeriod形成期
	 */
	public Map<String, ArrayList<String>> getMeanReversionGraphData(String section, String userName, int shares, int holdPeriod, int formingPeriod, String begin, String end){
		
		if (meanReversionData != null && lastHoldPeriod == holdPeriod 
				&& lastFormingPeriod == formingPeriod  && lastBegin.equals(begin) 
				&& lastEnd.equals(end) && lastSection.equals(section) && lastShares == shares) {
			return meanReversionData;
		}
		
		meanReversionData = new HashMap<>();
		stocks = new HashMap<>();
		deviationDegree = new HashMap<>();
		selectShares = new ArrayList<>();
		dates = new ArrayList<>();
		newStockPool = new ArrayList<>();
		marketIncome = new ArrayList<>();
		strategicIncome = new ArrayList<>();
		stockPool = new ArrayList<>();
		this.section = section;
		this.userName = userName;
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		getSectionCode();
		
		System.out.println("stockPool "+stockPool.size());
		
		
		int days = Integer.MAX_VALUE;
//		System.out.println("----1----");
		int daylong = stockDataService.getDate(begin, end).size();
		ArrayList<String> temp = new ArrayList<>();
		for(int i=0; i<Math.min(100, stockPool.size()); i++){
			temp.add(stockPool.get(i));
		}
		
//		System.out.println("newStockPool "+newStockPool.size());
//		System.out.println("dates "+dates.size());
		
		//获得股票偏离度
		deviationDegree = new HashMap<>();
		getStockPoolDeviationDegree(temp, formingPeriod, begin, end, daylong);
		System.out.println("MIN "+MIN+" MINCODE "+MINCODE);
		ArrayList<StockPO> minStock = getStockData(MINCODE, begin, end);
//		System.out.println("----3----");
		for (int i = minStock.size() - 1; i >= 0; i--) {
//			System.out.println("--loop--i----");
			dates.add(minStock.get(i).getDate());
		}
		days = dates.size();
//		System.out.println("----4----");
		System.out.println("newStockPool "+newStockPool.size());
		System.out.println("dates "+dates.size());
		//
//		System.out.println(newStockPool.size()+" "+dates.size());
		
		//调仓
		selectShares = new ArrayList<>();
		selectShares = transferPositions(days, shares, holdPeriod, deviationDegree);

		marketIncome = new ArrayList<>();
		strategicIncome = new ArrayList<>();
		
		if (section == null) {
			marketIncome = getRateOfReturn(newStockPool, days, begin, end, shares, holdPeriod, 0);
		} else {
			marketIncome = getBenchProfitEveryday(begin, end);
		}
		if(selectShares != null){
			strategicIncome = getRateOfReturn(selectShares, days, begin, end, shares, holdPeriod, 1);
		}else{
			strategicIncome = marketIncome;
		}
		
		ArrayList<String> sMarketIncome = new ArrayList<>();
		ArrayList<String> sStrategicIncome = new ArrayList<>();
		for(int i=0; i<strategicIncome.size(); i++){
			sMarketIncome.add(String.valueOf(marketIncome.get(i)));
			sStrategicIncome.add(String.valueOf(strategicIncome.get(i)));
		}
		data.put("date", dates);
		data.put("market", sMarketIncome);
		data.put("strategic", sStrategicIncome);
		
		double mmoney = 0;
		double smoney = 0;

		if(strategicIncome.size() == 0){
			smoney = 0;
		}else{
			smoney = (strategicIncome.get(strategicIncome.size()-1)/strategicIncome.size())*365;
		}
		if(marketIncome.size() == 0){
			mmoney = 0;
		}else{
			mmoney = (marketIncome.get(marketIncome.size()-1)/marketIncome.size())*365;
		}
		
//		yearRateOfReturn = df.format(smoney);
//		benchmarkYearRateOfReturn = df.format(mmoney);
//		maximumRetracement = parameterCalculation.getMaxDrawdownLevel(strategicIncome);
//		beta = parameterCalculation.getBetaCoefficient(marketIncome,strategicIncome);
//		alpha = parameterCalculation.getAlphaCoefficient(smoney, marketIncome, strategicIncome);
//		sharpeRatio = parameterCalculation.getSharpeRatio(strategicIncome);
//		
//		ArrayList<String> parameter = new ArrayList<>();
//		parameter.add(yearRateOfReturn);
//		parameter.add(benchmarkYearRateOfReturn);
//		parameter.add(maximumRetracement);
//		parameter.add(String.valueOf(beta));
//		parameter.add(alpha);
//		parameter.add(String.valueOf(sharpeRatio));
//		data.put("parameter", parameter);
		
		meanReversionData = data;
		lastSection = section; 
		lastShares = shares;
		lastHoldPeriod = holdPeriod; 
		lastFormingPeriod = formingPeriod; 
		lastBegin = begin;
		lastEnd = end;
		System.out.println("getMeanReversionGraphData success");
		return meanReversionData;
	}
	
	/**
	 * 超额收益
	 * @param stockPool
	 * @param shares
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<String, ArrayList<String>> GetMeanReturnRateGraphData(String section,  int shares, int holdPeriod, int formingPeriod, String begin, String end){
		
//		getMeanReversionGraphData(section, shares, holdPeriod, formingPeriod, begin, end);
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		
		calculationCycle = new ArrayList<>();
		excessIncome = new ArrayList<>();
		ArrayList<String> excessGraph = new ArrayList<>();
		
		DecimalFormat df = new DecimalFormat("0.00%");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		//
		int days = Integer.MAX_VALUE;
		for(int i=0; i<newStockPool.size(); i++){
			int temp = getStockData(newStockPool.get(i), begin, end).size();
			if(temp<days){
				days = temp;
			}
		}

		String series = "Excess";
		
		ArrayList<Integer> day = new ArrayList<>();
		
		for(int i=0; i<strategicIncome.size(); i++){
			day.add(i+1);
//			System.out.println(day.get(i));
			calculationCycle.add(String.valueOf(day.get(i)));
			excessIncome.add(df.format(strategicIncome.get(i)-marketIncome.get(i)));
			excessGraph.add(String.valueOf(strategicIncome.get(i)-marketIncome.get(i)));
			dataset.addValue(strategicIncome.get(i)-marketIncome.get(i), series, day.get(i));
		}
		
		data.put("date", calculationCycle);
		data.put("excessGraph", excessGraph);
		data.put("excessData", excessIncome);
		return data;
	}
	
	/**
	 * 策略胜率
	 * @param stockPool
	 * @param shares
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<String, ArrayList<String>> GetMeanWinningPercentageGraphData(String section, int shares, int holdPeriod, int formingPeriod, String begin, String end){

//		getMeanReversionGraphData(section, shares, holdPeriod, formingPeriod, begin, end);
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<String> winning = new ArrayList<>();
		
		winningPercentage = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00%");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		//
		int days = Integer.MAX_VALUE;
		for(int i=0; i<newStockPool.size(); i++){
			int temp = getStockData(newStockPool.get(i), begin, end).size();
			if(temp<days){
				days = temp;
			}
		}
		
		String series = "WinningPercentage";
		
		double winDay = 0.0;
		double loseDay = 0.0;
		ArrayList<String> day = new ArrayList<>();

		for(int i=0; i<strategicIncome.size(); i++){
			day.add(String.valueOf(i+1));
			if(marketIncome.get(i)>strategicIncome.get(i)){
				loseDay++;
			}else{
				winDay++;
			}
			winning.add(String.valueOf(winDay/(winDay+loseDay)));
			winningPercentage.add(df.format(winDay/(winDay+loseDay)));
			dataset.addValue(winDay/(winDay+loseDay), series, day.get(i));
		}
		
		data.put("winningGraph", winning);
		data.put("date", day);
		return data;
	}
	
	/**
	 * 获取基准的每日收益率
	 * @param Begin
	 * @param End
	 * @return
	 */
	private ArrayList<Double> getBenchProfitEveryday(String begin, String end) {
		
		ArrayList<Double> BenchmarkProfit = new ArrayList<Double>();

		ArrayList<BasePO> Benchmark = stockDataService.getBenchmarkByDate(section, begin, end);
		BenchmarkProfit = new ArrayList<Double>();

		BasePO basePO = new BasePO();
		double income = 0.0;
		double open = Benchmark.get(0).getAdjOpen();
		for (int i = 0; i < Benchmark.size(); i++) {
			basePO = Benchmark.get(i);
			income = (basePO.getAdjClose() - open) / open;
			BenchmarkProfit.add(income);
		}

		return BenchmarkProfit;
	}
	
	/**ֵ
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<Double> getRateOfReturn(ArrayList<String> shareNames, int days, String begin, String end, int shares,
			int holdPeriod, int isSell) {

		ArrayList<Double> rateOfReturn = new ArrayList<>();
		
//		Map<String, ArrayList<StockPO>> map = new HashMap<>();
//		for (String condition : shareNames) {
//			ArrayList<StockPO> stockList = new ArrayList<StockPO>();
//			stockList = getStockData(condition, begin, end);
//			map.put(condition, stockList);
//		}
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		double in = 0.0;
		double out = 0.0;
		double rate = 0.0;
		//
		int m = 0;
		int nextPeriod = 0;
		double money = 1000/shares;
		
//		double saveMoney = 0;
//		System.out.println(" " +shareNames.size());
		StockPO spo = new StockPO();
		for (int i = days-1; i >= 0; i--) {
			if (isSell == 0) {
				for (String condition : shareNames) {
					stockList = stocks.get(condition);
					spo = stockList.get(i);
					in = stockList.get(days-1).getOpen();
					out = spo.getAdjClose();
					rate += (out-in)/in;
				}
				rateOfReturn.add(rate/shareNames.size());
				rate = 0;
			} else {
				
				ArrayList<Double> lastDay = new ArrayList<>();
				for(int j=0; j<shares; j++){
					lastDay.add(0.0);
				}
				
				for (int j = 0; j < shares; j++) {
//					ArrayList<stockPO> stockList = new ArrayList<stockPO>();
					stockList = stocks.get(shareNames.get(m));
					if(stockList != null && stockList.size() >= days){
						spo = stockList.get(i);
						in = stockList.get(days-1-(nextPeriod/holdPeriod)*holdPeriod).getOpen();
						out = spo.getClose();
						lastDay.add(out);
						rate += (out/in)*money;
						m++;
					}
				}
				nextPeriod++;
				if(nextPeriod % holdPeriod == 0){
					money = rate/shares;
				}
				
				rateOfReturn.add((rate-1000)/1000);
				rate=0;
			}
		}
		return rateOfReturn;
	}

	
	/**
	 * @param shares
	 * @param deviationDegree
	 * @return
	 */
	public ArrayList<String> transferPositions(int days, int shares, int holdPeriod, Map<String,ArrayList<Double>> deviationDegree){
		
		if(deviationDegree.size() <= shares){
			return null;
		}
		ArrayList<String> names = new ArrayList<>();
		for(String name:deviationDegree.keySet()){
			names.add(name);
		}
		
		int location = 0;
		int n = deviationDegree.size();
		//
	    for(int day=0; day<days; day+=holdPeriod){
	    	 double a[] = new double[n];
	    	 Map<Double,String> temp = new HashMap<>(); 
	    	 int index = 0;
	    	 for(ArrayList<Double> list : deviationDegree.values()){
	    		if(list.size() > day){
	    			a[index] = list.get(day);
		 	    	temp.put(a[index], names.get(index));
		 	    	index++;
	    		}
	 	     }
	 	    util.heapSort(a);
	 	    int m = 0;
	 	    for(int i=a.length-1; m<shares; i--){
//	 	    	System.out.println(a[1]+" "+a[2]+" "+a[3]);
	 	    	selectShares.add(temp.get(a[i]));
	 	    	location++;
	 	    	m++;
	 	    }
//	 	    System.out.println(selectShares.size());
	 	    for(int i=0; i<holdPeriod-1; i++){
	 	    	for(int j=0; j<shares; j++){
//	 	    		System.out.println(location);
	 	    		selectShares.add(selectShares.get(location-shares+j));
	 	    	}
	 	    }
	 	    location += shares*(holdPeriod-1);
	    }
		return selectShares;
	}
	
	/**
	 * 获得股票偏离度
	 * @param stockPool
	 * @param begin
	 * @param end
	 * @return
	 */
	public void getStockPoolDeviationDegree(ArrayList<String> stockPool, int formingPeriod, String begin, String end, int standard){
		for(int i=0; i<stockPool.size(); i++){
//			System.out.println("getStockPoolDeviationDegree "+stockPool.get(i));
			deviationDegree = getDeviationDegree(stockPool.get(i), formingPeriod, begin,end, standard);
		}
	}
	
	/**
	 * 获得股票
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<StockPO> getStockData(String condition, String begin, String end){
		char flag = condition.charAt(0);
		ArrayList<StockPO> stockList = new ArrayList<>();
		if (Character.isDigit(flag)) {
			int code = Integer.parseInt(condition);
			stockList = stockDataService.getStockByCodeAndDate(code, begin, end);
		} else {
			String name = condition;
			stockList = stockDataService.getStockByNameAndDate(name, begin, end);
		}
		return stockList;
	}
	
	/**
	 * 
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<String,ArrayList<Double>> getDeviationDegree(String condition, int formingPeriod, String begin, String end, int standard) {
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		//
		int beforeDays = formingPeriod-1;
		int isBegin = 0;
		int code;
		
		if (Character.isDigit(condition.charAt(0))){
			code = Integer.parseInt(condition);
		}
		else{
			code = stockDataService.getCodeByName(condition);
		}
		
		int flag = 0;
		for(int i=0;i<formingPeriod-1;i++){
			isBegin = stockDataService.JudgeIfTheLast(code, begin);
			if(isBegin == -1){
				flag ++;
				if(flag >=10){
					beforeDays = i - 9;
					break;
				}
				else{
					begin = GetOrigin(String.valueOf(code),begin);
				}
				
			}
			else{
				begin = GetOrigin(String.valueOf(code),begin);
			}
		}
		flag = 0;
//		System.out.println("beforeDays "+beforeDays);
		stockList = getStockData(condition,begin,end);
	
		int daylong = stockDataService.getDate(begin, end).size();
//		System.out.println("daylong "+daylong+" "+stockList.size());
		if(stockList.size() <= 2*daylong/3 || stockList.size()<=20){
			return deviationDegree;
		}
		if(stockList.size() < MIN){
			MIN = stockList.size();
			MINCODE = condition;
		}
		
		StockPO spo = new StockPO();
		ArrayList<Double> adjCloses = new ArrayList<>();
		for(int i=stockList.size()-1;i>=0;i--){
			spo = stockList.get(i);
 			double close = spo.getAdjClose();
 			adjCloses.add(close);
	    }
		
		ArrayList<Double> closes = new ArrayList<>();
		closes = movingAverage.getAveData(adjCloses,formingPeriod,beforeDays);
	
		ArrayList<Double> d = getDeviationDegree(closes,adjCloses,beforeDays);
//		System.out.println("d "+d.size());
		deviationDegree.put(condition, d);
		newStockPool.add(condition);
		stocks.put(condition, stockList);
		
		return deviationDegree;
	}
	
	/**
	 * @param closes20       
	 * @param adjCloses  
	 * @return
	 */
	public ArrayList<Double> getDeviationDegree(ArrayList<Double> closes, ArrayList<Double> adjCloses,int beforeDays){
		if(closes == null){
			return null;
		}
		ArrayList<Double> deviationDegrees = new ArrayList<>();
		double deviationDegree;
		for(int i=0; i<closes.size(); i++){
			deviationDegree = (closes.get(i)-adjCloses.get(i+beforeDays)) / closes.get(i);
			deviationDegrees.add(deviationDegree);
		}
		return deviationDegrees;
	}
	
	private void getSectionCode(){
		System.out.println(section);
		if(section.equals("全部")){
			stockPool =  stockDataService.GetAllCode();
		}
		else if(section.equals("主板")){
			stockPool = stockDataService.getMainAllCode();
		}
		else if(section.equals("中小板")){
			stockPool = stockDataService.getGemAllCode();
		}
		else if(section.equals("创业板")){
			stockPool = stockDataService.getSmeAllCode();
		}
		else if(section.equals("我的自选股")){
			System.out.println(userName);
			stockPool = usersDao.getSelfStockByUsername(userName);
		}
	}

	public MeanReversionVO getParameter() {
		MeanReversionVO m = new MeanReversionVO(yearRateOfReturn, benchmarkYearRateOfReturn, maximumRetracement, 
				alpha, beta, sharpeRatio);
		return m;
	}

	public ArrayList<MeanReturnRateVO> getCalculationCycle() {
		ArrayList<MeanReturnRateVO> meanReturn = new ArrayList<>();
		for(int i=0; i<calculationCycle.size(); i++){
			MeanReturnRateVO m = new MeanReturnRateVO();
			m.setCalculationCycle(Integer.parseInt(calculationCycle.get(i)));
			m.setExcessIncome(excessIncome.get(i));
			m.setWinningPercentage(winningPercentage.get(i));
			meanReturn.add(m);
		}
		return meanReturn;
	}

	public DistributionHistogramVO getDistributionHistogram() {
		DistributionHistogramVO d = new DistributionHistogramVO(winDays, loseDays, winPercentage);
		return d;
	}
	
	private boolean isEqual(ArrayList<String> a1,ArrayList<String> a2){
		if(a1.size()==a2.size()){
			for(int i=0;i<a1.size();i++){
				if(!a1.get(i).equals(a2.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 获得指定日期的上一个有效日期
	 */
	public String GetOrigin(String Code,String Begin) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		String Origin = Begin;
		int volume;
		try {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				int code = Integer.parseInt(Code);
				volume = stockDataService.JudgeIfTheLast(code, Origin);
			} while (volume == 0);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}

}