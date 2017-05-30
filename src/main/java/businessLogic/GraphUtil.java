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

	StockDataService sds = new StockData();
	
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
	
	/**
	 * 获得指定日期的上一个有效日期
	 */
	public String GetOrigin(String Code,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
//		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		String Origin = Begin;
		int volume;
		try {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				int code = Integer.parseInt(Code);
				volume = sds.JudgeIfTheLast(code, Origin);
			} while (volume == 0);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
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
