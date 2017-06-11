package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
		
		Map<String, ArrayList<Double>> closeValue = new HashMap<>();
		ArrayList<Double> up = new ArrayList<>();
		ArrayList<Double> down = new ArrayList<>();
		
		String code = request.getParameter("code");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		System.out.println("code "+code+"begin "+begin);
		int fday = 10;
		if(request.getParameter("fday") != null){
			fday = Integer.parseInt(request.getParameter("fday"));
		}
		
		if(request.getParameter("fday").equals("") || request.getParameter("fday") == null || code.equals("") || code == null){
			closeValue = markovForecastService.CloseValueForecast("2", "2016-05-25", "2017-05-25", 20);
		}else{
			closeValue = markovForecastService.CloseValueForecast(code, begin, end, fday);
		}
		up = closeValue.get("up");
		down = closeValue.get("down");
		
		int days = up.size();
		Double[] ups = new Double[days];
		Double[] downs = new Double[days];
		Integer[] date = new Integer[days];
		for(int i=0; i<days; i++){
			ups[i] = up.get(i);
			downs[i] = down.get(i);
			date[i] = i+1;
		}
		
		JSONObject json = new JSONObject();
		json.put("Date", date);
		json.put("Ups", ups);
		json.put("Downs", downs);
		result = json.toString();
		return SUCCESS;
	}
	
	public String upAndDownForecast(){
		ArrayList<String> upAndDown = new ArrayList<>();
		upAndDown = markovForecastService.UpAndDownForecast("", "", "", 0);
		return SUCCESS;
	}
}
