package businessLogic;

import java.awt.Adjustable;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jni.Local;
import org.hibernate.id.IntegralDataTypeHolder;

import com.opensymphony.xwork2.validator.annotations.DoubleRangeFieldValidator;

import businessLogicService.GetStockBLService;
import data.StockData;
import dataService.StockDataService;
import po.BasePO;
import po.StockNameAndUpsPO;
import po.StockPO;
import po.UpsAndDownsPO;

public class GetStockBL implements GetStockBLService {

	// 注入股票查询的Dao
	private StockDataService stockDataService;

	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	private static ArrayList<String> codelist;
	
	public Map<String, ArrayList<String>> getVSdata(String code1, String code2, String begindate, String enddate) {
		Map<String, ArrayList<String>> vsdata = new HashMap<>();
		ArrayList<String> result = new ArrayList<>();
		if(begindate.equals("")||enddate.equals("")||code1.equals("")||code2.equals("")){
			result.add("null");
			vsdata.put("result", result);
			return vsdata;
		}
		try{
			LocalDate.parse(begindate);
			LocalDate.parse(enddate);
		}catch(Exception e){
			result.add("wrongdate");
			vsdata.put("result", result);
			return vsdata;
		}
		if(!IsCode(code1)){
			code1 = Tran(Integer.toString(stockDataService.getCodeByName(code1)));
		}
		if(code1.equals("000000")||!IsLegalCode(code1)){
			result.add("unknow");
			vsdata.put("result", result);
			return vsdata;
		}
		if(!IsCode(code2)){
			code2 = Tran(Integer.toString(stockDataService.getCodeByName(code2)));
		}
		if(code2.equals("000000")||!IsLegalCode(code2)){
			result.add("unknow");
			vsdata.put("result", result);
			return vsdata;
		}
		ArrayList<String> namelist = new ArrayList<>();
		namelist.add(stockDataService.getNameByCode(Integer.valueOf(code1)));
		namelist.add(stockDataService.getNameByCode(Integer.valueOf(code2)));
		
		ArrayList<StockPO> data1 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code1), begindate, enddate);
		ArrayList<StockPO> data2 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code2), begindate, enddate);
		int i = data1.size()-2;
		int j = data2.size()-2;
		ArrayList<String> datelist = new ArrayList<>();
		ArrayList<String> fluct1 = new ArrayList<>();
		ArrayList<String> fluct2 = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
		
		while(i>=0&&j>=0){
			LocalDate date1 = LocalDate.parse(data1.get(i).getDate());
			LocalDate date2 = LocalDate.parse(data2.get(j).getDate());
			System.out.println(date1+"---"+date2);
			if(date1.isEqual(date2)){
				datelist.add(date1.toString());
				fluct1.add(df.format((data1.get(i).getClose()-data1.get(i+1).getClose())/data1.get(i+1).getClose()*100));
				fluct2.add(df.format((data2.get(j).getClose()-data2.get(j+1).getClose())/data2.get(j+1).getClose()*100));
				i--;
				j--;
			}else if(date1.isBefore(date2)){
				System.out.println(date1);
				i--;
			}else{
				System.out.println(date2);
				j--;
			}
		}
		LocalDate Now = LocalDate.parse(enddate).plusDays(1);
		String minusweek = Now.minusDays(7).toString();
		String minusmonth = Now.minusDays(30).toString();
		String minusthreemonth = Now.minusDays(90).toString();
		String minushalfyear = Now.minusDays(180).toString();
		String minusyear = Now.minusDays(365).toString();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> pArrayList1 = new ArrayList<>();
		ArrayList<String> pArrayList2 = new ArrayList<>();
		
		pArrayList1.add(time.format(getDayBefore(Now.toString(), Integer.valueOf(code1))));
		pArrayList1.add(time.format(getDayBefore(minusweek, Integer.valueOf(code1))));
		pArrayList1.add(time.format(getDayBefore(minusmonth, Integer.valueOf(code1))));
		pArrayList1.add(time.format(getDayBefore(minusthreemonth, Integer.valueOf(code1))));
		pArrayList1.add(time.format(getDayBefore(minushalfyear, Integer.valueOf(code1))));
		pArrayList1.add(time.format(getDayBefore(minusyear, Integer.valueOf(code1))));
		
		pArrayList2.add(time.format(getDayBefore(Now.toString(), Integer.valueOf(code2))));
		pArrayList2.add(time.format(getDayBefore(minusweek, Integer.valueOf(code2))));
		pArrayList2.add(time.format(getDayBefore(minusmonth, Integer.valueOf(code2))));
		pArrayList2.add(time.format(getDayBefore(minusthreemonth, Integer.valueOf(code2))));
		pArrayList2.add(time.format(getDayBefore(minushalfyear, Integer.valueOf(code2))));
		pArrayList2.add(time.format(getDayBefore(minusyear, Integer.valueOf(code2))));
		
		ArrayList<String> parameter1 = new ArrayList<>();
		ArrayList<String> parameter2 = new ArrayList<>();
		
		ArrayList<StockPO> Nowmessage1 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code1), pArrayList1.get(0), pArrayList1.get(0));
		ArrayList<StockPO> Nowmessage2 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code2), pArrayList2.get(0), pArrayList2.get(0));
		
		for(int k=1;k<6;k++){
			ArrayList<StockPO> po1 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code1), pArrayList1.get(k), pArrayList1.get(k));
			ArrayList<StockPO> po2 = stockDataService.getStockByCodeAndDate(Integer.valueOf(code2), pArrayList2.get(k), pArrayList2.get(k));
			if(Nowmessage1.size()>0&&po1.size()>0){
				parameter1.add(df.format((Nowmessage1.get(0).getClose()-po1.get(0).getClose())/po1.get(0).getClose()*100)+"%");
			}else{
				parameter1.add("---");
			}
			if(Nowmessage2.size()>0&&po2.size()>0){
				parameter2.add(df.format((Nowmessage2.get(0).getClose()-po2.get(0).getClose())/po2.get(0).getClose()*100)+"%");
			}else{
				parameter2.add("---");
			}
		}
		result.add("success");
		vsdata.put("result", result);
		vsdata.put("date", datelist);
		vsdata.put("fluct1", fluct1);
		vsdata.put("fluct2", fluct2);
		vsdata.put("name", namelist);
		vsdata.put("parameter1", parameter1);
		vsdata.put("parameter2", parameter2);
		return vsdata;
	}

	/**
	 * 获得版块基准收益率
	 * 
	 * @param code
	 * @return
	 */
	public Map<String, ArrayList<String>> getBenchmark(String code) {

		Map<String, ArrayList<String>> data = new HashMap<>();

		ArrayList<BasePO> benchmark = new ArrayList<>();
		BasePO base = new BasePO();
		benchmark = stockDataService.getBenchmark(code);

		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> adjClose = new ArrayList<>();
		// System.out.println("benchmark "+benchmark.size());
		for (int i = 0; i < benchmark.size(); i++) {
			base = benchmark.get(i);
			date.add(base.getDate());
			adjClose.add(String.valueOf(base.getAdjClose()));
		}
		// System.out.println(adjClose.get(0)+" "+adjClose.get(1));
		// System.out.println(date.get(0)+" "+date.get(1));
		data.put("date", date);
		data.put("adjClose", adjClose);
		return data;
	}
	
	/**
	 * 获得个股收益率
	 * 
	 * @param code
	 * @return
	 */
	public Map<String, ArrayList<String>> getStockGains(String code, String begin, String end) {

		Map<String, ArrayList<String>> data = new HashMap<>();

		ArrayList<StockPO> stocks = new ArrayList<>();
		StockPO stockPO = new StockPO();
		stocks = stockDataService.getStockByCodeAndDate(Integer.parseInt(code), begin, end);

		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> adjClose = new ArrayList<>();
		ArrayList<String> name = new ArrayList<>();
		// System.out.println("benchmark "+benchmark.size());
		for (int i=stocks.size()-1; i >=0; i--) {
			stockPO = stocks.get(i);
			date.add(stockPO.getDate());
			adjClose.add(String.valueOf(stockPO.getAdjClose()));
		}
		name.add(stockDataService.getNameByCode(Integer.parseInt(code)));
		// System.out.println(adjClose.get(0)+" "+adjClose.get(1));
		// System.out.println(date.get(0)+" "+date.get(1));
		data.put("date", date);
		data.put("adjClose", adjClose);
		data.put("name", name);
		return data;
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
		DecimalFormat df = new DecimalFormat("0.00");
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		
		ArrayList<StockPO> stockList = stockDataService.getStockByDate(Date);
		for (StockPO po : stockList) {
			Date yesterday = getDayBefore(Date, Integer.valueOf(po.getCode()));
			if(Date.equals(time.format(yesterday))){
				continue;
			}
			StockPO po_1 = stockDataService.getStockByCodeAndDate(Integer.valueOf(po.getCode()), time.format(yesterday), time.format(yesterday)).get(0);
					
			double upsAndDowns = (po.getAdjClose()-po_1.getAdjClose())/po_1.getAdjClose();
			
			if(upsAndDowns>0.15||upsAndDowns<-0.15){
				continue;
			}
			for (int i = 0; i < distribution.length; i++) {
				if (upsAndDowns >= changes[i] && upsAndDowns <= changes[i + 1]) {
					distribution[i]++;
					break;
				}
			}
			StockNameAndUpsPO namePO = new StockNameAndUpsPO(stockDataService.getNameByCode(Integer.valueOf(po.getCode())), upsAndDowns);
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
				} 
				else {
					ups_Max.add(namePO);
				}
			} 
			else {
				if (downs_Max.size() > 0) {
					for (int i = 0; i < downs_Max.size(); i++) {
						if (namePO.getUps() <= downs_Max.get(i).getUps()) {
							downs_Max.add(i, namePO);
							if (downs_Max.size() > 5)
								downs_Max.remove(5);
							break;
						}
					}
				} 
				else {
					downs_Max.add(namePO);
				}
			}
		}
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
		int i = 0;
		try {
			do {
				dayBefore = time.parse(yesterday);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dayBefore);
				cal.add(Calendar.DATE, -1);
				yesterday = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				dayBefore = time.parse(yesterday);
				volume = stockDataService.getVolumeByDateAndCode(code, yesterday);
				i++;
			} while (volume == 0.0||i==7);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayBefore;
	}

	/**
	 * 得到当天所有股票交易信息
	 */
	public ArrayList<StockPO> getStockMessage() {
		return stockDataService.getStockByDate("2017-05-25");
	}
	
	/**
	 * 得到当天所有创业板股票交易信息
	 */
	public ArrayList<StockPO> getGemStockMessage() {
		ArrayList<String> codelist = stockDataService.getGemAllCode();
		ArrayList<StockPO> result = new ArrayList<>();
		for(int i=0;i<codelist.size();i++){
			ArrayList<StockPO> message = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)), "2017-05-25", "2017-05-25");
			if(message.size()>0){
				result.add(message.get(0));
			}
		}
		return result;
	}
	
	/**
	 * 得到当天所有中小板股票交易信息
	 */
	public ArrayList<StockPO> getSmeStockMessage() {
		ArrayList<String> codelist = stockDataService.getSmeAllCode();
		ArrayList<StockPO> result = new ArrayList<>();
		for(int i=0;i<codelist.size();i++){
			ArrayList<StockPO> message = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)), "2017-05-25", "2017-05-25");
			if(message.size()>0){
				result.add(message.get(0));
			}
		}
		return result;
	}

	/**
	 * 根据代码查找名称
	 */
	public String getNameByCode(String code) {
		return stockDataService.getNameByCode(Integer.valueOf(code));
	}

	/**
	 * 得到股票的上一个交易日的交易信息
	 */
	public ArrayList<StockPO> getLastStockByCode(String code) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println("getLastStockByCode--------enter---------");
		String date = time.format(getDayBefore("2017-05-25", Integer.valueOf(code)));
		// System.out.println("getLastStockByCode--------end-------");
		return stockDataService.getStockByCodeAndDate(Integer.valueOf(code), date, date);
	}
	
	/**
	 * 判断输入的是否为股票代码
	 * @param code
	 * @return
	 */
	private boolean IsCode(String code){
		String C = code.replace(" ", "");
		try{
			int number = Integer.valueOf(code);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 将股票代码转化为六位
	 * @param code
	 * @return
	 */
	private String Tran(String code){
		String Code = "";
		switch (code.length()) {
		case 6:
			Code = code;
			break;
		case 5:
			Code = "0"+code;
			break;
		case 4:
			Code = "00"+code;
			break;
		case 3:
			Code = "000"+code;
			break;
		case 2:
			Code = "0000"+code;
			break;
		case 1:
			Code = "00000"+code;
			break;
		default:
			break;
		}
		return Code;
	}
	
	/**
	 * 判断是否存在该股票代码
	 * @param code
	 * @return
	 */
	private boolean IsLegalCode(String code){
		if(codelist==null){
			codelist = stockDataService.GetAllCode();
		}
		for(int i=0;i<codelist.size();i++){
			if(code.equals(codelist.get(i))){
				return true;
			}
		}
		return false;
	}

//	public static void main(String[] args) {
//		GetStockBL bl = new GetStockBL();
//		StockDataService stockDataService = new StockData();
//		bl.setStockDataService(stockDataService);
//		UpsAndDownsPO a = bl.getDistributionOfUpsAndDowns("2011-05-11");
//		for (int i = 0; i < 5; i++)
//			System.out.println(a.getDowns_Max().get(i).getName() + ' ' + a.getDowns_Max().get(i).getUps());
//
//	}
}
