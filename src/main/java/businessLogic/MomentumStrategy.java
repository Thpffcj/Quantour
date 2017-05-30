package businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hamcrest.core.IsEqual;
import org.jfree.data.category.DefaultCategoryDataset;

import businessLogicService.MomentumStrategyService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;
import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;
import vo.quantify.MeanReversionVO;

public class MomentumStrategy implements MomentumStrategyService{
	private ArrayList<String> stockPool;
	private String section;
	private StockDataService sds= new StockData();
	private SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
	private ArrayList<Double[]> WinnerProfit = new ArrayList<Double[]>();
	//保存上次回测的数据
	private static DefaultCategoryDataset MStrategyComparedGraph = null;
	private static DefaultCategoryDataset MStrategyExtraProfitGraph = null;
	private static DefaultCategoryDataset MStrategyWinningGraph = null;
	private static DefaultCategoryDataset MStrategyYieldGraph = null;
	private static String lastSection = " "; 
	private static ArrayList<String> lastStockPool = new ArrayList<String>(); 
	private static int lastHoldTime = 0; 
	private static int lastExistTime = 0; 
	private static String lastBegin = " ";
	private static String lastEnd = " ";
	private static String lastBegin2 = " ";
	private static String lastEnd2 = " ";
	private static boolean lastisHold = false;
	private static int lastTime = 0;
	
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
	
