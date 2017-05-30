package dataService;

import java.util.ArrayList;
import java.util.Date;

import po.StockPO;

public interface StockDataService {

	public ArrayList<StockPO> getStockByCodeAndDate(String Code,String Begin,String End);
	
	public ArrayList<StockPO> getStockByNameAndDate(String Name,String Begin,String End);
	
	public ArrayList<StockPO> getStockByDate(String Begin);
	
	public int getVolumeByDateAndCode(int Code,String Begin);
	
	public int getVolumeByDateAndName(String Name,String Begin);
	
	public int JudgeIfTheLast(String code, String Begin);
	
	public ArrayList<StockPO> getCodeAndName();
	
	public ArrayList<StockPO> getCodeAndNameByPlate(String plate);
	
	public ArrayList<Double> getStockOpenBySection(String section, String begin, String end);
	
	public ArrayList<Double> getStockAdjCloseBySection(String section, String begin, String end);
	
	public ArrayList<String> getDate(String begin, String end);
	
	public int getCodeByName(String name);
	
	public StockPO getStockByOneDay(String Name,Date day);

}
