package businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class GraphUtil {

	StockDataService stockDataService = new StockData();
	
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
	 * 获得缺失数据的日期，包括周末和停牌日期
	 */
	public ArrayList<Date> GetDateOfSuspension(String condition, String begin, String end) throws ParseException{
		
		ArrayList<Date> Illegaldate = new ArrayList<>();
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		stockList = getStockData(condition, begin, end);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		//获取当前时间毫秒数
		Date begin1 = dateFormat.parse(begin);
		Date end1 = dateFormat.parse(end);
		long time1 = begin1.getTime();
		long time2 = end1.getTime();
		int location = 0;
		//判断这天是否在获取的数据中，如果不在，为缺失数据
		for (long i = time2; i > time1; i -= 24 * 60 * 60 * 1000L) {
			long time3 = (dateFormat.parse(stockList.get(location).getDate())).getTime();
			if (i == time3) {
				//小于搜索范围内的数据量
				if(location<stockList.size()){
					location++;
				}else{
					break;
				}
			} else {
				Date day = new Date(i);
				Illegaldate.add(day);
			}
		}
		return Illegaldate;
	}
	
	public String dateTransform(String time){
		int day = 0;
		int month = 0;
		int year = 2000;
    	String[] time1 = time.split("/");
    	year = Integer.parseInt(time1[2]) + year;
    	month = Integer.parseInt(time1[0]);
    	day = Integer.parseInt(time1[1]);
    	String sday = "";
    	String smonth = "";
    	if(day<10){
    		sday = "0" + String.valueOf(day);
    	}
    	else{
    		sday = String.valueOf(day);
    	}
    	if(month<10){
    		smonth = "0" + String.valueOf(month);
    	}
    	else{
    		smonth = String.valueOf(month);
    	}
    	String tTime = String.valueOf(year)+"-"+smonth+"-"+sday;
    	return tTime;
	}
}
