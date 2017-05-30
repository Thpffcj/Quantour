package businessLogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import businessLogicService.MeanReversionService;
import data.StockData;
import dataService.StockDataService;
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

	static ArrayList<Integer> calculationCycle = new ArrayList<>();
	static ArrayList<String> excessIncome = new ArrayList<>();
	static ArrayList<String> winningPercentage = new ArrayList<>();

	static double winDays;
	static double loseDays;
	static double winPercentage;
	
	static Map<String,ArrayList<Double>> deviationDegree = new HashMap<>();
	static ArrayList<String> selectShares = new ArrayList<>();
	static ArrayList<String> dates = new ArrayList<>();
	static ArrayList<String> newStockPool = new ArrayList<>();
	//市场收益率
	static ArrayList<Double> marketIncome = new ArrayList<>();
	//策略收益率
	static ArrayList<Double> strategicIncome = new ArrayList<>();

	static DefaultCategoryDataset meanReversionDataset = null;
	static DefaultCategoryDataset meanReturnRateDataset = null;
	static DefaultCategoryDataset meanWinningPercentageDataset = null;
	static DefaultCategoryDataset distributionHistogramDataset = null;
	static String lastSection = null; 
	static ArrayList<String> lastStockPool = new ArrayList<>(); 
	static int lastShares = 0;
	static int lastHoldPeriod = 0; 
	static int lastFormingPeriod = 0; 
	static String lastBegin = null;
	static String lastEnd = null;
	
