package action;

import java.util.ArrayList;

import businessLogicService.MarkovForecastService;

public class ForecastAction extends SuperAction{

	private static final long serialVersionUID = 1L;
	
	private MarkovForecastService markovForecastService;
	public void setMarkovForecastService(MarkovForecastService markovForecastService) {
		this.markovForecastService = markovForecastService;
	}

	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String closeValueForecast(){
		ArrayList<String> closeValue = new ArrayList<>();
		closeValue = markovForecastService.CloseValueForecast("", "", "", 0);
		return SUCCESS;
	}
	
	public String upAndDownForecast(){
		ArrayList<String> upAndDown = new ArrayList<>();
		upAndDown = markovForecastService.UpAndDownForecast("", "", "", 0);
		return SUCCESS;
	}
}
