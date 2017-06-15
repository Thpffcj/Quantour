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
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import businessLogicService.GetBollDataService;
import data.StockData;
import dataService.StockDataService;
import po.BasePO;
import po.StockPO;
import vo.quantify.MeanReversionVO;

public class GetBollData implements GetBollDataService{

	static String yearRateOfReturn;
	static String benchmarkYearRateOfReturn;
	static String maximumRetracement;
	static String alpha;
	static double beta;
	static double sharpeRatio;
	
	MovingAverage movingAverage = new MovingAverage();
//	GraphUtil graphUtil = new GraphUtil();
	ParameterCalculation parameterCalculation = new ParameterCalculation();
	
	Map<String, ArrayList<Double>> upDatas = new HashMap<>();
	Map<String, ArrayList<Double>> downDatas = new HashMap<>();
	ArrayList<String> newStockPool = new ArrayList<>();
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	/**
	 * 获得Boll线图所需要的数据
	 */
	public Map<String, ArrayList<String>> getBollData(String condition, String begin, String end) {
		
		Map<String, ArrayList<String>> KData = new HashMap<>();
		ArrayList<String> KDate = new ArrayList<>();
		ArrayList<String> KOpen = new ArrayList<>();
		ArrayList<String> KClose = new ArrayList<>();
		ArrayList<String> KLowest = new ArrayList<>();
		ArrayList<String> KHighest = new ArrayList<>();
		ArrayList<String> KVolumn = new ArrayList<>();
		ArrayList<StockPO> stock = new ArrayList<>();
		stock = getStockData(condition, begin, end);
		
		for(int i=stock.size()-1; i>=0; i--){
			StockPO spo = stock.get(i);
			KDate.add(spo.getDate());
			KOpen.add(String.valueOf(spo.getOpen()));
			KClose.add(String.valueOf(spo.getClose()));
			KLowest.add(String.valueOf(spo.getLow()));
			KHighest.add(String.valueOf(spo.getHigh()));
			KVolumn.add(String.valueOf(spo.getVolume()));
		}
		KData.put("KDate", KDate);
		KData.put("KOpen", KOpen);
		KData.put("KClose", KClose);
		KData.put("KLowest", KLowest);
		KData.put("KHighest", KHighest);
		KData.put("KVolumn", KVolumn);
		
		return KData;
	}
	