//	StockDataService sds = new StockData();
	MeanReversionUtil util = new MeanReversionUtil();
	MovingAverage movingAverage = new MovingAverage();
	ParameterCalculation parameterCalculation = new ParameterCalculation(); 
	GraphUtil graphUtil = new GraphUtil();

	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	/**
	 * holdPeriod������
	 * formingPeriod����
	 */
	public Map<String, ArrayList<String>> getMeanReversionGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end){
		
//		if (meanReversionDataset != null && lastShares == shares && lastHoldPeriod == holdPeriod 
//				&& lastFormingPeriod == formingPeriod  && lastBegin.equals(begin) 
//				&& lastEnd.equals(end) && lastStockPool.size() == stockPool.size()  && isEqual(lastStockPool, stockPool)) {
//			return meanReversionDataset;
//		}
		
		Map<String, ArrayList<String>> data = new HashMap<>();

		DecimalFormat df = new DecimalFormat("0.00%");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int daylong = stockDataService.getDate(begin, end).size();
//		newStockPool = new ArrayList<>();
//		for(int i=0; i<stockPool.size(); i++){
//			ArrayList<stockPO> stock = getStockData(stockPool.get(i), begin, end);
//			if(stock.size() >= daylong){
//				newStockPool.add(stockPool.get(i));
//			}
//		}
//		System.out.println(" "+newStockPool.size());
		
		deviationDegree = new HashMap<>();
		
		getStockPoolDeviationDegree(stockPool, formingPeriod, begin, end, daylong);
		
		
		int standard = Integer.MIN_VALUE;
		for (int i = 0; i < newStockPool.size(); i++) {
			int temp = getStockData(newStockPool.get(i), begin, end).size();
			if (temp > standard) {
				standard = temp;
			}
		}

		
		int days = Integer.MAX_VALUE;
		String condition = null;
		for (int i = 0; i < newStockPool.size(); i++) {
			int temp = getStockData(newStockPool.get(i), begin, end).size();
			if (temp <= days) {
				days = temp;
				condition = newStockPool.get(i);
			}
		}
		ArrayList<StockPO> minStock = getStockData(condition, begin, end);
		for (int i = minStock.size() - 1; i >= 0; i--) {
			dates.add(minStock.get(i).getDate());
		}

		selectShares = new ArrayList<>();
		selectShares = transferPositions(days, shares, holdPeriod, deviationDegree);
		
		String series1 = "����������";
		String series2 = "��׼������";

		marketIncome = new ArrayList<>();
		strategicIncome = new ArrayList<>();
		
		if (section == null) {
			marketIncome = getRateOfReturn(newStockPool, days, begin, end, shares, holdPeriod, 0);
		} else {
			marketIncome = getMarketIncomeBySection(section, begin, end);
		}
		if(selectShares != null){
			strategicIncome = getRateOfReturn(selectShares, days, begin, end, shares, holdPeriod, 1);
		}else{
			strategicIncome = marketIncome;
		}
		for (int i = 0; i < strategicIncome.size(); i++) {
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
		
		ArrayList<String> parameter = new ArrayList<>();
		parameter.add(yearRateOfReturn);
		parameter.add(benchmarkYearRateOfReturn);
		parameter.add(maximumRetracement);
		parameter.add(String.valueOf(beta));
		parameter.add(alpha);
		parameter.add(String.valueOf(sharpeRatio));
		data.put("parameter", parameter);
		
		meanReversionDataset = dataset;
		lastSection = section; 
		lastStockPool = stockPool; 
		lastShares = shares;
		lastHoldPeriod = holdPeriod; 
		lastFormingPeriod = formingPeriod; 
		lastBegin = begin;
		lastEnd = end;
		
		return data;
	}
	
	
	/**
	 * ͨ��������г�������
	 * @param section
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<Double> getMarketIncomeBySection(String section, String begin, String end){
		ArrayList<Double> marketIncome = new ArrayList<>();
		ArrayList<Double> opens = new ArrayList<>();
		ArrayList<Double> adjClose = new ArrayList<>();
		opens = stockDataService.getStockOpenBySection(section, begin, end);
		adjClose = stockDataService.getStockAdjCloseBySection(section, begin, end);
		double open = opens.get(0);
		for(int i=0; i<adjClose.size(); i++){
			marketIncome.add((adjClose.get(i)-open)/open);
		}
		return marketIncome;
	}
	
	/**
	 * ��ù�Ʊ��������
	 * ����ѡ��Ĺ�Ʊ���ʽ�ƽ������Ͷ�ʣ����Բ��Ե�������Ҳ��ֱ�Ӽ���ƽ��ֵ
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<Double> getRateOfReturn(ArrayList<String> shareNames, int days, String begin, String end, int shares,
			int holdPeriod, int isSell) {

		ArrayList<Double> rateOfReturn = new ArrayList<>();
		
		Map<String, ArrayList<StockPO>> map = new HashMap<>();
		for (String condition : shareNames) {
			ArrayList<StockPO> stockList = new ArrayList<StockPO>();
			stockList = getStockData(condition, begin, end);
			map.put(condition, stockList);
		}
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		double in = 0.0;
		double out = 0.0;
		double rate = 0.0;
		// �ֹ����е�λ��
		int m = 0;
		int nextPeriod = 0;
		double money = 1000/shares;
		
//		double saveMoney = 0;
//		System.out.println(" " +shareNames.size());
		StockPO spo = new StockPO();
		for (int i = days-1; i >= 0; i--) {
			if (isSell == 0) {
				for (String condition : shareNames) {
					stockList = map.get(condition);
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
					stockList = map.get(shareNames.get(m));
					spo = stockList.get(i);
					in = stockList.get(days-1-(nextPeriod/holdPeriod)*holdPeriod).getOpen();
					out = spo.getClose();
//					if(out<lastDay.get(j)){
//						saveMoney += (out/in)*money;
//					}
					lastDay.add(out);
//					System.out.println(shareNames.get(m)+" "+out+" "+in);
					rate += (out/in)*money;
					m++;
				}
//				System.out.println(rate);
				nextPeriod++;
				if(nextPeriod % holdPeriod == 0){
//					System.out.println(money);
					money = rate/shares;
				}
				
				rateOfReturn.add((rate-1000)/1000);
				rate=0;
			}
		}
		return rateOfReturn;
	}

	
	/**
	 *  ����
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
		//ѡ��ʱ���ڵ�����
	    for(int day=0; day<days; day+=holdPeriod){
	    	 double a[] = new double[n];
	    	 Map<Double,String> temp = new HashMap<>(); 
	    	 int index = 0;
	    	 for(ArrayList<Double> list : deviationDegree.values()){
//	    		 System.out.println(days+" "+list.size());
	    		 a[index] = list.get(day);
	 	    	temp.put(a[index], names.get(index));
	 	    	index++;
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
	 * �����Ʊ�����Ʊ��ƫ���
	 * @param stockPool
	 * @param begin
	 * @param end
	 * @return
	 */
	public void getStockPoolDeviationDegree(ArrayList<String> stockPool, int formingPeriod, String begin, String end, int standard){
		for(int i=0; i<stockPool.size(); i++){
			deviationDegree = getDeviationDegree(stockPool.get(i), formingPeriod, begin,end, standard);
		}
	}
	
	/**
	 * �������������������
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
	 * ���ָ����Ʊ��ƫ���
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<String,ArrayList<Double>> getDeviationDegree(String condition, int formingPeriod, String begin, String end, int standard) {
		
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		//��ѯ����֮ǰ��������
		int beforeDays = formingPeriod-1;
		int isBegin = 0;
		int code;
		
		if (Character.isDigit(condition.charAt(0))){
			code = Integer.parseInt(condition);
		}
		else{
			code = sds.getCodeByName(condition);
		}
		
		for(int i=0;i<formingPeriod-1;i++){
			isBegin = sds.JudgeIfTheLast(code, begin);
			//���֮ǰû��������
			if(isBegin==-1){
				beforeDays = i;
				break;
			}
			else{
				begin = graphUtil.GetOrigin(String.valueOf(code),begin);
			}
		}
		stockList = getStockData(condition,begin,end);
	
		
		StockPO spo = new StockPO();
		ArrayList<Double> adjCloses = new ArrayList<>();
		for(int i=stockList.size()-1;i>=0;i--){
			spo = stockList.get(i);
 			double close = spo.getAdjClose();
 			adjCloses.add(close);
	    }
		
		//��Ʊ��Ȩ���̼۾�ֵ���� 
		ArrayList<Double> closes = new ArrayList<>();
		closes = movingAverage.getAveData(adjCloses,formingPeriod,beforeDays);
	
//		for(int i=0; i<closes.size(); i++){
//			System.out.println(condition+" "+closes.get(i));
//		}
//		System.out.println(closes.size()+" "+standard);
		if(closes == null || closes.size() <= standard/2 || closes.size() < 10){
			return deviationDegree;
		}
	
		//��ù�Ʊƫ��ȼ���
//		System.out.println(closes.size()+" "+adjCloses.size()+" "+beforeDays);
		ArrayList<Double> d = getDeviationDegree(closes,adjCloses,beforeDays);
		deviationDegree.put(condition, d);
		newStockPool.add(condition);
		
		return deviationDegree;
	}
	
	/**
	 * ��ù�Ʊƫ��ȼ���
	 * @param closes20        ��Ʊ��Ȩ���̼۾�ֵ����
	 * @param adjCloses  ��Ʊ��Ȩ���̼ۼ���
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
	
	/**
	 * �����������벻ͬ�γ���/�����ڵĹ�ϵͼ
	 * @param stockPool
	 * @param shares
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public DefaultCategoryDataset GetMeanReturnRateGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end){
		
		calculationCycle = new ArrayList<>();
		excessIncome = new ArrayList<>();
		
		DecimalFormat df = new DecimalFormat("0.00%");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		//����ѡ���Ĺ�Ʊ
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
			calculationCycle.add(day.get(i));
			excessIncome.add(df.format(strategicIncome.get(i)-marketIncome.get(i)));
			dataset.addValue(strategicIncome.get(i)-marketIncome.get(i), series, day.get(i));
		}
		
		return dataset;
	}
	
	/**
	 * ����ʤ���벻ͬ�γ���/�����ڵĹ�ϵͼ
	 * @param stockPool
	 * @param shares
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public DefaultCategoryDataset GetMeanWinningPercentageGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end){

		winningPercentage = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00%");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		//����ѡ���Ĺ�Ʊ
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
		ArrayList<Integer> day = new ArrayList<>();

		for(int i=0; i<strategicIncome.size(); i++){
			day.add(i+1);
			if(marketIncome.get(i)>strategicIncome.get(i)){
				loseDay++;
			}else{
				winDay++;
			}
			winningPercentage.add(df.format(winDay/(winDay+loseDay)));
			dataset.addValue(winDay/(winDay+loseDay), series, day.get(i));
		}
		
		return dataset;
	}
	
	/**
	 * �����ʷֲ�ֱ��ͼ�����������ں��γ��ڣ�
	 * @param stockPool
	 * @param shares
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public DefaultCategoryDataset GetDistributionHistogramGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end){

		winDays = 0;
		loseDays = 0;
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		String seriesRed = "���������";
		String seriesGreen = "���������";
		
		Map<Double, Integer> distributionHistogram1 = new HashMap<>();
		Map<Double, Integer> distributionHistogram2 = new HashMap<>();
		
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		
		for(int i=0; i<strategicIncome.size(); i++){
			double income = Double.parseDouble(numberFormat.format(strategicIncome.get(i)));
			if(income>=0){
				if(income == -0){
					income = 0;
				}
				if(distributionHistogram1.containsKey(income)){
					int temp = distributionHistogram1.get(income);
					temp++;
					distributionHistogram1.put(income, temp);
				}
				else{
					distributionHistogram1.put(income, 1);
				}
				winDays++;
			}
			else{
				if(distributionHistogram2.containsKey(income)){
					int temp = distributionHistogram2.get(income);
					temp++;
					distributionHistogram2.put(income, temp);
				}
				else{
					distributionHistogram2.put(income, 1);
				}
				loseDays++;
			}
		}
		winPercentage = Double.parseDouble(numberFormat.format(winDays/(winDays+loseDays)));		
		
		int n = distributionHistogram1.size();
		int m = distributionHistogram2.size();
		double[] incomes1 = new double[n];
		double[] incomes2 = new double[m];

		int index1 = 0;
		int index2 = 0;
		for (Double name : distributionHistogram1.keySet()) {
			incomes1[index1] = name;
			index1++;
		}
		for (Double name : distributionHistogram2.keySet()) {
			double temp = -name;
			incomes2[index2] = temp;
			index2++;
		}

		util.heapSort(incomes1);
		util.heapSort(incomes2);
		
		dataset.addValue(0, seriesGreen, String.valueOf("0"));
		int i = 0;
		int j = 0;
		for(int p=0; p<m+n; p++){
			if(n == 0){
				dataset.addValue(distributionHistogram2.get(-incomes2[j]), seriesGreen, String.valueOf(incomes2[j]));
				j++;
				continue;
			}
			if(m == 0){
				dataset.addValue(distributionHistogram1.get(incomes1[i]), seriesRed, String.valueOf(incomes1[i]));
				i++;
				continue;
			}
			if(i == n){
				incomes1[i-1] = Integer.MAX_VALUE;
				i--;
			}
			if(j == m){
				incomes2[j-1] = Integer.MAX_VALUE;
				j--;
			}
			if(incomes1[i]<=incomes2[j]){
				dataset.addValue(distributionHistogram1.get(incomes1[i]), seriesRed, String.valueOf(incomes1[i]));
				i++;
			}else{
				dataset.addValue(distributionHistogram2.get(-incomes2[j]), seriesGreen, String.valueOf(incomes2[j]));
				j++;
			}
		}
		
		return dataset;
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
			m.setCalculationCycle(calculationCycle.get(i));
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

}