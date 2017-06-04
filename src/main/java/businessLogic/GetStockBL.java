package businessLogic;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.validator.annotations.DoubleRangeFieldValidator;

import businessLogicService.GetStockBLService;
import data.StockData;
import dataService.StockDataService;
import po.StockNameAndUpsPO;
import po.StockPO;
import po.UpsAndDownsPO;

public class GetStockBL implements GetStockBLService {

	int flag = 0;

	// 注入股票查询的Dao
	private StockDataService stockDataService;

	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}

	public ArrayList<String> getDate(String condition1, String condition2, String beginDate, String endDate) {

		ArrayList<String> date = new ArrayList<>();
		ArrayList<StockPO> stock1 = new ArrayList<>();
		ArrayList<StockPO> stock2 = new ArrayList<>();
		stock1 = getStockData(condition1, beginDate, endDate);
		stock2 = getStockData(condition2, beginDate, endDate);
		date = getSameDate(stock1, stock2);

		return date;
	}

	public Map<String, ArrayList<Double>> getVSData(String condition1, String condition2, String beginDate,
			String endDate) {

		Map<String, ArrayList<Double>> VSDatas = new HashMap<>();
		ArrayList<StockPO> stock1 = new ArrayList<>();
		ArrayList<StockPO> stock2 = new ArrayList<>();
		ArrayList<Double> newStock1 = new ArrayList<>();
		ArrayList<Double> newStock2 = new ArrayList<>();
		ArrayList<String> date = new ArrayList<>();
		stock1 = getStockData(condition1, beginDate, endDate);
		stock2 = getStockData(condition2, beginDate, endDate);
		date = getDate(condition1, condition2, beginDate, endDate);

		int day = 0;
		for (int i = stock1.size() - 1; i >= 0; i--) {
			if (stock1.get(i).getDate().equals(date.get(day))) {
				newStock1.add(stock1.get(i).getClose());
				day++;
			}
		}
		day = 0;
		for (int i = stock2.size() - 1; i >= 0; i--) {
			if (stock2.get(i).getDate().equals(date.get(day))) {
				newStock2.add(stock2.get(i).getClose());
				day++;
			}
		}
		// System.out.println(Integer.parseInt(stock1.get(0).getCode())+"
		// "+Integer.parseInt(stock2.get(0).getCode()));
		// System.out.println(stockDataService.getNameByCode(Integer.parseInt(stock1.get(0).getCode()))+"
		// "+stockDataService.getNameByCode(Integer.parseInt(stock2.get(0).getCode())));
		VSDatas.put(stockDataService.getNameByCode(Integer.parseInt(stock1.get(0).getCode())), newStock1);
		VSDatas.put(stockDataService.getNameByCode(Integer.parseInt(stock2.get(0).getCode())), newStock2);
		return VSDatas;
	}

	public ArrayList<String> getSameDate(ArrayList<StockPO> stock1, ArrayList<StockPO> stock2) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> date = new ArrayList<>();

		try {
			if (stock1.size() <= stock2.size()) {
				int j = stock1.size() - 1;
				for (int i = stock2.size() - 1; i >= 0; i--) {
					if (time.parse(stock1.get(j).getDate()).equals(time.parse(stock2.get(i).getDate()))) {
						date.add(stock1.get(j).getDate());
						j--;
					} else if (time.parse(stock1.get(j).getDate()).before(time.parse(stock2.get(i).getDate()))) {
						j--;
					} else {
						continue;
					}
				}
			} else {
				getSameDate(stock2, stock1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 根据输入情况搜索数据
	 * 
	 * @param condition
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<StockPO> getStockData(String condition, String begin, String end) {
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
	 */
	public UpsAndDownsPO getDistributionOfUpsAndDowns(String Date) {
		double[] changes = { -0.100000, -0.080000, -0.060000, -0.040000, -0.020000, 0, 0.020000, 0.040000, 0.060000,
				0.080000, 0.100000 };
		int[] distribution = new int[changes.length - 1];
		ArrayList<StockNameAndUpsPO> ups_Max = new ArrayList<StockNameAndUpsPO>();
		ArrayList<StockNameAndUpsPO> downs_Max = new ArrayList<StockNameAndUpsPO>();
		for (int i = 0; i < distribution.length; i++) {
			distribution[i] = 0;
		}
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 设置几位小数
		df.setMaximumFractionDigits(6);
		// 设置舍入模式
		df.setRoundingMode(RoundingMode.HALF_UP);
		ArrayList<StockPO> stockList = stockDataService.getStockByDate(Date);
		// System.out.println(stockList.size());
		for (StockPO po : stockList) {
			if (Double.parseDouble(po.getVolume()) != 0.0) {
				// System.out.println("A");
				Date yesterday = getDayBefore(Date, Integer.parseInt(po.getCode()));
				// System.out.println("B");
				StockPO po_1 = stockDataService
						.getStockByOneDay(stockDataService.getNameByCode(Integer.parseInt(po.getCode())), yesterday);
				// System.out.println("C");
				double upsAndDowns = Double
						.valueOf(df.format((po.getAdjClose() - po_1.getAdjClose()) / (po_1.getAdjClose())));

				for (int i = 0; i < distribution.length; i++) {
					if (upsAndDowns >= changes[i] && upsAndDowns <= changes[i + 1]) {
						distribution[i]++;
						break;
					}
				}
				StockNameAndUpsPO namePO = new StockNameAndUpsPO(
						stockDataService.getNameByCode(Integer.parseInt(po.getCode())), upsAndDowns);
				if (upsAndDowns >= 0) {
					if (ups_Max.size() > 0) {
						for (int i = 0; i < ups_Max.size(); i++) {
							if (namePO.getUps() >= ups_Max.get(i).getUps()) {
								ups_Max.add(i, namePO);
								if (ups_Max.size() > 5) {
									ups_Max.remove(5);
								}
								break;
							}
						}
					} else {
						ups_Max.add(namePO);
					}
				} else {
					if (downs_Max.size() > 0) {
						for (int i = 0; i < downs_Max.size(); i++) {
							if (namePO.getUps() <= downs_Max.get(i).getUps()) {
								downs_Max.add(i, namePO);
								if (downs_Max.size() > 5)
									downs_Max.remove(5);
								break;
							}
						}
					} else {
						downs_Max.add(namePO);
					}
				}
			}
		}
//		System.out.println("D");
//		System.out.println(ups_Max.size());
		UpsAndDownsPO po = new UpsAndDownsPO(distribution, ups_Max, downs_Max);
		return po;
	}

	/**
	 * 获得当前股票的前一交易日
	 * 
	 * @param today
	 * @return
	 */
	private Date getDayBefore(String today, int code) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		String yesterday = today;
		Date dayBefore = new Date();
		double volume;
		try {
			do {
				dayBefore = time.parse(yesterday);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dayBefore);
				cal.add(Calendar.DATE, -1);
				yesterday = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				dayBefore = time.parse(yesterday);
				volume = stockDataService.getVolumeByDateAndCode(code, yesterday);
				// System.out.println(volume);
			} while (volume == 0.0);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println(dayBefore);
		return dayBefore;
	}
	
	public ArrayList<StockPO> getStockMessage(){
		return stockDataService.getStockByDate("2017-05-25");
	}
	
	public String getNameByCode(String code){
		return stockDataService.getNameByCode(Integer.valueOf(code));
	}
	
	public ArrayList<StockPO> getLastStockByCode(String code){
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("getLastStockByCode--------enter---------");
		String date = time.format(getDayBefore("2017-05-25", Integer.valueOf(code)));
		System.out.println("getLastStockByCode--------end-------");
		return stockDataService.getStockByCodeAndDate(Integer.valueOf(code), date, date);
	}

	public static void main(String[] args) {
		GetStockBL bl = new GetStockBL();
		StockDataService stockDataService = new StockData();
		bl.setStockDataService(stockDataService);
		UpsAndDownsPO a = bl.getDistributionOfUpsAndDowns("2011-05-11");
		for (int i = 0; i < 5; i++)
			System.out.println(a.getDowns_Max().get(i).getName() + ' ' + a.getDowns_Max().get(i).getUps());

	}
}
