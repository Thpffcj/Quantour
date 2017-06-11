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

import businessLogicService.GetRSIDataService;
import data.StockData;
import dataService.StockDataService;
import po.BasePO;
import po.StockPO;
import vo.quantify.MeanReversionVO;

public class GetRSIData implements GetRSIDataService{

	static String yearRateOfReturn;
	static String benchmarkYearRateOfReturn;
	static String maximumRetracement;
	static String alpha;
	static double beta;
	static double sharpeRatio;
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
//	static StockDataService stockDataService = new StockData();
//	static GraphUtil graphUtil = new GraphUtil();
	//计算所需的各种参数
	ParameterCalculation parameterCalculation = new ParameterCalculation(); 
	
	Map<String, ArrayList<Double>> RSIDatas = new HashMap<>();
	ArrayList<String> newStockPool = new ArrayList<>();
	
	/**
	 * 获得指定时间内RSI图的数据
	 */
	public Map<String, ArrayList<String>> getRSIGraphData(String condition, String begin, String end) {

		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		try {
			begin = time.format(time.parse(begin));
			end = time.format(time.parse(end));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();

		int beforeDays = 20;
		int isBegin = 0;
		int code;
		if (Character.isDigit(condition.charAt(0))) {
			code = Integer.parseInt(condition);
		} else {
			stockList = stockDataService.getStockByNameAndDate(condition, begin, end);
			code = Integer.parseInt(stockList.get(0).getCode());
		}

		for (int i = 0; i < 20; i++) {
			isBegin = stockDataService.JudgeIfTheLast(code, begin);
			// 如果之前没有数据了
			if (isBegin == -1) {
				beforeDays = i;
				break;
			} else {
				begin = GetOrigin(String.valueOf(code), begin);
//				System.out.println(begin);
			}
		}
//		System.out.println(begin+" "+end);
		stockList = getStockData(condition, begin, end);

		if (stockList.size() == beforeDays) {
			return null;
		}

		double up = 0;
		double down = 0;
		String series = "RSI";

//		System.out.println(stockList.size());
		ArrayList<String> value = new ArrayList<>();
		ArrayList<String> date = new ArrayList<>();
//		System.out.println(stockList.size());
		for (int i = stockList.size() - 1; i > stockList.size() - 21; i--) {
			double close = stockList.get(i).getClose();
			double bclose = stockList.get(i - 1).getClose();
			if (bclose > close) {
				up = up + (bclose - close) / close;
			} else {
				down = down + (close - bclose) / close;
			}
		}
		value.add(String.valueOf(100 * up / (up + down)));
		date.add(stockList.get(stockList.size() - 20 - 1).getDate());
		dataset.addValue(100 * up / (up + down), series, stockList.get(stockList.size() - 20 - 1).getDate());

		for (int i = stockList.size() - 21; i > 0; i--) {
			double lclose = stockList.get(i + 20).getClose();
			double lbclose = stockList.get(i - 1 + 20).getClose();
			double close = stockList.get(i).getClose();
			double bclose = stockList.get(i - 1).getClose();
			if (bclose > close) {
				up = up + (bclose - close) / close;
			} else {
				down = down + (close - bclose) / close;
			}
			if (lbclose > lclose) {
				up = up - (lbclose - lclose) / lclose;
			} else {
				down = down - (lclose - lbclose) / lclose;
			}
			value.add(String.valueOf(100 * up / (up + down)));
			date.add(stockList.get(i - 1).getDate());
			dataset.addValue(100 * up / (up + down), series, stockList.get(i - 1).getDate());
		}
		// System.out.println(date.size()+" "+value.size());
		data.put("date", date);
		data.put("value", value);
		// RSIDatas.put(condition, value);
		// newStockPool.add(condition);
		return data;
	}

	public void GetMapDatas(ArrayList<String> stockPool, String begin, String end){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i=0; i<stockPool.size(); i++){
//			dataset = getRSIGraphData(stockPool.get(i), begin, end);
		}
	}
	
	/**
	 * 策略和基准的累计收益率比较图
	 * @param stockPool
	 * @param begin
	 * @param end
	 * @return
	 */
	public DefaultCategoryDataset GetRSIBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end){
		
		DecimalFormat df = new DecimalFormat("#.00%");
//		long start1 = System.nanoTime();
		GetMapDatas(stockPool, begin, end);
//		long start2 = System.nanoTime();
//		System.out.println(start2-start1);
		
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
		
//		long start3 = System.nanoTime();
//		System.out.println(start3-start2);
		String series1 = "MStrategy";
		String series2 = "MBenchmark";
		
		//计算基准和策略的收益率
		if(section == null){
			marketIncome = getRateOfReturn(newStockPool, days, begin, end, 0);
		}else{
			marketIncome = getMarketIncomeBySection(section, begin, end);
		}
		strategicIncome = getRateOfReturn(stockPool, days, begin, end, 1);
//		long start4 = System.nanoTime();
//		System.out.println(start4-start3);
//		long start5 = System.nanoTime();
//		System.out.println(start5-start4);
		
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
					if(RSIDatas.get(condition).get(days-1-i) <= 30){
						//如果已经买进,则跳过
						if(money.get(share) != 0.0){
							numbers.set(share, money.get(share)/spo.getOpen());
							money.set(share, 0.0);
						}
				    }
					//卖出
					else if(RSIDatas.get(condition).get(days-1-i) >= 70){
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
					rate += numbers.get(j)*stockList.get(i).getClose() + money.get(j);
				}
				rateOfReturn.add((rate-1000)/1000);
				rate=0;
			}
		}
		return rateOfReturn;
	}
	
	public String[] getSuggest(){
		String[] suggest = new String[2];
		suggest[0] = "RSI值越大，说明近一段时间内价格上涨所产生的波动占整个波动的比例越大。当RSI超过70时，我们认为涨幅过于强劲，接下来很有可能会反转下跌，所以定义70以上的区域为超买区，应当卖出。反之，我们定义30以下的区域为超卖区，应当买入。";
		suggest[1] = "RSI指标能够较为直观且有效的显示出一段时期内买卖双方的力量对比，帮助投资者较好的认清市场动态，掌握买卖时机，被多数投资者喜爱，尤其是短线操作中尤为给力，是最常用的技术指标之一。";
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