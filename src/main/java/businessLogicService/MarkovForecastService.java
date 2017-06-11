package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

public interface MarkovForecastService {
	
	//预测未来股票的复权收盘价所属的价格区间
	public Map<String, ArrayList<Double>> CloseValueForecast(String Name,String Begin,String End,int ForecastDays);
	
	//预测未来股票复权收盘价的上升或下降趋势
	public ArrayList<String> UpAndDownForecast(String Name,String Begin,String End,int ForecastDays);
}
