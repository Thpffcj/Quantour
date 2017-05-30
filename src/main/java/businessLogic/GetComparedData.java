package businessLogic;

import java.util.Date;

import org.jfree.data.category.DefaultCategoryDataset;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import businessLogicService.GetComparedDataService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class GetComparedData implements GetComparedDataService {

	/**
	 * 得到两个股票的每天最低值数据集
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 最低值数据集
	 */
	public DefaultCategoryDataset getLowestValue(String Name1, String Name2, String Begin, String End) {
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			stockList1 = sds.getStockByCodeAndDate(Code1, Begin, End);
		} else {
			stockList1 = sds.getStockByNameAndDate(Name1, Begin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			stockList2 = sds.getStockByCodeAndDate(Code2, Begin, End);
		} else {
			stockList2 = sds.getStockByNameAndDate(Name2, Begin, End);
		}

		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		for (int i = stockList1.size() - 1; i >= 0; i--) {
			sto1 = stockList1.get(i);
			sto2 = stockList2.get(i);

			double low1 = sto1.getLow();
			double low2 = sto2.getLow();
			String date = sto1.getDate();

			dataset.addValue(low1, sto1.getName(), date);
			dataset.addValue(low2, sto2.getName(), date);
			// System.out.println(low1+" "+sto1.getName()+" "+date);
			// System.out.println(low2+" "+sto2.getName()+" "+date);
		}

		return dataset;
	}

	/**
	 * 得到两个股票在一段时间内的最低值
	 * 
	 * @param Name1
	 * @param Name2
	 * @param Begin
	 * @param End
	 * @return 两个股票在一段时间内的最低值
	 */
	public String GetLowest(String Name1, String Name2, String Begin, String End) {
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			stockList1 = sds.getStockByCodeAndDate(Code1, Begin, End);
		} else {
			stockList1 = sds.getStockByNameAndDate(Name1, Begin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			stockList2 = sds.getStockByCodeAndDate(Code2, Begin, End);
		} else {
			stockList2 = sds.getStockByNameAndDate(Name2, Begin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		double lowest1 = 1000;
		double lowest2 = 1000;
		for (int i = stockList1.size() - 1; i >= 0; i--) {
			StockPO sto1 = stockList1.get(i);
			StockPO sto2 = stockList2.get(i);

			double low1 = sto1.getLow();
			double low2 = sto2.getLow();
			if (low1 < lowest1) {
				lowest1 = low1;
			}
			if (low2 < lowest2) {
				lowest2 = low2;
			}
		}
		return lowest1 + "    " + lowest2;
	}

	/**
	 * 得到两个股票的每天最高值数据集
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 最高值数据集
	 */
	public DefaultCategoryDataset getHighestValue(String Name1, String Name2, String Begin, String End) {
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			stockList1 = sds.getStockByCodeAndDate(Code1, Begin, End);
		} else {
			stockList1 = sds.getStockByNameAndDate(Name1, Begin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			stockList2 = sds.getStockByCodeAndDate(Code2, Begin, End);
		} else {
			stockList2 = sds.getStockByNameAndDate(Name2, Begin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}

		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// int dateNums = stockList1.size();
		// int displayNums = 8;
		// int interval = 1;
		// if(dateNums>displayNums){
		// interval= dateNums/displayNums;
		//
		// }

		for (int i = stockList1.size() - 1; i >= 0; i--) {
			sto1 = stockList1.get(i);
			sto2 = stockList2.get(i);

			double high1 = sto1.getHigh();
			double high2 = sto2.getHigh();
			String date = sto1.getDate();

			dataset.addValue(high1, sto1.getName(), date);
			dataset.addValue(high2, sto2.getName(), date);
		}

		return dataset;
	}

	/**
	 * 得到两个股票在一段时间内的最高值
	 * 
	 * @param Name1
	 * @param Name2
	 * @param Begin
	 * @param End
	 * @return 两个股票在一段时间内的最高值
	 */
	public String GetHighest(String Name1, String Name2, String Begin, String End) {
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			stockList1 = sds.getStockByCodeAndDate(Code1, Begin, End);
		} else {
			stockList1 = sds.getStockByNameAndDate(Name1, Begin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			stockList2 = sds.getStockByCodeAndDate(Code2, Begin, End);
		} else {
			stockList2 = sds.getStockByNameAndDate(Name2, Begin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		double highest1 = 0;
		double highest2 = 0;
		for (int i = stockList1.size() - 1; i >= 0; i--) {
			StockPO sto1 = stockList1.get(i);
			StockPO sto2 = stockList2.get(i);

			double high1 = sto1.getHigh();
			double high2 = sto2.getHigh();
			if (high1 > highest1) {
				highest1 = high1;
			}
			if (high2 > highest2) {
				highest2 = high2;
			}
		}
		return highest1 + "    " + highest2;
	}

	/**
	 * 得到每天涨幅/跌幅数据集
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 涨幅/跌幅数据集
	 */
	public DefaultCategoryDataset getRoseAndDropValue(String Name1, String Name2, String Begin, String End) {
		String Origin;

		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			Origin = GetOrigin(Code1,Begin);
			stockList1 = sds.getStockByCodeAndDate(Code1, Origin, End);
		} else {
			Origin = GetOrigin(Name1,Begin);
			stockList1 = sds.getStockByNameAndDate(Name1, Origin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			Origin = GetOrigin(Code2,Begin);
			stockList2 = sds.getStockByCodeAndDate(Code2, Origin, End);
		} else {
			Origin = GetOrigin(Name2,Begin);
			stockList2 = sds.getStockByNameAndDate(Name2, Origin, End);
		}

		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 设置几位小数
		df.setMaximumFractionDigits(6);
		// 设置舍入模式
		df.setRoundingMode(RoundingMode.HALF_UP);

		for (int i = stockList1.size() - 2; i >= 0; i--) {
			sto1 = stockList1.get(i + 1);
			sto2 = stockList1.get(i);
			double change1 = (sto2.getAdjClose() - sto1.getAdjClose()) / sto1.getAdjClose() * 100;
			change1 = Double.valueOf(df.format(change1));
			dataset.addValue(change1, sto2.getName(), sto2.getDate());

			sto1 = stockList2.get(i + 1);
			sto2 = stockList2.get(i);
			double change2 = (sto2.getAdjClose() - sto1.getAdjClose()) / sto1.getAdjClose() * 100;
			change2 = Double.valueOf(df.format(change2));
			dataset.addValue(change2, sto2.getName(), sto2.getDate());
		}

		return dataset;
	}

	/**
	 * 得到两个股票在一段时间内的涨幅/跌幅
	 * 
	 * @param Name1
	 * @param Name2
	 * @param Begin
	 * @param End
	 * @return 两个股票在一段时间内的涨幅/跌幅
	 */
	public String getRoseAndDrop(String Name1, String Name2, String Begin, String End) {
		String Origin;

		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			Origin = GetOrigin(Code1,Begin);
			stockList1 = sds.getStockByCodeAndDate(Code1, Origin, End);
		} else {
			Origin = GetOrigin(Name1,Begin);
			stockList1 = sds.getStockByNameAndDate(Name1, Origin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			Origin = GetOrigin(Code2,Begin);
			stockList2 = sds.getStockByCodeAndDate(Code2, Origin, End);
		} else {
			Origin = GetOrigin(Name2,Begin);
			stockList2 = sds.getStockByNameAndDate(Name2, Origin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 设置几位小数
		df.setMaximumFractionDigits(6);
		// 设置舍入模式
		df.setRoundingMode(RoundingMode.HALF_UP);
		
		StockPO sto1 = stockList1.get(0);
		StockPO sto2 = stockList1.get(stockList1.size()-1);
		double change1 = (sto1.getAdjClose() - sto2.getAdjClose()) / sto2.getAdjClose() * 100;
		change1 = Double.valueOf(df.format(change1));
		
		sto1 = stockList2.get(0);
		sto2 = stockList2.get(stockList2.size()-1);
		double change2 = (sto1.getAdjClose() - sto2.getAdjClose()) / sto2.getAdjClose() * 100;
		change2 = Double.valueOf(df.format(change2));
		return change1+"    "+change2;
	}

	/**
	 * 得到收盘价数据集
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 收盘价数据集
	 */
	public DefaultCategoryDataset getCloseValue(String Name1, String Name2, String Begin, String End) {
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			stockList1 = sds.getStockByCodeAndDate(Code1, Begin, End);
		} else {
			stockList1 = sds.getStockByNameAndDate(Name1, Begin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			stockList2 = sds.getStockByCodeAndDate(Code2, Begin, End);
		} else {
			stockList2 = sds.getStockByNameAndDate(Name2, Begin, End);
		}

		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}
		
		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = stockList1.size() - 1; i >= 0; i--) {
			sto1 = stockList1.get(i);
			sto2 = stockList2.get(i);

			double close1 = sto1.getClose();
			double close2 = sto2.getClose();
			String date = sto1.getDate();

			dataset.addValue(close1, sto1.getName(), date);
			dataset.addValue(close2, sto2.getName(), date);

		}

		return dataset;
	}

	/**
	 * 得到两个股票的对数收益率数据集
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 两个股票的对数收益率数据集
	 */
	public DefaultCategoryDataset getRateOfReturn(String Name1, String Name2, String Begin, String End) {
		String Origin;
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			Origin = GetOrigin(Code1,Begin);
			stockList1 = sds.getStockByCodeAndDate(Code1, Origin, End);
		} else {
			Origin = GetOrigin(Name1,Begin);
			stockList1 = sds.getStockByNameAndDate(Name1, Origin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			Origin = GetOrigin(Code2,Begin);
			stockList2 = sds.getStockByCodeAndDate(Code2, Origin, End);
		} else {
			Origin = GetOrigin(Name2,Begin);
			stockList2 = sds.getStockByNameAndDate(Name2, Origin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}

		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 设置几位小数
		df.setMaximumFractionDigits(6);
		// 设置舍入模式
		df.setRoundingMode(RoundingMode.HALF_UP);

		for (int i = stockList1.size() - 2; i >= 0; i--) {
			sto1 = stockList1.get(i + 1);
			sto2 = stockList1.get(i);
			double return1 = Math.log(sto2.getAdjClose() / sto1.getAdjClose()) * 100;
			return1 = Double.valueOf(df.format(return1));
			dataset.addValue(return1, sto2.getName(), sto2.getDate());

			sto1 = stockList2.get(i + 1);
			sto2 = stockList2.get(i);
			double return2 = Math.log(sto2.getAdjClose() / sto1.getAdjClose()) * 100;
			return2 = Double.valueOf(df.format(return2));
			dataset.addValue(return2, sto2.getName(), sto2.getDate());
		}

		return dataset;
	}

	/**
	 * 得到两个股票的对数收益率的方差
	 * 
	 * @param String
	 *            Name1
	 * @param String
	 *            Name2
	 * @param String
	 *            Begin
	 * @param String
	 *            End
	 * @return 两个股票的对数收益率方差结果
	 */
	public String getVariance(String Name1, String Name2, String Begin, String End) {
		String Origin;
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList1 = new ArrayList<StockPO>();
		ArrayList<StockPO> stockList2 = new ArrayList<StockPO>();

		if (ISNumber(Name1)) {
			int Code1 = Integer.valueOf(Name1);
			Origin = GetOrigin(Code1,Begin);
			stockList1 = sds.getStockByCodeAndDate(Code1, Origin, End);
		} else {
			Origin = GetOrigin(Name1,Begin);
			stockList1 = sds.getStockByNameAndDate(Name1, Origin, End);
		}

		if (ISNumber(Name2)) {
			int Code2 = Integer.valueOf(Name2);
			Origin = GetOrigin(Code2,Begin);
			stockList2 = sds.getStockByCodeAndDate(Code2, Origin, End);
		} else {
			Origin = GetOrigin(Name2,Begin);
			stockList2 = sds.getStockByNameAndDate(Name2, Origin, End);
		}
		
		if(stockList1.size()==0||stockList2.size()==0||stockList1.size()!=stockList2.size()){
			return null;
		}

		StockPO sto1 = new StockPO();
		StockPO sto2 = new StockPO();

		ArrayList<Double> return1 = new ArrayList<Double>();
		ArrayList<Double> return2 = new ArrayList<Double>();

		for (int i = stockList1.size() - 2; i >= 0; i--) {
			sto1 = stockList1.get(i + 1);
			sto2 = stockList1.get(i);
			double result1 = Math.log(sto2.getAdjClose() / sto1.getAdjClose()) * 100;
			return1.add(result1);

			sto1 = stockList2.get(i + 1);
			sto2 = stockList2.get(i);
			double result2 = Math.log(sto2.getAdjClose() / sto1.getAdjClose()) * 100;
			return2.add(result2);
		}

		String Variance = calculateVariance(return1) + "    " + calculateVariance(return2);

		return Variance;
	}

	// 计算方差的方法
	public String calculateVariance(ArrayList<Double> ret) {
		double average;
		double sum1 = 0;
		double sum2 = 0;
		double variance;

		for (int i = 0; i < ret.size(); i++) {
			sum1 = sum1 + ret.get(i);
		}
		average = sum1 / (ret.size() - 1);

		for (int i = 0; i < ret.size(); i++) {
			sum2 = sum2 + (ret.get(i) - average) * (ret.get(i) - average);
		}
		variance = sum2 / (ret.size() - 1);

		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 设置几位小数
		df.setMaximumFractionDigits(6);
		// 设置舍入模式
		df.setRoundingMode(RoundingMode.HALF_UP);
		String Var = df.format(variance);

		return Var;
	}

	/**
	 * 判断一个字符串是否为数字
	 * 
	 * @param s
	 * @return 结果
	 */
	public boolean ISNumber(String s) {
		try {
			int n = Integer.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 得到上一个有效工作日
	 * 
	 * @param Begin
	 * @return 上一个有效工作日字符串表示
	 */
	public String GetOrigin(int Code,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		String Origin = Begin;
		int volume;
		try {

			// SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			// String week = sdf.format(origin);
			//
			// if (week.equals("星期一")) {
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(origin);
			// cal.add(Calendar.DATE, -3);
			// Origin = (new
			// SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				volume = sds.getVolumeByDateAndCode(Code, Origin);
			} while (volume == 0);

			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}

	public String GetOrigin(String Name,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		String Origin = Begin;
		int volume;
		try {

			// SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			// String week = sdf.format(origin);
			//
			// if (week.equals("星期一")) {
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(origin);
			// cal.add(Calendar.DATE, -3);
			// Origin = (new
			// SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				volume = sds.getVolumeByDateAndName(Name, Origin);
			} while (volume == 0);

			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}
	// public static void main(String[] args) {
	// GetComparedData getComparedData = new GetComparedData();
	// String aString = getComparedData.getVariance("1", "151", "1/20/14",
	// "4/29/14");
	// String aString = getComparedData.GetOrigin("04/07/14");
	// System.out.println(aString);
	// DefaultCategoryDataset dataset1 =getComparedData.getLowestValue("1",
	// "10", "4/4/14", "4/10/14");
	//
	//
	// DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
	//
	// dataset2.addValue(10.59,"深发展A","4/4/14");
	// dataset2.addValue(6.28,"S ST华新","4/4/14");
	// dataset2.addValue(10.78,"深发展A","4/8/14");
	// dataset2.addValue(6.33,"S ST华新","4/8/14");
	// dataset2.addValue(11.16,"深发展A","4/9/14");
	// dataset2.addValue(6.37,"S ST华新","4/9/14");
	// dataset2.addValue(11.16,"深发展A","4/10/14");
	// dataset2.addValue(6.4,"S ST华新","4/10/14");
	// System.out.println(dataset1);
	// System.out.println(dataset2);
	// }
}