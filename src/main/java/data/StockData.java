package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.enterprise.inject.spi.Bean;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dataService.DateProcessingService;
import dataService.StockDataService;
import po.AreaClassifiedPO;
import po.BasePO;
import po.ConceptClassifiedPO;
import po.IndustryClassifiedPO;
import po.NamelistPO;
import po.StockPO;

public class StockData implements StockDataService {

	DateProcessingService dateProcessingService = new DateProcessing();
	
	private	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
	
	/**
	 * 根据开始日期、结束日期和股票编号得到股票列表
	 */
	public ArrayList<StockPO> getStockByCodeAndDate(int code, String begin, String end) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();

//		transaction = session.beginTransaction();
//		String hql = "from StockPO where code=? and date>=? and date<=?";
//		Query query = session.createQuery(hql);
//		query.setParameter(0, Tran(String.valueOf(code)));
//		query.setParameter(1, begin);
//		query.setParameter(2, end);
//		stockList = (ArrayList<StockPO>) query.getResultList();
//		transaction.commit();
//		session.close();
		ArrayList<String> days;
		try {
			transaction = session.beginTransaction();
			days = dateProcessingService.splitDays(begin, end);
			for(int i=0; i<days.size(); i++){
				String hql = "from StockPO where code=? and date=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, Tran(String.valueOf(code)));
				query.setParameter(1, days.get(i));
				StockPO spo = new StockPO();
				try {
					spo = (StockPO) query.getSingleResult();
					stockList.add(spo);
					transaction.commit();
				} catch (Exception e) {
					continue;
				}
			}
			session.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Collections.reverse(stockList);
		return stockList;
	}

	/**
	 * 根据版块编号获得版块数据
	 * @param code
	 * @return
	 */
	public ArrayList<BasePO> getBenchmark(String code){
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		ArrayList<BasePO> benchmark = new ArrayList<>();
		
		transaction = session.beginTransaction();
		String hql = "from BasePO where code=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, code);
		benchmark = (ArrayList<BasePO>) query.getResultList();
//		System.out.println(stockList.size());
		transaction.commit();
		session.close();
		return benchmark;
	}
	
	public ArrayList<BasePO> getBenchmarkByDate(String name,String begin,String end){
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		ArrayList<BasePO> stockList = new ArrayList<BasePO>();

		String code = "hs300";
		if(name.equals("主板")){
			code = "hs300";
		}else if(name.equals("创业板")){
			code = "399005";
		}else if(name.equals("中小板")){
			code = "399006";
		}
		ArrayList<String> days;
		try {
			transaction = session.beginTransaction();
			days = dateProcessingService.splitDays(begin, end);
			for(int i=0; i<days.size(); i++){
				String hql = "from BasePO where code=? and date=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, code);
				query.setParameter(1, days.get(i));
				BasePO spo = new BasePO();
				try {
					spo = (BasePO) query.getSingleResult();
					stockList.add(spo);
					transaction.commit();
				} catch (Exception e) {
					continue;
				}
			}
			session.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return stockList;
	}
	
	/**
	 * 根据开始日期、结束日期和股票名称得到股票列表
	 */
	public ArrayList<StockPO> getStockByNameAndDate(String name, String begin, String end) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		String code = "000001";
		
		transaction = session.beginTransaction();
		String hql = "select code from NamelistPO where name=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, name);
		try {
			code = (String) query.getSingleResult();
			transaction.commit();
			session.close();
			return getStockByCodeAndDate(Integer.parseInt(code), begin, end);
		} catch (Exception e) {
			session.close();
			return null;
		}
	}
	
	/**
	 * 得到所有股票代码
	 * @return
	 */
	public ArrayList<String> GetAllCode(){
		Session session = sessionFactory.openSession();
		
		ArrayList<String> namelist = new ArrayList<>();
		String hql = "select code from NamelistPO";
		Query query = session.createQuery(hql);
		namelist = (ArrayList<String>) query.getResultList();
		return namelist;
	}
	
	/**
	 * 得到所有股票名称
	 * @return
	 */
	public ArrayList<String> GetAllName(){
		Session session = sessionFactory.openSession();
		
		ArrayList<String> namelist = new ArrayList<>();
		String hql = "select name from NamelistPO";
		Query query = session.createQuery(hql);
		namelist = (ArrayList<String>) query.getResultList();
		return namelist;
	}
	
	/**
	 * 根据股票编号获得名称
	 * @param code
	 * @return
	 */
	public String getNameByCode(int code){
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		String name = null;
		transaction = session.beginTransaction();
		String hql = "select name from NamelistPO where code=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, Tran(String.valueOf(code)));
		if(String.valueOf(code).equals("603580")){
			System.out.println("----------------");
		}
		try {
			name = (String) query.getSingleResult();
			transaction.commit();
			session.close();
			return name;
		} catch (Exception e) {
			session.close();
			return null;
		}
	}
	
	/**
	 * 根据name得到code
	 */
	public int getCodeByName(String name){
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		String code = null;
		transaction = session.beginTransaction();
		String hql = "select code from NamelistPO where name=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, name);
		try {
			code = (String) query.getSingleResult();
			transaction.commit();
			session.close();
			return Integer.parseInt(code);
		} catch (Exception e) {
			session.close();
			return 0;
		}
	}

