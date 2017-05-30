package businessLogic;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import businessLogicService.MarketChangesService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;
import vo.MarketQuotationVO;

public class MarketChanges implements MarketChangesService{

	private String Day;
	private ArrayList<StockPO> todayList;
	private ArrayList<StockPO> yesterdayList;
	
	public MarketChanges(String Date){
		this.Day = Date;
		String dayBefore = getDayBefore(Day);
		StockDataService service = new StockData();
		this.todayList = service.getStockByDate(Day);
		this.yesterdayList = service.getStockByDate(dayBefore);
	}
	/**
	 * 获得当日总交易量
	 */
	public MarketQuotationVO getMarketChanges(){
		long totalVolumn = getTotalVolumn();
		int[] IncreaseAndDecrease = getTotalOfIncreaseAndDecrease();
		int[] Num = getNum(); 
		
		MarketQuotationVO vo = new MarketQuotationVO(Day,totalVolumn,
				IncreaseAndDecrease[0],IncreaseAndDecrease[1],
				IncreaseAndDecrease[2],IncreaseAndDecrease[3],
				IncreaseAndDecrease[4],IncreaseAndDecrease[5],Num[0],Num[1]);
		
		return vo;
	}
	/**
	 * 获得前一交易日
	 * @param today
	 * @return
	 */
	private static String getDayBefore(String today){
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		String yesterday = today;
		int volume;
		try {
			do{
				Date origin = time.parse(yesterday);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				yesterday = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				volume = sds.getVolumeByDateAndCode(1,yesterday);
			}while(volume==0);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return yesterday;
	}
	
	private long getTotalVolumn(){
		long totalVolumn = 0;
		for(StockPO po:todayList){
			totalVolumn += po.getVolume();
		}
		
		return totalVolumn;
	}
	/**
	 * 返回当日股票的涨跌幅情况
	 * @return
	 */
	private int[] getTotalOfIncreaseAndDecrease(){
		int[] numOfIncreaseAndDecrease = {0,0,0,0,0,0};
		//存放股票涨跌幅数量的数组
		//位置0为涨停股票数
		//1为跌停股票数
		//2为涨幅超过5%的股票数
		//3为跌幅超过5%的股票数
		//4为其它股票
		//5为总股票数
		for(StockPO po:todayList){
			if(po.getVolume()==0)
				continue;
			numOfIncreaseAndDecrease[5]++;
			String name = po.getName();
			DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
			// 设置几位小数
			df.setMaximumFractionDigits(6);
			// 设置舍入模式
			df.setRoundingMode(RoundingMode.HALF_UP);
			
			for(StockPO po1:yesterdayList){
				if(po1.getName().equals(name)){
					double changes = (po.getAdjClose()-po1.getAdjClose())/po1.getAdjClose();
					changes = Double.valueOf(df.format(changes));
					if(changes>=0.050000){
						numOfIncreaseAndDecrease[2]++;
						if(changes>=0.100000)
							numOfIncreaseAndDecrease[0]++;
					}
					else if(changes<=-0.050000){
						numOfIncreaseAndDecrease[3]++;
						if(changes<=-0.1)
							numOfIncreaseAndDecrease[1]++;
					}
					else
						numOfIncreaseAndDecrease[4]++;
					break;
				}
			}
		}
		
		return numOfIncreaseAndDecrease;
	}
	/**
	 * 
	 * @return
	 */
	private int[] getNum(){
		int[] num = {0,0};
		//数组位置0为开盘-收盘大于5%*上一个交易日收盘价的股票个数，
		//位置1为开盘‐收盘小于-5%*上一个交易日收盘价的股票个数
		for(StockPO po:todayList){
			String name = po.getName();
			for(StockPO po1:yesterdayList){
				if(name.equals(po1.getName())){
					double changes = (po.getOpen()-po.getClose());
					
					if(changes>0.05*po1.getClose())
						num[0]++;
					else if(changes<-0.05*po1.getClose())
						num[1]++;
					
					break;
				}
			}
		}
		
		return num;
	}

}
