package businessLogicService;

import java.util.ArrayList;

public interface MarkovForecastService {
	
	//预测未来股票的复权收盘价所属的价格区间
	public ArrayList<String> CloseValueForecast(String Name,String Begin,String End,int ForecastDays);
	
	//预测未来股票复权收盘价的上升或下降趋势
	public ArrayList<String> UpAndDownForecast(String Name,String Begin,String End,int ForecastDays);
}
