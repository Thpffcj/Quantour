package businessLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import businessLogicService.GetKGraphDataService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;
import vo.stockVO;

public class GetKGraphData implements GetKGraphDataService{
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	private static ArrayList<String> codelist = null;
	
	private static ArrayList<String> namelist = null;
	
	public void getKData(){
		
	}
	
	public Map<String, ArrayList<String>> getKData(String condition, String beginDate, String endDate) {
		
		Map<String, ArrayList<String>> KData = new HashMap<>();
		ArrayList<String> KDate = new ArrayList<>();
		ArrayList<String> KOpen = new ArrayList<>();
		ArrayList<String> KClose = new ArrayList<>();
		ArrayList<String> KLowest = new ArrayList<>();
		ArrayList<String> KHighest = new ArrayList<>();
		ArrayList<String> KVolumn = new ArrayList<>();
		ArrayList<StockPO> stock = new ArrayList<>();
		stock = getStockData(condition, beginDate, endDate);
		
		System.out.println(stock.size());
		for(int i=stock.size()-1; i>=0; i--){
			StockPO spo = stock.get(i);
			KDate.add(spo.getDate());
			KOpen.add(String.valueOf(spo.getOpen()));
			KClose.add(String.valueOf(spo.getClose()));
			KLowest.add(String.valueOf(spo.getLow()));
			KHighest.add(String.valueOf(spo.getHigh()));
			KVolumn.add(String.valueOf(spo.getVolume()));
		}
		KData.put("KDate", KDate);
		KData.put("KOpen", KOpen);
		KData.put("KClose", KClose);
		KData.put("KLowest", KLowest);
		KData.put("KHighest", KHighest);
		KData.put("KVolumn", KVolumn);
		
		return KData;
	}
	
	/**
	 * 得到所有股票编号和名称
	 */
	public ArrayList<stockVO> getCodeAndName(){
		StockDataService service = new StockData();
		ArrayList<StockPO> list = service.getCodeAndName();
		ArrayList<stockVO> result = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			result.add(new stockVO(list.get(i)));
		}
		
		return result;
	}
	
	/**
	 * 根据股票板块得到股票列表
	 * @param plate
	 * @return
	 */
	public ArrayList<stockVO> getCodeAndNameByPlate(String plate_type,String plate){
		StockDataService service = new StockData();
		ArrayList<String> list = service.getCodeByPlate(plate_type,plate);
		ArrayList<stockVO> result = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			result.add(new stockVO(new StockPO(list.get(i))));
		}
		return result;
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
			stockList = stockDataService.getStockByCodeAndDate(code, begin, end);
		} else {
			String name = condition;
			stockList = stockDataService.getStockByNameAndDate(name, begin, end);
		}
		return stockList;
	}
	
	public String getNameByCode(String code){
		if(code==null||code.equals("")){
			return "平安银行";
		}
		String name = stockDataService.getNameByCode(Integer.valueOf(code));
		return name;
	}
	
	public String getCodeByName(String name){
		if(name==null||name.equals("")){
			return "000001";
		}
		int code = stockDataService.getCodeByName(name);
		return Integer.toString(code);
	}
	
	/**
	 * 判断是否存在该股票
	 * @param code
	 * @return
	 */
	public boolean IsLegalCode(String code){
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
	
	public Map<String, String[]> getMatchList(String enter){
		String code = enter.replace(" ", "");
		boolean IsCode = IsCode(code);
		if(codelist==null){
			codelist = stockDataService.GetAllCode();
		}
		if(namelist==null){
			namelist = stockDataService.GetAllName();
		}
		
		int length = code.length();
		String[] c = new String[5];
		String[] name = new String[5];
		int k = 0;
		if(length==0){
			
		}else if(IsCode){
			for(int i=0;i<codelist.size();i++){
				if(code.equals(codelist.get(i).substring(0,length))){
					c[k] = codelist.get(i);
					name[k] = namelist.get(i);
					k++;
					if(k==5){
						break;
					}
				}
			}
		}else{
			for(int i=0;i<codelist.size();i++){
				if(code.equals(namelist.get(i).substring(0,length))){
					c[k] = codelist.get(i);
					name[k] = namelist.get(i);
					k++;
					if(k==5){
						break;
					}
				}
			}
		}
		
		Map<String, String[]> matchlist = new HashMap<>();
		matchlist.put("code", c);
		matchlist.put("name", name);
		return matchlist;
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
}
