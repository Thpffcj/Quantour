package businessLogicService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import po.StockPO;
import po.UpsAndDownsPO;

public interface GetStockBLService {

	public ArrayList<String> getDate(String stock1, String stock2, String beginDate, String endDate);
	
	public Map<String, ArrayList<Double>> getVSData(String stock1, String stock2, String beginDate, String endDate);
	
	public Map<String, ArrayList<String>> getBenchmark(String code);
	
	public UpsAndDownsPO getDistributionOfUpsAndDowns(String Date);
	
	public ArrayList<StockPO> getStockMessage();
	
	public String getNameByCode(String code);
	
	public ArrayList<StockPO> getLastStockByCode(String code);
}
