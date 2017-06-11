package businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import businessLogicService.MomentumStrategyService;
import data.UsersDaoImpl;
import dataService.StockDataService;
import dataService.UsersDao;
import po.BasePO;
import po.StockPO;
import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;
import vo.quantify.MeanReversionVO;

public class MomentumStrategy implements MomentumStrategyService{
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
		
	private ArrayList<String> stockPool;
	private String section;
	private String userName;
//	private StockDataService stockDataService= new StockData();
	private SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
	private ArrayList<Double[]> WinnerProfit = new ArrayList<Double[]>();
	
	private static String yearRateOfReturn;
	private static String benchmarkYearRateOfReturn;
	private static String maximumRetracement;
	private static String alpha;
	private static double beta;
	private static double sharpeRatio;
	
	private static ArrayList<Integer> calculationCycle = new ArrayList<>();
	private static ArrayList<String> excessIncome = new ArrayList<>();
	private static ArrayList<String> winningPercentage = new ArrayList<>();
	
	private static int WinDay = 0;
	private static int loseDay = 0;
	private static double winPercentage;
	
	public MomentumStrategy(){
		
	}
	
	/**
	 * 获得策略和基准的累计收益率比较图
	 * @param Begin
	 * @param End
	 * @param existTime
	 * @param holdTime
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, ArrayList<String>> getMStrategyComparedGraph(String userName,String section,String Begin,String End,int existTime,int holdTime) throws ParseException {

		Map<String, ArrayList<String>> data = new HashMap<>();
		
		this.section = section;
		this.userName = userName;
		getSectionCode();
		
		//作图线的名字
		String strategy = "策略收益率";
		String benchmark = "基准收益率";
		String day = "日期";
		//策略和基准的累计收益率
		ArrayList<String> Days = stockDataService.getDate(Begin, End);
		ArrayList<Double> StrategyProfit = getWinnerProfitEveryday(time.parse(Begin),time.parse(End),holdTime,existTime);
		ArrayList<Double> BenchmarkProfit = getBenchProfitEveryday(time.parse(Begin),time.parse(End));
		System.out.println(StrategyProfit.size());
		System.out.println(BenchmarkProfit.size());
		//计算年化收益率
		double aveProfit = 0;
		double benchaveProfit = 0;
		for(int i=0;i<StrategyProfit.size();i++){
			aveProfit += StrategyProfit.get(i);
		}
		aveProfit = aveProfit/StrategyProfit.size();
		
		for(int i=0;i<BenchmarkProfit.size();i++){
			benchaveProfit += BenchmarkProfit.get(i);
		}
		benchaveProfit = benchaveProfit/BenchmarkProfit.size();
		DecimalFormat df = new DecimalFormat("0.00%");
		this.yearRateOfReturn = df.format(aveProfit);
		this.benchmarkYearRateOfReturn = df.format(benchaveProfit);
		
		ParameterCalculation p = new ParameterCalculation();
		//计算阿尔法系数
		this.alpha = p.getAlphaCoefficient(aveProfit, BenchmarkProfit, StrategyProfit);
		//计算贝塔系数
		this.beta = p.getBetaCoefficient(BenchmarkProfit, StrategyProfit);
		//计算最大回撤率
		this.maximumRetracement = p.getMaxDrawdownLevel(StrategyProfit);
		//计算夏普比率
//		this.sharpeRatio = p.getSharpeRatio(StrategyProfit);
		//作图
		Collections.reverse(Days);
		
		ArrayList<String> StrategyProfit_String = new ArrayList<>();
		ArrayList<String> BenchmarkProfit_String = new ArrayList<>();
		
		for(int j=0;j<=Math.min(BenchmarkProfit.size(), StrategyProfit.size())-1;j++){
			StrategyProfit_String.add(StrategyProfit.get(j).toString());
			BenchmarkProfit_String.add(BenchmarkProfit.get(j).toString());
		}
		
		data.put(strategy, StrategyProfit_String);
		data.put(benchmark, BenchmarkProfit_String);
		data.put(day, Days);

		
		return data;
	}

	/**
	 * 额外收益率与不同持有期/形成期的关系图
	 * @param isHold
	 * @param Time
	 * @param Begin
	 * @param End
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, ArrayList<String>> getMStrategyExtraProfitGraph(String userName,String section,boolean isHold,int Time,String Begin,String End) throws ParseException {

		Map<String, ArrayList<String>> data = new HashMap<>();
		
		this.section = section;
		this.userName = userName;
		getSectionCode();
		
		double normalProfit = 0;
		ArrayList<String> Days = stockDataService.getDate(Begin, End);
		int daylong = Days.size();
		
		if(section==null){
			int num = 0;
			for(int i=0;i<stockPool.size();i++){
				ArrayList<StockPO> stock=stockDataService.getStockByCodeAndDate(Integer.valueOf(stockPool.get(i)), Begin, End);
				
				if(stock.size()>=daylong){
					num++;
					normalProfit += (stock.get(0).getAdjClose()-stock.get(stock.size()-1).getOpen())/stock.get(stock.size()-1).getOpen();
				}
			}
			normalProfit = normalProfit/num;
		}
		else{
			ArrayList<BasePO> Benchmark = stockDataService.getBenchmarkByDate(section, Begin, End);
			normalProfit = (Benchmark.get(Benchmark.size()-1).getAdjClose()-Benchmark.get(0).getAdjOpen())/Benchmark.get(0).getAdjOpen();	
		}
		ArrayList<String> ExtraProfit = new ArrayList<>();
		ArrayList<String> DayLong = new ArrayList<>();
		
		//若为真，持有期固定
		if(isHold){
			for(int i=10;i<121;i+=10){
				double totalProfit = getTotalProfit(Begin,End,i,Time);
				ExtraProfit.add(Double.toString(totalProfit));
				DayLong.add(Integer.toString(i));
				DecimalFormat df = new DecimalFormat("0.00%");
				this.calculationCycle.add(i);
				this.excessIncome.add(df.format(totalProfit-normalProfit));
			}
		}
		//若为假，形成期固定
		else{
			for(int i=Days.size()/10;i<=Days.size();i+=Days.size()/10){
				double totalProfit = getTotalProfit(Begin,End,Time,i);
				ExtraProfit.add(Double.toString(totalProfit));
				DayLong.add(Integer.toString(i));
				DecimalFormat df = new DecimalFormat("0.00%");
				this.calculationCycle.add(i);
				this.excessIncome.add(df.format(totalProfit-normalProfit));
			}
		}
		data.put("额外收益率", ExtraProfit);
		data.put("天数", DayLong);

		
		return data;
	}

	/**
	 * 策略胜率与不同持有期/形成期的关系图
	 */
	public Map<String, ArrayList<String>> getMStrategyWinningGraph(String userName,String section,boolean isHold, int Time, String Begin, String End) throws ParseException {

		Map<String, ArrayList<String>> data = new HashMap<>();
		
		this.section = section;
		this.userName = userName;
		getSectionCode();
		
		Date begin = time.parse(Begin);
		Date end = time.parse(End);
		ArrayList<String> WinPercentage = new ArrayList<>();
		ArrayList<String> DayLong = new ArrayList<>();
		ArrayList<String> Days = stockDataService.getDate(Begin, End);
		ArrayList<Double> BenchmarkProfit = getBenchProfitEveryday(begin,end);
		DecimalFormat df = new DecimalFormat("0.00%");
		if(isHold){
			for(int i=10;i<121;i+=10){
				int WinDay = 0;
				ArrayList<Double> WinnerProfit = getWinnerProfitEveryday(begin,end,Time,i);
				for(int j=0;j<Math.min(WinnerProfit.size(),BenchmarkProfit.size());j++){
					if(WinnerProfit.get(j)-BenchmarkProfit.get(j)>0){
						WinDay++;
					}
				}
				WinPercentage.add(Double.toString((double)(WinDay*1.0/Days.size())));
				DayLong.add(Integer.toString(i));
				winningPercentage.add(df.format((double)(WinDay*1.0/Days.size())));
			}
		}
		else{
			for(int i=Days.size()/10;i<=Days.size();i+=Days.size()/10){
				int WinDay = 0;
				ArrayList<Double> WinnerProfit = getWinnerProfitEveryday(begin,end,i,Time);
				for(int j=0;j<Math.min(WinnerProfit.size(), BenchmarkProfit.size());j++){
					if(WinnerProfit.get(j)-BenchmarkProfit.get(j)>0){
						WinDay++;
					}
				}
				WinPercentage.add(Double.toString((double)(WinDay*1.0/Days.size())));
				DayLong.add(Integer.toString(i));
				winningPercentage.add(df.format((double)(WinDay*1.0/Days.size())));
			}
		}
		data.put("策略胜率", WinPercentage);
		data.put("天数", DayLong);

		return data;
	}
	
	
	/**
	 * 获得给定形成期的赢家组合
	 * @param BeginDate
	 * @param EndDate
	 * @return
	 */
	private void getWinnerGroup(Date existBegin,Date existEnd,Date holdBegin,Date holdEnd){
		this.WinnerProfit = new ArrayList<Double[]>();
		ArrayList<Double> existProfit = new ArrayList<Double>();
		ArrayList<Double[]> holdProfit = new ArrayList<Double[]>();
		
		int WinnerNum = (stockPool.size()/5)+1;
		int daylong = stockDataService.getDate(time.format(existBegin), time.format(holdEnd)).size();  
		int holddaylong = stockDataService.getDate(time.format(holdBegin), time.format(holdEnd)).size(); 
		
		//判断股票在这段时间内是否有停牌
		for(int i=0;i<stockPool.size();i++){
			//获得该股票从生成期到持有期结束的所有
			ArrayList<StockPO> stock = stockDataService.getStockByCodeAndDate(Integer.valueOf(stockPool.get(i)), time.format(existBegin), time.format(holdEnd));
//			System.out.println(stock.size());
			Collections.reverse(stock);
			if(stock.size()>=daylong&&holddaylong>0){
				Double[] hp = new Double[holddaylong];
				for(int j=0;j<holddaylong;j++){
					double profit = stock.get(j+daylong-holddaylong).getAdjClose()/stock.get(daylong-holddaylong).getOpen()-1;
					hp[j] = profit;
				}
				double ep = stock.get(daylong-holddaylong).getAdjClose()/stock.get(0).getOpen()-1;
				existProfit.add(ep);
				holdProfit.add(hp);
			}
		}
		//筛选赢家组合,使用冒泡排序
		if(!existProfit.isEmpty()){
			for(int i=0;i<Math.min(WinnerNum, existProfit.size());i++){
				for(int j=existProfit.size()-1;j>0;j--){
					if(existProfit.get(j)>existProfit.get(j-1)){
						double temp1 = existProfit.get(j-1);
						existProfit.set(j-1, existProfit.get(j));
						existProfit.set(j, temp1);
						
						Double[] temp2 = new Double[holdProfit.get(j).length];
						System.arraycopy(holdProfit.get(j), 0, temp2, 0, holdProfit.get(j).length);
						System.arraycopy(holdProfit.get(j-1), 0, holdProfit.get(j),0, holdProfit.get(j-1).length);
						System.arraycopy(temp2, 0, holdProfit.get(j-1), 0, temp2.length);
					}
				}
			}
			this.WinnerProfit.add(holdProfit.get(0));
			existProfit.remove(0);
			holdProfit.remove(0);
		}
	}
	
