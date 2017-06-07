package dataService;

import java.util.ArrayList;
import java.util.Date;

import po.BasePO;
import po.StockPO;

public interface StockDataService {

	public ArrayList<StockPO> getStockByCodeAndDate(int code,String begin,String end);
	
	public ArrayList<StockPO> getStockByNameAndDate(String Name,String Begin,String End);
	
	public ArrayList<StockPO> getStockByDate(String Begin);
	
	public ArrayList<BasePO> getBenchmark(String code);
	
	public Double getVolumeByDateAndCode(int Code,String Begin);
	
	public Double getVolumeByDateAndName(String Name,String Begin);
	
	public int JudgeIfTheLast(int code, String Begin);
	
	public ArrayList<StockPO> getCodeAndName();
	
	public ArrayList<String> getPlate(String plate_type);
	
	public ArrayList<String> getCodeByPlate(String plate_type,String plate);
	
	public ArrayList<Double> getStockOpenBySection(String section, String begin, String end);
	
	public ArrayList<Double> getStockAdjCloseBySection(String section, String begin, String end);
	
	public ArrayList<String> getDate(String begin, String end);
	
	public ArrayList<String> GetAllCode();
	
	public ArrayList<String> GetAllName();
	
	public int getCodeByName(String name);
	
	public String getNameByCode(int code);
	
//	public StockPO getStockByOneDay(String Name,Date day);

}