	/**
	 * 获得指定时间内均线图的数据
	 */
	public Map<String, ArrayList<Double>> getAverageData(String condition, String begin, String end) {
		
		Map<String, ArrayList<Double>> data = new HashMap<>();
		
//		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		//查询日期之前的数据量
		int beforeDays = 19;
		int isBegin = 0;
		int code;
		if (Character.isDigit(condition.charAt(0))){
			code = Integer.parseInt(condition);
		}
		else{
			stockList = stockDataService.getStockByNameAndDate(condition, begin, end);
			code = Integer.parseInt(stockList.get(0).getCode());
		}
		
		int flag = 0;
		for(int i=0;i<19;i++){
			isBegin = stockDataService.JudgeIfTheLast(code, begin);
			//如果之前没有数据了
			if(isBegin == -1){
				flag ++;
				if(flag >=10){
					beforeDays = i - 10;
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
		System.out.println(begin+" "+beforeDays);
		stockList = getStockData(condition,begin,end);
//		
//		// 保留收盘价数据的集合
//		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
//		// 对应时间收盘价数据
//		TimeSeries MD = new TimeSeries("MD");
//		TimeSeries UP = new TimeSeries("UP");
//		TimeSeries DN = new TimeSeries("DN");
//		
//		ArrayList<Double> closes20 = new ArrayList<>();
//		ArrayList<Double> upData = new ArrayList<>();
//		ArrayList<Double> downData = new ArrayList<>();
//		
		StockPO spo = new StockPO();
//		ArrayList<String> dates = new ArrayList<>();
		ArrayList<Double> closes = new ArrayList<>();
		for(int i=stockList.size()-1;i>=0;i--){
			spo = stockList.get(i);
			String date = spo.getDate();
 			double close = spo.getClose();
 			closes.add(close);
	    }
		
//		closes20 = movingAverage.getAveData(closes,20,beforeDays);
//		
//		if(closes20 == null || closes20.size() == 0){
//			return null;
//		}
		
		ArrayList<Double> standardDeviation = new ArrayList<>();
		standardDeviation = getStandardDeviation(closes);
		
//		for(int i=0; i<closes20.size(); i++){
//			upData.add(closes20.get(i)+2*standardDeviation.get(i));
//			downData.add(closes20.get(i)-2*standardDeviation.get(i));
//		}
//	
//		upDatas.put(condition, upData);
//		downDatas.put(condition, downData);
//		
//		MD = addData(closes20, dates, MD, 19);
//		UP = addData(upData, dates, UP, 19);
//		DN = addData(downData, dates, DN, 19);
//		
//		timeSeriesCollection.addSeries(MD);
//		timeSeriesCollection.addSeries(UP);
//		timeSeriesCollection.addSeries(DN);
		
		newStockPool.add(condition);
		data.put("standardDeviation", standardDeviation);
		return data;
	}

	/**
	 * 获得标准差集合
	 * @param closes
	 * @return
	 */
	public ArrayList<Double> getStandardDeviation(ArrayList<Double> closes){
		ArrayList<Double> standardDeviations = new ArrayList<>();
		double standardDeviaton = 0;
		for(int i=0; i<closes.size()-19; i++){
			ArrayList<Double> temp = new ArrayList<>();
			for(int j=0; j<20; j++){
				temp.add(closes.get(i+j));
			}
			standardDeviaton = parameterCalculation.getStandardDeviation(temp);
			standardDeviations.add(standardDeviaton);
		}
		return standardDeviations;
	}
	
	public TimeSeries addData(ArrayList<Double> closes, ArrayList<String> dates, TimeSeries series,int beforeDays){
		for(int i=0;i<closes.size();i++){
			int day = 0;
			int month = 0;
			int year = 2000;
			String date = dates.get(beforeDays+i);
	    	String[] time = date.split("/");
	    	month = Integer.parseInt(time[0]);
	    	day = Integer.parseInt(time[1]);
	    	year = year + Integer.parseInt(time[2]);
	    	series.add(new Day(day,month,year),closes.get(i));
	    }
		return series;
	}
	
	public void getMapDatas(ArrayList<String> stockPool, String begin, String end){
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		for(int i=0; i<stockPool.size(); i++){
//			timeSeriesCollection = getAverageData(stockPool.get(i), begin, end);
		}
	}
	
	/**
	 * 策略和基准的累计收益率比较图
	 * @param stockPool
	 * @param begin
	 * @param end
	 * @return
	 */
	public DefaultCategoryDataset GetBollBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end){
		DecimalFormat df = new DecimalFormat("#.00%");
		getMapDatas(stockPool, begin, end);
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//市场收益
		ArrayList<Double> marketIncome = new ArrayList<>();
		//策略收益
		ArrayList<Double> strategicIncome = new ArrayList<>();
		
		int days = Integer.MAX_VALUE;
		ArrayList<StockPO> minDaysStock = new ArrayList<>();
		for(int i=0; i<newStockPool.size(); i++){
			ArrayList<StockPO> stock = getStockData(newStockPool.get(i), begin, end);
			int temp = stock.size();
			if(temp<days){
				days = temp;
				minDaysStock = stock;
			}
		}
		
		StockPO spo = new StockPO();
		ArrayList<String> dates = new ArrayList<>();
		for(int i = minDaysStock.size()-1; i>=0; i--){
			spo = minDaysStock.get(i);
 			String date = spo.getDate();
			dates.add(date);
	    }
		
		String series1 = "MStrategy";
		String series2 = "MBenchmark";
		
		//计算基准和策略的收益率
		if(section == null){
			marketIncome = getRateOfReturn(newStockPool, days, begin, end, 0);
		}else{
			marketIncome = getMarketIncomeBySection(section, begin, end);
		}
		strategicIncome = getRateOfReturn(stockPool, days, begin, end, 1);
	
		for(int i=0; i<strategicIncome.size(); i++){
			dataset.addValue(strategicIncome.get(i), series1, dates.get(i));
			dataset.addValue(marketIncome.get(i), series2, dates.get(i));
		}
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
		
		yearRateOfReturn = df.format(smoney);
		benchmarkYearRateOfReturn = df.format(mmoney);
		maximumRetracement = parameterCalculation.getMaxDrawdownLevel(strategicIncome);
		beta = parameterCalculation.getBetaCoefficient(marketIncome,strategicIncome);
		alpha = parameterCalculation.getAlphaCoefficient(smoney, marketIncome, strategicIncome);
		sharpeRatio = parameterCalculation.getSharpeRatio(strategicIncome);
		return dataset;
	}
	
	/**
	 * 通过版块获得市场收益率
	 * @param section
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<Double> getMarketIncomeBySection(String section, String begin, String end){
		ArrayList<Double> marketIncome = new ArrayList<>();
		ArrayList<Double> opens = new ArrayList<>();
		ArrayList<Double> adjClose = new ArrayList<>();
		opens = getBenchProfitEveryday(section, begin, end);
		adjClose = getBenchProfitEveryday(section, begin, end);
		double open = opens.get(0);
		for(int i=0; i<adjClose.size(); i++){
			marketIncome.add((adjClose.get(i)-open)/open);
		}
		return marketIncome;
	}
	
	/**
	 * 获得股票的收益率
	 * 策略选择的股票，资金平均分配投资，所以策略的收益率也是直接计算平均值
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<Double> getRateOfReturn(ArrayList<String> shareNames, int days, String begin, String end, int isSell) {

		ArrayList<Double> rateOfReturn = new ArrayList<>();
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		
		Map<String, ArrayList<StockPO>> map = new HashMap<>();
		for (String condition : shareNames) {
			stockList = getStockData(condition, begin, end);
			map.put(condition, stockList);
		}
		
		//每只股票的资金
		ArrayList<Double> money = new ArrayList<>();
		for(int i=0; i<shareNames.size(); i++){
			money.add(1000.0/shareNames.size());
		}
		//每只股票的个数
		ArrayList<Double> numbers = new ArrayList<>();
		for(int i=0; i<shareNames.size(); i++){
			numbers.add(0.0);
		}

		double in = 0;
		double out = 0;
		double rate = 0;
		
//		System.out.println(days);
		for (int i = days-1; i >= 0; i--) {
			if (isSell == 0) {
				for (String condition : shareNames) {
					stockList = map.get(condition);

					StockPO spo = new StockPO();
					spo = stockList.get(i);
					in = stockList.get(days-1).getOpen();
					out = spo.getAdjClose();
					rate += (out-in)/in;
				}
				rateOfReturn.add(rate/shareNames.size());
				rate = 0;
			} else {
				int share = 0;
				for (String condition : shareNames) {
					stockList = map.get(condition);
					
					StockPO spo = new StockPO();
					spo = stockList.get(i);
					// 买进
					if(spo.getClose() > upDatas.get(condition).get(days-1-i)){
						//如果已经买进,则跳过
						if(money.get(share) != 0.0){
							numbers.set(share, money.get(share)/spo.getOpen());
							money.set(share, 0.0);
						}
					}
					//卖出
					else if(spo.getClose() < downDatas.get(condition).get(days-1-i)){
						//如果已经卖出,则跳过
						if(numbers.get(share) != 0.0){
							money.set(share, numbers.get(share)*spo.getClose());
							numbers.set(share, 0.0);
						}
					}
					share++;
				}
				for(int j=0; j<share; j++){
					stockList = map.get(shareNames.get(j));
					rate += numbers.get(j)*stockList.get(i).getAdjClose() + money.get(j);
				}
				rateOfReturn.add((rate-1000)/1000);
				rate=0;
			}
		}
		return rateOfReturn;
	}
	
	public String[] getSuggest(){
		String[] suggest = new String[2];
		suggest[0] = "当价格自下而上突破上轨，即突破上方压力线时，我们认为多方力量正在走强，一波上涨行情已经形成，买入信号产生；当价格自上而下跌破下轨，即跌破支撑线时，我们认为空方力量正在走强，一波下跌趋势已经形成，卖出信号产生。";
		suggest[1] = "布林线本身既包括了趋势指标，也包括了震荡指标，能帮助我们快速的认清市场的走势，是非常常用的技术指标。一般情况下，使用布林线操作的胜率要高于KDJ和RSI等指标。因为这些指标通常会在价格盘整的时候失去作用，产生很多错误信号。而布林线可以很好的帮我们寻找盘整阶段，以及在盘整结束时及时入场。";
		return suggest;
	}
	
	public MeanReversionVO getParameter() {
		MeanReversionVO m = new MeanReversionVO(yearRateOfReturn, benchmarkYearRateOfReturn, maximumRetracement, 
				alpha, beta, sharpeRatio);
		return m;
	}
	
	/**
	 * 根据输入情况搜索数据
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
	
	/**
	 * 获取基准的每日收益率
	 * @param Begin
	 * @param End
	 * @return
	 */
	private ArrayList<Double> getBenchProfitEveryday(String section, String begin, String end) {
		
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
}