	/**
	 * 根据日期得到股票列表
	 */
	public ArrayList<StockPO> getStockByDate(String begin) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		transaction = session.beginTransaction();
		String hql = "from StockPO where date=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, begin);
		stockList = (ArrayList<StockPO>) query.getResultList();
//		System.out.println(stockList.size());
		transaction.commit();
		session.close();
		return stockList;
	}
	
	/**
	 * 获得某股票一天的数据，若不是交易日则返回null
	 * @param Name
	 * @param day
	 * @return
	 */
	public StockPO getStockByOneDay(String name, Date day){
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		StockPO stockPO = new StockPO();
		int code = getCodeByName(name);
		String date = time.format(day);

		transaction = session.beginTransaction();
		String hql = "from StockPO where code=? and date=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, Tran(String.valueOf(code)));
		query.setParameter(1, date);
		try {
			stockPO = (StockPO) query.getSingleResult();
			transaction.commit();
			session.close();
			return stockPO;
		} catch (Exception e) {
			session.close();
			return null;
		}
	}

	// 根据日期和股票号得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；
	public Double getVolumeByDateAndCode(int code,String begin) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		StockPO po;
		transaction = session.beginTransaction();
		String hql = "from StockPO where code=? and date=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, Tran(String.valueOf(code)));
		query.setParameter(1, begin);
		try {
			po = (StockPO) query.getSingleResult();
			transaction.commit();
			session.close();
			return Double.parseDouble(po.getVolume());
		} catch (Exception e) {
			session.close();
			return 0.0;
		}
	}
	
	// 根据日期和股票名称得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；
	public Double getVolumeByDateAndName(String Name,String Begin){

		return getVolumeByDateAndCode(getCodeByName(Name),Begin);
	}
	
	// 根据日期和股票号得到当日交易量，若该日期不是工作日，返回0；否则返回股票1号的交易量，即非0；是股票最开始一天则返回-1；(均线图专用方法！！)
	public int JudgeIfTheLast(int code, String begin) {
		double volume = getVolumeByDateAndCode(code, begin);
		if(volume == 0.0){
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * 得到所有股票编号和名称
	 */
	public ArrayList<StockPO> getCodeAndName(){
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		ArrayList<StockPO> list = new ArrayList<>();
		transaction = session.beginTransaction();
		String hql = "from StockPO";
		Query query = session.createQuery(hql);
		list = (ArrayList<StockPO>)query.getResultList();
		transaction.commit();
		session.close();
		return list;
	}
	
	public ArrayList<String> getPlate(String plate_type){
		
		ArrayList<String> list = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		String hql = "";
		transaction = session.beginTransaction();
		if(plate_type.equals("area")){
			hql = "select distinct area from AreaClassifiedPO";
		}else if(plate_type.equals("concept")){
			hql = "select distinct cName from ConceptClassifiedPO";
		}else if(plate_type.equals("industry")){
			hql = "select distinct cName from IndustryClassifiedPO";
		}
		Query query = session.createQuery(hql);
		list = (ArrayList<String>)query.getResultList();
		transaction.commit();
		session.close();
		return list;
	}
	
	/**
	 * 根据板块得到股票列表
	 * @param plate
	 * @return
	 */
	public ArrayList<String> getCodeByPlate(String plate_type,String plate){
		
		ArrayList<String> list = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		String hql = "";
		transaction = session.beginTransaction();
		if(plate_type.equals("area")){
			hql = "select po.code from AreaClassifiedPO po where po.area=?";
			Query query = session.createQuery(hql);
			query.setParameter(0,plate);
			list = (ArrayList<String>) query.getResultList();
		}else if(plate_type.equals("concept")){
			hql = "select po.code from ConceptClassifiedPO po where po.cName=?";
			Query query = session.createQuery(hql);
			query.setParameter(0,plate);
			list = (ArrayList<String>) query.getResultList();
		}else if(plate_type.equals("industry")){
			hql = "select po.code from IndustryClassifiedPO po where po.cName=?";
			Query query = session.createQuery(hql);
			query.setParameter(0,plate);
			list = (ArrayList<String>) query.getResultList();
		}
		Query query = session.createQuery(hql);
		query.setParameter(0,plate);
		transaction.commit();
		session.close();
		return list;
	}

	public ArrayList<String> getDate(String begin, String end) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		ArrayList<String> days = new ArrayList<String>();

		String code = "399006";
		
			transaction = session.beginTransaction();
			String hql = "select date from BasePO where code=? and date>=? and date<=?";
			Query query = session.createQuery(hql);
			query.setParameter(0, code);
			query.setParameter(1, begin);
			query.setParameter(2, end);
			days = (ArrayList<String>) query.getResultList();
			transaction.commit();
			session.close();
//			days = dateProcessingService.splitDays(begin, end);
		

		return days;
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
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
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
	
	/**
	 * 得到所有创业板股票代码
	 * @return
	 */
	public ArrayList<String> getSmeAllCode(){
		ArrayList<String> codelist = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		transaction = session.beginTransaction();
		String hql = "select code from SmeClassifiedPO";
		Query query = session.createQuery(hql);
		codelist = (ArrayList<String>) query.getResultList();
		transaction.commit();
		session.close();
		return codelist;
	}
	
	/**
	 * 得到所有中小板股票代码
	 * @return
	 */
	public ArrayList<String> getGemAllCode(){
		ArrayList<String> codelist = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		transaction = session.beginTransaction();
		String hql = "select code from GemClassifiedPO";
		Query query = session.createQuery(hql);
		codelist = (ArrayList<String>) query.getResultList();
		transaction.commit();
		session.close();
		return codelist;
	}
	
	/**
	 * 得到所有主板股票代码
	 * @return
	 */
	public ArrayList<String> getMainAllCode(){
		ArrayList<String> codelist = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		transaction = session.beginTransaction();
		String hql = "select code from MainClassifiedPO";
		Query query = session.createQuery(hql);
		codelist = (ArrayList<String>) query.getResultList();
		transaction.commit();
		session.close();
		return codelist;
	}
	
}