	/**
	 * 获得赢家组合在某段持有期的每日累计收益率
	 * @param Begin
	 * @param End
	 * @param Winner
	 * @return
	 * @throws ParseException 
	 */
	private ArrayList<Double> getWinnerProfitEveryHoldDay() {
		ArrayList<Double> Profit = new ArrayList<Double>();
		if(!WinnerProfit.isEmpty()){
			for(int i=0;i<WinnerProfit.get(0).length;i++){
				double pro = 0;
				for(int j=0;j<WinnerProfit.size();j++){
					pro += WinnerProfit.get(j)[i];
				}
				Profit.add(pro/WinnerProfit.size());
			}
		}
		return Profit;
	}
	
	/**
	 * 获得距离当前日期指定日期后的日期Date类
	 * @param Begin
	 * @param days
	 * @return
	 */
	private Date getDateAfterDays(Date Begin,int days){
		Calendar c = new GregorianCalendar();
		c.setTime(Begin);
		c.add(Calendar.DATE, days);
		Date End = c.getTime();
		return End;
	}
	/**
	 * 获得自选股票池的基准
	 * @param Begin
	 * @param End
	 * @param holdTime
	 * @return
	 */
	private ArrayList<Double> getSelfChosenBench(Date Begin,Date End){
		ArrayList<Double> SelfChosenProfit = new ArrayList<Double>();
		ArrayList<Double[]> Profit = new ArrayList<Double[]>();
		
		int daylong = stockDataService.getDate(time.format(Begin), time.format(End)).size(); 
		for(int i=0;i<stockPool.size();i++){
			ArrayList<StockPO> stock=stockDataService.getStockByCodeAndDate(Integer.valueOf(stockPool.get(i)), time.format(Begin), time.format(End));
			Collections.reverse(stock);				  
			Double[] p = new Double[stock.size()];
			if(stock.size()>=daylong){
				for(int j=0;j<stock.size()||stock.size()==0;j++){
					p[j]=(stock.get(j).getAdjClose()-stock.get(0).getOpen())/stock.get(0).getOpen();
				}
				Profit.add(p);
			}
		}
		
		for(int i=0;i<Profit.get(0).length;i++){
			double pro = 0;
			for(int j=0;j<Profit.size();j++){
				pro += Profit.get(j)[i];
			}
			SelfChosenProfit.add(pro/Profit.size());
		}
		return SelfChosenProfit;
		
	}
	/**
	 * 获得股票池的累计收益率
	 * @param Begin
	 * @param End
	 * @param existTime
	 * @param holdTime
	 * @return
	 * @throws ParseException
	 */
	private double getTotalProfit(String Begin,String End,int existTime,int holdTime) throws ParseException{
		//确定持有期和形成期
		Date holdBegin = time.parse(Begin);
		Date holdEnd = getDateAfterDays(holdBegin,holdTime);
		Date existEnd = time.parse(Begin);
		Date existBegin = getDateAfterDays(existEnd,-existTime);
		int remainday = (int)((time.parse(End).getTime()-holdEnd.getTime())/(24*60*60*1000));
		
		//策略的累计收益率
		double  totalProfitOfStrategy = 1;
		//判断持有开始日期是否超过回测的结束日期
		while(holdBegin.before(time.parse(End))){
			getWinnerGroup(existBegin,existEnd,holdBegin,holdEnd);
			double pro = 0;
			for(int j=0;j<WinnerProfit.size();j++){
				pro += WinnerProfit.get(j)[0];
			}
			pro = pro/WinnerProfit.size();
			
			holdBegin = holdEnd;
			holdEnd = getDateAfterDays(holdBegin,Math.min(holdTime,remainday));
			existEnd = holdBegin;
			existBegin = getDateAfterDays(existEnd,-existTime);
			remainday = (int)((time.parse(End).getTime()-holdEnd.getTime())/(24*60*60*1000));
			if(!WinnerProfit.isEmpty())
				totalProfitOfStrategy *= (1 + pro);
		}
		return totalProfitOfStrategy;
	}
	/**
	 * 获取基准的每日收益率
	 * @param Begin
	 * @param End
	 * @return
	 */
	private ArrayList<Double> getBenchProfitEveryday(Date Begin,Date End){
		ArrayList<Double> BenchmarkProfit = new ArrayList<Double>();
		if(section==null){
			BenchmarkProfit = getSelfChosenBench(Begin,End);
		}
		else{
			ArrayList<BasePO> Benchmark = stockDataService.getBenchmarkByDate(section, time.format(Begin), time.format(End));
//			System.out.println(Benchmark.size()+"a");
			BenchmarkProfit = new ArrayList<Double>();
			
			for(int i=0;i<Benchmark.size();i++){
				double income = (Benchmark.get(Benchmark.size()-1).getAdjClose()-Benchmark.get(0).getAdjOpen())/Benchmark.get(0).getAdjOpen();
				BenchmarkProfit.add(income);
			}
		}
		return BenchmarkProfit;
	}
	/**
	 * 获得赢家组合在某段时间内的每日累计收益率
	 * @param Begin
	 * @param End
	 * @param holdTime
	 * @param existTime
	 * @return
	 */
	private ArrayList<Double> getWinnerProfitEveryday(Date Begin,Date End,int holdTime,int existTime){
		Date holdBegin = Begin;
		Date holdEnd = getDateAfterDays(holdBegin,holdTime);
		Date existEnd = Begin;
		Date existBegin = getDateAfterDays(existEnd,-existTime);
		ArrayList<Double> Profit = new ArrayList<Double>();
		double totalProfitOfStrategy = 1;
		int remainday = (int)((End.getTime()-holdEnd.getTime())/(24*60*60*1000));
		while(holdBegin.before(End)){
			getWinnerGroup(existBegin,existEnd,holdBegin,holdEnd);
			ArrayList<Double> WinnerProfit = getWinnerProfitEveryHoldDay();
			System.out.println(holdEnd);
			for(int i=0;i<WinnerProfit.size();i++){
				Profit.add(WinnerProfit.get(i)*totalProfitOfStrategy);
			}
			if(WinnerProfit.size()>0)
				totalProfitOfStrategy *=(1 + WinnerProfit.get(WinnerProfit.size()-1));
			//持有期结束,调整持有期和形成期
			holdBegin = holdEnd;
			holdEnd = getDateAfterDays(holdBegin,Math.min(holdTime,remainday));
			existEnd = holdBegin;
			existBegin = getDateAfterDays(existEnd,-existTime);
			remainday = (int)((End.getTime()-holdEnd.getTime())/(24*60*60*1000));
		}
		return Profit;
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
			UsersDao usersDao = new UsersDaoImpl();
			stockPool = usersDao.getSelfStockByUsername(userName);
		}
	}
