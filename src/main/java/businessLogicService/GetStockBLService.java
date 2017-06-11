package businessLogicService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import po.StockPO;
import po.UpsAndDownsPO;

public interface GetStockBLService {

	public Map<String, ArrayList<String>> getVSdata(String code1, String code2, String begindate, String enddate);
	
	public Map<String, ArrayList<String>> getBenchmark(String code);
	
	public Map<String, ArrayList<String>> getStockGains(String code, String begin, String end);
	
	public UpsAndDownsPO getDistributionOfUpsAndDowns(String Date);
	
	public ArrayList<StockPO> getStockMessage();
	
	public ArrayList<StockPO> getGemStockMessage();
	
	public ArrayList<StockPO> getSmeStockMessage();
	
	public String getNameByCode(String code);
	
	public ArrayList<StockPO> getLastStockByCode(String code);
}
