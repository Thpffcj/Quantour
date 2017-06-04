package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import dataService.StockDataService;
import po.StockPO;

public class ScreeningSuspendedStock{

	private SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
	StockDataService sds = new StockData();
	
	public ArrayList<String> noSuspendedStock(ArrayList<String> stockPool, String begin, String end){
		ArrayList<String> noSuspend = new ArrayList<>();
		ArrayList<String> Days = sds.getDate(begin, end);
		int dayLong = Days.size();
		for(int i=0; i<stockPool.size(); i++){
			ArrayList<StockPO> stockList = getStockData(stockPool.get(i), begin, end);
			if(stockList == null || stockList.size() != dayLong){
				continue;
			}
			String code = stockList.get(i).getCode();
			noSuspend.add(sds.getNameByCode(Integer.parseInt(code)));
		}
		return noSuspend;
	}
	
	public Map<Integer, ArrayList<String>> getDuringNoSuspendedStock(ArrayList<String> stockPool, String begin, String end, int formingPeriod) throws ParseException{
		Map<Integer, ArrayList<String>> noSuspendedStock = new HashMap<>();
		
		ArrayList<String> days = sds.getDate(begin, end);
		int dayLong = days.size();
		
		ArrayList<String> noSuspend = new ArrayList<>();
	
		Date endTime = time.parse(end);
		
		int period = 0;
		period = dayLong/formingPeriod;
		
		Date holdBegin = null;
		Date holdEnd = time.parse(begin);
		for(int i=0; i<period; i++){
			holdBegin = holdEnd;
			holdEnd = getDateAfterDays(holdBegin, formingPeriod);
			noSuspend = noSuspendedStock(stockPool, time.format(holdBegin), time.format(holdEnd));
			noSuspendedStock.put(i, noSuspend);
		}
		noSuspend = noSuspendedStock(stockPool, time.format(holdBegin), time.format(endTime));
		noSuspendedStock.put(period, noSuspend);
		
		return noSuspendedStock;
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
			stockList = sds.getStockByCodeAndDate(code, begin, end);
		} else {
			String name = condition;
			stockList = sds.getStockByNameAndDate(name, begin, end);
		}
		return stockList;
	}
}
