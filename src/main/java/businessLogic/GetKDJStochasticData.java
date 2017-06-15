package businessLogic;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import businessLogicService.GetKDJStochasticDataService;
import data.StockData;
import dataService.StockDataService;
import po.BasePO;
import po.StockPO;
import vo.quantify.MeanReversionVO;

public class GetKDJStochasticData implements GetKDJStochasticDataService{

	static String yearRateOfReturn;
	static String benchmarkYearRateOfReturn;
	static String maximumRetracement;
	static String alpha;
	static double beta;
	static double sharpeRatio;
	
//	StockDataService sds = new StockData();
//	GraphUtil graphUtil = new GraphUtil();
	//计算所需的各种参数
	ParameterCalculation parameterCalculation = new ParameterCalculation(); 
	
	Map<String, ArrayList<Double>> KDatas = new HashMap<>();
	Map<String, ArrayList<Double>> DDatas = new HashMap<>();
	Map<String, ArrayList<Double>> JDatas = new HashMap<>();
	ArrayList<String> newStockPool = new ArrayList<>();
	
	int cycle = 9;
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	/**
	 * 获得指定时间内KDJ图的数据
	 */
	public Map<String, ArrayList<String>> getKDJStochasticData(String condition, String begin, String end) {
		
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		try {
			begin = time.format(time.parse(begin));
			end = time.format(time.parse(end));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();;
		
		int beforeDays = cycle-1;
		int isBegin = 0;
		int code;
		if (Character.isDigit(condition.charAt(0))){
			code = Integer.parseInt(condition);
		}
		else{
			stockList = stockDataService.getStockByNameAndDate(condition, begin, end);
			code = Integer.parseInt(stockList.get(0).getCode());
		}
		
		for(int i=0;i<cycle;i++){
			isBegin = stockDataService.JudgeIfTheLast(code, begin);
			//如果之前没有数据了
			if(isBegin==-1){
				beforeDays = i;
				break;
			}
			else{
				begin = GetOrigin(String.valueOf(code),begin);
			}
		}
		stockList = getStockData(condition, begin, end);
		
		if(stockList.size() == beforeDays){
			return null;
		}

		String seriesK = "K";
		String seriesD = "D";
		String seriesJ = "J";
		
		ArrayList<Double> RSV = new ArrayList<>();
		ArrayList<String> K = new ArrayList<>();
		ArrayList<String> D = new ArrayList<>();
		ArrayList<String> J = new ArrayList<>();
		ArrayList<String> dates = new ArrayList<>();
		
		ArrayList<Double> H = new ArrayList<>();
		ArrayList<Double> L = new ArrayList<>();
		double high = 0;
		double low = 0;
		
		//第一个周期内出现的最高价和最低价
		for(int i=0; i<cycle; i++){
			H.add(stockList.get(stockList.size()-1-i).getHigh());
			L.add(stockList.get(stockList.size()-1-i).getLow());
			high = getHighestPrice(H);
			low = getLowestPrice(L);
		}
		
		//第一天的各项数据,当没有前一日的K值或D值时，用50来代替
		RSV.add(calculateRSV(stockList.get(stockList.size()-1-cycle+1).getClose(),high,low));
		K.add(String.valueOf(calculateK(50.0, RSV.get(0))));
		D.add(String.valueOf(calculateD(50.0, Double.parseDouble(K.get(0)))));
		J.add(String.valueOf(calculateJ(Double.parseDouble(K.get(0)), Double.parseDouble(D.get(0)))));
		dates.add(stockList.get(stockList.size()-1-cycle+1).getDate());
		
		double temp = 0;
		for(int i=stockList.size()-1-cycle; i>=0; i--){
			temp = stockList.get(i).getHigh();
			if(temp > high){
				high = temp;
			}
			temp = stockList.get(i).getLow();
			if(temp < low){
				low = temp;
			}
			RSV.add(calculateRSV(stockList.get(i).getClose(), high, low));
			K.add(String.valueOf(calculateK(Double.parseDouble(K.get(K.size()-1)), RSV.get(RSV.size()-1))));
			D.add(String.valueOf(calculateD(Double.parseDouble(D.get(D.size()-1)), Double.parseDouble(K.get(K.size()-1)))));
			J.add(String.valueOf(calculateJ(Double.parseDouble(D.get(D.size()-1)), Double.parseDouble(K.get(K.size()-1)))));
			dates.add(stockList.get(i).getDate());
		}
		ArrayList<String> name = new ArrayList<>();
		name.add(stockDataService.getNameByCode(Integer.parseInt(condition)));
		
		data.put("K", K);
		data.put("D", D);
		data.put("J", J);
		data.put("date", dates);
		data.put("name", name);
//		for(int i=0; i<stockList.size()-cycle+1; i++){
//			dataset.addValue(K.get(i), seriesK, dates.get(i));
//			dataset.addValue(D.get(i), seriesD, dates.get(i));
//			dataset.addValue(J.get(i), seriesJ, dates.get(i));
//		}
		
//		KDatas.put(condition, K);
//		DDatas.put(condition, D);
//		JDatas.put(condition, J);
		
//		newStockPool.add(condition);
		return data;
	}
	
	public double calculateRSV(double close, double high, double low){
		return (close-low) / (high-low) * 100;
	}
	
	public double calculateK(double k, double rsv){
		return 2.0/3.0*k + 1.0/3.0*rsv;
	}

	public double calculateD(double d, double k){
		return 2.0/3.0*d + 1.0/3.0*k;
	}

	public double calculateJ(double d, double k){
		return 3.0*d - 2.0*k;
	}
	
	public double getHighestPrice(ArrayList<Double> closes){
		Comparator<Double> h = new Comparator<Double>() {
			public int compare(Double o1, Double o2) {
				if ((Double) o1 < (Double) o2)
					return 1;
				else{
					return -1;
				}
			}
		};
		closes.sort(h);
		double high = closes.get(0);
		return high;
	}
	
	public double getLowestPrice(ArrayList<Double> closes){
		Comparator<Double> l = new Comparator<Double>() {
			public int compare(Double o1, Double o2) {
				if ((Double) o1 > (Double) o2)
					return 1;
				else{
					return -1;
				}
			}
		};
		closes.sort(l);
		double low = closes.get(0);
		return low;
	}
	
	public void GetMapDatas(ArrayList<String> stockPool, String begin, String end){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i=0; i<stockPool.size(); i++){
//			dataset = getKDJStochasticData(stockPool.get(i), begin, end);
		}
	}
	
