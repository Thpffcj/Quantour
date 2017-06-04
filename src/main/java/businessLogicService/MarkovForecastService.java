package businessLogicService;

import java.util.ArrayList;

public interface MarkovForecastService {
	public ArrayList<String> CloseValueForecast(String Name,String Begin,String End,int ForecastDays);
	
	public ArrayList<String> UpAndDownForecast(String Name,String Begin,String End,int ForecastDays);
}