//	private boolean isEqual(ArrayList<String> a1,ArrayList<String> a2){
//		if(a1.size()==a2.size()){
//			for(int i=0;i<a1.size();i++){
//				if(!a1.get(i).equals(a2.get(i))){
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//	
//	private boolean IsEqualsection(String last,String now){
//		if(last==null){
//			if(now==null){
//				return true;
//			}else{
//				return false;
//			}
//		}else{
//			if(now ==null){
//				return false;
//			}else{
//				if(last.equals(now)){
//					return true;
//				}
//				return false;
//			}
//		}
//	}
	
	@Override
	public MeanReversionVO getParameter() {
		MeanReversionVO m = new MeanReversionVO(yearRateOfReturn, benchmarkYearRateOfReturn, maximumRetracement, 
				alpha, beta, sharpeRatio);
		return m;
	}
	@Override
	public ArrayList<MeanReturnRateVO> getCalculationCycle() {
		ArrayList<MeanReturnRateVO> meanReturn = new ArrayList<>();
		int x = Math.min(calculationCycle.size(), excessIncome.size());
		int y = Math.min(x, winningPercentage.size());
		for(int i=0; i<y; i++){
			MeanReturnRateVO m = new MeanReturnRateVO();
			m.setCalculationCycle(calculationCycle.get(i));
			m.setExcessIncome(excessIncome.get(i));
			m.setWinningPercentage(winningPercentage.get(i));
			meanReturn.add(m);
		}
		return meanReturn;
	}
	@Override
	public DistributionHistogramVO getDistributionHistogram() {
		DistributionHistogramVO d = new DistributionHistogramVO(WinDay, loseDay, winPercentage);
		return d;
	}
}
