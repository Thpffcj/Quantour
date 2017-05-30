package businessLogicService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import po.UpsAndDownsPO;

public interface GetStockBLService {

	public ArrayList<String> getDate(String stock1, String stock2, String beginDate, String endDate);
	
	public Map<String, ArrayList<Double>> getVSData(String stock1, String stock2, String beginDate, String endDate);
	
	public UpsAndDownsPO getDistributionOfUpsAndDowns(String Date);
}
