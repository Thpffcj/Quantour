package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import dataService.DateProcessingService;
import dataService.StockDataService;
import po.StockPO;

public class StockData implements StockDataService {

	DateProcessingService dateProcessingService = new DateProcessing();
	
	private	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 根据版块获得开盘价
	 */
	public ArrayList<Double> getStockOpenBySection(String section, String begin, String end){
		ArrayList<Double> open = new ArrayList<Double>();

		String code = "";
		if(section.equals("主板")){
			code = "000300";
		}else if(section.equals("创业板")){
			code = "399005";
		}else if(section.equals("中小板")){
			code = "399006";
		}
		try {
			Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ code + ".csv"),"UTF-8");
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
			Date BeginDate = time.parse(begin);
			Date EndDate = time.parse(end);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				Date now = time.parse(message[0]);
				if ((now.after(BeginDate) && now.before(EndDate)) || now.equals(BeginDate) || now.equals(EndDate)) {
					open.add(Double.parseDouble(message[2]));
				}
			}
			sc.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return open;
	}
	
	/**
	 * 根据版块获得复权收盘价
	 */
	public ArrayList<Double> getStockAdjCloseBySection(String section, String begin, String end){
		ArrayList<Double> adjClose = new ArrayList<Double>();

		String code = "";
		if(section == "主板"){
			code = "000300";
		}else if(section == "创业板"){
			code = "399005";
		}else if(section == "中小板"){
			code = "399006";
		}
		try {
			Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ code + ".csv"),"UTF-8");
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
			Date BeginDate = time.parse(begin);
			Date EndDate = time.parse(end);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				Date now = time.parse(message[0]);
				if ((now.after(BeginDate) && now.before(EndDate)) || now.equals(BeginDate) || now.equals(EndDate)) {
					adjClose.add(Double.parseDouble(message[1]));
				}
			}
			sc.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return adjClose;
	}
	
	/**
	 * 根据开始日期、结束日期和股票编号得到股票列表
	 */
	public ArrayList<StockPO> getStockByCodeAndDate(String code, String begin, String end) {
		
		Session session = sessionFactory.openSession();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();

		ArrayList<String> days;
		try {
			days = dateProcessingService.splitDays(begin, end);
			System.out.println(code + " " + begin + " " + end + " " + days.size());
			for(int i=0; i<days.size(); i++){
				String hql = "from StockPO where code=? and date=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, code);
				query.setParameter(1, days.get(i));
				StockPO spo = new StockPO();
				spo = (StockPO) query.uniqueResult();
				if(spo != null){
					stockList.add(spo);
				}
				session.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

//		if(begin != null){
//			try {
//				Scanner sc = new Scanner(
//						getClass().getClassLoader().getResourceAsStream("CSV/" + Tran(Integer.toString(code)) + ".csv"),
//						"UTF-8");
//
//				SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
//				SimpleDateFormat time1 = new SimpleDateFormat("MM/dd/yy");
//				Date BeginDate = time.parse(begin);
//				Date EndDate = time.parse(end);
//				while (sc.hasNextLine()) {
//					String line = sc.nextLine();
//					String[] message = line.split("\t");
//					Date now = time1.parse(message[1]);
//					if ((now.after(BeginDate) && now.before(EndDate)) || now.equals(BeginDate) || now.equals(EndDate)) {
//						stockList.add(new StockPO(message));
//					}
//					if (now.after(EndDate)) {
//						break;
//					}
//				}
//				sc.close();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		} else {
//			Scanner sc = new Scanner(
//					getClass().getClassLoader().getResourceAsStream("CSV/" + Tran(Integer.toString(code)) + ".csv"),
//					"UTF-8");
//			while (sc.hasNextLine()) {
//				String line = sc.nextLine();
//				String[] message = line.split("\t");
//				stockList.add(new StockPO(message));
//			}
//			sc.close();
//		}
//
		Collections.reverse(stockList);
		return stockList;
	}

	/**
	 * 根据开始日期、结束日期和股票名称得到股票列表
	 */
	public ArrayList<StockPO> getStockByNameAndDate(String name, String begin, String end) {
		
		Session session = sessionFactory.openSession();
		String code = "000001";
		
		String hql = "from NamelistPO where name=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, name);
		code = (String) query.uniqueResult();
		return getStockByCodeAndDate(code, begin, end);
//		String name = Name.replace(" ", "");
//		int code = 0;
//		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
//		while (sc.hasNextLine()) {
//			String line = sc.nextLine();
//			String[] message = line.split("\t");
//			String s = message[1].replaceAll(" ", "");
//			if (name.equals(s)) {
//				code = Integer.valueOf(message[0]);
//				sc.close();
//				break;
//			}
//		}
//
//		return getStockByCodeAndDate(code, Begin, End);
	}

	/**
	 * 根据日期得到股票列表
	 */
	public ArrayList<StockPO> getStockByDate(String Begin) {
		
		ArrayList<String> stockcode = new ArrayList<>();
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
		while (sc.hasNextLine()) {
			stockcode.add(sc.nextLine().split("\t")[0]);
		}
		sc.close();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		Date BeginDate;
		try {
			BeginDate = time.parse(Begin);
			for (int i = 0; i < stockcode.size(); i++) {
				Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ Tran(stockcode.get(i)) + ".csv"),"UTF-8");
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] message = line.split("\t");
					Date today = time.parse(message[1]);
					if (today.equals(BeginDate)) {
						stockList.add(new StockPO(message));
						break;
					}
				}
				scanner.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return stockList;
	}

	// 根据日期和股票号得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；
	public int getVolumeByDateAndCode(int Code,String Begin) {
		try {
			Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+Tran(String.valueOf(Code))+".csv"),"UTF-8");
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
			Date BeginDate = time.parse(Begin);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				Date today = time.parse(message[1]);
				if (today.equals(BeginDate)) {
					return Integer.valueOf(message[6]);
				}
			}
			sc.close();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	// 根据日期和股票名称得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；
	public int getVolumeByDateAndName(String Name,String Begin){
		String name = Name.replace(" ", "");
		int code = 0;
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] message = line.split("\t");
			String s = message[1].replaceAll(" ", "");
			if (name.equals(s)) {
				code = Integer.valueOf(message[0]);
				sc.close();
				break;
			}
		}

		return getVolumeByDateAndCode(code,Begin);
	}
	
	
	/**
	 * 根据name得到code
	 */
	public int getCodeByName(String name){
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] message = line.split("\t");
			if(name.equals(message[1])){
				sc.close();
				return Integer.parseInt(message[0]);
			}
		}
		sc.close();
		return -1;
	}
	
	// 根据日期和股票号得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；是股票最开始一天则返回-1；(均线图专用方法！！)
	public int JudgeIfTheLast(String Code, String Begin) {
		int volume=0;
		try {
			Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ Tran(String.valueOf(Code)) + ".csv"),"UTF-8");
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
			Date BeginDate = time.parse(Begin);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				Date today = time.parse(message[1]);
				if (today.equals(BeginDate)) {
					volume=Integer.valueOf(message[6]);
					break;
				}else if(today.after(BeginDate)){
					break;
				}
			}
			if(!(sc.hasNextLine())){
				volume=-1;
			}
			sc.close();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return volume;
	}
	
	/**
	 * 得到所有股票编号和名称
	 */
	public ArrayList<StockPO> getCodeAndName(){
		ArrayList<StockPO> list = new ArrayList<>();
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
		while (sc.hasNextLine()) {
			list.add(new StockPO(sc.nextLine()));
		}
		sc.close();
		return list;
	}
	
	/**
	 * 根据板块得到股票列表
	 * @param plate
	 * @return
	 */
	public ArrayList<StockPO> getCodeAndNameByPlate(String plate){
		ArrayList<StockPO> list = new ArrayList<>();
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/namelist.csv"),"UTF-8");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] message = line.split("\t");
			if(plate.equals(message[2])){
				list.add(new StockPO(message[0],message[1],message[2]));
			}
		}
		sc.close();
		
		return list;
	}

	public ArrayList<String> getDate(String begin, String end) {
		ArrayList<String> days = new ArrayList<String>();

		String code = "000300";
		try {
			Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ code + ".csv"),"UTF-8");
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
			Date BeginDate = time.parse(begin);
			Date EndDate = time.parse(end);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				Date now = time.parse(message[0]);
				if ((now.after(BeginDate) && now.before(EndDate)) || now.equals(BeginDate) || now.equals(EndDate)) {
					days.add(message[0]);
				}
			}
			sc.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}
	/**
	 * 获得某股票一天的数据，若不是交易日则返回null
	 * @param Name
	 * @param day
	 * @return
	 */
	public StockPO getStockByOneDay(String Name,Date day){
		int code = getCodeByName(Name);
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ Tran(String.valueOf(code)) + ".csv"),"UTF-8");
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		Date last = day;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] message = line.split("\t");
			Date now = null;
			try {
				now = time.parse(message[1]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(now.equals(day)){
				sc.close();
				return new StockPO(message);
			}
			else if((day.before(last)&&day.after(now))){
				sc.close();
				return null;
			}
			last = now;
		}
		return null;
	}
	
	/**
	 * 找到最合适的交易日
	 * @param Name
	 * @param day
	 * @return
	 */
	public Date getNearestDay(String Name,Date day){
		int code = getCodeByName(Name);
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("CSV/"+ Tran(String.valueOf(code)) + ".csv"),"UTF-8");
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		Date last = day;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] message = line.split("\t");
			Date now = null;
			try {
				now = time.parse(message[1]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(now.equals(day)||(day.before(last)&&day.after(now))){
				sc.close();
				return now;
			}
			last = now;
		}
		return last;
		
	}
	
	public String Tran(String code){
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
	
}