	/**
	 * 策略和基准的累计收益率比较图
	 * @param stockPool
	 * @param begin
	 * @param end
	 * @return
	 */
	 public DefaultCategoryDataset GetKDJStochasticBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end){
		
		DecimalFormat df = new DecimalFormat("#.00%");
		GetMapDatas(stockPool, begin, end);
		 
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
		for (int i = days - 1; i >= 0; i--) {
			if (isSell == 0) {
				for (String condition : shareNames) {
					stockList = map.get(condition);

					StockPO spo = new StockPO();
					spo = stockList.get(i);
					in = stockList.get(days - 1).getOpen();
					out = spo.getAdjClose();
					rate += (out - in) / in;
				}
				rateOfReturn.add(rate / shareNames.size());
				rate = 0;
			} else {
				int share = 0;
				for (String condition : shareNames) {
					stockList = map.get(condition);

					StockPO spo = new StockPO();
					spo = stockList.get(i);
					// 买进
					if (DDatas.get(condition).get(days-1-i) < 20
							&& (DDatas.get(condition).get(days-2-i)>KDatas.get(condition).get(days-2-i)||DDatas.get(condition).get(days-i)<KDatas.get(condition).get(days-i))) {
					
						// 如果已经买进,则跳过
						if (money.get(share) != 0.0) {
							numbers.set(share, money.get(share) / spo.getOpen());
							money.set(share, 0.0);
						}
					}
					// 卖出
					else if (DDatas.get(condition).get(days - 1 - i) > 80
							&& (DDatas.get(condition).get(days-2-i)<KDatas.get(condition).get(days-2-i)||DDatas.get(condition).get(days-i)>KDatas.get(condition).get(days-i))) {
						// 如果已经卖出,则跳过
						if (numbers.get(share) != 0.0) {
							money.set(share, numbers.get(share) * spo.getClose());
							numbers.set(share, 0.0);
						}
					}
					share++;
				}
				for (int j = 0; j < share; j++) {
					stockList = map.get(shareNames.get(j));
					rate += numbers.get(j) * stockList.get(i).getClose() + money.get(j);
				}
				rateOfReturn.add((rate - 1000) / 1000);
				rate = 0;
			}
		}
		return rateOfReturn;
	}
	
	public String[] getSuggest(){
		String[] suggest = new String[2];
		suggest[0] = "根据KDJ的取值，可将其划分为几个区域，即超买区、超卖区和徘徊区。按一般划分标准，K、D、J这三值在20以下为超卖区，是买入信号；K、D、J这三值在80以上为超买区，是卖出信号；K、D、J这三值在20-80之间为徘徊区，宜观望。本策略在D<20, K线和D线同时上升，且K线从下向上穿过D线时，全仓买入；当 D>80, K线和D线同时下降，且K线从上向下穿过D线时，全仓卖出。";
		suggest[1] = "KDJ指标在计算中考虑了计算周期内的最高价、最低价，兼顾了股价波动中的随机振幅，因而人们认为随机指标更真实地反映股价的波动，其提示作用更加明显。";
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