	public MomentumStrategy(ArrayList<String> stockPool,String section){
		this.stockPool = stockPool;
		this.section = section;
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
	public DefaultCategoryDataset getMStrategyComparedGraph(String Begin,String End,int existTime,int holdTime) throws ParseException {
		if(this.MStrategyComparedGraph!=null&&Begin.equals(this.lastBegin)&&End.equals(this.lastEnd)
				&&existTime==this.lastExistTime&&holdTime==this.lastHoldTime
				&&isEqual(this.lastStockPool,stockPool)&&IsEqualsection(section, lastSection)){
			return this.MStrategyComparedGraph;
		}else{
			MStrategyYieldGraph = null;
			MStrategyExtraProfitGraph = null;
			MStrategyWinningGraph = null;
		}
		DefaultCategoryDataset Compared = new DefaultCategoryDataset();
		//作图线的名字
		String strategy = "策略收益率";
		String benchmark = "基准收益率";
		//策略和基准的累计收益率
		ArrayList<String> Days = sds.getDate(Begin, End);
		ArrayList<Double> StrategyProfit = getWinnerProfitEveryday(time.parse(Begin),time.parse(End),holdTime,existTime);
		ArrayList<Double> BenchmarkProfit = getBenchProfitEveryday(time.parse(Begin),time.parse(End));
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
		this.sharpeRatio = p.getSharpeRatio(StrategyProfit);
		//作图
		for(int j=Math.min(Math.min(Days.size(), BenchmarkProfit.size()), StrategyProfit.size())-1;j>=0;j--){
			Compared.addValue(BenchmarkProfit.get(j), benchmark, Days.get(j));
			Compared.addValue(StrategyProfit.get(j), strategy, Days.get(j));
		}
		
		this.lastBegin=Begin;
		this.lastEnd=End;
		this.lastExistTime=existTime;
		this.lastHoldTime=holdTime;
		this.lastStockPool=stockPool;
		this.lastSection=section;
		this.MStrategyComparedGraph = Compared;
		
		return Compared;
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
	public DefaultCategoryDataset getMStrategyExtraProfitGraph(boolean isHold,int Time,String Begin,String End) throws ParseException {
		if(this.MStrategyExtraProfitGraph!=null&&Begin.equals(this.lastBegin2)&&End.equals(this.lastEnd2)
				&&isHold==this.lastisHold&&Time==this.lastTime
				&&isEqual(this.lastStockPool,stockPool)&&IsEqualsection(section, lastSection)){
			return this.MStrategyExtraProfitGraph;
		}
		DefaultCategoryDataset ExtraProfit = new DefaultCategoryDataset();
		String series = "额外收益率";
		double normalProfit = 0;
		ArrayList<String> Days = sds.getDate(Begin, End);
		int daylong = Days.size();
		
		if(section==null){
			int num = 0;
			for(int i=0;i<stockPool.size();i++){
				ArrayList<StockPO> stock=sds.getStockByNameAndDate(stockPool.get(i), Begin, End);
				
				if(stock.size()>=daylong){
					num++;
					normalProfit += (stock.get(0).getAdjClose()-stock.get(stock.size()-1).getOpen())/stock.get(stock.size()-1).getOpen();
				}
			}
			normalProfit = normalProfit/num;
		}
		else{
			ArrayList<Double> BenchmarkOpen = sds.getStockOpenBySection(section, Begin, End);
			ArrayList<Double> BenchmarkAdj = sds.getStockAdjCloseBySection(section, Begin, End);
			normalProfit = (BenchmarkAdj.get(0)-BenchmarkOpen.get(BenchmarkAdj.size()-1))/BenchmarkOpen.get(BenchmarkAdj.size()-1);	
		}
		//若为真，持有期固定
		if(isHold){
			for(int i=10;i<121;i+=10){
				double totalProfit = getTotalProfit(Begin,End,i,Time);
				ExtraProfit.addValue(totalProfit-normalProfit, series, (Integer)i);
				DecimalFormat df = new DecimalFormat("0.00%");
				this.calculationCycle.add(i);
				this.excessIncome.add(df.format(totalProfit-normalProfit));
			}
		}
		//若为假，形成期固定
		else{
			for(int i=Days.size()/10;i<=Days.size();i+=Days.size()/10){
				double totalProfit = getTotalProfit(Begin,End,Time,i);
				ExtraProfit.addValue(totalProfit-normalProfit, series, (Integer)i);
				DecimalFormat df = new DecimalFormat("0.00%");
				this.calculationCycle.add(i);
				this.excessIncome.add(df.format(totalProfit-normalProfit));
			}
		}
		this.lastBegin2=Begin;
		this.lastEnd2=End;
		this.lastisHold=isHold;
		this.lastTime=Time;
		this.lastStockPool=stockPool;
		this.lastSection=section;
		this.MStrategyExtraProfitGraph = ExtraProfit;
		
		return ExtraProfit;
	}

	/**
	 * 策略胜率与不同持有期/形成期的关系图
	 */
	public DefaultCategoryDataset getMStrategyWinningGraph(boolean isHold, int Time, String Begin, String End) throws ParseException {
		if(this.MStrategyWinningGraph!=null&&Begin.equals(this.lastBegin2)&&End.equals(this.lastEnd2)
				&&isHold==this.lastisHold&&Time==this.lastTime
				&&isEqual(this.lastStockPool,stockPool)&&IsEqualsection(section, lastSection)){
			return this.MStrategyWinningGraph;
		}
		DefaultCategoryDataset Winning = new DefaultCategoryDataset();
		String series = "策略胜率曲线";
		Date begin = time.parse(Begin);
		Date end = time.parse(End);
		
		ArrayList<String> Days = sds.getDate(Begin, End);
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
				Winning.addValue((double)(WinDay*1.0/Days.size()), series, (Integer)i);
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
				Winning.addValue((double)(WinDay*1.0/Days.size()), series, (Integer)i);
				winningPercentage.add(df.format((double)(WinDay*1.0/Days.size())));
			}
		}
		this.lastBegin2=Begin;
		this.lastEnd2=End;
		this.lastisHold=isHold;
		this.lastTime=Time;
		this.lastStockPool=stockPool;
		this.lastSection=section;
		this.MStrategyWinningGraph = Winning;
		return Winning;
	}
	/**
	 * 收益率频率分布直方图
	 */
	public DefaultCategoryDataset getMStrategyYieldGraph(String Begin,String End,int holdTime,int existTime) throws ParseException{
		if(this.MStrategyYieldGraph!=null&&Begin.equals(this.lastBegin)&&End.equals(this.lastEnd)
				&&existTime==this.lastExistTime&&holdTime==this.lastHoldTime
				&&isEqual(this.lastStockPool,stockPool)&&IsEqualsection(section, lastSection)){
			return this.MStrategyYieldGraph;
		}
		DefaultCategoryDataset Yield = new DefaultCategoryDataset();
		String series1 = "正收益次数";
		String series2 = "负收益次数";
		Date begin = time.parse(Begin);
		Date end = time.parse(End);
		
		ArrayList<Double> WinnerProfit = getWinnerProfitEveryday(begin,end,holdTime,existTime);
		ArrayList<Double> BenchmarkProfit = getBenchProfitEveryday(begin,end);
		ArrayList<Double> ExtraProfit = new ArrayList<Double>();
		
		for(int i=0;i<Math.min(WinnerProfit.size(), BenchmarkProfit.size());i++){
			double Profit = WinnerProfit.get(i)-BenchmarkProfit.get(i);
			BigDecimal bg = new BigDecimal(Profit).setScale(2, RoundingMode.UP);
			Profit = bg.doubleValue();		
			ExtraProfit.add(Profit);
		}
		
		while(!ExtraProfit.isEmpty()){
			int num = 0;
			double Profit = ExtraProfit.get(0);
			for(double d:ExtraProfit){
				if(d==Profit)
					num++;
			}
			if(Profit>0){
				Yield.addValue(num, series1, (Double)Profit);
				WinDay += num;
			}
			else{
				Yield.addValue(num, series2, (Double)Math.abs(Profit));
				loseDay += num;
			}
			while(ExtraProfit.contains(Profit)){
				ExtraProfit.remove(Profit);
			}
		}
		System.out.println(WinDay+" "+loseDay);
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		winPercentage = Double.parseDouble(numberFormat.format((1.0*WinDay/(WinDay+loseDay))));	
		this.lastBegin=Begin;
		this.lastEnd=End;
		this.lastExistTime=existTime;
		this.lastHoldTime=holdTime;
		this.lastStockPool=stockPool;
		this.lastSection=section;
		this.MStrategyYieldGraph = Yield;
		return Yield;
		
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
		int daylong = sds.getDate(time.format(existBegin), time.format(holdEnd)).size();  
		int holddaylong = sds.getDate(time.format(holdBegin), time.format(holdEnd)).size(); 
		
//		System.out.println(daylong);
//		System.out.println(holddaylong);
		//判断股票在这段时间内是否有停牌
		for(int i=0;i<stockPool.size();i++){
			//获得该股票从生成期到持有期结束的所有
			ArrayList<StockPO> stock = sds.getStockByNameAndDate(stockPool.get(i), time.format(existBegin), time.format(holdEnd));
			System.out.println(time.format(existBegin)+" "+time.format(holdEnd));
			System.out.println(stockPool.get(i)+"  " + stock.size());
			if(stock.size()>=daylong&&holddaylong>0){
				Double[] hp = new Double[holddaylong];
				for(int j=0;j<holddaylong;j++){
					hp[j] = (stock.get(j).getAdjClose()-stock.get(holddaylong-1).getOpen())/stock.get(holddaylong-1).getOpen();
				}
				double ep = (stock.get(holddaylong-1).getAdjClose()-stock.get(stock.size()-1).getOpen())/stock.get(stock.size()-1).getOpen();
				existProfit.add(ep);
				holdProfit.add(hp);
			}
			
		}
//		System.out.println(existProfit.size());
//		System.out.println(holdProfit.size());
		//筛选赢家组合,使用冒泡排序
		if(!existProfit.isEmpty()){
			for(int i=0;i<WinnerNum;i++){
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
		
		int daylong = sds.getDate(time.format(Begin), time.format(End)).size(); 
		for(int i=0;i<stockPool.size();i++){
			ArrayList<StockPO> stock=sds.getStockByNameAndDate(stockPool.get(i), time.format(Begin), time.format(End));
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
			ArrayList<Double> BenchmarkOpen = sds.getStockOpenBySection(section, time.format(Begin), time.format(End));
			ArrayList<Double> BenchmarkAdj = sds.getStockAdjCloseBySection(section, time.format(Begin), time.format(End));
			BenchmarkProfit = new ArrayList<Double>();
			
			for(int i=0;i<BenchmarkAdj.size();i++){
				double income = (BenchmarkAdj.get(i)-BenchmarkOpen.get(0))/BenchmarkOpen.get(0);
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
//			System.out.println(holdBegin);
//			System.out.println(holdEnd);
//			System.out.println(existBegin);
//			System.out.println(existEnd);
			getWinnerGroup(existBegin,existEnd,holdBegin,holdEnd);
			ArrayList<Double> WinnerProfit = getWinnerProfitEveryHoldDay();
			
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
	
	private boolean IsEqualsection(String last,String now){
		if(last==null){
			if(now==null){
				return true;
			}else{
				return false;
			}
		}else{
			if(now ==null){
				return false;
			}else{
				if(last.equals(now)){
					return true;
				}
				return false;
			}
		}
	}
	
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
